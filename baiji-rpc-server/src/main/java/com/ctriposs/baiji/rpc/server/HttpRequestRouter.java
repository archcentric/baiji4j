package com.ctriposs.baiji.rpc.server;

public interface HttpRequestRouter {

    ServiceMetadata getServiceMetaData();

    void process(RequestContext request, HttpResponseWrapper responseWriter);
}
