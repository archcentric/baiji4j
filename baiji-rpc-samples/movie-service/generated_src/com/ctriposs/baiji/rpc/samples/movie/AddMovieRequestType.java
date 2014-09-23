package com.ctriposs.baiji.rpc.samples.movie;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class AddMovieRequestType extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"AddMovieRequestType\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.movie\",\"doc\":null,\"fields\":[{\"name\":\"movie\",\"type\":[{\"type\":\"record\",\"name\":\"MovieDto\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.movie\",\"doc\":null,\"fields\":[{\"name\":\"id\",\"type\":[\"long\",\"null\"]},{\"name\":\"imdbId\",\"type\":[\"string\",\"null\"]},{\"name\":\"title\",\"type\":[\"string\",\"null\"]},{\"name\":\"rating\",\"type\":[\"double\",\"null\"]},{\"name\":\"director\",\"type\":[\"string\",\"null\"]},{\"name\":\"releaseDate\",\"type\":[\"string\",\"null\"]},{\"name\":\"tagLine\",\"type\":[\"string\",\"null\"]},{\"name\":\"genres\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]}]},\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public AddMovieRequestType(
        MovieDto movie
    ) {
        this.movie = movie;
    }

    public AddMovieRequestType() {
    }

    /**
     * The movie to be updated
     */
    public MovieDto movie;

    /**
     * The movie to be updated
     */
    public MovieDto getMovie() {
        return movie;
    }

    /**
     * The movie to be updated
     */
    public void setMovie(final MovieDto movie) {
        this.movie = movie;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.movie;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.movie = (MovieDto)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final AddMovieRequestType other = (AddMovieRequestType)obj;
        return 
            Objects.equal(this.movie, other.movie);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.movie == null ? 0 : this.movie.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("movie", movie)
            .toString();
    }
}
