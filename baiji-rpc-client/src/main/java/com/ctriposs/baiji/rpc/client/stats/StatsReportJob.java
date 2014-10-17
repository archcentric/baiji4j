package com.ctriposs.baiji.rpc.client.stats;

import com.ctriposs.baiji.rpc.client.ConnectionMode;
import com.ctriposs.baiji.rpc.common.logging.Logger;
import com.ctriposs.baiji.rpc.common.logging.LoggerFactory;
import com.ctriposs.baiji.rpc.common.stats.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by yqdong on 2014/9/29.
 */
public class StatsReportJob implements Runnable {

    public static final String STATS_PREFIX = "baiji.";

    public static final String STATS_PREFIX_REQUEST = STATS_PREFIX + "request.";
    public static final String STATS_PREFIX_RESPONSE = STATS_PREFIX + "response.";
    public static final String STATS_NAME_COUNT = STATS_PREFIX_REQUEST + "count";
    public static final String STATS_NAME_REQUEST_COST = STATS_PREFIX_REQUEST + "cost";
    public static final String STATS_PREFIX_REQUEST_COST = STATS_NAME_REQUEST_COST + ".";
    public static final String STATS_NAME_REQUEST_COST_DISTRIBUTION = STATS_PREFIX_REQUEST_COST + "distribution";
    public static final String STATS_NAME_EXCEPTION_COUNT = STATS_PREFIX + "exception.count";
    public static final String STATS_NAME_RESPONSE_SIZE = STATS_PREFIX_RESPONSE + "size";
    public static final String STATS_PREFIX_RESPONSE_SIZE = STATS_NAME_RESPONSE_SIZE + ".";
    public static final String STATS_NAME_RESPONSE_SIZE_DISTRIBUTION = STATS_PREFIX_RESPONSE_SIZE + "distribution";

    public static final String STATS_TAG_DISTRIBUTION = "distribution";
    public static final String STATS_TAG_OPERATION = "operation";
    public static final String STATS_TAG_SERVICE = "webservice";
    public static final String STATS_TAG_EXCEPTION_TYPE = "exceptiontype";
    public static final String STATS_TAG_EXCEPTION_NAME = "exceptionname";
    public static final String STATS_TAG_FRAMEWORK_VERSION = "frameworkversion";
    public static final String STATS_TAG_CODEGEN_VERSION = "codegenversion";
    public static final String STATS_TAG_CONNECTION_MODE = "connectionmode";
    public static final String STATS_TAG_SET_FEATURE_TYPE = "SetFeatureType";

    private static final String UNKNOWN = "Unknown";

    private final Logger _logger = LoggerFactory.getLogger(StatsReportJob.class);
    private final StatsLogger _statsLogger = StatsLoggerFactory.getStatsLogger();
    private final InvocationStatsStore _store;
    private final String _serviceName;
    private final String _frameworkVersion;
    private final String _codeGenVersion;
    private final String _connectionMode;

    public StatsReportJob(InvocationStatsStore store) {
        _store = store;
        _serviceName = getServiceFullName(_store.getServiceNamespace(), _store.getServiceName());
        _frameworkVersion = _store.getFrameworkVersion() != null ? _store.getFrameworkVersion() : UNKNOWN;
        _codeGenVersion = _store.getCodeGeneratorVersion() != null ? _store.getCodeGeneratorVersion() : UNKNOWN;
        _connectionMode = getConnectionModeText(_store.getConnectionMode());
    }

    @Override
    public void run() {
        try {
            sendStats();
        } catch (Exception ex) {
            _logger.error("Failed to report stats data", ex);
        }
    }

    private void sendStats() {
        Map<String, String> tagMap = new HashMap<String, String>();
        tagMap.put(STATS_TAG_SERVICE, _serviceName);
        tagMap.put(STATS_TAG_FRAMEWORK_VERSION, _frameworkVersion);
        tagMap.put(STATS_TAG_CODEGEN_VERSION, _codeGenVersion);
        tagMap.put(STATS_TAG_CONNECTION_MODE, _connectionMode);

        Collection<InvocationStats> allStats = _store.getAndResetAllStats();
        for (InvocationStats stats : allStats) {
            tagMap.put(STATS_TAG_OPERATION, _serviceName + "." + stats.getOperationName().toLowerCase());

            // Request cost
            AuditionData costStats = stats.getRequestCostAuditionData();
            tagMap.put(STATS_TAG_SET_FEATURE_TYPE, "count");
            _statsLogger.log(STATS_NAME_REQUEST_COST, costStats.count, tagMap);
            tagMap.put(STATS_TAG_SET_FEATURE_TYPE, "sum");
            _statsLogger.log(STATS_NAME_REQUEST_COST, costStats.sum, tagMap);
            tagMap.put(STATS_TAG_SET_FEATURE_TYPE, "min");
            _statsLogger.log(STATS_NAME_REQUEST_COST, costStats.min, tagMap);
            tagMap.put(STATS_TAG_SET_FEATURE_TYPE, "max");
            _statsLogger.log(STATS_NAME_REQUEST_COST, costStats.max, tagMap);
            tagMap.remove(STATS_TAG_SET_FEATURE_TYPE);

            // Request cost distribution
            tagMap.put(STATS_TAG_DISTRIBUTION, "0 ~ 10ms");
            _statsLogger.log(STATS_NAME_REQUEST_COST_DISTRIBUTION, stats.getRequestCountInCostRange(0L, 10L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "10 ~ 50ms");
            _statsLogger.log(STATS_NAME_REQUEST_COST_DISTRIBUTION, stats.getRequestCountInCostRange(10L, 50L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "50 ~ 200ms");
            _statsLogger.log(STATS_NAME_REQUEST_COST_DISTRIBUTION, stats.getRequestCountInCostRange(50L, 200L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "200 ~ 500ms");
            _statsLogger.log(STATS_NAME_REQUEST_COST_DISTRIBUTION, stats.getRequestCountInCostRange(200L, 500L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "500ms ~ 1s");
            _statsLogger.log(STATS_NAME_REQUEST_COST_DISTRIBUTION, stats.getRequestCountInCostRange(500L, 1000L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "1 ~ 5s");
            _statsLogger.log(STATS_NAME_REQUEST_COST_DISTRIBUTION, stats.getRequestCountInCostRange(1000L, 5 * 1000L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "5 ~ 10s");
            _statsLogger.log(STATS_NAME_REQUEST_COST_DISTRIBUTION, stats.getRequestCountInCostRange(5 * 1000L, 10 * 1000L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "10 ~ 30s");
            _statsLogger.log(STATS_NAME_REQUEST_COST_DISTRIBUTION, stats.getRequestCountInCostRange(10 * 1000L, 30 * 1000L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "30 ~ 100s");
            _statsLogger.log(STATS_NAME_REQUEST_COST_DISTRIBUTION, stats.getRequestCountInCostRange(30 * 1000L, 100 * 1000L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, ">= 100s");
            _statsLogger.log(STATS_NAME_REQUEST_COST_DISTRIBUTION, stats.getRequestCountInCostRange(100 * 1000L, null), tagMap);
            tagMap.remove(STATS_TAG_DISTRIBUTION);

            // Response size distribution
            tagMap.put(STATS_TAG_DISTRIBUTION, "0 ~ 10K");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE_DISTRIBUTION, stats.getResponseCountInSizeRange(0L, 10 * 1024L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "10 ~ 50K");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE_DISTRIBUTION, stats.getResponseCountInSizeRange(10 * 1024L, 50 * 1024L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "50 ~ 200K");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE_DISTRIBUTION, stats.getResponseCountInSizeRange(50 * 1024L, 200 * 1024L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "200 ~ 500K");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE_DISTRIBUTION, stats.getResponseCountInSizeRange(200 * 1024L, 500 * 1024L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "500k ~ 1M");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE_DISTRIBUTION, stats.getResponseCountInSizeRange(500 * 1024L, 1024 * 1024L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "1 ~ 5M");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE_DISTRIBUTION, stats.getResponseCountInSizeRange(1024 * 1024L, 5 * 1024 * 1024L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "5 ~ 10M");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE_DISTRIBUTION, stats.getResponseCountInSizeRange(5 * 1024 * 1024L, 10 * 1024 * 1024L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, ">= 10M");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE_DISTRIBUTION, stats.getResponseCountInSizeRange(10 * 1024 * 1024L, null), tagMap);
            tagMap.remove(STATS_TAG_DISTRIBUTION);

            // Request count
            _statsLogger.log(STATS_NAME_COUNT, stats.getInvocationCount(), tagMap);

            // Response size
            AuditionData responseSizeStats = stats.getResponseSizeAuditionData();
            tagMap.put(STATS_TAG_SET_FEATURE_TYPE, "count");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE, responseSizeStats.count, tagMap);
            tagMap.put(STATS_TAG_SET_FEATURE_TYPE, "sum");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE, responseSizeStats.sum, tagMap);
            tagMap.put(STATS_TAG_SET_FEATURE_TYPE, "min");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE, responseSizeStats.min, tagMap);
            tagMap.put(STATS_TAG_SET_FEATURE_TYPE, "max");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE, responseSizeStats.max, tagMap);
            tagMap.remove(STATS_TAG_SET_FEATURE_TYPE);

            // Exception
            ConcurrentMap<String, ConcurrentMap<String, SimpleCounter>> exceptionCounter = stats.getInvocationExceptionCounter();
            for (Map.Entry<String, ConcurrentMap<String, SimpleCounter>> entry : exceptionCounter.entrySet()) {
                tagMap.put(STATS_TAG_EXCEPTION_TYPE, entry.getKey());
                ConcurrentMap<String, SimpleCounter> exceptionNameMap = entry.getValue();
                for (Map.Entry<String, SimpleCounter> exceptionEntry : exceptionNameMap.entrySet()) {
                    tagMap.put(STATS_TAG_EXCEPTION_NAME, exceptionEntry.getKey());
                    _statsLogger.log(STATS_NAME_EXCEPTION_COUNT, exceptionEntry.getValue().get(), tagMap);
                }
            }
            tagMap.remove(STATS_TAG_EXCEPTION_TYPE);
            tagMap.remove(STATS_TAG_EXCEPTION_NAME);
        }
    }

    private static String getServiceFullName(String serviceNamespace, String serviceName) {
        return (StatsUtils.convertNamespaceToStatsValue(serviceNamespace) + "." + serviceName).toLowerCase();
    }

    private static String getConnectionModeText(ConnectionMode mode) {
        switch (mode) {
            case DIRECT:
                return "Direct";
            case INDIRECT:
                return "Indirect";
            default:
                return UNKNOWN;
        }
    }
}
