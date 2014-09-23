package com.ctriposs.baiji.rpc.samples.movie;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class GetMoviesRequestType extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"GetMoviesRequestType\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.movie\",\"doc\":null,\"fields\":[{\"name\":\"count\",\"type\":[\"int\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public GetMoviesRequestType(
        Integer count
    ) {
        this.count = count;
    }

    public GetMoviesRequestType() {
    }

    public Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(final Integer count) {
        this.count = count;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.count;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.count = (Integer)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final GetMoviesRequestType other = (GetMoviesRequestType)obj;
        return 
            Objects.equal(this.count, other.count);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.count == null ? 0 : this.count.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("count", count)
            .toString();
    }
}
