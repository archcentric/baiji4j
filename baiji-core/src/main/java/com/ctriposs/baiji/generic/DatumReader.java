package com.ctriposs.baiji.generic;

import com.ctriposs.baiji.io.Decoder;
import com.ctriposs.baiji.schema.Schema;

import java.io.IOException;

public interface DatumReader<D> {

    Schema getSchema();

    /**
     * Read a datum.
     * Traverse the schema, depth-first, reading all leaf values
     * in the schema into a datum that is returned.  If the provided datum is
     * non-null it may be reused and returned.
     */
    D read(D reuse, Decoder in) throws IOException;
}

