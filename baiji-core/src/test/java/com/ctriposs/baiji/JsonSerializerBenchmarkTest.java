package com.ctriposs.baiji;

import com.ctriposs.baiji.schema.Schema;
import com.ctriposs.baiji.specific.SpecificRecord;
import com.ctriposs.baiji.specific.SpecificRecordBase;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class JsonSerializerBenchmarkTest {

    private BenchmarkSerializer serializer;
    private int loop;

    private ConcurrentHashMap<String, List<ExecutionResult>> records = new ConcurrentHashMap<>();

    @Before
    public void setUp() throws Exception {
        loop = 50;
        warmUp();
    }

    @Test
    public void testJsonSerializerBenchmark() throws Exception {
        serializer = new JsonSerializerBenchmark();
        intBenchmark();
    }

    @Test
    public void testJacksonBenchmark() throws Exception {

    }

    @Test
    public void testBinaryBenchmark() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    private void warmUp() {
        // Pre-run until the JVM Stable
    }

    private void intBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark(42, "\"int\"");
        appendResults("serialize int", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("deserialize int", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }


    private double[] singleFieldBenchmark(Object fieldValue, String fieldType) {
        BenchmarkRecord benchmarkRecord = new BenchmarkRecord(fieldType);
        benchmarkRecord.put(0, fieldValue);

        List<Long> serializeTimes = new ArrayList<>();
        List<Long> deserializeTimes = new ArrayList<>();

        int bytesSize = 0;

        for (int i = 0; i < loop; i++) {
            try (OutputStream os = new ByteArrayOutputStream()) {
                long startTime = System.nanoTime();
                serializer.serialize(benchmarkRecord, os);
                long endTime = System.nanoTime();
                serializeTimes.add(endTime - startTime);

                byte[] bytes = ((ByteArrayOutputStream) os).toByteArray();
                bytesSize = bytes.length;
                InputStream is = new ByteArrayInputStream(bytes);
                long startTimeTwo = System.nanoTime();
                serializer.deserialize(BenchmarkRecord.class, is);
                long endTimeTwo = System.nanoTime();
                deserializeTimes.add(endTimeTwo - startTimeTwo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new double[]{aggregateResults(serializeTimes), aggregateResults(deserializeTimes), bytesSize};
    }

    private double aggregateResults(List<Long> results) {
        double sum = 0;
        for (int i = 0; i < results.size(); i++) {
            sum += results.get(i);
        }

        return sum/results.size();
    }

    private void appendResults(String type, ExecutionResult result) {
        if (records.containsKey(type)) {
            records.get(type).add(result);
        } else {
            List<ExecutionResult> resultList = new ArrayList<>();
            resultList.add(result);
            records.put(type, resultList);
        }
    }

    class JsonSerializerBenchmark implements BenchmarkSerializer {

        private JsonSerializer jsonSerializer = new JsonSerializer();

        @Override
        public String getName() {
            return "Self JsonSerializer";
        }

        @Override
        public <T extends SpecificRecord> void serialize(T obj, OutputStream os) throws IOException {
            jsonSerializer.serialize(obj, os);
        }

        @Override
        public <T extends SpecificRecord> T deserialize(Class<T> clazz, InputStream is) throws IOException {
            return jsonSerializer.deserialize(clazz, is);
        }

        @Override
        public void clearCache() {
            jsonSerializer.clearCache();
        }
    }


    class JacksonBenchmark implements BenchmarkSerializer {

        private ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public String getName() {
            return "Jackson DataBind";
        }

        @Override
        public <T extends SpecificRecord> void serialize(T obj, OutputStream os) throws IOException {

        }

        @Override
        public <T extends SpecificRecord> T deserialize(Class<T> clazz, InputStream is) throws IOException {
            return null;
        }

        @Override
        public void clearCache() {

        }
    }

    class BinaryBenchmark implements BenchmarkSerializer {

        private BinarySerializer binarySerializer = new BinarySerializer();

        @Override
        public String getName() {
            return "Self Binary Serializer";
        }

        @Override
        public <T extends SpecificRecord> void serialize(T obj, OutputStream os) throws IOException {

        }

        @Override
        public <T extends SpecificRecord> T deserialize(Class<T> clazz, InputStream is) throws IOException {
            return null;
        }

        @Override
        public void clearCache() {

        }
    }

    interface BenchmarkSerializer {

        String getName();

        <T extends SpecificRecord> void serialize(T obj, OutputStream os) throws IOException;

        <T extends SpecificRecord> T deserialize(Class<T> clazz, InputStream is) throws IOException;

        void clearCache();
    }

}

class BenchmarkRecord extends SpecificRecordBase {

    private Schema _schema;
    private Object fieldValue;

    public BenchmarkRecord(String recordType) {
        _schema = generateSchema(recordType);
    }

    @Override
    public Schema getSchema() {
        return _schema;
    }

    @Override
    public Object get(int fieldPos) {
        return fieldValue;
    }

    @Override
    public void put(int fieldPos, Object fieldValue) {
        this.fieldValue = fieldValue;
    }

    private Schema generateSchema(String recordType) {
        String s = "{\"type\":\"record\",\"name\":\"BenchmarkRecord\",\"namespace\":\"com.ctriposs.baiji\","
                + "\"fields\":[{\"name\":\"fieldName\",\"type\": " + recordType + "}]}";
        return Schema.parse(s);
    }
}

class ExecutionResult {
    public String serializer;
    public double time;
    public int bytesSize;

    public ExecutionResult(String serializer, double time, int bytesSize) {
        this.serializer = serializer;
        this.time = time;
        this.bytesSize = bytesSize;
    }
}
