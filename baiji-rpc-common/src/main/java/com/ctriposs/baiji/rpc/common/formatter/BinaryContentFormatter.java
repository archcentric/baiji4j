package com.ctriposs.baiji.rpc.common.formatter;

import com.ctriposs.baiji.BinarySerializer;

public class BinaryContentFormatter extends ContentFormatterBase {

    public static final String MEDIA_TYPE = "application/bjbin";
    public static final String EXTENSION = "bjbin";

    public BinaryContentFormatter() {
        super(MEDIA_TYPE, EXTENSION, null, new BinarySerializer());
    }
}
