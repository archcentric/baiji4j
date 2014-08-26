package com.ctriposs.baiji.specific;

import com.ctriposs.baiji.generic.DatumReader;
import com.ctriposs.baiji.io.Decoder;
import com.ctriposs.baiji.io.JsonDecoder;
import com.ctriposs.baiji.schema.RecordSchema;
import com.ctriposs.baiji.schema.Schema;

import java.io.IOException;

public class SpecificJsonReader<T> implements DatumReader<T> {

    private Schema root;

    public SpecificJsonReader(Schema schema) {
        this.root = schema;
    }

    @Override
    public Schema getSchema() {
        return root;
    }

    @Override
    public T read(T reuse, Decoder in) throws IOException {
        return null;
    }

    protected T readRecord(Schema schema, Decoder in) {
        RecordSchema recordSchema = (RecordSchema) schema;
        JsonDecoder jsonDecoder = (JsonDecoder) in;

    }
}
