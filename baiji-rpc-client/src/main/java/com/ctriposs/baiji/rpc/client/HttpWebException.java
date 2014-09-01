package com.ctriposs.baiji.rpc.client;

public class HttpWebException extends Exception implements HasExceptionType {
    private static final long serialVersionUID = 1L;
    private final int _statusCode;
    private final String _reasonPhrase;
    private final String _responseContent;

    public HttpWebException(int statusCode, String reasonPhrase, String responseContent) {
        super(statusCode + " " + reasonPhrase);
        this._statusCode = statusCode;
        this._reasonPhrase = reasonPhrase;
        this._responseContent = responseContent;
    }

    public int getStatusCode() {
        return _statusCode;
    }

    public String getReasonPhrase() {
        return _reasonPhrase;
    }

    public String getResponseContent() {
        return _responseContent;
    }

    @Override
    public String getExceptionTypeName() {
        return _statusCode == 408 ? "timeout" : Integer.toString(_statusCode);
    }
}
