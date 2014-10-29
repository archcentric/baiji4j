package com.ctriposs.baiji.rpc.client.registry;

import com.ctriposs.etcd.CEtcdClient;
import com.ctriposs.etcd.CEtcdClientException;
import com.ctriposs.etcd.CEtcdNode;
import com.ctriposs.etcd.CEtcdResult;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Client used to access etcd service registry.
 */
public class EtcdRegistryClient implements RegistryClient {

    private static final String BASE_KEY = "/soa4j";

    private final CEtcdClient _client;

    public EtcdRegistryClient(URI uri) {
        _client = new CEtcdClient(uri);
    }

    /**
     * Gets the metadata of the given service.
     *
     * @param serviceName      the name of the service
     * @param serviceNamespace the namespace of the service
     * @return
     */
    public ServiceInfo getServiceInfo(String serviceName, String serviceNamespace) {
        return new ServiceInfo(true, "N/A");
    }

    /**
     * Gets all instances of the given service from registry.
     *
     * @param serviceName      the name of the service
     * @param serviceNamespace the namespace of the service
     * @param subEnv           the sub environment of the instances to be fetched. nullable.
     * @return
     */
    @Override
    public List<InstanceInfo> getServiceInstances(String serviceName, String serviceNamespace, String subEnv) {

        String key = getServiceKey(serviceName, serviceNamespace, subEnv);
        CEtcdResult result;
        try {
            result = _client.listChildren(key, true);
        } catch (CEtcdClientException e) {
            return null;
        }
        if (result == null || result.node == null || result.node.nodes == null) {
            return null;
        }

        List<InstanceInfo> instances = new ArrayList<InstanceInfo>();
        for (CEtcdNode node : result.node.nodes) {
            if (!node.dir || node.nodes == null) {
                continue;
            }
            InstanceInfo instance = new InstanceInfo();
            for (CEtcdNode propertyNode : node.nodes) {
                String propertyName = getPropertyName(propertyNode.key);
                switch (propertyName) {
                    case "url":
                        instance.setServiceUrl(propertyNode.value);
                        break;
                    case "up":
                        boolean up = false;
                        if (propertyNode.value != null) {
                            up = Boolean.valueOf(propertyNode.value);
                        }
                        instance.setUp(up);
                        break;
                }
            }
            if (instance.getServiceUrl() != null) {
                instances.add(instance);
            }
        }
        return instances;
    }

    private static String getServiceKey(String serviceName, String serviceNamespace, String subEnv) {
        String key = BASE_KEY + "/" + normalizeKey(serviceName) + "/" + normalizeKey(serviceNamespace);
        if (subEnv != null) {
            key += "/" + normalizeKey(subEnv);
        }
        return key;
    }

    private static String normalizeKey(String key) {
        return key != null ? key.replace('/', '_') : null;
    }

    private static String getPropertyName(String key) {
        int lastSlashIndex = key.lastIndexOf('/');
        return key.substring(lastSlashIndex + 1);
    }
}
