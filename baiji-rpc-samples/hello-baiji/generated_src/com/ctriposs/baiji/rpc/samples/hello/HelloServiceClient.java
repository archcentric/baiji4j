package com.ctriposs.baiji.rpc.samples.hello;

import com.ctriposs.baiji.rpc.client.*;
import com.ctriposs.baiji.rpc.common.types.*;
import java.io.IOException;

public class HelloServiceClient extends ServiceClientBase<HelloServiceClient> {
    public static final String ORIGINAL_SERVICE_NAME = "Hello";

    public static final String ORIGINAL_SERVICE_NAMESPACE = "http://soa.ctriposs.com/baijirpc/sample/hello";

    public static final String CODE_GENERATOR_VERSION = "1.1.0.0";

    private HelloServiceClient(String baseUri) {
        super(HelloServiceClient.class, baseUri);
    }

    private HelloServiceClient(String serviceName, String serviceNamespace, String subEnv) throws ServiceLookupException {
        super(HelloServiceClient.class, serviceName, serviceNamespace, subEnv);
    }

    public static HelloServiceClient getInstance() {
        return ServiceClientBase.getInstance(HelloServiceClient.class);
    }

    public static HelloServiceClient getInstance(String baseUrl) {
        return ServiceClientBase.getInstance(HelloServiceClient.class, baseUrl);
    }

    public HelloResponseType sayHello(HelloRequestType request)
                                    throws Exception {
        return super.invoke("sayHello", request, HelloResponseType.class);
    }
    public com.ctriposs.baiji.rpc.common.types.CheckHealthResponseType checkHealth(com.ctriposs.baiji.rpc.common.types.CheckHealthRequestType request)
                                    throws Exception {
        return super.invoke("checkHealth", request, com.ctriposs.baiji.rpc.common.types.CheckHealthResponseType.class);
    }
}