package com.ctriposs.baiji.rpc.testservice;

/**
 * Created by yqdong on 2014/9/8.
 */
public class Client {

    public static void main(String[] args) throws Exception {
//        ServiceClientConfig config = new ServiceClientConfig();
//        config.setServiceRegistryUrl("http://localhost:4001");
//        config.setSubEnv("dev");
//        config.setServiceSubEnv(TestServiceClient.ORIGINAL_SERVICE_NAME, TestServiceClient.ORIGINAL_SERVICE_NAMESPACE, "dev");
//        TestServiceClient.initialize(config);

//        TestServiceClient client = TestServiceClient.getInstance(TestServiceClient.class);
        TestServiceClient client = TestServiceClient.getInstance(TestServiceClient.class, "http://localhost:8114/");
        GetItemsRequestType request = new GetItemsRequestType();
        request.setTake(5);
        GetItemsResponseType response = client.getItems(request);
        if (response.items == null || response.items.isEmpty()) {
            System.out.println("No item found.");
        } else {
            for (Item movie : response.items) {
                System.out.println(String.format("#%s %s", movie.itemId, movie.title));
            }
        }

        System.exit(0);
    }
}
