package com.ctriposs.baiji.rpc.samples.hello;

import com.ctriposs.baiji.rpc.common.types.CheckHealthRequestType;
import com.ctriposs.baiji.rpc.common.types.CheckHealthResponseType;

public class HelloServiceImpl implements HelloService {
    @Override
    public HelloResponseType sayHello(HelloRequestType request) {
        if (request == null || request.name == null || request.name.isEmpty()) {
            throw new IllegalArgumentException("Missing name parameter");
        }
        return new HelloResponseType(null, "Hello " + request.name);
    }

    @Override
    public CheckHealthResponseType checkHealth(CheckHealthRequestType request) {
        return new CheckHealthResponseType();
    }
}
