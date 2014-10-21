package com.ctriposs.baiji.fastjson;

import com.ctriposs.baiji.exception.BaijiRuntimeException;
import com.ctriposs.baiji.specific.SpecificRecordBase;

public class EnumRecord extends SpecificRecordBase {

    public static final com.ctriposs.baiji.schema.Schema SCHEMA = com.ctriposs.baiji.schema.Schema.parse(
            "{\"type\":\"record\",\"name\":\"EnumRecord\",\"namespace\":\"com.ctriposs.baiji.fastjson\"," +
                    "\"fields\":[{\"name\":\"enumType\",\"type\": { \"type\": \"enum\", \"name\":" +
                    " \"EnumType\", \"symbols\": [\"THIRD\", \"FIRST\", \"SECOND\"]} }]}");
    public EnumType enumType;

    public EnumRecord() {
    }

    public com.ctriposs.baiji.schema.Schema getSchema() {
        return SCHEMA;
    }

    public EnumType getEnumType() {
        return enumType;
    }

    public void setEnumType(EnumType enumType) {
        this.enumType = enumType;
    }

    @Override
    public Object get(int fieldPos) {
        switch (fieldPos) {
            case 0:
                return enumType;
            default:
                throw new BaijiRuntimeException("Bad index " + fieldPos + " in Get()");
        }
    }

    @Override
    public void put(int fieldPos, Object fieldValue) {
        switch (fieldPos) {
            case 0:
                enumType = (EnumType) fieldValue;
                break;
            default:
                throw new BaijiRuntimeException("Bad index " + fieldPos + " in Put()");
        }
    }
}
