package com.ctriposs.baiji.specific;

import com.ctriposs.baiji.exception.BaijiRuntimeException;
import com.ctriposs.baiji.schema.Schema;
import com.google.common.base.Objects;

public class SimpleRecord extends SpecificRecordBase implements SpecificRecord {

    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"Record\",\"namespace\":\"com.ctriposs.baiji.specific\",\"doc\":null,\"fields\":[{\"name\":\"sInt\",\"type\":[\"int\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public SimpleRecord(
            Integer sInt
    ) {
        this.sInt = sInt;
    }

    public SimpleRecord() {
    }

    public Integer sInt;

    public Integer getSInt() {
        return sInt;
    }

    public void setSInt(final Integer sInt) {
        this.sInt = sInt;
    }

    // Used by DatumWriter. Applications should not call.
    public Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.sInt;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, Object fieldValue) {
        switch (fieldPos) {
            case 0: this.sInt = (Integer)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final Record other = (Record)obj;
        return
                Objects.equal(this.sInt, other.sInt);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.sInt == null ? 0 : this.sInt.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("sInt", sInt)
                .toString();
    }
}
