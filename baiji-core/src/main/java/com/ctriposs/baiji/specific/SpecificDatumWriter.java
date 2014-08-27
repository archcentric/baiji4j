package com.ctriposs.baiji.specific;

import com.ctriposs.baiji.exception.BaijiRuntimeException;
import com.ctriposs.baiji.exception.BaijiTypeException;
import com.ctriposs.baiji.generic.PreresolvingDatumWriter;
import com.ctriposs.baiji.io.Encoder;
import com.ctriposs.baiji.schema.EnumSchema;
import com.ctriposs.baiji.schema.RecordSchema;
import com.ctriposs.baiji.schema.Schema;
import com.ctriposs.baiji.schema.SchemaType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link com.ctriposs.baiji.generic.DatumWriter DatumWriter} for generated Java classes.
 */
public class SpecificDatumWriter<T> extends PreresolvingDatumWriter<T> {

    public SpecificDatumWriter(Schema schema) {
        super(schema, new SpecificArrayAccess(), new DefaultMapAccess());
    }

    @Override
    protected void writeRecordFields(Object recordObj, RecordFieldWriter[] writers, Encoder encoder) throws IOException {
        SpecificRecord record = (SpecificRecord) recordObj;
        for (int i = 0; i < writers.length; i++) {
            RecordFieldWriter writer = writers[i];
            writer._fieldWriter.write(record.get(writer._field.getPos()), encoder);
        }
    }

    @Override
    protected void ensureRecordObject(RecordSchema recordSchema, Object value) {
        if (!(value instanceof SpecificRecord)) {
            throw new BaijiTypeException("Record object is not derived from ISpecificRecord");
        }
    }

    @Override
    protected void writeField(Object record, String fieldName, int fieldPos, ItemWriter writer,
                              Encoder encoder) throws IOException {
        writer.write(((SpecificRecord) record).get(fieldPos), encoder);
    }

    @Override
    protected ItemWriter resolveEnum(EnumSchema es) {
        Class<?> clazz = ObjectCreator.INSTANCE.getClass(es);
        Object[] enumValues = clazz.getEnumConstants();
        Map<Object, Integer> translator = new HashMap<Object, Integer>(enumValues.length);
        for (int i = 0; i < enumValues.length; i++) {
            Object value = enumValues[i];
            String symbol = value.toString();
            if (es.contains(symbol)) {
                translator.put(value, es.ordinal(symbol));
            }
        }

        return new EnumItemWriter(es, clazz, translator);
    }

    private static class EnumItemWriter implements ItemWriter {
        private final EnumSchema _schema;
        private final Class<?> _clazz;
        private final Map<Object, Integer> _translator;

        public EnumItemWriter(EnumSchema schema, Class<?> clazz, Map<Object, Integer> translator) {
            _schema = schema;
            _clazz = clazz;
            _translator = translator;
        }

        @Override
        public void write(Object value, Encoder encoder) throws IOException {
            if (value == null) {
                throw new BaijiTypeException("value is null in SpecificDatumWriter.EnumItemWriter.write");
            }
            if (value.getClass() == _clazz) {
                Integer translated = _translator.get(value);
                if (translated == null) {
                    throw new BaijiTypeException("Unknown enum value:" + value.toString());
                }
                encoder.writeEnum(translated);
            } else {
                encoder.writeEnum(_schema.ordinal(value.toString()));
            }
        }
    }

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
                return obj instanceof SpecificRecord &&
                        (((RecordSchema) ((SpecificRecord) obj).getSchema())).getSchemaName().equals(
                                ((RecordSchema) (sc)).getSchemaName());
            case ENUM:
                return obj.getClass().isEnum() && ((EnumSchema) sc).getSymbols().contains(obj.toString());
            case ARRAY:
                return obj instanceof List;
            case MAP:
                return obj instanceof Map;
            case UNION:
                return false; // Union directly within another union not allowed!
            default:
                throw new BaijiRuntimeException("Unknown schema type: " + sc.getType());
        }
    }

    private static class SpecificArrayAccess implements ArrayAccess {
        @Override
        public void ensureArrayObject(Object value) {
            if (!(value instanceof List)) {
                throw new BaijiTypeException("Array does not implement non-generic IList");
            }
        }

        @Override
        public long getArrayLength(Object value) {
            return ((List) value).size();
        }

        @Override
        public void writeArrayValues(Object array, ItemWriter valueWriter, Encoder encoder)
                throws IOException {
            List list = (List) array;
            for (int i = 0; i < list.size(); i++) {
                valueWriter.write(list.get(i), encoder);
            }
        }
    }
}

