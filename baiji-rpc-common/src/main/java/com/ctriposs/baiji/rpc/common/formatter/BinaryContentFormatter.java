package com.ctriposs.baiji.rpc.common.formatter;

import com.ctriposs.baiji.BinarySerializer;

public class BinaryContentFormatter extends ContentFormatterBase {

    public static final String MEDIA_TYPE = "application/x-baiji-bin";
    public static final String EXTENSION = "bin";

    public BinaryContentFormatter() {
        super(MEDIA_TYPE, EXTENSION, null, new BinarySerializer());
    }
}
