package com.ctriposs.baiji.rpc.mobile.common.types;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

/**
 * Simplest H5 request type without request data except H5 request head.
 */
@SuppressWarnings("all")
public class SimpleH5RequestType extends SpecificRecordBase implements SpecificRecord, HasMobileRequestHead {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"SimpleH5RequestType\",\"namespace\":\"com.ctriposs.baiji.rpc.mobile.common.types\",\"doc\":null,\"fields\":[{\"name\":\"head\",\"type\":[{\"type\":\"record\",\"name\":\"MobileRequestHead\",\"namespace\":\"com.ctriposs.baiji.rpc.mobile.common.types\",\"doc\":null,\"fields\":[{\"name\":\"syscode\",\"type\":[\"string\",\"null\"]},{\"name\":\"lang\",\"type\":[\"string\",\"null\"]},{\"name\":\"auth\",\"type\":[\"string\",\"null\"]},{\"name\":\"cid\",\"type\":[\"string\",\"null\"]},{\"name\":\"ctok\",\"type\":[\"string\",\"null\"]},{\"name\":\"cver\",\"type\":[\"string\",\"null\"]},{\"name\":\"sid\",\"type\":[\"string\",\"null\"]},{\"name\":\"extension\",\"type\":[{\"type\":\"record\",\"name\":\"ExtensionFieldType\",\"namespace\":\"com.ctriposs.baiji.rpc.mobile.common.types\",\"doc\":null,\"fields\":[{\"name\":\"name\",\"type\":[\"string\",\"null\"]},{\"name\":\"value\",\"type\":[\"string\",\"null\"]}]},\"null\"]}]},\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public SimpleH5RequestType(
        MobileRequestHead head
    ) {
        this.head = head;
    }

    public SimpleH5RequestType() {
    }

    public MobileRequestHead head;

    public MobileRequestHead getHead() {
        return head;
    }

    public void setHead(final MobileRequestHead head) {
        this.head = head;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.head;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.head = (MobileRequestHead)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final SimpleH5RequestType other = (SimpleH5RequestType)obj;
        return 
            Objects.equal(this.head, other.head);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.head == null ? 0 : this.head.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("head", head)
            .toString();
    }
}
