package com.ctriposs.baiji.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TypeConverter {

    private static final ConcurrentMap<String, Converter> _converterCache = new ConcurrentHashMap<>();

    static {
        _converterCache.put(URI.class.getName() + "-" + String.class.getName(), new ObjectToStringConverter());
        _converterCache.put(String.class.getName() + "-" + URI.class.getName(), new StringToUriConverter());
        _converterCache.put(Byte.class.getName() + "-" + Integer.class.getName(), new ByteToIntConverter());
        _converterCache.put(Integer.class.getName() + "-" + Byte.class.getName(), new IntToByteConverter());
        _converterCache.put(BigDecimal.class.getName() + "-" + String.class.getName(), new ObjectToStringConverter());
        _converterCache.put(String.class.getName() + "-" + BigDecimal.class.getName(), new StringToBigDecimalConverter());
        _converterCache.put(Integer.class.getName() + "-" + Short.class.getName(), new IntToShortConverter());
        _converterCache.put(Short.class.getName() + "-" + Integer.class.getName(), new ShortToIntConverter());
        _converterCache.put(BigInteger.class.getName() + "-" + String.class.getName(), new ObjectToStringConverter());
        _converterCache.put(String.class.getName() + "-" + BigInteger.class.getName(), new StringToBigIntegerConverter());
    }

    @SuppressWarnings(value="unchecked")
    public static <S, D> D convert(S source, Class<D> clazz) throws Exception {
        String key = source.getClass().getName() + "-" + clazz.getName();
        Converter converter = _converterCache.get(key);
        return (D) converter.convert(source, clazz);
    }

    private static class ObjectToStringConverter implements Converter<Object, String> {

        @Override
        public String convert(Object source, Class<String> clazz) throws Exception {
            return source != null ? source.toString() : null;
        }
    }

    private static class StringToUriConverter implements Converter<String, URI> {

        @Override
        public URI convert(String source, Class<URI> clazz) throws Exception {
            return source != null ? new URI(source) : null;
        }
    }

    private static class ByteToIntConverter implements Converter<Byte, Integer> {

        @Override
        public Integer convert(Byte source, Class<Integer> clazz) throws Exception {
            return source != null ? source.intValue() : null;
        }
    }

    private static class IntToByteConverter implements Converter<Integer, Byte> {

        @Override
        public Byte convert(Integer source, Class<Byte> clazz) throws Exception {
            return source != null ? source.byteValue() : null;
        }
    }

    private static class StringToBigDecimalConverter implements Converter<String, BigDecimal> {

        @Override
        public BigDecimal convert(String source, Class<BigDecimal> clazz) throws Exception {
            return source != null ? new BigDecimal(source) : null;
        }
    }

    private static class IntToShortConverter implements Converter<Integer, Short> {

        @Override
        public Short convert(Integer source, Class<Short> clazz) throws Exception {
            return source != null ? source.shortValue() : null;
        }
    }

    private static class ShortToIntConverter implements Converter<Short, Integer> {

        @Override
        public Integer convert(Short source, Class<Integer> clazz) throws Exception {
            return source != null ? source.intValue() : null;
        }
    }

    private static class StringToBigIntegerConverter implements Converter<String, BigInteger> {

        @Override
        public BigInteger convert(String source, Class<BigInteger> clazz) throws Exception {
            return source != null ? new BigInteger(source) : null;
        }
    }
}
