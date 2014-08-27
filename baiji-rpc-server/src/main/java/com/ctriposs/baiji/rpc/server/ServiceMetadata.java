package com.ctriposs.baiji.rpc.server;

/**
 * Meta data of the service
 */
public class ServiceMetadata {

    private String _serviceName;

    private String _serviceNamespace;

    private String _codeGeneratorVersion;

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
}
