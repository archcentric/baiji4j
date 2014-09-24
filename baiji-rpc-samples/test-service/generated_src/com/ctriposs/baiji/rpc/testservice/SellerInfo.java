package com.ctriposs.baiji.rpc.testservice;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class SellerInfo extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"SellerInfo\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"sellerUserName\",\"type\":[\"string\",\"null\"]},{\"name\":\"feedbackScore\",\"type\":[\"long\",\"null\"]},{\"name\":\"positiveFeedbackPercent\",\"type\":[\"double\",\"null\"]},{\"name\":\"feedbackRatingStar\",\"type\":[\"string\",\"null\"]},{\"name\":\"topRatedSeller\",\"type\":[\"boolean\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public SellerInfo(
        String sellerUserName,
        Long feedbackScore,
        Double positiveFeedbackPercent,
        String feedbackRatingStar,
        Boolean topRatedSeller
    ) {
        this.sellerUserName = sellerUserName;
        this.feedbackScore = feedbackScore;
        this.positiveFeedbackPercent = positiveFeedbackPercent;
        this.feedbackRatingStar = feedbackRatingStar;
        this.topRatedSeller = topRatedSeller;
    }

    public SellerInfo() {
    }

    public String sellerUserName;

    public Long feedbackScore;

    public Double positiveFeedbackPercent;

    public String feedbackRatingStar;

    public Boolean topRatedSeller;

    public String getSellerUserName() {
        return sellerUserName;
    }

    public void setSellerUserName(final String sellerUserName) {
        this.sellerUserName = sellerUserName;
    }

    public Long getFeedbackScore() {
        return feedbackScore;
    }

    public void setFeedbackScore(final Long feedbackScore) {
        this.feedbackScore = feedbackScore;
    }

    public Double getPositiveFeedbackPercent() {
        return positiveFeedbackPercent;
    }

    public void setPositiveFeedbackPercent(final Double positiveFeedbackPercent) {
        this.positiveFeedbackPercent = positiveFeedbackPercent;
    }

    public String getFeedbackRatingStar() {
        return feedbackRatingStar;
    }

    public void setFeedbackRatingStar(final String feedbackRatingStar) {
        this.feedbackRatingStar = feedbackRatingStar;
    }

    public Boolean isTopRatedSeller() {
        return topRatedSeller;
    }

    public void setTopRatedSeller(final Boolean topRatedSeller) {
        this.topRatedSeller = topRatedSeller;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.sellerUserName;
            case 1: return this.feedbackScore;
            case 2: return this.positiveFeedbackPercent;
            case 3: return this.feedbackRatingStar;
            case 4: return this.topRatedSeller;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.sellerUserName = (String)fieldValue; break;
            case 1: this.feedbackScore = (Long)fieldValue; break;
            case 2: this.positiveFeedbackPercent = (Double)fieldValue; break;
            case 3: this.feedbackRatingStar = (String)fieldValue; break;
            case 4: this.topRatedSeller = (Boolean)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final SellerInfo other = (SellerInfo)obj;
        return 
            Objects.equal(this.sellerUserName, other.sellerUserName) &&
            Objects.equal(this.feedbackScore, other.feedbackScore) &&
            Objects.equal(this.positiveFeedbackPercent, other.positiveFeedbackPercent) &&
            Objects.equal(this.feedbackRatingStar, other.feedbackRatingStar) &&
            Objects.equal(this.topRatedSeller, other.topRatedSeller);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.sellerUserName == null ? 0 : this.sellerUserName.hashCode());
        result = 31 * result + (this.feedbackScore == null ? 0 : this.feedbackScore.hashCode());
        result = 31 * result + (this.positiveFeedbackPercent == null ? 0 : this.positiveFeedbackPercent.hashCode());
        result = 31 * result + (this.feedbackRatingStar == null ? 0 : this.feedbackRatingStar.hashCode());
        result = 31 * result + (this.topRatedSeller == null ? 0 : this.topRatedSeller.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("sellerUserName", sellerUserName)
            .add("feedbackScore", feedbackScore)
            .add("positiveFeedbackPercent", positiveFeedbackPercent)
            .add("feedbackRatingStar", feedbackRatingStar)
            .add("topRatedSeller", topRatedSeller)
            .toString();
    }
}
