package com.ctriposs.baiji.rpc.samples.math;

import com.ctriposs.baiji.rpc.common.BaijiContract;

@BaijiContract(serviceName = "Hello", serviceNamespace = "http://soa.ctriposs.com/baijirpc/sample/math", codeGeneratorVersion = "1.0.0.0")
public interface MathService {

    GetFactorialResponseType getFactorial(GetFactorialRequestType request) throws Exception;

    com.ctriposs.baiji.rpc.common.types.CheckHealthResponseType checkHealth(com.ctriposs.baiji.rpc.common.types.CheckHealthRequestType request) throws Exception;
}