package com.ctriposs.baiji.rpc.samples.movie;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class FindMoviesByGenreRequestType extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"FindMoviesByGenreRequestType\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.movie\",\"doc\":null,\"fields\":[{\"name\":\"genre\",\"type\":[\"string\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public FindMoviesByGenreRequestType(
        String genre
    ) {
        this.genre = genre;
    }

    public FindMoviesByGenreRequestType() {
    }

    /**
     * Genre to search
     */
    public String genre;

    /**
     * Genre to search
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Genre to search
     */
    public void setGenre(final String genre) {
        this.genre = genre;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.genre;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.genre = (String)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final FindMoviesByGenreRequestType other = (FindMoviesByGenreRequestType)obj;
        return 
            Objects.equal(this.genre, other.genre);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.genre == null ? 0 : this.genre.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("genre", genre)
            .toString();
    }
}
