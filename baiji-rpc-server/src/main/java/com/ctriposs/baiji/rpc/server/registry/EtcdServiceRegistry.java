package com.ctriposs.baiji.rpc.server.registry;

import com.ctriposs.baiji.rpc.common.logging.Logger;
import com.ctriposs.baiji.rpc.common.logging.LoggerFactory;
import com.ctriposs.etcd.CEtcdClient;
import com.ctriposs.etcd.CEtcdClientException;
import com.ctriposs.etcd.CEtcdResult;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EtcdServiceRegistry implements ServiceRegistry {

    private static final int DEFAULT_HEARTBEAT_INTERVAL = 30 * 1000; // In milliseconds
    private static final int HEARTBEAT_MISS_TOLERANCE = 3;

    private static final Logger _logger = LoggerFactory.getLogger(EtcdServiceRegistry.class);

    private final Object _lock = new Object();
    private final List<ServiceInfo> _services = new LinkedList<ServiceInfo>();
    private final CEtcdClient _client;
    private final Timer _heartbeatTimer = new Timer("EtcdServiceRegistry_Heartbeat", true);
    private int _heartbeatInterval = DEFAULT_HEARTBEAT_INTERVAL;
    private int _registrationTtl = _heartbeatInterval * HEARTBEAT_MISS_TOLERANCE / 1000;
    private boolean _running;
    private boolean _timerStarted;

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
        synchronized (_lock) {
            _services.add(serviceInfo);
            if (_running && !_timerStarted) {
                startHeartbeatTimer();
            }
        }
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
        synchronized (_lock) {
            if (_running) {
                return;
            }
            _running = true;
            if (!_services.isEmpty()) {
                startHeartbeatTimer();
            }
        }
    }

    @Override
    public void stop() {
        synchronized (_lock) {
            if (!_running) {
                return;
            }
            _running = false;
            _heartbeatTimer.cancel();
            _timerStarted = false;
        }
    }

    private void startHeartbeatTimer() {
        _heartbeatTimer.scheduleAtFixedRate(new HeartbeatTask(), 0, _heartbeatInterval);
        _timerStarted = true;
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
                    _logger.error("Error occurs in HeartbeatTask.", ex);
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
                boolean nodeExisted = result != null && result.node != null;
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
                String msg = String.format("Error occurs when registering service %s-%s, url = %s.", service.getServiceName(),
                        service.getServiceNamespace(), service.getServiceUrl());
                _logger.error(msg, ex);
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
                String msg = String.format("Error occurs when sending heartbeat request for service %s-%s, url = %s.",
                        service.getServiceName(), service.getServiceNamespace(), service.getServiceUrl());
                _logger.error(msg, ex);
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
                    normalizeKey(service.getServiceName()),
                    normalizeKey(service.getServiceNamespace()));
            if (service.getSubEnv() != null && !service.getSubEnv().isEmpty()) {
                key += "/" + normalizeKey(service.getSubEnv());
            }
            return key;
        }

        private String getInstanceKey(ServiceInfo service) {
            return getServiceKey(service) + "/" + normalizeKey(service.getServiceUrl());
        }
    }

    private static String normalizeKey(String key) {
        return key.replace('/', '_');
    }
}
