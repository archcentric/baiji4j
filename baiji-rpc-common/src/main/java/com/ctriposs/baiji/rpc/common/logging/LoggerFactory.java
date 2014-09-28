package com.ctriposs.baiji.rpc.common.logging;

import com.ctriposs.baiji.rpc.common.logging.slf4j.Slf4jLoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by yqdong on 2014/9/26.
 */
public final class LoggerFactory {

    private static final Logger DUMMY_LOGGER = new DummyLogger();

    private static final ConcurrentMap<String, ILoggerFactory> _loggerFactories = new ConcurrentHashMap<>();

    private static ILoggerFactory _currentFactory = null;

    static {
        try {
            Class slf4jClass = Class.forName("org.slf4j.LoggerFactory");
            if (slf4jClass != null) {
                register(new Slf4jLoggerFactory());
            }
        } catch (Exception e) {
        }
    }

    private LoggerFactory() {
    }

    /**
     * Register an ILoggerFactory instance to the system.
     *
     * @param factory
     */
    public static void register(ILoggerFactory factory) {
        _loggerFactories.put(factory.name(), factory);
        if (_loggerFactories.size() == 1) {
            _currentFactory = factory;
        }
    }

    /**
     * Set the ILoggerFactory to be used in the system.
     *
     * @param name the name of ILoggerFactory to use
     */
    public static void setCurrentFactory(String name) {
        ILoggerFactory factory = _loggerFactories.get(name);
        if (factory != null) {
            _currentFactory = factory;
        }
    }

    /**
     * Return a logger named corresponding to the class passed as parameter, using
     * the statically bound {@link ILoggerFactory} instance.
     *
     * @param clazz the returned logger will be named after clazz
     * @return logger
     */
    public static Logger getLogger(Class clazz) {
        return _currentFactory != null ? _currentFactory.getLogger(clazz) : DUMMY_LOGGER;
    }

    /**
     * Return an appropriate {@link Logger} instance as specified by the
     * <code>name</code> parameter.
     * <p/>
     * <p>Null-valued name arguments are considered invalid.
     *
     * @param name the name of the Logger to return
     * @return a Logger instance
     */
    public static Logger getLogger(String name) {
        return _currentFactory != null ? _currentFactory.getLogger(name) : DUMMY_LOGGER;
    }
}
