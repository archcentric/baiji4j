package com.ctriposs.baiji.rpc.testservice;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class SellingStatus extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"SellingStatus\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"currentPrice\",\"type\":[{\"type\":\"record\",\"name\":\"Amount\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"currencyId\",\"type\":[\"string\",\"null\"]},{\"name\":\"value\",\"type\":[\"double\",\"null\"]}]},\"null\"]},{\"name\":\"convertedCurrentPrice\",\"type\":[\"Amount\",\"null\"]},{\"name\":\"bidCount\",\"type\":[\"int\",\"null\"]},{\"name\":\"sellingState\",\"type\":[\"string\",\"null\"]},{\"name\":\"timeLeft\",\"type\":[\"string\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public SellingStatus(
        Amount currentPrice,
        Amount convertedCurrentPrice,
        Integer bidCount,
        String sellingState,
        String timeLeft
    ) {
        this.currentPrice = currentPrice;
        this.convertedCurrentPrice = convertedCurrentPrice;
        this.bidCount = bidCount;
        this.sellingState = sellingState;
        this.timeLeft = timeLeft;
    }

    public SellingStatus() {
    }

    public Amount currentPrice;

    public Amount convertedCurrentPrice;

    public Integer bidCount;

    public String sellingState;

    public String timeLeft;

    public Amount getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(final Amount currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Amount getConvertedCurrentPrice() {
        return convertedCurrentPrice;
    }

    public void setConvertedCurrentPrice(final Amount convertedCurrentPrice) {
        this.convertedCurrentPrice = convertedCurrentPrice;
    }

    public Integer getBidCount() {
        return bidCount;
    }

    public void setBidCount(final Integer bidCount) {
        this.bidCount = bidCount;
    }

    public String getSellingState() {
        return sellingState;
    }

    public void setSellingState(final String sellingState) {
        this.sellingState = sellingState;
    }

    public String getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(final String timeLeft) {
        this.timeLeft = timeLeft;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.currentPrice;
            case 1: return this.convertedCurrentPrice;
            case 2: return this.bidCount;
            case 3: return this.sellingState;
            case 4: return this.timeLeft;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.currentPrice = (Amount)fieldValue; break;
            case 1: this.convertedCurrentPrice = (Amount)fieldValue; break;
            case 2: this.bidCount = (Integer)fieldValue; break;
            case 3: this.sellingState = (String)fieldValue; break;
            case 4: this.timeLeft = (String)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final SellingStatus other = (SellingStatus)obj;
        return 
            Objects.equal(this.currentPrice, other.currentPrice) &&
            Objects.equal(this.convertedCurrentPrice, other.convertedCurrentPrice) &&
            Objects.equal(this.bidCount, other.bidCount) &&
            Objects.equal(this.sellingState, other.sellingState) &&
            Objects.equal(this.timeLeft, other.timeLeft);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.currentPrice == null ? 0 : this.currentPrice.hashCode());
        result = 31 * result + (this.convertedCurrentPrice == null ? 0 : this.convertedCurrentPrice.hashCode());
        result = 31 * result + (this.bidCount == null ? 0 : this.bidCount.hashCode());
        result = 31 * result + (this.sellingState == null ? 0 : this.sellingState.hashCode());
        result = 31 * result + (this.timeLeft == null ? 0 : this.timeLeft.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("currentPrice", currentPrice)
            .add("convertedCurrentPrice", convertedCurrentPrice)
            .add("bidCount", bidCount)
            .add("sellingState", sellingState)
            .add("timeLeft", timeLeft)
            .toString();
    }
}
