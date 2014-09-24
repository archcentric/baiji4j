package com.ctriposs.baiji.rpc.testservice;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class Condition extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"Condition\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"conditionId\",\"type\":[\"int\",\"null\"]},{\"name\":\"conditionDisplayName\",\"type\":[\"string\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public Condition(
        Integer conditionId,
        String conditionDisplayName
    ) {
        this.conditionId = conditionId;
        this.conditionDisplayName = conditionDisplayName;
    }

    public Condition() {
    }

    public Integer conditionId;

    public String conditionDisplayName;

    public Integer getConditionId() {
        return conditionId;
    }

    public void setConditionId(final Integer conditionId) {
        this.conditionId = conditionId;
    }

    public String getConditionDisplayName() {
        return conditionDisplayName;
    }

    public void setConditionDisplayName(final String conditionDisplayName) {
        this.conditionDisplayName = conditionDisplayName;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.conditionId;
            case 1: return this.conditionDisplayName;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.conditionId = (Integer)fieldValue; break;
            case 1: this.conditionDisplayName = (String)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final Condition other = (Condition)obj;
        return 
            Objects.equal(this.conditionId, other.conditionId) &&
            Objects.equal(this.conditionDisplayName, other.conditionDisplayName);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.conditionId == null ? 0 : this.conditionId.hashCode());
        result = 31 * result + (this.conditionDisplayName == null ? 0 : this.conditionDisplayName.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("conditionId", conditionId)
            .add("conditionDisplayName", conditionDisplayName)
            .toString();
    }
}
