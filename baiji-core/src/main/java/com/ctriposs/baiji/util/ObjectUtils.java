package com.ctriposs.baiji.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class ObjectUtils {

    public static int hashCode(Object obj) {
        return obj != null ? obj.hashCode() : 0;
    }

    public static boolean equals(Map<?, ?> map1, Map<?, ?> map2) {
        if (map1 == null) {
            return map2 == null;
        }
        if (map1.size() != map2.size()) {
            return false;
        }
        for (Map.Entry<?, ?> entry1 : map1.entrySet()) {
            if (!map2.containsKey(entry1.getKey())) {
                return false;
            }
            if (!equals(entry1.getValue(), map2.get(entry1.getKey()))) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(Object obj1, Object obj2) {
        if (obj1 == null) {
            return obj2 == null;
        }
        if (obj1 instanceof byte[] && obj2 instanceof byte[]) {
            return Arrays.equals((byte[]) obj1, (byte[]) obj2);
        } else if (obj1 instanceof Object[] && obj2 instanceof Object[]) {
            return Arrays.deepEquals((Object[]) obj1, (Object[]) obj2);
        } else {
            return obj1.equals(obj2);
        }
    }

    public static boolean equals(List<?> list1, List<?> list2) {
        if (list1 == null)
            return list2 == null;

        if (list1.size() != list2.size())
            return false;

        for (int i = 0; i < list1.size(); i++) {
            if (list1.get(i) != list2.get(i))
                return false;
        }

        return true;
    }
}
