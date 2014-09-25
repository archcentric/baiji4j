package com.ctriposs.baiji.rpc.server.handler;

import com.ctriposs.baiji.rpc.server.HttpRequestWrapper;
import com.ctriposs.baiji.rpc.server.HttpResponseWrapper;
import com.ctriposs.baiji.rpc.server.ServiceHost;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;

import java.net.URI;
import java.net.URISyntaxException;

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
        String absoluteUrl = getAbsoluteUrl(request.requestUrl(), request.requestPath(), _targetUrl, _relative);
        writeRedirectResponse(response, absoluteUrl);
    }

    private static String getAbsoluteUrl(String requestUrl, String requestPath,
                                         String targetUrl, boolean relative)
            throws URISyntaxException {
        if (!relative) {
            return targetUrl;
        }

        URI requestUri = new URI(requestUrl);
        if (targetUrl.startsWith("/")) {
            return requestUri.relativize(new URI(targetUrl)).toString();
        } else if (targetUrl.startsWith("~/")) {
            String baseUrl = requestUrl.substring(0, requestUrl.length() - requestPath.length());
            return baseUrl + targetUrl.substring(1);
        } else {
            return requestUrl.substring(0, requestUrl.lastIndexOf("/") + 1) + targetUrl;
        }
    }

    public static void writeRedirectResponse(HttpResponseWrapper response, String targetUrl) {
        response.setStatus(HttpStatus.SC_MOVED_TEMPORARILY);
        response.setHeader(HttpHeaders.LOCATION, targetUrl);
        response.sendResponse();
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getAbsoluteUrl("/sayHello", "/sayHello", "~/metadata", true));
    }
}
