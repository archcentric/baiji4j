package com.ctriposs.baiji.rpc.server;

public class RequestPath {

    private String _methodName;

    public RequestPath(String methodName) {
        _methodName = methodName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (_methodName == null ? 0 : _methodName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        RequestPath other = (RequestPath) obj;
        if (_methodName == null) {
            return other._methodName == null;
        } else {
            return _methodName.equals(other._methodName);
        }
    }
}
