package com.ctriposs.baiji.rpc.common.types;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

/**
 * This is service, validation or framework-level error.
 */
@SuppressWarnings("all")
public class ErrorDataType extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"ErrorDataType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"message\",\"type\":[\"string\",\"null\"]},{\"name\":\"errorCode\",\"type\":[\"string\",\"null\"]},{\"name\":\"stackTrace\",\"type\":[\"string\",\"null\"]},{\"name\":\"severityCode\",\"type\":[{\"type\":\"enum\",\"name\":\"SeverityCodeType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"symbols\":[\"ERROR\",\"WARNING\"]},\"null\"]},{\"name\":\"errorFields\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"ErrorFieldType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"fieldName\",\"type\":[\"string\",\"null\"]},{\"name\":\"errorCode\",\"type\":[\"string\",\"null\"]},{\"name\":\"message\",\"type\":[\"string\",\"null\"]}]}},\"null\"]},{\"name\":\"errorClassification\",\"type\":[{\"type\":\"enum\",\"name\":\"ErrorClassificationCodeType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"symbols\":[\"SERVICE_ERROR\",\"VALIDATION_ERROR\",\"FRAMEWORK_ERROR\",\"SLAERROR\",\"SECURITY_ERROR\"]},\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public ErrorDataType(
        String message,
        String errorCode,
        String stackTrace,
        SeverityCodeType severityCode,
        List<ErrorFieldType> errorFields,
        ErrorClassificationCodeType errorClassification
    ) {
        this.message = message;
        this.errorCode = errorCode;
        this.stackTrace = stackTrace;
        this.severityCode = severityCode;
        this.errorFields = errorFields;
        this.errorClassification = errorClassification;
    }

    public ErrorDataType() {
    }

    /**
     * A brief description of the condition that raised the error.
     */
    public String message;


    /**
     * A unique code that identifies the particular error condition that occurred.
     */
    public String errorCode;


    /**
     * StackTrace of exception causing this error, only used in debug mode.
     */
    public String stackTrace;


    /**
     * Indicates whether the reported problem is fatal (an error) or is less- severe (a warning). Review the error message details for information on the cause.
     */
    public SeverityCodeType severityCode;


    /**
     * Some warning and error messages return one or more variables that contain contextual information about the error. This is often the field or value that triggered the error.
     */
    public List<ErrorFieldType> errorFields;


    /**
     * API errors are divided between three classes: service errors, validation errors and framework errors.
     */
    public ErrorClassificationCodeType errorClassification;

    /**
     * A brief description of the condition that raised the error.
     */
    public String getMessage() {
        return message;
    }

    /**
     * A brief description of the condition that raised the error.
     */
    public void setMessage(final String message) {
        this.message = message;
    }


    /**
     * A unique code that identifies the particular error condition that occurred.
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * A unique code that identifies the particular error condition that occurred.
     */
    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }


    /**
     * StackTrace of exception causing this error, only used in debug mode.
     */
    public String getStackTrace() {
        return stackTrace;
    }

    /**
     * StackTrace of exception causing this error, only used in debug mode.
     */
    public void setStackTrace(final String stackTrace) {
        this.stackTrace = stackTrace;
    }


    /**
     * Indicates whether the reported problem is fatal (an error) or is less- severe (a warning). Review the error message details for information on the cause.
     */
    public SeverityCodeType getSeverityCode() {
        return severityCode;
    }

    /**
     * Indicates whether the reported problem is fatal (an error) or is less- severe (a warning). Review the error message details for information on the cause.
     */
    public void setSeverityCode(final SeverityCodeType severityCode) {
        this.severityCode = severityCode;
    }


    /**
     * Some warning and error messages return one or more variables that contain contextual information about the error. This is often the field or value that triggered the error.
     */
    public List<ErrorFieldType> getErrorFields() {
        return errorFields;
    }

    /**
     * Some warning and error messages return one or more variables that contain contextual information about the error. This is often the field or value that triggered the error.
     */
    public void setErrorFields(final List<ErrorFieldType> errorFields) {
        this.errorFields = errorFields;
    }


    /**
     * API errors are divided between three classes: service errors, validation errors and framework errors.
     */
    public ErrorClassificationCodeType getErrorClassification() {
        return errorClassification;
    }

    /**
     * API errors are divided between three classes: service errors, validation errors and framework errors.
     */
    public void setErrorClassification(final ErrorClassificationCodeType errorClassification) {
        this.errorClassification = errorClassification;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.message;
            case 1: return this.errorCode;
            case 2: return this.stackTrace;
            case 3: return this.severityCode;
            case 4: return this.errorFields;
            case 5: return this.errorClassification;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.message = (String)fieldValue; break;
            case 1: this.errorCode = (String)fieldValue; break;
            case 2: this.stackTrace = (String)fieldValue; break;
            case 3: this.severityCode = (SeverityCodeType)fieldValue; break;
            case 4: this.errorFields = (List<ErrorFieldType>)fieldValue; break;
            case 5: this.errorClassification = (ErrorClassificationCodeType)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final ErrorDataType other = (ErrorDataType)obj;
        return 
            Objects.equal(this.message, other.message) &&
            Objects.equal(this.errorCode, other.errorCode) &&
            Objects.equal(this.stackTrace, other.stackTrace) &&
            Objects.equal(this.severityCode, other.severityCode) &&
            Objects.equal(this.errorFields, other.errorFields) &&
            Objects.equal(this.errorClassification, other.errorClassification);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.message == null ? 0 : this.message.hashCode());
        result = 31 * result + (this.errorCode == null ? 0 : this.errorCode.hashCode());
        result = 31 * result + (this.stackTrace == null ? 0 : this.stackTrace.hashCode());
        result = 31 * result + (this.severityCode == null ? 0 : this.severityCode.hashCode());
        result = 31 * result + (this.errorFields == null ? 0 : this.errorFields.hashCode());
        result = 31 * result + (this.errorClassification == null ? 0 : this.errorClassification.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("message", message)
            .add("errorCode", errorCode)
            .add("stackTrace", stackTrace)
            .add("severityCode", severityCode)
            .add("errorFields", errorFields)
            .add("errorClassification", errorClassification)
            .toString();
    }
}
