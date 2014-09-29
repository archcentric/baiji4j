package com.ctriposs.baiji.convert;

public interface Converter<S, T> {

    T convert(S source) throws Exception;
}
