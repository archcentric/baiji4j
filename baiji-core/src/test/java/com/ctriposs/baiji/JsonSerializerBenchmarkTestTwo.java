package com.ctriposs.baiji;

import com.ctriposs.baiji.specific.SimpleRecord;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonSerializerBenchmarkTestTwo {

    private JsonStreamSerializer serializer = new JsonStreamSerializer();
    private ObjectMapper objectMapper = new ObjectMapper();
    private int loop = 100000;

    public static void main(String[] args) throws Exception {
        JsonSerializerBenchmarkTestTwo test = new JsonSerializerBenchmarkTestTwo();
        test.testJsonSerializerBenchmark();
        test.testJacksonSerializerBenchmark();
    }

    public void testJsonSerializerBenchmark() throws Exception {
        intBenchmark();
    }

    public void testJacksonSerializerBenchmark() {
        jacksonBenchmark();
    }

    private void intBenchmark() {
        serializer.clearCache();
        singleFieldBenchmark(42, "\"int\"");
    }

    private void singleFieldBenchmark(Object fieldValue, String fieldType) {
        SimpleRecord simpleRecord = new SimpleRecord();
        simpleRecord.put(0, 42);

        OutputStream os = new ByteArrayOutputStream();

        List<Long> deserializeTimeList = new ArrayList<>();
        for (int i = 0; i < loop; i++) {
            try {
                serializer.serialize(simpleRecord, os);

                byte[] bytes = ((ByteArrayOutputStream) os).toByteArray();
                ((ByteArrayOutputStream) os).reset();

                InputStream is = new ByteArrayInputStream(bytes);
                long start = System.nanoTime();
                serializer.deserialize(SimpleRecord.class, is);
                deserializeTimeList.add(System.nanoTime() - start);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        printResults(deserializeTimeList);
    }

    private void jacksonBenchmark() {
        SimpleRecord simpleRecord = new SimpleRecord();
        simpleRecord.put(0, 42);

        OutputStream os = new ByteArrayOutputStream();

        List<Long> deserializeTimeList = new ArrayList<>();
        for (int i = 0; i < loop; i++) {
            try {
                objectMapper.writeValue(os, simpleRecord);

                byte[] bytes = ((ByteArrayOutputStream) os).toByteArray();
                ((ByteArrayOutputStream) os).reset();

                InputStream is = new ByteArrayInputStream(bytes);
                long start = System.nanoTime();
                objectMapper.readValue(is, SimpleRecord.class);
                deserializeTimeList.add(System.nanoTime() - start);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        printResults(deserializeTimeList);
    }

    private void printResults(List<Long> timeList) {
        long sum = 0;
        for (long l : timeList) {
            sum += l;
        }

        System.out.println("Result: " + (sum/(1000*timeList.size())) + " pus/op");
    }
}
