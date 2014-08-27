package com.ctriposs.baiji.rpc.server.netty;

import com.ctriposs.baiji.rpc.server.HttpRequestRouter;
import com.google.common.base.Preconditions;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;

public abstract class HttpServerBuilder<T extends HttpServerBuilder, S extends HttpServer> {

    protected final ServerBootstrap _nettyBootstrap;
    protected HttpRequestRouter _requestRouter;
    protected int _maxContentLength = 1024 * 1024;

    protected HttpServerBuilder(int serverPort) {
        _nettyBootstrap = new ServerBootstrap();
        _nettyBootstrap.localAddress(serverPort);
    }

    public T requestRouter(HttpRequestRouter requestRouter) {
        _requestRouter = requestRouter;
        return returnBuilder();
    }

    public T maxContentLength(int length) {
        if (length > 0) {
            _maxContentLength = length;
        }
        return returnBuilder();
    }

    public <O> T serverSocketOption(ChannelOption<O> channelOption, O value) {
        _nettyBootstrap.option(channelOption, value);
        return returnBuilder();
    }

    public <O> T clientSocketOption(ChannelOption<O> channelOption, O value) {
        _nettyBootstrap.childOption(channelOption, value);
        return returnBuilder();
    }

    public S build() {
        validate();
        configureBootstrap();
        S server = createServer();
        server.setMaxContentLength(_maxContentLength);
        return server;
    }

    protected abstract S createServer();

    protected abstract void configureBootstrap();

    protected void validate() {
        Preconditions.checkState(_requestRouter != null, "Request router is not set.");
    }

    private T returnBuilder() {
        return (T) this;
    }
}
