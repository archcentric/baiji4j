package com.ctriposs.baiji.rpc.samples.movie;

import com.ctriposs.baiji.rpc.server.BaijiHttpRequestRouter;
import com.ctriposs.baiji.rpc.server.HttpRequestRouter;
import com.ctriposs.baiji.rpc.server.ServiceConfig;
import com.ctriposs.baiji.rpc.server.netty.BlockingHttpServerBuilder;
import io.netty.channel.ChannelOption;

public final class StartServer {

    public static void main(String[] args) throws Exception {
        ServiceConfig config = new ServiceConfig();
        config.setOutputExceptionStackTrace(true);
        HttpRequestRouter router = new BaijiHttpRequestRouter(config, MovieServiceImpl.class);

        BlockingHttpServerBuilder builder = new BlockingHttpServerBuilder(8112);

        builder.requestRouter(router)
                .withWorkerCount(10)
                .serverSocketOption(ChannelOption.SO_BACKLOG, 100)
                .clientSocketOption(ChannelOption.TCP_NODELAY, true)
                .build().startWithoutWaitingForShutdown();
    }
}
