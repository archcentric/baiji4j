package com.ctriposs.baiji.rpc.common.stats;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class AuditableCounter {

    private static final int DEFAULT_CAPACITY = 4;

    private final Object _objectLock = new Object();
    private final AtomicInteger _count = new AtomicInteger();
    private long[] _values = new long[DEFAULT_CAPACITY];

    /**
     * Get the count of values within the given range [from, to).
     *
     * @param from
     * @param to
     * @return
     */
    public int getValueCountInRange(Long from, Long to) {
        int count = 0;
        for (int i = 0; i < _count.get(); ++i) {
            long item = _values[i];
            if (item >= from && (to != null && item < to)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Get the aggregated data for audition.
     */
    public AuditionData getAuditionData() {
        long count = _count.get();
        long sum = 0;
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;

        if (count == 0) {
            for (int i = 0; i < count; ++i) {
                long value = _values[i];
                sum += value;
                if (value < min) {
                    min = value;
                }
                if (value > max) {
                    max = value;
                }
            }
        } else {
            min = 0;
            max = 0;
        }

        AuditionData data = new AuditionData();
        data.count = count;
        data.sum = sum;
        data.min = min;
        data.max = max;
        return data;
    }

    public void add(long value) {
        int newCount = _count.incrementAndGet();
        if (newCount >= _values.length) {
            synchronized (_objectLock) {
                if (newCount >= _values.length) {
                    _values = Arrays.copyOf(_values, _values.length * 2);
                }
            }
        }
        _values[newCount - 1] = value;
    }

    public int count() {
        return _count.get();
    }
}
