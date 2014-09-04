package com.ctriposs.baiji.rpc.samples.crosstest;

import com.ctriposs.baiji.rpc.common.BaijiContract;

@BaijiContract(serviceName = "TestSerialize", serviceNamespace = "http://soa.ctriposs.com/baijirpc/sample/crosstest", codeGeneratorVersion = "1.0.0.0")
public interface TestService {

    CrossTestResponseType testSerialize(CrossTestRequestType request);
}