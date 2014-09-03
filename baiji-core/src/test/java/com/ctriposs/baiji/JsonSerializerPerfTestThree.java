package com.ctriposs.baiji;

import com.ctriposs.baiji.specific.TestSerializerSampleList;
import org.junit.Before;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class JsonSerializerPerfTestThree {

    JsonSerializer serializer;

    @Before
    public void setUp() throws Exception {
        serializer = new JsonSerializer();
    }

    private void testDeserialize(int threadCount, int loop) {
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            Serializer ser = new Serializer(countDownLatch, loop);
            Thread thread = new Thread(ser);
            thread.start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e){/**/}
    }

    class Serializer implements Runnable {

        private CountDownLatch cdl;
        private int loop;

        public List<Long> results = new ArrayList<>();

        public Serializer(CountDownLatch cdl, int loop) {
            this.cdl = cdl;
            this.loop = loop;
        }

        @Override
        public void run() {
            for (int i = 0; i < loop; i++) {
                try (InputStream is = JsonSerializerPerfTestThree.class.getResourceAsStream("/t50records.json")) {
                    long start = System.currentTimeMillis();
                    TestSerializerSampleList sample = serializer.deserialize(TestSerializerSampleList.class, is);
                    long end = System.currentTimeMillis();
                    results.add(end - start);
                } catch (IOException e) {/**/}
            }

            cdl.countDown();
        }
    }
}
