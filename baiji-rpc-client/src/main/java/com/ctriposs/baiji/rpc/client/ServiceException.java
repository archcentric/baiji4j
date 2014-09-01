package com.ctriposs.baiji.rpc.client;

import com.ctriposs.baiji.rpc.common.HasResponseStatus;
import com.ctriposs.baiji.rpc.common.types.ErrorDataType;
import com.ctriposs.baiji.rpc.common.types.ResponseStatusType;

import java.util.List;

public class ServiceException extends Exception implements HasExceptionType {

    private static final long serialVersionUID = -1L;

    private String _errorCode;
    private HasResponseStatus _responseObject;

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, HasResponseStatus responseObject) {
        super(message);
        _responseObject = responseObject;
    }

    public ServiceException(String message, HasResponseStatus responseObject, String errorCode) {
        super(message);
        _responseObject = responseObject;
        _errorCode = errorCode;
    }

    /**
     * Gets the error code of the first error
     *
     * @return
     */
    public String getErrorCode() {
        return _errorCode;
    }

    /**
     * Sets the error code of the first error
     *
     * @param errorCode
     */
    public void setErrorCode(String errorCode) {
        _errorCode = errorCode;
    }

    /**
     * Gets the deserialized response object containing error data
     *
     * @return
     */
    public HasResponseStatus getResponseObject() {
        return _responseObject;
    }

    /**
     * Sets the deserialized response object containing error data
     *
     * @param responseObject
     */
    public void setResponseObject(HasResponseStatus responseObject) {
        _responseObject = responseObject;
    }

    /**
     * Gets the list of error data
     *
     * @return
     */
    public List<ErrorDataType> getResponseErrors() {
        if (_responseObject == null || _responseObject.getResponseStatus() == null) {
            return null;
        }
        return _responseObject.getResponseStatus().getErrors();
    }

    @Override
    public String getExceptionTypeName() {
        ResponseStatusType responseStatus = _responseObject.getResponseStatus();
        if (responseStatus != null && responseStatus.getErrors() != null && !responseStatus.getErrors().isEmpty())
            return responseStatus.getErrors().get(0).getErrorClassification().toString().toLowerCase();
        return "other";
    }
}
