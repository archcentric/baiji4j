package com.ctriposs.baiji.rpc.client;

import com.ctriposs.baiji.rpc.common.HasResponseStatus;
import com.ctriposs.baiji.rpc.common.formatter.BinaryContentFormatter;
import com.ctriposs.baiji.rpc.common.formatter.ContentFormatter;
import com.ctriposs.baiji.rpc.common.formatter.JsonContentFormatter;
import com.ctriposs.baiji.rpc.common.types.AckCodeType;
import com.ctriposs.baiji.rpc.common.types.ErrorDataType;
import com.ctriposs.baiji.rpc.common.types.ResponseStatusType;
import com.ctriposs.baiji.specific.SpecificRecord;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ServiceClientBase<DerivedClient extends ServiceClientBase> {

    private static final String DEFAULT_FORMAT = BinaryContentFormatter.EXTENSION;
    private static final int DEFAULT_REQUEST_TIME_OUT = 10 * 1000;
    private static final int DEFAULT_SOCKET_TIME_OUT = 10 * 1000;
    private static final int DEFAULT_CONNECT_TIME_OUT = 10 * 1000;
    private static final int DEFAULT_MAX_CONNECTIONS = 20;
    private static final long OLD_CLIENT_DISPOSE_DELAY = 30 * 1000;
    private static final String APP_ID_HTTP_HEADER = "SOA20-Client-AppId";

    protected static final String ORIGINAL_SERVICE_NAME_FIELD_NAME = "OriginalServiceName";
    protected static final String ORIGINAL_SERVICE_NAMESPACE_FIELD_NAME = "OriginalServiceNamespace";

    private static ServiceClientConfig CLIENT_CONFIG;

    protected static final Map<String, ContentFormatter> _contentFormatters =
            new HashMap<String, ContentFormatter>();

    protected static final Map<String, ServiceClientBase> _clientCache =
            new HashMap<String, ServiceClientBase>();

    protected static final Map<String, Map<String, String>> _serviceMetadataCache =
            new HashMap<String, Map<String, String>>();

    private final Logger _logger;

    private boolean _registryMode;
    private String _baseUri, _serviceName, _serviceNamespace, _subEnv;
    private String _format = DEFAULT_FORMAT;
    private int _requestTimeOut = DEFAULT_REQUEST_TIME_OUT;
    private int _socketTimeOut = DEFAULT_SOCKET_TIME_OUT;
    private int _connectTimeOut = DEFAULT_CONNECT_TIME_OUT;
    private int _maxConnections = DEFAULT_MAX_CONNECTIONS;
    private final Map<String, String> _headers = new HashMap<String, String>();
    private final AtomicReference<CloseableHttpClient> _client = new AtomicReference<CloseableHttpClient>(createClient());
    private final ScheduledExecutorService _clientDisposeService = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }
    });

    static {
        registerContentFormatter(new BinaryContentFormatter());
        registerContentFormatter(new JsonContentFormatter());
    }

    /**
     * Initialize the ServiceClient with a global {@link ServiceClientConfig}.
     * <p/>
     * This needs to be called before obtaining any service client instance;
     *
     * @param config provides the global config
     */
    public static void initialize(ServiceClientConfig config) {
        CLIENT_CONFIG = config;
    }

    protected ServiceClientBase(Class<DerivedClient> clientClass) {
        _logger = LoggerFactory.getLogger(clientClass);
    }

    protected ServiceClientBase(Class<DerivedClient> clientClass, String baseUri) {
        this(clientClass);
        _registryMode = false;
        _baseUri = baseUri;
    }

    protected ServiceClientBase(Class<DerivedClient> clientClass, String serviceName, String serviceNamespace,
                                String subEnv) throws ServiceLookupException {
        this(clientClass);

        _serviceName = serviceName;
        _serviceNamespace = serviceNamespace;
        _subEnv = subEnv;

        resolveServiceBaseUri();

        if (_baseUri == null || _baseUri.isEmpty()) {
            String message = String.format("Unable to find service(%s-%s}) url mapping in registry",
                    _serviceName, _serviceNamespace);
            throw new ServiceLookupException(message);
        }

        _registryMode = true;
    }

    /**
     * Gets the direct client instance for testing at development stage.
     * <p/>
     * In dev environment, you can use this method to obtain the client instance. It will cached as a singleton.
     * <p/>
     * NOTE: please DO NOT use this method to obtain client instance in production or official test environments.
     * Because the returned client instance is bound to a specified target service URL.
     * When the service URL is changed, the client may not be able to access the service any more.
     * Besides, the client will not send any metrics to Central Logging, either.
     *
     * @param baseUrl
     * @return
     */
    public static <DerivedClient extends ServiceClientBase> DerivedClient getInstance(Class<DerivedClient> clientClass, String baseUrl) {
        return getInstanceInternal(clientClass, baseUrl, false);
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
                            ORIGINAL_SERVICE_NAMESPACE_FIELD_NAME
                    };
                    for (String fieldName : requiredFieldNames) {
                        try {
                            Field field = clientClass.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            if (String.class.equals(field.getClass())) {
                                String fieldValue = (String) field.get(null);
                                metadata.put(fieldName, fieldValue);
                            }
                        } catch (Exception e) {
                        }
                    }

                    if (metadata.size() != requiredFieldNames.length) {
                        String message = String.format(
                                "Service name and namespace constants are not in the generated service client code: {0}, {1}",
                                ORIGINAL_SERVICE_NAME_FIELD_NAME,
                                ORIGINAL_SERVICE_NAMESPACE_FIELD_NAME);
                        throw new RuntimeException(message);
                    }

                    _serviceMetadataCache.put(clientName, metadata);
                }
            }
        }

        // TODO: Support subEnv.
        //string subEnvKey = string.Format(
        //    SERVICE_REGISTRY_SUBENV_KEY_FORMAT,
        //    serviceMetadataCache[type.FullName][OriginalServiceNameFieldName],
        //    serviceMetadataCache[type.FullName][OriginalServiceNamespaceFieldName]);
        //string subEnv = ConfigUtils.GetNullableAppSetting(subEnvKey);
        //subEnv = string.IsNullOrWhiteSpace(subEnv) ? SERVICE_REGISTRY_SUBENV : subEnv.Trim().ToLower();
        String subEnv = null;

        return getInstanceInternal(clientClass,
                _serviceMetadataCache.get(clientName).get(ORIGINAL_SERVICE_NAME_FIELD_NAME),
                _serviceMetadataCache.get(clientName).get(ORIGINAL_SERVICE_NAMESPACE_FIELD_NAME),
                subEnv);
    }


    static <DerivedClient extends ServiceClientBase> DerivedClient getInstanceInternal(Class<DerivedClient> clientClass,
                                                                                       String baseUrl, boolean registryClient) {
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

                    if (!registryClient) {
                        //log.Info(string.Format("Initialized client instance with direct service url {0}", baseUri));
                        //log.Warn(
                        //    "Client is initialized in direct connection mode(without registry), this is only recommended for local testing, not for formal testing or production!");
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
            throws ServiceException, HttpWebException, IOException {
        return invokeInternal(operation, request, responseClass);
    }

    private <TReq extends SpecificRecord, TResp extends SpecificRecord> TResp invokeInternal(String operationName, TReq request,
                                                                                             Class<TResp> responseClass)
            throws ServiceException, HttpWebException, IOException {
        CloseableHttpResponse httpResponse = null;

        try {
            ContentFormatter formatter = _contentFormatters.get(_format);
            HttpPost httpPost = prepareWebRequest(operationName, request, formatter);
            httpResponse = _client.get().execute(httpPost);
            checkHttpResponseStatus(httpResponse);
            TResp response = formatter.deserialize(responseClass, httpResponse.getEntity().getContent());
            if (response instanceof HasResponseStatus) {
                checkResponseStatus((HasResponseStatus) response);
            }
            return response;
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException ioe) {
                }
            }
        }
    }

    private <TReq extends SpecificRecord> HttpPost prepareWebRequest(String operationName, TReq request,
                                                                     ContentFormatter contentFormatter)
            throws IOException {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(_connectTimeOut)
                .setConnectionRequestTimeout(_requestTimeOut)
                .setSocketTimeout(_socketTimeOut)
                .build();

        String baseUri = _baseUri.endsWith("/") ? _baseUri : _baseUri + "/";
        String requestUri = baseUri + operationName + "." + _format;
        HttpPost httpPost = new HttpPost(requestUri);
        httpPost.setConfig(config);
        for (Map.Entry<String, String> header : _headers.entrySet()) {
            httpPost.addHeader(header.getKey(), header.getValue());
        }
        if (CLIENT_CONFIG != null && CLIENT_CONFIG.getAppId() != null) {
            httpPost.addHeader(APP_ID_HTTP_HEADER, CLIENT_CONFIG.getAppId());
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        contentFormatter.serialize(output, request);
        HttpEntity entity = new ByteArrayEntity(output.toByteArray());
        httpPost.setEntity(entity);

        return httpPost;
    }

    private void checkHttpResponseStatus(CloseableHttpResponse response) throws HttpWebException {
        if (response.getStatusLine().getStatusCode() <= 200) {
            return;
        }

        String responseContent = getResponseContent(response);
        StatusLine status = response.getStatusLine();
        throw new HttpWebException(status.getStatusCode(), status.getReasonPhrase(), responseContent);
    }

    private String getResponseContent(CloseableHttpResponse response) {
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
                response.close();
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

        List<ErrorDataType> errors = responseStatus.getErrors();
        if (errors != null && !errors.isEmpty() && errors.get(0) != null) {
            ErrorDataType error = responseStatus.getErrors().get(0);
            throw new ServiceException(error.getMessage(), response, error.getErrorCode());
        } else // should not happen in real case, just for defensive programming
        {
            String message = "Failed response without error data, please file a bug to the service owner!";
            throw new ServiceException(message, response);
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

    /**
     * Gets the collection of headers to be added to outgoing requests.
     *
     * @return
     */
    public Map<String, String> headers() {
        return _headers;
    }

    /**
     * Gets the base URI of the target service.
     *
     * @return
     */
    public String getBaseUri() {
        return _baseUri;
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
     * Gets
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
        this._requestTimeOut = requestTimeOut;
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
        this._socketTimeOut = socketTimeOut;
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
        this._connectTimeOut = connectTimeOut;
    }

    /**
     * Gets the maximum HTTP connections which can be established to the target service.
     *
     * @return the maximum HTTP connections which can be established to the target service
     */
    public int getMaxConnections() {
        return _maxConnections;
    }

    public void setMaxConnections(int maxConnections_) {
        if (this._maxConnections != maxConnections_) {
            this._maxConnections = maxConnections_;
            reloadClient();
        }
    }

    private void resolveServiceBaseUri() {
        // TODO: Implement service registry support.
    }
}
