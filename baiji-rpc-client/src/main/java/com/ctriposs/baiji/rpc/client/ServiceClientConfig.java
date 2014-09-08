package com.ctriposs.baiji.rpc.client;

import java.util.HashMap;
import java.util.Map;

public class ServiceClientConfig {

    private String _appId;

    private String _serviceRegistryUrl;

    private String _subEnv;

    private final Map<String, String> _serviceSubEnv = new HashMap<String, String>();

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

    /**
     * Gets the URL of service registry.
     *
     * @return
     */
    public String getServiceRegistryUrl() {
        return _serviceRegistryUrl;
    }

    /**
     * Sets the URL of service registry.
     *
     * @param serviceRegistryUrl
     */
    public void setServiceRegistryUrl(String serviceRegistryUrl) {
        this._serviceRegistryUrl = serviceRegistryUrl;
    }

    /**
     * Gets the sub environment in which the application runs.
     *
     * @return
     */
    public String getSubEnv() {
        return _subEnv;
    }

    /**
     * Sets the sub environment in which the application runs.
     *
     * @param subEnv
     */
    public void setSubEnv(String subEnv) {
        this._subEnv = subEnv;
    }

    /**
     * Gets the sub environment setting specified for the given service.
     *
     * @param serviceName
     * @param serviceNamespace
     */
    public String getServiceSubEnv(String serviceName, String serviceNamespace) {
        String key = serviceName + "|" + serviceNamespace;
        return _serviceSubEnv.get(key);
    }


    /**
     * Sets the target sub environment for the given service.
     *
     * @param serviceName
     * @param serviceNamespace
     * @param subEnv
     */
    public void setServiceSubEnv(String serviceName, String serviceNamespace, String subEnv) {
        String key = serviceName + "|" + serviceNamespace;
        if (subEnv != null && !subEnv.isEmpty()) {
            _serviceSubEnv.put(key, subEnv);
        } else {
            _serviceSubEnv.remove(key);
        }
    }

}
