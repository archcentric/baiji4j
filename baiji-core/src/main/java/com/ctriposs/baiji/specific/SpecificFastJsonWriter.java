package com.ctriposs.baiji.specific;

import com.alibaba.fastjson.JSONWriter;
import com.ctriposs.baiji.exception.BaijiRuntimeException;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.util.Base64;

import java.io.*;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SpecificFastJsonWriter<T> {

    /**
     * The only public write interface
     * @param schema the object schema
     * @param obj the java object
     * @param os the destination output stream
     */
    public void write(Schema schema, T obj, OutputStream os) {
        if (schema instanceof RecordSchema) {
            if (os != null) {
                try {
                    RecordSchema recordSchema = (RecordSchema) schema;
                    JSONWriter jsonWriter = new JSONWriter(new BufferedWriter(new OutputStreamWriter(os)));
                    writeRecord(recordSchema, obj, jsonWriter);
                    jsonWriter.close();
                } catch (IOException e) {
                    throw new BaijiRuntimeException("Baiji serialization process wrong");
                }
            } else {
                throw new BaijiRuntimeException("OutputStream can't be null");
            }
        } else {
            throw new BaijiRuntimeException("schema must be a RecordSchema");
        }
    }

    private void writeRecord(RecordSchema schema, Object datum, JSONWriter writer) {
        List<Field> fields = schema.getFields();
        writer.startObject();
        for (Field field : fields) {
            Object value = ((SpecificRecord) datum).get(field.getPos());
            if (value == null)
                continue;
            writer.writeKey(field.getName());
            writeValue(field.getSchema(), value, writer);
        }
        writer.endObject();
    }

    private void writeValue(Schema fieldSchema, Object datum, JSONWriter writer) {
        try {
            switch (fieldSchema.getType()) {
                case INT:
                    writer.writeValue(datum);
                    break;
                case LONG:
                    writer.writeValue(datum);
                    break;
                case DOUBLE:
                    writer.writeValue(datum);
                    break;
                case FLOAT:
                    writer.writeValue(datum);
                    break;
                case BOOLEAN:
                    writer.writeValue(datum);
                    break;
                case STRING:
                    writer.writeValue(datum.toString());
                    break;
                case BYTES:
                    writeBytes(datum, writer);
                    break;
                case NULL:
                    writer.writeValue(null);
                    break;
                case DATETIME:
                    writer.writeValue(((Calendar) datum).getTimeInMillis());
                    break;
                case RECORD:
                    writeRecord((RecordSchema) fieldSchema, datum, writer);
                    break;
                case ENUM:
                    writeEnum((EnumSchema) fieldSchema, (Enum) datum, writer);
                    break;
                case ARRAY:
                    writeArray((ArraySchema) fieldSchema, (List) datum, writer);
                    break;
                case MAP:
                    writeMap((MapSchema) fieldSchema, (Map) datum, writer);
                    break;
                case UNION:
                    writeUnion((UnionSchema) fieldSchema, datum, writer);
                    break;
                default:
                    error(fieldSchema, datum);
            }
        } catch (NullPointerException e) {
            throw npe(e, " of "+ fieldSchema.getName());
        }
    }

    private void writeBytes(Object datum, JSONWriter writer) {
        byte[] bytes = (byte[]) datum;
        writer.writeValue(Base64.encode(bytes));
    }

    private void writeEnum(EnumSchema schema, Enum en, JSONWriter writer) {
        String value = schema.get(en.ordinal());
        writer.writeObject(value);
    }

    private void writeArray(ArraySchema schema, List array, JSONWriter writer) {
        Schema itemSchema = schema.getItemSchema();
        writer.startArray();
        for (Iterator iterator = getArrayElements(array); iterator.hasNext();) {
            writeValue(itemSchema, iterator.next(), writer);
        }
        writer.endArray();
    }

    /** Called by {@link #writeArray} to enumerate array elements.*/
    private Iterator getArrayElements(List array) {
        return array.iterator();
    }

    /** Called to write map.*/
    private void writeMap(MapSchema schema, Map map, JSONWriter writer) {
        Schema valueSchema = schema.getValueSchema();
        Iterable<Map.Entry<Object, Object>> iterable = getMapEntries(map);
        writer.startObject();
        for (Map.Entry entry : iterable) {
            writer.writeKey(entry.getKey().toString());
            writeValue(valueSchema, entry.getValue(), writer);
        }
        writer.endObject();
    }

    /** Called by {@link #writeMap} to enumerate map elements.*/
    @SuppressWarnings("unchecked")
    private Iterable<Map.Entry<Object, Object>> getMapEntries(Map map) {
        return map.entrySet();
    }

    private void writeUnion(UnionSchema schema, Object datum, JSONWriter writer) {
        if (schema.size() != 2) {
            // default do nothing if schema size is not 2
            return;
        }

        for (Schema childSchema : schema.getSchemas()) {
            if (childSchema.getType() == SchemaType.NULL)
                continue;

            writeValue(childSchema, datum, writer);
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
}
