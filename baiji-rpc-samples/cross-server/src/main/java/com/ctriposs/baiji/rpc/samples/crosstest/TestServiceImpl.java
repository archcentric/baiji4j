package com.ctriposs.baiji.rpc.samples.crosstest;

import java.util.*;

public class TestServiceImpl implements TestService {

    @Override
    public CrossTestResponseType testSerialize(CrossTestRequestType request) {
        if (request == null || request.name == null || request.name.isEmpty()) {
            throw new IllegalArgumentException("Missing name parameter");
        }

        TestSerializerSampleList sampleList = new TestSerializerSampleList();
        List<TestSerializerSample> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(createSample(i));
        }
        sampleList.setSamples(list);

        return new CrossTestResponseType(null, sampleList, "Hello" + request.name);
    }

    private TestSerializerSample createSample(long id) {

        TestSerializerSample sample = new TestSerializerSample();

        sample.bigint1 = id;
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

        return sample;
    }
}
