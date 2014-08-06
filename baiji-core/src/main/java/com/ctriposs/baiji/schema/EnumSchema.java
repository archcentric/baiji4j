package com.ctriposs.baiji.schema;

import com.ctriposs.baiji.exception.BaijiRuntimeException;
import com.ctriposs.baiji.util.ObjectUtils;
import org.codehaus.jackson.JsonNode;

import java.util.*;

public class EnumSchema extends NamedSchema implements Iterable<String> {

    private final List<String> _symbols;

    private final Map<String, Integer> _symbolMap;

    /**
     * Constructor for enum schema
     *
     * @param name      name of enum
     * @param doc
     * @param aliases   list of aliases for the name
     * @param symbols   list of enum symbols
     * @param symbolMap map of enum symbols and value
     * @param props
     * @param names     list of named schema already read
     */
    public EnumSchema(SchemaName name, String doc, List<SchemaName> aliases, List<String> symbols,
                      Map<String, Integer> symbolMap, PropertyMap props, SchemaNames names)

    {
        super(SchemaType.ENUM, name, doc, aliases, props, names);
        if (null == name.getName()) {
            throw new SchemaParseException("name cannot be null for enum schema.");
        }
        _symbols = symbols;
        _symbolMap = symbolMap;
    }

    /**
     * Static function to return new instance of EnumSchema
     *
     * @param token    JSON object for enum schema
     * @param props
     * @param names    list of named schema already parsed in
     * @param encSpace enclosing namespace for the enum schema
     * @return
     */
    static EnumSchema newInstance(JsonNode token, PropertyMap props, SchemaNames names, String encSpace) {
        SchemaName name = getName(token, encSpace);
        List<SchemaName> aliases = getAliases(token, name.getSpace(), name.getEncSpace());
        String doc = JsonHelper.getOptionalString(token, "doc");

        JsonNode jsymbols = token.get("symbols");
        if (jsymbols == null) {
            throw new SchemaParseException("Enum has no symbols: " + name);
        }
        if (!jsymbols.isArray()) {
            throw new SchemaParseException("symbols field in enum must be an array.");
        }

        List<String> symbols = new ArrayList<String>();
        Map<String, Integer> symbolMap = new HashMap<String, Integer>();
        int i = 0;
        for (JsonNode jsymbol : jsymbols) {
            String s = jsymbol.getTextValue();
            if (symbolMap.containsKey(s)) {
                throw new SchemaParseException("Duplicate symbol: " + s);
            }

            symbolMap.put(s, i++);
            symbols.add(s);
        }
        return new EnumSchema(name, doc, aliases, symbols, symbolMap, props, names);
    }

    /**
     * Get count of enum symbols
     *
     * @return
     */
    public int size() {
        return _symbols.size();
    }

    public List<String> getSymbols() {
        return _symbols;
    }

    /**
     * Returns the position of the given symbol within this enum.
     * Throws BaijiException if the symbol is not found in this enum.
     *
     * @param symbol name of the symbol to find
     * @return position of the given symbol in this enum schema
     */
    public int ordinal(String symbol) {
        Integer value = _symbolMap.get(symbol);
        if (value != null) {
            return value.intValue();
        }
        throw new BaijiRuntimeException("No such symbol: " + symbol);
    }

    /**
     * Returns the enum symbol of the given index to the list
     *
     * @param index symbol index
     * @return symbol name
     */
    public String get(int index) {

        if (index < _symbols.size()) {
            return _symbols.get(index);
        }
        throw new BaijiRuntimeException("Enumeration out of range. Must be less than " + _symbols.size() + ", but is " +
                index);
    }

    /**
     * Checks if given symbol is in the list of enum symbols
     *
     * @param symbol symbol to check
     * @return true if symbol exist, false otherwise
     */
    public boolean contains(String symbol) {
        return _symbolMap.containsKey(symbol);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof EnumSchema)) {
            return false;
        }
        EnumSchema that = (EnumSchema) obj;
        if (getSchemaName().equals(that.getSchemaName()) && _symbols.size() == that._symbols.size()) {
            for (int i = 0; i < _symbols.size(); ++i) {
                if (!_symbols.get(i).equals(that._symbols.get(i))) {
                    return false;
                }
            }
            return ObjectUtils.equals(that.getProps(), getProps());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int value = (int) (getSchemaName().hashCode() + ObjectUtils.hashCode(getProps()));
        for (String symbol : _symbols) {
            value += 23 * symbol.hashCode();
        }
        return value;
    }

    /**
     * Checks if this schema can read data written by the given schema. Used for decoding data.
     *
     * @param writerSchema
     * @return true if this and writer schema are compatible based on the Baiji specification, false otherwise
     */
    @Override
    public boolean canRead(Schema writerSchema) {
        if (writerSchema.getType() != getType()) {
            return false;
        }

        EnumSchema that = (EnumSchema) writerSchema;
        if (!that.getSchemaName().equals(getSchemaName())) {
            if (!inAliases(that.getSchemaName())) {
                return false;
            }
        }

        // we defer checking of symbols. Writer may have a symbol missing from the reader,
        // but if writer never used the missing symbol, then reader should still be able to read the data
        return true;
    }

    @Override
    public Iterator<String> iterator() {
        return _symbols.iterator();
    }
}
