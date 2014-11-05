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
    @JsonIgnore
    private static Map<String, GenericBenchmarkRecord> recordMap = new HashMap<>();
    @JsonIgnore
    private Schema schema;

    public GenericBenchmarkRecord(String recordType) {
        String s = "{\"type\":\"record\",\"name\":\"GenericBenchmarkRecord\",\"namespace\":\"com.ctriposs.baiji.generic\","
                + "\"fields\":[{\"name\":\"fieldValue\",\"type\": " + recordType + "}]}";

        schema = schemaMap.get(recordType);
        if (schema == null) {
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
        GenericBenchmarkRecord benchmarkRecord = recordMap.get(recordType);

        if (benchmarkRecord == null) {
            switch (recordType) {
                case "int":
                    benchmarkRecord = new GenericIntBenchmarkRecord();
                    break;
            }
        }

        return benchmarkRecord;
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

    private static class GenericIntBenchmarkRecord extends GenericBenchmarkRecord {

        public GenericIntBenchmarkRecord() {
            super("int");
        }
    }
}
