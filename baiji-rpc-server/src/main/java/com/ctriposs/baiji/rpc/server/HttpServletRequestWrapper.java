package com.ctriposs.baiji.rpc.server;

import com.ctriposs.baiji.exception.BaijiRuntimeException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yqdong on 2014/9/19.
 */
public class HttpServletRequestWrapper extends HttpRequestWrapperBase {

    private final HttpServletRequest _request;
    private final URI _requestUri;
    private Map<String, String> _requestHeaders;

    public HttpServletRequestWrapper(HttpServletRequest request) {
        _request = request;
        try {
            _requestUri = new URI(request.getRequestURL().toString());
        } catch (URISyntaxException e) {
            throw new BaijiRuntimeException(e);
        }
    }

    @Override
    public InputStream requestBody() throws IOException {
        return _request.getInputStream();
    }

    @Override
    public Map<String, String> requestHeaders() {
        if (_requestHeaders == null) {
            Map<String, String> requestHeaders = new HashMap<>();
            Enumeration<String> headerNames = _request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String headerName = headerNames.nextElement();
                    requestHeaders.put(headerName.toLowerCase(), _request.getHeader(headerName));
                }
            }
            _requestHeaders = requestHeaders;
        }
        return _requestHeaders;
    }

    @Override
    public String httpMethod() {
        return _request.getMethod();
    }

    @Override
    public String requestUrl() {
        return _request.getRequestURL().toString();
    }

    @Override
    public String contextPath() {
        return _request.getContextPath();
    }

    @Override
    public String requestPath() {
        return _request.getPathInfo();
    }

    @Override
    public String httpScheme() {
        return _requestUri.getScheme();
    }

    @Override
    public String queryString() {
        return _request.getQueryString();
    }

    @Override
    protected String directClientIp() {
        return _request.getRemoteAddr();
    }
}
