package com.ctriposs.baiji.specific;

import com.ctriposs.baiji.JsonSerializer;
import com.ctriposs.baiji.io.JsonEncoder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
