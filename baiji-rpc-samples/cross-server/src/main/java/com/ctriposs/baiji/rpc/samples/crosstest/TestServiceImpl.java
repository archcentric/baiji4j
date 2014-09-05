package com.ctriposs.baiji.rpc.samples.crosstest;

import java.util.*;

public class TestServiceImpl implements TestService {

    @Override
    public CrossTestResponseType testSerialize(CrossTestRequestType request) {
        if (request == null || request.name == null || request.name.isEmpty()) {
            throw new IllegalArgumentException("Missing name parameter");
        }

        TestSerializerSample sample = request.getSample();
        TestSerializerSampleList sampleList = new TestSerializerSampleList();
        List<TestSerializerSample> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(generateSample(sample, 2048 + i));
        }
        sampleList.setSamples(list);

        return new CrossTestResponseType(null, sampleList, "Hello" + request.name);
    }

    private TestSerializerSample createSample(long id, String str, String bytes, String record) {

        TestSerializerSample sample = new TestSerializerSample();

        sample.bigint1 = id;
        sample.boolean1 = false;
        sample.double1 = 2.099328;
        sample.enum1 = Enum1Values.GREEN;
        sample.int1 = 2000;
        sample.string1 = str.toUpperCase();
        sample.bytes1 = bytes.toLowerCase().getBytes();
        sample.list1 = Arrays.asList("a", "b", "c");
        Map<String, Integer> map = new HashMap<>();
        map.put("1a", 1);
        map.put("2b", 2);
        map.put("3c", 3);
        sample.map1 = map;
        sample.record = new Record(1, true, record);
        Record2 record2 = new Record2();
        record2.bigint2 = 2048L;
        record2.enum2 = Enum2Values.PLANE;
        Map<String, Record> recordMap = new HashMap<>();
        recordMap.put("m1", new Record(1, true, record));
        recordMap.put("m2", new Record(2, true, record));
        record2.map2 = recordMap;
        sample.container1 = new Record2Container(Arrays.asList(record2));

        return sample;
    }

    private TestSerializerSample generateSample(TestSerializerSample sample, int id) {
        sample.enum1 = Enum1Values.BLUE;
        sample.int1 = id;

        return sample;
    }
}
