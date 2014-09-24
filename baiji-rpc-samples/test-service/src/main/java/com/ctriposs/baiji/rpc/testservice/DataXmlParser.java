package com.ctriposs.baiji.rpc.testservice;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by yqdong on 2014/9/24.
 */
public final class DataXmlParser {

    private DataXmlParser() {
    }

    public static List<Item> parse(InputStream stream) throws Exception {
        if (stream == null) {
            return new ArrayList<Item>(0);
        }

        List<Item> items = new ArrayList<Item>();

        DocumentBuilder dbBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = dbBuilder.parse(stream);
        NodeList itemNodes = doc.getDocumentElement().getElementsByTagName("item");
        for (int i = 0; i < itemNodes.getLength(); ++i) {
            Item item = parseItem(itemNodes.item(i));
            if (item != null) {
                items.add(item);
            }
        }

        return items;
    }

    private static Item parseItem(Node node) {
        if (node == null) {
            return null;
        }
        Element element = (Element) node;
        Item item = new Item();
        item.itemId = parseString(element.getElementsByTagName("itemId").item(0));
        item.title = parseString(element.getElementsByTagName("title").item(0));
        item.globalId = parseString(element.getElementsByTagName("globalId").item(0));
        item.subtitle = parseString(element.getElementsByTagName("subtitle").item(0));
        item.primaryCategory = parseCategory(element.getElementsByTagName("primaryCategory").item(0));
        item.secondaryCategory = parseCategory(element.getElementsByTagName("secondaryCategory").item(0));
        item.galleryUrl = parseString(element.getElementsByTagName("galleryUrl").item(0));
        item.title = parseString(element.getElementsByTagName("title").item(0));

        item.galleryInfoContainer = new ArrayList<GalleryUrl>();
        NodeList nodes = element.getElementsByTagName("galleryInfoContainer");
        for (int i = 0; i < nodes.getLength(); ++i) {
            GalleryUrl url = parseGalleryUrl(nodes.item(i));
            if (url != null) {
                item.galleryInfoContainer.add(url);
            }
        }

        item.viewItemUrl = parseString(element.getElementsByTagName("viewItemUrl").item(0));
        item.charityId = parseString(element.getElementsByTagName("charityId").item(0));
        item.productId = parseProductId(element.getElementsByTagName("productId").item(0));
        item.productId = parseProductId(element.getElementsByTagName("productId").item(0));

        item.paymentMethods = new ArrayList<String>();
        nodes = element.getElementsByTagName("paymentMethods");
        for (int i = 0; i < nodes.getLength(); ++i) {
            String method = parseString(nodes.item(i));
            if (method != null) {
                item.paymentMethods.add(method);
            }
        }

        item.autoPay = parseBoolean(element.getElementsByTagName("autoPay").item(0));
        item.postalCode = parseString(element.getElementsByTagName("postalCode").item(0));
        item.location = parseString(element.getElementsByTagName("location").item(0));
        item.country = parseString(element.getElementsByTagName("country").item(0));
        item.storeInfo = parseStoreFront(element.getElementsByTagName("storeInfo").item(0));
        item.sellerInfo = parseSellerInfo(element.getElementsByTagName("sellerInfo").item(0));
        item.shippingInfo = parseShippingInfo(element.getElementsByTagName("shippingInfo").item(0));
        item.sellingStatus = parseSellingStatus(element.getElementsByTagName("sellingStatus").item(0));
        item.listingInfo = parseListingInfo(element.getElementsByTagName("listingInfo").item(0));
        item.returnsAccepted = parseBoolean(element.getElementsByTagName("returnsAccepted").item(0));

        item.galleryPlusPictureUrls = new ArrayList<String>();
        nodes = element.getElementsByTagName("galleryPlusPictureUrls");
        for (int i = 0; i < nodes.getLength(); ++i) {
            String url = parseString(nodes.item(i));
            if (url != null) {
                item.galleryPlusPictureUrls.add(url);
            }
        }

        item.compatibility = parseString(element.getElementsByTagName("compatibility").item(0));
        item.distance = parseDistance(element.getElementsByTagName("distance").item(0));
        item.condition = parseCondition(element.getElementsByTagName("condition").item(0));
        item.isMultiVariationListing = parseBoolean(element.getElementsByTagName("isMultiVariationListing").item(0));
        item.discountPriceInfo = parseDiscountPriceInfo(element.getElementsByTagName("discountPriceInfo").item(0));
        item.pictureUrlSuperSize = parseString(element.getElementsByTagName("pictureUrlSuperSize").item(0));
        item.pictureUrlLarge = parseString(element.getElementsByTagName("pictureUrlLarge").item(0));
        item.unitPrice = parseUnitPriceInfo(element.getElementsByTagName("unitPrice").item(0));
        item.topRatedListing = parseBoolean(element.getElementsByTagName("topRatedListing").item(0));
        return item;
    }

    private static Category parseCategory(Node node) {
        if (node == null) {
            return null;
        }
        Element element = (Element) node;
        Category category = new Category();
        category.categoryId = parseString(element.getElementsByTagName("categoryId").item(0));
        category.categoryName = parseString(element.getElementsByTagName("categoryName").item(0));
        return category;
    }

    private static GalleryUrl parseGalleryUrl(Node node) {
        if (node == null) {
            return null;
        }
        Element element = (Element) node;
        GalleryUrl url = new GalleryUrl();
        url.gallerySize = parseEnum(GallerySize.class, element.getAttribute("gallerySize"));
        url.value = element.getTextContent();
        return url;
    }

    private static ProductId parseProductId(Node node) {
        if (node == null) {
            return null;
        }
        Element element = (Element) node;
        ProductId id = new ProductId();
        id.type = element.getAttribute("type");
        id.value = element.getTextContent();
        return id;
    }

    private static StoreFront parseStoreFront(Node node) {
        if (node == null) {
            return null;
        }
        Element element = (Element) node;
        StoreFront storeFront = new StoreFront();
        storeFront.storeName = parseString(element.getElementsByTagName("storeName").item(0));
        storeFront.storeUrl = parseString(element.getElementsByTagName("storeUrl").item(0));
        return storeFront;
    }

    private static SellerInfo parseSellerInfo(Node node) {
        if (node == null) {
            return null;
        }
        Element element = (Element) node;
        SellerInfo sellerInfo = new SellerInfo();
        sellerInfo.sellerUserName = parseString(element.getElementsByTagName("sellerUserName").item(0));
        sellerInfo.feedbackScore = parseLong(element.getElementsByTagName("feedbackScore").item(0));
        sellerInfo.positiveFeedbackPercent = parseDouble(element.getElementsByTagName("positiveFeedbackPercent").item(0));
        sellerInfo.feedbackRatingStar = parseString(element.getElementsByTagName("feedbackRatingStar").item(0));
        sellerInfo.topRatedSeller = parseBoolean(element.getElementsByTagName("topRatedSeller").item(0));
        return sellerInfo;
    }

    private static ShippingInfo parseShippingInfo(Node node) {
        if (node == null) {
            return null;
        }
        Element element = (Element) node;
        ShippingInfo shippingInfo = new ShippingInfo();
        shippingInfo.shippingServiceCost = parseAmount(element.getElementsByTagName("shippingServiceCost").item(0));
        shippingInfo.shippingType = parseString(element.getElementsByTagName("shippingType").item(0));
        shippingInfo.shipToLocations = new ArrayList<String>();
        NodeList shipToLocationsNodes = element.getElementsByTagName("shipToLocations");
        for (int i = 0; i < shipToLocationsNodes.getLength(); ++i) {
            shippingInfo.shipToLocations.add(parseString(shipToLocationsNodes.item(i)));
        }
        shippingInfo.expeditedShipping = parseBoolean(element.getElementsByTagName("expeditedShipping").item(0));
        shippingInfo.oneDayShippingAvailable = parseBoolean(element.getElementsByTagName("oneDayShippingAvailable").item(0));
        shippingInfo.handlingTime = parseInt(element.getElementsByTagName("handlingTime").item(0));
        shippingInfo.intermediatedShipping = parseBoolean(element.getElementsByTagName("intermediatedShipping").item(0));
        return shippingInfo;
    }

    private static SellingStatus parseSellingStatus(Node node) {
        if (node == null) {
            return null;
        }
        Element element = (Element) node;
        SellingStatus sellingStatus = new SellingStatus();
        sellingStatus.currentPrice = parseAmount(element.getElementsByTagName("currentPrice").item(0));
        sellingStatus.convertedCurrentPrice = parseAmount(element.getElementsByTagName("convertedCurrentPrice").item(0));
        sellingStatus.bidCount = parseInt(element.getElementsByTagName("bidCount").item(0));
        sellingStatus.sellingState = parseString(element.getElementsByTagName("sellingState").item(0));
        sellingStatus.timeLeft = parseString(element.getElementsByTagName("timeLeft").item(0));
        return sellingStatus;
    }

    private static ListingInfo parseListingInfo(Node node) {
        if (node == null) {
            return null;
        }
        Element element = (Element) node;
        ListingInfo listingInfo = new ListingInfo();
        listingInfo.bestOfferEnabled = parseBoolean(element.getElementsByTagName("bestOfferEnabled").item(0));
        listingInfo.buyItNowAvailable = parseBoolean(element.getElementsByTagName("buyItNowAvailable").item(0));
        listingInfo.buyItNowPrice = parseAmount(element.getElementsByTagName("buyItNowPrice").item(0));
        listingInfo.convertedBuyItNowPrice = parseAmount(element.getElementsByTagName("convertedBuyItNowPrice").item(0));
        listingInfo.startTime = parseDateTime(element.getElementsByTagName("startTime").item(0));
        listingInfo.endTime = parseDateTime(element.getElementsByTagName("endTime").item(0));
        listingInfo.listingType = parseString(element.getElementsByTagName("listingType").item(0));
        listingInfo.gift = parseBoolean(element.getElementsByTagName("gift").item(0));
        return listingInfo;
    }

    private static Distance parseDistance(Node node) {
        if (node == null) {
            return null;
        }
        Element element = (Element) node;
        Distance distance = new Distance();
        distance.unit = element.getAttribute("unit");
        distance.value = parseDouble(element);
        return distance;
    }

    private static Condition parseCondition(Node node) {
        if (node == null) {
            return null;
        }
        Element element = (Element) node;
        Condition condition = new Condition();
        condition.conditionId = parseInt(element.getElementsByTagName("conditionId").item(0));
        condition.conditionDisplayName = parseString(element.getElementsByTagName("conditionDisplayName").item(0));
        return condition;
    }

    private static DiscountPriceInfo parseDiscountPriceInfo(Node node) {
        if (node == null) {
            return null;
        }
        Element element = (Element) node;
        DiscountPriceInfo info = new DiscountPriceInfo();
        info.originalRetailPrice = parseAmount(element.getElementsByTagName("originalRetailPrice").item(0));
        info.minimunAdvertisedPriceExposure = parseEnum(MapExposure.class,
                element.getElementsByTagName("minimunAdvertisedPriceExposure").item(0));
        info.pricingTreatment = parseEnum(PriceTreatment.class,
                element.getElementsByTagName("pricingTreatment").item(0));
        info.soldOnEbay = parseBoolean(element.getElementsByTagName("soldOnEbay").item(0));
        info.soldOffEbay = parseBoolean(element.getElementsByTagName("soldOffEbay").item(0));
        return info;
    }


    private static UnitPriceInfo parseUnitPriceInfo(Node node) {
        if (node == null) {
            return null;
        }
        Element element = (Element) node;
        UnitPriceInfo info = new UnitPriceInfo();
        info.type = parseString(element.getElementsByTagName("type").item(0));
        info.quantity = parseDouble(element.getElementsByTagName("quantity").item(0));
        return info;
    }

    private static Amount parseAmount(Node node) {
        if (node == null) {
            return null;
        }
        Element element = (Element) node;
        Amount amount = new Amount();
        amount.currencyId = element.getAttribute("currencyId");
        amount.value = parseDouble(element);
        return amount;
    }

    private static String parseString(Node node) {
        return node != null ? node.getTextContent() : null;
    }

    private static Boolean parseBoolean(Node node) {
        return Boolean.valueOf(parseString(node));
    }

    private static Integer parseInt(Node node) {
        String text = parseString(node);
        return text != null ? Integer.valueOf(text) : null;
    }

    private static Long parseLong(Node node) {
        String text = parseString(node);
        return text != null ? Long.valueOf(text) : null;
    }

    private static Double parseDouble(Node node) {
        String text = parseString(node);
        return text != null ? Double.valueOf(text) : null;
    }

    private static <T extends Enum<T>> T parseEnum(Class<T> enumType,
                                                   Node node) {
        return parseEnum(enumType, parseString(node));
    }

    private static <T extends Enum<T>> T parseEnum(Class<T> enumType,
                                                   String value) {
        return value != null ? Enum.valueOf(enumType, value.toUpperCase()) : null;
    }

    private static Calendar parseDateTime(Node node) {
        String text = parseString(node);
        return text != null ? DatatypeConverter.parseDateTime(text) : null;
    }
}
