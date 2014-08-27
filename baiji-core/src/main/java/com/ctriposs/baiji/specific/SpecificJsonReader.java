package com.ctriposs.baiji.specific;

import com.ctriposs.baiji.generic.DatumReader;
import com.ctriposs.baiji.io.Decoder;
import com.ctriposs.baiji.io.JsonDecoder;
import com.ctriposs.baiji.schema.RecordSchema;
import com.ctriposs.baiji.schema.Schema;

import java.io.IOException;
import java.lang.reflect.Constructor;

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

    /**
     * Parse as JSON Node
     * @param reuse
     * @param source the source string
     * @return a record instance
     */
    public T read(T reuse, String source) {
        return null;
    }

    protected T readRecord(Object reuse, JsonReadable recordReader, RecordSchema recordSchema) {
        return null;
    }

    private class RecordReader implements JsonReadable {

        private final Constructor constructor;

        public RecordReader(RecordSchema recordSchema) {
            this.constructor = getConstructor(recordSchema);
        }

        @Override
        public Object read(Object reuse) throws Exception {
            return reuse == null ? reuse : constructor.newInstance();
        }
    }

    private static Constructor getConstructor(Schema schema) {
        ObjectCreator objectCreator = ObjectCreator.INSTANCE;
        Constructor constructor = null;
        try {
            constructor = objectCreator.getClass(schema).getConstructor(new Class[]{});
        } catch (NoSuchMethodException e) {
            return null;
        }

        return constructor;
    }
}
