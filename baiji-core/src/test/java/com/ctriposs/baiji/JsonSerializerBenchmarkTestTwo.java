package com.ctriposs.baiji;

import com.ctriposs.baiji.generic.GenericBenchmarkRecord;
import com.ctriposs.baiji.specific.Enum1Values;
import com.ctriposs.baiji.specific.Enum2Values;
import com.ctriposs.baiji.specific.ModelFilling2;
import com.ctriposs.baiji.specific.SimpleRecord;
import com.google.common.collect.Lists;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

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
        //jacksonIntBenchmark();
        /*booleanBenchmark();
        longBenchmark();
        doubleBenchmark();
        stringBenchmark();
        bytesBenchmark();
        enumBenchmark();
        arrayBenchmark();
        mapBenchmark();
        recordBenchmark();*/
    }

    public void testJacksonSerializerBenchmark() {

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

    private void booleanBenchmark() {
        serializer.clearCache();
        singleFieldBenchmark(true, "\"boolean\"");
    }

    private void longBenchmark() {
        serializer.clearCache();
        singleFieldBenchmark(1024 * 1024 * 16L, "\"long\"");
    }

    private void doubleBenchmark() {
        serializer.clearCache();
        singleFieldBenchmark(24.00000001, "\"double\"");
    }

    private void stringBenchmark() {
        serializer.clearCache();
        singleFieldBenchmark("testString", "\"string\"");
    }

    private void bytesBenchmark() {
        serializer.clearCache();
        singleFieldBenchmark("testBytes".getBytes(), "\"bytes\"");
    }

    private void enumBenchmark() {
        serializer.clearCache();
        singleFieldBenchmark(Enum1Values.RED, "{\"type\":\"enum\",\"name\":\"Enum1Values\",\"namespace\":\"com.ctriposs.baiji.specific\",\"doc\":null,\"symbols\":[\"BLUE\",\"RED\",\"GREEN\"]}");
    }

    private void arrayBenchmark() {
        serializer.clearCache();
        singleFieldBenchmark(Lists.newArrayList(1, 2, 3, 4, 5), "{\"type\":\"array\",\"items\":\"int\"}");

    }

    private void mapBenchmark() {
        serializer.clearCache();
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("1a", 1);
        map.put("2b", 2);
        map.put("3c", 3);
        singleFieldBenchmark(map, "{\"type\":\"map\",\"values\":\"int\"}");
    }

    private void recordBenchmark() {
        serializer.clearCache();
        ModelFilling2 record = new ModelFilling2(1024 * 1024 * 16L, "testRecord", Lists.newArrayList("a", "b", "c"), Enum2Values.BIKE);
        singleFieldBenchmark(record, record.getSchema().toString());
    }

    private void singleFieldBenchmark(Object fieldValue, String fieldType) {
        GenericBenchmarkRecord.recordType = fieldType;
        GenericBenchmarkRecord benchmarkRecord = new GenericBenchmarkRecord();
        benchmarkRecord.put(0, fieldValue);

        SimpleRecord simpleRecord = new SimpleRecord();
        simpleRecord.put(0, 42);

        OutputStream os = new ByteArrayOutputStream();

        for (int i = 0; i < loop; i++) {
            try {
                serializer.serialize(benchmarkRecord, os);

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
        GenericBenchmarkRecord.recordType = "\"int\"";
        GenericBenchmarkRecord benchmarkRecord = new GenericBenchmarkRecord();
        benchmarkRecord.put(0, 42);

        SimpleRecord simpleRecord = new SimpleRecord();
        simpleRecord.put(0, 42);

        OutputStream os = new ByteArrayOutputStream();

        for (int i = 0; i < loop; i++) {
            try {
                objectMapper.writeValue(os, benchmarkRecord);

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
