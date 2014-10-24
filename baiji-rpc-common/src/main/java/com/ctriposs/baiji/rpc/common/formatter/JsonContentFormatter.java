package com.ctriposs.baiji.rpc.common.formatter;

import com.ctriposs.baiji.JsonSerializer;

public class JsonContentFormatter extends ContentFormatterBase {

    public static final String MEDIA_TYPE = "application/x-baiji-json";
    public static final String EXTENSION = "bjjson";

    public JsonContentFormatter() {
        super(MEDIA_TYPE, EXTENSION, "UTF-8", new JsonSerializer());
    }
}
