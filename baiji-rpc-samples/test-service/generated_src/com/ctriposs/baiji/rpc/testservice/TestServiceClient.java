package com.ctriposs.baiji.rpc.testservice;

import com.ctriposs.baiji.rpc.client.*;
import com.ctriposs.baiji.rpc.common.types.*;
import java.io.IOException;

public class TestServiceClient extends ServiceClientBase<TestServiceClient> {
    public static final String ORIGINAL_SERVICE_NAME = "TestService";

    public static final String ORIGINAL_SERVICE_NAMESPACE = "http://soa.ctriposs.com/baijirpc/testservice";

    private TestServiceClient(String baseUri) {
        super(TestServiceClient.class, baseUri);
    }

    private TestServiceClient(String serviceName, String serviceNamespace, String subEnv) throws ServiceLookupException {
        super(TestServiceClient.class, serviceName, serviceNamespace, subEnv);
    }

    public GetItemsResponseType getItems(GetItemsRequestType request)
                                    throws ServiceException, HttpWebException, IOException {
        return super.invoke("getItems", request, GetItemsResponseType.class);
    }
}