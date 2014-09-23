package com.ctriposs.baiji.rpc.server.plugin.metadata;

import com.ctriposs.baiji.rpc.server.ServiceHost;
import com.ctriposs.baiji.rpc.server.plugin.Plugin;

/**
 * Created by yqdong on 2014/9/19.
 */
public class MetadataPlugin implements Plugin {

    @Override
    public void register(ServiceHost serviceHost) {
        serviceHost.getConfig().requestHandlers.add(new MetadataRequestHandler());
    }
}
