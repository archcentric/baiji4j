package com.ctriposs.baiji.rpc.client.registry;

import java.util.List;

public interface RegistryClient {

    List<InstanceInfo> getServiceInstances(String serviceName, String serviceNamespace, String subEnv);
}
