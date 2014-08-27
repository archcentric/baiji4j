package com.ctriposs.baiji.rpc.common.types;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

/**
 * Shows the pagination data for the item search.
 * Child elements include the page number returned, the maximum entries returned per page,
 * the total number of pages that can be returned, and the total number of items that match the search criteria.
 */
@SuppressWarnings("all")
public class PaginationOutputType extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"PaginationOutputType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"pageNumber\",\"type\":[\"int\",\"null\"]},{\"name\":\"entriesPerPage\",\"type\":[\"int\",\"null\"]},{\"name\":\"totalPages\",\"type\":[\"int\",\"null\"]},{\"name\":\"totalEntries\",\"type\":[\"int\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public PaginationOutputType(
        Integer pageNumber,
        Integer entriesPerPage,
        Integer totalPages,
        Integer totalEntries
    ) {
        this.pageNumber = pageNumber;
        this.entriesPerPage = entriesPerPage;
        this.totalPages = totalPages;
        this.totalEntries = totalEntries;
    }

    public PaginationOutputType() {
    }

    /**
     * The subset of item data returned in the current response.
     * Search results are divided into sets, or "pages," of item data.
     * The number of pages is equal to the total number of items matching the search criteria
     * divided by the value specified for entriesPerPage in the request.
     * The response for a request contains one "page" of item data.
     * This returned value indicates the page number of item data returned (a subset of the complete result set).
     * If this field contains 1, the response contains the first page of item data (the default).
     * If the value returned in totalEntries is less than the value for entriesPerPage,
     * pageNumber returns 1 and the response contains the entire result set.
     * The value of pageNumber is normally equal to the value input for paginationInput.pageNumber.
     * However, if the number input for pageNumber is greater than the total possible pages of output,
     * Baiji returns the last page of item data in the result set, and the value for pageNumber is set to
     * the respective (last) page number.
     */
    public Integer pageNumber;


    /**
     * The maximum number of items that can be returned in the response.
     * This number is always equal to the value input for paginationInput.entriesPerPage.
     * The end of the result set has been reached if the number specified for entriesPerPage is greater than
     * the number of items found on the specified pageNumber.
     * In this case, there will be fewer items returned than the number specified in entriesPerPage.
     * This can be determined by comparing the entriesPerPage value with the value returned
     * in the count attribute for the searchResult field.
     */
    public Integer entriesPerPage;


    /**
     * The total number of pages of data that could be returned by repeated search requests.
     * Note that if you modify the value of inputPagination.entriesPerPage in a request,
     * the value output for totalPages will change.
     * A value of "0" is returned if service does not find any items that match the search criteria.
     */
    public Integer totalPages;


    /**
     * The total number of items found that match the search criteria in your request.
     * Depending on the input value for entriesPerPage, the response might include only a portion (a page) of the entire result set.
     * A value of "0" is returned if service does not find any items that match the search criteria.
     */
    public Integer totalEntries;

    /**
     * The subset of item data returned in the current response.
     * Search results are divided into sets, or "pages," of item data.
     * The number of pages is equal to the total number of items matching the search criteria
     * divided by the value specified for entriesPerPage in the request.
     * The response for a request contains one "page" of item data.
     * This returned value indicates the page number of item data returned (a subset of the complete result set).
     * If this field contains 1, the response contains the first page of item data (the default).
     * If the value returned in totalEntries is less than the value for entriesPerPage,
     * pageNumber returns 1 and the response contains the entire result set.
     * The value of pageNumber is normally equal to the value input for paginationInput.pageNumber.
     * However, if the number input for pageNumber is greater than the total possible pages of output,
     * Baiji returns the last page of item data in the result set, and the value for pageNumber is set to
     * the respective (last) page number.
     */
    public Integer getPageNumber() {
        return pageNumber;
    }

    /**
     * The subset of item data returned in the current response.
     * Search results are divided into sets, or "pages," of item data.
     * The number of pages is equal to the total number of items matching the search criteria
     * divided by the value specified for entriesPerPage in the request.
     * The response for a request contains one "page" of item data.
     * This returned value indicates the page number of item data returned (a subset of the complete result set).
     * If this field contains 1, the response contains the first page of item data (the default).
     * If the value returned in totalEntries is less than the value for entriesPerPage,
     * pageNumber returns 1 and the response contains the entire result set.
     * The value of pageNumber is normally equal to the value input for paginationInput.pageNumber.
     * However, if the number input for pageNumber is greater than the total possible pages of output,
     * Baiji returns the last page of item data in the result set, and the value for pageNumber is set to
     * the respective (last) page number.
     */
    public void setPageNumber(final Integer pageNumber) {
        this.pageNumber = pageNumber;
    }


    /**
     * The maximum number of items that can be returned in the response.
     * This number is always equal to the value input for paginationInput.entriesPerPage.
     * The end of the result set has been reached if the number specified for entriesPerPage is greater than
     * the number of items found on the specified pageNumber.
     * In this case, there will be fewer items returned than the number specified in entriesPerPage.
     * This can be determined by comparing the entriesPerPage value with the value returned
     * in the count attribute for the searchResult field.
     */
    public Integer getEntriesPerPage() {
        return entriesPerPage;
    }

    /**
     * The maximum number of items that can be returned in the response.
     * This number is always equal to the value input for paginationInput.entriesPerPage.
     * The end of the result set has been reached if the number specified for entriesPerPage is greater than
     * the number of items found on the specified pageNumber.
     * In this case, there will be fewer items returned than the number specified in entriesPerPage.
     * This can be determined by comparing the entriesPerPage value with the value returned
     * in the count attribute for the searchResult field.
     */
    public void setEntriesPerPage(final Integer entriesPerPage) {
        this.entriesPerPage = entriesPerPage;
    }


    /**
     * The total number of pages of data that could be returned by repeated search requests.
     * Note that if you modify the value of inputPagination.entriesPerPage in a request,
     * the value output for totalPages will change.
     * A value of "0" is returned if service does not find any items that match the search criteria.
     */
    public Integer getTotalPages() {
        return totalPages;
    }

    /**
     * The total number of pages of data that could be returned by repeated search requests.
     * Note that if you modify the value of inputPagination.entriesPerPage in a request,
     * the value output for totalPages will change.
     * A value of "0" is returned if service does not find any items that match the search criteria.
     */
    public void setTotalPages(final Integer totalPages) {
        this.totalPages = totalPages;
    }


    /**
     * The total number of items found that match the search criteria in your request.
     * Depending on the input value for entriesPerPage, the response might include only a portion (a page) of the entire result set.
     * A value of "0" is returned if service does not find any items that match the search criteria.
     */
    public Integer getTotalEntries() {
        return totalEntries;
    }

    /**
     * The total number of items found that match the search criteria in your request.
     * Depending on the input value for entriesPerPage, the response might include only a portion (a page) of the entire result set.
     * A value of "0" is returned if service does not find any items that match the search criteria.
     */
    public void setTotalEntries(final Integer totalEntries) {
        this.totalEntries = totalEntries;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.pageNumber;
            case 1: return this.entriesPerPage;
            case 2: return this.totalPages;
            case 3: return this.totalEntries;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.pageNumber = (Integer)fieldValue; break;
            case 1: this.entriesPerPage = (Integer)fieldValue; break;
            case 2: this.totalPages = (Integer)fieldValue; break;
            case 3: this.totalEntries = (Integer)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final PaginationOutputType other = (PaginationOutputType)obj;
        return 
            Objects.equal(this.pageNumber, other.pageNumber) &&
            Objects.equal(this.entriesPerPage, other.entriesPerPage) &&
            Objects.equal(this.totalPages, other.totalPages) &&
            Objects.equal(this.totalEntries, other.totalEntries);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.pageNumber == null ? 0 : this.pageNumber.hashCode());
        result = 31 * result + (this.entriesPerPage == null ? 0 : this.entriesPerPage.hashCode());
        result = 31 * result + (this.totalPages == null ? 0 : this.totalPages.hashCode());
        result = 31 * result + (this.totalEntries == null ? 0 : this.totalEntries.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("pageNumber", pageNumber)
            .add("entriesPerPage", entriesPerPage)
            .add("totalPages", totalPages)
            .add("totalEntries", totalEntries)
            .toString();
    }
}
