package com.ctriposs.baiji.schema;


import com.ctriposs.baiji.exception.BaijiRuntimeException;
import com.ctriposs.baiji.util.ObjectUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnionSchema extends UnnamedSchema {
    private final List<Schema> _schemas;

    private UnionSchema(List<Schema> schemas, PropertyMap props) {
        super(SchemaType.UNION, props);
        if (schemas == null) {
            throw new IllegalArgumentException("schemas");
        }
        _schemas = schemas;
    }

    /**
     * Static function to return instance of the union schema
     *
     * @param array    JSON object for the union schema
     * @param props
     * @param names    list of named schemas already read
     * @param encSpace enclosing namespace of the schema
     * @return
     */
    static UnionSchema newInstance(ArrayNode array, PropertyMap props, SchemaNames names, String encSpace) {
        List<Schema> schemas = new ArrayList<Schema>();
        Map<String, String> uniqueSchemas = new HashMap<String, String>();

        for (JsonNode node : array) {
            Schema unionType = parseJson(node, names, encSpace);
            if (null == unionType) {
                throw new SchemaParseException("Invalid JSON in union" + node);
            }

            String name = unionType.getName();
            if (uniqueSchemas.containsKey(name)) {
                throw new SchemaParseException("Duplicate type in union: " + name);
            }

            uniqueSchemas.put(name, name);
            schemas.add(unionType);
        }

        return new UnionSchema(schemas, props);
    }


    /**
     * @return List of schemas in the union
     */
    public List<Schema> getSchemas() {
        return _schemas;
    }

    /**
     * @return Count of schemas in the union
     */
    public int size() {
        return _schemas.size();
    }

    /**
     * Returns the schema at the given branch.
     *
     * @param index Index to the branch, starting with 0.
     * @return The branch corresponding to the given index.
     */
    public Schema get(int index) {
        return _schemas.get(index);

    }

    /**
     * Writes union schema in JSON format
     *
     * @param writer
     * @param names    list of named schemas already written
     * @param encSpace enclosing namespace of the schema
     */
    protected void writeJson(JsonGenerator writer, SchemaNames names,
                             String encSpace)
            throws IOException {
        writer.writeStartArray();
        for (Schema schema : _schemas) {
            schema.writeJson(writer, names, encSpace);
        }
        writer.writeEndArray();
    }

    /**
     * Returns the index of a branch that can read the data written by the given schema s.
     *
     * @param s The schema to match the branches against.
     * @return The index of the matching branch. If non matches a -1 is returned.
     */
    public int matchingBranch(Schema s) {
        if (s instanceof UnionSchema) {
            throw new BaijiRuntimeException("Cannot find a match against union schema");
        }
        // Try exact match.
        for (int i = 0; i < _schemas.size(); i++) {
            if (_schemas.get(i).canRead(s)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks if this schema can read data written by the given schema. Used for decoding data.
     *
     * @param writerSchema The writer's schema to match against.
     * @return true if this and writer schema are compatible based on the BAIJI specification, false otherwise
     */
    @Override
    public boolean canRead(Schema writerSchema) {
        return writerSchema.getType() == SchemaType.UNION || matchingBranch(writerSchema) >= 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UnionSchema)) {
            return false;
        }
        UnionSchema that = (UnionSchema) obj;
        if (!that._schemas.equals(_schemas)) {
            return false;
        }
        return ObjectUtils.equals(that.getProps(), getProps());
    }


    @Override
    public int hashCode() {
        int result = 53;
        for (Schema schema : _schemas) {
            result += 89 * schema.hashCode();
        }
        result += ObjectUtils.hashCode(getProps());
        return result;
    }
}
