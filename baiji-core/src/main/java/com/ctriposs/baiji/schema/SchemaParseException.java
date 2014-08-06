package com.ctriposs.baiji.schema;

import com.ctriposs.baiji.exception.BaijiRuntimeException;

/**
 * Thrown for errors parsing schemas.
 */
public class SchemaParseException extends BaijiRuntimeException {
    public SchemaParseException(Throwable cause) {
        super(cause);
    }

    public SchemaParseException(String message) {
        super(message);
    }
}

