package com.ctriposs.baiji.rpc.common.formatter;

import com.ctriposs.baiji.BinarySerializer;

public class BinaryContentFormatter extends ContentFormatterBase {

    public BinaryContentFormatter() {
        super("application/x-baiji-bin", "bin", "UTF-8", new BinarySerializer());
    }
}
