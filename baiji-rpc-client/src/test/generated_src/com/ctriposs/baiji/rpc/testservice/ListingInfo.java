package com.ctriposs.baiji.rpc.testservice;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class ListingInfo extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"ListingInfo\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"bestOfferEnabled\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"buyItNowAvailable\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"buyItNowPrice\",\"type\":[{\"type\":\"record\",\"name\":\"Amount\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"currencyId\",\"type\":[\"string\",\"null\"]},{\"name\":\"value\",\"type\":[\"double\",\"null\"]}]},\"null\"]},{\"name\":\"convertedBuyItNowPrice\",\"type\":[\"Amount\",\"null\"]},{\"name\":\"startTime\",\"type\":[\"datetime\",\"null\"]},{\"name\":\"endTime\",\"type\":[\"datetime\",\"null\"]},{\"name\":\"listingType\",\"type\":[\"string\",\"null\"]},{\"name\":\"gift\",\"type\":[\"boolean\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public ListingInfo(
        Boolean bestOfferEnabled,
        Boolean buyItNowAvailable,
        Amount buyItNowPrice,
        Amount convertedBuyItNowPrice,
        java.util.Calendar startTime,
        java.util.Calendar endTime,
        String listingType,
        Boolean gift
    ) {
        this.bestOfferEnabled = bestOfferEnabled;
        this.buyItNowAvailable = buyItNowAvailable;
        this.buyItNowPrice = buyItNowPrice;
        this.convertedBuyItNowPrice = convertedBuyItNowPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.listingType = listingType;
        this.gift = gift;
    }

    public ListingInfo() {
    }

    public Boolean bestOfferEnabled;

    public Boolean buyItNowAvailable;

    public Amount buyItNowPrice;

    public Amount convertedBuyItNowPrice;

    public java.util.Calendar startTime;

    public java.util.Calendar endTime;

    public String listingType;

    public Boolean gift;

    public Boolean isBestOfferEnabled() {
        return bestOfferEnabled;
    }

    public void setBestOfferEnabled(final Boolean bestOfferEnabled) {
        this.bestOfferEnabled = bestOfferEnabled;
    }

    public Boolean isBuyItNowAvailable() {
        return buyItNowAvailable;
    }

    public void setBuyItNowAvailable(final Boolean buyItNowAvailable) {
        this.buyItNowAvailable = buyItNowAvailable;
    }

    public Amount getBuyItNowPrice() {
        return buyItNowPrice;
    }

    public void setBuyItNowPrice(final Amount buyItNowPrice) {
        this.buyItNowPrice = buyItNowPrice;
    }

    public Amount getConvertedBuyItNowPrice() {
        return convertedBuyItNowPrice;
    }

    public void setConvertedBuyItNowPrice(final Amount convertedBuyItNowPrice) {
        this.convertedBuyItNowPrice = convertedBuyItNowPrice;
    }

    public java.util.Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(final java.util.Calendar startTime) {
        this.startTime = startTime;
    }

    public java.util.Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(final java.util.Calendar endTime) {
        this.endTime = endTime;
    }

    public String getListingType() {
        return listingType;
    }

    public void setListingType(final String listingType) {
        this.listingType = listingType;
    }

    public Boolean isGift() {
        return gift;
    }

    public void setGift(final Boolean gift) {
        this.gift = gift;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.bestOfferEnabled;
            case 1: return this.buyItNowAvailable;
            case 2: return this.buyItNowPrice;
            case 3: return this.convertedBuyItNowPrice;
            case 4: return this.startTime;
            case 5: return this.endTime;
            case 6: return this.listingType;
            case 7: return this.gift;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.bestOfferEnabled = (Boolean)fieldValue; break;
            case 1: this.buyItNowAvailable = (Boolean)fieldValue; break;
            case 2: this.buyItNowPrice = (Amount)fieldValue; break;
            case 3: this.convertedBuyItNowPrice = (Amount)fieldValue; break;
            case 4: this.startTime = (java.util.Calendar)fieldValue; break;
            case 5: this.endTime = (java.util.Calendar)fieldValue; break;
            case 6: this.listingType = (String)fieldValue; break;
            case 7: this.gift = (Boolean)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final ListingInfo other = (ListingInfo)obj;
        return 
            Objects.equal(this.bestOfferEnabled, other.bestOfferEnabled) &&
            Objects.equal(this.buyItNowAvailable, other.buyItNowAvailable) &&
            Objects.equal(this.buyItNowPrice, other.buyItNowPrice) &&
            Objects.equal(this.convertedBuyItNowPrice, other.convertedBuyItNowPrice) &&
            Objects.equal(this.startTime, other.startTime) &&
            Objects.equal(this.endTime, other.endTime) &&
            Objects.equal(this.listingType, other.listingType) &&
            Objects.equal(this.gift, other.gift);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.bestOfferEnabled == null ? 0 : this.bestOfferEnabled.hashCode());
        result = 31 * result + (this.buyItNowAvailable == null ? 0 : this.buyItNowAvailable.hashCode());
        result = 31 * result + (this.buyItNowPrice == null ? 0 : this.buyItNowPrice.hashCode());
        result = 31 * result + (this.convertedBuyItNowPrice == null ? 0 : this.convertedBuyItNowPrice.hashCode());
        result = 31 * result + (this.startTime == null ? 0 : this.startTime.hashCode());
        result = 31 * result + (this.endTime == null ? 0 : this.endTime.hashCode());
        result = 31 * result + (this.listingType == null ? 0 : this.listingType.hashCode());
        result = 31 * result + (this.gift == null ? 0 : this.gift.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("bestOfferEnabled", bestOfferEnabled)
            .add("buyItNowAvailable", buyItNowAvailable)
            .add("buyItNowPrice", buyItNowPrice)
            .add("convertedBuyItNowPrice", convertedBuyItNowPrice)
            .add("startTime", startTime)
            .add("endTime", endTime)
            .add("listingType", listingType)
            .add("gift", gift)
            .toString();
    }
}
