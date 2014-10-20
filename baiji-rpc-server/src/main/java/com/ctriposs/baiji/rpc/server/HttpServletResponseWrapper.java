package com.ctriposs.baiji.rpc.server;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class HttpServletResponseWrapper implements HttpResponseWrapper {

    private final HttpServletResponse _response;
    private final ExecutionResult _result = new ExecutionResult();
    private boolean _responseSent;

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
    public ExecutionResult getExecutionResult() {
        return _result;
    }

    @Override
    public OutputStream getResponseStream() throws IOException {
        return _response.getOutputStream();
    }

    @Override
    public void sendResponse() {
        _responseSent = true;
    }

    @Override
    public boolean isResponseSent() {
        return _responseSent;
    }
}
