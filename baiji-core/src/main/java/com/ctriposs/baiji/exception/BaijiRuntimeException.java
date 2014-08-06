package com.ctriposs.baiji.exception;

/**
 * Base Baiji exception.
 */
public class BaijiRuntimeException extends RuntimeException {
    public BaijiRuntimeException(Throwable cause) {
        super(cause);
    }

    public BaijiRuntimeException(String message) {
        super(message);
    }

    public BaijiRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}

