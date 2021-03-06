package com.ctriposs.baiji.rpc.server.netty;

import com.ctriposs.baiji.rpc.server.HttpRequestRouter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class NonBlockingHttpServer extends HttpServer {

    private final int _routerExecutorThreads;

    NonBlockingHttpServer(ServerBootstrap bootstrap, HttpRequestRouter requestRouter,
                                    int routerExecutorThreads) {
        super(bootstrap, requestRouter);
        _routerExecutorThreads = routerExecutorThreads;
    }

    @Override
    protected void addRouterToPipeline(SocketChannel ch) {
        EventExecutorGroup eventExecutor = new DefaultEventExecutorGroup(_routerExecutorThreads);
        ch.pipeline().addLast(eventExecutor, "router", new ServerHandler(_requestRouter));
    }
}
