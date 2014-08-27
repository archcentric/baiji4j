package com.ctriposs.baiji.rpc.server;

import com.ctriposs.baiji.exception.BaijiRuntimeException;
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

    private static final Logger _logger = LoggerFactory.getLogger(BaijiServlet.class);

    private BaijiHttpRequestRouter _router;

    @Override
    public void init() {

        Class<?> serviceClass = getServiceClass();
        ServiceConfig serviceConfig = buildServiceConfig();
        _router = new BaijiHttpRequestRouter(serviceConfig, serviceClass);
    }

    private Class<?> getServiceClass() {
        ServletConfig servletConfig = getServletConfig();
        String serviceClassName = servletConfig.getInitParameter("service-class");

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

        String detailedError = servletConfig.getInitParameter("detailed-error");
        if ("true".equalsIgnoreCase(detailedError)) {
            config.setOutputExceptionStackTrace(true);
        }

        String reuseService = servletConfig.getInitParameter("reuse-service");
        if (reuseService != null && !"true".equalsIgnoreCase(reuseService)) {
            config.setNewServiceInstancePerRequest(true);
        }

        return config;
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
