package com.ctriposs.baiji.specific;

import com.ctriposs.baiji.generic.DatumWriter;
import com.ctriposs.baiji.io.Encoder;
import com.ctriposs.baiji.io.JsonEncoder;
import com.ctriposs.baiji.schema.*;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpecificJsonWriter<T> {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String CHARSET = "ISO-8859-1";

    private Schema root;

    public SpecificJsonWriter(Schema root) {
        this.root = root;
    }

    public Schema getSchema() {
        return root;
    }

    public void write(T datum, Encoder out) throws IOException {
        writeRecord(root, datum, out);
    }

    /** Called to write data.*/
    protected void write(Schema schema,Object datum, JsonEncoder out) throws IOException {
        try {
            switch (schema.getType()) {
                case INT:
                    out.writeInt(((Number)datum).intValue());
                    break;
                case LONG:
                    out.writeLong((Long)datum);
                    break;
                case FLOAT:
                    out.writeFloat((Float)datum);
                    break;
                case DOUBLE:
                    out.writeDouble((Double)datum);
                    break;
                case BOOLEAN:
                    out.writeBoolean((Boolean) datum);
                    break;
                case STRING:
                    out.writeString(datum.toString());
                    break;
                case BYTES:
                    out.writeBytes((byte[])datum);
                    break;
                case NULL:
                    out.writeNull();
                    break;
                case RECORD:
                    writeInnerRecord(schema, datum, out);
                    break;
                case ENUM:
                    writeEnum(schema, datum, out);
                    break;
                case ARRAY:
                    writeArray(schema, datum, out);
                    break;
                case MAP:
                    writeMap(schema, datum, out);
                    break;
                case UNION:
                    writeUnion(schema, datum, out);
                    break;
                default:
            }
        } catch (NullPointerException e) {

        }
    }

    /** Called to write a record.*/
    protected void writeRecord(Schema schema, Object datum, Encoder out) throws IOException {
        JsonEncoder jsonEncoder = (JsonEncoder) out;
        RecordSchema recordSchema = (RecordSchema) schema;
        jsonEncoder.writeStartObject();
        for (Field field : recordSchema.getFields()) {
            Object value = ((SpecificRecord) datum).get(field.getPos());
            if (value == null)
                continue;
            jsonEncoder.writeFieldName(field.getName());
            writeFieldValue(value, field, jsonEncoder);
        }
        jsonEncoder.writeEndObject();
        jsonEncoder.flush();
    }

    protected void writeInnerRecord(Schema schema, Object datum, Encoder out) throws IOException {
        JsonEncoder jsonEncoder = (JsonEncoder) out;
        RecordSchema recordSchema = (RecordSchema) schema;
        jsonEncoder.writeStartObject();
        for (Field field : recordSchema.getFields()) {
            Object value = ((SpecificRecord) datum).get(field.getPos());
            if (value == null)
                continue;
            jsonEncoder.writeFieldName(field.getName());
            writeFieldValue(value, field, jsonEncoder);
        }
        jsonEncoder.writeEndObject();
    }

    /** Called to write a single field of a record.*/
    protected void writeFieldValue(Object datum, Field f, JsonEncoder out) throws IOException {
        try {
            write(f.getSchema(), datum, out);
        } catch (NullPointerException e) {

        }
    }

    /** Called to write a array.*/
    protected void writeArray(Schema schema, Object datum, JsonEncoder out) throws IOException {
        ArraySchema arraySchema = (ArraySchema) schema;
        Schema element = arraySchema.getItemSchema();
        long size = getArraySize(datum);
        out.writeArrayStart();
        out.setItemCount(size);
        for (Iterator iterator = getArrayElements(datum); iterator.hasNext();) {
            out.startItem();
            write(element, iterator.next(), out);
        }
        out.writeArrayEnd();
    }

    /**
     * Called by {@link #writeArray} to get the size of an array.
     * @param array an array
     * @return the size of an array
     */
    protected long getArraySize(Object array) {
        return ((List)array).size();
    }

    /** Called by {@link #writeArray} to enumerate array elements.*/
    protected Iterator getArrayElements(Object array) {
        return ((List) array).iterator();
    }

    /** Called to write an enum value.*/
    protected void writeEnum(Schema schema, Object datum, JsonEncoder out) throws IOException {
        EnumSchema enumSchema = (EnumSchema) schema;
        String value = enumSchema.get(((Enum) datum).ordinal());
        int[] values = new int[enumSchema.size()];
        for (String symbol : enumSchema.getSymbols()) {
            values[enumSchema.ordinal(symbol)] = enumSchema.ordinal(symbol);
        }
        out.writeString(value);
    }

    /** Called to write a map.*/
    protected void writeMap(Schema schema, Object datum, JsonEncoder out) throws IOException {
        MapSchema mapSchema = (MapSchema) schema;
        Schema value = mapSchema.getValueSchema();
        int size = getMapSize(datum);
        out.writeMapStart();
        out.setItemCount(size);
        for (Map.Entry<Object, Object> entry : getMapEntries(datum)) {
            out.startItem();
            out.writeFieldName(entry.getKey().toString());
            write(value, entry.getValue(), out);
        }
        out.writeMapEnd();
    }

    /** Called by {@link #writeMap} to get the size of a map.*/
    protected int getMapSize(Object map) {
        return ((Map) map).size();
    }

    /** Called by {@link #writeMap} to enumerate map elements.*/
    protected Iterable<Map.Entry<Object, Object>> getMapEntries(Object map) {
        return ((Map) map).entrySet();
    }

    /** Called to write an union.*/
    protected void writeUnion(Schema schema, Object datum, JsonEncoder out) throws IOException {
        UnionSchema unionSchema = (UnionSchema) schema;
        for (int i = 0; i < unionSchema.size(); i++) {
            if (unionSchema.get(i).getType() == SchemaType.NULL) {
                continue;
            }

            write(unionSchema.get(i), datum, out);
            return;
        }
    }
}
