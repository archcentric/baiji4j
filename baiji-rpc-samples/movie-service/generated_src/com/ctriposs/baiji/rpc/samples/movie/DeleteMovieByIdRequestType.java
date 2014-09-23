package com.ctriposs.baiji.rpc.samples.movie;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class DeleteMovieByIdRequestType extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"DeleteMovieByIdRequestType\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.movie\",\"doc\":null,\"fields\":[{\"name\":\"id\",\"type\":[\"long\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public DeleteMovieByIdRequestType(
        Long id
    ) {
        this.id = id;
    }

    public DeleteMovieByIdRequestType() {
    }

    /**
     * The identifier of the movie to delete
     */
    public Long id;

    /**
     * The identifier of the movie to delete
     */
    public Long getId() {
        return id;
    }

    /**
     * The identifier of the movie to delete
     */
    public void setId(final Long id) {
        this.id = id;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.id;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.id = (Long)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final DeleteMovieByIdRequestType other = (DeleteMovieByIdRequestType)obj;
        return 
            Objects.equal(this.id, other.id);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.id == null ? 0 : this.id.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("id", id)
            .toString();
    }
}
