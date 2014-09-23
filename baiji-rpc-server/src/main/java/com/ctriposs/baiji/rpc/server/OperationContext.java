package com.ctriposs.baiji.rpc.server;

import com.ctriposs.baiji.specific.SpecificRecord;

public class OperationContext {

    private HttpRequestWrapper _request;
    private SpecificRecord _requestObject;

    public OperationContext(HttpRequestWrapper request, SpecificRecord requestObject) {
        _request = request;
        _requestObject = requestObject;
    }

    public HttpRequestWrapper getRequest() {
        return this._request;
    }


    public SpecificRecord getRequestObject() {
        return this._requestObject;
    }
}
