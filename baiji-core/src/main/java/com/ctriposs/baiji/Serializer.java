package com.ctriposs.baiji;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Serializer {

    /**
     * Serialize the given object into the stream.
     * @param obj
     * @param stream
     * @param <T>
     * @throws IOException
     */
    <T> void serialize(T obj, OutputStream stream) throws IOException;

    /**
     * Deserialize an object with the given type from the stream
     * @param objClass
     * @param stream
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T deserialize(Class<T> objClass, InputStream stream) throws IOException;
}
