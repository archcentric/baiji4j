package com.ctriposs.baiji.rpc.server.netty;

import com.ctriposs.baiji.rpc.server.HttpRequestRouter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.socket.SocketChannel;

public class BlockingHttpServer extends HttpServer {

    BlockingHttpServer(ServerBootstrap bootstrap, HttpRequestRouter httpRequestRouter) {
        super(bootstrap, httpRequestRouter);
    }

    @Override
    protected void addRouterToPipeline(SocketChannel ch) {
        ch.pipeline().addLast("router", new ServerHandler(_requestRouter));
    }
}
