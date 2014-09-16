package com.ctriposs.baiji.rpc.server.netty;

import com.ctriposs.baiji.rpc.server.HttpRequestRouter;
import com.ctriposs.baiji.rpc.server.HttpResponseWrapper;
import com.ctriposs.baiji.rpc.server.RequestContext;
import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class ServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger _logger = LoggerFactory.getLogger(ServerHandler.class);

    private final HttpRequestRouter _requestRouter;

    public ServerHandler(HttpRequestRouter requestRouter) {
        super(true);
        Preconditions.checkNotNull(requestRouter, "Request router can not be null.");
        _requestRouter = requestRouter;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        RequestContext requestContext = createRequestContext(fullHttpRequest);
        HttpResponseWrapper responseWrapper = createResponseWrapper(channelHandlerContext, fullHttpRequest);
        _requestRouter.process(requestContext, responseWrapper);
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

    private RequestContext createRequestContext(FullHttpRequest request) throws URISyntaxException {
        RequestContext context = new RequestContext();
        context.RequestBody = new ByteBufInputStream(request.content());
        if (request.headers() != null) {
            context.RequestHeaders = new HashMap<String, String>();
            for (Map.Entry<String, String> headerEntry : request.headers()) {
                context.RequestHeaders.put(headerEntry.getKey(), headerEntry.getValue());
            }
        }
        context.RequestMethod = request.getMethod().name();
        URI uri = new URI(request.getUri());
        context.RequestPath = uri.getPath();
        context.RequestProtocol = request.getProtocolVersion().toString();
        context.RequestQueryString = uri.getQuery();
        context.RequestScheme = uri.getScheme();
        return context;
    }

    private HttpResponseWrapper createResponseWrapper(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
        return new NettyHttpResponseWrapper(channelHandlerContext, fullHttpRequest);
    }
}
