package com.ctriposs.baiji.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TypeConverter {

    private static final ConcurrentMap<String, Converter> _converterCache = new ConcurrentHashMap<>();

    static {
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
    public static <S, D> D convert(S source, Class<D> clazz)  {
        String key = source.getClass().getName() + "-" + clazz.getName();
        Converter converter = _converterCache.get(key);
        return (D) converter.convert(source, clazz);
    }

    @SuppressWarnings(value="unchecked")
    public static <S, D> List<D> convertToList(List<S> sList, Class<D> clazz) {
        if (sList == null || sList.size() == 0)
            return null;

        List<D> list = new ArrayList<>();
        String key = sList.get(0).getClass().getName() + "-" + clazz.getName();
        Converter converter = _converterCache.get(key);
        for (S s : sList) {
            list.add((D) converter.convert(s, clazz));
        }

        return list;
    }

    /**
     * Register converter for customer extension
     * @param sClass the source class type
     * @param dClass the destination class type
     * @param converter the specific converter
     * @param <S> S
     * @param <D> D
     */
    public static <S, D> void registerConverter(Class<S> sClass, Class<D> dClass, Converter converter) {
        String key = sClass.getName() + "-" + dClass.getName();
        _converterCache.putIfAbsent(key, converter);
    }

    private static class ObjectToStringConverter implements Converter<Object, String> {

        @Override
        public String convert(Object source, Class<String> clazz) {
            return source != null ? source.toString() : null;
        }
    }

    private static class ByteToIntConverter implements Converter<Byte, Integer> {

        @Override
        public Integer convert(Byte source, Class<Integer> clazz) {
            return source != null ? source.intValue() : null;
        }
    }

    private static class IntToByteConverter implements Converter<Integer, Byte> {

        @Override
        public Byte convert(Integer source, Class<Byte> clazz) {
            return source != null ? source.byteValue() : null;
        }
    }

    private static class StringToBigDecimalConverter implements Converter<String, BigDecimal> {

        @Override
        public BigDecimal convert(String source, Class<BigDecimal> clazz) {
            return source != null ? new BigDecimal(source) : null;
        }
    }

    private static class IntToShortConverter implements Converter<Integer, Short> {

        @Override
        public Short convert(Integer source, Class<Short> clazz) {
            return source != null ? source.shortValue() : null;
        }
    }

    private static class ShortToIntConverter implements Converter<Short, Integer> {

        @Override
        public Integer convert(Short source, Class<Integer> clazz) {
            return source != null ? source.intValue() : null;
        }
    }

    private static class StringToBigIntegerConverter implements Converter<String, BigInteger> {

        @Override
        public BigInteger convert(String source, Class<BigInteger> clazz) {
            return source != null ? new BigInteger(source) : null;
        }
    }
}
