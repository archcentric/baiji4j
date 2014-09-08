package com.ctriposs.baiji.rpc.server.registry;

import com.ctriposs.etcd.CEtcdClient;
import com.ctriposs.etcd.CEtcdClientException;
import com.ctriposs.etcd.CEtcdResult;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EtcdServiceRegistry implements ServiceRegistry {

    private static final int DEFAULT_HEARTBEAT_INTERVAL = 30 * 1000; // In milliseconds
    private static final int HEARTBEAT_MISS_TOLERANCE = 3;

    private final List<ServiceInfo> _services = new LinkedList<ServiceInfo>();
    private final CEtcdClient _client;
    private final Timer _heartbeatTimer = new Timer("EtcdServiceRegistry_Heartbeat", true);
    private int _heartbeatInterval = DEFAULT_HEARTBEAT_INTERVAL;
    private int _registrationTtl = _heartbeatInterval * HEARTBEAT_MISS_TOLERANCE / 1000;
    private boolean _running;

    public EtcdServiceRegistry(String serviceUrl) {
        if (serviceUrl == null || serviceUrl.isEmpty()) {
            throw new IllegalArgumentException("serviceUrl can't be null or empty.");
        }
        _client = new CEtcdClient(URI.create(serviceUrl));
    }

    @Override
    public void addService(ServiceInfo serviceInfo) {
        if (serviceInfo == null) {
            throw new IllegalArgumentException("serviceInfo can't be null.");
        }
        _services.add(serviceInfo);
    }

    @Override
    public int getHeartbeatInterval() {
        return _heartbeatInterval;
    }

    @Override
    public void setHeartbeatInterval(int interval) {
        if (interval > 0) {
            _heartbeatInterval = interval;
            _registrationTtl = interval * HEARTBEAT_MISS_TOLERANCE / 1000;
        }
    }

    @Override
    public void run() {
        if (_running) {
            return;
        }
        _running = true;
        _heartbeatTimer.scheduleAtFixedRate(new HeartbeatTask(), 0, _heartbeatInterval);
    }

    @Override
    public void stop() {
        if (!_running) {
            return;
        }
        _running = false;
        _heartbeatTimer.cancel();
    }

    private class HeartbeatTask extends TimerTask {

        private static final String BASE_KEY = "/soa4j";
        private static final String URL_KEY = "/url";
        private static final String UP_KEY = "/up";

        private final Map<ServiceInfo, Boolean> _registrationFlags = new ConcurrentHashMap<>();
        private final Map<ServiceInfo, Boolean> _lastStatus = new ConcurrentHashMap<>();

        @Override
        public void run() {
            List<ServiceInfo> services = new ArrayList<ServiceInfo>(_services);
            for (ServiceInfo service : services) {
                try {
                    Boolean registrationFlag = _registrationFlags.get(service);
                    if (!Boolean.TRUE.equals(registrationFlag) || service.isDirty()) {
                        if (register(service)) {
                            service.setDirty(false);
                            _registrationFlags.put(service, Boolean.TRUE);
                        }
                    } else {
                        if (!heartbeat(service)) {
                            register(service);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        private boolean register(ServiceInfo service) {
            try {
                CEtcdResult result;

                String serviceKey = getServiceKey(service);
                result = _client.get(serviceKey);
                if (result == null || result.node == null) {
                    try {
                        _client.createDir(serviceKey);
                    } catch (CEtcdClientException ex) {
                        ex.printStackTrace();
                        return false;
                    }
                }

                String instanceKey = getInstanceKey(service);
                result = _client.get(instanceKey);
                boolean nodeExisted =result != null && result.node != null;
                _client.createDir(instanceKey, _registrationTtl, nodeExisted);

                _client.set(instanceKey + URL_KEY, service.getServiceUrl());
                Boolean status = getStatus(service);
                _client.set(instanceKey + UP_KEY, String.valueOf(status));
                _lastStatus.put(service, status);
                for (Map.Entry<String, String> metadata : service.getMetadata().entrySet()) {
                    _client.set(metadata.getKey(), metadata.getValue());
                }

                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }

        private boolean heartbeat(ServiceInfo service) throws CEtcdClientException {
            try {
                String instanceKey = getInstanceKey(service);
                CEtcdResult result = _client.createDir(instanceKey, _registrationTtl, true);
                if (result.isError()) {
                      return false;
                }
                Boolean status = getStatus(service);
                Boolean lastStatus = _lastStatus.get(service);
                if (!status.equals(lastStatus)) {
                    _client.set(instanceKey + UP_KEY, String.valueOf(status));
                    _lastStatus.put(service, status);
                }
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }

        private boolean getStatus(ServiceInfo service) {
            boolean isHealthy = true;
            try {
                HealthCheckHandler handler = service.getHealthCheckHandler();
                if (handler != null) {
                    isHealthy = handler.isHealthy();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                isHealthy = false;
            }
            return isHealthy;
        }

        private String getServiceKey(ServiceInfo service) {
            String key = String.format(BASE_KEY + "/%s/%s",
                    normalizeText(service.getServiceName()),
                    normalizeText(service.getServiceNamespace()));
            if (service.getSubEnv() != null && !service.getSubEnv().isEmpty()) {
                key += "/" + normalizeText(service.getSubEnv());
            }
            return key;
        }

        private String getInstanceKey(ServiceInfo service) {
            return getServiceKey(service) + "/" + normalizeText(service.getServiceUrl());
        }
    }

    private static String normalizeText(String text) {
        return text.replace('/', '_');
    }
}
