package com.ctriposs.baiji.rpc.common.logging.slf4j;

import com.ctriposs.baiji.rpc.common.logging.Logger;

import java.util.Map;

/**
 * Created by yqdong on 2014/9/26.
 */
public class Slf4jLogger implements Logger {

    private static final String MSG_WITH_TITLE = "{}: {}";
    private static final String MSG_WITH_TITLE_ATTRS = "{}: {}\n{}";
    private static final String MSG_WITH_ATTRS = "{}\n{}";

    private final org.slf4j.Logger _logger;

    public Slf4jLogger(org.slf4j.Logger logger) {
        _logger = logger;
    }

    @Override
    public void debug(String title, String message) {
        _logger.debug(MSG_WITH_TITLE, title, message);
    }

    @Override
    public void debug(String title, Throwable throwable) {
        _logger.debug(title, throwable);
    }

    @Override
    public void debug(String title, String message, Map<String, String> attrs) {
        _logger.debug(MSG_WITH_TITLE_ATTRS, title, message, getAttributeText(attrs));
    }

    @Override
    public void debug(String title, Throwable throwable, Map<String, String> attrs) {
        _logger.debug(title + "\n" + getAttributeText(attrs), throwable);
    }

    @Override
    public void debug(String message) {
        _logger.debug(message);
    }

    @Override
    public void debug(Throwable throwable) {
        _logger.debug(throwable.getMessage(), throwable);
    }

    @Override
    public void debug(String message, Map<String, String> attrs) {
        _logger.debug(MSG_WITH_ATTRS, message, getAttributeText(attrs));
    }

    @Override
    public void debug(Throwable throwable, Map<String, String> attrs) {
        _logger.debug(getAttributeText(attrs), throwable);
    }

    @Override
    public void info(String title, String message) {
        _logger.info(MSG_WITH_TITLE, title, message);
    }

    @Override
    public void info(String title, Throwable throwable) {
        _logger.info(title, throwable);
    }

    @Override
    public void info(String title, String message, Map<String, String> attrs) {
        _logger.info(MSG_WITH_TITLE_ATTRS, title, message, getAttributeText(attrs));
    }

    @Override
    public void info(String title, Throwable throwable, Map<String, String> attrs) {
        _logger.info(title + "\n" + getAttributeText(attrs), throwable);
    }

    @Override
    public void info(String message) {
        _logger.info(message);
    }

    @Override
    public void info(Throwable throwable) {
        _logger.info(throwable.getMessage(), throwable);
    }

    @Override
    public void info(String message, Map<String, String> attrs) {
        _logger.info(MSG_WITH_ATTRS, message, getAttributeText(attrs));
    }

    @Override
    public void info(Throwable throwable, Map<String, String> attrs) {
        _logger.info(getAttributeText(attrs), throwable);
    }

    @Override
    public void warn(String title, String message) {
        _logger.warn(MSG_WITH_TITLE, title, message);
    }

    @Override
    public void warn(String title, Throwable throwable) {
        _logger.warn(title, throwable);
    }

    @Override
    public void warn(String title, String message, Map<String, String> attrs) {
        _logger.warn(MSG_WITH_TITLE_ATTRS, title, message, getAttributeText(attrs));
    }

    @Override
    public void warn(String title, Throwable throwable, Map<String, String> attrs) {
        _logger.warn(title + "\n" + getAttributeText(attrs), throwable);
    }

    @Override
    public void warn(String message) {
        _logger.warn(message);
    }

    @Override
    public void warn(Throwable throwable) {
        _logger.warn(throwable.getMessage(), throwable);
    }

    @Override
    public void warn(String message, Map<String, String> attrs) {
        _logger.warn(MSG_WITH_ATTRS, message, getAttributeText(attrs));
    }

    @Override
    public void warn(Throwable throwable, Map<String, String> attrs) {
        _logger.warn(getAttributeText(attrs), throwable);
    }

    @Override
    public void error(String title, String message) {
        _logger.error(MSG_WITH_TITLE, title, message);
    }

    @Override
    public void error(String title, Throwable throwable) {
        _logger.error(title, throwable);
    }

    @Override
    public void error(String title, String message, Map<String, String> attrs) {
        _logger.error(MSG_WITH_TITLE_ATTRS, title, message, getAttributeText(attrs));
    }

    @Override
    public void error(String title, Throwable throwable, Map<String, String> attrs) {
        _logger.error(title + "\n" + getAttributeText(attrs), throwable);
    }

    @Override
    public void error(String message) {
        _logger.error(message);
    }

    @Override
    public void error(Throwable throwable) {
        _logger.error(throwable.getMessage(), throwable);
    }

    @Override
    public void error(String message, Map<String, String> attrs) {
        _logger.error(MSG_WITH_ATTRS, message, getAttributeText(attrs));
    }

    @Override
    public void error(Throwable throwable, Map<String, String> attrs) {
        _logger.error(getAttributeText(attrs), throwable);
    }

    private String getAttributeText(Map<String, String> attrs) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : attrs.entrySet()) {
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(entry.getValue());
        }
        return builder.toString();
    }
}
