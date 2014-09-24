package com.ctriposs.baiji.rpc.testservice;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class UnitPriceInfo extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"UnitPriceInfo\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"type\",\"type\":[\"string\",\"null\"]},{\"name\":\"quantity\",\"type\":[\"double\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public UnitPriceInfo(
        String type,
        Double quantity
    ) {
        this.type = type;
        this.quantity = quantity;
    }

    public UnitPriceInfo() {
    }

    public String type;

    public Double quantity;

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(final Double quantity) {
        this.quantity = quantity;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.type;
            case 1: return this.quantity;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.type = (String)fieldValue; break;
            case 1: this.quantity = (Double)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final UnitPriceInfo other = (UnitPriceInfo)obj;
        return 
            Objects.equal(this.type, other.type) &&
            Objects.equal(this.quantity, other.quantity);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.type == null ? 0 : this.type.hashCode());
        result = 31 * result + (this.quantity == null ? 0 : this.quantity.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("type", type)
            .add("quantity", quantity)
            .toString();
    }
}
