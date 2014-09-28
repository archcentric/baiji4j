package com.ctriposs.baiji.rpc.server;

import com.ctriposs.baiji.exception.BaijiRuntimeException;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * Created by yqdong on 2014/9/15.
 */
public abstract class BaijiServletBase extends HttpServlet {

    protected static final Logger _logger = LoggerFactory.getLogger(BaijiServlet.class);
    private final BaijiServletContext _context = BaijiServletContext.INSTANCE;
    private HttpRequestRouter _router;

    @Override
    public void init() {
        _router = _context.getRequestRouter();
    }

    protected void processRequest(ServletRequest req, ServletResponse resp)
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
        environment.RequestScheme = uri.getScheme();
        return environment;
    }

    private HttpResponseWrapper createResponseWrapper(HttpServletResponse response) {
        return new HttpServletResponseWrapper(response);
    }
}
