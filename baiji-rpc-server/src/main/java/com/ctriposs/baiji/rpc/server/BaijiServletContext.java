package com.ctriposs.baiji.rpc.server;

import com.ctriposs.baiji.rpc.server.registry.ServiceRegistry;

/**
 * Created by yqdong on 2014/9/28.
 */
public class BaijiServletContext {

    public static final BaijiServletContext INSTANCE = new BaijiServletContext();

    private int _port;
    private String _subEnv;
    private ServiceRegistry _serviceRegistry;
    private HttpRequestRouter _requestRouter;

    private BaijiServletContext() {
    }

    public int getPort() {
        return _port;
    }

    public void setPort(int port) {
        _port = port;
    }

    public String getSubEnv() {
        return _subEnv;
    }

    public void setSubEnv(String subEnv) {
        _subEnv = subEnv;
    }

    public ServiceRegistry getServiceRegistry() {
        return _serviceRegistry;
    }

    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        _serviceRegistry = serviceRegistry;
    }

    public HttpRequestRouter getRequestRouter() {
        return _requestRouter;
    }

    public void setRequestRouter(HttpRequestRouter requestRouter) {
        _requestRouter = requestRouter;
    }
}
