package com.ctriposs.baiji.rpc.server;

import java.io.IOException;
import java.io.OutputStream;

public interface HttpResponseWrapper {

    void setHeader(String name, String value);

    void setStatus(int status);

    ExecutionResult getExecutionResult();

    OutputStream getResponseStream() throws IOException;

    void sendResponse();

    boolean isResponseSent();
}
