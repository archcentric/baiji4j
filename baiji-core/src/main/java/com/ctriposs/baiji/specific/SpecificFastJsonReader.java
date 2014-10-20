package com.ctriposs.baiji.specific;

import com.alibaba.fastjson.JSONReader;
import com.ctriposs.baiji.exception.BaijiRuntimeException;
import com.ctriposs.baiji.schema.Field;
import com.ctriposs.baiji.schema.RecordSchema;
import com.ctriposs.baiji.schema.Schema;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

public class SpecificFastJsonReader<T> {

    @SuppressWarnings("unchecked")
    public T read(Schema schema, InputStream is) {
        if (schema instanceof RecordSchema) {
            RecordSchema recordSchema = (RecordSchema) schema;
            JSONReader jsonReader = new JSONReader(new BufferedReader(new InputStreamReader(is)));

            return (T) readRecord(recordSchema, jsonReader);
        } else {
            throw new BaijiRuntimeException("Schema must be RecordSchema");
        }
    }

    private Object readRecord(RecordSchema schema, JSONReader reader) {
        Iterator<Field> iterator = schema.getFields().iterator();
        reader.startObject();
        while (iterator.hasNext() && reader.hasNext()) {
            Field field = iterator.next();

        }

        return null;
    }

    private Object readValue(Schema schema, JSONReader reader) {
        return null;
    }

}
