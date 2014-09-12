package com.ctriposs.baiji.specific;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class SpecificJsonSerializerReuseTest {

    private SpecificJsonWriter<TestSerializerSample> writer;
    private SpecificJsonReader<TestSerializerSample> reader;

    @Before
    public void setUp() throws Exception {
        writer = new SpecificJsonWriter<>();
        reader = new SpecificJsonReader<>(TestSerializerSample.SCHEMA);
    }

    @Test
    public void testReuse() throws Exception {
        TestSerializerSample record = new TestSerializerSample();
        record.put("string1", "好好学习");

        // First serialize
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        writer.write(record.getSchema(), record, os);

        // Convert the output-stream to input-stream
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

        TestSerializerSample recordTwo = reader.read(null, is);
        Assert.assertEquals(recordTwo.getString1(), "好好学习");

        os.flush();
        writer.write(recordTwo.getSchema(), record, os);
        is = new ByteArrayInputStream(os.toByteArray());

        TestSerializerSample recordThree = reader.read(recordTwo, is);
        Assert.assertEquals(recordThree.getString1(), "好好学习");
    }
}
