package com.ctriposs.baiji.rpc.common.types;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

/**
 * Common type definition of the request payload, concrete request types may choose to include
 * this common type for optional versioning and output selecting requirements. The recommended
 * naming convention we use for the concrete type names is the name of the service (the verb or call name)
 * followed by "RequestType": VerbNameRequestType
 */
@SuppressWarnings("all")
public class CommonRequestType extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"CommonRequestType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"version\",\"type\":[\"string\",\"null\"]},{\"name\":\"outputSelector\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public CommonRequestType(
        String version,
        List<String> outputSelector
    ) {
        this.version = version;
        this.outputSelector = outputSelector;
    }

    public CommonRequestType() {
    }

    /**
     * The version number of the API code that you are programming against (e.g., 1.2.0).
     * If not set, defaults to latest version. Whether and how this field is leveraged depends on specific service.
     */
    public String version;


    /**
     * You can use the OutputSelector field to restrict the data returned by this call.
     * When you make a call such as GetItem that retrieves data from baiji service,
     * the OutputSelector field is useful for restricting the data returned.
     * This field makes the call response easier to use, especially when a large payload would be returned.
     * If you use the OutputSelector field, the output data will include only the fields you specified in the request.
     * For example, if you are using GetItem and you want the item data in the response to be restricted to
     * the ViewItemURL (the URL where a user can view the listing) and BookItNowPrice,
     * then within the GetItem request, specify those output fields.
     * The output selecting logic is handled uniformly at SOA framework level.
     */
    public List<String> outputSelector;

    /**
     * The version number of the API code that you are programming against (e.g., 1.2.0).
     * If not set, defaults to latest version. Whether and how this field is leveraged depends on specific service.
     */
    public String getVersion() {
        return version;
    }

    /**
     * The version number of the API code that you are programming against (e.g., 1.2.0).
     * If not set, defaults to latest version. Whether and how this field is leveraged depends on specific service.
     */
    public void setVersion(final String version) {
        this.version = version;
    }


    /**
     * You can use the OutputSelector field to restrict the data returned by this call.
     * When you make a call such as GetItem that retrieves data from baiji service,
     * the OutputSelector field is useful for restricting the data returned.
     * This field makes the call response easier to use, especially when a large payload would be returned.
     * If you use the OutputSelector field, the output data will include only the fields you specified in the request.
     * For example, if you are using GetItem and you want the item data in the response to be restricted to
     * the ViewItemURL (the URL where a user can view the listing) and BookItNowPrice,
     * then within the GetItem request, specify those output fields.
     * The output selecting logic is handled uniformly at SOA framework level.
     */
    public List<String> getOutputSelector() {
        return outputSelector;
    }

    /**
     * You can use the OutputSelector field to restrict the data returned by this call.
     * When you make a call such as GetItem that retrieves data from baiji service,
     * the OutputSelector field is useful for restricting the data returned.
     * This field makes the call response easier to use, especially when a large payload would be returned.
     * If you use the OutputSelector field, the output data will include only the fields you specified in the request.
     * For example, if you are using GetItem and you want the item data in the response to be restricted to
     * the ViewItemURL (the URL where a user can view the listing) and BookItNowPrice,
     * then within the GetItem request, specify those output fields.
     * The output selecting logic is handled uniformly at SOA framework level.
     */
    public void setOutputSelector(final List<String> outputSelector) {
        this.outputSelector = outputSelector;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.version;
            case 1: return this.outputSelector;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.version = (String)fieldValue; break;
            case 1: this.outputSelector = (List<String>)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final CommonRequestType other = (CommonRequestType)obj;
        return 
            Objects.equal(this.version, other.version) &&
            Objects.equal(this.outputSelector, other.outputSelector);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.version == null ? 0 : this.version.hashCode());
        result = 31 * result + (this.outputSelector == null ? 0 : this.outputSelector.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("version", version)
            .add("outputSelector", outputSelector)
            .toString();
    }
}
