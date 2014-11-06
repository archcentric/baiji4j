package com.ctriposs.baiji;

import com.ctriposs.baiji.specific.SimpleRecord;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonSerializerBenchmarkTestTwo {

    private JsonStreamSerializer serializer = new JsonStreamSerializer();
    private ObjectMapper objectMapper = new ObjectMapper();
    private int loop = 5000000;

    public static void main(String[] args) throws Exception {
        JsonSerializerBenchmarkTestTwo test = new JsonSerializerBenchmarkTestTwo();
        Thread.sleep(10 * 1000);
        test.testJsonSerializerBenchmark();
    }

    public void testJsonSerializerBenchmark() throws Exception {
        intBenchmark();
    }

    public void testJacksonSerializerBenchmark() {
        jacksonBenchmark();
    }

    private void intBenchmark() throws IOException {
        singleFieldBenchmark();
    }

    private void singleFieldBenchmark() throws IOException {
        SimpleRecord simpleRecord = new SimpleRecord();
        simpleRecord.put(0, 42);

        OutputStream os = new ByteArrayOutputStream();
        serializer.serialize(simpleRecord, os);
        byte[] bytes = ((ByteArrayOutputStream) os).toByteArray();
        ((ByteArrayOutputStream) os).reset();

        for (int i = 0; i < loop; i++) {
            InputStream is = new ByteArrayInputStream(bytes);
            serializer.deserialize(SimpleRecord.class, is);
        }
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
