package com.ctriposs.baiji.rpc.client;

public enum ConnectionMode {
    DIRECT("Direct"),
    INDIRECT("Indirect");

    private final String _text;

    ConnectionMode(String text) {
        _text = text;
    }

    @Override
    public String toString() {
        return _text;
    }
}
