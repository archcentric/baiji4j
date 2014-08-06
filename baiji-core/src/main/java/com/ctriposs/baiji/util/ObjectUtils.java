package com.ctriposs.baiji.util;

public final class ObjectUtils {
    public static int hashCode(Object obj) {
        return obj != null ? obj.hashCode() : 0;
    }

    public static boolean equals(Object obj1, Object obj2) {
        if (obj1 == null) {
            return obj2 == null;
        }
        return obj1.equals(obj2);
    }
}
