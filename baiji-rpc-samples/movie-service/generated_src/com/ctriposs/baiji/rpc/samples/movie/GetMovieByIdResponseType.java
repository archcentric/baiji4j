package com.ctriposs.baiji.rpc.samples.movie;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class GetMovieByIdResponseType extends SpecificRecordBase implements SpecificRecord, HasResponseStatus {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"GetMovieByIdResponseType\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.movie\",\"doc\":null,\"fields\":[{\"name\":\"responseStatus\",\"type\":[{\"type\":\"record\",\"name\":\"ResponseStatusType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"timestamp\",\"type\":[\"datetime\",\"null\"]},{\"name\":\"ack\",\"type\":[{\"type\":\"enum\",\"name\":\"AckCodeType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"symbols\":[\"SUCCESS\",\"FAILURE\",\"WARNING\",\"PARTIAL_FAILURE\"]},\"null\"]},{\"name\":\"errors\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"ErrorDataType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"message\",\"type\":[\"string\",\"null\"]},{\"name\":\"errorCode\",\"type\":[\"string\",\"null\"]},{\"name\":\"stackTrace\",\"type\":[\"string\",\"null\"]},{\"name\":\"severityCode\",\"type\":[{\"type\":\"enum\",\"name\":\"SeverityCodeType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"symbols\":[\"ERROR\",\"WARNING\"]},\"null\"]},{\"name\":\"errorFields\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"ErrorFieldType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"fieldName\",\"type\":[\"string\",\"null\"]},{\"name\":\"errorCode\",\"type\":[\"string\",\"null\"]},{\"name\":\"message\",\"type\":[\"string\",\"null\"]}]}},\"null\"]},{\"name\":\"errorClassification\",\"type\":[{\"type\":\"enum\",\"name\":\"ErrorClassificationCodeType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"symbols\":[\"SERVICE_ERROR\",\"VALIDATION_ERROR\",\"FRAMEWORK_ERROR\",\"SLAERROR\",\"SECURITY_ERROR\"]},\"null\"]}]}},\"null\"]},{\"name\":\"build\",\"type\":[\"string\",\"null\"]},{\"name\":\"version\",\"type\":[\"string\",\"null\"]},{\"name\":\"extension\",\"type\":[{\"type\":\"record\",\"name\":\"ExtensionType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"id\",\"type\":[\"int\",\"null\"]},{\"name\":\"version\",\"type\":[\"string\",\"null\"]},{\"name\":\"contentType\",\"type\":[\"string\",\"null\"]},{\"name\":\"value\",\"type\":[\"string\",\"null\"]}]},\"null\"]}]},\"null\"]},{\"name\":\"movie\",\"type\":[{\"type\":\"record\",\"name\":\"MovieDto\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.movie\",\"doc\":null,\"fields\":[{\"name\":\"id\",\"type\":[\"long\",\"null\"]},{\"name\":\"imdbId\",\"type\":[\"string\",\"null\"]},{\"name\":\"title\",\"type\":[\"string\",\"null\"]},{\"name\":\"rating\",\"type\":[\"double\",\"null\"]},{\"name\":\"director\",\"type\":[\"string\",\"null\"]},{\"name\":\"releaseDate\",\"type\":[\"string\",\"null\"]},{\"name\":\"tagLine\",\"type\":[\"string\",\"null\"]},{\"name\":\"genres\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]}]},\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public GetMovieByIdResponseType(
        com.ctriposs.baiji.rpc.common.types.ResponseStatusType responseStatus,
        MovieDto movie
    ) {
        this.responseStatus = responseStatus;
        this.movie = movie;
    }

    public GetMovieByIdResponseType() {
    }

    public com.ctriposs.baiji.rpc.common.types.ResponseStatusType responseStatus;


    /**
     * Movie with specific id
     */
    public MovieDto movie;

    public com.ctriposs.baiji.rpc.common.types.ResponseStatusType getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(final com.ctriposs.baiji.rpc.common.types.ResponseStatusType responseStatus) {
        this.responseStatus = responseStatus;
    }


    /**
     * Movie with specific id
     */
    public MovieDto getMovie() {
        return movie;
    }

    /**
     * Movie with specific id
     */
    public void setMovie(final MovieDto movie) {
        this.movie = movie;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.responseStatus;
            case 1: return this.movie;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.responseStatus = (com.ctriposs.baiji.rpc.common.types.ResponseStatusType)fieldValue; break;
            case 1: this.movie = (MovieDto)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final GetMovieByIdResponseType other = (GetMovieByIdResponseType)obj;
        return 
            Objects.equal(this.responseStatus, other.responseStatus) &&
            Objects.equal(this.movie, other.movie);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.responseStatus == null ? 0 : this.responseStatus.hashCode());
        result = 31 * result + (this.movie == null ? 0 : this.movie.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("responseStatus", responseStatus)
            .add("movie", movie)
            .toString();
    }
}
