package com.ctriposs.baiji.convert;

public interface Converter<S, D> {

    D convert(S source, Class<D> clazz);
}
