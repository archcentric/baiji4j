package com.ctriposs.baiji.rpc.server.handler;

import com.ctriposs.baiji.rpc.server.HttpRequestWrapper;
import com.ctriposs.baiji.rpc.server.HttpResponseWrapper;
import com.ctriposs.baiji.rpc.server.ServiceHost;

public interface RequestHandler {

    void handle(ServiceHost host, HttpRequestWrapper request, HttpResponseWrapper response)
            throws Exception;
}
