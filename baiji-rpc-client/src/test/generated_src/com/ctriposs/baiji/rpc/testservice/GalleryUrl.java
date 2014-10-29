package com.ctriposs.baiji.rpc.testservice;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class GalleryUrl extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"GalleryUrl\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"gallerySize\",\"type\":[{\"type\":\"enum\",\"name\":\"GallerySize\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"symbols\":[\"SMALL\",\"MEDIUM\",\"LARGE\"]},\"null\"]},{\"name\":\"value\",\"type\":[\"string\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public GalleryUrl(
        GallerySize gallerySize,
        String value
    ) {
        this.gallerySize = gallerySize;
        this.value = value;
    }

    public GalleryUrl() {
    }

    public GallerySize gallerySize;

    public String value;

    public GallerySize getGallerySize() {
        return gallerySize;
    }

    public void setGallerySize(final GallerySize gallerySize) {
        this.gallerySize = gallerySize;
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
            case 0: return this.gallerySize;
            case 1: return this.value;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.gallerySize = (GallerySize)fieldValue; break;
            case 1: this.value = (String)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final GalleryUrl other = (GalleryUrl)obj;
        return 
            Objects.equal(this.gallerySize, other.gallerySize) &&
            Objects.equal(this.value, other.value);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.gallerySize == null ? 0 : this.gallerySize.hashCode());
        result = 31 * result + (this.value == null ? 0 : this.value.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("gallerySize", gallerySize)
            .add("value", value)
            .toString();
    }
}
