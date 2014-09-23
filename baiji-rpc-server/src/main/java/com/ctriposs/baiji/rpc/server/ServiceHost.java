package com.ctriposs.baiji.rpc.server;

/**
 * Created by yqdong on 2014/9/17.
 */
public interface ServiceHost {

    HostConfig getConfig();

    ServiceMetadata getServiceMetaData();

    void processRequest(HttpRequestWrapper request, HttpResponseWrapper response);
}
