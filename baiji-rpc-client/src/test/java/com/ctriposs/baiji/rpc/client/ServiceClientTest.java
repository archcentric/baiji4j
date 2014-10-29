package com.ctriposs.baiji.rpc.client;

import com.ctriposs.baiji.rpc.client.filter.HttpRequestFilter;
import com.ctriposs.baiji.rpc.client.filter.HttpResponseFilter;
import com.ctriposs.baiji.rpc.testservice.GetItemsRequestType;
import com.ctriposs.baiji.rpc.testservice.GetItemsResponseType;
import com.ctriposs.baiji.rpc.testservice.TestServiceClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.HttpHostConnectException;
import org.junit.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yqdong on 2014/10/28.
 */
@Ignore
public class ServiceClientTest {

    private static final String SERVICE_URL = "http://localhost:8114/";
    private static final String SERVICE_REGISTRY_URL = "http://localhost:4001/";
    private static final String SUB_ENV = "dev";

    private TestServiceClient _client;


    @BeforeClass
    public static void initialize() {
        ServiceClientConfig config = new ServiceClientConfig();
        config.setServiceRegistryUrl(SERVICE_REGISTRY_URL);
        config.setSubEnv(SUB_ENV);
        TestServiceClient.initialize(config);
    }

    @After
    public void after() {
        if (_client != null) {
            resetFilters(_client);
            _client = null;
        }
    }

    @Test
    public void testDirectClientCache() {
        TestServiceClient client1 = TestServiceClient.getInstance(SERVICE_URL);
        TestServiceClient client2 = TestServiceClient.getInstance(SERVICE_URL);
        Assert.assertSame(client1, client2);
    }

    @Test
    public void testIndirectClientCache() {
        TestServiceClient client1 = TestServiceClient.getInstance();
        TestServiceClient client2 = TestServiceClient.getInstance();
        Assert.assertSame(client1, client2);
    }

    @Test
    public void testDirectServiceInvoke() throws Exception {
        _client = TestServiceClient.getInstance(SERVICE_URL);
        GetItemsRequestType request = new GetItemsRequestType();
        request.setTake(5);
        request.setValidationString("123456");
        GetItemsResponseType response = _client.getItems(request);
        Assert.assertEquals(5, response.items.size());
    }

    @Test
    public void testIndirectServiceInvoke() throws Exception {
        _client = TestServiceClient.getInstance();
        GetItemsRequestType request = new GetItemsRequestType();
        request.setTake(5);
        request.setValidationString("123456");
        GetItemsResponseType response = _client.getItems(request);
        Assert.assertEquals(5, response.items.size());
    }


    @Test
    public void testIndirectServiceLoadBalance() throws Exception {
        final int REQUEST_COUNT = 50;

        _client = TestServiceClient.getInstance();

        final ConcurrentMap<String, AtomicInteger> serviceUsedCounts = new ConcurrentHashMap<>();

        _client.setLocalHttpRequestFilter(new HttpRequestFilter() {
            @Override
            public void apply(HttpRequestBase request) {
                String uri = request.getURI().toString();
                AtomicInteger count = serviceUsedCounts.get(uri);
                if (count == null) {
                    count = new AtomicInteger();
                    AtomicInteger existedCount = serviceUsedCounts.putIfAbsent(uri, count);
                    if (existedCount != null) {
                        count = existedCount;
                    }
                }
                count.incrementAndGet();
            }
        });

        for (int i = 0; i < REQUEST_COUNT; ++i) {
            GetItemsRequestType request = new GetItemsRequestType();
            request.setTake(5);
            request.setValidationString("123456");
            GetItemsResponseType response = _client.getItems(request);
            Assert.assertEquals(5, response.items.size());
        }

        int expectedCount = REQUEST_COUNT / serviceUsedCounts.size();
        int collectedCount = 0;
        for (Map.Entry<String, AtomicInteger> entry : serviceUsedCounts.entrySet()) {
            int count = entry.getValue().get();
            collectedCount += count;
            Assert.assertTrue(expectedCount == count || expectedCount + 1 == count);
        }
        Assert.assertEquals(REQUEST_COUNT, collectedCount);
    }

    @Test
    public void testFilters() throws Exception {
        _client = TestServiceClient.getInstance(SERVICE_URL);

        final AtomicInteger filterExecutionState = new AtomicInteger();

        _client.setLocalHttpRequestFilter(new HttpRequestFilter() {
            @Override
            public void apply(HttpRequestBase request) {
                Assert.assertEquals(1, filterExecutionState.incrementAndGet());
            }
        });
        TestServiceClient.setGlobalHttpRequestFilter(new HttpRequestFilter() {
            @Override
            public void apply(HttpRequestBase request) {
                Assert.assertEquals(2, filterExecutionState.incrementAndGet());
            }
        });
        TestServiceClient.setGlobalHttpResponseFilter(new HttpResponseFilter() {
            @Override
            public void apply(HttpResponse response) {
                Assert.assertEquals(3, filterExecutionState.incrementAndGet());
            }
        });
        _client.setLocalHttpResponseFilter(new HttpResponseFilter() {
            @Override
            public void apply(HttpResponse response) {
                Assert.assertEquals(4, filterExecutionState.incrementAndGet());
            }
        });

        GetItemsRequestType request = new GetItemsRequestType();
        request.setTake(5);
        request.setValidationString("123456");
        GetItemsResponseType response = _client.getItems(request);
        Assert.assertEquals(5, response.items.size());
    }

    @Test(expected = HttpHostConnectException.class)
    public void testResponseFiltersWithHttpHostConnectException() throws Exception {
        _client = TestServiceClient.getInstance("http://localhost:56731/");

        _client.setLocalHttpResponseFilter(new HttpResponseFilter() {
            @Override
            public void apply(HttpResponse response) {
                Assert.fail("Response Filter shall not be executed when web exception is thrown.");
            }
        });
        TestServiceClient.setGlobalHttpResponseFilter(new HttpResponseFilter() {
            @Override
            public void apply(HttpResponse response) {
                Assert.fail("Response Filter shall not be executed when web exception is thrown.");
            }
        });

        GetItemsRequestType request = new GetItemsRequestType();
        request.setTake(5);
        request.setValidationString("123456");
        _client.getItems(request);
    }

    @Test(expected = ServiceException.class)
    public void testResponseFiltersWithServiceException() throws Exception {
        _client = TestServiceClient.getInstance(SERVICE_URL);

        final AtomicInteger filterExecutionState = new AtomicInteger();

        TestServiceClient.setGlobalHttpResponseFilter(new HttpResponseFilter() {
            @Override
            public void apply(HttpResponse response) {
                Assert.assertEquals(1, filterExecutionState.incrementAndGet());
            }
        });
        _client.setLocalHttpResponseFilter(new HttpResponseFilter() {
            @Override
            public void apply(HttpResponse response) {
                Assert.assertEquals(2, filterExecutionState.incrementAndGet());
            }
        });

        GetItemsRequestType request = new GetItemsRequestType();
        request.setTake(5);
        request.setValidationString("123456");
        request.setReturnWrappedErrorResponse(true);
        _client.getItems(request);
    }

    @Test(expected = HttpWebException.class)
    public void testResponseFiltersWithHttpWebExeption() throws Exception {
        _client = TestServiceClient.getInstance(SERVICE_URL + "test-service/");

        final AtomicInteger filterExecutionState = new AtomicInteger();

        TestServiceClient.setGlobalHttpRequestFilter(new HttpRequestFilter() {
            @Override
            public void apply(HttpRequestBase request) {
                Assert.assertEquals(1, filterExecutionState.incrementAndGet());
            }
        });
        TestServiceClient.setGlobalHttpResponseFilter(new HttpResponseFilter() {
            @Override
            public void apply(HttpResponse response) {
                Assert.assertEquals(2, filterExecutionState.incrementAndGet());
            }
        });

        GetItemsRequestType request = new GetItemsRequestType();
        request.setTake(5);
        request.setValidationString("123456");
        _client.getItems(request);
    }

    private void resetFilters(TestServiceClient client) {
        TestServiceClient.setGlobalHttpRequestFilter(null);
        TestServiceClient.setGlobalHttpResponseFilter(null);
        client.setLocalHttpRequestFilter(null);
        client.setLocalHttpResponseFilter(null);
    }
}
