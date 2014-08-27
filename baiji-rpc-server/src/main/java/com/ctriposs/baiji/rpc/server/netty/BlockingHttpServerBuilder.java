package com.ctriposs.baiji.rpc.server.netty;

import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.oio.OioServerSocketChannel;

public class BlockingHttpServerBuilder extends HttpServerBuilder<BlockingHttpServerBuilder, BlockingHttpServer> {

    private int workerCount = 200;

    public BlockingHttpServerBuilder(int port) {
        super(port);
    }

    public BlockingHttpServerBuilder withWorkerCount(int workerCount) {
        this.workerCount = workerCount;
        return this;
    }

    @Override
    protected BlockingHttpServer createServer() {
        return new BlockingHttpServer(_nettyBootstrap, _requestRouter);
    }

    @Override
    protected void configureBootstrap() {
        _nettyBootstrap.group(new OioEventLoopGroup(workerCount))
                .channel(OioServerSocketChannel.class);
    }
}
