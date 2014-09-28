package com.ctriposs.baiji.rpc.server;

import com.ctriposs.baiji.exception.BaijiRuntimeException;
import com.ctriposs.baiji.rpc.common.logging.Logger;
import com.ctriposs.baiji.rpc.common.logging.LoggerFactory;
import com.ctriposs.baiji.rpc.server.registry.EtcdServiceRegistry;
import com.ctriposs.baiji.rpc.server.registry.ServiceInfo;
import com.ctriposs.baiji.rpc.server.registry.ServiceRegistry;
import org.apache.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yqdong on 2014/9/15.
 */
public abstract class BaijiServletBase extends HttpServlet {

    // Context Params
    protected static final String ETCD_SERVICE_URL_PARAM = "etcd-service-url";
    protected static final String SERVICE_PORT_PARAM = "service-port";
    protected static final String SUB_ENV_PARAM = "sub-env";

    // Servlet Init Params
    protected static final String SERVICE_CLASS_PARAM = "service-class";
    protected static final String DETAILED_ERROR_PARAM = "detailed-error";
    protected static final String DEBUG_MODE_PARAM = "debug";
    protected static final String REUSE_SERVICE_PARAM = "reuse-service";

    protected static final Logger _logger = LoggerFactory.getLogger(BaijiServlet.class);
    protected static final Object _classLock = new Object();
    protected static ServiceRegistry _serviceRegistry;
    protected static int _port;
    protected static String _subEnv;
    protected ServiceHost _serviceHost;

    @Override
    public void init() {
        Class<?> serviceClass = getServiceClass();
        HostConfig config = buildHostConfig();
        _serviceHost = new BaijiServiceHost(config, serviceClass);
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

    private HostConfig buildHostConfig() {
        HostConfig config = new HostConfig();

        ServletConfig servletConfig = getServletConfig();

        String detailedError = servletConfig.getInitParameter(DETAILED_ERROR_PARAM);
        if ("true".equalsIgnoreCase(detailedError)) {
            config.outputExceptionStackTrace = true;
        }

        String debugMode = servletConfig.getInitParameter(DEBUG_MODE_PARAM);
        if ("true".equalsIgnoreCase(debugMode)) {
            config.debugMode = true;
        }

        String reuseService = servletConfig.getInitParameter(REUSE_SERVICE_PARAM);
        if (reuseService != null && !"true".equalsIgnoreCase(reuseService)) {
            config.newServiceInstancePerRequest = true;
        }

        return config;
    }

    private void registerService() {
        initializeServiceRegistry();

        if (_serviceRegistry == null) {
            return;
        }

        ServiceMetadata metadata = _serviceHost.getServiceMetaData();
        ServletContext servletContext = getServletContext();
        ServiceInfo serviceInfo = new ServiceInfo.Builder().serviceName(metadata.getServiceName())
                .serviceNamespace(metadata.getServiceNamespace())
                .port(_port).contextPath(servletContext.getContextPath()).subEnv(_subEnv).build();
        _serviceRegistry.addService(serviceInfo);
    }

    private void initializeServiceRegistry() {
        synchronized (_classLock) {
            ServletContext servletContext = getServletContext();

            String portString = servletContext.getInitParameter(SERVICE_PORT_PARAM);
            if (portString != null && !portString.isEmpty()) {
                try {
                    _port = Integer.valueOf(portString);
                } catch (NumberFormatException ex) {
                    _logger.error("Invalid " + SERVICE_PORT_PARAM + " value: " + portString);
                    return;
                }
            }

            _subEnv = servletContext.getInitParameter(SUB_ENV_PARAM);

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

    protected void processRequest(ServletRequest req, ServletResponse resp)
            throws ServletException, IOException {
        if (_serviceHost == null) {
            ((HttpServletResponse) resp).sendError(HttpStatus.SC_SERVICE_UNAVAILABLE,
                    "No service hosted here.");
            return;
        }

        if (!(req instanceof HttpServletRequest && resp instanceof HttpServletResponse)) {
            throw new ServletException("Not HTTP request or response");
        }

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        HttpRequestWrapper requestWrapper = createRequestWrapper(request);
        HttpResponseWrapper responseWrapper = createResponseWrapper(response);
        _serviceHost.processRequest(requestWrapper, responseWrapper);
    }

    private HttpRequestWrapper createRequestWrapper(HttpServletRequest request) {
        return new HttpServletRequestWrapper(request);
    }

    private HttpResponseWrapper createResponseWrapper(HttpServletResponse response) {
        return new HttpServletResponseWrapper(response);
    }
}
