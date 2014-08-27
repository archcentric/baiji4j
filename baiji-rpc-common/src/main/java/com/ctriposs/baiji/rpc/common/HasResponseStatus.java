package com.ctriposs.baiji.rpc.common;

import com.ctriposs.baiji.rpc.common.types.ResponseStatusType;

public interface HasResponseStatus {
    
    ResponseStatusType getResponseStatus();

    void setResponseStatus(ResponseStatusType responseStatus);
}
