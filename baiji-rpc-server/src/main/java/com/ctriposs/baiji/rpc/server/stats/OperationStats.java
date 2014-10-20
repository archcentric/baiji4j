package com.ctriposs.baiji.rpc.server.stats;

import com.ctriposs.baiji.rpc.common.stats.AuditableCounter;
import com.ctriposs.baiji.rpc.common.stats.AuditionData;
import com.ctriposs.baiji.rpc.common.stats.SimpleCounter;

/**
 * Created by yqdong on 2014/10/15.
 */
public class OperationStats {

    private final String _operationName;
    private final SimpleCounter _requestCounter = new SimpleCounter();
    private final SimpleCounter _successCounter = new SimpleCounter();
    private final SimpleCounter _serviceExceptionCounter = new SimpleCounter();
    private final SimpleCounter _frameworkExceptionCounter = new SimpleCounter();
    private final AuditableCounter _requestCostCounter = new AuditableCounter();
    private final AuditableCounter _operationCostCounter = new AuditableCounter();
    private final AuditableCounter _responseSizeCounter = new AuditableCounter();

    public OperationStats(String operationName) {
        _operationName = operationName;
    }

    public String getOperationName() {
        return _operationName;
    }

    public void markRequest() {
        _requestCounter.inc();
    }

    public void markSuccess() {
        _successCounter.inc();
    }

    public void markServiceException() {
        _serviceExceptionCounter.inc();
    }

    public void markFrameworkException() {
        _frameworkExceptionCounter.inc();
    }

    public void addRequestCost(long cost) {
        _requestCostCounter.add(cost);
    }

    public void addOperationCost(long cost) {
        _operationCostCounter.add(cost);
    }

    public void addResponseSize(long size) {
        _responseSizeCounter.add(size);
    }

    public long getRequestCount() {
        return _requestCounter.get();
    }

    public long getSuccessCount() {
        return _successCounter.get();
    }

    public long getServiceExceptionCount() {
        return _serviceExceptionCounter.get();
    }

    public long getFrameworkExceptionCount() {
        return _frameworkExceptionCounter.get();
    }

    public AuditionData getRequestCostAuditionData() {
        return _requestCostCounter.getAuditionData();
    }

    public int getRequestCountInRequestCostRange(Long from, Long to) {
        return _requestCostCounter.getValueCountInRange(from, to);
    }

    public AuditionData getOperationCostAuditionData() {
        return _operationCostCounter.getAuditionData();
    }

    public int getRequestCountInOperationCostRange(Long from, Long to) {
        return _operationCostCounter.getValueCountInRange(from, to);
    }

    public AuditionData getResponseSizeAuditionData() {
        return _responseSizeCounter.getAuditionData();
    }

    public int getRequestCountInResponseSizeRange(Long from, Long to) {
        return _responseSizeCounter.getValueCountInRange(from, to);
    }
}
