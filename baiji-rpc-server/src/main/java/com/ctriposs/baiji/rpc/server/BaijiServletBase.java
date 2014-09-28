package com.ctriposs.baiji.rpc.server;

import com.ctriposs.baiji.rpc.common.logging.Logger;
import com.ctriposs.baiji.rpc.common.logging.LoggerFactory;
import org.apache.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yqdong on 2014/9/15.
 */
public abstract class BaijiServletBase extends HttpServlet {

    protected static final Logger _logger = LoggerFactory.getLogger(BaijiServlet.class);
    protected ServiceHost _serviceHost;

    @Override
    public void init() {
        _serviceHost = BaijiServletContext.INSTANCE.getServiceHost();
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
