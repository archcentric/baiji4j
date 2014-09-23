package com.ctriposs.baiji.rpc.server.filter;

import com.ctriposs.baiji.rpc.server.HttpRequestWrapper;
import com.ctriposs.baiji.rpc.server.HttpResponseWrapper;
import com.ctriposs.baiji.rpc.server.ServiceHost;

/**
 * Defines a request filter to be executed after deserializing request object and before executing the operation.
 *
 * Created by yqdong on 2014/9/17.
 */
public interface RequestFilter {

    void apply(ServiceHost host, HttpRequestWrapper request, HttpResponseWrapper response);
}
