package com.ctriposs.baiji.rpc.samples.crosstest;

import java.util.*;

public class Client {

    public static void main(String[] args) throws Exception {
        TestServiceClient client = TestServiceClient.getInstance(TestServiceClient.class, "http://localhost:8113/");
        CrossTestRequestType requestType = new CrossTestRequestType("beepboop", createSample());
        CrossTestResponseType responseType = client.testSerialize(requestType);
        System.out.println(responseType.getMessage());
        System.out.println(responseType.getResponseStatus());
        System.out.println(responseType.getSampleList().getSamples().size());
    }

    private static TestSerializerSample createSample() {

        TestSerializerSample sample = new TestSerializerSample();
        Random random = new Random();

        sample.bigint1 = random.nextLong();
        sample.boolean1 = false;
        sample.double1 = random.nextDouble();
        sample.enum1 = Enum1Values.GREEN;
        sample.int1 = random.nextInt();
        sample.string1 = UUID.randomUUID().toString();
        sample.bytes1 = UUID.randomUUID().toString().getBytes();
        sample.list1 = Arrays.asList("a", "b", "c");
        Map<String, Integer> map = new HashMap<>();
        map.put("1a", 1);
        map.put("2b", 2);
        map.put("3c", 3);
        sample.map1 = map;
        sample.record = new Record(1, true, "testRecord");
        Record2 record2 = new Record2();
        record2.bigint2 = random.nextLong();
        record2.enum2 = Enum2Values.PLANE;
        Map<String, Record> recordMap = new HashMap<>();
        recordMap.put("m1", new Record(1, true, "testRecord"));
        recordMap.put("m2", new Record(2, true, "testRecordRecord"));
        record2.map2 = recordMap;
        sample.container1 = new Record2Container(Arrays.asList(record2));

        return sample;
    }
}
