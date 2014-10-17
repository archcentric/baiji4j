package com.ctriposs.baiji.rpc.testservice;

import com.ctriposs.baiji.rpc.common.BaijiContract;

@BaijiContract(serviceName = "TestService", serviceNamespace = "http://soa.ctriposs.com/baijirpc/testservice", codeGeneratorVersion = "1.1.0.0")
public interface TestService {

    GetItemsResponseType getItems(GetItemsRequestType request) throws Exception;

    com.ctriposs.baiji.rpc.common.types.CheckHealthResponseType checkHealth(com.ctriposs.baiji.rpc.common.types.CheckHealthRequestType request) throws Exception;
}