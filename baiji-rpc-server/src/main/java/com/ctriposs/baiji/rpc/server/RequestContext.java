package com.ctriposs.baiji.rpc.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class RequestContext {

    /**
     * Required:Yes, A InputStream with the request body, if any.
     * null MAY be used as a placeholder if there is no request body.
     */
    public InputStream RequestBody;
    /**
     * Required:Yes, An Map of request headers.
     */
    public Map<String, String> RequestHeaders;
    /**
     * Required:Yes, A string containing the HTTP request method of the request (e.g., "GET", "POST").
     */
    public String RequestMethod;
    /**
     * Required:Yes, A string containing the request path. The path MUST be relative to the "root" of the application.
     */
    public String RequestPath;
    /**
     * Required:Yes, A string containing the portion of the request path corresponding to the "root" of the application.
     */
    public String RequestPathBase;
    /**
     * Required:Yes, A string containing the protocol name and version (e.g. "HTTP/1.0" or "HTTP/1.1").
     */
    public String RequestProtocol;
    /**
     * Required:Yes, A string containing the query string component of the HTTP request URI, without the leading ��?�� (e.g., "foo=bar&baz=quux"). The value may be an empty string.
     */
    public String RequestQueryString;

    /**
     * Request extension
     */
    public String RequestExtention;

    /**
     * Required:Yes, A string containing the URI scheme used for the request (e.g., "http", "https").
     */
    public String RequestScheme;
    /**
     * Required:Yes, A Stream used to write out the response body, if any.
     */
    public OutputStream ResponseBody;
    /**
     * Required:Yes, An IDictionary of response headers.
     */
    public Map<String, String> ResponseHeaders;
    /**
     * Required:No, An optional int containing the HTTP response status code as defined in RFC 2616 section 6.1.1. The default is 200.
     */
    public Integer ResponseStatusCode = 200;
    /**
     * Required:No, An optional string containing the protocol name and version (e.g. "HTTP/1.0" or "HTTP/1.1"). If none is provided then the ��RequestProtocol�� value is the default.
     */
    public String ResponseProtocol;
    /**
     * Required:Yes, The string "1.0" indicating Baiji RPC version.
     */
    public String Version = "1.0";

}
