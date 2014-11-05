package com.ctriposs.baiji.generic;


import com.ctriposs.baiji.schema.Schema;
import com.ctriposs.baiji.specific.SpecificRecordBase;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public abstract class GenericBenchmarkRecord extends SpecificRecordBase {

    private Object fieldValue;

    @JsonIgnore
    private static Map<String, Schema> schemaMap = new HashMap<>();

    protected static final Map<String, Class<? extends GenericBenchmarkRecord>> recordClasses = new HashMap<>();
    @JsonIgnore
    private Schema schema;

    static {
        recordClasses.put("\"int\"", GenericIntBenchmarkRecord.class);
        recordClasses.put("\"boolean\"", GenericBooleanBenchmarkRecord.class);
    }

    public GenericBenchmarkRecord(String recordType) {
        schema = schemaMap.get(recordType);

        if (schema == null) {
            String s = "{\"type\":\"record\",\"name\":\"" + recordClasses.get(recordType).getSimpleName() + "\",\"namespace\":\"com.ctriposs.baiji.generic\","
                    + "\"fields\":[{\"name\":\"fieldValue\",\"type\":" + recordType + "}]}";
            schema = Schema.parse(s);
            schemaMap.put(recordType, schema);
        }
    }

    @Override
    @JsonIgnore
    public Schema getSchema() {
        return schema;
    }

    @JsonIgnore
    public static GenericBenchmarkRecord getBenchmarkRecord(String recordType) {
        Class<? extends GenericBenchmarkRecord> clazz = recordClasses.get(recordType);

        try {
            return clazz.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }

    @Override
    public Object get(int fieldPos) {
        return fieldValue;
    }

    @Override
    public void put(int fieldPos, Object fieldValue) {
        this.fieldValue = fieldValue;
    }

    /*public static class GenericIntBenchmarkRecord extends GenericBenchmarkRecord {

        public GenericIntBenchmarkRecord() {
            super("\"int\"");
        }
    }*/
}
