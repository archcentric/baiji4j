package com.ctriposs.baiji.rpc.server;

import com.ctriposs.baiji.rpc.server.stats.ServiceStats;

/**
 * Created by yqdong on 2014/9/17.
 */
public interface ServiceHost {

    HostConfig getConfig();

    ServiceMetadata getServiceMetaData();

    ServiceStats getServiceStats();

    void processRequest(HttpRequestWrapper request, HttpResponseWrapper response);
}
