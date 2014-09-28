package com.ctriposs.baiji.rpc.common.stats;

import java.util.Map;

/**
 * An interface used to record application statistics.
 * <p/>
 * Only the following values are allows in stats item name and tag name/value:
 * Alphabetic characters, numbers, "-", "_", ".", "/".
 * <p/>
 * Created by yqdong on 2014/9/26.
 */
public interface StatsLogger {

    /**
     * Record a stats data of long.
     */
    void log(String metricName, long value);

    /**
     * Record a stats data of long with some extra tags.
     */
    void log(String metricName, long value, Map<String, String> tags);

    /**
     * Record a stats data of float.
     */
    void log(String metricName, float value);

    /**
     * Record a stats data of float with some extra tags.
     */
    void log(String metricName, float value, Map<String, String> tags);
}
