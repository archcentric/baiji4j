package com.ctriposs.baiji.rpc.client;

import com.ctriposs.baiji.rpc.client.filter.HttpRequestFilter;
import com.ctriposs.baiji.rpc.client.filter.HttpResponseFilter;
import com.ctriposs.baiji.rpc.client.registry.EtcdRegistryClient;
import com.ctriposs.baiji.rpc.client.registry.InstanceInfo;
import com.ctriposs.baiji.rpc.client.registry.RegistryClient;
import com.ctriposs.baiji.rpc.client.registry.ServiceInfo;
import com.ctriposs.baiji.rpc.client.stats.InvocationStats;
import com.ctriposs.baiji.rpc.client.stats.InvocationStatsStore;
import com.ctriposs.baiji.rpc.client.stats.StatsReportJob;
import com.ctriposs.baiji.rpc.common.HasResponseStatus;
import com.ctriposs.baiji.rpc.common.ServiceCommons;
import com.ctriposs.baiji.rpc.common.formatter.BinaryContentFormatter;
import com.ctriposs.baiji.rpc.common.formatter.ContentFormatter;
import com.ctriposs.baiji.rpc.common.formatter.JsonContentFormatter;
import com.ctriposs.baiji.rpc.common.logging.Logger;
import com.ctriposs.baiji.rpc.common.logging.LoggerFactory;
import com.ctriposs.baiji.rpc.common.tracing.Span;
import com.ctriposs.baiji.rpc.common.tracing.SpanType;
import com.ctriposs.baiji.rpc.common.tracing.Tracer;
import com.ctriposs.baiji.rpc.common.tracing.TracerFactory;
import com.ctriposs.baiji.rpc.common.types.AckCodeType;
import com.ctriposs.baiji.rpc.common.types.ErrorDataType;
import com.ctriposs.baiji.rpc.common.types.ResponseStatusType;
import com.ctriposs.baiji.rpc.common.util.DaemonThreadFactory;
import com.ctriposs.baiji.specific.SpecificRecord;
import com.ctriposs.baiji.util.VersionUtils;
import com.google.common.base.Joiner;
import com.google.common.util.concurrent.*;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ServiceClientBase<DerivedClient extends ServiceClientBase> {

    private static final String DEFAULT_FORMAT = JsonContentFormatter.EXTENSION;
    private static final int DEFAULT_REQUEST_TIME_OUT = 10 * 1000;
    private static final int DEFAULT_SOCKET_TIME_OUT = 10 * 1000;
    private static final int DEFAULT_CONNECT_TIME_OUT = 10 * 1000;
    private static final int DEFAULT_MAX_CONNECTIONS = 20;
    private static final long OLD_CLIENT_DISPOSE_DELAY = 30 * 1000;
    private static final int MAX_INIT_REG_SYNC_ATTEMPTS = 3;
    private static final int INIT_REG_SYNC_INTERVAL = 5 * 1000; // 5 seconds
    private static final int DEFAULT_REG_SYNC_INTERVAL = 5 * 60 * 1000; // 5 minutes
    private static final int DEFAULT_STATS_REPORTING_INTERVAL = 30 * 1000; // 30 seconds

    protected static final String ORIGINAL_SERVICE_NAME_FIELD_NAME = "ORIGINAL_SERVICE_NAME";
    protected static final String ORIGINAL_SERVICE_NAMESPACE_FIELD_NAME = "ORIGINAL_SERVICE_NAMESPACE";
    protected static final String CODE_GENERATOR_VERSION_FIELD_NAME = "CODE_GENERATOR_VERSION";

    private static ServiceClientConfig _clientConfig = new ServiceClientConfig();

    private static final String _frameworkVersion;

    private static final Map<String, ContentFormatter> _contentFormatters =
            new HashMap<String, ContentFormatter>();
    private static final Map<String, ServiceClientBase> _clientCache =
            new HashMap<String, ServiceClientBase>();
    private static final Map<String, Map<String, String>> _serviceMetadataCache =
            new HashMap<String, Map<String, String>>();
    private static HttpRequestFilter _globalHttpRequestFilter;
    private static HttpResponseFilter _globalHttpResponseFilter;

    private static RegistryClient _registryClient;

    private final Logger _logger;
    private final Tracer _tracer;

    private String _serviceName, _serviceNamespace, _subEnv;
    private String _format = DEFAULT_FORMAT;
    private int _requestTimeOut = DEFAULT_REQUEST_TIME_OUT;
    private int _socketTimeOut = DEFAULT_SOCKET_TIME_OUT;
    private int _connectTimeOut = DEFAULT_CONNECT_TIME_OUT;
    private int _maxConnections = DEFAULT_MAX_CONNECTIONS;
    private ServiceInfo _serviceInfo;
    private String[] _serviceUris;
    private HttpRequestFilter _localHttpRequestFilter;
    private HttpResponseFilter _localHttpResponseFilter;
    private RequestConfig _requestConfig;
    private final AtomicInteger _lastUsedServiceIndex = new AtomicInteger(-1);
    private final ConnectionMode _connectionMode;
    private final Map<String, String> _headers = new HashMap<String, String>();
    private final InvocationStatsStore _statsStore;
    private final AtomicReference<CloseableHttpClient> _client = new AtomicReference<CloseableHttpClient>(createClient());
    private final AtomicReference<CloseableHttpAsyncClient> _asyncClient = new AtomicReference<CloseableHttpAsyncClient>(createAsyncClient());

    private final ScheduledExecutorService _registrySyncService = new ScheduledThreadPoolExecutor(1, new DaemonThreadFactory());
    private final ScheduledExecutorService _clientDisposeService = new ScheduledThreadPoolExecutor(1, new DaemonThreadFactory());
    private final ScheduledExecutorService _statsReportService
            = new ScheduledThreadPoolExecutor(1, new DaemonThreadFactory());
    private final ExecutorService _delegateAsyncTransformer = Executors.newFixedThreadPool(5);
    private final ListeningExecutorService _transformerPool = MoreExecutors.listeningDecorator(_delegateAsyncTransformer);

    static {
        registerContentFormatter(new BinaryContentFormatter());
        registerContentFormatter(new JsonContentFormatter());
        _frameworkVersion = VersionUtils.getPackageVersion(ServiceClientBase.class);
    }

    /**
     * Gets the collection of headers to be added to outgoing requests.
     *
     * @return
     */
    public Map<String, String> headers() {
        return _headers;
    }

    /**
     * Gets the name of target service.
     *
     * @return
     */
    public String getServiceName() {
        return _serviceName;
    }

    /**
     * Gets the namespace of the target service.
     *
     * @return
     */
    public String getServiceNamespace() {
        return _serviceNamespace;
    }

    public String getSubEnv() {
        return _subEnv;
    }

    /**
     * Gets the current calling format.
     *
     * @return
     */
    public String getFormat() {
        return _format;
    }

    public void setFormat(String format) {
        if (!_contentFormatters.containsKey(format)) {
            throw new IllegalArgumentException(String.format("Format %s is not supported.", format));
        }
        _format = format;
    }

    /**
     * Returns the timeout in milliseconds used when requesting a connection
     * from the connection manager. A timeout value of zero is interpreted as an
     * infinite timeout.
     * <p/>
     * A timeout value of zero is interpreted as an infinite timeout. A negative
     * value is interpreted as undefined (system default).
     * <p/>
     * Default: <code>10000</code>
     */
    public int getRequestTimeout() {
        return _requestTimeOut;
    }

    public void setRequestTimeout(int requestTimeOut) {
        if (requestTimeOut < 0 || requestTimeOut == _requestTimeOut) {
            return;
        }
        this._requestTimeOut = requestTimeOut;
        updateRequestConfig();
    }

    /**
     * Defines the socket timeout (<code>SO_TIMEOUT</code>) in milliseconds,
     * which is the timeout for waiting for data or, put differently, a maximum
     * period inactivity between two consecutive data packets).
     * <p/>
     * A timeout value of zero is interpreted as an infinite timeout. A negative
     * value is interpreted as undefined (system default).
     * <p/>
     * Default: <code>10000</code>
     */
    public int getSocketTimeout() {
        return _socketTimeOut;
    }

    public void setSocketTimeout(int socketTimeOut) {
        if (socketTimeOut < 0 || socketTimeOut == _socketTimeOut) {
            return;
        }
        this._socketTimeOut = socketTimeOut;
        updateRequestConfig();
    }

    /**
     * Determines the timeout in milliseconds until a connection is established.
     * A timeout value of zero is interpreted as an infinite timeout.
     * <p/>
     * A timeout value of zero is interpreted as an infinite timeout. A negative
     * value is interpreted as undefined (system default).
     * <p/>
     * Default: <code>10000</code>
     */
    public int getConnectTimeout() {
        return _connectTimeOut;
    }

    public void setConnectTimeout(int connectTimeOut) {
        if (connectTimeOut < 0 || connectTimeOut == _connectTimeOut) {
            return;
        }
        this._connectTimeOut = connectTimeOut;
        updateRequestConfig();
    }

    /**
     * Gets the maximum HTTP connections which can be established to the target service.
     *
     * @return the maximum HTTP connections which can be established to the target service
     */
    public int getMaxConnections() {
        return _maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        if (this._maxConnections != maxConnections) {
            this._maxConnections = maxConnections;
            reloadClient();
        }
    }

    private void updateRequestConfig() {
        _requestConfig = RequestConfig.custom()
                .setConnectTimeout(_connectTimeOut)
                .setConnectionRequestTimeout(_requestTimeOut)
                .setSocketTimeout(_socketTimeOut)
                .build();
    }

    /**
     * The request filter is called before any request.
     * This request filter is executed globally.
     *
     * @return
     */
    public static HttpRequestFilter getGlobalHttpRequestFilter() {
        return _globalHttpRequestFilter;
    }

    public static void setGlobalHttpRequestFilter(HttpRequestFilter filter) {
        _globalHttpRequestFilter = filter;
    }


    /**
     * Gets the response action is called once the server response is available.
     * It will allow you to access raw response information.
     * This response action is executed globally.
     * Note that you should NOT consume the response stream as this is handled by Baiji RPC
     */
    public static HttpResponseFilter getGlobalHttpResponseFilter() {
        return _globalHttpResponseFilter;
    }

    public static void setGlobalHttpResponseFilter(HttpResponseFilter filter) {
        _globalHttpResponseFilter = filter;
    }

    /**
     * The request filter is called before any request.
     * This request filter only works with the instance where it was set (not global).
     *
     * @return
     */
    public HttpRequestFilter getLocalHttpRequestFilter() {
        return _localHttpRequestFilter;
    }

    public void setLocalHttpRequestFilter(HttpRequestFilter filter) {
        _localHttpRequestFilter = filter;
    }

    /**
     * The response action is called once the server response is available.
     * It will allow you to access raw response information.
     * Note that you should NOT consume the response stream as this is handled by Baiji RPC
     *
     * @return
     */
    public HttpResponseFilter getLocalHttpResponseFilter() {
        return _localHttpResponseFilter;
    }

    public void setLocalHttpResponseFilter(HttpResponseFilter filter) {
        _localHttpResponseFilter = filter;
    }

    /**
     * Initialize the ServiceClient with a global {@link ServiceClientConfig}.
     * <p/>
     * This needs to be called before obtaining any service client instance;
     *
     * @param config provides the global config
     */
    public static void initialize(ServiceClientConfig config) {
        _clientConfig = config != null ? config : new ServiceClientConfig();
        String registryServiceUrl = _clientConfig.getServiceRegistryUrl();
        if (registryServiceUrl == null) {
            _registryClient = null;
        } else {
            try {
                _registryClient = new EtcdRegistryClient(URI.create(registryServiceUrl));
            } catch (Exception ex) {
                _registryClient = null;
            }
        }
    }

    protected ServiceClientBase(Class<DerivedClient> clientClass, String baseUri) {
        _logger = LoggerFactory.getLogger(clientClass);
        _tracer = TracerFactory.getTracer(clientClass);
        _connectionMode = ConnectionMode.DIRECT;

        _serviceUris = new String[]{baseUri};
        _statsStore = new InvocationStatsStore();
        _serviceInfo = new ServiceInfo(true, "");

        updateRequestConfig();

        _logger.info("Initialized client instance with direct service url " + baseUri);
    }

    protected ServiceClientBase(Class<DerivedClient> clientClass, String serviceName, String serviceNamespace,
                                String subEnv) throws ServiceLookupException {
        _logger = LoggerFactory.getLogger(clientClass);
        _tracer = TracerFactory.getTracer(clientClass);
        _connectionMode = ConnectionMode.INDIRECT;

        _serviceName = serviceName;
        _serviceNamespace = serviceNamespace;
        _subEnv = subEnv;

        updateRequestConfig();

        initServiceBaseUriFromReg();

        String codeGeneratorVersion = _serviceMetadataCache.get(clientClass.getName()).get(CODE_GENERATOR_VERSION_FIELD_NAME);
        _statsStore = new InvocationStatsStore(_serviceName, _serviceNamespace, _connectionMode, _frameworkVersion, codeGeneratorVersion);

        // Only report stats data in indirect connection mode.
        _statsReportService.scheduleAtFixedRate(new StatsReportJob(_statsStore), DEFAULT_STATS_REPORTING_INTERVAL,
                DEFAULT_STATS_REPORTING_INTERVAL, TimeUnit.MILLISECONDS);
    }

    /**
     * Gets the direct client instance for testing at development stage.
     * <p/>
     * In dev environment, you can use this method to obtain the client instance. It will cached as a singleton.
     * <p/>
     * NOTE: please DO NOT use this method to obtain client instance in production or official test environments.
     * Because the returned client instance is bound to a specified target service URL.
     * When the service URL is changed, the client may not be able to access the service any more.
     * Besides, the client will not collect any stats data, either.
     *
     * @param baseUrl
     * @return
     */
    public static <DerivedClient extends ServiceClientBase> DerivedClient getInstance(Class<DerivedClient> clientClass, String baseUrl) {
        return getInstanceInternal(clientClass, baseUrl);
    }

    /**
     * Gets the indirect client instance of the service.
     * <p/>
     * In production or official test environments, please use this one to obtain the client instance.
     * The instance will be created and cached for future use.
     * It will update the target URL by querying the registry service periodically.
     * <p/>
     * For test environments, please configure sub environment name in AppSettings section of config file, e.g.: TBD
     * No sub environment is required for UAT and production environments.
     *
     * @param clientClass
     * @param <DerivedClient>
     * @return
     */
    public static <DerivedClient extends ServiceClientBase> DerivedClient getInstance(Class<DerivedClient> clientClass) {
        String clientName = clientClass.getName();
        if (!_serviceMetadataCache.containsKey(clientName) || _serviceMetadataCache.get(clientName) == null) {
            synchronized (_serviceMetadataCache) {
                if (!_serviceMetadataCache.containsKey(clientName) || _serviceMetadataCache.get(clientName) == null) {
                    Map<String, String> metadata = new HashMap<String, String>();
                    String[] requiredFieldNames = new String[]{
                            ORIGINAL_SERVICE_NAME_FIELD_NAME,
                            ORIGINAL_SERVICE_NAMESPACE_FIELD_NAME,
                            CODE_GENERATOR_VERSION_FIELD_NAME
                    };
                    for (String fieldName : requiredFieldNames) {
                        try {
                            Field field = clientClass.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            if (String.class.equals(field.getType())) {
                                String fieldValue = (String) field.get(null);
                                metadata.put(fieldName, fieldValue);
                            }
                        } catch (Exception e) {
                        }
                    }

                    if (!metadata.containsKey(CODE_GENERATOR_VERSION_FIELD_NAME)) {
                        metadata.put(CODE_GENERATOR_VERSION_FIELD_NAME, "1.0.0.0");
                    }

                    if (metadata.size() != requiredFieldNames.length) {
                        String message = String.format(
                                "Service name and namespace constants are not in the generated service client code: %s, %s",
                                ORIGINAL_SERVICE_NAME_FIELD_NAME,
                                ORIGINAL_SERVICE_NAMESPACE_FIELD_NAME);
                        throw new RuntimeException(message);
                    }

                    _serviceMetadataCache.put(clientName, metadata);
                }
            }
        }

        return getInstanceInternal(clientClass,
                _serviceMetadataCache.get(clientName).get(ORIGINAL_SERVICE_NAME_FIELD_NAME),
                _serviceMetadataCache.get(clientName).get(ORIGINAL_SERVICE_NAMESPACE_FIELD_NAME),
                _clientConfig.getSubEnv());
    }


    static <DerivedClient extends ServiceClientBase> DerivedClient getInstanceInternal(Class<DerivedClient> clientClass,
                                                                                       String baseUrl) {
        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new IllegalArgumentException("baseUrl can't be null or empty");
        }
        if (!_clientCache.containsKey(baseUrl)) {
            synchronized (_clientCache) {
                if (!_clientCache.containsKey(baseUrl)) {
                    DerivedClient client;
                    try {
                        Constructor<DerivedClient> ctor = clientClass.getDeclaredConstructor(String.class);
                        ctor.setAccessible(true);
                        client = ctor.newInstance(baseUrl);
                    } catch (Exception e) {
                        throw new RuntimeException("Error occurs when creating client instance.", e);
                    }

                    _clientCache.put(baseUrl, client);
                }
            }
        }

        return (DerivedClient) _clientCache.get(baseUrl);
    }

    static <DerivedClient extends ServiceClientBase> DerivedClient getInstanceInternal(Class<DerivedClient> clientClass,
                                                                                       String serviceName,
                                                                                       String serviceNamespace,
                                                                                       String subEnv) {
        if (serviceName == null || serviceName.isEmpty()) {
            throw new IllegalArgumentException("serviceName can't be null or empty");
        }
        if (serviceNamespace == null || serviceNamespace.isEmpty()) {
            throw new IllegalArgumentException("serviceNamespace can't be null or empty");
        }

        String key = serviceName + "{" + serviceNamespace + "}";
        if (!_clientCache.containsKey(key)) {
            synchronized (_clientCache) {
                if (!_clientCache.containsKey(key)) {
                    DerivedClient client;
                    try {
                        Constructor<DerivedClient> ctor = clientClass.getDeclaredConstructor(String.class, String.class, String.class);
                        ctor.setAccessible(true);
                        client = ctor.newInstance(serviceName, serviceNamespace, subEnv);
                    } catch (Exception e) {
                        throw new RuntimeException("Error occurs when creating client instance.", e);
                    }
                    _clientCache.put(key, client);
                }
            }
        }

        return (DerivedClient) _clientCache.get(key);
    }

    public <TReq extends SpecificRecord, TResp extends SpecificRecord> TResp invoke(String operation, TReq request,
                                                                                    Class<TResp> responseClass)
            throws Exception {
        Span span = null;
        if (_tracer.isTracing()) {
            span = _tracer.startSpan(operation, getClass().getSimpleName(), SpanType.WEB_SERVICE);
        }
        return invokeInternal(operation, request, responseClass, span);
    }

    private <TReq extends SpecificRecord, TResp extends SpecificRecord> TResp invokeInternal(String operationName, TReq request,
                                                                                             Class<TResp> responseClass, Span span)
            throws Exception {

        if (_serviceInfo == null || !_serviceInfo.isReady()) {
            throw new IllegalStateException("The service is not ready for use now.");
        }

        long startTime = System.currentTimeMillis();
        _statsStore.getStats(operationName).markInvocation();
        CloseableHttpResponse httpResponse = null;
        try {
            ContentFormatter formatter = _contentFormatters.get(_format);
            HttpPost httpPost = prepareWebRequest(operationName, request, formatter, span);
            httpResponse = _client.get().execute(httpPost);

            applyHttpResponseFilters(httpResponse);

            checkHttpResponseStatus(httpResponse);

            int contentLength = getContentLength(httpResponse);
            _statsStore.getStats(operationName).addResponseSize(contentLength);

            TResp response = formatter.deserialize(responseClass, httpResponse.getEntity().getContent());
            if (response instanceof HasResponseStatus) {
                checkResponseStatus((HasResponseStatus) response);
            }
            _statsStore.getStats(operationName).markSuccess();
            return response;
        } catch (ServiceException ex) {
            Map<String, String> additionalInfo = getClientInfo();
            additionalInfo.put("Operation", operationName);
            _logger.error(getLogTitle("ServiceException"), ex, additionalInfo);

            InvocationStats stats = _statsStore.getStats(operationName);
            stats.markException(ex);
            stats.markFailure();

            throw ex;
        } catch (Exception ex) {
            logGeneralException(operationName, ex);

            InvocationStats stats = _statsStore.getStats(operationName);
            stats.markException(ex);
            stats.markFailure();

            if (ex instanceof HttpWebException) {
                HttpWebException hwe = (HttpWebException) ex;
                _logger.debug(String.format("Status Code: %s\nReason Phrase: %s", hwe.getStatusCode(),
                        hwe.getReasonPhrase()));
            }

            throw ex;
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException ioe) {
                }
            }
            if (span != null) {
                span.stop();
            }
            _statsStore.getStats(operationName).addRequestCost(System.currentTimeMillis() - startTime);
        }
    }

    public <TReq extends SpecificRecord, TResp extends SpecificRecord> TResp invokeAsync(String operation, TReq request, Class<TResp> responseClass) {
        Span span = null;
        if (_tracer.isTracing()) {
            span = _tracer.startSpan(operation, getClass().getSimpleName(), SpanType.WEB_SERVICE);
        }
        try {
            return invokeAsyncInternal(operation, request, responseClass, span).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        }

        return null;
    }

    public <TReq extends SpecificRecord, TResp extends SpecificRecord> ListenableFuture<TResp> invokeAsyncInternal(final String operation, TReq request, final Class<TResp> responseClass, Span span) {
        if (_serviceInfo == null || !_serviceInfo.isReady()) {
            throw new IllegalStateException("The service is not ready for use now.");
        }

        try {
            final ContentFormatter contentFormatter = _contentFormatters.get(_format);
            HttpPost httpPost = prepareWebRequest(operation, request, contentFormatter, span);

            ListenableFuture<HttpResponse> httpResponse = asyncExecuteHttp(httpPost);

            return Futures.transform(httpResponse, new AsyncTransformation<>(_transformerPool, responseClass, operation));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ListenableFuture<HttpResponse> asyncExecuteHttp(HttpUriRequest request) {
        final SettableFuture<HttpResponse> future = SettableFuture.create();

        _asyncClient.get().execute(request, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse result) {
                future.set(result);
            }

            @Override
            public void failed(Exception ex) {
                future.setException(ex);
            }

            @Override
            public void cancelled() {
                future.setException(new InterruptedException());
            }
        });

        return future;
    }

    private int getContentLength(HttpResponse response) {
        Header lengthHeader = response.getFirstHeader(HttpHeaders.CONTENT_LENGTH);
        if (lengthHeader == null) {
            return 0;
        }

        try {
            return Integer.valueOf(lengthHeader.getValue());
        } catch (Exception ex) {
            return 0;
        }
    }

    private <TReq extends SpecificRecord> HttpPost prepareWebRequest(String operationName, TReq request,
                                                                     ContentFormatter contentFormatter, Span span)
            throws IOException {
        String baseUri = getServiceBaseUri();
        if (baseUri == null) {
            throw new IllegalStateException("No service instance available at the time.");
        }
        String requestUri = baseUri + operationName + "." + _format;
        HttpPost httpPost = new HttpPost(requestUri);
        httpPost.setConfig(_requestConfig);

        if (span != null) {
            httpPost.addHeader(ServiceCommons.TRACE_ID_HTTP_HEADER, String.valueOf(span.getTraceId()));
            httpPost.addHeader(ServiceCommons.SPAN_ID_HTTP_HEADER, String.valueOf(span.getSpanId()));
        }
        for (Map.Entry<String, String> header : _headers.entrySet()) {
            httpPost.addHeader(header.getKey(), header.getValue());
        }
        if (_clientConfig != null && _clientConfig.getAppId() != null) {
            httpPost.addHeader(ServiceCommons.APP_ID_HTTP_HEADER, _clientConfig.getAppId());
        }

        applyHttpRequestFilters(httpPost);

        httpPost.addHeader(HttpHeaders.CONTENT_TYPE, contentFormatter.getContentType());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        contentFormatter.serialize(output, request);
        HttpEntity entity = new ByteArrayEntity(output.toByteArray());
        httpPost.setEntity(entity);

        return httpPost;
    }

    private void checkHttpResponseStatus(HttpResponse response)
            throws HttpWebException {
        if (response.getStatusLine().getStatusCode() <= 200) {
            return;
        }

        String responseContent = getResponseContent(response);
        StatusLine status = response.getStatusLine();
        HttpWebException ex = new HttpWebException(status.getStatusCode(), status.getReasonPhrase(), responseContent);

        throw ex;
    }

    private String getResponseContent(HttpResponse response) {
        String responseContent = null;
        InputStreamReader reader = null;
        try {
            char[] buffer = new char[512];
            StringBuilder builder = new StringBuilder();
            reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
            int length;
            while ((length = reader.read(buffer, 0, buffer.length)) >= 0) {
                builder.append(buffer, 0, length);
            }
            responseContent = builder.toString();
        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
            try {
                if (response instanceof CloseableHttpResponse) {
                    ((CloseableHttpResponse) response).close();
                }
            } catch (IOException e) {
            }
        }
        return responseContent;
    }

    private void checkResponseStatus(HasResponseStatus response) throws ServiceException {
        ResponseStatusType responseStatus = response.getResponseStatus();
        if (responseStatus.getAck() != AckCodeType.FAILURE) {
            return;
        }

        ServiceException ex;
        List<ErrorDataType> errors = responseStatus.getErrors();
        if (errors != null && !errors.isEmpty() && errors.get(0) != null) {
            ErrorDataType error = responseStatus.getErrors().get(0);
            ex = new ServiceException(error.getMessage(), response, error.getErrorCode());
        } else {
            // should not happen in real case, just for defensive programming
            String message = "Failed response without error data, please file a bug to the service owner!";
            ex = new ServiceException(message, response);
        }
        throw ex;
    }

    private void applyHttpRequestFilters(HttpRequestBase request) {
        HttpRequestFilter filter = _localHttpRequestFilter;
        if (filter != null) {
            filter.apply(request);
        }

        filter = _globalHttpRequestFilter;
        if (filter != null) {
            filter.apply(request);
        }
    }

    private void applyHttpResponseFilters(HttpResponse response) {
        HttpResponseFilter filter = _globalHttpResponseFilter;
        if (filter != null) {
            filter.apply(response);
        }

        filter = _localHttpResponseFilter;
        if (filter != null) {
            filter.apply(response);
        }
    }

    private void reloadClient() {
        final CloseableHttpClient oldClient = _client.get();
        _client.set(createClient());
        if (oldClient != null) {
            _clientDisposeService.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        oldClient.close();
                    } catch (Throwable t) {
                        _logger.warn("Error occurs when shutting down an old HTTP client.", t);
                    }
                }
            }, OLD_CLIENT_DISPOSE_DELAY, TimeUnit.MILLISECONDS);
        }
    }

    private CloseableHttpClient createClient() {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setMaxConnPerRoute(_maxConnections)
                .build();
        return httpClient;
    }

    private CloseableHttpAsyncClient createAsyncClient() {
        CloseableHttpAsyncClient httpAsyncClient;
        httpAsyncClient = HttpAsyncClients.custom()
                .setMaxConnPerRoute(_maxConnections)
                .build();

        httpAsyncClient.start();

        return httpAsyncClient;
    }

    private void close(HttpResponse response) {
        if (response == null)
            return;

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            EntityUtils.consumeQuietly(entity);
        }
    }

    /**
     * Register calling format.
     *
     * @param contentFormatter
     */
    public static void registerContentFormatter(ContentFormatter contentFormatter) {
        if (!_contentFormatters.containsKey(contentFormatter.getExtension())) {
            _contentFormatters.put(contentFormatter.getExtension(), contentFormatter);
        }
    }

    public static Collection<String> getSupportFormats() {
        return _contentFormatters.keySet();
    }

    private void initServiceBaseUriFromReg() {
        Runnable syncRegTask = new SyncRegistryTask();

        // First time sync
        for (int i = 1; i <= MAX_INIT_REG_SYNC_ATTEMPTS; i++) {
            syncRegTask.run();
            if (_serviceUris != null && _serviceUris.length != 0) {
                String msg = String.format("Initialized client instance with registry %s. Targeting service: %s-%s. TargetURLs: %s.",
                        _clientConfig.getServiceRegistryUrl(), _serviceName, _serviceNamespace, Joiner.on(";").join(_serviceUris));
                _logger.info(msg);
                break;
            }

            if (i < MAX_INIT_REG_SYNC_ATTEMPTS) {
                try {
                    Thread.sleep(INIT_REG_SYNC_INTERVAL);
                } catch (InterruptedException e) {
                    break;
                }
            } else {
                String msg = String.format("Unable to find service(%s-%s) url mapping in registry %s", _serviceName,
                        _serviceNamespace, _clientConfig.getServiceRegistryUrl());
                _logger.error(msg);
            }
        }

        // Periodically sync registry
        _registrySyncService.scheduleAtFixedRate(syncRegTask, 0, DEFAULT_REG_SYNC_INTERVAL, TimeUnit.MILLISECONDS);
    }

    private String getServiceBaseUri() {
        String serviceUri = null;
        if (_connectionMode == ConnectionMode.DIRECT) {
            serviceUri = _serviceUris[0];
        } else if (_connectionMode == ConnectionMode.INDIRECT) {
            if (_serviceUris.length == 0) {
                _lastUsedServiceIndex.set(-1);
                serviceUri = null;
            } else {
                int index;
                while (true) {
                    index = _lastUsedServiceIndex.incrementAndGet();
                    if (index >= 0 && index < _serviceUris.length) {
                        break;
                    }
                    if (_lastUsedServiceIndex.compareAndSet(index, 0)) {
                        index = 0;
                        break;
                    }
                }
                try {
                    serviceUri = _serviceUris[index];
                } catch (Exception ex) {
                    serviceUri = _serviceUris.length != 0 ? _serviceUris[0] : null;
                }
            }
        }

        if (serviceUri != null) {
            serviceUri = serviceUri.endsWith("/") ? serviceUri : serviceUri + "/";
        }
        return serviceUri;
    }

    private void logGeneralException(String operationName, Exception ex) {
        String title = getLogTitle("General Exception");
        Map<String, String> additionalInfo = getClientInfo();
        additionalInfo.put("Operation", operationName);
        if (ex instanceof HttpWebException) {
            _logger.warn(title, ex, additionalInfo);
        } else {
            _logger.error(title, ex, additionalInfo);
        }
    }

    private String getLogTitle(String title) {
        String serviceContact = _serviceInfo != null ? _serviceInfo.getServiceContact() : null;
        if (serviceContact == null || serviceContact.isEmpty()) {
            return title;
        }

        StringBuilder titleBuilder = new StringBuilder();
        if (title == null || title.isEmpty()) {
            title = title.trim();
            titleBuilder.append(title);
            if (!title.endsWith(".")) {
                titleBuilder.append(".");
            }
            titleBuilder.append(" ");
        }

        titleBuilder.append("Service Owner: ");
        titleBuilder.append(serviceContact);
        return titleBuilder.toString();
    }

    private Map<String, String> getClientInfo() {
        Map<String, String> clientInfo = new HashMap<String, String>();
        if (_serviceName != null && _serviceNamespace != null) {
            clientInfo.put("Service", _serviceName + "{" + _serviceNamespace + "}");
        }
        clientInfo.put("ServiceContact",
                _serviceInfo != null && _serviceInfo.getServiceContact() != null ? _serviceInfo.getServiceContact() : "");
        clientInfo.put("ConnectionMode", _connectionMode.toString());
        clientInfo.put("Format", _format);
        return clientInfo;
    }

    private class SyncRegistryTask implements Runnable {

        @Override
        public void run() {
            if (_registryClient == null) {
                _logger.fatal(getLogTitle("No service registry is configured."));
                return;
            }

            try {
                _serviceInfo = _registryClient.getServiceInfo(_serviceName, _serviceNamespace);
            } catch (Exception ex) {
                _logger.error(getLogTitle("Update service info failed."), ex);
            }

            try {
                List<InstanceInfo> instances = _registryClient.getServiceInstances(_serviceName, _serviceNamespace, _subEnv);
                if (instances != null) {
                    ArrayList<String> uris = new ArrayList<String>();
                    for (InstanceInfo instance : instances) {
                        if (instance.isUp()) {
                            uris.add(instance.getServiceUrl());
                        }
                    }
                    _serviceUris = uris.toArray(new String[0]);
                } else {
                    _serviceUris = new String[0];
                }
            } catch (Exception ex) {
                _logger.error(getLogTitle("Sync Service Registry failed."), ex);
            }
        }
    }

    private class AsyncTransformation<TResp extends SpecificRecord> implements AsyncFunction<HttpResponse, TResp> {

        private final ListeningExecutorService transformPool;
        private final Class<TResp> clazz;
        private final String operation;

        private ContentFormatter contentFormatter = _contentFormatters.get(_format);

        public AsyncTransformation(final ListeningExecutorService transformPool, final Class<TResp> clazz, final String operation) {
            this.transformPool = transformPool;
            this.clazz = clazz;
            this.operation = operation;
        }

        @Override
        public ListenableFuture<TResp> apply(HttpResponse response) throws Exception {
            return transformPool.submit(new TransformWorker<TResp>(response, clazz, operation));
        }

        private class TransformWorker<TResp extends SpecificRecord> implements Callable<TResp> {

            private final HttpResponse response;
            private final Class<TResp> clazz;
            private final String operation;

            public TransformWorker(final HttpResponse response, final Class<TResp> clazz, final String operation) {
                this.response = response;
                this.clazz = clazz;
                this.operation = operation;
            }

            @Override
            public TResp call() throws Exception {

                applyHttpResponseFilters(response);

                checkHttpResponseStatus(response);

                int contentLength = getContentLength(response);
                _statsStore.getStats(operation).addResponseSize(contentLength);

                TResp resp = contentFormatter.deserialize(clazz, response.getEntity().getContent());
                if (response instanceof HasResponseStatus) {
                    checkResponseStatus((HasResponseStatus) response);
                }

                close(response);
                _statsStore.getStats(operation).markSuccess();

                return resp;
            }
        }
    }
}
