package com.ctriposs.baiji.rpc.server;

import com.ctriposs.baiji.exception.BaijiRuntimeException;
import com.ctriposs.baiji.rpc.server.registry.EtcdServiceRegistry;
import com.ctriposs.baiji.rpc.server.registry.ServiceInfo;
import com.ctriposs.baiji.rpc.server.registry.ServiceRegistry;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * An {@link javax.servlet.http.HttpServlet} that responds to Baiji RPC requests.
 */
public class BaijiServlet extends HttpServlet {

    // Context Params
    private static final String ETCD_SERVICE_URL_PARAM = "etcd-service-url";
    private static final String SERVICE_PORT_PARAM = "service-port";

    // Servlet Init Params
    private static final String SERVICE_CLASS_PARAM = "service-class";
    private static final String DETAILED_ERROR_PARAM = "detailed-error";
    private static final String REUSE_SERVICE_PARAM = "reuse-service";

    private static final Logger _logger = LoggerFactory.getLogger(BaijiServlet.class);
    private static final Object _classLock = new Object();
    private static ServiceRegistry _serviceRegistry;
    private static int _port;
    private HttpRequestRouter _router;

    @Override
    public void init() {
        Class<?> serviceClass = getServiceClass();
        ServiceConfig serviceConfig = buildServiceConfig();
        _router = new BaijiHttpRequestRouter(serviceConfig, serviceClass);
        registerService();
    }

    private Class<?> getServiceClass() {
        ServletConfig servletConfig = getServletConfig();
        String serviceClassName = servletConfig.getInitParameter(SERVICE_CLASS_PARAM);

        if (serviceClassName == null || serviceClassName.length() == 0) {
            final String errorMsg = "No service class is configured for BaijiServlet.";
            _logger.error(errorMsg);
            throw new BaijiRuntimeException(errorMsg);
        }

        Class<?> serviceClass;
        try {
            serviceClass = Class.forName(serviceClassName);
        } catch (ClassNotFoundException e) {
            String errorMsg = "Unable to locate service class: " + serviceClassName;
            _logger.error(errorMsg);
            throw new BaijiRuntimeException(errorMsg, e);
        }
        return serviceClass;
    }

    private ServiceConfig buildServiceConfig() {
        ServiceConfig config = new ServiceConfig();

        ServletConfig servletConfig = getServletConfig();

        String detailedError = servletConfig.getInitParameter(DETAILED_ERROR_PARAM);
        if ("true".equalsIgnoreCase(detailedError)) {
            config.setOutputExceptionStackTrace(true);
        }

        String reuseService = servletConfig.getInitParameter(REUSE_SERVICE_PARAM);
        if (reuseService != null && !"true".equalsIgnoreCase(reuseService)) {
            config.setNewServiceInstancePerRequest(true);
        }

        return config;
    }

    private void registerService() {
        initializeServiceRegistry();

        if (_serviceRegistry == null) {
            return;
        }

        ServiceMetadata metadata = _router.getServiceMetaData();
        ServletContext servletContext = getServletContext();
        ServiceInfo serviceInfo = new ServiceInfo.Builder().serviceName(metadata.getServiceName())
                .serviceNamespace(metadata.getServiceNamespace())
                .port(_port).contextPath(servletContext.getContextPath()).build();
        _serviceRegistry.addService(serviceInfo);
    }

    private void initializeServiceRegistry() {
        synchronized (_classLock) {
            ServletContext servletContext = getServletContext();

            String portString = servletContext.getInitParameter(SERVICE_PORT_PARAM);
            if (portString != null && !portString.isEmpty()) {
                _port = Integer.valueOf(portString);
            }

            String serviceUrl = servletContext.getInitParameter(ETCD_SERVICE_URL_PARAM);
            if (serviceUrl == null || serviceUrl.isEmpty()) {
                return;
            }

            _serviceRegistry = new EtcdServiceRegistry(serviceUrl);
            _serviceRegistry.run();
        }
    }

    @Override
    public void destroy() {
        synchronized (_classLock) {
            if (_serviceRegistry != null) {
                _serviceRegistry.stop();
                _serviceRegistry = null;
            }
        }
    }

    @Override
    public void service(ServletRequest req, ServletResponse resp)
            throws ServletException, IOException {

        if (_router == null) {
            ((HttpServletResponse) resp).sendError(HttpStatus.SC_SERVICE_UNAVAILABLE,
                    "No service processor is configured.");
            return;
        }

        if (!(req instanceof HttpServletRequest && resp instanceof HttpServletResponse)) {
            throw new ServletException("non-HTTP request or response");
        }

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        RequestContext requestContext = createRequestContext(request);
        HttpResponseWrapper responseWrapper = createResponseWrapper(response);
        _router.process(requestContext, responseWrapper);
    }

    private RequestContext createRequestContext(HttpServletRequest request) throws IOException {
        RequestContext environment = new RequestContext();
        environment.RequestBody = request.getInputStream();
        if (request.getHeaderNames() != null) {
            environment.RequestHeaders = new HashMap<String, String>();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                environment.RequestHeaders.put(headerName, request.getHeader(headerName));
            }
        }
        environment.RequestMethod = request.getMethod();
        URI uri;
        try {
            uri = new URI(request.getRequestURL().toString());
        } catch (URISyntaxException e) {
            throw new BaijiRuntimeException(e);
        }
        environment.RequestPath = request.getPathInfo();
        environment.RequestProtocol = request.getProtocol();
        environment.RequestQueryString = request.getQueryString();
        environment.RequestPathBase = request.getContextPath();
        environment.RequestScheme = uri.getScheme();
        return environment;
    }

    private HttpResponseWrapper createResponseWrapper(HttpServletResponse response) {
        return new HttpServletResponseWrapper(response);
    }
}
