package com.ctriposs.baiji.rpc.common.types;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

/**
 * Reserved for future use.
 */
@SuppressWarnings("all")
public class ExtensionType extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"ExtensionType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"id\",\"type\":[\"int\",\"null\"]},{\"name\":\"version\",\"type\":[\"string\",\"null\"]},{\"name\":\"contentType\",\"type\":[\"string\",\"null\"]},{\"name\":\"value\",\"type\":[\"string\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public ExtensionType(
        Integer id,
        String version,
        String contentType,
        String value
    ) {
        this.id = id;
        this.version = version;
        this.contentType = contentType;
        this.value = value;
    }

    public ExtensionType() {
    }

    /**
     * Reserved for future use.
     */
    public Integer id;


    /**
     * Reserved for future use.
     */
    public String version;


    /**
     * Reserved for future use.
     */
    public String contentType;


    /**
     * Reserved for future use.
     */
    public String value;

    /**
     * Reserved for future use.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Reserved for future use.
     */
    public void setId(final Integer id) {
        this.id = id;
    }


    /**
     * Reserved for future use.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Reserved for future use.
     */
    public void setVersion(final String version) {
        this.version = version;
    }


    /**
     * Reserved for future use.
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Reserved for future use.
     */
    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }


    /**
     * Reserved for future use.
     */
    public String getValue() {
        return value;
    }

    /**
     * Reserved for future use.
     */
    public void setValue(final String value) {
        this.value = value;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.id;
            case 1: return this.version;
            case 2: return this.contentType;
            case 3: return this.value;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.id = (Integer)fieldValue; break;
            case 1: this.version = (String)fieldValue; break;
            case 2: this.contentType = (String)fieldValue; break;
            case 3: this.value = (String)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final ExtensionType other = (ExtensionType)obj;
        return 
            Objects.equal(this.id, other.id) &&
            Objects.equal(this.version, other.version) &&
            Objects.equal(this.contentType, other.contentType) &&
            Objects.equal(this.value, other.value);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.id == null ? 0 : this.id.hashCode());
        result = 31 * result + (this.version == null ? 0 : this.version.hashCode());
        result = 31 * result + (this.contentType == null ? 0 : this.contentType.hashCode());
        result = 31 * result + (this.value == null ? 0 : this.value.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("id", id)
            .add("version", version)
            .add("contentType", contentType)
            .add("value", value)
            .toString();
    }
}
