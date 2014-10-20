package com.ctriposs.baiji.rpc.server;

/**
 * Created by yqdong on 2014/10/17.
 */
public class ExecutionResult {

    private boolean _validationExceptionThrown;
    private boolean _frameworkExceptionThrown;
    private boolean _serviceExceptionThrown;
    private long _responseSize;

    public boolean validationExceptionThrown() {
        return _validationExceptionThrown;
    }

    public void setValidationExceptionThrown(boolean value) {
        _validationExceptionThrown = value;
    }

    public boolean frameworkExceptionThrown() {
        return _frameworkExceptionThrown;
    }

    public void setFrameworkExceptionThrown(boolean value) {
        _frameworkExceptionThrown = value;
    }

    public boolean serviceExceptionThrown() {
        return _serviceExceptionThrown;
    }

    public void setServiceExceptionThrown(boolean value) {
        _serviceExceptionThrown = value;
    }

    public long responseSize() {
        return _responseSize;
    }

    public void setResponseSize(long size) {
        _responseSize = size;
    }
}
