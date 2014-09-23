package com.ctriposs.baiji.rpc.samples.hello;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class HelloRequestType extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"HelloRequestType\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.hello\",\"doc\":null,\"fields\":[{\"name\":\"name\",\"type\":[\"string\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public HelloRequestType(
        String name
    ) {
        this.name = name;
    }

    public HelloRequestType() {
    }

    public String name;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.name;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.name = (String)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final HelloRequestType other = (HelloRequestType)obj;
        return 
            Objects.equal(this.name, other.name);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.name == null ? 0 : this.name.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("name", name)
            .toString();
    }
}
