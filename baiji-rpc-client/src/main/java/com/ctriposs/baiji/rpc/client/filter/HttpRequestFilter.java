package com.ctriposs.baiji.rpc.client.filter;

import org.apache.http.client.methods.HttpRequestBase;

/**
 * Created by yqdong on 2014/9/22.
 */
public interface HttpRequestFilter {

    void apply(HttpRequestBase request);
}
