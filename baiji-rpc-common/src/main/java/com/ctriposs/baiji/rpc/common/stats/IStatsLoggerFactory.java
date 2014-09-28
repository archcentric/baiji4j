package com.ctriposs.baiji.rpc.common.stats;

/**
 * Created by yqdong on 2014/9/26.
 */
public interface IStatsLoggerFactory {

    String name();

    StatsLogger getStatsLogger();
}
