package com.ctriposs.baiji.rpc.testservice;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class Amount extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"Amount\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"currencyId\",\"type\":[\"string\",\"null\"]},{\"name\":\"value\",\"type\":[\"double\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public Amount(
        String currencyId,
        Double value
    ) {
        this.currencyId = currencyId;
        this.value = value;
    }

    public Amount() {
    }

    public String currencyId;

    public Double value;

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(final String currencyId) {
        this.currencyId = currencyId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(final Double value) {
        this.value = value;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.currencyId;
            case 1: return this.value;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.currencyId = (String)fieldValue; break;
            case 1: this.value = (Double)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final Amount other = (Amount)obj;
        return 
            Objects.equal(this.currencyId, other.currencyId) &&
            Objects.equal(this.value, other.value);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.currencyId == null ? 0 : this.currencyId.hashCode());
        result = 31 * result + (this.value == null ? 0 : this.value.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("currencyId", currencyId)
            .add("value", value)
            .toString();
    }
}
