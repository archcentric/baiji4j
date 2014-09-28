package com.ctriposs.baiji.rpc.common.stats;

import java.util.Map;

/**
 * A dummy stats logger which does nothing when being called.
 *
 * Created by yqdong on 2014/9/26.
 */
public class DummyStatsLogger implements StatsLogger {

    @Override
    public void log(String metricName, long value) {
    }

    @Override
    public void log(String metricName, long value, Map<String, String> tags) {
    }

    @Override
    public void log(String metricName, float value) {
    }

    @Override
    public void log(String metricName, float value, Map<String, String> tags) {
    }
}
