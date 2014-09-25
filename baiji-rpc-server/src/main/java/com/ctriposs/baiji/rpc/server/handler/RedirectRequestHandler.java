package com.ctriposs.baiji.rpc.server.handler;

import com.ctriposs.baiji.rpc.server.HttpRequestWrapper;
import com.ctriposs.baiji.rpc.server.HttpResponseWrapper;
import com.ctriposs.baiji.rpc.server.ServiceHost;
import com.ctriposs.baiji.rpc.server.util.UrlUtil;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;

/**
 * Created by yqdong on 2014/9/25.
 */
public class RedirectRequestHandler implements RequestHandler {

    private final String _targetUrl;
    private final boolean _relative;

    public RedirectRequestHandler(String targetUrl, boolean relative) {
        _targetUrl = targetUrl;
        _relative = relative;
    }

    @Override
    public void handle(ServiceHost host, HttpRequestWrapper request, HttpResponseWrapper response) throws Exception {
        String absoluteUrl = _relative
                ? UrlUtil.getAbsoluteUrl(request.requestUrl(), request.requestPath(), _targetUrl)
                : _targetUrl;
        writeRedirectResponse(response, absoluteUrl);
    }

    public static void writeRedirectResponse(HttpResponseWrapper response, String targetUrl) {
        response.setStatus(HttpStatus.SC_MOVED_TEMPORARILY);
        response.setHeader(HttpHeaders.LOCATION, targetUrl);
        response.sendResponse();
    }
}
