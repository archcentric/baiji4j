package com.ctriposs.baiji.rpc.server.handler;

import com.ctriposs.baiji.rpc.server.HttpRequestWrapper;
import com.ctriposs.baiji.rpc.server.HttpResponseWrapper;
import com.ctriposs.baiji.rpc.server.OperationHandler;
import com.ctriposs.baiji.rpc.server.ServiceHost;
import com.ctriposs.baiji.rpc.server.util.ErrorUtil;
import com.ctriposs.baiji.rpc.server.util.ResponseUtil;
import com.ctriposs.baiji.specific.SpecificRecord;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            Throwable actualEx;
            if (ex instanceof InvocationTargetException) {
                actualEx = ((InvocationTargetException)ex).getTargetException();
            } else {
                actualEx = ex;
            }
            String errMsg = actualEx.getClass().getName() + " - " + actualEx.getMessage();
            _logger.error(errMsg, actualEx);
            try {
                SpecificRecord errorResponse = ErrorUtil.buildFrameworkErrorResponse(
                        operationHandler.getResponseType(), "RequestException", errMsg, actualEx, host);
                ResponseUtil.writeResponse(request, response, errorResponse, host);
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
