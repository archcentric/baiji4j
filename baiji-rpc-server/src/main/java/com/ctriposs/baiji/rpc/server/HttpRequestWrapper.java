package com.ctriposs.baiji.rpc.server;

import com.ctriposs.baiji.specific.SpecificRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

public interface HttpRequestWrapper {

    /**
     * An InputStream with the request body, if any.
     * null MAY be used as a placeholder if there is no request body.
     */
    public InputStream requestBody() throws IOException;

    /**
     * A map of all request headers.
     */
    public Map<String, String> requestHeaders();

    /**
     * The HTTP request method of the request (e.g., "GET", "POST").
     */
    public String httpMethod();

    /**
     * The request path. The path MUST be relative to the "root" of the service mapping.
     */
    public String requestPath();

    /**
     * The full request URL.
     */
    public String requestUrl();

    /**
     * The URI scheme used for the request (e.g., "http", "https").
     */
    public String httpScheme();

    /**
     * The actual IP of the client.
     * X-FORWRAD-FOR and X-REAL-IP may be used to determine th value.
     * @return
     */
    public String clientIp();

    /**
     * The query string component of the HTTP request URI, without the leading "?" (e.g., "foo=bar&baz=quux").
     * The value may be an empty string.
     */
    public String queryString();

    /**
     * The extracted operation name. MAY be null if the request is invalid or not a service request.
     */
    public String operationName();

    public void setOperationName(String operationName);

    /**
     * The expected response content type of the request.
     */
    public String responseContentType();

    public void setResponseContentType(String contentType);

    /**
     * The deserialized request object.
     */
    public SpecificRecord requestObject();

    public void setRequestObject(SpecificRecord obj);

    /**
     * The handler of requested operation.
     */
    public OperationHandler operationHandler();

    public void setOperationHandler(OperationHandler handler);

    /**
     * The key-value map of query string.
     * @return
     */
    public Map<String, String> queryMap();
}
