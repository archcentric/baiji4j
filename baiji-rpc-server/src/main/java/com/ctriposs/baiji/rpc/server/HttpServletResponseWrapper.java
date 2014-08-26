package com.ctriposs.baiji.rpc.server;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class HttpServletResponseWrapper implements HttpResponseWrapper {

    private final HttpServletResponse _response;

    public HttpServletResponseWrapper(HttpServletResponse response) {
        _response = response;
    }

    @Override
    public void setHeader(String name, String value) {
        _response.setHeader(name, value);
    }

    @Override
    public void setStatus(int status) {
        _response.setStatus(status);
    }

    @Override
    public OutputStream getResponseStream() throws IOException {
        return _response.getOutputStream();
    }

    @Override
    public void sendResponse() {
    }
}
