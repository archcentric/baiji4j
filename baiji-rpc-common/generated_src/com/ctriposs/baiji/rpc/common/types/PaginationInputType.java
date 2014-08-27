package com.ctriposs.baiji.rpc.common.types;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

/**
 * Controls the pagination of the result set.
 * Child elements specify the maximum number of items to return per call and which page of data to return.
 * Controls which items are returned in the response, but does not control the details associated with the returned items.
 */
@SuppressWarnings("all")
public class PaginationInputType extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"PaginationInputType\",\"namespace\":\"com.ctriposs.baiji.rpc.common.types\",\"doc\":null,\"fields\":[{\"name\":\"pageNumber\",\"type\":[\"int\",\"null\"]},{\"name\":\"entriesPerPage\",\"type\":[\"int\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public PaginationInputType(
        Integer pageNumber,
        Integer entriesPerPage
    ) {
        this.pageNumber = pageNumber;
        this.entriesPerPage = entriesPerPage;
    }

    public PaginationInputType() {
    }

    /**
     * Specifies which subset of data (or "page") to return in the call response.
     * The number of data pages is determined by the total number of items matching
     * the request search criteria (returned in paginationOutput.totalEntries)
     * divided by the number of entries to display in each response (entriesPerPage).
     * You can return max number of pages of the result set by issuing multiple requests
     * and specifying, in sequence, the pages to return.
     * Specify a positive value equal to or lower than the number of pages available
     * (which you determine by examining the results of your initial request).
     */
    public Integer pageNumber;


    /**
     * Specifies the maximum number of entries to return in a single call.
     * If the number of entries found on the specified pageNumber is less than the value specified here,
     * the number of items returned will be less than the value of entriesPerPage. This indicates the end of the result set.
     */
    public Integer entriesPerPage;

    /**
     * Specifies which subset of data (or "page") to return in the call response.
     * The number of data pages is determined by the total number of items matching
     * the request search criteria (returned in paginationOutput.totalEntries)
     * divided by the number of entries to display in each response (entriesPerPage).
     * You can return max number of pages of the result set by issuing multiple requests
     * and specifying, in sequence, the pages to return.
     * Specify a positive value equal to or lower than the number of pages available
     * (which you determine by examining the results of your initial request).
     */
    public Integer getPageNumber() {
        return pageNumber;
    }

    /**
     * Specifies which subset of data (or "page") to return in the call response.
     * The number of data pages is determined by the total number of items matching
     * the request search criteria (returned in paginationOutput.totalEntries)
     * divided by the number of entries to display in each response (entriesPerPage).
     * You can return max number of pages of the result set by issuing multiple requests
     * and specifying, in sequence, the pages to return.
     * Specify a positive value equal to or lower than the number of pages available
     * (which you determine by examining the results of your initial request).
     */
    public void setPageNumber(final Integer pageNumber) {
        this.pageNumber = pageNumber;
    }


    /**
     * Specifies the maximum number of entries to return in a single call.
     * If the number of entries found on the specified pageNumber is less than the value specified here,
     * the number of items returned will be less than the value of entriesPerPage. This indicates the end of the result set.
     */
    public Integer getEntriesPerPage() {
        return entriesPerPage;
    }

    /**
     * Specifies the maximum number of entries to return in a single call.
     * If the number of entries found on the specified pageNumber is less than the value specified here,
     * the number of items returned will be less than the value of entriesPerPage. This indicates the end of the result set.
     */
    public void setEntriesPerPage(final Integer entriesPerPage) {
        this.entriesPerPage = entriesPerPage;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.pageNumber;
            case 1: return this.entriesPerPage;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.pageNumber = (Integer)fieldValue; break;
            case 1: this.entriesPerPage = (Integer)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final PaginationInputType other = (PaginationInputType)obj;
        return 
            Objects.equal(this.pageNumber, other.pageNumber) &&
            Objects.equal(this.entriesPerPage, other.entriesPerPage);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.pageNumber == null ? 0 : this.pageNumber.hashCode());
        result = 31 * result + (this.entriesPerPage == null ? 0 : this.entriesPerPage.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("pageNumber", pageNumber)
            .add("entriesPerPage", entriesPerPage)
            .toString();
    }
}
