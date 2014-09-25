package com.ctriposs.baiji.rpc.server.plugin.metadata;

import com.ctriposs.baiji.rpc.server.HttpRequestWrapper;
import com.ctriposs.baiji.rpc.server.HttpResponseWrapper;
import com.ctriposs.baiji.rpc.server.ServiceHost;
import com.ctriposs.baiji.rpc.server.handler.RequestHandler;

/**
 * Created by yqdong on 2014/9/19.
 */
public class MetadataRequestHandler implements RequestHandler {

    private static final MetadataPage INDEX_PAGE = new IndexMetadataPage();

    @Override
    public void handle(ServiceHost host, HttpRequestWrapper request, HttpResponseWrapper response) throws Exception {
        String[] pathSegs = request.requestPath().substring(1).split("/");
        String firstSeg = pathSegs[0].toLowerCase();

        if (pathSegs.length == 1) {
            if ("metadata".equals(firstSeg)) {
                INDEX_PAGE.render(host, request, response);
            }
        } else if ("metadata".equals(pathSegs[1])) {
            String operation = request.queryMap().get("op");
            MetadataPage page;
            if (operation == null) {
                page = new ContentFormatMetadataPage(firstSeg);
            } else {
                page = new OperationMetadataPage(firstSeg, operation);
            }
            page.render(host, request, response);
        }
    }
}
