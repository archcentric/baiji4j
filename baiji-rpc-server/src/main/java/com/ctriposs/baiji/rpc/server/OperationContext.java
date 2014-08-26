package com.ctriposs.baiji.rpc.server;

import com.ctriposs.baiji.specific.SpecificRecord;

public class OperationContext {

    private RequestContext _environment;
    private String _operationName;
    private SpecificRecord _requestObject;

    public OperationContext(RequestContext environment, String operationName, SpecificRecord requestObject) {
        _environment = environment;
        _operationName = operationName;
        _requestObject = requestObject;
    }

    public RequestContext get_environment() {
        return this._environment;
    }

    public String getOperationName() {
        return this._operationName;
    }

    public SpecificRecord getRequestObject() {
        return this._requestObject;
    }
}
