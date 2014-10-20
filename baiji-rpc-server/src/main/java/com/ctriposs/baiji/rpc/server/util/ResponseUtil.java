package com.ctriposs.baiji.rpc.server.util;

import com.ctriposs.baiji.rpc.common.HasResponseStatus;
import com.ctriposs.baiji.rpc.common.formatter.ContentFormatter;
import com.ctriposs.baiji.rpc.common.types.AckCodeType;
import com.ctriposs.baiji.rpc.common.types.ErrorDataType;
import com.ctriposs.baiji.rpc.common.types.ResponseStatusType;
import com.ctriposs.baiji.rpc.common.types.SeverityCodeType;
import com.ctriposs.baiji.rpc.server.ContentFormatConfig;
import com.ctriposs.baiji.rpc.server.HttpRequestWrapper;
import com.ctriposs.baiji.rpc.server.HttpResponseWrapper;
import com.ctriposs.baiji.rpc.server.ServiceHost;
import com.ctriposs.baiji.specific.SpecificRecord;
import com.google.common.io.CountingOutputStream;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;

import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;

/**
 * Created by yqdong on 2014/9/19.
 */
public final class ResponseUtil {

    private ResponseUtil() {
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
        CountingOutputStream countingOutputStream = new CountingOutputStream(outputStream);
        ContentFormatter formatter = getContentFormatter(request, host);
        formatter.serialize(countingOutputStream, responseObject);

        response.getExecutionResult().setResponseSize(countingOutputStream.getCount());

        String encoding = formatter.getEncoding();
        String contentType = formatter.getContentType() + ((encoding == null) ? "" : "; charset=" + encoding);
        response.setHeader(HttpHeaders.CONTENT_TYPE, contentType);

        // Write response
        response.sendResponse();
    }

    private static ContentFormatter getContentFormatter(HttpRequestWrapper request, ServiceHost host) {
        ContentFormatConfig config = host.getConfig().contentFormatConfig;

        ContentFormatter formatter;
        if (request.responseContentType() != null) {
            formatter = config.getFormatter(request.responseContentType());
        } else {
            formatter = getContentFormatterFromAccept(request, config);
        }
        return formatter != null ? formatter : config.getDefaultFormatter();
    }

    private static ContentFormatter getContentFormatterFromAccept(HttpRequestWrapper request, ContentFormatConfig config) {
        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        if (acceptHeader == null || acceptHeader.isEmpty()) {
            return null;
        }

        String[] accepts = acceptHeader.split(",");
        for (String accept : accepts) {
            if (accept == null || accept.isEmpty()) {
                continue;
            }
            ContentFormatter formatter = config.getFormatter(accept.trim());
            if (formatter != null) {
                return formatter;
            }
        }

        return null;
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
