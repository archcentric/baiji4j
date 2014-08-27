package com.ctriposs.baiji.rpc.common.formatter;

import com.ctriposs.baiji.JsonSerializer;

public class JsonContentFormatter extends ContentFormatterBase {

    public JsonContentFormatter() {
        super("application/json", "json", "UTF-8", new JsonSerializer());
    }
}
