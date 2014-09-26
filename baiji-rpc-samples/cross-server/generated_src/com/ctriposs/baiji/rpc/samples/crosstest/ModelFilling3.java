package com.ctriposs.baiji.rpc.samples.crosstest;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class ModelFilling3 extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"ModelFilling3\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.crosstest\",\"doc\":null,\"fields\":[{\"name\":\"intfilling\",\"type\":[\"int\",\"null\"]},{\"name\":\"doublefilling\",\"type\":[\"double\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public ModelFilling3(
        Integer intfilling,
        Double doublefilling
    ) {
        this.intfilling = intfilling;
        this.doublefilling = doublefilling;
    }

    public ModelFilling3() {
    }

    public Integer intfilling;

    public Double doublefilling;

    public Integer getIntfilling() {
        return intfilling;
    }

    public void setIntfilling(final Integer intfilling) {
        this.intfilling = intfilling;
    }

    public Double getDoublefilling() {
        return doublefilling;
    }

    public void setDoublefilling(final Double doublefilling) {
        this.doublefilling = doublefilling;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.intfilling;
            case 1: return this.doublefilling;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.intfilling = (Integer)fieldValue; break;
            case 1: this.doublefilling = (Double)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final ModelFilling3 other = (ModelFilling3)obj;
        return 
            Objects.equal(this.intfilling, other.intfilling) &&
            Objects.equal(this.doublefilling, other.doublefilling);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.intfilling == null ? 0 : this.intfilling.hashCode());
        result = 31 * result + (this.doublefilling == null ? 0 : this.doublefilling.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("intfilling", intfilling)
            .add("doublefilling", doublefilling)
            .toString();
    }
}
