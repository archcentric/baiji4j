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

    private static TestSerializerSample generateSample(TestSerializerSample sample, int id) {

        TestSerializerSample copySample = new TestSerializerSample(
                id,sample.tinyint1,sample.smallint1,sample.bigint1,sample.boolean1,sample.double1,
                sample.string1,sample.record,sample.list1,sample.map1,sample.enum1,sample.nullableint,
                sample.bytes1,sample.container1,sample.innerSample);
        copySample.enum1 = Enum1Values.BLUE;

        return copySample;
    }
}
