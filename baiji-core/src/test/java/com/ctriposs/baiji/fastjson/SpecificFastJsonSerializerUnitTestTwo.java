package com.ctriposs.baiji.fastjson;

import com.ctriposs.baiji.specific.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SpecificFastJsonSerializerUnitTestTwo {

    private SpecificFastJsonWriter<TestRecord> writer;
    private SpecificFastJsonReader<TestRecord> reader;

    @Before
    public void setUp() throws Exception {
        writer = new SpecificFastJsonWriter<>();
        reader = new SpecificFastJsonReader<>(TestRecord.SCHEMA);
    }

    @Test
    public void testSerializeBoolean() throws Exception {
        singleFieldTest("flag", true);
    }

    @Test
    public void testSerializeInt() throws Exception {
        singleFieldTest("num1", 42);
    }

    @Test
    public void testSerializeLong() throws Exception {
        singleFieldTest("num2", 1024 * 1024 * 16L);
    }

    @Test
    public void testSerializeDouble() throws Exception {
        singleFieldTest("realNum2", 24.00000001);
    }

    @Test
    public void testSerializeFloat() throws Exception {
        singleFieldTest("realNum1", 12.0000f);
    }

    @Test
    public void testSerializeBytes() throws Exception {
        TestRecord sample = serializeAndDeserialize("data", "天天向上".getBytes());
        Assert.assertArrayEquals(sample.getData(), "天天向上".getBytes());
    }

    @Test
    public void testSerializeArray() throws Exception {
        singleFieldTest("nums", Arrays.asList(1, 2, 3));
    }

    @Test
    public void testSerializeMap() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("1a", "a");
        map.put("2b", "b");
        map.put("3c", "c");
        singleFieldTest("names", map);
    }

    private void singleFieldTest(String fieldName, Object fieldValue) throws IOException {
        TestRecord sample = serializeAndDeserialize(fieldName, fieldValue);
        Assert.assertEquals(fieldValue, sample.get(fieldName));
    }

    private TestRecord serializeAndDeserialize(String fieldName, Object fieldValue) throws IOException {
        TestRecord record = new TestRecord();
        record.put(fieldName, fieldValue);

        // First serialize
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        writer.write(record.getSchema(), record, os);

        // Convert the output-stream to input-stream
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

        // Then deserialize
        return reader.read(null, is);
        //return record;
    }
}
