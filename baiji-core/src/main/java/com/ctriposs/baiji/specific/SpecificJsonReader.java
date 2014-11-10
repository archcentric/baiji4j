package com.ctriposs.baiji.specific;

import com.ctriposs.baiji.exception.BaijiRuntimeException;
import com.ctriposs.baiji.schema.*;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class SpecificJsonReader<T> {

    private static final JsonFactory FACTORY = new JsonFactory();
    private final Schema root;

    public SpecificJsonReader(Schema root) {
        this.root = root;
    }

    /**
     * The only public interface
     * @param reuse the reuse object
     * @param is the input stream
     * @return an object
     */
    @SuppressWarnings("unchecked")
    public T read(T reuse, InputStream is) {
        if (root instanceof RecordSchema) {
            RecordSchema schema = (RecordSchema) root;

            try (JsonParser jp = FACTORY.createJsonParser(is)) {
                return (T) readRecord(schema, reuse, new RecordReader(schema), jp, false);
            } catch (Exception e) {
                throw new BaijiRuntimeException(e);
            }
        } else {
            throw new BaijiRuntimeException("Schema must be RecordSchema");
        }
    }

    private Object readRecord(RecordSchema schema, Object reuse, JsonReadable reader, JsonParser jp, boolean nested) throws Exception {
        Object record = reader.read(reuse);
        if (!nested) {
            jp.nextToken();
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jp.getCurrentName();
            Field field = schema.getField(fieldName);
            if (field != null) {
                jp.nextToken();
                Object value = readValue(field.getSchema(), jp);
                put(record, field.getPos(), value);
            } else {
                throw new BaijiRuntimeException("The schema has no such field");
            }
        }

        return record;
    }

    private void put(Object obj, int fieldPos, Object fieldValue) {
        ((SpecificRecord) obj).put(fieldPos, fieldValue);
    }

    private Object readValue(Schema schema, JsonParser jp) throws Exception {
        try {
            switch (schema.getType()) {
                case NULL:
                    return readNull();
                case INT:
                    return readInt(jp);
                case BOOLEAN:
                    return readBoolean(jp);
                case DOUBLE:
                    return readDouble(jp);
                case LONG:
                    return readLong(jp);
                case FLOAT:
                    return readFloat(jp);
                case STRING:
                    return readString(jp);
                case BYTES:
                    return readBytes(jp);
                case DATETIME:
                    return readDateTime(jp);
                case RECORD:
                    return readRecord((RecordSchema) schema, null, new RecordReader((RecordSchema) schema), jp, true);
                case MAP:
                    return readMap((MapSchema) schema, jp);
                case ENUM:
                    return readEnum((EnumSchema) schema, jp);
                case UNION:
                    return readUnion((UnionSchema) schema, jp);
                case ARRAY:
                    return readArray((ArraySchema) schema, jp);
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

    private Object readInt(JsonParser jp) throws IOException {
        return jp.getIntValue();
    }

    private Boolean readBoolean(JsonParser jp) throws IOException {
        return jp.getCurrentToken() == JsonToken.VALUE_TRUE;
    }

    private Object readDouble(JsonParser jp) throws IOException {
        return jp.getDoubleValue();
    }

    private Object readFloat(JsonParser jp) throws IOException {
        return jp.getFloatValue();
    }

    private Object readLong(JsonParser jp) throws IOException {
        return jp.getLongValue();
    }

    private Object readString(JsonParser jp) throws IOException {
        return jp.getText();
    }

    private Object readBytes(JsonParser jp) throws IOException {
        return jp.getBinaryValue();
    }

    private Object readDateTime(JsonParser jp) throws IOException {
        Calendar calendar = Calendar.getInstance();
        long ms = jp.getNumberValue().longValue();
        calendar.setTimeInMillis(ms);
        return calendar;
    }

    private Object readMap(MapSchema schema, JsonParser jp) throws Exception {
        MapReader reader = new MapReader(schema);
        Map map = (Map) reader.read(null);
        Schema valueSchema = schema.getValueSchema();
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String key = jp.getCurrentName();
            jp.nextToken();
            Object value = readValue(valueSchema, jp);
            reader.add(map, key, value);
        }

        return map;
    }

    private Object readEnum(EnumSchema schema, JsonParser jp) throws Exception {
        EnumReader enumReader = new EnumReader(schema);
        return enumReader.read(jp.getText());
    }

    private Object readUnion(UnionSchema schema, JsonParser jp) throws Exception {
        if (schema.size() != 2) {
            throw new BaijiRuntimeException("UnionSchema size must be 2");
        }

        for (Schema itemSchema : schema.getSchemas()) {
            if (itemSchema.getType() == SchemaType.NULL)
                continue;

            return readValue(itemSchema, jp);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private Object readArray(ArraySchema schema, JsonParser jp) throws Exception {
        ArrayReader reader = new ArrayReader(schema);
        Schema itemSchema = schema.getItemSchema();
        List list = (List) reader.read(null);
        while (jp.nextToken() != JsonToken.END_ARRAY) {
            Object value = readValue(itemSchema, jp);
            list.add(value);
        }

        return list;
    }

    private interface JsonReadable {
        Object read(Object reuse) throws Exception;
    }

    private static class RecordReader implements JsonReadable {

        private final Class<?> clazz;

        public RecordReader(RecordSchema schema) {
            clazz = getClazz(schema);
        }

        @Override
        public Object read(Object reuse) throws Exception {
            return reuse == null ? clazz.newInstance() : reuse;
        }
    }

    private static class MapReader implements JsonReadable {

        private final Class<?> clazz;

        public MapReader(Schema schema) {
            clazz = getClazz(schema);
        }

        @Override
        public Object read(Object reuse) throws Exception {
            return reuse == null ? clazz.newInstance() : reuse;
        }

        @SuppressWarnings("unchecked")
        public void add(Object map, String key, Object value) {
            ((Map) map).put(key, value);
        }
    }

    private static class EnumReader implements JsonReadable {

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

    private static class ArrayReader implements JsonReadable {

        private final Class<?> clazz;

        public ArrayReader(Schema schema) {
            clazz = getClazz(schema);
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
                list = (List) clazz.newInstance();
            }

            return list;
        }
    }

    private static Class<?> getClazz(Schema schema) {
        ObjectCreator objectCreator = ObjectCreator.INSTANCE;
        return objectCreator.getClass(schema);
    }

    /** Helper method for adding a message to an NPE. */
    protected NullPointerException npe(NullPointerException e, String s) {
        NullPointerException result = new NullPointerException(e.getMessage()+s);
        result.initCause(e.getCause() == null ? e : e.getCause());
        return result;
    }
}
