package com.ctriposs.baiji.rpc.server.netty;

import com.ctriposs.baiji.rpc.common.logging.Logger;
import com.ctriposs.baiji.rpc.common.logging.LoggerFactory;
import com.ctriposs.baiji.rpc.server.HttpRequestWrapper;
import com.ctriposs.baiji.rpc.server.HttpResponseWrapper;
import com.ctriposs.baiji.rpc.server.ServiceHost;
import com.google.common.base.Preconditions;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

import java.net.SocketException;
import java.net.URISyntaxException;

public class ServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger _logger = LoggerFactory.getLogger(ServerHandler.class);

    private final ServiceHost _serviceHost;

    public ServerHandler(ServiceHost serviceHost) {
        super(true);
        Preconditions.checkNotNull(serviceHost, "Service host can not be null.");
        _serviceHost = serviceHost;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        HttpRequestWrapper httpRequestWrapper = createRequestContext(channelHandlerContext, fullHttpRequest);
        HttpResponseWrapper responseWrapper = createResponseWrapper(channelHandlerContext, fullHttpRequest);
        _serviceHost.processRequest(httpRequestWrapper, responseWrapper);
    }

    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof SocketException) {
            // SocketExceptions are normal, ignore them
        } else {
            _logger.warn("Exception caught in netty handler.", cause);
        }
    }

    private HttpRequestWrapper createRequestContext(ChannelHandlerContext channelHandlerContext, FullHttpRequest request) throws URISyntaxException {
        return new NettyHttpRequestWrapper(channelHandlerContext, request);
    }

    private HttpResponseWrapper createResponseWrapper(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
        return new NettyHttpResponseWrapper(channelHandlerContext, fullHttpRequest);
    }
}
