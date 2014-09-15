package com.ctriposs.baiji.generic;


import com.ctriposs.baiji.schema.Schema;
import com.ctriposs.baiji.specific.SpecificRecordBase;

public class GenericBenchmarkRecord extends SpecificRecordBase {

    private Schema _schema;
    private Object fieldValue;

    public static String recordType;

    public GenericBenchmarkRecord() {
        _schema = generateSchema(recordType);
    }

    @Override
    public Schema getSchema() {
        return _schema;
    }

    @Override
    public Object get(int fieldPos) {
        return fieldValue;
    }

    @Override
    public void put(int fieldPos, Object fieldValue) {
        this.fieldValue = fieldValue;
    }

    private Schema generateSchema(String recordType) {
        String s = "{\"type\":\"record\",\"name\":\"BenchmarkRecord\",\"namespace\":\"com.ctriposs.baiji\","
                + "\"fields\":[{\"name\":\"fieldName\",\"type\": " + recordType + "}]}";
        return Schema.parse(s);
    }
}
