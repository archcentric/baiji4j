package com.ctriposs.baiji.rpc.server;

import com.ctriposs.baiji.rpc.common.formatter.BinaryContentFormatter;
import com.ctriposs.baiji.rpc.common.formatter.ContentFormatter;
import com.ctriposs.baiji.rpc.common.formatter.JsonContentFormatter;

import java.util.HashMap;
import java.util.Map;

/**
 * Baiji Rpc Service Configuration
 */
public class ServiceConfig {

    private boolean _newServiceInstancePerRequest;

    private boolean _outputExceptionStackTrace;

    private ContentFormatter _defaultFormatter;

    private Map<String, ContentFormatter> _specifiedFormatters;

    public ServiceConfig() {
        this._defaultFormatter = new JsonContentFormatter();
        _specifiedFormatters = new HashMap<String, ContentFormatter>();
        registerSpecifiedFormatters(new JsonContentFormatter());
    }

    public boolean isNewServiceInstancePerRequest() {
        return _newServiceInstancePerRequest;
    }

    public void setNewServiceInstancePerRequest(boolean newServiceInstancePerRequest) {
        this._newServiceInstancePerRequest = newServiceInstancePerRequest;
    }

    public ContentFormatter getDefaultFormatter() {
        return _defaultFormatter;
    }

    public void setDefaultFormatter(ContentFormatter _defaultFormatter) {
        this._defaultFormatter = _defaultFormatter;
    }

    public Map<String, ContentFormatter> getSpecifiedFormatters() {
        return _specifiedFormatters;
    }

    public void setSpecifiedFormatters(Map<String, ContentFormatter> specifiedFormatters) {
        this._specifiedFormatters = new HashMap<String, ContentFormatter>(specifiedFormatters);
    }

    public void registerSpecifiedFormatters(ContentFormatter formatter) {
        _specifiedFormatters.put(formatter.getExtension(), formatter);
    }

    public boolean isOutputExceptionStackTrace() {
        return _outputExceptionStackTrace;
    }

    public void setOutputExceptionStackTrace(boolean outputExceptionStackTrace) {
        this._outputExceptionStackTrace = outputExceptionStackTrace;
    }
}
