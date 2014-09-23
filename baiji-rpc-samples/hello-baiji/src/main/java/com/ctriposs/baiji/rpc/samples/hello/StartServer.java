package com.ctriposs.baiji.rpc.samples.hello;

import com.ctriposs.baiji.rpc.server.BaijiServiceHost;
import com.ctriposs.baiji.rpc.server.HostConfig;
import com.ctriposs.baiji.rpc.server.ServiceHost;
import com.ctriposs.baiji.rpc.server.netty.BlockingHttpServerBuilder;
import io.netty.channel.ChannelOption;

public final class StartServer {

    public static void main(String[] args) throws Exception {
        HostConfig config = new HostConfig();
        config.debugMode = true;
        ServiceHost host = new BaijiServiceHost(config, HelloServiceImpl.class);

        BlockingHttpServerBuilder builder = new BlockingHttpServerBuilder(8111);

        builder.serviceHost(host)
               .withWorkerCount(10)
               .serverSocketOption(ChannelOption.SO_BACKLOG, 100)
               .clientSocketOption(ChannelOption.TCP_NODELAY, true)
               .build().startWithoutWaitingForShutdown();
    }
}
