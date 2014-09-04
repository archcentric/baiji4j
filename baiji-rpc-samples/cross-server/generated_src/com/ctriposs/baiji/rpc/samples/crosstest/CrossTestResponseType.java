package com.ctriposs.baiji.rpc.samples.crosstest;

import com.ctriposs.baiji.rpc.common.HasResponseStatus;
import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class CrossTestResponseType extends SpecificRecordBase implements SpecificRecord, HasResponseStatus {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"CrossTestResponseType\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.crosstest\",\"doc\":null,\"fields\":[{\"name\":\"responseStatus\",\"type\":[{\"type\":\"record\",\"name\":\"ResponseStatusType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"timestamp\",\"type\":[\"string\",\"null\"]},{\"name\":\"ack\",\"type\":[{\"type\":\"enum\",\"name\":\"AckCodeType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"symbols\":[\"SUCCESS\",\"FAILURE\",\"WARNING\",\"PARTIAL_FAILURE\"]},\"null\"]},{\"name\":\"errors\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"ErrorDataType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"message\",\"type\":[\"string\",\"null\"]},{\"name\":\"errorCode\",\"type\":[\"string\",\"null\"]},{\"name\":\"stackTrace\",\"type\":[\"string\",\"null\"]},{\"name\":\"severityCode\",\"type\":[{\"type\":\"enum\",\"name\":\"SeverityCodeType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"symbols\":[\"ERROR\",\"WARNING\"]},\"null\"]},{\"name\":\"errorFields\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"ErrorFieldType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"fieldName\",\"type\":[\"string\",\"null\"]},{\"name\":\"errorCode\",\"type\":[\"string\",\"null\"]},{\"name\":\"message\",\"type\":[\"string\",\"null\"]}]}},\"null\"]},{\"name\":\"errorClassification\",\"type\":[{\"type\":\"enum\",\"name\":\"ErrorClassificationCodeType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"symbols\":[\"SERVICE_ERROR\",\"VALIDATION_ERROR\",\"FRAMEWORK_ERROR\",\"SLAERROR\",\"SECURITY_ERROR\"]},\"null\"]}]}},\"null\"]},{\"name\":\"build\",\"type\":[\"string\",\"null\"]},{\"name\":\"version\",\"type\":[\"string\",\"null\"]},{\"name\":\"extension\",\"type\":[{\"type\":\"record\",\"name\":\"ExtensionType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"id\",\"type\":[\"int\",\"null\"]},{\"name\":\"version\",\"type\":[\"string\",\"null\"]},{\"name\":\"contentType\",\"type\":[\"string\",\"null\"]},{\"name\":\"value\",\"type\":[\"string\",\"null\"]}]},\"null\"]}]},\"null\"]},{\"name\":\"sampleList\",\"type\":[{\"type\":\"record\",\"name\":\"TestSerializerSampleList\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.crosstest\",\"doc\":null,\"fields\":[{\"name\":\"samples\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"TestSerializerSample\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.crosstest\",\"doc\":null,\"fields\":[{\"name\":\"int1\",\"type\":[\"int\",\"null\"]},{\"name\":\"tinyint1\",\"type\":[\"int\",\"null\"]},{\"name\":\"smallint1\",\"type\":[\"int\",\"null\"]},{\"name\":\"bigint1\",\"type\":[\"long\",\"null\"]},{\"name\":\"boolean1\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"double1\",\"type\":[\"double\",\"null\"]},{\"name\":\"string1\",\"type\":[\"string\",\"null\"]},{\"name\":\"record\",\"type\":[{\"type\":\"record\",\"name\":\"Record\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.crosstest\",\"doc\":null,\"fields\":[{\"name\":\"sInt\",\"type\":[\"int\",\"null\"]},{\"name\":\"sBoolean\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"sString\",\"type\":[\"string\",\"null\"]}]},\"null\"]},{\"name\":\"list1\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"map1\",\"type\":[{\"type\":\"map\",\"values\":\"int\"},\"null\"]},{\"name\":\"enum1\",\"type\":[{\"type\":\"enum\",\"name\":\"Enum1Values\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.crosstest\",\"doc\":null,\"symbols\":[\"BLUE\",\"RED\",\"GREEN\"]},\"null\"]},{\"name\":\"nullableint\",\"type\":[\"int\",\"null\"]},{\"name\":\"bytes1\",\"type\":[\"bytes\",\"null\"]},{\"name\":\"container1\",\"type\":[{\"type\":\"record\",\"name\":\"Record2Container\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.crosstest\",\"doc\":null,\"fields\":[{\"name\":\"record2list\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"Record2\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.crosstest\",\"doc\":null,\"fields\":[{\"name\":\"enum2\",\"type\":[{\"type\":\"enum\",\"name\":\"Enum2Values\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.crosstest\",\"doc\":null,\"symbols\":[\"CAR\",\"BIKE\",\"PLANE\"]},\"null\"]},{\"name\":\"bigint2\",\"type\":[\"long\",\"null\"]},{\"name\":\"nullablebigint\",\"type\":[\"long\",\"null\"]},{\"name\":\"list2\",\"type\":[{\"type\":\"array\",\"items\":\"int\"},\"null\"]},{\"name\":\"map2\",\"type\":[{\"type\":\"map\",\"values\":\"Record\"},\"null\"]},{\"name\":\"byteslist\",\"type\":[{\"type\":\"array\",\"items\":\"bytes\"},\"null\"]},{\"name\":\"filling\",\"type\":[{\"type\":\"record\",\"name\":\"ModelFilling\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.crosstest\",\"doc\":null,\"fields\":[{\"name\":\"stringfilling1\",\"type\":[\"string\",\"null\"]},{\"name\":\"stringfilling2\",\"type\":[\"string\",\"null\"]},{\"name\":\"stringfilling3\",\"type\":[\"string\",\"null\"]},{\"name\":\"stringfilling4\",\"type\":[\"string\",\"null\"]},{\"name\":\"intfilling\",\"type\":[\"int\",\"null\"]},{\"name\":\"boolfilling\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"modelfilling\",\"type\":[{\"type\":\"record\",\"name\":\"ModelFilling2\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.crosstest\",\"doc\":null,\"fields\":[{\"name\":\"longfilling\",\"type\":[\"long\",\"null\"]},{\"name\":\"stringfilling\",\"type\":[\"string\",\"null\"]},{\"name\":\"listfilling\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"enumfilling\",\"type\":[\"Enum2Values\",\"null\"]}]},\"null\"]},{\"name\":\"modelfilling3\",\"type\":[{\"type\":\"record\",\"name\":\"ModelFilling3\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.crosstest\",\"doc\":null,\"fields\":[{\"name\":\"intfilling\",\"type\":[\"int\",\"null\"]},{\"name\":\"doublefilling\",\"type\":[\"double\",\"null\"]},{\"name\":\"listsfilling\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":\"int\"}},\"null\"]},{\"name\":\"mapsfilling\",\"type\":[{\"type\":\"map\",\"values\":{\"type\":\"map\",\"values\":\"string\"}},\"null\"]}]},\"null\"]},{\"name\":\"enumfilling\",\"type\":[\"Enum1Values\",\"null\"]}]},\"null\"]}]}},\"null\"]}]},\"null\"]},{\"name\":\"innerSample\",\"type\":[\"TestSerializerSample\",\"null\"]}]}},\"null\"]}]},\"null\"]},{\"name\":\"message\",\"type\":[\"string\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public CrossTestResponseType(
        com.ctriposs.baiji.rpc.common.types.ResponseStatusType responseStatus,
        TestSerializerSampleList sampleList,
        String message
    ) {
        this.responseStatus = responseStatus;
        this.sampleList = sampleList;
        this.message = message;
    }

    public CrossTestResponseType() {
    }

    public com.ctriposs.baiji.rpc.common.types.ResponseStatusType responseStatus;

    public TestSerializerSampleList sampleList;

    public String message;

    public com.ctriposs.baiji.rpc.common.types.ResponseStatusType getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(final com.ctriposs.baiji.rpc.common.types.ResponseStatusType responseStatus) {
        this.responseStatus = responseStatus;
    }

    public TestSerializerSampleList getSampleList() {
        return sampleList;
    }

    public void setSampleList(final TestSerializerSampleList sampleList) {
        this.sampleList = sampleList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    // Used by DatumWriter. Applications should not call.
    public Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.responseStatus;
            case 1: return this.sampleList;
            case 2: return this.message;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, Object fieldValue) {
        switch (fieldPos) {
            case 0: this.responseStatus = (com.ctriposs.baiji.rpc.common.types.ResponseStatusType)fieldValue; break;
            case 1: this.sampleList = (TestSerializerSampleList)fieldValue; break;
            case 2: this.message = (String)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final CrossTestResponseType other = (CrossTestResponseType)obj;
        return 
            Objects.equal(this.responseStatus, other.responseStatus) &&
            Objects.equal(this.sampleList, other.sampleList) &&
            Objects.equal(this.message, other.message);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.responseStatus == null ? 0 : this.responseStatus.hashCode());
        result = 31 * result + (this.sampleList == null ? 0 : this.sampleList.hashCode());
        result = 31 * result + (this.message == null ? 0 : this.message.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("responseStatus", responseStatus)
            .add("sampleList", sampleList)
            .add("message", message)
            .toString();
    }
}
