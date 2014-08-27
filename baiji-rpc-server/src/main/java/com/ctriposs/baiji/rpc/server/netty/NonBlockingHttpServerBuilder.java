package com.ctriposs.baiji.rpc.server.netty;

import com.ctriposs.baiji.rpc.server.HttpRequestRouter;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NonBlockingHttpServerBuilder extends HttpServerBuilder<NonBlockingHttpServerBuilder, NonBlockingHttpServer> {

    private int _selectorCount;
    private int _executorThreadCount;

    public NonBlockingHttpServerBuilder(int serverPort) {
        super(serverPort);
    }

    public NonBlockingHttpServerBuilder withSelectorCount(int selectorCount) {
        this._selectorCount = selectorCount;
        return this;
    }

    public NonBlockingHttpServerBuilder requestRouter(HttpRequestRouter requestRouter, int executorThreadCount) {
        this._requestRouter = requestRouter;
        this._executorThreadCount = executorThreadCount;
        return this;
    }

    @Override
    protected NonBlockingHttpServer createServer() {
        return new NonBlockingHttpServer(_nettyBootstrap, _requestRouter, _executorThreadCount);
    }

    @Override
    protected void configureBootstrap() {
        _nettyBootstrap.group(new NioEventLoopGroup(_selectorCount))
                .channel(NioServerSocketChannel.class);
    }
}
