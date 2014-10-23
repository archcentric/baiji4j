package com.ctriposs.baiji;

import com.ctriposs.baiji.specific.*;

import java.io.*;
import java.util.*;

public class SingleTest {

    public static void main(String[] args) throws IOException {
        SingleTest test = new SingleTest();
        test.testSerialize();
    }

    private void testSerialize() throws IOException {
        JsonSerializer jsonSerializer = new JsonSerializer();

        TestSerializerSample sample = new TestSerializerSample();

        sample.bigint1 = 110L;
        sample.boolean1 = false;
        sample.double1 = 2.099328;
        sample.enum1 = Enum1Values.GREEN;
        sample.int1 = 2000;
        sample.string1 = "testSerialize";
        sample.bytes1 = "testBytes".getBytes();
        sample.list1 = Arrays.asList("a", "b", "c");
        Map<String, Integer> map = new HashMap<>();
        map.put("1a", 1);
        map.put("2b", 2);
        map.put("3c", 3);
        sample.map1 = map;
        sample.record = new Record(1, true, "testRecord");
        Record2 record2 = new Record2();
        record2.bigint2 = 2048L;
        record2.enum2 = Enum2Values.PLANE;
        Map<String, Record> recordMap = new HashMap<>();
        recordMap.put("m1", new Record(1, true, "testRecord"));
        recordMap.put("m2", new Record(2, true, "testRecord"));
        record2.map2 = recordMap;
        sample.container1 = new Record2Container(Arrays.asList(record2));

        OutputStream os = new ByteArrayOutputStream();

        for (int i = 0; i < 20000; i++) {
            jsonSerializer.serialize(sample, os);
        }
        ((ByteArrayOutputStream) os).reset();
        long end = System.nanoTime();
        for (int i = 0; i < 2000000; i++) {
            jsonSerializer.serialize(sample, os);
        }
        long endTwo = System.nanoTime();
        ((ByteArrayOutputStream) os).reset();
        System.out.println((endTwo - end)/(2000000));
    }
}
