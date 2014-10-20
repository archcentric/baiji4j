package com.ctriposs.baiji.rpc.server.stats;

import com.ctriposs.baiji.rpc.common.logging.Logger;
import com.ctriposs.baiji.rpc.common.logging.LoggerFactory;
import com.ctriposs.baiji.rpc.common.stats.AuditionData;
import com.ctriposs.baiji.rpc.common.stats.StatsLogger;
import com.ctriposs.baiji.rpc.common.stats.StatsLoggerFactory;
import com.ctriposs.baiji.rpc.common.stats.StatsUtils;
import com.ctriposs.baiji.rpc.server.ServiceMetadata;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yqdong on 2014/10/15.
 */
public class StatsReportJob implements Runnable {

    public static final String STATS_PREFIX = "soa4j.service.";

    public static final String STATS_PREFIX_REQUEST = STATS_PREFIX + "request.";
    public static final String STATS_PREFIX_RESPONSE = STATS_PREFIX + "response.";
    public static final String STATS_NAME_REQUEST_COUNT = STATS_PREFIX_REQUEST + "count";
    public static final String STATS_NAME_REQUEST_COST = STATS_PREFIX_REQUEST + "cost";
    public static final String STATS_NAME_REQUEST_COST_DISTRIBUTION = STATS_NAME_REQUEST_COST + ".distribution";
    public static final String STATS_NAME_OPERATION_COST = STATS_PREFIX + "operation.cost";
    public static final String STATS_NAME_OPERATION_COST_DISTRIBUTION = STATS_NAME_OPERATION_COST + ".distribution";
    public static final String STATS_NAME_RESPONSE_SIZE = STATS_PREFIX_RESPONSE + "size";
    public static final String STATS_NAME_RESPONSE_SIZE_DISTRIBUTION = STATS_NAME_RESPONSE_SIZE + ".distribution";
    public static final String STATS_NAME_RESPONSE_TYPE_DISTRIBUTION = STATS_PREFIX_RESPONSE + "type.distribution";
    public static final String STATS_PREFIX_ERROR = STATS_PREFIX + "error.";
    public static final String STATS_NAME_ERROR_COUNT = STATS_PREFIX_ERROR + "count";
    public static final String STATS_NAME_ERROR_PERCENTAGE = STATS_PREFIX_ERROR + "percentage";

    public static final String STATS_TAG_DISTRIBUTION = "distribution";
    public static final String STATS_TAG_OPERATION = "operation";
    public static final String STATS_TAG_SERVICE = "webservice";
    public static final String STATS_TAG_FRAMEWORK_VERSION = "frameworkversion";
    public static final String STATS_TAG_CODEGEN_VERSION = "codegenversion";
    public static final String STATS_TAG_SET_FEATURE_TYPE = "SetFeatureType";

    private static final String UNKNOWN = "Unknown";

    private final Logger _logger = LoggerFactory.getLogger(StatsReportJob.class);
    private final StatsLogger _statsLogger = StatsLoggerFactory.getStatsLogger();
    private final ServiceStats _serviceStats;
    private final String _serviceName;
    private final String _frameworkVersion;
    private final String _codeGenVersion;

    public StatsReportJob(String frameworkVersion, ServiceStats serviceStats) {
        _serviceStats = serviceStats;
        _frameworkVersion = frameworkVersion != null ? frameworkVersion : UNKNOWN;
        ServiceMetadata metadata = _serviceStats.getServiceMetadata();
        _serviceName = getServiceFullName(metadata.getServiceNamespace(), metadata.getServiceName());
        _codeGenVersion = metadata.getCodeGeneratorVersion() != null ? metadata.getCodeGeneratorVersion() : UNKNOWN;
    }

    @Override
    public void run() {
        try {
            sendStats();
        } catch (Exception ex) {
            _logger.error("Failed to report stats data", ex);
        }
    }

    private static String getServiceFullName(String serviceNamespace, String serviceName) {
        return (StatsUtils.convertNamespaceToStatsValue(serviceNamespace) + "." + serviceName).toLowerCase();
    }

    private void sendStats() {
        Map<String, String> tagMap = new HashMap<String, String>();
        tagMap.put(STATS_TAG_SERVICE, _serviceName);
        tagMap.put(STATS_TAG_FRAMEWORK_VERSION, _frameworkVersion);
        tagMap.put(STATS_TAG_CODEGEN_VERSION, _codeGenVersion);

        Collection<OperationStats> allStats = _serviceStats.getAndResetAllOperationStats();
        for (OperationStats stats : allStats) {
            tagMap.put(STATS_TAG_OPERATION, stats.getOperationName());

            // Request cost
            AuditionData requestCostAuditionData = stats.getRequestCostAuditionData();
            tagMap.put(STATS_TAG_SET_FEATURE_TYPE, "count");
            _statsLogger.log(STATS_NAME_REQUEST_COST, requestCostAuditionData.count, tagMap);
            tagMap.put(STATS_TAG_SET_FEATURE_TYPE, "min");
            _statsLogger.log(STATS_NAME_REQUEST_COST, requestCostAuditionData.min, tagMap);
            tagMap.put(STATS_TAG_SET_FEATURE_TYPE, "max");
            _statsLogger.log(STATS_NAME_REQUEST_COST, requestCostAuditionData.max, tagMap);
            tagMap.put(STATS_TAG_SET_FEATURE_TYPE, "sum");
            _statsLogger.log(STATS_NAME_REQUEST_COST, requestCostAuditionData.sum, tagMap);
            tagMap.remove(STATS_TAG_SET_FEATURE_TYPE);

            // Request cost distribution
            tagMap.put(STATS_TAG_DISTRIBUTION, "0 ~ 10ms");
            _statsLogger.log(STATS_NAME_REQUEST_COST_DISTRIBUTION, stats.getRequestCountInRequestCostRange(0L, 10L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "10 ~ 50ms");
            _statsLogger.log(STATS_NAME_REQUEST_COST_DISTRIBUTION, stats.getRequestCountInRequestCostRange(10L, 50L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "50 ~ 200ms");
            _statsLogger.log(STATS_NAME_REQUEST_COST_DISTRIBUTION, stats.getRequestCountInRequestCostRange(50L, 200L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "200 ~ 500ms");
            _statsLogger.log(STATS_NAME_REQUEST_COST_DISTRIBUTION, stats.getRequestCountInRequestCostRange(200L, 500L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "500ms ~ 1s");
            _statsLogger.log(STATS_NAME_REQUEST_COST_DISTRIBUTION, stats.getRequestCountInRequestCostRange(500L, 1000L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "1 ~ 5s");
            _statsLogger.log(STATS_NAME_REQUEST_COST_DISTRIBUTION, stats.getRequestCountInRequestCostRange(1000L, 5 * 1000L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "5 ~ 10s");
            _statsLogger.log(STATS_NAME_REQUEST_COST_DISTRIBUTION, stats.getRequestCountInRequestCostRange(5 * 1000L, 10 * 1000L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "10 ~ 30s");
            _statsLogger.log(STATS_NAME_REQUEST_COST_DISTRIBUTION, stats.getRequestCountInRequestCostRange(10 * 1000L, 30 * 1000L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "30 ~ 100s");
            _statsLogger.log(STATS_NAME_REQUEST_COST_DISTRIBUTION, stats.getRequestCountInRequestCostRange(30 * 1000L, 100 * 1000L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, ">= 100s");
            _statsLogger.log(STATS_NAME_REQUEST_COST_DISTRIBUTION, stats.getRequestCountInRequestCostRange(100 * 1000L, null), tagMap);
            tagMap.remove(STATS_TAG_DISTRIBUTION);

            // Operation cost
            AuditionData operationCostAuditionData = stats.getOperationCostAuditionData();
            tagMap.put(STATS_TAG_SET_FEATURE_TYPE, "count");
            _statsLogger.log(STATS_NAME_OPERATION_COST, operationCostAuditionData.count, tagMap);
            tagMap.put(STATS_TAG_SET_FEATURE_TYPE, "min");
            _statsLogger.log(STATS_NAME_OPERATION_COST, operationCostAuditionData.min, tagMap);
            tagMap.put(STATS_TAG_SET_FEATURE_TYPE, "max");
            _statsLogger.log(STATS_NAME_OPERATION_COST, operationCostAuditionData.max, tagMap);
            tagMap.put(STATS_TAG_SET_FEATURE_TYPE, "sum");
            _statsLogger.log(STATS_NAME_OPERATION_COST, operationCostAuditionData.sum, tagMap);
            tagMap.remove(STATS_TAG_SET_FEATURE_TYPE);

            // Operation cost distribution
            tagMap.put(STATS_TAG_DISTRIBUTION, "0 ~ 10ms");
            _statsLogger.log(STATS_NAME_OPERATION_COST_DISTRIBUTION, stats.getRequestCountInOperationCostRange(0L, 10L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "10 ~ 50ms");
            _statsLogger.log(STATS_NAME_OPERATION_COST_DISTRIBUTION, stats.getRequestCountInOperationCostRange(10L, 50L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "50 ~ 200ms");
            _statsLogger.log(STATS_NAME_OPERATION_COST_DISTRIBUTION, stats.getRequestCountInOperationCostRange(50L, 200L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "200 ~ 500ms");
            _statsLogger.log(STATS_NAME_OPERATION_COST_DISTRIBUTION, stats.getRequestCountInOperationCostRange(200L, 500L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "500ms ~ 1s");
            _statsLogger.log(STATS_NAME_OPERATION_COST_DISTRIBUTION, stats.getRequestCountInOperationCostRange(500L, 1000L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "1 ~ 5s");
            _statsLogger.log(STATS_NAME_OPERATION_COST_DISTRIBUTION, stats.getRequestCountInOperationCostRange(1000L, 5 * 1000L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "5 ~ 10s");
            _statsLogger.log(STATS_NAME_OPERATION_COST_DISTRIBUTION, stats.getRequestCountInOperationCostRange(5 * 1000L, 10 * 1000L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "10 ~ 30s");
            _statsLogger.log(STATS_NAME_OPERATION_COST_DISTRIBUTION, stats.getRequestCountInOperationCostRange(10 * 1000L, 30 * 1000L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "30 ~ 100s");
            _statsLogger.log(STATS_NAME_OPERATION_COST_DISTRIBUTION, stats.getRequestCountInOperationCostRange(30 * 1000L, 100 * 1000L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, ">= 100s");
            _statsLogger.log(STATS_NAME_OPERATION_COST_DISTRIBUTION, stats.getRequestCountInOperationCostRange(100 * 1000L, null), tagMap);
            tagMap.remove(STATS_TAG_DISTRIBUTION);

            // Response size cost
            AuditionData responseSizeAuditionData = stats.getResponseSizeAuditionData();
            tagMap.put(STATS_TAG_SET_FEATURE_TYPE, "count");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE, responseSizeAuditionData.count, tagMap);
            tagMap.put(STATS_TAG_SET_FEATURE_TYPE, "min");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE, responseSizeAuditionData.min, tagMap);
            tagMap.put(STATS_TAG_SET_FEATURE_TYPE, "max");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE, responseSizeAuditionData.max, tagMap);
            tagMap.put(STATS_TAG_SET_FEATURE_TYPE, "sum");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE, responseSizeAuditionData.sum, tagMap);
            tagMap.remove(STATS_TAG_SET_FEATURE_TYPE);

            // Operation cost distribution
            tagMap.put(STATS_TAG_DISTRIBUTION, "0 ~ 10k");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE_DISTRIBUTION, stats.getRequestCountInResponseSizeRange(0L, 10 * 1024L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "10 ~ 50k");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE_DISTRIBUTION, stats.getRequestCountInResponseSizeRange(10 * 1024L, 50 * 1024L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "50 ~ 200k");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE_DISTRIBUTION, stats.getRequestCountInResponseSizeRange(50 * 1024L, 200 * 1024L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "200 ~ 500k");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE_DISTRIBUTION, stats.getRequestCountInResponseSizeRange(200 * 1024L, 500 * 1024L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "500k ~ 1M");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE_DISTRIBUTION, stats.getRequestCountInResponseSizeRange(500 * 1024L, 1024 * 1024L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "1 ~ 5M");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE_DISTRIBUTION, stats.getRequestCountInResponseSizeRange(1024 * 1024L, 5 * 1024 * 1024L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, "5 ~ 10M");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE_DISTRIBUTION, stats.getRequestCountInResponseSizeRange(5 * 1024 * 1024L, 10 * 1024 * 1024L), tagMap);
            tagMap.put(STATS_TAG_DISTRIBUTION, ">= 10M");
            _statsLogger.log(STATS_NAME_RESPONSE_SIZE_DISTRIBUTION, stats.getRequestCountInResponseSizeRange(10 * 1024 * 1024L, null), tagMap);
            tagMap.remove(STATS_TAG_DISTRIBUTION);

            // Request counts
            long requestCount = stats.getRequestCount();
            _statsLogger.log(STATS_NAME_REQUEST_COUNT, stats.getRequestCount());
            long serviceExceptionCount = stats.getServiceExceptionCount();
            tagMap.put(STATS_TAG_DISTRIBUTION, "Service Exception");
            _statsLogger.log(STATS_NAME_ERROR_COUNT, serviceExceptionCount, tagMap);
            _statsLogger.log(STATS_NAME_ERROR_PERCENTAGE, (float)serviceExceptionCount / requestCount, tagMap);
            long frameworkExceptionCount = stats.getFrameworkExceptionCount();
            tagMap.put(STATS_TAG_DISTRIBUTION, "Framework Exception");
            _statsLogger.log(STATS_NAME_ERROR_COUNT, frameworkExceptionCount, tagMap);
            _statsLogger.log(STATS_NAME_ERROR_PERCENTAGE, (float)frameworkExceptionCount / requestCount, tagMap);
        }
    }
}
