package com.ctriposs.baiji.rpc.samples.math;

import com.ctriposs.baiji.rpc.client.*;
import com.ctriposs.baiji.rpc.common.types.*;
import java.io.IOException;

public class MathServiceClient extends ServiceClientBase<MathServiceClient> {
    public static final String ORIGINAL_SERVICE_NAME = "Hello";

    public static final String ORIGINAL_SERVICE_NAMESPACE = "http://soa.ctriposs.com/baijirpc/sample/math";

    private MathServiceClient(String baseUri) {
        super(MathServiceClient.class, baseUri);
    }

    private MathServiceClient(String serviceName, String serviceNamespace, String subEnv) throws ServiceLookupException {
        super(MathServiceClient.class, serviceName, serviceNamespace, subEnv);
    }

    public GetFactorialResponseType getFactorial(GetFactorialRequestType request)
                                    throws ServiceException, HttpWebException, IOException {
        return super.invoke("getFactorial", request, GetFactorialResponseType.class);
    }
    public com.ctriposs.baiji.rpc.common.types.CheckHealthResponseType checkHealth(com.ctriposs.baiji.rpc.common.types.CheckHealthRequestType request)
                                    throws ServiceException, HttpWebException, IOException {
        return super.invoke("checkHealth", request, com.ctriposs.baiji.rpc.common.types.CheckHealthResponseType.class);
    }
}