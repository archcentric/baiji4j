package com.ctriposs.baiji;

import com.ctriposs.baiji.generic.GenericBenchmarkRecord;
import org.codehaus.jackson.map.MappingIterator;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SingleTest {

    public static void main(String[] args) throws IOException {
        SingleTest test = new SingleTest();
        test.testSerialize();
    }

    private void testSerialize() throws IOException {
        JsonSerializer jsonSerializer = new JsonSerializer();
        GenericBenchmarkRecord.recordType = "\"int\"";
        GenericBenchmarkRecord benchmarkRecord = new GenericBenchmarkRecord();
        benchmarkRecord.put(0, 42);

        OutputStream os = new ByteArrayOutputStream();

        /*while (true) {
            jsonSerializer.serialize(benchmarkRecord, os);
            byte[] bytes = ((ByteArrayOutputStream) os).toByteArray();
            ((ByteArrayOutputStream) os).reset();
            InputStream is = new ByteArrayInputStream(bytes);
            jsonSerializer.deserialize(GenericBenchmarkRecord.class, is);
            is.close();
        }*/
        long start = System.nanoTime();
        for (int i = 0; i < 2000000; i++) {
            jsonSerializer.serialize(benchmarkRecord, os);
        }
        long end = System.nanoTime();
        ((ByteArrayOutputStream) os).reset();
        System.out.println((end - start)/(2000000));
    }
}
