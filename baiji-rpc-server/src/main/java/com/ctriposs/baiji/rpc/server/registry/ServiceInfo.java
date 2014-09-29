package com.ctriposs.baiji.rpc.server.registry;

import com.ctriposs.baiji.rpc.server.util.NetworkUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceInfo {

    private String _serviceName;
    private String _serviceNamespace;
    private String _ipAddress;
    private int _port = 80;
    private String _contextPath;
    private String _serviceUrl;
    private String _subEnv;
    private HealthCheckHandler _healthCheckHandler;
    private boolean _dirty;
    private final Map<String, String> _metadata = new ConcurrentHashMap<String, String>();

    private ServiceInfo() {
    }

    public String getServiceName() {
        return _serviceName;
    }

    public String getServiceNamespace() {
        return _serviceNamespace;
    }

    public String getIpAddress() {
        return _ipAddress;
    }

    public int getPort() {
        return _port;
    }

    public String getContextPath() {
        return _contextPath;
    }

    public String getServiceUrl() {
        if (_serviceUrl == null) {
            StringBuilder builder = new StringBuilder("http://");
            builder.append(_ipAddress);
            if (_port != 80) {
                builder.append(":");
                builder.append(_port);
            }
            builder.append(_contextPath);
            if (!_contextPath.endsWith("/")) {
                builder.append("/");
            }
            _serviceUrl = builder.toString();
        }
        return _serviceUrl;
    }

    public String getSubEnv() {
        return _subEnv;
    }

    public HealthCheckHandler getHealthCheckHandler() {
        return _healthCheckHandler;
    }

    public void setHealthCheckHandler(HealthCheckHandler handler) {
        _healthCheckHandler = handler;
    }

    public Map<String, String> getMetadata() {
        return _metadata;
    }

    public void setMetadata(String key, String value) {
        _metadata.put(key, value);
        _dirty = true;
    }

    public void removeMetadata(String key) {
        _metadata.remove(key);
        _dirty = true;
    }

    boolean isDirty() {
        return _dirty;
    }

    void setDirty(boolean dirty) {
        _dirty = dirty;
    }

    public static class Builder {

        private final ServiceInfo _serviceInfo = new ServiceInfo();

        public Builder() {
            _serviceInfo._port = 80;
            _serviceInfo._ipAddress = NetworkUtil.getIpAddress();
            _serviceInfo._contextPath = "/";
        }

        public Builder serviceName(String serviceName) {
            if (serviceName != null && !serviceName.isEmpty()) {
                _serviceInfo._serviceName = serviceName;
            }
            return this;
        }

        public Builder serviceNamespace(String serviceNamespace) {
            if (serviceNamespace != null && !serviceNamespace.isEmpty()) {
                _serviceInfo._serviceNamespace= serviceNamespace;
            }
            return this;
        }

        public Builder ipAddress(String ipAddress) {
            if (ipAddress != null && !ipAddress.isEmpty()) {
                _serviceInfo._ipAddress = ipAddress;
            } else {
                _serviceInfo._ipAddress = NetworkUtil.getIpAddress();
            }
            return this;
        }

        public Builder port(int port) {
            if (port > 0) {
                _serviceInfo._port = port;
            }
            return this;
        }

        public Builder contextPath(String path) {
            if (path != null && !path.isEmpty()) {
                _serviceInfo._contextPath = path.charAt(0) == '/' ? path : "/" + path;
            } else {
                _serviceInfo._contextPath = "/";
            }
            return this;
        }

        public Builder subEnv(String subEnv) {
            _serviceInfo._subEnv = subEnv;
            return this;
        }

        public Builder setMetadata(String key, String value) {
            _serviceInfo._metadata.put(key, value);
            return this;
        }

        public ServiceInfo build() {
            if (_serviceInfo._serviceName == null || _serviceInfo._serviceNamespace == null) {
                throw new IllegalStateException("serviceName and serviceNamespace must be set.");
            }
            return _serviceInfo;
        }
    }
}
