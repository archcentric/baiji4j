package com.ctriposs.baiji.io;

import com.ctriposs.baiji.schema.Schema;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;

import java.io.IOException;
import java.io.InputStream;

public class JsonDecoder implements Decoder {

    private JsonParser in;
    private static JsonFactory jsonFactory = new JsonFactory();

    private Schema schema;

    public JsonDecoder(InputStream in) {

    }

    public JsonDecoder(Schema schema, String in) throws IOException {
        this.schema = schema;
        init(in);
    }

    public JsonDecoder(Schema schema, InputStream in) throws IOException {
        this.schema = schema;
        init(in);
    }

    private void init(InputStream in) throws IOException {
        this.in = jsonFactory.createJsonParser(in);
        this.in.nextToken();
    }

    private void init(String in) throws IOException {
        this.in = jsonFactory.createJsonParser(in);
        this.in.nextToken();
    }

    @Override
    public void readNull() throws IOException {
    }

    @Override
    public boolean readBoolean() throws IOException {
        return false;
    }

    @Override
    public int readInt() throws IOException {
        return 0;
    }

    @Override
    public long readLong() throws IOException {
        return 0;
    }

    @Override
    public float readFloat() throws IOException {
        return 0;
    }

    @Override
    public double readDouble() throws IOException {
        return 0;
    }

    @Override
    public byte[] readBytes() throws IOException {
        return new byte[0];
    }

    @Override
    public String readString() throws IOException {
        return null;
    }

    @Override
    public int readEnum() throws IOException {
        return 0;
    }

    @Override
    public long readArrayStart() throws IOException {
        return 0;
    }

    @Override
    public long readArrayNext() throws IOException {
        return 0;
    }

    @Override
    public long readMapStart() throws IOException {
        return 0;
    }

    @Override
    public long readMapNext() throws IOException {
        return 0;
    }

    @Override
    public int readUnionIndex() throws IOException {
        return 0;
    }
}
