package com.ctriposs.baiji.rpc.common;

import com.ctriposs.baiji.rpc.mobile.common.types.MobileRequestHead;

/**
 * Created by yqdong on 2014/9/22.
 */
public interface HasMobileRequestHead {

    MobileRequestHead getHead();

    void setHead(MobileRequestHead head);
}
