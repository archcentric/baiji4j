package com.ctriposs.baiji.rpc.server.util;

import javax.servlet.ServletConfig;

public final class ConfigUtil {

    private ConfigUtil() {
    }

    public static int getIntConfig(ServletConfig config, String key, int defaultValue) {
        String paramValue = config.getInitParameter(key);
        if (paramValue == null || paramValue.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(paramValue).intValue();
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}
