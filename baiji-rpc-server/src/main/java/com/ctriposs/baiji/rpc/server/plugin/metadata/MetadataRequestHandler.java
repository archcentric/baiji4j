package com.ctriposs.baiji.rpc.server.plugin.metadata;

import com.ctriposs.baiji.rpc.server.*;
import com.ctriposs.baiji.rpc.server.handler.RequestHandler;
import com.ctriposs.baiji.util.VersionUtils;
import com.google.common.net.HttpHeaders;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * Created by yqdong on 2014/9/19.
 */
public class MetadataRequestHandler implements RequestHandler {

    @Override
    public void handle(ServiceHost host, HttpRequestWrapper request, HttpResponseWrapper response) throws Exception {
        String[] pathSegs = request.requestPath().substring(1).split("/");
        String firstSeg = pathSegs[0].toLowerCase();

        if (pathSegs.length == 1) {
            if ("metadata".equals(firstSeg)) {
                renderFullMetadata(host, response);
            }
            return;
        }

        if (!"metadata".equals(pathSegs[1])) {
            return;
        }

        renderFullMetadata(host, response);
    }

    private void renderFullMetadata(ServiceHost host, HttpResponseWrapper response) throws IOException {
        response.setHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
        OutputStreamWriter writer = new OutputStreamWriter(response.getResponseStream());
        ServiceMetadata metadata = host.getServiceMetaData();
        writer.write("Service Name: " + metadata.getServiceName() + "\n");
        writer.write("Service Namespace: " + metadata.getServiceNamespace() + "\n");
        writer.write("Baiji Version: " + VersionUtils.getPackageVersion(ServiceHost.class) + "\n");
        writer.write("CodeGen Version: " + metadata.getCodeGeneratorVersion() + "\n");
        writer.write("\n");
        writer.write("Supported Operations:\n");
        for (Map.Entry<RequestPath, OperationHandler> entry : metadata.getOperationHandlers().entrySet()) {
            OperationHandler handler = entry.getValue();
            writer.write(String.format("\t%s(%s)\n", handler.getMethodName(),
                    handler.getMethod().getParameterTypes()[0].getName()));
        }
        writer.flush();
        response.sendResponse();
    }
}
