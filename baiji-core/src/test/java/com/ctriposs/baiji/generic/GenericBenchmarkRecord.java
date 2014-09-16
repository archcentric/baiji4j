package com.ctriposs.baiji.generic;


import com.ctriposs.baiji.schema.Schema;
import com.ctriposs.baiji.specific.SpecificRecordBase;

public class GenericBenchmarkRecord extends SpecificRecordBase {

    private Object fieldValue;

    public static String recordType;

    @Override
    public Schema getSchema() {
        String s = "{\"type\":\"record\",\"name\":\"GenericBenchmarkRecord\",\"namespace\":\"com.ctriposs.baiji.generic\","
                + "\"fields\":[{\"name\":\"fieldName\",\"type\": " + recordType + "}]}";
        return Schema.parse(s);
    }

    @Override
    public Object get(int fieldPos) {
        return fieldValue;
    }

    @Override
    public void put(int fieldPos, Object fieldValue) {
        this.fieldValue = fieldValue;
    }
}
