package com.ctriposs.baiji.rpc.common.tracing;


/**
 * An interface used to retrieve and operate span status.
 *
 * Multiple spans can build a tree through parent-children relationship. There is no parent span for the root span.
 *
 * Created by yqdong on 2014/10/20.
 */
public interface Span {

    public static final long ROOT_SPAN_ID = 0l;

    /**
     * Stop the span.
     */
    void stop();

    /**
     * Gets a value indicating whether the span has been stopped.
     */
    boolean isStopped();

    /**
     * Gets the start time of the span in milliseconds.
     *
     * @return
     */
    long getStartTimeMillis();

    /**
     * Gets the stop time in milliseconds.
     *
     * @return stop time in millis
     */
    long getStopTimeMillis();


    /**
     * Gets the accumulated open duration in milliseconds.
     *
     * @return accumulated duration in millis
     */
    long getAccumulateMillis();

    /**
     * Gets a value indicating whether the span is running or not.
     *
     * @return the running status of the span
     */
    boolean isRunning();

    /**
     * Gets a string description of the span.
     *
     * @return
     */
    String getDescription();

    /**
     * The ID of the span.
     *
     * @return
     */
    long getSpanId();

    /**
     * The parent span. Return null if this is a root span.
     *
     * @return parent span
     */
    Span getParent();

    /**
     * The ID of trace the span is associated with.
     *
     * All the spans within one trace share the same trace ID.
     *
     * @return
     */
    long getTraceId();

    /**
     * The ID of parent span. Returns ROOT_SPAN_ID for root spans.
     *
     * @return parent
     */
    long getParentId();
}
