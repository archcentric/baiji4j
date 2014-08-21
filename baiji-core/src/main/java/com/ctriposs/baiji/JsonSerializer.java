package com.ctriposs.baiji;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JsonSerializer implements Serializer {

    @Override
    public <T> void serialize(T obj, OutputStream stream) throws IOException {

    }

    @Override
    public <T> T deserialize(Class<T> objClass, InputStream stream) throws IOException {
        return null;
    }
}
