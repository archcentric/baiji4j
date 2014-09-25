package com.ctriposs.baiji.rpc.samples.math;

import com.ctriposs.baiji.rpc.client.ServiceClientConfig;

public class Client {

    public static void main(String[] args) throws Exception {
        ServiceClientConfig config = new ServiceClientConfig();
        config.setServiceRegistryUrl("http://localhost:4001");
        MathServiceClient.initialize(config);

//        MathServiceClient client = MathServiceClient.getInstance(MathServiceClient.class);
        MathServiceClient client = MathServiceClient.getInstance(MathServiceClient.class, "http://localhost:8115/");
        GetFactorialRequestType request = new GetFactorialRequestType(5L);
        GetFactorialResponseType response = client.getFactorial(request);
        System.out.println(response.getResponseStatus());
        System.out.println(response.getResult());

        System.exit(0);
    }
}
