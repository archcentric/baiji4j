package com.ctriposs.baiji.rpc.server.plugin;

import com.ctriposs.baiji.rpc.server.ServiceHost;

/**
 * Created by yqdong on 2014/9/17.
 */
public interface Plugin {

    void register(ServiceHost serviceHost);
}
