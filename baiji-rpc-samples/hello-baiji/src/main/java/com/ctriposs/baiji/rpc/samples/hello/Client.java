package com.ctriposs.baiji.rpc.samples.hello;

public class Client {

    public static void main(String[] args) throws Exception {
        HelloServiceClient client = HelloServiceClient.getInstance(HelloServiceClient.class, "http://localhost:8111/");
        HelloRequestType request = new HelloRequestType("World");
        HelloResponseType response = client.sayHello(request);
        System.out.println(response.getResponseStatus());
        System.out.println(response.getMessage());
    }
}
