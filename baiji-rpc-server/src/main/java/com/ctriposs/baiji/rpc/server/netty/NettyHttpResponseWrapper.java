package com.ctriposs.baiji.rpc.server.netty;

import com.ctriposs.baiji.rpc.server.HttpResponseWrapper;
import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;

class NettyHttpResponseWrapper implements HttpResponseWrapper {

    private final ChannelHandlerContext _context;
    private final FullHttpRequest _request;
    private final Map<String, String> _headers = new HashMap<String, String>();
    private ByteBuf _contentBuffer;
    private OutputStream _responseStream;
    private int _status = 200;
    private boolean _responseSent;

    NettyHttpResponseWrapper(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
        _context = channelHandlerContext;
        _request = fullHttpRequest;
    }

    @Override
    public void setHeader(String name, String value) {
        Preconditions.checkState(!_responseSent, "Header can't be updated after response is sent.");
        _headers.put(name, value);
    }

    @Override
    public void setStatus(int status) {
        Preconditions.checkState(!_responseSent, "HTTP status can't be updated after response is sent.");
        _status = status;
    }

    @Override
    public OutputStream getResponseStream() throws IOException {
        Preconditions.checkState(!_responseSent, "No data can be written into response stream after response is sent.");
        if (_responseStream == null) {
            _contentBuffer = _context.alloc().buffer();
            _responseStream = new ByteBufOutputStream(_contentBuffer);
        }
        return _responseStream;
    }

    @Override
    public void sendResponse() {
        ByteBuf content = _contentBuffer;
        if (content == null) {
            content = Unpooled.buffer(0);
        }
        FullHttpResponse response = new DefaultFullHttpResponse(_request.getProtocolVersion(),
                HttpResponseStatus.valueOf(_status), content);

        boolean keepAlive = HttpHeaders.isKeepAlive(_request);
        if (keepAlive) {
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }

        for (Map.Entry<String, String> header : _headers.entrySet()) {
            response.headers().set(header.getKey(), header.getValue());
        }

        ChannelFuture writeFuture = _context.write(response);

        if (!keepAlive) {
            writeFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
