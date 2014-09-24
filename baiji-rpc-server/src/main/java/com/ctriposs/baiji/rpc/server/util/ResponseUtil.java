package com.ctriposs.baiji.rpc.server.util;

import com.ctriposs.baiji.rpc.common.HasResponseStatus;
import com.ctriposs.baiji.rpc.common.formatter.ContentFormatter;
import com.ctriposs.baiji.rpc.common.types.*;
import com.ctriposs.baiji.rpc.server.HttpRequestWrapper;
import com.ctriposs.baiji.rpc.server.HttpResponseWrapper;
import com.ctriposs.baiji.rpc.server.ServiceHost;
import com.ctriposs.baiji.specific.SpecificRecord;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by yqdong on 2014/9/19.
 */
public final class ResponseUtil {

    private ResponseUtil() {
    }

    public static SpecificRecord buildErrorResponse(Class<?> responseClass, String errorCode, String message,
                                                    ServiceHost host) throws Exception {
        return buildErrorResponse(responseClass, errorCode, message, null, host);
    }

    public static SpecificRecord buildErrorResponse(Class<?> responseClass, String errorCode, String message,
                                                    Throwable t, ServiceHost host) throws Exception {
        HasResponseStatus responseObj = (HasResponseStatus) responseClass.newInstance();

        ResponseStatusType responseStatus = new ResponseStatusType();
        responseStatus.ack = AckCodeType.FAILURE;
        responseStatus.timestamp = Calendar.getInstance();

        ErrorDataType errorData = new ErrorDataType();
        errorData.errorCode = errorCode;
        errorData.errorClassification = ErrorClassificationCodeType.FRAMEWORK_ERROR;
        errorData.message = message;
        errorData.severityCode = SeverityCodeType.ERROR;
        if (t != null && (host.getConfig().debugMode || host.getConfig().outputExceptionStackTrace)) {
            String stackTrace = ExceptionUtils.getStackTrace(t);
            errorData.stackTrace = stackTrace;
        }
        responseStatus.errors = new ArrayList<ErrorDataType>();
        responseStatus.errors.add(errorData);

        responseObj.setResponseStatus(responseStatus);

        return (SpecificRecord) responseObj;
    }

    public static void writeResponse(HttpRequestWrapper request, HttpResponseWrapper response, SpecificRecord responseObject,
                                     ServiceHost host) throws Exception {
        response.setStatus(HttpStatus.SC_OK);

        OutputStream outputStream = response.getResponseStream();

        // Populate response status
        ResponseStatusType responseStatus = ((HasResponseStatus) responseObject).getResponseStatus();
        if (responseStatus == null) {
            responseStatus = new ResponseStatusType();
            ((HasResponseStatus) responseObject).setResponseStatus(responseStatus);
        }

        if (responseStatus.ack == null) { // populate mandatory ack
            if (containSevereError(responseStatus)) {
                responseStatus.ack = AckCodeType.FAILURE;
            } else {
                responseStatus.ack = AckCodeType.SUCCESS;
            }
        }

        if (responseStatus.timestamp == null) {
            responseStatus.timestamp = Calendar.getInstance();
        }

        // Serialization
        ContentFormatter formatter = getContentFormatter(request, host);
        formatter.serialize(outputStream, responseObject);

        String encoding = formatter.getEncoding();
        String contentType = formatter.getContentType() + ((encoding == null) ? "" : "; charset=" + encoding);
        response.setHeader(HttpHeaders.CONTENT_TYPE, contentType);

        // Write response
        response.sendResponse();
    }

    private static ContentFormatter getContentFormatter(HttpRequestWrapper request, ServiceHost host) {
        ContentFormatter formatter = host.getConfig().contentFormatConfig.getFormatter(request.responseContentType());
        return formatter != null ? formatter : host.getConfig().contentFormatConfig.getDefaultFormatter();
    }

    private static boolean containSevereError(ResponseStatusType responseStatus) {
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

    public static void writeHttpStatusResponse(HttpResponseWrapper response, int status) {
        response.setStatus(status);
        response.sendResponse();
    }
}
