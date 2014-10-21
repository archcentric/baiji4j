package com.ctriposs.baiji.fastjson;

import com.ctriposs.baiji.exception.BaijiRuntimeException;
import com.ctriposs.baiji.schema.Schema;
import com.ctriposs.baiji.specific.SpecificRecord;
import com.ctriposs.baiji.specific.SpecificRecordBase;
import com.google.common.base.Objects;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TestRecord extends SpecificRecordBase implements SpecificRecord {

    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"TestRecord\",\"namespace\":\"com.ctriposs.baiji.fastjson\",\"doc\":null,\"fields\":[{\"name\":\"flag\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"num1\",\"type\":[\"int\",\"null\"]},{\"name\":\"num2\",\"type\":[\"long\",\"null\"]},{\"name\":\"realNum1\",\"type\":[\"float\",\"null\"]},{\"name\":\"realNum2\",\"type\":[\"double\",\"null\"]},{\"name\":\"record\",\"type\":[{\"type\":\"record\",\"name\":\"InnerRecord\",\"namespace\":\"com.ctriposs.baiji.specific\",\"doc\":null,\"fields\":[{\"name\":\"id\",\"type\":[\"int\",\"null\"]},{\"name\":\"name\",\"type\":[\"string\",\"null\"]}]},\"null\"]},{\"name\":\"parent\",\"type\":[\"TestRecord\",\"null\"]},{\"name\":\"nums\",\"type\":[{\"type\":\"array\",\"items\":\"int\"},\"null\"]},{\"name\":\"names\",\"type\":[{\"type\":\"map\",\"values\":\"string\"},\"null\"]},{\"name\":\"data\",\"type\":[\"bytes\",\"null\"]}]}");

    @Override
    public Schema getSchema() {
        return SCHEMA;
    }

    public TestRecord(
            Boolean flag,
            Integer num1,
            Long num2,
            Float realNum1,
            Double realNum2,
            InnerRecord record,
            TestRecord parent,
            List<Integer> nums,
            Map<String, String> names,
            byte[] data
    ) {
        this.flag = flag;
        this.num1 = num1;
        this.num2 = num2;
        this.realNum1 = realNum1;
        this.realNum2 = realNum2;
        this.record = record;
        this.parent = parent;
        this.nums = nums;
        this.names = names;
        this.data = data;
    }

    public TestRecord() {
    }

    public Boolean flag;

    public Integer num1;

    public Long num2;

    public Float realNum1;

    public Double realNum2;

    public InnerRecord record;

    public TestRecord parent;

    public List<Integer> nums;

    public Map<String, String> names;

    private byte[] data;

    public Boolean isFlag() {
        return flag;
    }

    public void setFlag(final Boolean flag) {
        this.flag = flag;
    }

    public Integer getNum1() {
        return num1;
    }

    public void setNum1(final Integer num1) {
        this.num1 = num1;
    }

    public Long getNum2() {
        return num2;
    }

    public void setNum2(final Long num2) {
        this.num2 = num2;
    }

    public Float getRealNum1() {
        return realNum1;
    }

    public void setRealNum2(final Float realNum1) {
        this.realNum1 = realNum1;
    }

    public Double getRealNum2() {
        return realNum2;
    }

    public void setRealNum2(final Double realNum2) {
        this.realNum2 = realNum2;
    }

    public InnerRecord getRecord() {
        return record;
    }

    public void setRecord(final InnerRecord record) {
        this.record = record;
    }

    public TestRecord getParent() {
        return parent;
    }

    public void setParent(final TestRecord parent) {
        this.parent = parent;
    }

    public List<Integer> getNums() {
        return nums;
    }

    public void setNums(final List<Integer> nums) {
        this.nums = nums;
    }

    public Map<String, String> getNames() {
        return names;
    }

    public void setNames(final Map<String, String> names) {
        this.names = names;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(final byte[] data) {
        this.data = data;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0:
                return this.flag;
            case 1:
                return this.num1;
            case 2:
                return this.num2;
            case 3:
                return this.realNum1;
            case 4:
                return this.realNum2;
            case 5:
                return this.record;
            case 6:
                return this.parent;
            case 7:
                return this.nums;
            case 8:
                return this.names;
            case 9:
                return this.data;
            default:
                throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value = "unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0:
                this.flag = (Boolean) fieldValue;
                break;
            case 1:
                this.num1 = (Integer) fieldValue;
                break;
            case 2:
                this.num2 = (Long) fieldValue;
                break;
            case 3:
                this.realNum1 = (Float) fieldValue;
                break;
            case 4:
                this.realNum2 = (Double) fieldValue;
                break;
            case 5:
                this.record = (InnerRecord) fieldValue;
                break;
            case 6:
                this.parent = (TestRecord) fieldValue;
                break;
            case 7:
                this.nums = (List<Integer>) fieldValue;
                break;
            case 8:
                this.names = (Map<String, String>) fieldValue;
                break;
            case 9:
                this.data = (byte[]) fieldValue;
                break;
            default:
                throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final TestRecord other = (TestRecord) obj;
        return
                Objects.equal(this.flag, other.flag) &&
                        Objects.equal(this.num1, other.num1) &&
                        Objects.equal(this.num2, other.num2) &&
                        Objects.equal(this.realNum1, other.realNum1) &&
                        Objects.equal(this.realNum2, other.realNum2) &&
                        Objects.equal(this.record, other.record) &&
                        Objects.equal(this.parent, other.parent) &&
                        Objects.equal(this.nums, other.nums) &&
                        Objects.equal(this.names, other.names) &&
                        Arrays.equals(this.data, other.data);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (this.flag == null ? 0 : this.flag.hashCode());
        result = 31 * result + (this.num1 == null ? 0 : this.num1.hashCode());
        result = 31 * result + (this.num2 == null ? 0 : this.num2.hashCode());
        result = 31 * result + (this.realNum1 == null ? 0 : this.realNum1.hashCode());
        result = 31 * result + (this.realNum2 == null ? 0 : this.realNum2.hashCode());
        result = 31 * result + (this.record == null ? 0 : this.record.hashCode());
        result = 31 * result + (this.parent == null ? 0 : this.parent.hashCode());
        result = 31 * result + (this.nums == null ? 0 : this.nums.hashCode());
        result = 31 * result + (this.names == null ? 0 : this.names.hashCode());
        result = 31 * result + (this.data == null ? 0 : Arrays.hashCode(data));
        return result;
    }
}
