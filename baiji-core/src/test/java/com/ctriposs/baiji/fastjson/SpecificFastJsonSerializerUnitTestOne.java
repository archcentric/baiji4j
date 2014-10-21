package com.ctriposs.baiji.fastjson;

import com.ctriposs.baiji.specific.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SpecificFastJsonSerializerUnitTestOne {

    private SpecificFastJsonWriter<SimpleTestRecord> writer;
    private SpecificFastJsonReader<SimpleTestRecord> reader;

    @Before
    public void setUp() throws Exception {
        writer = new SpecificFastJsonWriter<>();
        reader = new SpecificFastJsonReader<>(SimpleTestRecord.SCHEMA);
    }

    @Test
    public void testSerializeBoolean() throws Exception {
        singleFieldTest("sBoolean", true);
    }

    @Test
    public void testSerializeInt() throws Exception {
        singleFieldTest("sInt", 42);
    }

    @Test
    public void testSerializeString() throws Exception {
        singleFieldTest("sString", "好好学习");
    }

    private void singleFieldTest(String fieldName, Object fieldValue) throws IOException {
        SimpleTestRecord sample = serializeAndDeserialize(fieldName, fieldValue);
        Assert.assertEquals(fieldValue, sample.get(fieldName));
    }

    private SimpleTestRecord serializeAndDeserialize(String fieldName, Object fieldValue) throws IOException {
        SimpleTestRecord record = new SimpleTestRecord();
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
