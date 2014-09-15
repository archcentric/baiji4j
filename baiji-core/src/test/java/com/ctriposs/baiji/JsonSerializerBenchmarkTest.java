package com.ctriposs.baiji;

import com.ctriposs.baiji.generic.GenericBenchmarkRecord;
import com.ctriposs.baiji.specific.Enum1Values;
import com.ctriposs.baiji.specific.SpecificRecord;
import com.google.common.collect.Lists;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JsonSerializerBenchmarkTest {

    private BenchmarkSerializer serializer;
    private int loop;
    private boolean run;

    private ConcurrentHashMap<String, List<ExecutionResult>> records = new ConcurrentHashMap<>();

    @Before
    public void setUp() throws Exception {
        loop = 50;
        run = false;
        warmUp();
    }

    @Test
    public void testBenchmark() throws Exception {
        loop = 1000;
        run = true;
    }

    public void testJsonSerializerBenchmark() throws Exception {
        serializer = new JsonSerializerBenchmark();
        for (int i = 0; i < loop; i++) {
            intBenchmark();
            booleanBenchmark();
            longBenchmark();
            doubleBenchmark();
            stringBenchmark();
            bytesBenchmark();
            enumBenchmark();
            arrayBenchmark();
            mapBenchmark();
        }
    }

    public void testJacksonBenchmark() throws Exception {
        serializer = new JacksonBenchmark();
        for (int i = 0; i < loop; i++) {
            intBenchmark();
            booleanBenchmark();
            longBenchmark();
            doubleBenchmark();
            stringBenchmark();
            bytesBenchmark();
            enumBenchmark();
            arrayBenchmark();
            mapBenchmark();
        }
    }

    public void testBinaryBenchmark() throws Exception {
        serializer = new BinaryBenchmark();
        for (int i = 0; i < loop; i++) {
            intBenchmark();
            booleanBenchmark();
            longBenchmark();
            doubleBenchmark();
            stringBenchmark();
            bytesBenchmark();
            enumBenchmark();
            arrayBenchmark();
            mapBenchmark();
        }
    }

    @After
    public void tearDown() throws Exception {

    }

    private void warmUp() throws Exception {
        // Pre-run until the JVM Stable
        testJsonSerializerBenchmark();
    }

    private void intBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark(42, "\"int\"");
        appendResults("serialize int", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("deserialize int", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void booleanBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark(true, "\"boolean\"");
        appendResults("serialize boolean", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("deserialize boolean", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void longBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark(1024 * 1024 * 16L, "\"long\"");
        appendResults("serialize long", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("deserialize long", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void doubleBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark(24.00000001, "\"double\"");
        appendResults("serialize double", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("deserialize double", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void stringBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark("testString", "\"string\"");
        appendResults("serialize string", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("deserialize string", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void bytesBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark("testBytes".getBytes(), "\"bytes\"");
        appendResults("serialize bytes", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("deserialize bytes", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void enumBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark(Enum1Values.RED, "{\"type\":\"enum\",\"name\":\"Enum1Values\",\"namespace\":\"com.ctriposs.baiji.specific\",\"doc\":null,\"symbols\":[\"BLUE\",\"RED\",\"GREEN\"]}");
        appendResults("serialize enum", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("deserialize enum", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void arrayBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark(Lists.newArrayList(1, 2, 3, 4, 5), "{\"type\":\"array\",\"items\":\"int\"}");
        appendResults("serialize array", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("deserialize array", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));

    }

    private void mapBenchmark() {
        serializer.clearCache();
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("1a", 1);
        map.put("2b", 2);
        map.put("3c", 3);
        double[] results = singleFieldBenchmark(map, "{\"type\":\"map\",\"values\":\"int\"}");
        appendResults("serialize map", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("deserialize map", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void recordBenchmark() {
        serializer.clearCache();
    }

    private double[] singleFieldBenchmark(Object fieldValue, String fieldType) {
        GenericBenchmarkRecord.recordType = fieldType;
        GenericBenchmarkRecord benchmarkRecord = new GenericBenchmarkRecord();
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
                System.out.println(bytesSize);
                InputStream is = new ByteArrayInputStream(bytes);
                long startTimeTwo = System.nanoTime();
                serializer.deserialize(GenericBenchmarkRecord.class, is);
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
        if (!run)
            return;

        if (records.containsKey(type)) {
            records.get(type).add(result);
        } else {
            List<ExecutionResult> resultList = new ArrayList<>();
            resultList.add(result);
            records.put(type, resultList);
        }
    }

    private void print(ConcurrentHashMap<String, List<ExecutionResult>> result) {
        if (!run)
            return;

        String str = "(" + loop + " loops" + ")";
        for (String key : result.keySet()) {
            System.out.println(key + str);
            List<ExecutionResult> re = result.get(key);
            for (ExecutionResult r : re) {
                System.out.println("\t\t\t" + r.serializer + " " + ": " + r.time + "(avg) ns/op, " + r.bytesSize + " bytes");
            }
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
            objectMapper.writeValue(os, obj);
        }

        @Override
        public <T extends SpecificRecord> T deserialize(Class<T> clazz, InputStream is) throws IOException {
            return objectMapper.readValue(is, clazz);
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
            binarySerializer.serialize(obj, os);
        }

        @Override
        public <T extends SpecificRecord> T deserialize(Class<T> clazz, InputStream is) throws IOException {
            return binarySerializer.deserialize(clazz, is);
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
