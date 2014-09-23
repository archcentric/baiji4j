package com.ctriposs.baiji.rpc.server;

import java.util.HashMap;
import java.util.Map;

/**
 * Meta data of the service
 */
public class ServiceMetadata {

    private String _serviceName;

    private String _serviceNamespace;

    private String _codeGeneratorVersion;

    private final Map<RequestPath, OperationHandler> _operationHandlers = new HashMap<RequestPath, OperationHandler>();

    public String getServiceName() {
        return _serviceName;
    }

    public void setServiceName(String serviceName) {
        this._serviceName = serviceName;
    }

    public String getServiceNamespace() {
        return _serviceNamespace;
    }

    public void setServiceNamespace(String serviceNamespace) {
        this._serviceNamespace = serviceNamespace;
    }

    public String getCodeGeneratorVersion() {
        return _codeGeneratorVersion;
    }

    public void setCodeGeneratorVersion(String codeGeneratorVersion) {
        this._codeGeneratorVersion = codeGeneratorVersion;
    }

    public boolean hasOperation(String operationName) {
        return _operationHandlers.containsKey(new RequestPath(operationName));
    }

    public Map<RequestPath, OperationHandler> getOperationHandlers() {
        return _operationHandlers;
    }

    public void registerOperationHandler(String operationName, OperationHandler operationHandler) {
        _operationHandlers.put(new RequestPath(operationName), operationHandler);
    }

    public OperationHandler getOperationHandler(String operationName) {
        return _operationHandlers.get(new RequestPath(operationName));
    }
}
