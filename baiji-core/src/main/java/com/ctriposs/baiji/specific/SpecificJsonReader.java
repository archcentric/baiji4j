package com.ctriposs.baiji.specific;

import com.ctriposs.baiji.generic.DatumReader;
import com.ctriposs.baiji.io.Decoder;
import com.ctriposs.baiji.schema.Schema;

import java.io.IOException;

public class SpecificJsonReader<T> implements DatumReader<T> {

    public SpecificJsonReader(Schema schema) {

    }

    @Override
    public Schema getSchema() {
        return null;
    }

    @Override
    public T read(T reuse, Decoder in) throws IOException {
        return null;
    }
}
