package com.ctriposs.baiji;

import com.ctriposs.baiji.schema.Schema;
import com.ctriposs.baiji.specific.SimpleRecord;
import com.ctriposs.baiji.specific.SpecificJsonWriter;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class JsonSerializerBenchmarkTestFour {

    public static void main(String[] args) throws IOException {
        JsonSerializerBenchmarkTestFour testFour = new JsonSerializerBenchmarkTestFour();
        testFour.testJacksonSerialize();
    }

    private void testJsonSerialize() {
        SpecificJsonWriter jsonWriter = new SpecificJsonWriter<SimpleRecord>();

        SimpleRecord simpleRecord = new SimpleRecord();
        simpleRecord.sInt = 10001;

        OutputStream os = new ByteArrayOutputStream();
        Schema schema = simpleRecord.getSchema();

        long start = System.nanoTime();
        for (int i = 0; i < 50000; i++) {
            jsonWriter.write(schema, simpleRecord, os);
        }
        long end = System.nanoTime();
        System.out.println("Used time: " + (end - start)/1000000);

        ((ByteArrayOutputStream) os).reset();
    }

    private void testJacksonSerialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleRecord simpleRecord = new SimpleRecord();
        simpleRecord.sInt = 10001;

        OutputStream os = new ByteArrayOutputStream();

        long start = System.nanoTime();
        for (int i = 0; i < 50000; i++) {
            objectMapper.writeValue(os, simpleRecord);
        }
        long end = System.nanoTime();
        System.out.println("Used time: " + (end - start)/1000000);

        ((ByteArrayOutputStream) os).reset();
    }
}
