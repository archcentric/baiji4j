package com.ctriposs.baiji.rpc.server.netty;

import com.ctriposs.baiji.rpc.server.ServiceHost;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.socket.SocketChannel;

public class BlockingHttpServer extends HttpServer {

    BlockingHttpServer(ServerBootstrap bootstrap, ServiceHost serviceHost) {
        super(bootstrap, serviceHost);
    }

    @Override
    protected void addRouterToPipeline(SocketChannel ch) {
        ch.pipeline().addLast("router", new ServerHandler(_serviceHost));
    }
}
