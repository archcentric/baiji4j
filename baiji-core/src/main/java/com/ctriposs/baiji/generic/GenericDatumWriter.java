package com.ctriposs.baiji.generic;

import com.ctriposs.baiji.exception.BaijiRuntimeException;
import com.ctriposs.baiji.io.Encoder;
import com.ctriposs.baiji.schema.EnumSchema;
import com.ctriposs.baiji.schema.RecordSchema;
import com.ctriposs.baiji.schema.Schema;
import com.ctriposs.baiji.schema.SchemaType;

import java.io.IOException;
import java.util.Map;

/**
 * {@link DatumWriter} for generic Java objects.
 */
public class GenericDatumWriter<D> extends PreresolvingDatumWriter<D> {
    public GenericDatumWriter(Schema schema) {
        super(schema, new GenericArrayAccess(), new DefaultMapAccess());
    }

    @Override
    protected void writeRecordFields(Object recordObj, RecordFieldWriter[] writers, Encoder encoder)
            throws IOException {
        GenericRecord record = (GenericRecord) recordObj;
        for (RecordFieldWriter writer : writers) {
            writer._fieldWriter.write(record.get(writer._field.getName()), encoder);
        }
    }

    @Override
    protected void ensureRecordObject(RecordSchema recordSchema, Object value) {
        if (value == null || !(value instanceof GenericRecord)
                || !(((GenericRecord) value).getSchema().equals(recordSchema))) {
            throw typeMismatch(value, "record", "GenericRecord");
        }
    }

    @Override
    protected void writeField(Object record, String fieldName, int fieldPos, ItemWriter writer,
                              Encoder encoder) throws IOException {
        writer.write(((GenericRecord) record).get(fieldName), encoder);
    }

    @Override
    protected ItemWriter resolveEnum(EnumSchema es) {
        return new EnumItemWriter(es);
    }

    private static class EnumItemWriter implements ItemWriter {
        private final EnumSchema _schema;

        public EnumItemWriter(EnumSchema schema) {
            _schema = schema;
        }

        @Override
        public void write(Object value, Encoder encoder) throws IOException {
            if (value == null || !(value instanceof GenericEnum)
                    || !(((GenericEnum) value).getSchema().equals(_schema))) {
                throw typeMismatch(value, "enum", "GenericEnum");
            }
            encoder.writeEnum(_schema.ordinal(((GenericEnum) value).getValue()));
        }
    }

    /*
   * TODO: This method of determining the Union branch has problems. If the data is Map<String, Object>
   * if there are two branches one with record schema and the other with map, it choose the first one. Similarly if
   * the data is byte[] and there are fixed and bytes schemas as branches, it choose the first one that matches.
   * Also it does not recognize the arrays of primitive types.
   */
    @Override
    protected boolean unionBranchMatches(Schema sc, Object obj) {
        if (obj == null && sc.getType() != SchemaType.NULL) {
            return false;
        }
        switch (sc.getType()) {
            case NULL:
                return obj == null;
            case BOOLEAN:
                return obj instanceof Boolean;
            case INT:
                return obj instanceof Integer;
            case LONG:
                return obj instanceof Long;
            case FLOAT:
                return obj instanceof Float;
            case DOUBLE:
                return obj instanceof Double;
            case BYTES:
                return obj instanceof byte[];
            case STRING:
                return obj instanceof String;
            case RECORD:
                return obj instanceof GenericRecord
                        && ((GenericRecord) obj).getSchema().getSchemaName().equals(((RecordSchema) sc).getSchemaName());
            case ENUM:
                return obj instanceof GenericEnum
                        && ((GenericEnum) obj).getSchema().getSchemaName().equals(((EnumSchema) sc).getSchemaName());
            case ARRAY:
                return obj.getClass().isArray() && !(obj instanceof byte[]);
            case MAP:
                return obj instanceof Map<?, ?>;
            case UNION:
                return false;   // Union directly within another union not allowed!
            default:
                throw new BaijiRuntimeException("Unknown schema type: " + sc.getType());
        }
    }

    private static class GenericArrayAccess implements ArrayAccess {
        @Override
        public void ensureArrayObject(Object value) {
            if (value == null || !(value.getClass().isArray())) {
                throw typeMismatch(value, "array", "Array");
            }
        }

        @Override
        public long getArrayLength(Object value) {
            return ((Object[]) value).length;
        }

        public void writeArrayValues(Object array, ItemWriter valueWriter, Encoder encoder)
                throws IOException {
            Object[] arrayInstance = (Object[]) array;
            for (int i = 0; i < arrayInstance.length; i++) {
                encoder.startItem();
                valueWriter.write(arrayInstance[i], encoder);
            }
        }
    }
}

