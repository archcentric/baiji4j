package com.ctriposs.baiji.rpc.server.netty;

import com.ctriposs.baiji.exception.BaijiRuntimeException;
import com.ctriposs.baiji.rpc.server.HttpRequestWrapperBase;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yqdong on 2014/9/19.
 */
public class NettyHttpRequestWrapper extends HttpRequestWrapperBase {

    private final ChannelHandlerContext _channelHandlerContext;
    private final FullHttpRequest _request;
    private final URI _requestUri;
    private InputStream _requestBody;
    private Map<String, String> _requestHeaders;
    private String _directClientIp;

    public NettyHttpRequestWrapper(ChannelHandlerContext channelHandlerContext, FullHttpRequest request) {
        _channelHandlerContext = channelHandlerContext;
        _request = request;
        try {
            _requestUri = new URI(request.getUri());
        } catch (URISyntaxException e) {
            throw new BaijiRuntimeException(e);
        }
    }

    @Override
    public InputStream requestBody() throws IOException {
        if (_requestBody == null) {
            _requestBody = new ByteBufInputStream(_request.content());
        }
        return _requestBody;
    }

    @Override
    public Map<String, String> requestHeaders() {
        if (_requestHeaders == null) {
            Map<String, String> requestHeaders = new HashMap<>();
            for (Map.Entry<String, String> headerEntry : _request.headers()) {
                requestHeaders.put(headerEntry.getKey(), headerEntry.getValue());
            }
            _requestHeaders = requestHeaders;
        }
        return _requestHeaders;
    }

    @Override
    public String httpMethod() {
        return _request.getMethod().name();
    }

    @Override
    public String requestPath() {
        return _requestUri.getPath();
    }

    @Override
    public String httpScheme() {
        return _requestUri.getScheme();
    }

    @Override
    public String queryString() {
        return _requestUri.getQuery();
    }

    @Override
    protected String directClientIp() {
        if (_directClientIp == null) {
            SocketAddress remoteAddress = _channelHandlerContext.channel().remoteAddress();
            if (remoteAddress instanceof InetSocketAddress) {
                _directClientIp = ((InetSocketAddress) remoteAddress).getHostName();
            } else {
                _directClientIp = remoteAddress.toString();
            }
        }
        return _directClientIp;
    }
}
