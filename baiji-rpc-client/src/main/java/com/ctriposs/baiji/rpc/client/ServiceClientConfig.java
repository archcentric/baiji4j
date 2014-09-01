package com.ctriposs.baiji.rpc.client;

public class ServiceClientConfig {

    private String _appId;

    /**
     * Initialize an empty {@link ServiceClientConfig} instance.
     */
    public ServiceClientConfig() {
    }

    /**
     * Initialize a {@link ServiceClientConfig} instance with configurations.
     *
     * @param appId the App ID of the application using the client
     */
    public ServiceClientConfig(String appId) {
        _appId = appId;
    }

    /**
     * Gets the App ID of the application using the client.
     *
     * @return the App ID of the application using the client
     */
    public String getAppId() {
        return _appId;
    }

    /**
     * Sets the App ID of the application using the client.
     */
    public void setAppId(String appId) {
        this._appId = appId;
    }
}
