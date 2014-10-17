package com.ctriposs.baiji.rpc.client.stats;

import com.ctriposs.baiji.rpc.client.ConnectionMode;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by yqdong on 2014/9/29.
 */
public class InvocationStatsStore {

    private final AtomicReference<ConcurrentHashMap<String, InvocationStats>> _statsMap
            = new AtomicReference<>(new ConcurrentHashMap<String, InvocationStats>());
    private final String _serviceName;
    private final String _serviceNamespace;
    private final ConnectionMode _connectionMode;
    private final String _frameworkVersion;
    private final String _codeGeneratorVersion;

    public InvocationStatsStore() {
        this(null, null, null, null, null);
    }

    public InvocationStatsStore(String serviceName, String serviceNamespace,
                                ConnectionMode connectionMode, String frameworkVersion,
                                String codeGeneratorVersion) {
        _serviceName = serviceName;
        _serviceNamespace = serviceNamespace;
        _connectionMode = connectionMode;
        _frameworkVersion = frameworkVersion;
        _codeGeneratorVersion = codeGeneratorVersion;
    }

    public String getServiceName() {
        return _serviceName;
    }

    public String getServiceNamespace() {
        return _serviceNamespace;
    }

    public ConnectionMode getConnectionMode() {
        return _connectionMode;
    }

    public String getFrameworkVersion() {
        return _frameworkVersion;
    }

    public String getCodeGeneratorVersion() {
        return _codeGeneratorVersion;
    }

    public Collection<InvocationStats> getAndResetAllStats() {
        ConcurrentHashMap<String, InvocationStats> statsMap
                = _statsMap.getAndSet(new ConcurrentHashMap<String, InvocationStats>());
        return statsMap.values();
    }

    public InvocationStats getStats(String operationName) {
        ConcurrentHashMap<String, InvocationStats> statsMap = _statsMap.get();
        InvocationStats stats = statsMap.get(operationName);
        if (stats == null) {
            stats = new InvocationStats(operationName);
            InvocationStats existedStats = statsMap.putIfAbsent(operationName, stats);
            if (existedStats != null) {
                stats = existedStats;
            }
        }
        return stats;
    }
}
