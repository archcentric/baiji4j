package com.ctriposs.baiji.rpc.server.handler;

import com.ctriposs.baiji.rpc.server.HttpRequestWrapper;
import com.ctriposs.baiji.rpc.server.HttpResponseWrapper;
import com.ctriposs.baiji.rpc.server.ServiceHost;

/**
 * Created by yqdong on 2014/9/17.
 */
public interface ExceptionHandler {

    void handle(ServiceHost host, HttpRequestWrapper request, HttpResponseWrapper response,
                Exception ex);
}
