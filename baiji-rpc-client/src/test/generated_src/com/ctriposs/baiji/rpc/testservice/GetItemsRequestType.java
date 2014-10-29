package com.ctriposs.baiji.rpc.testservice;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class GetItemsRequestType extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"GetItemsRequestType\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"take\",\"type\":[\"int\",\"null\"]},{\"name\":\"sleep\",\"type\":[\"int\",\"null\"]},{\"name\":\"validationString\",\"type\":[\"string\",\"null\"]},{\"name\":\"generateRandomException\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"returnWrappedErrorResponse\",\"type\":[\"boolean\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public GetItemsRequestType(
        Integer take,
        Integer sleep,
        String validationString,
        Boolean generateRandomException,
        Boolean returnWrappedErrorResponse
    ) {
        this.take = take;
        this.sleep = sleep;
        this.validationString = validationString;
        this.generateRandomException = generateRandomException;
        this.returnWrappedErrorResponse = returnWrappedErrorResponse;
    }

    public GetItemsRequestType() {
    }

    public Integer take;

    public Integer sleep;

    public String validationString;

    public Boolean generateRandomException;

    public Boolean returnWrappedErrorResponse;

    public Integer getTake() {
        return take;
    }

    public void setTake(final Integer take) {
        this.take = take;
    }

    public Integer getSleep() {
        return sleep;
    }

    public void setSleep(final Integer sleep) {
        this.sleep = sleep;
    }

    public String getValidationString() {
        return validationString;
    }

    public void setValidationString(final String validationString) {
        this.validationString = validationString;
    }

    public Boolean isGenerateRandomException() {
        return generateRandomException;
    }

    public void setGenerateRandomException(final Boolean generateRandomException) {
        this.generateRandomException = generateRandomException;
    }

    public Boolean isReturnWrappedErrorResponse() {
        return returnWrappedErrorResponse;
    }

    public void setReturnWrappedErrorResponse(final Boolean returnWrappedErrorResponse) {
        this.returnWrappedErrorResponse = returnWrappedErrorResponse;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.take;
            case 1: return this.sleep;
            case 2: return this.validationString;
            case 3: return this.generateRandomException;
            case 4: return this.returnWrappedErrorResponse;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.take = (Integer)fieldValue; break;
            case 1: this.sleep = (Integer)fieldValue; break;
            case 2: this.validationString = (String)fieldValue; break;
            case 3: this.generateRandomException = (Boolean)fieldValue; break;
            case 4: this.returnWrappedErrorResponse = (Boolean)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final GetItemsRequestType other = (GetItemsRequestType)obj;
        return 
            Objects.equal(this.take, other.take) &&
            Objects.equal(this.sleep, other.sleep) &&
            Objects.equal(this.validationString, other.validationString) &&
            Objects.equal(this.generateRandomException, other.generateRandomException) &&
            Objects.equal(this.returnWrappedErrorResponse, other.returnWrappedErrorResponse);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.take == null ? 0 : this.take.hashCode());
        result = 31 * result + (this.sleep == null ? 0 : this.sleep.hashCode());
        result = 31 * result + (this.validationString == null ? 0 : this.validationString.hashCode());
        result = 31 * result + (this.generateRandomException == null ? 0 : this.generateRandomException.hashCode());
        result = 31 * result + (this.returnWrappedErrorResponse == null ? 0 : this.returnWrappedErrorResponse.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("take", take)
            .add("sleep", sleep)
            .add("validationString", validationString)
            .add("generateRandomException", generateRandomException)
            .add("returnWrappedErrorResponse", returnWrappedErrorResponse)
            .toString();
    }
}
