package com.ctriposs.baiji;

import com.ctriposs.baiji.specific.SimpleRecord;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;

public class JsonSerializerBenchmarkTestTwo {

    private JsonSerializer serializer = new JsonSerializer();
    private ObjectMapper objectMapper = new ObjectMapper();
    private int loop = 500000;

    public static void main(String[] args) throws Exception {
        JsonSerializerBenchmarkTestTwo test = new JsonSerializerBenchmarkTestTwo();
        test.testJsonSerializerBenchmark();
    }

    public void testJsonSerializerBenchmark() throws Exception {
        intBenchmark();
    }

    public void testJacksonSerializerBenchmark() {
        jacksonIntBenchmark();
    }

    private void intBenchmark() {
        serializer.clearCache();
        long start = System.nanoTime();
        singleFieldBenchmark(42, "\"int\"");
        long end = System.nanoTime();
        System.out.println("Used time: " + (end - start)/1000000);
    }

    private void jacksonIntBenchmark() {
        long start = System.nanoTime();
        jacksonBenchmark();
        long end = System.nanoTime();
        System.out.println("Used time: " + (end - start)/1000000);
    }

    private void singleFieldBenchmark(Object fieldValue, String fieldType) {
        SimpleRecord simpleRecord = new SimpleRecord();
        simpleRecord.put(0, 42);

        OutputStream os = new ByteArrayOutputStream();

        for (int i = 0; i < loop; i++) {
            try {
                serializer.serialize(simpleRecord, os);

                byte[] bytes = ((ByteArrayOutputStream) os).toByteArray();
                ((ByteArrayOutputStream) os).reset();

                //InputStream is = new ByteArrayInputStream(bytes);
                //serializer.deserialize(GenericBenchmarkRecord.class, is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void jacksonBenchmark() {
        SimpleRecord simpleRecord = new SimpleRecord();
        simpleRecord.put(0, 42);

        OutputStream os = new ByteArrayOutputStream();

        for (int i = 0; i < loop; i++) {
            try {
                objectMapper.writeValue(os, simpleRecord);

                byte[] bytes = ((ByteArrayOutputStream) os).toByteArray();
                ((ByteArrayOutputStream) os).reset();

                //InputStream is = new ByteArrayInputStream(bytes);
                //objectMapper.readValue(is, GenericBenchmarkRecord.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
