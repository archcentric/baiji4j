package com.ctriposs.baiji.rpc.testservice;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class GetItemsResponseType extends SpecificRecordBase implements SpecificRecord, HasResponseStatus {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"GetItemsResponseType\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"responseStatus\",\"type\":[{\"type\":\"record\",\"name\":\"ResponseStatusType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"timestamp\",\"type\":[\"datetime\",\"null\"]},{\"name\":\"ack\",\"type\":[{\"type\":\"enum\",\"name\":\"AckCodeType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"symbols\":[\"SUCCESS\",\"FAILURE\",\"WARNING\",\"PARTIAL_FAILURE\"]},\"null\"]},{\"name\":\"errors\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"ErrorDataType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"message\",\"type\":[\"string\",\"null\"]},{\"name\":\"errorCode\",\"type\":[\"string\",\"null\"]},{\"name\":\"stackTrace\",\"type\":[\"string\",\"null\"]},{\"name\":\"severityCode\",\"type\":[{\"type\":\"enum\",\"name\":\"SeverityCodeType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"symbols\":[\"ERROR\",\"WARNING\"]},\"null\"]},{\"name\":\"errorFields\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"ErrorFieldType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"fieldName\",\"type\":[\"string\",\"null\"]},{\"name\":\"errorCode\",\"type\":[\"string\",\"null\"]},{\"name\":\"message\",\"type\":[\"string\",\"null\"]}]}},\"null\"]},{\"name\":\"errorClassification\",\"type\":[{\"type\":\"enum\",\"name\":\"ErrorClassificationCodeType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"symbols\":[\"SERVICE_ERROR\",\"VALIDATION_ERROR\",\"FRAMEWORK_ERROR\",\"SLAERROR\",\"SECURITY_ERROR\"]},\"null\"]}]}},\"null\"]},{\"name\":\"build\",\"type\":[\"string\",\"null\"]},{\"name\":\"version\",\"type\":[\"string\",\"null\"]},{\"name\":\"extension\",\"type\":[{\"type\":\"record\",\"name\":\"ExtensionType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"id\",\"type\":[\"int\",\"null\"]},{\"name\":\"version\",\"type\":[\"string\",\"null\"]},{\"name\":\"contentType\",\"type\":[\"string\",\"null\"]},{\"name\":\"value\",\"type\":[\"string\",\"null\"]}]},\"null\"]}]},\"null\"]},{\"name\":\"items\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"Item\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"itemId\",\"type\":[\"string\",\"null\"]},{\"name\":\"title\",\"type\":[\"string\",\"null\"]},{\"name\":\"globalId\",\"type\":[\"string\",\"null\"]},{\"name\":\"subtitle\",\"type\":[\"string\",\"null\"]},{\"name\":\"primaryCategory\",\"type\":[{\"type\":\"record\",\"name\":\"Category\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"categoryId\",\"type\":[\"string\",\"null\"]},{\"name\":\"categoryName\",\"type\":[\"string\",\"null\"]}]},\"null\"]},{\"name\":\"secondaryCategory\",\"type\":[\"Category\",\"null\"]},{\"name\":\"galleryUrl\",\"type\":[\"string\",\"null\"]},{\"name\":\"galleryInfoContainer\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"GalleryUrl\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"gallerySize\",\"type\":[{\"type\":\"enum\",\"name\":\"GallerySize\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"symbols\":[\"SMALL\",\"MEDIUM\",\"LARGE\"]},\"null\"]},{\"name\":\"value\",\"type\":[\"string\",\"null\"]}]}},\"null\"]},{\"name\":\"viewItemUrl\",\"type\":[\"string\",\"null\"]},{\"name\":\"charityId\",\"type\":[\"string\",\"null\"]},{\"name\":\"productId\",\"type\":[{\"type\":\"record\",\"name\":\"ProductId\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"type\",\"type\":[\"string\",\"null\"]},{\"name\":\"value\",\"type\":[\"string\",\"null\"]}]},\"null\"]},{\"name\":\"paymentMethods\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"autoPay\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"postalCode\",\"type\":[\"string\",\"null\"]},{\"name\":\"location\",\"type\":[\"string\",\"null\"]},{\"name\":\"country\",\"type\":[\"string\",\"null\"]},{\"name\":\"storeInfo\",\"type\":[{\"type\":\"record\",\"name\":\"StoreFront\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"storeName\",\"type\":[\"string\",\"null\"]},{\"name\":\"storeUrl\",\"type\":[\"string\",\"null\"]}]},\"null\"]},{\"name\":\"sellerInfo\",\"type\":[{\"type\":\"record\",\"name\":\"SellerInfo\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"sellerUserName\",\"type\":[\"string\",\"null\"]},{\"name\":\"feedbackScore\",\"type\":[\"long\",\"null\"]},{\"name\":\"positiveFeedbackPercent\",\"type\":[\"double\",\"null\"]},{\"name\":\"feedbackRatingStar\",\"type\":[\"string\",\"null\"]},{\"name\":\"topRatedSeller\",\"type\":[\"boolean\",\"null\"]}]},\"null\"]},{\"name\":\"shippingInfo\",\"type\":[{\"type\":\"record\",\"name\":\"ShippingInfo\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"shippingServiceCost\",\"type\":[{\"type\":\"record\",\"name\":\"Amount\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"currencyId\",\"type\":[\"string\",\"null\"]},{\"name\":\"value\",\"type\":[\"double\",\"null\"]}]},\"null\"]},{\"name\":\"shippingType\",\"type\":[\"string\",\"null\"]},{\"name\":\"shipToLocations\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"expeditedShipping\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"oneDayShippingAvailable\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"handlingTime\",\"type\":[\"int\",\"null\"]},{\"name\":\"intermediatedShipping\",\"type\":[\"boolean\",\"null\"]}]},\"null\"]},{\"name\":\"sellingStatus\",\"type\":[{\"type\":\"record\",\"name\":\"SellingStatus\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"currentPrice\",\"type\":[\"Amount\",\"null\"]},{\"name\":\"convertedCurrentPrice\",\"type\":[\"Amount\",\"null\"]},{\"name\":\"bidCount\",\"type\":[\"int\",\"null\"]},{\"name\":\"sellingState\",\"type\":[\"string\",\"null\"]},{\"name\":\"timeLeft\",\"type\":[\"string\",\"null\"]}]},\"null\"]},{\"name\":\"listingInfo\",\"type\":[{\"type\":\"record\",\"name\":\"ListingInfo\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"bestOfferEnabled\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"buyItNowAvailable\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"buyItNowPrice\",\"type\":[\"Amount\",\"null\"]},{\"name\":\"convertedBuyItNowPrice\",\"type\":[\"Amount\",\"null\"]},{\"name\":\"startTime\",\"type\":[\"datetime\",\"null\"]},{\"name\":\"endTime\",\"type\":[\"datetime\",\"null\"]},{\"name\":\"listingType\",\"type\":[\"string\",\"null\"]},{\"name\":\"gift\",\"type\":[\"boolean\",\"null\"]}]},\"null\"]},{\"name\":\"returnsAccepted\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"galleryPlusPictureUrls\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"compatibility\",\"type\":[\"string\",\"null\"]},{\"name\":\"distance\",\"type\":[{\"type\":\"record\",\"name\":\"Distance\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"unit\",\"type\":[\"string\",\"null\"]},{\"name\":\"value\",\"type\":[\"double\",\"null\"]}]},\"null\"]},{\"name\":\"condition\",\"type\":[{\"type\":\"record\",\"name\":\"Condition\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"conditionId\",\"type\":[\"int\",\"null\"]},{\"name\":\"conditionDisplayName\",\"type\":[\"string\",\"null\"]}]},\"null\"]},{\"name\":\"isMultiVariationListing\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"discountPriceInfo\",\"type\":[{\"type\":\"record\",\"name\":\"DiscountPriceInfo\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"originalRetailPrice\",\"type\":[\"Amount\",\"null\"]},{\"name\":\"minimunAdvertisedPriceExposure\",\"type\":[{\"type\":\"enum\",\"name\":\"MapExposure\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"symbols\":[\"PRE_CHECKOUT\",\"DURING_CHECKOUT\"]},\"null\"]},{\"name\":\"pricingTreatment\",\"type\":[{\"type\":\"enum\",\"name\":\"PriceTreatment\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"symbols\":[\"STP\",\"MAP\"]},\"null\"]},{\"name\":\"soldOnEbay\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"soldOffEbay\",\"type\":[\"boolean\",\"null\"]}]},\"null\"]},{\"name\":\"pictureUrlSuperSize\",\"type\":[\"string\",\"null\"]},{\"name\":\"pictureUrlLarge\",\"type\":[\"string\",\"null\"]},{\"name\":\"unitPrice\",\"type\":[{\"type\":\"record\",\"name\":\"UnitPriceInfo\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"type\",\"type\":[\"string\",\"null\"]},{\"name\":\"quantity\",\"type\":[\"double\",\"null\"]}]},\"null\"]},{\"name\":\"topRatedListing\",\"type\":[\"boolean\",\"null\"]}]}},\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public GetItemsResponseType(
        com.ctriposs.baiji.rpc.common.types.ResponseStatusType responseStatus,
        List<Item> items
    ) {
        this.responseStatus = responseStatus;
        this.items = items;
    }

    public GetItemsResponseType() {
    }

    public com.ctriposs.baiji.rpc.common.types.ResponseStatusType responseStatus;

    public List<Item> items;

    public com.ctriposs.baiji.rpc.common.types.ResponseStatusType getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(final com.ctriposs.baiji.rpc.common.types.ResponseStatusType responseStatus) {
        this.responseStatus = responseStatus;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(final List<Item> items) {
        this.items = items;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.responseStatus;
            case 1: return this.items;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.responseStatus = (com.ctriposs.baiji.rpc.common.types.ResponseStatusType)fieldValue; break;
            case 1: this.items = (List<Item>)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final GetItemsResponseType other = (GetItemsResponseType)obj;
        return 
            Objects.equal(this.responseStatus, other.responseStatus) &&
            Objects.equal(this.items, other.items);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.responseStatus == null ? 0 : this.responseStatus.hashCode());
        result = 31 * result + (this.items == null ? 0 : this.items.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("responseStatus", responseStatus)
            .add("items", items)
            .toString();
    }
}
