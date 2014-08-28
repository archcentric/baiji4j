package com.ctriposs.baiji.rpc.samples.movie;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class MovieDto extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"MovieDto\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.movie\",\"doc\":null,\"fields\":[{\"name\":\"id\",\"type\":[\"long\",\"null\"]},{\"name\":\"imdbId\",\"type\":[\"string\",\"null\"]},{\"name\":\"title\",\"type\":[\"string\",\"null\"]},{\"name\":\"rating\",\"type\":[\"double\",\"null\"]},{\"name\":\"director\",\"type\":[\"string\",\"null\"]},{\"name\":\"releaseDate\",\"type\":[\"string\",\"null\"]},{\"name\":\"tagLine\",\"type\":[\"string\",\"null\"]},{\"name\":\"genres\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public MovieDto(
        Long id,
        String imdbId,
        String title,
        Double rating,
        String director,
        String releaseDate,
        String tagLine,
        List<String> genres
    ) {
        this.id = id;
        this.imdbId = imdbId;
        this.title = title;
        this.rating = rating;
        this.director = director;
        this.releaseDate = releaseDate;
        this.tagLine = tagLine;
        this.genres = genres;
    }

    public MovieDto() {
    }

    public Long id;

    public String imdbId;

    public String title;

    public Double rating;

    public String director;

    public String releaseDate;

    public String tagLine;

    public List<String> genres;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(final String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(final Double rating) {
        this.rating = rating;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(final String director) {
        this.director = director;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(final String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(final String tagLine) {
        this.tagLine = tagLine;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(final List<String> genres) {
        this.genres = genres;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.id;
            case 1: return this.imdbId;
            case 2: return this.title;
            case 3: return this.rating;
            case 4: return this.director;
            case 5: return this.releaseDate;
            case 6: return this.tagLine;
            case 7: return this.genres;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.id = (Long)fieldValue; break;
            case 1: this.imdbId = (String)fieldValue; break;
            case 2: this.title = (String)fieldValue; break;
            case 3: this.rating = (Double)fieldValue; break;
            case 4: this.director = (String)fieldValue; break;
            case 5: this.releaseDate = (String)fieldValue; break;
            case 6: this.tagLine = (String)fieldValue; break;
            case 7: this.genres = (List<String>)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final MovieDto other = (MovieDto)obj;
        return 
            Objects.equal(this.id, other.id) &&
            Objects.equal(this.imdbId, other.imdbId) &&
            Objects.equal(this.title, other.title) &&
            Objects.equal(this.rating, other.rating) &&
            Objects.equal(this.director, other.director) &&
            Objects.equal(this.releaseDate, other.releaseDate) &&
            Objects.equal(this.tagLine, other.tagLine) &&
            Objects.equal(this.genres, other.genres);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.id == null ? 0 : this.id.hashCode());
        result = 31 * result + (this.imdbId == null ? 0 : this.imdbId.hashCode());
        result = 31 * result + (this.title == null ? 0 : this.title.hashCode());
        result = 31 * result + (this.rating == null ? 0 : this.rating.hashCode());
        result = 31 * result + (this.director == null ? 0 : this.director.hashCode());
        result = 31 * result + (this.releaseDate == null ? 0 : this.releaseDate.hashCode());
        result = 31 * result + (this.tagLine == null ? 0 : this.tagLine.hashCode());
        result = 31 * result + (this.genres == null ? 0 : this.genres.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("id", id)
            .add("imdbId", imdbId)
            .add("title", title)
            .add("rating", rating)
            .add("director", director)
            .add("releaseDate", releaseDate)
            .add("tagLine", tagLine)
            .add("genres", genres)
            .toString();
    }
}
