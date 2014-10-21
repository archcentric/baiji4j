package com.ctriposs.baiji.fastjson;

import com.ctriposs.baiji.exception.BaijiRuntimeException;
import com.ctriposs.baiji.schema.Schema;
import com.ctriposs.baiji.specific.SpecificRecord;
import com.ctriposs.baiji.specific.SpecificRecordBase;
import com.google.common.base.Objects;

public class InnerRecord extends SpecificRecordBase implements SpecificRecord {

    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"InnerRecord\",\"namespace\":\"com.ctriposs.baiji.fastjson\",\"doc\":null,\"fields\":[{\"name\":\"id\",\"type\":[\"int\",\"null\"]},{\"name\":\"name\",\"type\":[\"string\",\"null\"]}]}");

    @Override
    public Schema getSchema() {
        return SCHEMA;
    }

    public InnerRecord(
            Integer id,
            String name
    ) {
        this.id = id;
        this.name = name;
    }

    public InnerRecord() {
    }

    public Integer id;

    public String name;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0:
                return this.id;
            case 1:
                return this.name;
            default:
                throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value = "unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0:
                this.id = (Integer) fieldValue;
                break;
            case 1:
                this.name = (String) fieldValue;
                break;
            default:
                throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final InnerRecord other = (InnerRecord) obj;
        return
                Objects.equal(this.id, other.id) &&
                        Objects.equal(this.name, other.name);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (this.id == null ? 0 : this.id.hashCode());
        result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
        return result;
    }
}
