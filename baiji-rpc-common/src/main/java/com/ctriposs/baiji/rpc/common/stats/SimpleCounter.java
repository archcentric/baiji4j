package com.ctriposs.baiji.rpc.common.stats;

import java.util.concurrent.atomic.AtomicLong;

public class SimpleCounter {

    private final AtomicLong _value = new AtomicLong();

    public void inc() {
        _value.addAndGet(1);
    }

    public void dec() {
        _value.decrementAndGet();
    }

    public long get() {
        return _value.get();
    }

    public void reset() {
        _value.getAndSet(0);
    }

    public long getThenReset() {
        return _value.getAndSet(0);
    }
}
