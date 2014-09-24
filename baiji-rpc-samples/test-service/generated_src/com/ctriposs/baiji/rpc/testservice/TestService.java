package com.ctriposs.baiji.rpc.testservice;

import com.ctriposs.baiji.rpc.common.BaijiContract;

@BaijiContract(serviceName = "TestService", serviceNamespace = "http://soa.ctriposs.com/baijirpc/testservice", codeGeneratorVersion = "1.0.0.0")
public interface TestService {

    GetItemsResponseType getItems(GetItemsRequestType request) throws Exception;
}