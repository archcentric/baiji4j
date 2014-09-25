package com.ctriposs.baiji.rpc.server;

import com.ctriposs.baiji.specific.SpecificRecord;
import com.google.common.net.HttpHeaders;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yqdong on 2014/9/19.
 */
public abstract class HttpRequestWrapperBase implements HttpRequestWrapper {

    private String _clientIp;

    private String _operationName;

    private String _responseContentType;

    private SpecificRecord _requestObject;

    private OperationHandler _operationHandler;

    @Override
    public String clientIp() {
        if (_clientIp == null) {
            String xForwardFor = requestHeaders().get(HttpHeaders.X_FORWARDED_FOR);
            String xRealIp =requestHeaders().get("X-Real-IP");
            if (xForwardFor != null) {
                _clientIp = xForwardFor;
            } else if (xRealIp != null) {
                _clientIp = xRealIp;
            } else {
                _clientIp = directClientIp();
            }
        }
        return _clientIp;
    }

    /**
     * The client IP obtained directly from the client connection.
     * @return
     */
    protected abstract String directClientIp();

    @Override
    public String operationName() {
        return _operationName;
    }

    @Override
    public void setOperationName(String operationName) {
        _operationName = operationName;
    }

    @Override
    public String responseContentType() {
        return _responseContentType;
    }

    @Override
    public void setResponseContentType(String contentType) {
        _responseContentType = contentType;
    }

    @Override
    public SpecificRecord requestObject() {
        return _requestObject;
    }

    @Override
    public void setRequestObject(SpecificRecord obj) {
        _requestObject = obj;
    }

    @Override
    public OperationHandler operationHandler() {
        return _operationHandler;
    }

    @Override
    public void setOperationHandler(OperationHandler handler) {
        _operationHandler = handler;
    }

    /**
     * The key-value map of query string.
     *
     * @return
     */
    @Override
    public Map<String, String> queryMap() {
        String queryString = queryString();
        if (queryString == null || queryString.isEmpty()) {
            return new HashMap<String, String>(0);
        }
        Map<String, String> queryMap = new LinkedHashMap<String, String>();
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            try {
                queryMap.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                        URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
            }
        }
        return queryMap;
    }
}
