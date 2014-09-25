package com.ctriposs.baiji.rpc.samples.math;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class GetFactorialRequestType extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"GetFactorialRequestType\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.math\",\"doc\":null,\"fields\":[{\"name\":\"number\",\"type\":[\"long\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public GetFactorialRequestType(
        Long number
    ) {
        this.number = number;
    }

    public GetFactorialRequestType() {
    }

    public Long number;

    public Long getNumber() {
        return number;
    }

    public void setNumber(final Long number) {
        this.number = number;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.number;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.number = (Long)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final GetFactorialRequestType other = (GetFactorialRequestType)obj;
        return 
            Objects.equal(this.number, other.number);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.number == null ? 0 : this.number.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("number", number)
            .toString();
    }
}
