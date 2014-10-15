package com.ctriposs.baiji.rpc.server.handler;

import com.ctriposs.baiji.rpc.common.logging.Logger;
import com.ctriposs.baiji.rpc.common.logging.LoggerFactory;
import com.ctriposs.baiji.rpc.server.HttpRequestWrapper;
import com.ctriposs.baiji.rpc.server.HttpResponseWrapper;
import com.ctriposs.baiji.rpc.server.ServiceHost;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;

/**
 * Created by yqdong on 2014/9/19.
 */
public class NotFoundRequestHandler implements RequestHandler {

    private static final Logger _logger = LoggerFactory.getLogger(NotFoundRequestHandler.class);

    @Override
    public void handle(ServiceHost host, HttpRequestWrapper request, HttpResponseWrapper response)
            throws Exception {
        _logger.warn(String.format("%s request not found: %s", request.clientIp(), request.requestPath()));

        StringBuilder text = new StringBuilder();
        if (host.getConfig().debugMode) {
            text.append("Handler for Request not found: \n\n")
                .append("Request.HttpMethod: " + request.httpMethod() + "\n")
                .append("Request.RequestPath: " + request.requestPath() + "\n")
                .append("Request.QueryString: " + (request.queryString() != null ? request.queryString() : "") + "\n");
        } else {
            text.append("404");
        }
        response.getResponseStream().write( text.toString().getBytes("UTF-8"));

        response.setHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
        response.setStatus(HttpStatus.SC_NOT_FOUND);
        response.sendResponse();
    }
}
