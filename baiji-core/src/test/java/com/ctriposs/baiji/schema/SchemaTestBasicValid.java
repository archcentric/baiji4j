package com.ctriposs.baiji.schema;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SchemaTestBasicValid extends SchemaTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // Primitive types - shorthand
                new Object[]{"null", true},
                new Object[]{"boolean", true},
                new Object[]{"int", true},
                new Object[]{"long", true},
                new Object[]{"float", true},
                new Object[]{"double", true},
                new Object[]{"bytes", true},
                new Object[]{"string", true},
                new Object[]{"\"null\"", true},
                new Object[]{"\"boolean\"", true},
                new Object[]{"\"int\"", true},
                new Object[]{"\"long\"", true},
                new Object[]{"\"float\"", true},
                new Object[]{"\"double\"", true},
                new Object[]{"\"bytes\"", true},
                new Object[]{"\"string\"", true},

                // Primitive types - longer
                new Object[]{"{ \"type\": \"null\" }", true},
                new Object[]{"{ \"type\": \"boolean\" }", true},
                new Object[]{"{ \"type\": \"int\" }", true},
                new Object[]{"{ \"type\": \"long\" }", true},
                new Object[]{"{ \"type\": \"float\" }", true},
                new Object[]{"{ \"type\": \"double\" }", true},
                new Object[]{"{ \"type\": \"bytes\" }", true},
                new Object[]{"{ \"type\": \"string\" }", true},
                // Record
                new Object[]{"{\"type\": \"record\",\"name\": \"Test\",\"fields\": [{\"name\": \"f\",\"type\": \"long\"}]}", true},
                new Object[]{"{\"type\": \"record\",\"name\": \"Test\",\"fields\": " +
                        "[{\"name\": \"f1\",\"type\": \"long\", true},{\"name\": \"f2\", \"type\": \"int\"}]}", true},
                new Object[]{"{\"type\": \"error\",\"name\": \"Test\",\"fields\": " +
                        "[{\"name\": \"f1\",\"type\": \"long\", true},{\"name\": \"f2\", \"type\": \"int\"}]}", true},
                new Object[]{"{\"type\":\"record\",\"name\":\"LongList\"," +
                        "\"fields\":[{\"name\":\"value\",\"type\":\"long\", true},{\"name\":\"next\",\"type\":[\"LongList\",\"null\"]}]}"
                        , true}, // Recursive.

                // Enum
                new Object[]{"{\"type\": \"enum\", \"name\": \"Test\", \"symbols\": [\"A\", \"B\"]}", true},

                // Array
                new Object[]{"{\"type\": \"array\", \"items\": \"long\"}", true},
                new Object[]{
                        "{\"type\": \"array\",\"items\": {\"type\": \"enum\", \"name\": \"Test\", \"symbols\": [\"A\", \"B\"]}}", true},

                // Map
                new Object[]{"{\"type\": \"map\", \"values\": \"long\"}", true},
                new Object[]{
                        "{\"type\": \"map\",\"values\": {\"type\": \"enum\", \"name\": \"Test\", \"symbols\": [\"A\", \"B\"]}}", true},

                // Union
                new Object[]{"[\"string\", \"null\", \"long\"]", true},
                new Object[]{"[\"string\", \"long\", \"long\"]", true}
        });
    }

    private final String _schema;
    private final boolean _valid;

    public SchemaTestBasicValid(String schema, boolean valid) {
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
