package com.ctriposs.baiji.rpc.common.tracing;

/**
 * Created by yqdong on 2014/10/20.
 */
public interface Tracer {

    /**
     * Starts a trace span
     * <p/>
     * If there is no started span in the current thread span, a new root span will be started.
     * Otherwise, a child span of the current span will be created and set to current span.
     *
     * @param spanName    the name of the span
     * @param serviceName the name of the service
     * @param spanType    the type of the span
     * @return an ISpan instance
     */
    Span startSpan(String spanName, String serviceName, SpanType spanType);

    /**
     * Gets a value indicating whether tracing has been started.
     *
     * @return the tracing status
     */
    boolean isTracing();

    /**
     * Clean up all the spans in the current thread space.
     */
    void clear();

    /**
     * Continue tracing based on the specified information.
     * It shall be used to continue tracing across process boundary, such as invoking another process or a web service.
     * The target process or service can use these information to continue tracing.
     *
     * @param spanName    span name
     * @param serviceName service name
     * @param traceId     trace id
     * @param parentId    parent id
     * @param spanType    span type
     */
    Span continueSpan(String spanName, String serviceName, long traceId, long parentId, SpanType spanType);

    /**
     * Gets the current opened span.
     *
     * @return current span
     */
    Span getCurrentSpan();
}
