package com.ctriposs.baiji.rpc.common.stats;

/**
 * Created by yqdong on 2014/10/8.
 */
public final class StatsUtils {

    private StatsUtils() {
    }

    public static String convertNamespaceToStatsValue(String namespace) {
        if (namespace == null || namespace.isEmpty()) {
            return namespace;
        }

        String prefix;
        int schemaIndex = namespace.indexOf("://");
        if (schemaIndex < 0) {
            prefix = namespace.substring(namespace.indexOf("/") + 1);
        } else {
            prefix = namespace.substring(namespace.indexOf("/", schemaIndex + "://".length()) + 1);
        }
        prefix = prefix.replace('/', '.');
        return prefix;
    }
}
