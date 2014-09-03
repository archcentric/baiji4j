package com.ctriposs.baiji.schema;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SchemaTestBasicInvalid extends SchemaTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // Record
                new Object[]{"{\"type\":\"record\",\"name\":\"LongList\"," +
                        "\"fields\":[{\"name\":\"value\",\"type\":\"long\"},{\"name\":\"next\",\"type\":[\"LongListA\",\"null\"]}]}", false},
                new Object[]{"{\"type\":\"record\",\"name\":\"LongList\"}", false},
                new Object[]{"{\"type\":\"record\",\"name\":\"LongList\", \"fields\": \"hi\"}", false},

                // Enum
                new Object[]{"{\"type\": \"enum\", \"name\": \"Status\", \"symbols\": \"Normal Caution Critical\"}", false},
                new Object[]{"{\"type\": \"enum\", \"name\": [ 0, 1, 1, 2, 3, 5, 8 }, \"symbols\": [\"Golden\", \"Mean\"]}", false},
                new Object[]{"{\"type\": \"enum\", \"symbols\" : [\"I\", \"will\", \"fail\", \"no\", \"name\"]}", false},
                new Object[]{"{\"type\": \"enum\", \"name\": \"Test\", \"symbols\" : [\"AA\", \"AA\"]}", false},

                // Union
                new Object[]{"[\"string\", \"long\", \"long\"]", false},
                new Object[]{"[{\"type\": \"array\", \"items\": \"long\"}, {\"type\": \"array\", \"items\": \"string\"}]", false},
        });
    }

    private final String _schema;
    private final boolean _valid;

    public SchemaTestBasicInvalid(String schema, boolean valid) {
        _schema = schema;
        _valid = valid;
    }

    @Test
    public void testBasic() {
        try {
            Schema.parse(_schema);
            Assert.assertTrue(_valid);
        } catch (SchemaParseException ex) {
            Assert.assertFalse(_valid);
        }
    }
}
