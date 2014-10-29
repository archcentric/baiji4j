package com.ctriposs.baiji.rpc.testservice;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class ShippingInfo extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"ShippingInfo\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"shippingServiceCost\",\"type\":[{\"type\":\"record\",\"name\":\"Amount\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"currencyId\",\"type\":[\"string\",\"null\"]},{\"name\":\"value\",\"type\":[\"double\",\"null\"]}]},\"null\"]},{\"name\":\"shippingType\",\"type\":[\"string\",\"null\"]},{\"name\":\"shipToLocations\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"expeditedShipping\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"oneDayShippingAvailable\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"handlingTime\",\"type\":[\"int\",\"null\"]},{\"name\":\"intermediatedShipping\",\"type\":[\"boolean\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public ShippingInfo(
        Amount shippingServiceCost,
        String shippingType,
        List<String> shipToLocations,
        Boolean expeditedShipping,
        Boolean oneDayShippingAvailable,
        Integer handlingTime,
        Boolean intermediatedShipping
    ) {
        this.shippingServiceCost = shippingServiceCost;
        this.shippingType = shippingType;
        this.shipToLocations = shipToLocations;
        this.expeditedShipping = expeditedShipping;
        this.oneDayShippingAvailable = oneDayShippingAvailable;
        this.handlingTime = handlingTime;
        this.intermediatedShipping = intermediatedShipping;
    }

    public ShippingInfo() {
    }

    public Amount shippingServiceCost;

    public String shippingType;

    public List<String> shipToLocations;

    public Boolean expeditedShipping;

    public Boolean oneDayShippingAvailable;

    public Integer handlingTime;

    public Boolean intermediatedShipping;

    public Amount getShippingServiceCost() {
        return shippingServiceCost;
    }

    public void setShippingServiceCost(final Amount shippingServiceCost) {
        this.shippingServiceCost = shippingServiceCost;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(final String shippingType) {
        this.shippingType = shippingType;
    }

    public List<String> getShipToLocations() {
        return shipToLocations;
    }

    public void setShipToLocations(final List<String> shipToLocations) {
        this.shipToLocations = shipToLocations;
    }

    public Boolean isExpeditedShipping() {
        return expeditedShipping;
    }

    public void setExpeditedShipping(final Boolean expeditedShipping) {
        this.expeditedShipping = expeditedShipping;
    }

    public Boolean isOneDayShippingAvailable() {
        return oneDayShippingAvailable;
    }

    public void setOneDayShippingAvailable(final Boolean oneDayShippingAvailable) {
        this.oneDayShippingAvailable = oneDayShippingAvailable;
    }

    public Integer getHandlingTime() {
        return handlingTime;
    }

    public void setHandlingTime(final Integer handlingTime) {
        this.handlingTime = handlingTime;
    }

    public Boolean isIntermediatedShipping() {
        return intermediatedShipping;
    }

    public void setIntermediatedShipping(final Boolean intermediatedShipping) {
        this.intermediatedShipping = intermediatedShipping;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.shippingServiceCost;
            case 1: return this.shippingType;
            case 2: return this.shipToLocations;
            case 3: return this.expeditedShipping;
            case 4: return this.oneDayShippingAvailable;
            case 5: return this.handlingTime;
            case 6: return this.intermediatedShipping;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.shippingServiceCost = (Amount)fieldValue; break;
            case 1: this.shippingType = (String)fieldValue; break;
            case 2: this.shipToLocations = (List<String>)fieldValue; break;
            case 3: this.expeditedShipping = (Boolean)fieldValue; break;
            case 4: this.oneDayShippingAvailable = (Boolean)fieldValue; break;
            case 5: this.handlingTime = (Integer)fieldValue; break;
            case 6: this.intermediatedShipping = (Boolean)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final ShippingInfo other = (ShippingInfo)obj;
        return 
            Objects.equal(this.shippingServiceCost, other.shippingServiceCost) &&
            Objects.equal(this.shippingType, other.shippingType) &&
            Objects.equal(this.shipToLocations, other.shipToLocations) &&
            Objects.equal(this.expeditedShipping, other.expeditedShipping) &&
            Objects.equal(this.oneDayShippingAvailable, other.oneDayShippingAvailable) &&
            Objects.equal(this.handlingTime, other.handlingTime) &&
            Objects.equal(this.intermediatedShipping, other.intermediatedShipping);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.shippingServiceCost == null ? 0 : this.shippingServiceCost.hashCode());
        result = 31 * result + (this.shippingType == null ? 0 : this.shippingType.hashCode());
        result = 31 * result + (this.shipToLocations == null ? 0 : this.shipToLocations.hashCode());
        result = 31 * result + (this.expeditedShipping == null ? 0 : this.expeditedShipping.hashCode());
        result = 31 * result + (this.oneDayShippingAvailable == null ? 0 : this.oneDayShippingAvailable.hashCode());
        result = 31 * result + (this.handlingTime == null ? 0 : this.handlingTime.hashCode());
        result = 31 * result + (this.intermediatedShipping == null ? 0 : this.intermediatedShipping.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("shippingServiceCost", shippingServiceCost)
            .add("shippingType", shippingType)
            .add("shipToLocations", shipToLocations)
            .add("expeditedShipping", expeditedShipping)
            .add("oneDayShippingAvailable", oneDayShippingAvailable)
            .add("handlingTime", handlingTime)
            .add("intermediatedShipping", intermediatedShipping)
            .toString();
    }
}
