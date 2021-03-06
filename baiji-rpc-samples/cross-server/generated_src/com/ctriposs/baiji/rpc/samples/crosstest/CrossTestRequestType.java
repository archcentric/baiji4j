package com.ctriposs.baiji.rpc.samples.crosstest;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class CrossTestRequestType extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"CrossTestRequestType\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.crosstest\",\"doc\":null,\"fields\":[{\"name\":\"name\",\"type\":[\"string\",\"null\"]},{\"name\":\"sample\",\"type\":[{\"type\":\"record\",\"name\":\"TestSerializerSample\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.crosstest\",\"doc\":null,\"fields\":[{\"name\":\"int1\",\"type\":[\"int\",\"null\"]},{\"name\":\"tinyint1\",\"type\":[\"int\",\"null\"]},{\"name\":\"smallint1\",\"type\":[\"int\",\"null\"]},{\"name\":\"bigint1\",\"type\":[\"long\",\"null\"]},{\"name\":\"boolean1\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"double1\",\"type\":[\"double\",\"null\"]},{\"name\":\"string1\",\"type\":[\"string\",\"null\"]},{\"name\":\"record\",\"type\":[{\"type\":\"record\",\"name\":\"Record\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.crosstest\",\"doc\":null,\"fields\":[{\"name\":\"sInt\",\"type\":[\"int\",\"null\"]},{\"name\":\"sBoolean\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"sString\",\"type\":[\"string\",\"null\"]}]},\"null\"]},{\"name\":\"list1\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"map1\",\"type\":[{\"type\":\"map\",\"values\":\"int\"},\"null\"]},{\"name\":\"enum1\",\"type\":[{\"type\":\"enum\",\"name\":\"Enum1Values\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.crosstest\",\"doc\":null,\"symbols\":[\"BLUE\",\"RED\",\"GREEN\"]},\"null\"]},{\"name\":\"nullableint\",\"type\":[\"int\",\"null\"]},{\"name\":\"bytes1\",\"type\":[\"bytes\",\"null\"]},{\"name\":\"container1\",\"type\":[{\"type\":\"record\",\"name\":\"Record2Container\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.crosstest\",\"doc\":null,\"fields\":[{\"name\":\"record2list\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"Record2\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.crosstest\",\"doc\":null,\"fields\":[{\"name\":\"enum2\",\"type\":[{\"type\":\"enum\",\"name\":\"Enum2Values\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.crosstest\",\"doc\":null,\"symbols\":[\"CAR\",\"BIKE\",\"PLANE\"]},\"null\"]},{\"name\":\"bigint2\",\"type\":[\"long\",\"null\"]},{\"name\":\"nullablebigint\",\"type\":[\"long\",\"null\"]},{\"name\":\"list2\",\"type\":[{\"type\":\"array\",\"items\":\"int\"},\"null\"]},{\"name\":\"map2\",\"type\":[{\"type\":\"map\",\"values\":\"Record\"},\"null\"]},{\"name\":\"byteslist\",\"type\":[{\"type\":\"array\",\"items\":\"bytes\"},\"null\"]},{\"name\":\"filling\",\"type\":[{\"type\":\"record\",\"name\":\"ModelFilling\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.crosstest\",\"doc\":null,\"fields\":[{\"name\":\"stringfilling1\",\"type\":[\"string\",\"null\"]},{\"name\":\"stringfilling2\",\"type\":[\"string\",\"null\"]},{\"name\":\"stringfilling3\",\"type\":[\"string\",\"null\"]},{\"name\":\"stringfilling4\",\"type\":[\"string\",\"null\"]},{\"name\":\"intfilling\",\"type\":[\"int\",\"null\"]},{\"name\":\"boolfilling\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"modelfilling\",\"type\":[{\"type\":\"record\",\"name\":\"ModelFilling2\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.crosstest\",\"doc\":null,\"fields\":[{\"name\":\"longfilling\",\"type\":[\"long\",\"null\"]},{\"name\":\"stringfilling\",\"type\":[\"string\",\"null\"]},{\"name\":\"listfilling\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"enumfilling\",\"type\":[\"Enum2Values\",\"null\"]}]},\"null\"]},{\"name\":\"modelfilling3\",\"type\":[{\"type\":\"record\",\"name\":\"ModelFilling3\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.crosstest\",\"doc\":null,\"fields\":[{\"name\":\"intfilling\",\"type\":[\"int\",\"null\"]},{\"name\":\"doublefilling\",\"type\":[\"double\",\"null\"]}]},\"null\"]},{\"name\":\"enumfilling\",\"type\":[\"Enum1Values\",\"null\"]}]},\"null\"]}]}},\"null\"]}]},\"null\"]},{\"name\":\"innerSample\",\"type\":[\"TestSerializerSample\",\"null\"]}]},\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public CrossTestRequestType(
        String name,
        TestSerializerSample sample
    ) {
        this.name = name;
        this.sample = sample;
    }

    public CrossTestRequestType() {
    }

    public String name;

    public TestSerializerSample sample;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public TestSerializerSample getSample() {
        return sample;
    }

    public void setSample(final TestSerializerSample sample) {
        this.sample = sample;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.name;
            case 1: return this.sample;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.name = (String)fieldValue; break;
            case 1: this.sample = (TestSerializerSample)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final CrossTestRequestType other = (CrossTestRequestType)obj;
        return 
            Objects.equal(this.name, other.name) &&
            Objects.equal(this.sample, other.sample);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
        result = 31 * result + (this.sample == null ? 0 : this.sample.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("name", name)
            .add("sample", sample)
            .toString();
    }
}
