package com.ctriposs.baiji.rpc.common.types;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

/**
 * A variable that contains specific information about the context of this error.
 * For example, in request validation failure case, the problematic field name might be returned as an error field.
 * Use error fields to flag fields that users need to correct.
 * Also use error fields to distinguish between errors when multiple errors are returned.
 */
@SuppressWarnings("all")
public class ErrorFieldType extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"ErrorFieldType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"fieldName\",\"type\":[\"string\",\"null\"]},{\"name\":\"errorCode\",\"type\":[\"string\",\"null\"]},{\"name\":\"message\",\"type\":[\"string\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public ErrorFieldType(
        String fieldName,
        String errorCode,
        String message
    ) {
        this.fieldName = fieldName;
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorFieldType() {
    }

    /**
     * The name of the field caused the error.
     */
    public String fieldName;


    /**
     * Error code
     */
    public String errorCode;


    /**
     * Error message
     */
    public String message;

    /**
     * The name of the field caused the error.
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * The name of the field caused the error.
     */
    public void setFieldName(final String fieldName) {
        this.fieldName = fieldName;
    }


    /**
     * Error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Error code
     */
    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }


    /**
     * Error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Error message
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.fieldName;
            case 1: return this.errorCode;
            case 2: return this.message;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.fieldName = (String)fieldValue; break;
            case 1: this.errorCode = (String)fieldValue; break;
            case 2: this.message = (String)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final ErrorFieldType other = (ErrorFieldType)obj;
        return 
            Objects.equal(this.fieldName, other.fieldName) &&
            Objects.equal(this.errorCode, other.errorCode) &&
            Objects.equal(this.message, other.message);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.fieldName == null ? 0 : this.fieldName.hashCode());
        result = 31 * result + (this.errorCode == null ? 0 : this.errorCode.hashCode());
        result = 31 * result + (this.message == null ? 0 : this.message.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("fieldName", fieldName)
            .add("errorCode", errorCode)
            .add("message", message)
            .toString();
    }
}
