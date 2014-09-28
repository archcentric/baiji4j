package com.ctriposs.baiji.rpc.server;

import com.ctriposs.baiji.exception.BaijiRuntimeException;
import com.ctriposs.baiji.rpc.server.registry.EtcdServiceRegistry;
import com.ctriposs.baiji.rpc.server.registry.ServiceInfo;
import com.ctriposs.baiji.rpc.server.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by yqdong on 2014/9/28.
 */
public class BaijiListener implements ServletContextListener {

    protected static final String ETCD_SERVICE_URL_PARAM = "etcd-service-url";
    protected static final String SERVICE_PORT_PARAM = "service-port";
    protected static final String SUB_ENV_PARAM = "sub-env";
    protected static final String SERVICE_CLASS_PARAM = "service-class";
    protected static final String DETAILED_ERROR_PARAM = "detailed-error";
    protected static final String REUSE_SERVICE_PARAM = "reuse-service";

    private static final Logger _logger = LoggerFactory.getLogger(BaijiListener.class);
    private static final Object _classLock = new Object();

    private final BaijiServletContext _context = BaijiServletContext.INSTANCE;


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        Class<?> serviceClass = getServiceClass(context);
        ServiceConfig serviceConfig = buildServiceConfig(context);
        HttpRequestRouter router = new BaijiHttpRequestRouter(serviceConfig, serviceClass);
        _context.setRequestRouter(router);

        registerService(context);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        synchronized (_classLock) {
            ServiceRegistry registry = _context.getServiceRegistry();
            if (registry != null) {
                registry.stop();
                _context.setServiceRegistry(null);
            }
        }
    }

    private Class<?> getServiceClass(ServletContext context) {
        String serviceClassName = context.getInitParameter(SERVICE_CLASS_PARAM);

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

    private ServiceConfig buildServiceConfig(ServletContext context) {
        ServiceConfig config = new ServiceConfig();

        String detailedError = context.getInitParameter(DETAILED_ERROR_PARAM);
        if ("true".equalsIgnoreCase(detailedError)) {
            config.setOutputExceptionStackTrace(true);
        }

        String reuseService = context.getInitParameter(REUSE_SERVICE_PARAM);
        if (reuseService != null && !"true".equalsIgnoreCase(reuseService)) {
            config.setNewServiceInstancePerRequest(true);
        }

        return config;
    }

    private void registerService(ServletContext context) {
        initializeServiceRegistry(context);

        if (_context == null) {
            return;
        }

        ServiceMetadata metadata = _context.getRequestRouter().getServiceMetaData();
        ServiceInfo serviceInfo = new ServiceInfo.Builder().serviceName(metadata.getServiceName())
                .serviceNamespace(metadata.getServiceNamespace())
                .port(_context.getPort()).contextPath(context.getContextPath())
                .subEnv(_context.getSubEnv()).build();
        _context.getServiceRegistry().addService(serviceInfo);
    }

    private void initializeServiceRegistry(ServletContext context) {
        synchronized (_classLock) {
            String portString = context.getInitParameter(SERVICE_PORT_PARAM);
            if (portString != null && !portString.isEmpty()) {
                try {
                    _context.setPort(Integer.valueOf(portString));
                } catch (NumberFormatException ex) {
                    _logger.error("Invalid " + SERVICE_PORT_PARAM + " value: " + portString);
                    return;
                }
            }

            _context.setSubEnv(context.getInitParameter(SUB_ENV_PARAM));

            String serviceUrl = context.getInitParameter(ETCD_SERVICE_URL_PARAM);
            if (serviceUrl == null || serviceUrl.isEmpty()) {
                return;
            }

            ServiceRegistry serviceRegistry = new EtcdServiceRegistry(serviceUrl);
            serviceRegistry.run();
            _context.setServiceRegistry(serviceRegistry);
        }
    }
}
