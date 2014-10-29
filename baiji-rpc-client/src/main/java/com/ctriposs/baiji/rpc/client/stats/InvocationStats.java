package com.ctriposs.baiji.rpc.client.stats;

import com.ctriposs.baiji.rpc.client.HttpWebException;
import com.ctriposs.baiji.rpc.client.ServiceException;
import com.ctriposs.baiji.rpc.common.stats.AuditableCounter;
import com.ctriposs.baiji.rpc.common.stats.AuditionData;
import com.ctriposs.baiji.rpc.common.stats.SimpleCounter;
import com.ctriposs.baiji.rpc.common.types.ErrorClassificationCodeType;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by yqdong on 2014/9/28.
 */
public class InvocationStats {

    private final String _operationName;
    private final SimpleCounter _invokeCounter = new SimpleCounter();
    private final SimpleCounter _successCounter = new SimpleCounter();
    private final SimpleCounter _failedCounter = new SimpleCounter();
    private final AuditableCounter _responseSizeCounter = new AuditableCounter();
    private final AuditableCounter _requestCostCounter = new AuditableCounter();
    private final ConcurrentMap<String, ConcurrentMap<String, SimpleCounter>> _exceptionCounter
            = new ConcurrentHashMap<String, ConcurrentMap<String, SimpleCounter>>();

    public InvocationStats(String operationName) {
        _operationName = operationName;
    }

    public String getOperationName() {
        return _operationName;
    }

    public long getInvocationCount() {
        return _invokeCounter.get();
    }

    public long getSuccessCount() {
        return _successCounter.get();
    }

    public long getFailedCount() {
        return _failedCounter.get();
    }

    public AuditionData getResponseSizeAuditionData() {
        return _responseSizeCounter.getAuditionData();
    }

    public int getResponseCountInSizeRange(Long from, Long to) {
        return _responseSizeCounter.getValueCountInRange(from, to);
    }

    public AuditionData getRequestCostAuditionData() {
        return _requestCostCounter.getAuditionData();
    }

    public int getRequestCountInCostRange(Long from, Long to) {
        return _requestCostCounter.getValueCountInRange(from, to);
    }

    public ConcurrentMap<String, ConcurrentMap<String, SimpleCounter>> getInvocationExceptionCounter() {
        return _exceptionCounter;
    }

    public void markSuccess() {
        _successCounter.inc();
    }

    public void markFailure() {
        _failedCounter.inc();
    }

    public void markException(Exception ex) {
        String exceptionTypeName = "other";
        if (ex instanceof ServiceException) {
            ServiceException se = (ServiceException) ex;
            if (se.getResponseErrors() != null && !se.getResponseErrors().isEmpty()) {
                ErrorClassificationCodeType errorClassification = se.getResponseErrors().get(0).getErrorClassification();
                if (errorClassification != null) {
                    exceptionTypeName = errorClassification.toString().toLowerCase();
                }
            }
        } else if (ex instanceof HttpWebException) {
            int statusCode = ((HttpWebException) ex).getStatusCode();
            switch (statusCode) {
                case HttpStatus.SC_FORBIDDEN:
                case HttpStatus.SC_NOT_FOUND:
                case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                case HttpStatus.SC_SERVICE_UNAVAILABLE:
                case 429:
                    exceptionTypeName = String.valueOf(statusCode);
                    break;
                case HttpStatus.SC_REQUEST_TIMEOUT:
                    exceptionTypeName = "timeout";
                    break;
                case HttpStatus.SC_ACCEPTED:
                    break;
                default:
                    break;
            }
        } else if (ex instanceof IOException) {
            exceptionTypeName = "timeout";
        }

        ConcurrentMap<String, SimpleCounter> exceptionMap = _exceptionCounter.get(exceptionTypeName);
        if (exceptionMap == null) {
            exceptionMap = new ConcurrentHashMap<String, SimpleCounter>();
            ConcurrentMap<String, SimpleCounter> existedMap = _exceptionCounter.putIfAbsent(exceptionTypeName,
                    exceptionMap);
            if (existedMap != null) {
                exceptionMap = existedMap;
            }
        }

        String exceptionName = ex.getClass().getName();
        if (ex.getCause() != null) {
            exceptionName = ex.getCause().getClass().getName();
        }

        SimpleCounter exceptionCounter = exceptionMap.get(exceptionName);
        if (exceptionCounter == null) {
            exceptionCounter = new SimpleCounter();
            SimpleCounter existedCounter = exceptionMap.putIfAbsent(exceptionName, exceptionCounter);
            if (existedCounter != null) {
                exceptionCounter = existedCounter;
            }
        }

        exceptionCounter.inc();
    }

    public void markInvocation() {
        _invokeCounter.inc();
    }

    public void addRequestCost(long cost) {
        _requestCostCounter.add(cost);
    }

    public void addResponseSize(long size) {
        _responseSizeCounter.add(size);
    }
}
