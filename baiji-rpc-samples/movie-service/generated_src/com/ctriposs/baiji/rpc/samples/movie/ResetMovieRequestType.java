package com.ctriposs.baiji.rpc.samples.movie;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class ResetMovieRequestType extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"ResetMovieRequestType\",\"namespace\":\"com.ctriposs.baiji.rpc.samples.movie\",\"doc\":null,\"fields\":[]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

}
