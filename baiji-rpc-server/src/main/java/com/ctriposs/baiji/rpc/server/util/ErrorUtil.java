package com.ctriposs.baiji.rpc.server.util;

import com.ctriposs.baiji.rpc.common.HasResponseStatus;
import com.ctriposs.baiji.rpc.common.types.*;
import com.ctriposs.baiji.rpc.server.ServiceHost;
import com.ctriposs.baiji.specific.SpecificRecord;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by yqdong on 2014/9/25.
 */
public final class ErrorUtil {

    private ErrorUtil() {
    }

    public static SpecificRecord buildFrameworkErrorResponse(Class<?> responseClass, String errorCode, String message,
                                                    ServiceHost host) throws Exception {
        return buildFrameworkErrorResponse(responseClass, errorCode, message, null, host);
    }

    public static SpecificRecord buildFrameworkErrorResponse(Class<?> responseClass, String errorCode, String message,
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
}
