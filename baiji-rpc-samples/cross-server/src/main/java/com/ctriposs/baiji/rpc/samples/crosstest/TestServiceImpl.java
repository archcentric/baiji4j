package com.ctriposs.baiji.rpc.samples.crosstest;

import com.ctriposs.baiji.exception.BaijiRuntimeException;
import com.ctriposs.baiji.rpc.common.types.CheckHealthRequestType;
import com.ctriposs.baiji.rpc.common.types.CheckHealthResponseType;

import java.util.ArrayList;
import java.util.List;

public class TestServiceImpl implements TestService {

    @Override
    public CrossTestResponseType testSerialize(CrossTestRequestType request) throws Exception {
        if (request == null || request.name == null || request.name.isEmpty()) {
            throw new IllegalArgumentException("Missing name parameter");
        }

        TestSerializerSample sample = request.getSample();

        byte[] bytes1 = sample.bytes1;
        for (int i = 0; i < 256; i++) {
            if ((bytes1[i] - (byte) i) == 0)
                continue;
            else
                throw new BaijiRuntimeException("bytes serialize mismatched!");
        }

        if (Math.abs(sample.getDate1().getTimeInMillis() - java.util.Calendar.getInstance().getTimeInMillis()) > 1000)
            throw new BaijiRuntimeException("Date time is parsed incorrectly.");

        TestSerializerSampleList sampleList = new TestSerializerSampleList();
        List<TestSerializerSample> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(generateSample(sample, 2048 + i));
        }
        sampleList.setSamples(list);

        return new CrossTestResponseType(null, sampleList, "Hello" + request.name);
    }

    @Override
    public CheckHealthResponseType checkHealth(CheckHealthRequestType request) throws Exception {
        return new CheckHealthResponseType();
    }

    private static TestSerializerSample generateSample(TestSerializerSample sample, int id) {

        TestSerializerSample copySample = new TestSerializerSample(
                id, sample.tinyint1, sample.smallint1, sample.bigint1, sample.boolean1, sample.double1, sample.date1,
                sample.string1, sample.record, sample.list1, sample.map1, sample.enum1, sample.nullableint,
                sample.bytes1, sample.container1, sample.innerSample);
        copySample.enum1 = Enum1Values.BLUE;

        return copySample;
    }
}
