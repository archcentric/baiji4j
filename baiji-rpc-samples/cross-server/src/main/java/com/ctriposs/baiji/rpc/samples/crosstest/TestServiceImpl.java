package com.ctriposs.baiji.rpc.samples.crosstest;

import java.io.UnsupportedEncodingException;
import java.util.*;

import com.ctriposs.baiji.exception.BaijiRuntimeException;

public class TestServiceImpl implements TestService {

    @Override
    public CrossTestResponseType testSerialize(CrossTestRequestType request) {
        if (request == null || request.name == null || request.name.isEmpty()) {
            throw new IllegalArgumentException("Missing name parameter");
        }

        TestSerializerSample sample = request.getSample();

        // Check bytes encoding/decoding function.
        // string1 is the reference of bytes1 using UTF8 encoding from client.
        try {
            String byteString = new String(sample.getBytes1(), 0, sample.getBytes1().length, "UTF-8");
            if (!sample.getString1().equals(byteString)) {
                throw new BaijiRuntimeException("bytes serialize mismatched!");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

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
