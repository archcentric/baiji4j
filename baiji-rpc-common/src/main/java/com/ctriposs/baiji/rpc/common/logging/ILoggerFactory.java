package com.ctriposs.baiji.rpc.common.logging;

/**
 * Created by yqdong on 2014/9/26.
 */
public interface ILoggerFactory {

    /**
     * Return the name of the logger factory specifying the type of logger it manages.
     *
     * @return
     */
    String name();

    /**
     * Return a logger named corresponding to the class passed as parameter, using
     * the statically bound {@link ILoggerFactory} instance.
     *
     * @param clazz the returned logger will be named after clazz
     * @return logger
     */
    Logger getLogger(Class clazz);

    /**
     * Return an appropriate {@link Logger} instance as specified by the
     * <code>name</code> parameter.
     * <p/>
     * <p>Null-valued name arguments are considered invalid.
     *
     * @param name the name of the Logger to return
     * @return a Logger instance
     */
    Logger getLogger(String name);
}
