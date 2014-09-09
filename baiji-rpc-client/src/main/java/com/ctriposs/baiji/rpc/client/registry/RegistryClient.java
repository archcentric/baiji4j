package com.ctriposs.baiji.rpc.client.registry;

import java.util.List;

/**
 * Client used to access service registry.
 */
public interface RegistryClient {

    /**
     * Gets all instances of the given service from registry.
     *
     * @param serviceName the name of the service
     * @param serviceNamespace the namespace of the service
     * @param subEnv the sub environment of the instances to be fetched. nullable.
     * @return
     */
    List<InstanceInfo> getServiceInstances(String serviceName, String serviceNamespace, String subEnv);
}
