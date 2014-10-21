package com.ctriposs.baiji.specific;


import com.ctriposs.baiji.exception.BaijiRuntimeException;
import com.ctriposs.baiji.schema.*;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public final class SpecificJsonWriter<T> {

    static final JsonFactory FACTORY = new JsonFactory();

    static {
        FACTORY.enable(JsonParser.Feature.ALLOW_COMMENTS);
    }

    /**
     * The only public write interface
     * @param schema the object schema
     * @param obj the object
     * @param os the final output stream
     */
    public void write(Schema schema, T obj, OutputStream os) {
        if (schema instanceof RecordSchema) {
            if (os != null) {
                try {
                    RecordSchema recordSchema = (RecordSchema) schema;
                    JsonGenerator g = FACTORY.createJsonGenerator(os, JsonEncoding.UTF8);
                    writeRecord(recordSchema, obj, g);
                    g.flush();
                    g.close();
                } catch (IOException e) {
                    throw new BaijiRuntimeException("Serialize process failed.", e);
                }
            } else {
                throw new BaijiRuntimeException("Output stream can't be null");
            }
        } else {
            throw new BaijiRuntimeException("schema must be RecordSchema");
        }
    }

    /** Called to write record.*/
    protected void writeRecord(RecordSchema recordSchema, Object datum, JsonGenerator generator) throws IOException {
        List<Field> fields = recordSchema.getFields();
        generator.writeStartObject();
        for (Field field : fields) {
            Object value = ((SpecificRecord) datum).get(field.getPos());
            if (value == null)
                continue;
            generator.writeFieldName(field.getName());
            writeValue(field.getSchema(), value, generator);
        }
        generator.writeEndObject();
    }

    /** Called to write value.*/
    private void writeValue(Schema fieldSchema, Object datum, JsonGenerator generator) throws IOException {
        try {
            switch (fieldSchema.getType()) {
                case INT:
                    generator.writeNumber((Integer) datum);
                    break;
                case LONG:
                    generator.writeNumber((Long) datum);
                    break;
                case DOUBLE:
                    generator.writeNumber((Double) datum);
                    break;
                case FLOAT:
                    generator.writeNumber((Float) datum);
                    break;
                case BOOLEAN:
                    generator.writeBoolean((Boolean) datum);
                    break;
                case STRING:
                    generator.writeString(datum.toString());
                    break;
                case BYTES:
                    writeBytes(datum, generator);
                    break;
                case NULL:
                    generator.writeNull();
                    break;
                case DATETIME:
                    generator.writeNumber(((Calendar) datum).getTimeInMillis());
                    break;
                case RECORD:
                    writeRecord((RecordSchema) fieldSchema, datum, generator);
                    break;
                case ENUM:
                    writeEnum((EnumSchema) fieldSchema, (Enum) datum, generator);
                    break;
                case ARRAY:
                    writeArray((ArraySchema) fieldSchema, (List) datum, generator);
                    break;
                case MAP:
                    writeMap((MapSchema) fieldSchema, (Map) datum, generator);
                    break;
                case UNION:
                    writeUnion((UnionSchema) fieldSchema, datum, generator);
                    break;
                default:
                    error(fieldSchema, datum);
                    break;
            }
        } catch (NullPointerException e) {
            throw npe(e, " of "+ fieldSchema.getName());
        }
    }

    /** Called to write bytes.*/
    private void writeBytes(Object datum, JsonGenerator generator) throws IOException {
        byte[] bytes = (byte[]) datum;
        generator.writeBinary(bytes, 0, bytes.length);
    }

    /** Called to write enum.*/
    private void writeEnum(EnumSchema enumSchema, Enum en, JsonGenerator generator) throws IOException {
        String value = enumSchema.get(en.ordinal());
        generator.writeString(value);
    }

    /** Called to write array.*/
    private void writeArray(ArraySchema arraySchema, List array, JsonGenerator generator) throws IOException {
        Schema itemSchema = arraySchema.getItemSchema();
        generator.writeStartArray();
        for (Iterator iterator = getArrayElements(array); iterator.hasNext();) {
            writeValue(itemSchema, iterator.next(), generator);
        }
        generator.writeEndArray();
    }

    /** Called by {@link #writeArray} to enumerate array elements.*/
    private Iterator getArrayElements(List array) {
        return array.iterator();
    }

    /** Called to write map.*/
    private void writeMap(MapSchema mapSchema, Map map, JsonGenerator generator) throws IOException {
        Schema valueSchema = mapSchema.getValueSchema();
        Iterable<Map.Entry<Object, Object>> iterable = getMapEntries(map);
        generator.writeStartObject();
        for (Map.Entry<Object, Object> entry : iterable) {
            generator.writeFieldName(entry.getKey().toString());
            writeValue(valueSchema, entry.getValue(), generator);
        }
        generator.writeEndObject();
    }

    /** Called by {@link #writeMap} to enumerate map elements.*/
    private Iterable<Map.Entry<Object, Object>> getMapEntries(Map map) {
        return map.entrySet();
    }

    /** Called to write union.*/
    private void writeUnion(UnionSchema unionSchema, Object datum, JsonGenerator generator) throws IOException {
        int size = unionSchema.size();
        for (int i = 0; i < size; i++) {
            if (unionSchema.get(i).getType() == SchemaType.NULL)
                continue;

            writeValue(unionSchema.get(i), datum, generator);
            return;
        }
    }

    /** Error method.*/
    private void error(Schema schema, Object datum) {
        throw new BaijiRuntimeException("Not a " + schema + ": " + datum);
    }

    /** Helper method for adding a message to an NPE. */
    protected NullPointerException npe(NullPointerException e, String s) {
        NullPointerException result = new NullPointerException(e.getMessage()+s);
        result.initCause(e.getCause() == null ? e : e.getCause());
        return result;
    }

    private interface JsonWritable {
        void write(Schema schema, Object datum, JsonGenerator g) throws Exception;
    }

    private class IntegerWriter implements JsonWritable {

        @Override
        public void write(Schema schema, Object datum, JsonGenerator g) throws Exception {
            g.writeNumber((Integer) datum);
        }
    }

    private class LongWriter implements JsonWritable {

        @Override
        public void write(Schema schema, Object datum, JsonGenerator g) throws Exception {
            g.writeNumber((Long) datum);
        }
    }

    private class DoubleWriter implements JsonWritable {

        @Override
        public void write(Schema schema, Object datum, JsonGenerator g) throws Exception {
            g.writeNumber((Double) datum);
        }
    }

    private class FloatWriter implements JsonWritable {

        @Override
        public void write(Schema schema, Object datum, JsonGenerator g) throws Exception {
            g.writeNumber((Float) datum);
        }
    }

    private class BooleanWriter implements JsonWritable {

        @Override
        public void write(Schema schema, Object datum, JsonGenerator g) throws Exception {
            g.writeBoolean((Boolean) datum);
        }
    }

    private class StringWriter implements JsonWritable {

        @Override
        public void write(Schema schema, Object datum, JsonGenerator g) throws Exception {
            g.writeString(datum.toString());
        }
    }

    private class BytesWriter implements JsonWritable {

        @Override
        public void write(Schema schema, Object datum, JsonGenerator g) throws Exception {
            byte[] bytes = (byte[]) datum;
            g.writeBinary(bytes);
        }
    }
}
