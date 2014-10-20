package com.ctriposs.baiji.rpc.server.util;

import com.ctriposs.baiji.rpc.common.formatter.ContentFormatter;
import com.ctriposs.baiji.rpc.server.HttpRequestWrapper;
import com.ctriposs.baiji.rpc.server.ServiceHost;
import com.ctriposs.baiji.specific.SpecificRecord;
import org.apache.http.HttpHeaders;

import java.io.IOException;

/**
 * Created by yqdong on 2014/9/19.
 */
public final class RequestUtil {

    private RequestUtil() {
    }

    public static <T extends SpecificRecord> T getRequestObj(HttpRequestWrapper request,
                                                             Class<T> requestType,
                                                             ServiceHost host) {
        String requestContentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        if (requestContentType == null || requestContentType.isEmpty()) {
            requestContentType = request.responseContentType();
        }

        if (requestContentType == null || requestContentType.isEmpty()) {
            throw new RuntimeException("Unknown request content type.");
        }

        ContentFormatter formatter = host.getConfig().contentFormatConfig.getFormatter(requestContentType);
        if (formatter == null) {
            throw new RuntimeException("Unsupported content type: " + requestContentType);
        }

        try {
            T requestObject = formatter.deserialize(requestType, request.requestBody());
            return requestObject;
        } catch (IOException ex) {
            throw new RuntimeException("Unable to deserialize '" + requestContentType + "' request using "
                    + requestType.getName());
        }
    }
}
