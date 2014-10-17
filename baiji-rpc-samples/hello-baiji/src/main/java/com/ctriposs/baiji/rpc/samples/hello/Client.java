package com.ctriposs.baiji.rpc.samples.hello;

import com.ctriposs.baiji.rpc.client.ServiceClientConfig;

public class Client {

    public static void main(String[] args) throws Exception {
        ServiceClientConfig config = new ServiceClientConfig();
        config.setServiceRegistryUrl("http://localhost:4001");
        HelloServiceClient.initialize(config);

//        HelloServiceClient client = HelloServiceClient.getInstance("http://localhost:8111/");
        HelloServiceClient client = HelloServiceClient.getInstance();
        HelloRequestType request = new HelloRequestType("World");
        HelloResponseType response = client.sayHello(request);
        System.out.println(response.getResponseStatus());
        System.out.println(response.getMessage());

        System.exit(0);
    }
}
