package com.ctriposs.baiji.rpc.server.netty;

import com.ctriposs.baiji.rpc.server.ServiceHost;
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

    public NonBlockingHttpServerBuilder serviceHost(ServiceHost serviceHost, int executorThreadCount) {
        this._serviceHost = serviceHost;
        this._executorThreadCount = executorThreadCount;
        return this;
    }

    @Override
    protected NonBlockingHttpServer createServer() {
        return new NonBlockingHttpServer(_nettyBootstrap, _serviceHost, _executorThreadCount);
    }

    @Override
    protected void configureBootstrap() {
        _nettyBootstrap.group(new NioEventLoopGroup(_selectorCount))
                .channel(NioServerSocketChannel.class);
    }
}
