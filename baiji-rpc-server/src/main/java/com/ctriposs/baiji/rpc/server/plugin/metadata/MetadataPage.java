package com.ctriposs.baiji.rpc.server.plugin.metadata;

import com.ctriposs.baiji.rpc.server.HttpRequestWrapper;
import com.ctriposs.baiji.rpc.server.HttpResponseWrapper;
import com.ctriposs.baiji.rpc.server.ServiceHost;

/**
 * Created by yqdong on 2014/9/25.
 */
public interface MetadataPage {

    void render(ServiceHost host, HttpRequestWrapper request, HttpResponseWrapper response) throws Exception;
}
