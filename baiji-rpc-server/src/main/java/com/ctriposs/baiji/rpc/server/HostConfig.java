package com.ctriposs.baiji.rpc.server;

import com.ctriposs.baiji.rpc.server.filter.PreRequestFilter;
import com.ctriposs.baiji.rpc.server.filter.RequestFilter;
import com.ctriposs.baiji.rpc.server.filter.ResponseFilter;
import com.ctriposs.baiji.rpc.server.handler.DefaultExceptionHandler;
import com.ctriposs.baiji.rpc.server.handler.ExceptionHandler;
import com.ctriposs.baiji.rpc.server.handler.RequestHandler;
import com.ctriposs.baiji.rpc.server.handler.RestRequestHandler;
import com.ctriposs.baiji.rpc.server.plugin.Plugin;
import com.ctriposs.baiji.rpc.server.plugin.metadata.MetadataPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yqdong on 2014/9/18.
 */
public class HostConfig {

    public final ContentFormatConfig contentFormatConfig = new ContentFormatConfig();

    public final List<PreRequestFilter> preRequestFilters = new ArrayList<PreRequestFilter>();

    public final List<RequestFilter> requestFilters = new ArrayList<RequestFilter>();

    public final List<RequestHandler> requestHandlers = new ArrayList<RequestHandler>();

    public ExceptionHandler exceptionHandler;

    public final List<ResponseFilter> responseFilters = new ArrayList<ResponseFilter>();

    public final List<Plugin> plugins = new ArrayList<Plugin>();

    public boolean debugMode;

    public boolean newServiceInstancePerRequest;

    @Deprecated
    public boolean outputExceptionStackTrace;

    public HostConfig() {
        reset();
    }

    private void reset() {
        contentFormatConfig.reset();
        resetFilters();
        resetHandlers();
        resetPlugins();
    }

    private void resetFilters() {
        preRequestFilters.clear();
        requestFilters.clear();
        responseFilters.clear();
    }

    private void resetHandlers() {
        requestHandlers.clear();
        requestHandlers.add(new RestRequestHandler());
        exceptionHandler = new DefaultExceptionHandler();
    }

    private void resetPlugins() {
        plugins.clear();
        plugins.add(new MetadataPlugin());
    }
}
