package com.ctriposs.baiji;

import com.ctriposs.baiji.generic.GenericBenchmarkRecord;
import com.ctriposs.baiji.specific.*;
import com.google.common.collect.Lists;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class JsonSerializerBenchmarkTest {

    private BenchmarkSerializer serializer = new JsonSerializerBenchmark();
    private int loop;
    private boolean run;

    private ConcurrentHashMap<String, List<ExecutionResult>> records = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        JsonSerializerBenchmarkTest test = new JsonSerializerBenchmarkTest();
        test.testBenchmark();
    }

    public void testBenchmark() throws Exception {
        testJackson();
        testBinary();
        testJson();
        print(records);
    }

    public void testJsonSerializerBenchmark() throws Exception {
        serializer = new JsonSerializerBenchmark();
        intBenchmark();
        booleanBenchmark();
        /*longBenchmark();
        doubleBenchmark();
        stringBenchmark();
        bytesBenchmark();
        enumBenchmark();
        arrayBenchmark();
        mapBenchmark();
        recordBenchmark();*/
       /* if (run) {
            benchmarkFiveThreads();
            benchmarkTenThreads();
            benchmarkTwentyThreads();
        }*/
    }

    public void testJacksonBenchmark() throws Exception {
        serializer = new JacksonBenchmark();
        intBenchmark();
        booleanBenchmark();
        /*longBenchmark();
        doubleBenchmark();
        stringBenchmark();
        bytesBenchmark();
        enumBenchmark();
        arrayBenchmark();
        mapBenchmark();
        recordBenchmark();*/
      /*  if (run) {
            benchmarkFiveThreads();
            benchmarkTenThreads();
            benchmarkTwentyThreads();
        }*/
    }

    public void testBinaryBenchmark() throws Exception {
        serializer = new BinaryBenchmark();
        intBenchmark();
        booleanBenchmark();
        /*longBenchmark();
        doubleBenchmark();
        stringBenchmark();
        bytesBenchmark();
        enumBenchmark();
        arrayBenchmark();
        mapBenchmark();
        recordBenchmark();*/
        /*if (run) {
            benchmarkFiveThreads();
            benchmarkTenThreads();
            benchmarkTwentyThreads();
        }*/
    }

    private void jsonWarmUp() throws Exception {
        loop = 800;
        run = false;
        testJsonSerializerBenchmark();
        System.out.println("Json Warm Up Done");
    }

    private void testJson() throws Exception {
        jsonWarmUp();
        loop = 40000;
        run = true;
        testJsonSerializerBenchmark();
    }

    private void binaryWarmUp() throws Exception {
        loop = 800;
        run = false;
        testBinaryBenchmark();
        System.out.println("Binary warm up done");
    }

    private void testBinary() throws Exception {
        binaryWarmUp();
        loop = 40000;
        run = true;
        testBinaryBenchmark();
    }

    private void jacksonWarmUp() throws Exception {
        loop = 800;
        run = false;
        testJacksonBenchmark();
        System.out.println("Jackson warm up done");
    }

    private void testJackson() throws Exception {
        jacksonWarmUp();
        loop = 40000;
        run = true;
        testJacksonBenchmark();
    }

    private void intBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark(42, "\"int\"");
        appendResults("write int", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("parse int", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void booleanBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark(true, "\"boolean\"");
        appendResults("write boolean", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("parse boolean", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void longBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark(1024 * 1024 * 16L, "\"long\"");
        appendResults("write long", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("parse long", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void doubleBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark(24.00000001, "\"double\"");
        appendResults("write double", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("parse double", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void stringBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark("testString", "\"string\"");
        appendResults("write string", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("parse string", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void bytesBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark("testBytes".getBytes(), "\"bytes\"");
        appendResults("write bytes", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("parse bytes", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void enumBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark(Enum1Values.RED, "{\"type\":\"enum\",\"name\":\"Enum1Values\",\"namespace\":\"com.ctriposs.baiji.specific\",\"doc\":null,\"symbols\":[\"BLUE\",\"RED\",\"GREEN\"]}");
        appendResults("write enum", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("parse enum", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void arrayBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark(Lists.newArrayList(1, 2, 3, 4, 5), "{\"type\":\"array\",\"items\":\"int\"}");
        appendResults("write array", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("parse array", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));

    }

    private void mapBenchmark() {
        serializer.clearCache();
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("1a", 1);
        map.put("2b", 2);
        map.put("3c", 3);
        double[] results = singleFieldBenchmark(map, "{\"type\":\"map\",\"values\":\"int\"}");
        appendResults("write map", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("parse map", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void recordBenchmark() {
        serializer.clearCache();
        ModelFilling2 record = new ModelFilling2(1024 * 1024 * 16L, "testRecord", Lists.newArrayList("a", "b", "c"), Enum2Values.BIKE);
        double[] results = singleFieldBenchmark(record, record.getSchema().toString());
        appendResults("write record", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("parse record", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void benchmarkFiveThreads() throws ExecutionException, InterruptedException {
        benchmarkMultiThread(5);
    }

    private void benchmarkTenThreads() throws ExecutionException, InterruptedException {
        benchmarkMultiThread(10);
    }

    private void benchmarkTwentyThreads() throws ExecutionException, InterruptedException {
        benchmarkMultiThread(20);
    }

    private void benchmarkMultiThread(int threadNum) throws ExecutionException, InterruptedException {
        serializer.clearCache();

        ExecutorService executorService = Executors.newCachedThreadPool();
        ArrayList<Future<ArrayList<Double>>> futures = new ArrayList<>();

        for (int i = 0; i < threadNum; i++) {
            futures.add(executorService.submit(new Callable<ArrayList<Double>>() {
                @Override
                public ArrayList<Double> call() throws Exception {
                    ModelFilling2 record = new ModelFilling2(1024 * 1024 * 16L, "testRecord", Lists.newArrayList("a", "b", "c"), Enum2Values.BIKE);
                    double[] results = singleFieldBenchmark(record, record.getSchema().toString());
                    ArrayList<Double> result = new ArrayList<>();
                    result.add(results[0]);
                    result.add(results[1]);
                    result.add(results[2]);
                    return result;
                }
            }));
        }

        ArrayList<ArrayList<Double>> results = new ArrayList<>();
        for (Future<ArrayList<Double>> future : futures) {
            results.add(future.get());
        }

        executorService.shutdown();

        double serialize = 0;
        double deserialize = 0;
        int bytesSize = 0;

        for (int i = 0; i < results.size(); i++) {
            ArrayList<Double> perThread = results.get(i);
            serialize += perThread.get(0);
            deserialize += perThread.get(1);
            bytesSize = perThread.get(2).intValue();
        }

        serialize = serialize/results.size();
        deserialize = deserialize/results.size();

        appendResults(threadNum + " threads serialize records", new ExecutionResult(serializer.getName(), serialize, bytesSize));
        appendResults(threadNum + " threads deserialize records", new ExecutionResult(serializer.getName(), deserialize, bytesSize));
    }

    private double[] singleFieldBenchmark(Object fieldValue, String fieldType) {
        GenericBenchmarkRecord benchmarkRecord = GenericBenchmarkRecord.getBenchmarkRecord(fieldType);
        benchmarkRecord.put(0, fieldValue);

        List<Long> serializeTimes = new ArrayList<>();
        List<Long> deserializeTimes = new ArrayList<>();

        int bytesSize = 0;
        OutputStream os = new ByteArrayOutputStream();

        for (int i = 0; i < loop; i++) {
            try {
                long startTime = System.nanoTime();
                serializer.serialize(benchmarkRecord, os);
                long endTime = System.nanoTime();
                serializeTimes.add((endTime - startTime)/1000);

                byte[] bytes = ((ByteArrayOutputStream) os).toByteArray();
                ((ByteArrayOutputStream) os).reset();

                bytesSize = bytes.length;
                InputStream is = new ByteArrayInputStream(bytes);
                long startTimeTwo = System.nanoTime();
                serializer.deserialize(benchmarkRecord.getClass(), is);
                long endTimeTwo = System.nanoTime();
                deserializeTimes.add((endTimeTwo - startTimeTwo)/1000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new double[]{aggregateResults(serializeTimes), aggregateResults(deserializeTimes), bytesSize};
    }

    private double aggregateResults(List<Long> results) {
        double sum = 0;
        for (Long result : results) {
            sum += result;
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
                System.out.println("\t\t\t" + r.serializer + " " + ": " + r.time + "(avg) μs/op, " + r.bytesSize + " bytes");
            }
        }
    }

    class JsonSerializerBenchmark implements BenchmarkSerializer {

        private JsonStreamSerializer jsonSerializer = new JsonStreamSerializer();

        @Override
        public String getName() {
            return "Json Serializer";
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
            return "Binary Serializer";
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
            binarySerializer.clearCache();
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
