package com.ctriposs.baiji.specific;

import com.ctriposs.baiji.generic.DatumWriter;
import com.ctriposs.baiji.io.Encoder;
import com.ctriposs.baiji.io.JsonEncoder;
import com.ctriposs.baiji.schema.ArraySchema;
import com.ctriposs.baiji.schema.Field;
import com.ctriposs.baiji.schema.RecordSchema;
import com.ctriposs.baiji.schema.Schema;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;

public class SpecificJsonWriter<T> implements DatumWriter<T> {

    private Schema root;

    public SpecificJsonWriter(Schema root) {
        this.root = root;
    }

    @Override
    public Schema getSchema() {
        return null;
    }

    @Override
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
                    out.writeString(((CharSequence) datum).toString());
                    break;
                case BYTES:
                    out.writeBytes((ByteBuffer) datum);
                    break;
                case NULL:
                    out.writeNull();
                    break;
                case ENUM:
                    break;
                case ARRAY:
                    break;
                case MAP:
                    break;
                case UNION:
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
    protected void writeArray(Schema schema, String fieldName, Object datum, JsonEncoder out) throws IOException {
        ArraySchema arraySchema = (ArraySchema) schema;
        Schema element = arraySchema.getItemSchema();
        out.writeArrayStart(fieldName);
        for (Iterator iterator = getArrayElements(datum); iterator.hasNext();) {
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

    /** Called to write a nested object.*/
    protected void writeNestRecord() {

    }
}
