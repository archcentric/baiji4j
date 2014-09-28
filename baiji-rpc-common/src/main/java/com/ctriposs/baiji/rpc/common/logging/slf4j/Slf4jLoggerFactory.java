package com.ctriposs.baiji.rpc.common.logging.slf4j;

import com.ctriposs.baiji.rpc.common.logging.ILoggerFactory;
import com.ctriposs.baiji.rpc.common.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yqdong on 2014/9/26.
 */
public class Slf4jLoggerFactory implements ILoggerFactory {

    @Override
    public String name() {
        return "slf4j";
    }

    @Override
    public Logger getLogger(Class clazz) {
        return new Slf4jLogger(LoggerFactory.getLogger(clazz));
    }

    @Override
    public Logger getLogger(String name) {
        return new Slf4jLogger(LoggerFactory.getLogger(name));
    }
}
