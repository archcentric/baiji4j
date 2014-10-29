package com.ctriposs.baiji.rpc.testservice;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class StoreFront extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"StoreFront\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"storeName\",\"type\":[\"string\",\"null\"]},{\"name\":\"storeUrl\",\"type\":[\"string\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public StoreFront(
        String storeName,
        String storeUrl
    ) {
        this.storeName = storeName;
        this.storeUrl = storeUrl;
    }

    public StoreFront() {
    }

    public String storeName;

    public String storeUrl;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(final String storeName) {
        this.storeName = storeName;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(final String storeUrl) {
        this.storeUrl = storeUrl;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.storeName;
            case 1: return this.storeUrl;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.storeName = (String)fieldValue; break;
            case 1: this.storeUrl = (String)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final StoreFront other = (StoreFront)obj;
        return 
            Objects.equal(this.storeName, other.storeName) &&
            Objects.equal(this.storeUrl, other.storeUrl);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.storeName == null ? 0 : this.storeName.hashCode());
        result = 31 * result + (this.storeUrl == null ? 0 : this.storeUrl.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("storeName", storeName)
            .add("storeUrl", storeUrl)
            .toString();
    }
}
