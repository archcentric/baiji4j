package com.ctriposs.baiji.rpc.common.stats;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by yqdong on 2014/9/26.
 */
public class StatsLoggerFactory {

    private static final StatsLogger DUMMY_LOGGER = new DummyStatsLogger();

    private static final ConcurrentMap<String, IStatsLoggerFactory> _loggerFacotries = new ConcurrentHashMap<>();

    private static IStatsLoggerFactory _currentFactory = null;

    private StatsLoggerFactory() {
    }

    /**
     * Register an IStatsLoggerFactory instance to the system.
     *
     * @param factory
     */
    public static void register(IStatsLoggerFactory factory) {
        _loggerFacotries.put(factory.name(), factory);
        if (_loggerFacotries.size() == 1) {
            _currentFactory = factory;
        }
    }

    /**
     * Set the IStatsLoggerFactory to be used in the system.
     *
     * @param name the name of IStatsLoggerFactory to use
     */
    public static void setCurrentFactory(String name) {
        IStatsLoggerFactory factory = _loggerFacotries.get(name);
        if (factory != null) {
            _currentFactory = factory;
        }
    }

    /**
     * Return the current using StatsLogger.
     *
     * @return
     */
    public static StatsLogger getStatsLogger() {
        return _currentFactory != null ? _currentFactory.getStatsLogger() : DUMMY_LOGGER;
    }
}
