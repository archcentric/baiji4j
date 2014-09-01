package com.ctriposs.baiji;

import com.ctriposs.baiji.specific.SpecificRecord;
import com.ctriposs.baiji.specific.TestSerializerSample;
import com.ctriposs.baiji.specific.enum1_values;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

public class JsonSerializerUnitTest {

    JsonSerializer serializer;

    @Before
    public void setUp() throws Exception {
        serializer = new JsonSerializer();
    }

    @Test
    public void testNonRecursiveSerialize() throws Exception {
        OutputStream os = null;
        InputStream is = null;

        try {
            TestSerializerSample expected = new TestSerializerSample();
            expected.bigint1 = 16 * 1024L;
            expected.boolean1 = false;
            expected.double1 = 2.099328;
            expected.enum1 = enum1_values.GREEN;
            expected.float1 = 12384.83f;
            expected.int1 = 231;
            expected.string1 = "testserialize";
            List<CharSequence> list = new ArrayList<CharSequence>();
            list.add("a");
            list.add("b");
            list.add("c");
            expected.list1 = list;
            Map<CharSequence, Integer> map = new HashMap<CharSequence, Integer>();
            map.put("1a", 1);
            map.put("2b", 2);
            map.put("3c", 3);
            expected.map1 = map;
            expected.bytes1 = ByteBuffer.wrap("testBytes".getBytes());

            os = serialize(expected);

            is = new ByteArrayInputStream(((ByteArrayOutputStream) os).toByteArray());
            TestSerializerSample  actual = deserialize(TestSerializerSample.class, is);

            Assert.assertEquals(expected.bigint1, actual.bigint1);
            Assert.assertEquals(expected.boolean1, actual.boolean1);
            Assert.assertEquals(expected.double1, actual.double1, 5*Math.ulp(expected.double1));
            Assert.assertEquals(expected.enum1, actual.enum1);
            Assert.assertEquals(expected.float1, actual.float1, 5*Math.ulp(expected.float1));
            Assert.assertEquals(expected.int1, actual.int1);
            Assert.assertEquals(expected.string1, actual.string1);
            Assert.assertEquals(expected.list1, actual.list1);
            Assert.assertEquals(expected.map1, actual.map1);
            Assert.assertEquals(expected.bytes1, actual.bytes1);
        } finally {
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    private <T extends SpecificRecord> OutputStream serialize(T record) throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        serializer.serialize(record, os);

        return os;
    }

    private <T extends SpecificRecord> T deserialize(Class<T> clazz, InputStream is) throws IOException {
        return serializer.deserialize(clazz, is);
    }
}
