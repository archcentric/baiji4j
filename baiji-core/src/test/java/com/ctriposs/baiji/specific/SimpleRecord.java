package com.ctriposs.baiji.specific;

import com.ctriposs.baiji.schema.Schema;
import org.codehaus.jackson.annotate.JsonIgnore;

public class SimpleRecord extends SpecificRecordBase {

    @JsonIgnore
    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"SimpleRecord\",\"namespace\":\"com.ctriposs.baiji.specific\",\"doc\":null,\"fields\":[{\"name\":\"sInt\",\"type\":[\"int\",\"null\"]}]}");

    @Override
    @JsonIgnore
    public Schema getSchema() { return SCHEMA; }

    private Integer sInt;

    public Integer getSInt() {
        return sInt;
    }

    public void setSInt(final Integer sInt) {
        this.sInt = sInt;
    }

    // Used by DatumWriter. Applications should not call.
    public Object get(int fieldPos) {
        return sInt;
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, Object fieldValue) {
        this.sInt = (Integer) fieldValue;
    }
}
