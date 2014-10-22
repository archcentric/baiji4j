package com.ctriposs.baiji.rpc.server.handler;

import com.ctriposs.baiji.rpc.common.logging.Logger;
import com.ctriposs.baiji.rpc.common.logging.LoggerFactory;
import com.ctriposs.baiji.rpc.server.HttpRequestWrapper;
import com.ctriposs.baiji.rpc.server.HttpResponseWrapper;
import com.ctriposs.baiji.rpc.server.OperationHandler;
import com.ctriposs.baiji.rpc.server.ServiceHost;
import com.ctriposs.baiji.rpc.server.stats.OperationStats;
import com.ctriposs.baiji.rpc.server.util.ErrorUtil;
import com.ctriposs.baiji.rpc.server.util.ResponseUtil;
import com.ctriposs.baiji.specific.SpecificRecord;
import org.apache.http.HttpStatus;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by yqdong on 2014/9/19.
 */
public class DefaultExceptionHandler implements ExceptionHandler {

    private static final Logger _logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @Override
    public void handle(ServiceHost host, HttpRequestWrapper request, HttpResponseWrapper response, Exception ex) {
        OperationHandler operationHandler = request.operationHandler();
        if (operationHandler != null) {
            try {
                OperationStats stats = host.getServiceStats().getOperationStats(request.operationName());

                Throwable actualEx;
                if (ex instanceof InvocationTargetException) {
                    actualEx = ((InvocationTargetException) ex).getTargetException();
                } else {
                    actualEx = ex;
                }

                String errorCode = actualEx.getClass().getSimpleName();
                String errMsg = actualEx.getMessage() != null ? actualEx.getMessage() : actualEx.getClass().getName();
                _logger.error(errMsg, actualEx);

                SpecificRecord errorResponse;
                if (response.getExecutionResult().serviceExceptionThrown()) {
                    errorResponse = ErrorUtil.buildServiceErrorResponse(
                            operationHandler.getResponseType(), errorCode, errMsg, actualEx, host);
                    stats.markServiceException();
                } else {
                    errorResponse = ErrorUtil.buildFrameworkErrorResponse(
                            operationHandler.getResponseType(), errorCode, errMsg, actualEx, host);
                    stats.markFrameworkException();
                }
                ResponseUtil.writeResponse(request, response, errorResponse, host);
                stats.addResponseSize(response.getExecutionResult().responseSize());
            } catch (Exception e) {
                _logger.error("Internal server error", e);
                ResponseUtil.writeHttpStatusResponse(response, HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            _logger.error("Internal server error", ex);
            ResponseUtil.writeHttpStatusResponse(response, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
