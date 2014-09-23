package com.ctriposs.baiji.rpc.server;

import com.ctriposs.baiji.rpc.common.formatter.BinaryContentFormatter;
import com.ctriposs.baiji.rpc.common.formatter.ContentFormatter;
import com.ctriposs.baiji.rpc.common.formatter.JsonContentFormatter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yqdong on 2014/9/18.
 */
public class ContentFormatConfig {

    private String _defaultContentType;

    /**
     * All registered content formatters.
     * Key is the content type of the associated content formatter.
     */
    private final Map<String, ContentFormatter> _registeredFormatters
            = new HashMap<String, ContentFormatter>();
    /**
     * Map from registered extension to the corresponding content type.
     */
    private final Map<String, String> _extContentTypes= new HashMap<String, String>();

    public ContentFormatConfig() {
        reset();
    }

    public Map<String, ContentFormatter> getRegisteredFormatters() {
        return _registeredFormatters;
    }

    public void registerFormatter(ContentFormatter formatter) {
        if (_registeredFormatters.isEmpty()) {
            _defaultContentType = formatter.getContentType();
        }
        _registeredFormatters.put(formatter.getContentType(), formatter);
        _extContentTypes.put(formatter.getExtension(), formatter.getContentType());
    }

    public ContentFormatter getDefaultFormatter() {
        return _defaultContentType != null ? _registeredFormatters.get(_defaultContentType) : null;
    }

    public ContentFormatter getFormatter(String contentType) {
        return _registeredFormatters.get(contentType);
    }

    public String getContentTypeFromExt(String extension) {
        return _extContentTypes.get(extension);
    }

    public String getDefaultContentType() {
        return _defaultContentType;
    }

    public void setDefaultContentType(String contentType) {
        if (!_registeredFormatters.containsKey(contentType)) {
            throw new IllegalArgumentException("Unknown content type: " + contentType);
        }
        _defaultContentType = contentType;
    }

    public void reset() {
        _registeredFormatters.clear();
        _extContentTypes.clear();
        _defaultContentType = null;
        registerFormatter(new JsonContentFormatter());
        registerFormatter(new BinaryContentFormatter());
    }
}
