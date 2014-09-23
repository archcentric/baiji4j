package com.ctriposs.baiji.rpc.client.filter;

import org.apache.http.HttpResponse;

/**
 * Created by yqdong on 2014/9/22.
 */
public interface HttpResponseFilter {

    void apply(HttpResponse response);
}
