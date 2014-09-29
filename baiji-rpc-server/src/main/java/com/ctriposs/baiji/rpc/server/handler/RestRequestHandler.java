package com.ctriposs.baiji.rpc.server.handler;

import com.ctriposs.baiji.rpc.server.HttpRequestWrapper;
import com.ctriposs.baiji.rpc.server.ServiceHost;

/**
 * Created by yqdong on 2014/9/19.
 */
public class RestRequestHandler extends ServiceRequestHandlerBase {

    @Override
    protected boolean processOperationData(ServiceHost host, HttpRequestWrapper request) {
        // Extract path
        String path = request.requestPath();
        if (path == null || path.isEmpty()) {
            return false;
        }
        while (path.startsWith("/")) {
            path = path.substring(1); // Remove the beginning "/"
        }
        while (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        String[] keyBase = path.split("/");
        if (keyBase.length != 1) {
            return false;
        }

        // Extract "extension" for content type
        int extStart = keyBase[0].lastIndexOf(".");
        if (extStart != -1) {
            request.setOperationName(keyBase[0].substring(0, extStart));
            String ext = keyBase[0].substring(extStart + 1);
            String contentType = host.getConfig().contentFormatConfig.getContentTypeFromExt(ext);
            request.setResponseContentType(contentType);
        } else {
            request.setOperationName(keyBase[0]);
        }

        return true;
    }
}
