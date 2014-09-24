package com.ctriposs.baiji.rpc.testservice;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class Item extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"Item\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"itemId\",\"type\":[\"string\",\"null\"]},{\"name\":\"title\",\"type\":[\"string\",\"null\"]},{\"name\":\"globalId\",\"type\":[\"string\",\"null\"]},{\"name\":\"subtitle\",\"type\":[\"string\",\"null\"]},{\"name\":\"primaryCategory\",\"type\":[{\"type\":\"record\",\"name\":\"Category\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"categoryId\",\"type\":[\"string\",\"null\"]},{\"name\":\"categoryName\",\"type\":[\"string\",\"null\"]}]},\"null\"]},{\"name\":\"secondaryCategory\",\"type\":[\"Category\",\"null\"]},{\"name\":\"galleryUrl\",\"type\":[\"string\",\"null\"]},{\"name\":\"galleryInfoContainer\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"GalleryUrl\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"gallerySize\",\"type\":[{\"type\":\"enum\",\"name\":\"GallerySize\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"symbols\":[\"SMALL\",\"MEDIUM\",\"LARGE\"]},\"null\"]},{\"name\":\"value\",\"type\":[\"string\",\"null\"]}]}},\"null\"]},{\"name\":\"viewItemUrl\",\"type\":[\"string\",\"null\"]},{\"name\":\"charityId\",\"type\":[\"string\",\"null\"]},{\"name\":\"productId\",\"type\":[{\"type\":\"record\",\"name\":\"ProductId\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"type\",\"type\":[\"string\",\"null\"]},{\"name\":\"value\",\"type\":[\"string\",\"null\"]}]},\"null\"]},{\"name\":\"paymentMethods\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"autoPay\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"postalCode\",\"type\":[\"string\",\"null\"]},{\"name\":\"location\",\"type\":[\"string\",\"null\"]},{\"name\":\"country\",\"type\":[\"string\",\"null\"]},{\"name\":\"storeInfo\",\"type\":[{\"type\":\"record\",\"name\":\"StoreFront\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"storeName\",\"type\":[\"string\",\"null\"]},{\"name\":\"storeUrl\",\"type\":[\"string\",\"null\"]}]},\"null\"]},{\"name\":\"sellerInfo\",\"type\":[{\"type\":\"record\",\"name\":\"SellerInfo\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"sellerUserName\",\"type\":[\"string\",\"null\"]},{\"name\":\"feedbackScore\",\"type\":[\"long\",\"null\"]},{\"name\":\"positiveFeedbackPercent\",\"type\":[\"double\",\"null\"]},{\"name\":\"feedbackRatingStar\",\"type\":[\"string\",\"null\"]},{\"name\":\"topRatedSeller\",\"type\":[\"boolean\",\"null\"]}]},\"null\"]},{\"name\":\"shippingInfo\",\"type\":[{\"type\":\"record\",\"name\":\"ShippingInfo\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"shippingServiceCost\",\"type\":[{\"type\":\"record\",\"name\":\"Amount\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"currencyId\",\"type\":[\"string\",\"null\"]},{\"name\":\"value\",\"type\":[\"double\",\"null\"]}]},\"null\"]},{\"name\":\"shippingType\",\"type\":[\"string\",\"null\"]},{\"name\":\"shipToLocations\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"expeditedShipping\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"oneDayShippingAvailable\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"handlingTime\",\"type\":[\"int\",\"null\"]},{\"name\":\"intermediatedShipping\",\"type\":[\"boolean\",\"null\"]}]},\"null\"]},{\"name\":\"sellingStatus\",\"type\":[{\"type\":\"record\",\"name\":\"SellingStatus\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"currentPrice\",\"type\":[\"Amount\",\"null\"]},{\"name\":\"convertedCurrentPrice\",\"type\":[\"Amount\",\"null\"]},{\"name\":\"bidCount\",\"type\":[\"int\",\"null\"]},{\"name\":\"sellingState\",\"type\":[\"string\",\"null\"]},{\"name\":\"timeLeft\",\"type\":[\"string\",\"null\"]}]},\"null\"]},{\"name\":\"listingInfo\",\"type\":[{\"type\":\"record\",\"name\":\"ListingInfo\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"bestOfferEnabled\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"buyItNowAvailable\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"buyItNowPrice\",\"type\":[\"Amount\",\"null\"]},{\"name\":\"convertedBuyItNowPrice\",\"type\":[\"Amount\",\"null\"]},{\"name\":\"startTime\",\"type\":[\"datetime\",\"null\"]},{\"name\":\"endTime\",\"type\":[\"datetime\",\"null\"]},{\"name\":\"listingType\",\"type\":[\"string\",\"null\"]},{\"name\":\"gift\",\"type\":[\"boolean\",\"null\"]}]},\"null\"]},{\"name\":\"returnsAccepted\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"galleryPlusPictureUrls\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"compatibility\",\"type\":[\"string\",\"null\"]},{\"name\":\"distance\",\"type\":[{\"type\":\"record\",\"name\":\"Distance\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"unit\",\"type\":[\"string\",\"null\"]},{\"name\":\"value\",\"type\":[\"double\",\"null\"]}]},\"null\"]},{\"name\":\"condition\",\"type\":[{\"type\":\"record\",\"name\":\"Condition\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"conditionId\",\"type\":[\"int\",\"null\"]},{\"name\":\"conditionDisplayName\",\"type\":[\"string\",\"null\"]}]},\"null\"]},{\"name\":\"isMultiVariationListing\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"discountPriceInfo\",\"type\":[{\"type\":\"record\",\"name\":\"DiscountPriceInfo\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"originalRetailPrice\",\"type\":[\"Amount\",\"null\"]},{\"name\":\"minimunAdvertisedPriceExposure\",\"type\":[{\"type\":\"enum\",\"name\":\"MapExposure\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"symbols\":[\"PRE_CHECKOUT\",\"DURING_CHECKOUT\"]},\"null\"]},{\"name\":\"pricingTreatment\",\"type\":[{\"type\":\"enum\",\"name\":\"PriceTreatment\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"symbols\":[\"STP\",\"MAP\"]},\"null\"]},{\"name\":\"soldOnEbay\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"soldOffEbay\",\"type\":[\"boolean\",\"null\"]}]},\"null\"]},{\"name\":\"pictureUrlSuperSize\",\"type\":[\"string\",\"null\"]},{\"name\":\"pictureUrlLarge\",\"type\":[\"string\",\"null\"]},{\"name\":\"unitPrice\",\"type\":[{\"type\":\"record\",\"name\":\"UnitPriceInfo\",\"namespace\":\"com.ctriposs.baiji.rpc.testservice\",\"doc\":null,\"fields\":[{\"name\":\"type\",\"type\":[\"string\",\"null\"]},{\"name\":\"quantity\",\"type\":[\"double\",\"null\"]}]},\"null\"]},{\"name\":\"topRatedListing\",\"type\":[\"boolean\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public Item(
        String itemId,
        String title,
        String globalId,
        String subtitle,
        Category primaryCategory,
        Category secondaryCategory,
        String galleryUrl,
        List<GalleryUrl> galleryInfoContainer,
        String viewItemUrl,
        String charityId,
        ProductId productId,
        List<String> paymentMethods,
        Boolean autoPay,
        String postalCode,
        String location,
        String country,
        StoreFront storeInfo,
        SellerInfo sellerInfo,
        ShippingInfo shippingInfo,
        SellingStatus sellingStatus,
        ListingInfo listingInfo,
        Boolean returnsAccepted,
        List<String> galleryPlusPictureUrls,
        String compatibility,
        Distance distance,
        Condition condition,
        Boolean isMultiVariationListing,
        DiscountPriceInfo discountPriceInfo,
        String pictureUrlSuperSize,
        String pictureUrlLarge,
        UnitPriceInfo unitPrice,
        Boolean topRatedListing
    ) {
        this.itemId = itemId;
        this.title = title;
        this.globalId = globalId;
        this.subtitle = subtitle;
        this.primaryCategory = primaryCategory;
        this.secondaryCategory = secondaryCategory;
        this.galleryUrl = galleryUrl;
        this.galleryInfoContainer = galleryInfoContainer;
        this.viewItemUrl = viewItemUrl;
        this.charityId = charityId;
        this.productId = productId;
        this.paymentMethods = paymentMethods;
        this.autoPay = autoPay;
        this.postalCode = postalCode;
        this.location = location;
        this.country = country;
        this.storeInfo = storeInfo;
        this.sellerInfo = sellerInfo;
        this.shippingInfo = shippingInfo;
        this.sellingStatus = sellingStatus;
        this.listingInfo = listingInfo;
        this.returnsAccepted = returnsAccepted;
        this.galleryPlusPictureUrls = galleryPlusPictureUrls;
        this.compatibility = compatibility;
        this.distance = distance;
        this.condition = condition;
        this.isMultiVariationListing = isMultiVariationListing;
        this.discountPriceInfo = discountPriceInfo;
        this.pictureUrlSuperSize = pictureUrlSuperSize;
        this.pictureUrlLarge = pictureUrlLarge;
        this.unitPrice = unitPrice;
        this.topRatedListing = topRatedListing;
    }

    public Item() {
    }

    public String itemId;

    public String title;

    public String globalId;

    public String subtitle;

    public Category primaryCategory;

    public Category secondaryCategory;

    public String galleryUrl;

    public List<GalleryUrl> galleryInfoContainer;

    public String viewItemUrl;

    public String charityId;

    public ProductId productId;

    public List<String> paymentMethods;

    public Boolean autoPay;

    public String postalCode;

    public String location;

    public String country;

    public StoreFront storeInfo;

    public SellerInfo sellerInfo;

    public ShippingInfo shippingInfo;

    public SellingStatus sellingStatus;

    public ListingInfo listingInfo;

    public Boolean returnsAccepted;

    public List<String> galleryPlusPictureUrls;

    public String compatibility;

    public Distance distance;

    public Condition condition;

    public Boolean isMultiVariationListing;

    public DiscountPriceInfo discountPriceInfo;

    public String pictureUrlSuperSize;

    public String pictureUrlLarge;

    public UnitPriceInfo unitPrice;

    public Boolean topRatedListing;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(final String itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getGlobalId() {
        return globalId;
    }

    public void setGlobalId(final String globalId) {
        this.globalId = globalId;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(final String subtitle) {
        this.subtitle = subtitle;
    }

    public Category getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(final Category primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    public Category getSecondaryCategory() {
        return secondaryCategory;
    }

    public void setSecondaryCategory(final Category secondaryCategory) {
        this.secondaryCategory = secondaryCategory;
    }

    public String getGalleryUrl() {
        return galleryUrl;
    }

    public void setGalleryUrl(final String galleryUrl) {
        this.galleryUrl = galleryUrl;
    }

    public List<GalleryUrl> getGalleryInfoContainer() {
        return galleryInfoContainer;
    }

    public void setGalleryInfoContainer(final List<GalleryUrl> galleryInfoContainer) {
        this.galleryInfoContainer = galleryInfoContainer;
    }

    public String getViewItemUrl() {
        return viewItemUrl;
    }

    public void setViewItemUrl(final String viewItemUrl) {
        this.viewItemUrl = viewItemUrl;
    }

    public String getCharityId() {
        return charityId;
    }

    public void setCharityId(final String charityId) {
        this.charityId = charityId;
    }

    public ProductId getProductId() {
        return productId;
    }

    public void setProductId(final ProductId productId) {
        this.productId = productId;
    }

    public List<String> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(final List<String> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public Boolean isAutoPay() {
        return autoPay;
    }

    public void setAutoPay(final Boolean autoPay) {
        this.autoPay = autoPay;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public StoreFront getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(final StoreFront storeInfo) {
        this.storeInfo = storeInfo;
    }

    public SellerInfo getSellerInfo() {
        return sellerInfo;
    }

    public void setSellerInfo(final SellerInfo sellerInfo) {
        this.sellerInfo = sellerInfo;
    }

    public ShippingInfo getShippingInfo() {
        return shippingInfo;
    }

    public void setShippingInfo(final ShippingInfo shippingInfo) {
        this.shippingInfo = shippingInfo;
    }

    public SellingStatus getSellingStatus() {
        return sellingStatus;
    }

    public void setSellingStatus(final SellingStatus sellingStatus) {
        this.sellingStatus = sellingStatus;
    }

    public ListingInfo getListingInfo() {
        return listingInfo;
    }

    public void setListingInfo(final ListingInfo listingInfo) {
        this.listingInfo = listingInfo;
    }

    public Boolean isReturnsAccepted() {
        return returnsAccepted;
    }

    public void setReturnsAccepted(final Boolean returnsAccepted) {
        this.returnsAccepted = returnsAccepted;
    }

    public List<String> getGalleryPlusPictureUrls() {
        return galleryPlusPictureUrls;
    }

    public void setGalleryPlusPictureUrls(final List<String> galleryPlusPictureUrls) {
        this.galleryPlusPictureUrls = galleryPlusPictureUrls;
    }

    public String getCompatibility() {
        return compatibility;
    }

    public void setCompatibility(final String compatibility) {
        this.compatibility = compatibility;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(final Distance distance) {
        this.distance = distance;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(final Condition condition) {
        this.condition = condition;
    }

    public Boolean isIsMultiVariationListing() {
        return isMultiVariationListing;
    }

    public void setIsMultiVariationListing(final Boolean isMultiVariationListing) {
        this.isMultiVariationListing = isMultiVariationListing;
    }

    public DiscountPriceInfo getDiscountPriceInfo() {
        return discountPriceInfo;
    }

    public void setDiscountPriceInfo(final DiscountPriceInfo discountPriceInfo) {
        this.discountPriceInfo = discountPriceInfo;
    }

    public String getPictureUrlSuperSize() {
        return pictureUrlSuperSize;
    }

    public void setPictureUrlSuperSize(final String pictureUrlSuperSize) {
        this.pictureUrlSuperSize = pictureUrlSuperSize;
    }

    public String getPictureUrlLarge() {
        return pictureUrlLarge;
    }

    public void setPictureUrlLarge(final String pictureUrlLarge) {
        this.pictureUrlLarge = pictureUrlLarge;
    }

    public UnitPriceInfo getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(final UnitPriceInfo unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Boolean isTopRatedListing() {
        return topRatedListing;
    }

    public void setTopRatedListing(final Boolean topRatedListing) {
        this.topRatedListing = topRatedListing;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.itemId;
            case 1: return this.title;
            case 2: return this.globalId;
            case 3: return this.subtitle;
            case 4: return this.primaryCategory;
            case 5: return this.secondaryCategory;
            case 6: return this.galleryUrl;
            case 7: return this.galleryInfoContainer;
            case 8: return this.viewItemUrl;
            case 9: return this.charityId;
            case 10: return this.productId;
            case 11: return this.paymentMethods;
            case 12: return this.autoPay;
            case 13: return this.postalCode;
            case 14: return this.location;
            case 15: return this.country;
            case 16: return this.storeInfo;
            case 17: return this.sellerInfo;
            case 18: return this.shippingInfo;
            case 19: return this.sellingStatus;
            case 20: return this.listingInfo;
            case 21: return this.returnsAccepted;
            case 22: return this.galleryPlusPictureUrls;
            case 23: return this.compatibility;
            case 24: return this.distance;
            case 25: return this.condition;
            case 26: return this.isMultiVariationListing;
            case 27: return this.discountPriceInfo;
            case 28: return this.pictureUrlSuperSize;
            case 29: return this.pictureUrlLarge;
            case 30: return this.unitPrice;
            case 31: return this.topRatedListing;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.itemId = (String)fieldValue; break;
            case 1: this.title = (String)fieldValue; break;
            case 2: this.globalId = (String)fieldValue; break;
            case 3: this.subtitle = (String)fieldValue; break;
            case 4: this.primaryCategory = (Category)fieldValue; break;
            case 5: this.secondaryCategory = (Category)fieldValue; break;
            case 6: this.galleryUrl = (String)fieldValue; break;
            case 7: this.galleryInfoContainer = (List<GalleryUrl>)fieldValue; break;
            case 8: this.viewItemUrl = (String)fieldValue; break;
            case 9: this.charityId = (String)fieldValue; break;
            case 10: this.productId = (ProductId)fieldValue; break;
            case 11: this.paymentMethods = (List<String>)fieldValue; break;
            case 12: this.autoPay = (Boolean)fieldValue; break;
            case 13: this.postalCode = (String)fieldValue; break;
            case 14: this.location = (String)fieldValue; break;
            case 15: this.country = (String)fieldValue; break;
            case 16: this.storeInfo = (StoreFront)fieldValue; break;
            case 17: this.sellerInfo = (SellerInfo)fieldValue; break;
            case 18: this.shippingInfo = (ShippingInfo)fieldValue; break;
            case 19: this.sellingStatus = (SellingStatus)fieldValue; break;
            case 20: this.listingInfo = (ListingInfo)fieldValue; break;
            case 21: this.returnsAccepted = (Boolean)fieldValue; break;
            case 22: this.galleryPlusPictureUrls = (List<String>)fieldValue; break;
            case 23: this.compatibility = (String)fieldValue; break;
            case 24: this.distance = (Distance)fieldValue; break;
            case 25: this.condition = (Condition)fieldValue; break;
            case 26: this.isMultiVariationListing = (Boolean)fieldValue; break;
            case 27: this.discountPriceInfo = (DiscountPriceInfo)fieldValue; break;
            case 28: this.pictureUrlSuperSize = (String)fieldValue; break;
            case 29: this.pictureUrlLarge = (String)fieldValue; break;
            case 30: this.unitPrice = (UnitPriceInfo)fieldValue; break;
            case 31: this.topRatedListing = (Boolean)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final Item other = (Item)obj;
        return 
            Objects.equal(this.itemId, other.itemId) &&
            Objects.equal(this.title, other.title) &&
            Objects.equal(this.globalId, other.globalId) &&
            Objects.equal(this.subtitle, other.subtitle) &&
            Objects.equal(this.primaryCategory, other.primaryCategory) &&
            Objects.equal(this.secondaryCategory, other.secondaryCategory) &&
            Objects.equal(this.galleryUrl, other.galleryUrl) &&
            Objects.equal(this.galleryInfoContainer, other.galleryInfoContainer) &&
            Objects.equal(this.viewItemUrl, other.viewItemUrl) &&
            Objects.equal(this.charityId, other.charityId) &&
            Objects.equal(this.productId, other.productId) &&
            Objects.equal(this.paymentMethods, other.paymentMethods) &&
            Objects.equal(this.autoPay, other.autoPay) &&
            Objects.equal(this.postalCode, other.postalCode) &&
            Objects.equal(this.location, other.location) &&
            Objects.equal(this.country, other.country) &&
            Objects.equal(this.storeInfo, other.storeInfo) &&
            Objects.equal(this.sellerInfo, other.sellerInfo) &&
            Objects.equal(this.shippingInfo, other.shippingInfo) &&
            Objects.equal(this.sellingStatus, other.sellingStatus) &&
            Objects.equal(this.listingInfo, other.listingInfo) &&
            Objects.equal(this.returnsAccepted, other.returnsAccepted) &&
            Objects.equal(this.galleryPlusPictureUrls, other.galleryPlusPictureUrls) &&
            Objects.equal(this.compatibility, other.compatibility) &&
            Objects.equal(this.distance, other.distance) &&
            Objects.equal(this.condition, other.condition) &&
            Objects.equal(this.isMultiVariationListing, other.isMultiVariationListing) &&
            Objects.equal(this.discountPriceInfo, other.discountPriceInfo) &&
            Objects.equal(this.pictureUrlSuperSize, other.pictureUrlSuperSize) &&
            Objects.equal(this.pictureUrlLarge, other.pictureUrlLarge) &&
            Objects.equal(this.unitPrice, other.unitPrice) &&
            Objects.equal(this.topRatedListing, other.topRatedListing);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.itemId == null ? 0 : this.itemId.hashCode());
        result = 31 * result + (this.title == null ? 0 : this.title.hashCode());
        result = 31 * result + (this.globalId == null ? 0 : this.globalId.hashCode());
        result = 31 * result + (this.subtitle == null ? 0 : this.subtitle.hashCode());
        result = 31 * result + (this.primaryCategory == null ? 0 : this.primaryCategory.hashCode());
        result = 31 * result + (this.secondaryCategory == null ? 0 : this.secondaryCategory.hashCode());
        result = 31 * result + (this.galleryUrl == null ? 0 : this.galleryUrl.hashCode());
        result = 31 * result + (this.galleryInfoContainer == null ? 0 : this.galleryInfoContainer.hashCode());
        result = 31 * result + (this.viewItemUrl == null ? 0 : this.viewItemUrl.hashCode());
        result = 31 * result + (this.charityId == null ? 0 : this.charityId.hashCode());
        result = 31 * result + (this.productId == null ? 0 : this.productId.hashCode());
        result = 31 * result + (this.paymentMethods == null ? 0 : this.paymentMethods.hashCode());
        result = 31 * result + (this.autoPay == null ? 0 : this.autoPay.hashCode());
        result = 31 * result + (this.postalCode == null ? 0 : this.postalCode.hashCode());
        result = 31 * result + (this.location == null ? 0 : this.location.hashCode());
        result = 31 * result + (this.country == null ? 0 : this.country.hashCode());
        result = 31 * result + (this.storeInfo == null ? 0 : this.storeInfo.hashCode());
        result = 31 * result + (this.sellerInfo == null ? 0 : this.sellerInfo.hashCode());
        result = 31 * result + (this.shippingInfo == null ? 0 : this.shippingInfo.hashCode());
        result = 31 * result + (this.sellingStatus == null ? 0 : this.sellingStatus.hashCode());
        result = 31 * result + (this.listingInfo == null ? 0 : this.listingInfo.hashCode());
        result = 31 * result + (this.returnsAccepted == null ? 0 : this.returnsAccepted.hashCode());
        result = 31 * result + (this.galleryPlusPictureUrls == null ? 0 : this.galleryPlusPictureUrls.hashCode());
        result = 31 * result + (this.compatibility == null ? 0 : this.compatibility.hashCode());
        result = 31 * result + (this.distance == null ? 0 : this.distance.hashCode());
        result = 31 * result + (this.condition == null ? 0 : this.condition.hashCode());
        result = 31 * result + (this.isMultiVariationListing == null ? 0 : this.isMultiVariationListing.hashCode());
        result = 31 * result + (this.discountPriceInfo == null ? 0 : this.discountPriceInfo.hashCode());
        result = 31 * result + (this.pictureUrlSuperSize == null ? 0 : this.pictureUrlSuperSize.hashCode());
        result = 31 * result + (this.pictureUrlLarge == null ? 0 : this.pictureUrlLarge.hashCode());
        result = 31 * result + (this.unitPrice == null ? 0 : this.unitPrice.hashCode());
        result = 31 * result + (this.topRatedListing == null ? 0 : this.topRatedListing.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("itemId", itemId)
            .add("title", title)
            .add("globalId", globalId)
            .add("subtitle", subtitle)
            .add("primaryCategory", primaryCategory)
            .add("secondaryCategory", secondaryCategory)
            .add("galleryUrl", galleryUrl)
            .add("galleryInfoContainer", galleryInfoContainer)
            .add("viewItemUrl", viewItemUrl)
            .add("charityId", charityId)
            .add("productId", productId)
            .add("paymentMethods", paymentMethods)
            .add("autoPay", autoPay)
            .add("postalCode", postalCode)
            .add("location", location)
            .add("country", country)
            .add("storeInfo", storeInfo)
            .add("sellerInfo", sellerInfo)
            .add("shippingInfo", shippingInfo)
            .add("sellingStatus", sellingStatus)
            .add("listingInfo", listingInfo)
            .add("returnsAccepted", returnsAccepted)
            .add("galleryPlusPictureUrls", galleryPlusPictureUrls)
            .add("compatibility", compatibility)
            .add("distance", distance)
            .add("condition", condition)
            .add("isMultiVariationListing", isMultiVariationListing)
            .add("discountPriceInfo", discountPriceInfo)
            .add("pictureUrlSuperSize", pictureUrlSuperSize)
            .add("pictureUrlLarge", pictureUrlLarge)
            .add("unitPrice", unitPrice)
            .add("topRatedListing", topRatedListing)
            .toString();
    }
}
