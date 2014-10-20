package com.ctriposs.baiji.rpc.server.stats;

import com.ctriposs.baiji.rpc.server.ServiceMetadata;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by yqdong on 2014/10/15.
 */
public class ServiceStats {

    private final ServiceMetadata _metadata;
    private final AtomicReference<ConcurrentHashMap<String, OperationStats>> _operationStatsMap
            = new AtomicReference<>(new ConcurrentHashMap<String, OperationStats>());

    public ServiceStats(ServiceMetadata metadata) {
        _metadata = metadata;
    }

    public ServiceMetadata getServiceMetadata() {
        return _metadata;
    }

    public Collection<OperationStats> getAndResetAllOperationStats() {
        ConcurrentHashMap<String, OperationStats> statsMap
                = _operationStatsMap.getAndSet(new ConcurrentHashMap<String, OperationStats>());
        return statsMap.values();
    }

    public OperationStats getOperationStats(String operationName) {
        ConcurrentHashMap<String, OperationStats> statsMap = _operationStatsMap.get();
        OperationStats stats = statsMap.get(operationName);
        if (stats == null) {
            stats = new OperationStats(operationName);
            OperationStats existedStats = statsMap.putIfAbsent(operationName, stats);
            if (existedStats != null) {
                stats = existedStats;
            }
        }
        return stats;
    }
}
