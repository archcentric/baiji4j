package com.ctriposs.baiji.convert;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TypeConverter {

    private static final ConcurrentMap<Class<?>, Converter> _converterCache = new ConcurrentHashMap<>();

    static {

    }

    public static <S, T> T convert(S source) {
        Converter converter = _converterCache.get(source.getClass());
        return (T) converter.convert(source);
    }
}
