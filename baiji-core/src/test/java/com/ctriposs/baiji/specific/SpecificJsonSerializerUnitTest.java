package com.ctriposs.baiji.specific;

import com.ctriposs.baiji.JsonSerializer;
import com.ctriposs.baiji.io.JsonEncoder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static com.ctriposs.baiji.specific.TestSerializerSample.*;

public class SpecificJsonSerializerUnitTest {

    private SpecificJsonWriter<TestSerializerSample> writer;
    private SpecificJsonReader<TestSerializerSample> reader;

    @Before
    public void setUp() throws Exception {
        writer = new SpecificJsonWriter<TestSerializerSample>(SCHEMA$);
        reader = new SpecificJsonReader<TestSerializerSample>(SCHEMA$);
    }

    @Test
    public void testSerializeBoolean() throws Exception {
        singleFieldTest("boolean1", true);
    }

    @Test
    public void testSerializeInt() throws Exception {
        singleFieldTest("int1", 42);
    }

    @Test
    public void testSerializeLong() throws Exception {
        singleFieldTest("bigint1", 1024 * 1024 * 16L);
    }

    @Test
    public void testSerializeFloat() throws Exception {
        singleFieldTest("float1", 42.24342F);
    }

    @Test
    public void testSerializeDouble() throws Exception {
        singleFieldTest("double1", 24.00000001);
    }

    @Test
    public void testSerializeString() throws Exception {
        singleFieldTest("string1", "beepboop");
    }

    @Test
    public void testSerializeBytes() throws Exception {
        singleFieldTest("bytes1", ByteBuffer.wrap("beepboop".getBytes()));
    }

    @Test
    public void testSerializeEnum() throws Exception {
        singleFieldTest("enum1", enum1_values.RED);
    }

    private void singleFieldTest(String fieldName, Object fieldValue) throws IOException {
        TestSerializerSample sample = serializeAndDeserialize(fieldName, fieldValue);
        Assert.assertEquals(fieldValue, sample.get(fieldName));
    }

    private TestSerializerSample serializeAndDeserialize(String fieldName, Object fieldValue) throws IOException {
        TestSerializerSample record = new TestSerializerSample();
        record.put(fieldName, fieldValue);

        // First serialize
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        writer.write(record, new JsonEncoder(SCHEMA$, os));

        // Convert the output-stream to input-stream
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

        // Then deserialize
        return reader.read(null, JsonSerializer.readStream(is));
        //return record;
    }
}
