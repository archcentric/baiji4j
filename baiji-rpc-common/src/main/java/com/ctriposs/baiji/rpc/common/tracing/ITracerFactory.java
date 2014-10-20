package com.ctriposs.baiji.rpc.common.tracing;

/**
 * Created by yqdong on 2014/10/20.
 */
public interface ITracerFactory {

    /**
     * Return the name of the tracer factory specifying the type of tracer it manages.
     *
     * @return
     */
    String name();

    /**
     * Gets a tracer instance based on the given type.
     *
     * @param type The type
     * @return Tracer instance
     */
    Tracer getTracer(Class<?> type);

    /**
     * Gets a tracer instance based on the given name.
     *
     * @param name tracer name
     * @return Tracer instance
     */
    Tracer getTracer(String name);
}
