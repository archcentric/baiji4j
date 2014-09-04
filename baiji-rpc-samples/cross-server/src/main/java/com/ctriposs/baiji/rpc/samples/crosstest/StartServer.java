package com.ctriposs.baiji.rpc.samples.crosstest;

import com.ctriposs.baiji.rpc.server.BaijiHttpRequestRouter;
import com.ctriposs.baiji.rpc.server.HttpRequestRouter;
import com.ctriposs.baiji.rpc.server.ServiceConfig;
import com.ctriposs.baiji.rpc.server.netty.BlockingHttpServerBuilder;
import io.netty.channel.ChannelOption;

public class StartServer {

    public static void main(String[] args) throws Exception {
        ServiceConfig config = new ServiceConfig();
        HttpRequestRouter router = new BaijiHttpRequestRouter(config, TestServiceImpl.class);

        BlockingHttpServerBuilder builder = new BlockingHttpServerBuilder(8113);

        builder.requestRouter(router)
                .withWorkerCount(10)
                .serverSocketOption(ChannelOption.SO_BACKLOG, 100)
                .clientSocketOption(ChannelOption.TCP_NODELAY, true)
                .build().startWithoutWaitingForShutdown();
    }
}
