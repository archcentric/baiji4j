package com.ctriposs.baiji.rpc.mobile.common.types;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class ExtensionFieldType extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"ExtensionFieldType\",\"namespace\":\"com.ctriposs.baiji.rpc.mobile.common.types\",\"doc\":null,\"fields\":[{\"name\":\"name\",\"type\":[\"string\",\"null\"]},{\"name\":\"value\",\"type\":[\"string\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public ExtensionFieldType(
        String name,
        String value
    ) {
        this.name = name;
        this.value = value;
    }

    public ExtensionFieldType() {
    }

    public String name;

    public String value;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.name;
            case 1: return this.value;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.name = (String)fieldValue; break;
            case 1: this.value = (String)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final ExtensionFieldType other = (ExtensionFieldType)obj;
        return 
            Objects.equal(this.name, other.name) &&
            Objects.equal(this.value, other.value);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
        result = 31 * result + (this.value == null ? 0 : this.value.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("name", name)
            .add("value", value)
            .toString();
    }
}
