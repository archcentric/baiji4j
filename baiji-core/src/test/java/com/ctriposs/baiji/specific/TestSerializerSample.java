package com.ctriposs.baiji.specific;

import com.ctriposs.baiji.exception.BaijiRuntimeException;
import com.ctriposs.baiji.schema.Schema;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

public class TestSerializerSample extends SpecificRecordBase {

  public static final Schema SCHEMA$ = Schema.parse("{\"type\":\"record\",\"name\":\"TestSerializerSample\",\"namespace\":\"com.ctriposs.baiji.specific\",\"fields\":[{\"name\":\"string1\",\"type\":\"string\"},{\"name\":\"int1\",\"type\":\"int\"},{\"name\":\"tinyint1\",\"type\":\"int\"},{\"name\":\"smallint1\",\"type\":\"int\"},{\"name\":\"bigint1\",\"type\":\"long\"},{\"name\":\"boolean1\",\"type\":\"boolean\"},{\"name\":\"float1\",\"type\":\"float\"},{\"name\":\"double1\",\"type\":\"double\"},{\"name\":\"list1\",\"type\":{\"type\":\"array\",\"items\":\"string\"}},{\"name\":\"map1\",\"type\":{\"type\":\"map\",\"values\":\"int\"}},{\"name\":\"struct1\",\"type\":{\"type\":\"record\",\"name\":\"struct1_name\",\"fields\":[{\"name\":\"sInt\",\"type\":\"int\"},{\"name\":\"sBoolean\",\"type\":\"boolean\"},{\"name\":\"sString\",\"type\":\"string\"}]}},{\"name\":\"union1\",\"type\":[\"float\",\"boolean\",\"string\"]},{\"name\":\"enum1\",\"type\":{\"type\":\"enum\",\"name\":\"enum1_values\",\"symbols\":[\"BLUE\",\"RED\",\"GREEN\"]}},{\"name\":\"nullableint\",\"type\":[\"int\",\"null\"]},{\"name\":\"bytes1\",\"type\":\"bytes\"}]}");
  public static Schema getClassSchema() { return SCHEMA$; }
  public CharSequence string1;
  public int int1;
  public int tinyint1;
  public int smallint1;
  public long bigint1;
  public boolean boolean1;
  public float float1;
  public double double1;
  public List<CharSequence> list1;
  public Map<CharSequence,Integer> map1;
  public struct1_name struct1;
  public Object union1;
  public enum1_values enum1;
  public Integer nullableint;
  public ByteBuffer bytes1;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public TestSerializerSample() {}

  /**
   * All-args constructor.
   */
  public TestSerializerSample(CharSequence string1, Integer int1, Integer tinyint1, Integer smallint1, Long bigint1, Boolean boolean1, Float float1, Double double1, List<CharSequence> list1, Map<CharSequence,Integer> map1, struct1_name struct1, Object union1, enum1_values enum1, Integer nullableint, java.nio.ByteBuffer bytes1) {
    this.string1 = string1;
    this.int1 = int1;
    this.tinyint1 = tinyint1;
    this.smallint1 = smallint1;
    this.bigint1 = bigint1;
    this.boolean1 = boolean1;
    this.float1 = float1;
    this.double1 = double1;
    this.list1 = list1;
    this.map1 = map1;
    this.struct1 = struct1;
    this.union1 = union1;
    this.enum1 = enum1;
    this.nullableint = nullableint;
    this.bytes1 = bytes1;
  }

  public Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public Object get(int field$) {
    switch (field$) {
    case 0: return string1;
    case 1: return int1;
    case 2: return tinyint1;
    case 3: return smallint1;
    case 4: return bigint1;
    case 5: return boolean1;
    case 6: return float1;
    case 7: return double1;
    case 8: return list1;
    case 9: return map1;
    case 10: return struct1;
    case 11: return union1;
    case 12: return enum1;
    case 13: return nullableint;
    case 14: return bytes1;
    default: throw new BaijiRuntimeException("Bad index");
    }
  }
  
  public void put(int field$, Object value$) {
    switch (field$) {
    case 0: string1 = (CharSequence)value$; break;
    case 1: int1 = (Integer)value$; break;
    case 2: tinyint1 = (Integer)value$; break;
    case 3: smallint1 = (Integer)value$; break;
    case 4: bigint1 = (Long)value$; break;
    case 5: boolean1 = (Boolean)value$; break;
    case 6: float1 = (Float)value$; break;
    case 7: double1 = (Double)value$; break;
    case 8: list1 = (List<CharSequence>)value$; break;
    case 9: map1 = (Map<CharSequence,Integer>)value$; break;
    case 10: struct1 = (struct1_name)value$; break;
    case 11: union1 = (Object)value$; break;
    case 12: enum1 = (enum1_values)value$; break;
    case 13: nullableint = (Integer)value$; break;
    case 14: bytes1 = (ByteBuffer)value$; break;
    default: throw new BaijiRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'string1' field.
   */
  public CharSequence getString1() {
    return string1;
  }

  /**
   * Sets the value of the 'string1' field.
   * @param value the value to set.
   */
  public void setString1(CharSequence value) {
    this.string1 = value;
  }

  /**
   * Gets the value of the 'int1' field.
   */
  public Integer getInt1() {
    return int1;
  }

  /**
   * Sets the value of the 'int1' field.
   * @param value the value to set.
   */
  public void setInt1(Integer value) {
    this.int1 = value;
  }

  /**
   * Gets the value of the 'tinyint1' field.
   */
  public Integer getTinyint1() {
    return tinyint1;
  }

  /**
   * Sets the value of the 'tinyint1' field.
   * @param value the value to set.
   */
  public void setTinyint1(Integer value) {
    this.tinyint1 = value;
  }

  /**
   * Gets the value of the 'smallint1' field.
   */
  public Integer getSmallint1() {
    return smallint1;
  }

  /**
   * Sets the value of the 'smallint1' field.
   * @param value the value to set.
   */
  public void setSmallint1(Integer value) {
    this.smallint1 = value;
  }

  /**
   * Gets the value of the 'bigint1' field.
   */
  public Long getBigint1() {
    return bigint1;
  }

  /**
   * Sets the value of the 'bigint1' field.
   * @param value the value to set.
   */
  public void setBigint1(Long value) {
    this.bigint1 = value;
  }

  /**
   * Gets the value of the 'boolean1' field.
   */
  public Boolean getBoolean1() {
    return boolean1;
  }

  /**
   * Sets the value of the 'boolean1' field.
   * @param value the value to set.
   */
  public void setBoolean1(Boolean value) {
    this.boolean1 = value;
  }

  /**
   * Gets the value of the 'float1' field.
   */
  public Float getFloat1() {
    return float1;
  }

  /**
   * Sets the value of the 'float1' field.
   * @param value the value to set.
   */
  public void setFloat1(Float value) {
    this.float1 = value;
  }

  /**
   * Gets the value of the 'double1' field.
   */
  public Double getDouble1() {
    return double1;
  }

  /**
   * Sets the value of the 'double1' field.
   * @param value the value to set.
   */
  public void setDouble1(Double value) {
    this.double1 = value;
  }

  /**
   * Gets the value of the 'list1' field.
   */
  public java.util.List<CharSequence> getList1() {
    return list1;
  }

  /**
   * Sets the value of the 'list1' field.
   * @param value the value to set.
   */
  public void setList1(java.util.List<CharSequence> value) {
    this.list1 = value;
  }

  /**
   * Gets the value of the 'map1' field.
   */
  public java.util.Map<CharSequence,Integer> getMap1() {
    return map1;
  }

  /**
   * Sets the value of the 'map1' field.
   * @param value the value to set.
   */
  public void setMap1(java.util.Map<CharSequence,Integer> value) {
    this.map1 = value;
  }

  /**
   * Gets the value of the 'struct1' field.
   */
  public struct1_name getStruct1() {
    return struct1;
  }

  /**
   * Sets the value of the 'struct1' field.
   * @param value the value to set.
   */
  public void setStruct1(struct1_name value) {
    this.struct1 = value;
  }

  /**
   * Gets the value of the 'union1' field.
   */
  public Object getUnion1() {
    return union1;
  }

  /**
   * Sets the value of the 'union1' field.
   * @param value the value to set.
   */
  public void setUnion1(Object value) {
    this.union1 = value;
  }

  /**
   * Gets the value of the 'enum1' field.
   */
  public enum1_values getEnum1() {
    return enum1;
  }

  /**
   * Sets the value of the 'enum1' field.
   * @param value the value to set.
   */
  public void setEnum1(enum1_values value) {
    this.enum1 = value;
  }

  /**
   * Gets the value of the 'nullableint' field.
   */
  public Integer getNullableint() {
    return nullableint;
  }

  /**
   * Sets the value of the 'nullableint' field.
   * @param value the value to set.
   */
  public void setNullableint(Integer value) {
    this.nullableint = value;
  }

  /**
   * Gets the value of the 'bytes1' field.
   */
  public java.nio.ByteBuffer getBytes1() {
    return bytes1;
  }

  /**
   * Sets the value of the 'bytes1' field.
   * @param value the value to set.
   */
  public void setBytes1(java.nio.ByteBuffer value) {
    this.bytes1 = value;
  }
}
