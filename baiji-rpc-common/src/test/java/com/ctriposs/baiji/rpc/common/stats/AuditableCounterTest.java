package com.ctriposs.baiji.rpc.common.stats;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

/**
 * Created by yqdong on 2014/10/28.
 */
public class AuditableCounterTest {

    private static final int THREAD_COUNT = 30;
    private static final long ITEM_COUNT = 1000000L;
    private AuditableCounter _counter;

    @Before
    public void before() {
        _counter = new AuditableCounter();
    }

    @After
    public void after() {
        _counter = null;
    }

    @Test
    public void integrityTest() throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);
        try {
            List<Future<?>> futures = new ArrayList<Future<?>>();
            for (int i = 1; i <= THREAD_COUNT; i++) {
                futures.add(service.submit(new Runnable() {

                    @Override
                    public void run() {
                        for (int j = 1; j <= ITEM_COUNT; ++j) {
                            _counter.add(j);
                        }
                        _counter.getValueCountInRange(0L, ITEM_COUNT + 1);
                    }

                }));
            }

            for (Future<?> future : futures) {
                future.get();
            }

            assertEquals(_counter.getValueCountInRange(0L, ITEM_COUNT + 1), _counter.count());
            AuditionData auditionData = _counter.getAuditionData();
            assertEquals(ITEM_COUNT * THREAD_COUNT, auditionData.count);
            assertEquals(THREAD_COUNT * ((ITEM_COUNT + 1) * ITEM_COUNT / 2), auditionData.sum);
            assertEquals(1, auditionData.min);
            assertEquals(ITEM_COUNT, auditionData.max);
            assertEquals(100 * THREAD_COUNT, _counter.getValueCountInRange(100L, 200L));
        } finally {
            service.shutdown();
        }
    }
}
