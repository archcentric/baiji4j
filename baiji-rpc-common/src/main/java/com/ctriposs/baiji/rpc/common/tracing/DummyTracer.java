package com.ctriposs.baiji.rpc.common.tracing;

/**
 * Created by yqdong on 2014/10/20.
 */
public class DummyTracer implements Tracer {
    @Override
    public Span startSpan(String spanName, String serviceName, SpanType spanType) {
        return null;
    }

    @Override
    public boolean isTracing() {
        return false;
    }

    @Override
    public void clear() {
    }

    @Override
    public Span continueSpan(String spanName, String serviceName, long traceId, long parentId, SpanType spanType) {
        return null;
    }

    @Override
    public Span getCurrentSpan() {
        return null;
    }
}
