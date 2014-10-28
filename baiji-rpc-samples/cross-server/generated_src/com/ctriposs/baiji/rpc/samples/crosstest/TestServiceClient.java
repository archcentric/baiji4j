package com.ctriposs.baiji.rpc.samples.crosstest;

import com.ctriposs.baiji.rpc.client.*;
import com.ctriposs.baiji.rpc.common.types.*;
import java.io.IOException;

public class TestServiceClient extends ServiceClientBase<TestServiceClient> {
    public static final String ORIGINAL_SERVICE_NAME = "TestSerialize";

    public static final String ORIGINAL_SERVICE_NAMESPACE = "http://soa.ctriposs.com/baijirpc/sample/crosstest";

    public static final String CODE_GENERATOR_VERSION = "1.1.0.0";

    private TestServiceClient(String baseUri) {
        super(TestServiceClient.class, baseUri);
    }

    private TestServiceClient(String serviceName, String serviceNamespace, String subEnv) throws ServiceLookupException {
        super(TestServiceClient.class, serviceName, serviceNamespace, subEnv);
    }

    public static TestServiceClient getInstance() {
        return ServiceClientBase.getInstance(TestServiceClient.class);
    }

    public static TestServiceClient getInstance(String baseUrl) {
        return ServiceClientBase.getInstance(TestServiceClient.class, baseUrl);
    }

    public CrossTestResponseType testSerialize(CrossTestRequestType request)
                                    throws Exception {
        return super.invoke("testSerialize", request, CrossTestResponseType.class);
    }
    public com.ctriposs.baiji.rpc.common.types.CheckHealthResponseType checkHealth(com.ctriposs.baiji.rpc.common.types.CheckHealthRequestType request)
                                    throws Exception {
        return super.invoke("checkHealth", request, com.ctriposs.baiji.rpc.common.types.CheckHealthResponseType.class);
    }
}