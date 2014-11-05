package com.ctriposs.baiji;

import com.ctriposs.baiji.schema.Schema;
import com.ctriposs.baiji.specific.*;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class JsonSerializerBenchmarkTestFour {

    public static void main(String[] args) throws IOException {
        JsonSerializerBenchmarkTestFour testFour = new JsonSerializerBenchmarkTestFour();
        testFour.testComplexJacksonSerialize();
    }

    private void testJsonSerialize() {
        SpecificJsonWriter jsonWriter = new SpecificJsonWriter<SimpleRecord>();

        SimpleRecord simpleRecord = new SimpleRecord();
        simpleRecord.put(0, 10001);

        OutputStream os = new ByteArrayOutputStream();
        Schema schema = simpleRecord.getSchema();

        long start = System.nanoTime();
        for (int i = 0; i < 50000; i++) {
            jsonWriter.write(schema, simpleRecord, os);
        }
        long end = System.nanoTime();
        System.out.println("Used time: " + (end - start)/1000000);

        ((ByteArrayOutputStream) os).reset();
    }

    private void testComplexJsonSerialize() {
        SpecificJsonWriter jsonWriter = new SpecificJsonWriter<TestSerializerSample>();

        Record2 record2 = new Record2();

        record2.bigint2 = 12345678L;
        record2.enum2 = Enum2Values.BIKE;
        List<Integer> integerList = new ArrayList<Integer>();
        integerList.add(1);
        integerList.add(2);
        integerList.add(3);
        record2.list2 = integerList;
        List<byte[]> bytesList = new ArrayList<>();
        bytesList.add("abc".getBytes());
        bytesList.add("abc".getBytes());
        record2.byteslist = bytesList;
        Record record = new Record();
        record.sInt = 200;
        record.sBoolean = true;
        record.sString = "123";
        Map<String, Record> map = new HashMap<>();
        map.put("test", record);
        map.put("test", record);
        map.put("test", record);
        record2.map2 = map;
        ModelFilling filling = new ModelFilling();
        filling.stringfilling1 = "filling1";
        filling.stringfilling2 = "filling2";
        record2.filling = filling;

        OutputStream os = new ByteArrayOutputStream();
        Schema schema = record2.getSchema();

        long start = System.nanoTime();
        for (int i = 0; i < 20000; i++) {
            jsonWriter.write(schema, record2, os);
        }
        long end = System.nanoTime();
        System.out.println("Used time: " + (end - start)/1000000);

        ((ByteArrayOutputStream) os).reset();
    }

    private void testJacksonSerialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleRecord simpleRecord = new SimpleRecord();
        simpleRecord.put(0, 10001);

        OutputStream os = new ByteArrayOutputStream();

        long start = System.nanoTime();
        for (int i = 0; i < 50000; i++) {
            objectMapper.writeValue(os, simpleRecord);
        }
        long end = System.nanoTime();
        System.out.println("Used time: " + (end - start)/1000000);

        ((ByteArrayOutputStream) os).reset();
    }

    private void testComplexJacksonSerialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Record2 record2 = new Record2();

        record2.bigint2 = 12345678L;
        record2.enum2 = Enum2Values.BIKE;
        List<Integer> integerList = new ArrayList<Integer>();
        integerList.add(1);
        integerList.add(2);
        integerList.add(3);
        record2.list2 = integerList;
        List<byte[]> bytesList = new ArrayList<>();
        bytesList.add("abc".getBytes());
        bytesList.add("abc".getBytes());
        record2.byteslist = bytesList;
        Record record = new Record();
        record.sInt = 200;
        record.sBoolean = true;
        record.sString = "123";
        Map<String, Record> map = new HashMap<>();
        map.put("test", record);
        map.put("test", record);
        map.put("test", record);
        record2.map2 = map;
        ModelFilling filling = new ModelFilling();
        filling.stringfilling1 = "filling1";
        filling.stringfilling2 = "filling2";
        record2.filling = filling;

        OutputStream os = new ByteArrayOutputStream();

        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            objectMapper.writeValue(os, record2);
        }
        long end = System.nanoTime();
        System.out.println("Used time: " + (end - start)/1000000);

        ((ByteArrayOutputStream) os).reset();
    }
}
