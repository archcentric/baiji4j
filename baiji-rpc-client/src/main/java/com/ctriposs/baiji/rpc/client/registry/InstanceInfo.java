package com.ctriposs.baiji.rpc.client.registry;

public class InstanceInfo {

    private String _serviceUrl;

    private boolean _up;

    public String getServiceUrl() {
        return _serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this._serviceUrl = serviceUrl;
    }

    public boolean isUp() {
        return _up;
    }

    public void setUp(boolean up) {
        this._up = up;
    }
}
