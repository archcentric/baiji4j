package com.ctriposs.baiji.rpc.server;

import com.ctriposs.baiji.exception.BaijiRuntimeException;
import com.ctriposs.baiji.rpc.common.BaijiContract;
import com.ctriposs.baiji.rpc.common.HasResponseStatus;
import com.ctriposs.baiji.rpc.common.formatter.ContentFormatter;
import com.ctriposs.baiji.rpc.common.types.*;
import com.ctriposs.baiji.specific.SpecificRecord;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.*;

public class BaijiHttpRequestRouter {

    private static final Logger _logger = LoggerFactory.getLogger(OperationHandler.class);

    private final Map<RequestPath, OperationHandler> _handlers = new HashMap<RequestPath, OperationHandler>();
    private final ServiceConfig _config;
    private final ServiceMetadata _serviceMetadata;

    public BaijiHttpRequestRouter(ServiceConfig config, Class<?> serviceType) {
        this._config = config;
        if (this._config.getDefaultFormatter() == null) { // default formatter must be provided
            String errMsg = "Missing mandatory default content formatter in service config";
            _logger.error(errMsg);
            throw new BaijiRuntimeException(errMsg);
        }
        Class<?> contract = this.findContract(serviceType);
        if (contract == null) {
            String errMsg = "Can't find BaijiContract on service type " + serviceType;
            throw new BaijiRuntimeException(errMsg);
        }

        _serviceMetadata = this.getServiceMetaData(contract);

        // Cache all operation methods
        for (Method method : contract.getMethods()) {
            // Create handler
            OperationHandler handler = new OperationHandler(config, serviceType, method);
            RequestPath path = new RequestPath(method.getName().toLowerCase());
            // Fail due to duplicate entries
            if (_handlers.containsKey(path)) {
                String errMsg = String.format("duplicate method %s on service type %s is not allowed", method.getName(), serviceType);
                _logger.error(errMsg);
                throw new BaijiRuntimeException(errMsg);
            }
            _handlers.put(path, handler);
        }
    }

    private static ServiceMetadata getServiceMetaData(Class<?> contract) {
        BaijiContract annotation = contract.getAnnotation(BaijiContract.class);
        ServiceMetadata metaData = new ServiceMetadata();
        metaData.setServiceName(annotation.serviceName());
        metaData.setServiceNamespace(annotation.serviceNamespace());
        metaData.setCodeGeneratorVersion(annotation.codeGeneratorVersion());
        return metaData;
    }

    private static Class<?> findContract(Class<?> serviceType) {
        Class<?>[] interfaces = serviceType.getInterfaces();
        if (interfaces == null || interfaces.length == 0) return null;
        for (Class<?> intf : interfaces) {
            if (intf.getAnnotation(BaijiContract.class) != null) {
                return intf;
            }
        }
        return null;
    }

    private OperationHandler selectHandler(RequestContext environment) {
        // Extract path
        String path = environment.RequestPath;
        if (path == null || path.isEmpty()) {
            return null;
        }
        while (path.startsWith("/")) {
            path = path.substring(1); // Remove the beginning "/"
        }
        while (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        String[] keyBase = path.split("/");
        if (keyBase.length != 1) {
            return null;
        }

        // Extract "extension" for media type
        int extStart = keyBase[0].lastIndexOf(".");
        if (extStart != -1) {
            String ext = keyBase[0].substring(extStart + 1);
            environment.RequestExtention = ext;
            keyBase[0] = keyBase[0].substring(0, keyBase[0].length() - ext.length() - 1);
        }

        RequestPath requestPath = new RequestPath(keyBase[0].toLowerCase());

        return _handlers.get(requestPath);
    }

    private ContentFormatter negotiateFormat(RequestContext environment) {
        ContentFormatter formatter = null;
        if (environment.RequestExtention != null && !environment.RequestExtention.isEmpty()) {
            // Try specified
            Map<String, ContentFormatter> specifiedFormatters = _config.getSpecifiedFormatters();
            if (specifiedFormatters != null) {
                formatter = specifiedFormatters.get(environment.RequestExtention);
            }
        }

        // Use default when no suitable specified formatter is found.
        if (formatter == null) {
            formatter = _config.getDefaultFormatter();
        }

        return formatter;
    }

    private static Map<String, String> splitQuery(String queryString) throws UnsupportedEncodingException {
        if (queryString == null || queryString.isEmpty()) {
            return null;
        }
        Map<String, String> queryMap = new LinkedHashMap<String, String>();
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            queryMap.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return queryMap;
    }

    // Routing -> ParameterBinding/Deserialization -> Invocation -> Serialization -> Write response
    public void process(RequestContext request, HttpResponseWrapper responseWriter) {

        ContentFormatter formatter = null;
        OperationHandler handler = null;
        try {
            // Routing
            handler = this.selectHandler(request);
            if (handler == null) {
                _logger.error("No handler found.");
                this.writeHttpResponse(responseWriter, HttpStatus.SC_NOT_FOUND);
                return; // Nothing more to do
            }

            SpecificRecord requestObject = null;
            formatter = this.negotiateFormat(request);

            // ParameterBinding or Deserialization
            if ("GET".equalsIgnoreCase(request.RequestMethod)) { // REST call, for testing only
                // binding parameters
                requestObject = handler.getEmptyRequestInstance();
                // request parameters binding
                Map<String, String> requestQueryMap = splitQuery(request.RequestQueryString);
                if (requestQueryMap != null && requestQueryMap.size() > 0) {
                    BeanUtils.populate(requestObject, requestQueryMap);
                }
            } else if ("POST".equalsIgnoreCase(request.RequestMethod)) { // RPC call
                requestObject = formatter.deserialize(handler.getRequestType(), request.RequestBody);
            } else { // for Baiji RPC, only GET & POST are allowed
                this.writeHttpResponse(responseWriter, HttpStatus.SC_METHOD_NOT_ALLOWED);
                return; // Nothing more to do
            }

            if (requestObject == null) { // defensive programming
                String errMsg = "Unable to bind request with request object of type " + handler.getRequestType();
                _logger.error(errMsg);
                SpecificRecord errorResponse = (SpecificRecord) this.buildErrorResponse(
                        handler, "NoRequestObject", errMsg, ErrorClassificationCodeType.FRAMEWORK_ERROR, null);
                this.writeBaijiResponse(responseWriter, errorResponse, request, formatter);
                return; // Nothing more to do
            }

            // Invocation
            OperationContext operationContext = new OperationContext(request, handler.getMethodName(), requestObject);

            SpecificRecord responseObject = null;
            try {
                responseObject = handler.invoke(operationContext);
            } catch (Exception ex) {
                String errMsg = "Fail to invoke target service method " + handler.getMethodName();
                _logger.error(errMsg, ex);
                SpecificRecord errorResponse = (SpecificRecord) this.buildErrorResponse(
                        handler, "ServiceInvocationError", errMsg, ErrorClassificationCodeType.SERVICE_ERROR, ex);
                this.writeBaijiResponse(responseWriter, errorResponse, request, formatter);
                return; // Nothing more to do
            }

            if (responseObject == null) { // defensive programming
                String errMsg = "Fail to get response object when invoking the service";
                _logger.error(errMsg);
                SpecificRecord errorResponse = (SpecificRecord) this.buildErrorResponse(
                        handler, "NoResponseObject", errMsg, ErrorClassificationCodeType.FRAMEWORK_ERROR, null);
                this.writeBaijiResponse(responseWriter, errorResponse, request, formatter);
                return; // Nothing more to do
            }

            this.writeBaijiResponse(responseWriter, responseObject, request, formatter);

        } catch (Exception ex) {
            if (request != null && formatter != null && handler != null) {
                String errMsg = ex.getMessage();
                _logger.error(errMsg, ex);
                try {
                    SpecificRecord errorResponse = (SpecificRecord) this.buildErrorResponse(
                            handler, "RequestException", errMsg, ErrorClassificationCodeType.FRAMEWORK_ERROR, ex);
                    this.writeBaijiResponse(responseWriter, errorResponse, request, formatter);
                } catch (Exception e) {
                    _logger.error("Internal server error", e);
                    this.writeHttpResponse(responseWriter, HttpStatus.SC_INTERNAL_SERVER_ERROR);
                }
                return; // Nothing more to do
            } else {
                _logger.error("Internal server error", ex);
                this.writeHttpResponse(responseWriter, HttpStatus.SC_INTERNAL_SERVER_ERROR);
                return; // Nothing more to do
            }
        } finally {
            if (request != null) {
                if (request.RequestBody != null) {
                    try {
                        request.RequestBody.close();
                    } catch (IOException e) {
                        _logger.error("Fail to close request input stream", e);
                    }
                }
                if (request.RequestBody != null) {
                    try {
                        request.RequestBody.close();
                    } catch (IOException e) {
                        _logger.error("Fail to close response output stream", e);
                    }
                }
            }
        }
    }

    private HasResponseStatus buildErrorResponse(OperationHandler handler, String errorCode, String errorMessage,
                                                 ErrorClassificationCodeType errorClassificationCode, Throwable t) throws Exception {
        HasResponseStatus responseObj = (HasResponseStatus) handler.getEmptyResponseInstance();

        ResponseStatusType responseStatus = new ResponseStatusType();
        responseStatus.ack = AckCodeType.FAILURE;
        responseStatus.timestamp = this.getCurrentTimestamp();
        ErrorDataType errorData = new ErrorDataType();
        errorData.errorCode = errorCode;
        errorData.errorClassification = errorClassificationCode;
        errorData.message = errorMessage;
        errorData.severityCode = SeverityCodeType.ERROR;
        if (t != null && this._config.isOutputExceptionStackTrace()) {
            String stackTrace = ExceptionUtils.getStackTrace(t);
            errorData.stackTrace = stackTrace;
        }
        responseStatus.errors = new ArrayList<ErrorDataType>();
        responseStatus.errors.add(errorData);

        responseObj.setResponseStatus(responseStatus);
        return responseObj;
    }

    private String getCurrentTimestamp() {
        Date date = new Date();
        return new Timestamp(date.getTime()).toString();
    }

    private void writeBaijiResponse(HttpResponseWrapper responseWriter, SpecificRecord responseObject,
                                    RequestContext environment, ContentFormatter formatter) throws Exception {
        responseWriter.setStatus(HttpStatus.SC_OK);

        OutputStream outputStream = responseWriter.getResponseStream();
        environment.ResponseBody = outputStream;

        // Populate response status
        ResponseStatusType responseStatus = ((HasResponseStatus) responseObject).getResponseStatus();
        if (responseStatus == null) {
            responseStatus = new ResponseStatusType();
            ((HasResponseStatus) responseObject).setResponseStatus(responseStatus);
        }

        if (responseStatus.ack == null) { // populate mandatory ack
            if (this.containSevereError(responseStatus)) {
                responseStatus.ack = AckCodeType.FAILURE;
            } else {
                responseStatus.ack = AckCodeType.SUCCESS;
            }
        }

        if (responseStatus.timestamp == null) {
            responseStatus.timestamp = this.getCurrentTimestamp();
        }

        // Serialization
        formatter.serialize(outputStream, responseObject);

        String encoding = formatter.getEncoding();
        String contentType = formatter.getMediaType() + ((encoding == null) ? "" : "; charset=" + encoding);
        responseWriter.setHeader(HttpHeaders.CONTENT_TYPE, contentType);

        // Write response
        responseWriter.sendResponse();
    }

    private boolean containSevereError(ResponseStatusType responseStatus) {
        List<ErrorDataType> errors = responseStatus.getErrors();
        if (errors != null && errors.size() > 0) {
            for (ErrorDataType errorData : errors) {
                if (errorData.severityCode != null && errorData.severityCode == SeverityCodeType.ERROR) {
                    return true;
                }
            }
        }
        return false;
    }

    private void writeHttpResponse(HttpResponseWrapper responseWrapper, int responseStatus) {
        responseWrapper.setStatus(responseStatus);
        responseWrapper.sendResponse();
    }
}
