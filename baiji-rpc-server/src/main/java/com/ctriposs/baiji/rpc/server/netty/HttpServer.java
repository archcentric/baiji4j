package com.ctriposs.baiji.rpc.server.netty;

import com.ctriposs.baiji.rpc.server.ServiceHost;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HttpServer {

    private static final Logger _logger = LoggerFactory.getLogger(HttpServer.class);

    protected final ServiceHost _serviceHost;
    private final ServerBootstrap _bootstrap;
    private int _maxContentLength = 1048576;
    private ChannelFuture _serverShutdownFuture;

    protected HttpServer(ServerBootstrap bootstrap, ServiceHost serviceHost) {
        _bootstrap = bootstrap;
        _serviceHost = serviceHost;
    }

    public void start() throws Exception {
        startWithoutWaitingForShutdown();
        _serverShutdownFuture.sync();
    }

    public void startWithoutWaitingForShutdown() throws Exception {
        _bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                        .addLast("logger", new LoggingHandler())
                        .addLast("decoder", new HttpRequestDecoder())
                        .addLast("aggregator", new HttpObjectAggregator(_maxContentLength))
                        .addLast("encoder", new HttpResponseEncoder());
                addRouterToPipeline(ch);
            }
        });
        Channel channel = _bootstrap.bind().sync().channel();
        _logger.info("Started netty http module at port: " + channel.localAddress());
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    stop();
                } catch (InterruptedException e) {
                    _logger.error("Error while shutting down.", e);
                }
            }
        }));
        _serverShutdownFuture = channel.closeFuture();
    }

    public void stop() throws InterruptedException {
        _logger.info("Shutting down server.");
        Future<?> acceptorTermFuture = _bootstrap.group().shutdownGracefully();
        Future<?> workerTermFuture = _bootstrap.childGroup().shutdownGracefully();

        _logger.info("Waiting for acceptor threads to stop.");
        acceptorTermFuture.sync();
        _logger.info("Waiting for worker threads to stop.");
        workerTermFuture.sync();
        _logger.info("Shutdown complete.");
    }

    public int getMaxContentLength() {
        return _maxContentLength;
    }

    public void setMaxContentLength(int _maxContentLength) {
        if (_maxContentLength > 0) {
            this._maxContentLength = _maxContentLength;
        }
    }

    protected abstract void addRouterToPipeline(SocketChannel ch);
}
