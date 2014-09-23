package com.ctriposs.baiji.rpc.common.types;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

/**
 * Response status type definition of a response payload.
 * Per Baiji SOA policy, all concrete response types must include this response status type
 * as a root element with element name 'ResponseStatus'.
 * This is required for unified response status/error handling at framework level.
 * The recommended naming convention we use for the concrete type names is the name of
 * the service (the verb or call name) followed by "ResponseType": VerbNameResponseType
 */
@SuppressWarnings("all")
public class ResponseStatusType extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"ResponseStatusType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"timestamp\",\"type\":[\"datetime\",\"null\"]},{\"name\":\"ack\",\"type\":[{\"type\":\"enum\",\"name\":\"AckCodeType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"symbols\":[\"SUCCESS\",\"FAILURE\",\"WARNING\",\"PARTIAL_FAILURE\"]},\"null\"]},{\"name\":\"errors\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"ErrorDataType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"message\",\"type\":[\"string\",\"null\"]},{\"name\":\"errorCode\",\"type\":[\"string\",\"null\"]},{\"name\":\"stackTrace\",\"type\":[\"string\",\"null\"]},{\"name\":\"severityCode\",\"type\":[{\"type\":\"enum\",\"name\":\"SeverityCodeType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"symbols\":[\"ERROR\",\"WARNING\"]},\"null\"]},{\"name\":\"errorFields\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"ErrorFieldType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"fieldName\",\"type\":[\"string\",\"null\"]},{\"name\":\"errorCode\",\"type\":[\"string\",\"null\"]},{\"name\":\"message\",\"type\":[\"string\",\"null\"]}]}},\"null\"]},{\"name\":\"errorClassification\",\"type\":[{\"type\":\"enum\",\"name\":\"ErrorClassificationCodeType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"symbols\":[\"SERVICE_ERROR\",\"VALIDATION_ERROR\",\"FRAMEWORK_ERROR\",\"SLAERROR\",\"SECURITY_ERROR\"]},\"null\"]}]}},\"null\"]},{\"name\":\"build\",\"type\":[\"string\",\"null\"]},{\"name\":\"version\",\"type\":[\"string\",\"null\"]},{\"name\":\"extension\",\"type\":[{\"type\":\"record\",\"name\":\"ExtensionType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"id\",\"type\":[\"int\",\"null\"]},{\"name\":\"version\",\"type\":[\"string\",\"null\"]},{\"name\":\"contentType\",\"type\":[\"string\",\"null\"]},{\"name\":\"value\",\"type\":[\"string\",\"null\"]}]},\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public ResponseStatusType(
        java.util.Calendar timestamp,
        AckCodeType ack,
        List<ErrorDataType> errors,
        String build,
        String version,
        ExtensionType extension
    ) {
        this.timestamp = timestamp;
        this.ack = ack;
        this.errors = errors;
        this.build = build;
        this.version = version;
        this.extension = extension;
    }

    public ResponseStatusType() {
    }

    /**
     * This value represents the date and time when a Baiji service processed the request.
     * The value of this element is set by framework automatically, value set by service implementation will be overwritten.
     */
    public java.util.Calendar timestamp;


    /**
     * Indicates whether the call was successfully processed by Baiji.
     */
    public AckCodeType ack;


    /**
     * A list of framework, validation or service-level errors or warnings (if any)
     * that were raised when a Baiji service processed the request.
     * Only returned if there were warnings or errors.
     */
    public List<ErrorDataType> errors;


    /**
     * This refers to the particular software build that Baiji service used
     * when processing the request and generating the response.
     * This includes the version number plus additional information.
     */
    public String build;


    /**
     * The version of service used to process the request.
     */
    public String version;


    /**
     * Reserved for future extension.
     */
    public ExtensionType extension;

    /**
     * This value represents the date and time when a Baiji service processed the request.
     * The value of this element is set by framework automatically, value set by service implementation will be overwritten.
     */
    public java.util.Calendar getTimestamp() {
        return timestamp;
    }

    /**
     * This value represents the date and time when a Baiji service processed the request.
     * The value of this element is set by framework automatically, value set by service implementation will be overwritten.
     */
    public void setTimestamp(final java.util.Calendar timestamp) {
        this.timestamp = timestamp;
    }


    /**
     * Indicates whether the call was successfully processed by Baiji.
     */
    public AckCodeType getAck() {
        return ack;
    }

    /**
     * Indicates whether the call was successfully processed by Baiji.
     */
    public void setAck(final AckCodeType ack) {
        this.ack = ack;
    }


    /**
     * A list of framework, validation or service-level errors or warnings (if any)
     * that were raised when a Baiji service processed the request.
     * Only returned if there were warnings or errors.
     */
    public List<ErrorDataType> getErrors() {
        return errors;
    }

    /**
     * A list of framework, validation or service-level errors or warnings (if any)
     * that were raised when a Baiji service processed the request.
     * Only returned if there were warnings or errors.
     */
    public void setErrors(final List<ErrorDataType> errors) {
        this.errors = errors;
    }


    /**
     * This refers to the particular software build that Baiji service used
     * when processing the request and generating the response.
     * This includes the version number plus additional information.
     */
    public String getBuild() {
        return build;
    }

    /**
     * This refers to the particular software build that Baiji service used
     * when processing the request and generating the response.
     * This includes the version number plus additional information.
     */
    public void setBuild(final String build) {
        this.build = build;
    }


    /**
     * The version of service used to process the request.
     */
    public String getVersion() {
        return version;
    }

    /**
     * The version of service used to process the request.
     */
    public void setVersion(final String version) {
        this.version = version;
    }


    /**
     * Reserved for future extension.
     */
    public ExtensionType getExtension() {
        return extension;
    }

    /**
     * Reserved for future extension.
     */
    public void setExtension(final ExtensionType extension) {
        this.extension = extension;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.timestamp;
            case 1: return this.ack;
            case 2: return this.errors;
            case 3: return this.build;
            case 4: return this.version;
            case 5: return this.extension;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.timestamp = (java.util.Calendar)fieldValue; break;
            case 1: this.ack = (AckCodeType)fieldValue; break;
            case 2: this.errors = (List<ErrorDataType>)fieldValue; break;
            case 3: this.build = (String)fieldValue; break;
            case 4: this.version = (String)fieldValue; break;
            case 5: this.extension = (ExtensionType)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final ResponseStatusType other = (ResponseStatusType)obj;
        return 
            Objects.equal(this.timestamp, other.timestamp) &&
            Objects.equal(this.ack, other.ack) &&
            Objects.equal(this.errors, other.errors) &&
            Objects.equal(this.build, other.build) &&
            Objects.equal(this.version, other.version) &&
            Objects.equal(this.extension, other.extension);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.timestamp == null ? 0 : this.timestamp.hashCode());
        result = 31 * result + (this.ack == null ? 0 : this.ack.hashCode());
        result = 31 * result + (this.errors == null ? 0 : this.errors.hashCode());
        result = 31 * result + (this.build == null ? 0 : this.build.hashCode());
        result = 31 * result + (this.version == null ? 0 : this.version.hashCode());
        result = 31 * result + (this.extension == null ? 0 : this.extension.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("timestamp", timestamp)
            .add("ack", ack)
            .add("errors", errors)
            .add("build", build)
            .add("version", version)
            .add("extension", extension)
            .toString();
    }
}
