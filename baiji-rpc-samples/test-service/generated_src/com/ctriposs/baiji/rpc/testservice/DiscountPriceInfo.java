package com.ctriposs.baiji.rpc.testservice;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class DiscountPriceInfo extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"DiscountPriceInfo\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"originalRetailPrice\",\"type\":[{\"type\":\"record\",\"name\":\"Amount\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"currencyId\",\"type\":[\"string\",\"null\"]},{\"name\":\"value\",\"type\":[\"double\",\"null\"]}]},\"null\"]},{\"name\":\"minimunAdvertisedPriceExposure\",\"type\":[{\"type\":\"enum\",\"name\":\"MapExposure\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"symbols\":[\"PRE_CHECKOUT\",\"DURING_CHECKOUT\"]},\"null\"]},{\"name\":\"pricingTreatment\",\"type\":[{\"type\":\"enum\",\"name\":\"PriceTreatment\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"symbols\":[\"STP\",\"MAP\"]},\"null\"]},{\"name\":\"soldOnEbay\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"soldOffEbay\",\"type\":[\"boolean\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public DiscountPriceInfo(
        Amount originalRetailPrice,
        MapExposure minimunAdvertisedPriceExposure,
        PriceTreatment pricingTreatment,
        Boolean soldOnEbay,
        Boolean soldOffEbay
    ) {
        this.originalRetailPrice = originalRetailPrice;
        this.minimunAdvertisedPriceExposure = minimunAdvertisedPriceExposure;
        this.pricingTreatment = pricingTreatment;
        this.soldOnEbay = soldOnEbay;
        this.soldOffEbay = soldOffEbay;
    }

    public DiscountPriceInfo() {
    }

    public Amount originalRetailPrice;

    public MapExposure minimunAdvertisedPriceExposure;

    public PriceTreatment pricingTreatment;

    public Boolean soldOnEbay;

    public Boolean soldOffEbay;

    public Amount getOriginalRetailPrice() {
        return originalRetailPrice;
    }

    public void setOriginalRetailPrice(final Amount originalRetailPrice) {
        this.originalRetailPrice = originalRetailPrice;
    }

    public MapExposure getMinimunAdvertisedPriceExposure() {
        return minimunAdvertisedPriceExposure;
    }

    public void setMinimunAdvertisedPriceExposure(final MapExposure minimunAdvertisedPriceExposure) {
        this.minimunAdvertisedPriceExposure = minimunAdvertisedPriceExposure;
    }

    public PriceTreatment getPricingTreatment() {
        return pricingTreatment;
    }

    public void setPricingTreatment(final PriceTreatment pricingTreatment) {
        this.pricingTreatment = pricingTreatment;
    }

    public Boolean isSoldOnEbay() {
        return soldOnEbay;
    }

    public void setSoldOnEbay(final Boolean soldOnEbay) {
        this.soldOnEbay = soldOnEbay;
    }

    public Boolean isSoldOffEbay() {
        return soldOffEbay;
    }

    public void setSoldOffEbay(final Boolean soldOffEbay) {
        this.soldOffEbay = soldOffEbay;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.originalRetailPrice;
            case 1: return this.minimunAdvertisedPriceExposure;
            case 2: return this.pricingTreatment;
            case 3: return this.soldOnEbay;
            case 4: return this.soldOffEbay;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.originalRetailPrice = (Amount)fieldValue; break;
            case 1: this.minimunAdvertisedPriceExposure = (MapExposure)fieldValue; break;
            case 2: this.pricingTreatment = (PriceTreatment)fieldValue; break;
            case 3: this.soldOnEbay = (Boolean)fieldValue; break;
            case 4: this.soldOffEbay = (Boolean)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final DiscountPriceInfo other = (DiscountPriceInfo)obj;
        return 
            Objects.equal(this.originalRetailPrice, other.originalRetailPrice) &&
            Objects.equal(this.minimunAdvertisedPriceExposure, other.minimunAdvertisedPriceExposure) &&
            Objects.equal(this.pricingTreatment, other.pricingTreatment) &&
            Objects.equal(this.soldOnEbay, other.soldOnEbay) &&
            Objects.equal(this.soldOffEbay, other.soldOffEbay);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.originalRetailPrice == null ? 0 : this.originalRetailPrice.hashCode());
        result = 31 * result + (this.minimunAdvertisedPriceExposure == null ? 0 : this.minimunAdvertisedPriceExposure.hashCode());
        result = 31 * result + (this.pricingTreatment == null ? 0 : this.pricingTreatment.hashCode());
        result = 31 * result + (this.soldOnEbay == null ? 0 : this.soldOnEbay.hashCode());
        result = 31 * result + (this.soldOffEbay == null ? 0 : this.soldOffEbay.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("originalRetailPrice", originalRetailPrice)
            .add("minimunAdvertisedPriceExposure", minimunAdvertisedPriceExposure)
            .add("pricingTreatment", pricingTreatment)
            .add("soldOnEbay", soldOnEbay)
            .add("soldOffEbay", soldOffEbay)
            .toString();
    }
}
