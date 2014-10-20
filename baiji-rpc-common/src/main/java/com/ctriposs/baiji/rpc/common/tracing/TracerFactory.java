package com.ctriposs.baiji.rpc.common.tracing;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by yqdong on 2014/10/20.
 */
public final class TracerFactory {

    private static final Tracer DUMMY_TRACER = new DummyTracer();

    private static final ConcurrentMap<String, ITracerFactory> _tracerFactories = new ConcurrentHashMap<>();

    private static ITracerFactory _currentFactory = null;

    private TracerFactory() {
    }

    /**
     * Register an ITracerFactory instance to the system.
     *
     * @param factory
     */
    public static void register(ITracerFactory factory) {
        _tracerFactories.put(factory.name(), factory);
        if (_tracerFactories.size() == 1) {
            _currentFactory = factory;
        }
    }

    /**
     * Set the ITracerFactory to be used in the system.
     *
     * @param name the name of ITracerFactory to use
     */
    public static void setCurrentFactory(String name) {
        ITracerFactory factory = _tracerFactories.get(name);
        if (factory != null) {
            _currentFactory = factory;
        }
    }

    /**
     * Gets a tracer instance based on the given type.
     *
     * @param clazz The type
     * @return ITrace instance
     */
    public static Tracer getTracer(Class<?> clazz) {
        return _currentFactory != null ? _currentFactory.getTracer(clazz) : DUMMY_TRACER;
    }

    /**
     * Gets a tracer instance based on the given name.
     *
     * @param name tracer name
     * @return Tracer instance
     */
    public static Tracer getTracer(String name) {
        return _currentFactory != null ? _currentFactory.getTracer(name) : DUMMY_TRACER;
    }
}
