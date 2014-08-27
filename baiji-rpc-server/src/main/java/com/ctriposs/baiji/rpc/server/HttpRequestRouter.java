package com.ctriposs.baiji.rpc.server;

public interface HttpRequestRouter {

    void process(RequestContext request, HttpResponseWrapper responseWriter);
}
