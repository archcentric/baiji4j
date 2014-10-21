package com.ctriposs.baiji.specific;

import com.alibaba.fastjson.JSONReader;
import com.ctriposs.baiji.exception.BaijiRuntimeException;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.util.Base64;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class SpecificFastJsonReader<T> {

    private final Schema root;

    public SpecificFastJsonReader(Schema schema) {
        this.root = schema;
    }

    @SuppressWarnings("unchecked")
    public T read(T reuse, InputStream is) {
        if (root instanceof RecordSchema) {
            RecordSchema recordSchema = (RecordSchema) root;

            try (JSONReader jsonReader = new JSONReader(new BufferedReader(new InputStreamReader(is)))) {
                return (T) readRecord(recordSchema, reuse, new RecordReader(recordSchema), jsonReader);
            } catch (Exception e) {
                throw new BaijiRuntimeException(e);
            }
        } else {
            throw new BaijiRuntimeException("Schema must be RecordSchema");
        }
    }

    private Object readRecord(RecordSchema schema, Object reuse, JsonReadable recordReader, JSONReader reader) throws Exception {
        Object r = recordReader.read(reuse);
        reader.startObject();
        while (reader.hasNext()) {
            String key = reader.readString();
            if (schema.contains(key)) {
                Field field = schema.getField(key);
                Object value = readValue(field.getSchema(), reader);
                put(r, field.getPos(), value);
            }
        }

        reader.endObject();
        return r;
    }

    private void put(Object obj, int fieldPos, Object fieldValue) {
        ((SpecificRecord) obj).put(fieldPos, fieldValue);
    }

    private Object readValue(Schema schema, JSONReader reader) throws Exception {
        try {
            switch (schema.getType()) {
                case NULL:
                    return readNull();
                case INT:
                    return readInt(reader);
                case BOOLEAN:
                    return readBoolean(reader);
                case DOUBLE:
                    return readDouble(reader);
                case LONG:
                    return readLong(reader);
                case FLOAT:
                    return readFloat(reader);
                case STRING:
                    return readString(reader);
                case BYTES:
                    return readBytes(reader);
                case DATETIME:
                    return readDateTime(reader);
                case RECORD:
                    return readRecord((RecordSchema) schema, null, new RecordReader((RecordSchema) schema), reader);
                case MAP:
                    return readMap((MapSchema) schema, reader);
                case ENUM:
                    return readEnum((EnumSchema) schema, reader);
                case UNION:
                    return readUnion((UnionSchema) schema, reader);
                case ARRAY:
                    return readArray((ArraySchema) schema, reader);
                default:
                    throw new BaijiRuntimeException("No such schema type");
            }
        } catch (NullPointerException e) {
            throw npe(e, " of " + schema.getName());
        }
    }

    private Object readNull() {
        return null;
    }

    private Object readInt(JSONReader reader) {
        return reader.readInteger();
    }

    private Object readBoolean(JSONReader reader) {
        return reader.readObject(Boolean.class);
    }

    private Object readDouble(JSONReader reader) {
        return reader.readObject(Double.class);
    }

    private Object readLong(JSONReader reader) {
        return reader.readObject(Long.class);
    }

    private Object readFloat(JSONReader reader) {
        return reader.readObject(Float.class);
    }

    private Object readString(JSONReader reader) {
        return reader.readString();
    }

    private Object readBytes(JSONReader reader) {
        return Base64.decode(reader.readString());
    }

    private Object readDateTime(JSONReader reader) {
        Calendar calendar = Calendar.getInstance();
        long ms = reader.readLong();
        calendar.setTimeInMillis(ms);
        return calendar;
    }

    private Object readMap(MapSchema schema, JSONReader reader) throws Exception {
        MapReader mapReader = new MapReader(schema);
        Map map = (Map) mapReader.read(null);
        reader.startObject();
        while (reader.hasNext()) {
            String key = reader.readString();
            Object value = readValue(schema.getValueSchema(), reader);
            mapReader.add(map, key, value);
        }
        reader.endObject();

        return map;
    }

    private Object readEnum(EnumSchema schema, JSONReader reader) throws Exception {
        EnumReader enumReader = new EnumReader(schema);
        return enumReader.read(reader.readString());
    }

    private Object readUnion(UnionSchema schema, JSONReader reader) throws Exception {
        for (Schema childSchema : schema.getSchemas()) {
            if (childSchema.getType() == SchemaType.NULL)
                continue;

            return readValue(childSchema, reader);
        }

        return null;
    }

    private Object readArray(ArraySchema schema, JSONReader reader) throws Exception {
        ArrayReader arrayReader = new ArrayReader(schema);
        List list = (List) arrayReader.read(null);
        reader.startArray();
        while (reader.hasNext()) {
            Object value = readValue(schema.getItemSchema(), reader);
            list.add(value);
        }
        reader.endArray();

        return list;
    }

    private interface JsonReadable {
        Object read(Object reuse) throws Exception;
    }

    private class RecordReader implements JsonReadable {

        private final Constructor constructor;

        public RecordReader(RecordSchema schema) {
            constructor = getConstructor(schema);
        }

        @Override
        public Object read(Object reuse) throws Exception {
            return reuse == null ? constructor.newInstance() : reuse;
        }
    }

    private class MapReader implements JsonReadable {

        private final Constructor constructor;

        public MapReader(Schema schema) {
            this.constructor = getConstructor(schema);
        }

        @Override
        public Object read(Object reuse) throws Exception {
            return reuse == null ? constructor.newInstance() : reuse;
        }

        public void add(Object map, String key, Object value) {
            ((Map) map).put(key, value);
        }
    }

    private class EnumReader implements JsonReadable {

        private int[] translator;
        private EnumSchema enumSchema;

        public EnumReader(Schema schema) {
            enumSchema = (EnumSchema) schema;
            translator = new int[enumSchema.getSymbols().size()];
            for (String symbol : enumSchema.getSymbols()) {
                int index = enumSchema.ordinal(symbol);
                translator[index] = enumSchema.ordinal(symbol);
            }
        }

        @Override
        public Object read(Object reuse) throws Exception {
            String value = reuse.toString();
            return Enum.valueOf((Class)ObjectCreator.INSTANCE.getClass(enumSchema), value);
        }
    }

    private class ArrayReader implements JsonReadable {

        private final Constructor constructor;

        public ArrayReader(Schema schema) {
            this.constructor = getConstructor(schema);
        }

        @Override
        public Object read(Object reuse) throws Exception {
            List list;
            if (reuse != null) {
                try {
                    list = (List) reuse;
                } catch (ClassCastException e) {
                    throw new BaijiRuntimeException(e);
                }
                list.clear();
            } else {
                list = (List) constructor.newInstance();
            }

            return list;
        }
    }

    private static Constructor getConstructor(Schema schema) {
        ObjectCreator objectCreator = ObjectCreator.INSTANCE;
        Constructor constructor;
        try {
            constructor = objectCreator.getClass(schema).getConstructor(new Class[]{});
        } catch (NoSuchMethodException e) {
            return null;
        }

        return constructor;
    }

    /** Helper method for adding a message to an NPE. */
    protected NullPointerException npe(NullPointerException e, String s) {
        NullPointerException result = new NullPointerException(e.getMessage()+s);
        result.initCause(e.getCause() == null ? e : e.getCause());
        return result;
    }
}
