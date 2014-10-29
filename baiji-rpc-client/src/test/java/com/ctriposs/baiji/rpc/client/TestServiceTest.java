package com.ctriposs.baiji.rpc.client;

import com.ctriposs.baiji.rpc.testservice.GetItemsRequestType;
import com.ctriposs.baiji.rpc.testservice.GetItemsResponseType;
import com.ctriposs.baiji.rpc.testservice.TestServiceClient;
import org.junit.Ignore;
import org.junit.Test;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;

/**
 * Created by yqdong on 2014/10/28.
 */
@Ignore
public class TestServiceTest {

    private static final String SERVICE_URL = "http://localhost:8114/";
    private static final int DEFAULT_TIME_OUT = 10 * 1000;

    @Test
    public void testClient() throws Exception {
        TestServiceClient client = TestServiceClient.getInstance(SERVICE_URL);
        restoreDefault(client);

        final int count = 5;
        GetItemsRequestType request = new GetItemsRequestType();
        request.setTake(count);
        request.setValidationString("1234567");
        GetItemsResponseType type = client.getItems(request);
        assertEquals(count, type.getItems().size());
    }

    @Test(expected = org.apache.http.conn.ConnectTimeoutException.class)
    public void testConnectTimeoutException() throws Exception {
        TestServiceClient client = TestServiceClient.getInstance("http://www.google.com/test-service/");
        restoreDefault(client);
        client.setConnectTimeout(5);

        client.invoke("GetItems", new GetItemsRequestType(), GetItemsResponseType.class);
    }

    @Test(expected = SocketTimeoutException.class)
    public void testSocketTimeoutException() throws Exception {
        TestServiceClient client = TestServiceClient.getInstance(SERVICE_URL);
        client.setConnectTimeout(DEFAULT_TIME_OUT);
        client.setRequestTimeout(DEFAULT_TIME_OUT);
        client.setSocketTimeout(5);

        GetItemsRequestType request = new GetItemsRequestType();
        request.setSleep(1000);
        client.getItems(request);
    }

    @Test(expected = UnknownHostException.class)
    public void testHostException() throws Exception {
        TestServiceClient client = TestServiceClient.getInstance("http://unknown.host.you.know.it/");
        restoreDefault(client);

        GetItemsRequestType request = new GetItemsRequestType();
        request.setTake(5);
        request.setValidationString("1234567");
        client.getItems(request);
    }

    @Test
    public void testHttpException() throws Exception {
        TestServiceClient client = TestServiceClient.getInstance("http://localhost:8114/unknown/");
        restoreDefault(client);

        GetItemsRequestType request = new GetItemsRequestType();
        request.setTake(5);
        request.setValidationString("1234567");
        try {
            client.getItems(request);
        } catch (HttpWebException ex) {
            assertEquals("404", ex.getExceptionTypeName());
        }
    }

    @Test
    public void testServiceExceptionServerSideError() throws Exception {
        TestServiceClient client = TestServiceClient.getInstance(SERVICE_URL);
        restoreDefault(client);

        GetItemsRequestType request = new GetItemsRequestType();
        request.setTake(5);
        request.setValidationString("1234567");
        request.setGenerateRandomException(true);
        try {
            // Try multiple times because the exception generation is random.
            for (int i = 0; i < 5; ++i) {
                client.getItems(request);
            }
        } catch (ServiceException ex) {
            assertEquals("service_error", ex.getExceptionTypeName());
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void testMultiThread() throws Exception {
        final int threadCount = 5, operationCount = 20;

        final TestServiceClient client = TestServiceClient.getInstance(SERVICE_URL);
        restoreDefault(client);
        final AtomicLong lastMaxConnChangedTime = new AtomicLong(0);
        final int maxConnectionChangeInterval = 15 * 1000;
        final AtomicInteger maxConnections = new AtomicInteger(5);
        client.setMaxConnections(maxConnections.get());
        final AtomicInteger successCount = new AtomicInteger(), failedCount = new AtomicInteger();
        final CountDownLatch finishLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; ++i) {
            final int index = i;
            new Thread(new Runnable() {
                private final Random random_ = new Random();
                private final int threadIndex_ = index;

                @Override
                public void run() {
                    int succ = 0, failed = 0;
                    for (int j = 0; j < operationCount; ++j) {
                        GetItemsRequestType request = new GetItemsRequestType();
                        request.setTake(random_.nextInt(10) + 1);
                        request.setValidationString("1234567");
                        try {
                            GetItemsResponseType response = client.getItems(request);
                            if (response.getItems().size() == request.getTake()) {
                                ++succ;
                            } else {
                                ++failed;
                            }
                        } catch (Exception e) {
                            ++failed;
                        }
                        if (threadIndex_ == maxConnections.get()
                                && System.currentTimeMillis() - lastMaxConnChangedTime.get() > maxConnectionChangeInterval) {
                            lastMaxConnChangedTime.set(System.currentTimeMillis());
                            client.setMaxConnections(maxConnections.incrementAndGet());
                        }
                    }
                    successCount.addAndGet(succ);
                    failedCount.addAndGet(failed);
                    finishLatch.countDown();
                }
            }).start();
        }

        finishLatch.await();

        assertEquals(threadCount * operationCount, successCount.get());
        assertEquals(0, failedCount.get());
    }

    /**
     * Restore default settings for client instance.
     *
     * @param client service client needs to be updated.
     */
    private void restoreDefault(ServiceClientBase client) {
        client.setConnectTimeout(DEFAULT_TIME_OUT);
        client.setRequestTimeout(DEFAULT_TIME_OUT);
        client.setSocketTimeout(DEFAULT_TIME_OUT);
    }
}
