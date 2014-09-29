package com.ctriposs.baiji.convert;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TypeConverter {

    private static final ConcurrentMap<Class<?>, Converter> _converterCache = new ConcurrentHashMap<>();

    static {
        _converterCache.put(URI.class, new UriToStringConverter());
    }

    public static <S, T> T convert(S source) throws Exception {
        Converter converter = _converterCache.get(source.getClass());
        return (T) converter.convert(source);
    }

    private static class UriToStringConverter implements Converter<URI, String> {

        @Override
        public String convert(URI source) throws Exception {
            return source != null ? source.toString() : "";
        }
    }

    private static class StringToUriConverter implements Converter<String, URI> {

        @Override
        public URI convert(String source) throws Exception {
            return source != null ? new URI(source) : null;
        }
    }
}
