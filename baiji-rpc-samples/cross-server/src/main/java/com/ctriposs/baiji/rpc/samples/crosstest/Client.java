package com.ctriposs.baiji.rpc.samples.crosstest;

import org.junit.Assert;

import java.util.*;

public class Client {

    public static void main(String[] args) throws Exception {
        TestServiceClient client = TestServiceClient.getInstance(TestServiceClient.class, "http://localhost:8113/");
        CrossTestRequestType requestType = new CrossTestRequestType("beepboop", createSample());
        CrossTestResponseType responseType = client.testSerialize(requestType);
        System.out.println(responseType.getMessage());
        System.out.println(responseType.getResponseStatus());
        checkStatus(requestType.getSample(), responseType.getSampleList().getSamples());
        System.out.println("Passed!!!");
    }

    private static TestSerializerSample createSample() {

        TestSerializerSample sample = new TestSerializerSample();
        Random random = new Random();

        sample.bigint1 = random.nextLong();
        sample.boolean1 = false;
        sample.double1 = random.nextDouble();
        sample.enum1 = Enum1Values.GREEN;
        sample.date1 = Calendar.getInstance();
        sample.int1 = random.nextInt();
        sample.string1 = UUID.randomUUID().toString();
        sample.bytes1 = new byte[256];
        for (int i= 0; i < 256; ++i) {
            sample.bytes1[i] = (byte)i;
        }
        sample.list1 = Arrays.asList("啦", "b", "c");
        Map<String, Integer> map = new HashMap<>();
        map.put("1a", 1);
        map.put("2b", 2);
        map.put("3c", 3);
        sample.map1 = map;
        sample.record = new Record(1, true, "testRecord");
        TestSerializerSample innerSample = new TestSerializerSample();
        innerSample.bigint1 = random.nextLong();
        innerSample.boolean1 = true;
        innerSample.double1 = random.nextDouble();
        innerSample.list1 = Arrays.asList("你", "我", "他");
        innerSample.map1 = map;
        sample.innerSample = innerSample;
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

    private static void checkStatus(TestSerializerSample expected, List<TestSerializerSample> actualList) {
        for (int i = 0; i < actualList.size(); i++) {
            TestSerializerSample actual = actualList.get(i);

            Assert.assertEquals(expected.bigint1, actual.bigint1);
            Assert.assertEquals(expected.boolean1, actual.boolean1);
            Assert.assertEquals(expected.double1, actual.double1);
            Assert.assertEquals(expected.enum1, Enum1Values.GREEN);
            Assert.assertEquals(expected.string1, actual.string1);
            Assert.assertEquals((long)actual.int1, (long)(i + 2048));
            Assert.assertEquals(expected.list1.size(), actual.list1.size());
            Assert.assertEquals(expected.map1.size(), actual.map1.size());
            Assert.assertEquals(expected.list1, actual.list1);
            Assert.assertEquals(expected.map1, actual.map1);
            Assert.assertEquals(expected.record, actual.record);
            Assert.assertArrayEquals(expected.bytes1, actual.bytes1);

            Assert.assertNotNull(actual.innerSample);
            Assert.assertEquals(expected.innerSample.bigint1, actual.innerSample.bigint1);
            Assert.assertEquals(expected.innerSample.boolean1, actual.innerSample.boolean1);
            Assert.assertEquals(expected.innerSample.double1, actual.innerSample.double1);
            Assert.assertEquals(expected.innerSample.list1, actual.innerSample.list1);
            Assert.assertEquals(expected.innerSample.map1, actual.innerSample.map1);

            Record2 expectedRecord2 = expected.container1.getRecord2list().get(0);
            Record2 actualRecord2 = actual.container1.getRecord2list().get(0);

            Assert.assertEquals(expectedRecord2.bigint2, actualRecord2.bigint2);
            Assert.assertEquals(expectedRecord2.enum2, actualRecord2.enum2);
            Assert.assertEquals(expectedRecord2.map2, actualRecord2.map2);
        }
    }
}
