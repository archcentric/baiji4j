package com.ctriposs.baiji.rpc.samples.hello;

import com.ctriposs.baiji.rpc.client.*;
import com.ctriposs.baiji.rpc.common.types.*;
import java.io.IOException;

public class HelloServiceClient extends ServiceClientBase<HelloServiceClient> {
    public static final String ORIGINAL_SERVICE_NAME = "Hello";

    public static final String ORIGINAL_SERVICE_NAMESPACE = "http://soa.ctriposs.com/baijirpc/sample/hello";

    private HelloServiceClient(String baseUri) {
        super(HelloServiceClient.class, baseUri);
    }

    private HelloServiceClient(String serviceName, String serviceNamespace, String subEnv) throws ServiceLookupException {
        super(HelloServiceClient.class, serviceName, serviceNamespace, subEnv);
    }

    public HelloResponseType sayHello(HelloRequestType request)
                                    throws ServiceException, HttpWebException, IOException {
        return super.invoke("sayHello", request, HelloResponseType.class);
    }
    public com.ctriposs.baiji.rpc.common.types.CheckHealthResponseType checkHealth(com.ctriposs.baiji.rpc.common.types.CheckHealthRequestType request)
                                    throws ServiceException, HttpWebException, IOException {
        return super.invoke("checkHealth", request, com.ctriposs.baiji.rpc.common.types.CheckHealthResponseType.class);
    }
}