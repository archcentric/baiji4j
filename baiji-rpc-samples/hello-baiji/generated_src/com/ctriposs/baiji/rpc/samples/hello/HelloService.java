package com.ctriposs.baiji.rpc.samples.hello;

import com.ctriposs.baiji.rpc.common.BaijiContract;

@BaijiContract(serviceName = "Hello", serviceNamespace = "http://soa.ctriposs.com/baijirpc/sample/hello", codeGeneratorVersion = "1.0.0.0")
public interface HelloService {

    HelloResponseType sayHello(HelloRequestType request) throws Exception;

    com.ctriposs.baiji.rpc.common.types.CheckHealthResponseType checkHealth(com.ctriposs.baiji.rpc.common.types.CheckHealthRequestType request) throws Exception;
}