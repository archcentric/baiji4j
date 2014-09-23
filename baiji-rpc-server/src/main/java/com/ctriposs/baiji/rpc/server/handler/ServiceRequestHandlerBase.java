package com.ctriposs.baiji.rpc.server.handler;

import com.ctriposs.baiji.rpc.server.*;
import com.ctriposs.baiji.rpc.server.filter.PreRequestFilter;
import com.ctriposs.baiji.rpc.server.filter.RequestFilter;
import com.ctriposs.baiji.rpc.server.filter.ResponseFilter;
import com.ctriposs.baiji.rpc.server.util.RequestUtil;
import com.ctriposs.baiji.rpc.server.util.ResponseUtil;
import com.ctriposs.baiji.specific.SpecificRecord;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by yqdong on 2014/9/18.
 */
public abstract class ServiceRequestHandlerBase implements RequestHandler {

    protected final Logger _logger;

    protected ServiceRequestHandlerBase() {
        _logger = LoggerFactory.getLogger(getClass());
    }

    /**
     * Process request and find out operation name and request content type info.
     *
     * @param host
     * @param request
     * @return A boolean value indicating whether the operation can be processed by this handler.
     */
    protected abstract boolean processOperationData(ServiceHost host, HttpRequestWrapper request);

    public void handle(ServiceHost host, HttpRequestWrapper request, HttpResponseWrapper response)
            throws Exception {
        if (!processOperationData(host, request)) {
            return;
        }

        OperationHandler handler = host.getServiceMetaData().getOperationHandler(request.operationName());
        if (handler == null) {
            return; // Nothing more to do
        }

        request.setOperationHandler(handler);

        if (applyPreRequestFilters(host, handler, request, response)) {
            return;
        }

        SpecificRecord requestObject;
        // ParameterBinding or Deserialization
        if ("GET".equalsIgnoreCase(request.httpMethod())) { // REST call, for testing only
            requestObject = handler.getEmptyRequestInstance();
            // Request parameters binding
            Map<String, String> requestQueryMap = request.queryMap();
            if (requestQueryMap != null && requestQueryMap.size() > 0) {
                BeanUtils.populate(requestObject, requestQueryMap);
            }
        } else if ("POST".equalsIgnoreCase(request.httpMethod())) { // RPC call
            requestObject = RequestUtil.getRequestObj(request, handler.getRequestType(), host);
        } else { // for Baiji RPC, only GET & POST are allowed
            ResponseUtil.writeHttpStatusResponse(response, HttpStatus.SC_METHOD_NOT_ALLOWED);
            return; // Nothing more to do
        }

        if (requestObject == null) { // defensive programming
            String errMsg = "Unable to bind request with request object of type " + handler.getRequestType();
            _logger.error(errMsg);
            SpecificRecord errorResponse = ResponseUtil.buildErrorResponse(handler.getResponseType(),
                    "NoRequestObject", errMsg, null, host);
            ResponseUtil.writeResponse(request, response, errorResponse, host);
            return; // Nothing more to do
        }

        request.setRequestObject(requestObject);

        if (applyRequestFilters(host, handler, request, response)) {
            return;
        }

        // Invocation
        OperationContext operationContext = new OperationContext(request, requestObject);

        SpecificRecord responseObject = handler.invoke(operationContext, host.getConfig().newServiceInstancePerRequest);

        if (responseObject == null) { // defensive programming
            String errMsg = "Fail to get response object when invoking the service";
            _logger.error(errMsg);
            SpecificRecord errorResponse = ResponseUtil.buildErrorResponse(handler.getResponseType(),
                    "NoResponseObject", errMsg, null, host);
            ResponseUtil.writeResponse(request, response, errorResponse, host);
            return; // Nothing more to do
        }

        if (applyResponseFilters(host, handler, request, response, responseObject)) {
            return;
        }

        ResponseUtil.writeResponse(request, response, responseObject, host);
    }

    /**
     * Apply all pre-request filters.
     *
     * @param host
     * @param request
     * @param response
     * @return A boolean value indicating whether the request has been handled by a filter.
     */
    private boolean applyPreRequestFilters(ServiceHost host, OperationHandler operationHandler,
                                           HttpRequestWrapper request, HttpResponseWrapper response) {
        for (PreRequestFilter filter : host.getConfig().preRequestFilters) {
            filter.apply(host, request, response);
            if (response.isResponseSent()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Apply all request filters.
     *
     * @param host
     * @param request
     * @param response
     * @return A boolean value indicating whether the request has been handled by a filter.
     */
    private boolean applyRequestFilters(ServiceHost host, OperationHandler operationHandler,
                                        HttpRequestWrapper request, HttpResponseWrapper response) {
        List<RequestFilter>[] filters = operationHandler.getRequestFilters();

        for (int i = 0; i < filters[0].size(); ++i) {
            RequestFilter filter = filters[0].get(i);
            filter.apply(host, request, response);
            if (response.isResponseSent()) {
                return true;
            }
        }

        for (RequestFilter filter : host.getConfig().requestFilters) {
            filter.apply(host, request, response);
            if (response.isResponseSent()) {
                return true;
            }
        }

        for (int i = 0; i < filters[1].size(); ++i) {
            RequestFilter filter = filters[1].get(i);
            filter.apply(host, request, response);
            if (response.isResponseSent()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Apply all response filters.
     *
     * @param host
     * @param request
     * @param response
     * @return A boolean value indicating whether the request has been handled by a filter.
     */
    private boolean applyResponseFilters(ServiceHost host, OperationHandler operationHandler,
                                         HttpRequestWrapper request, HttpResponseWrapper response,
                                         SpecificRecord responseObj) {
        List<ResponseFilter>[] filters = operationHandler.getResponseFilters();

        for (int i = 0; i < filters[0].size(); ++i) {
            ResponseFilter filter = filters[0].get(i);
            filter.apply(host, request, response, responseObj);
            if (response.isResponseSent()) {
                return true;
            }
        }

        for (ResponseFilter filter : host.getConfig().responseFilters) {
            filter.apply(host, request, response, responseObj);
            if (response.isResponseSent()) {
                return true;
            }
        }

        for (int i = 0; i < filters[1].size(); ++i) {
            ResponseFilter filter = filters[1].get(i);
            filter.apply(host, request, response, responseObj);
            if (response.isResponseSent()) {
                return true;
            }
        }

        return false;
    }
}
