package com.ctriposs.baiji.rpc.client;

public class ServiceLookupException extends Exception {

    private static final long serialVersionUID = -1L;

    public ServiceLookupException() {
    }

    public ServiceLookupException(String message) {
        super(message);
    }
}
