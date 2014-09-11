package com.ctriposs.baiji;

import com.ctriposs.baiji.specific.SpecificRecord;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.InputStream;
import java.io.OutputStream;

public class JsonSerializerBenchmarkTest {


    class JsonSerializerBenchmark implements BenchmarkSerializer {

        private JsonSerializer jsonSerializer = new JsonSerializer();

        @Override
        public String getName() {
            return "Self JsonSerializer";
        }

        @Override
        public <T extends SpecificRecord> void serialize(String obj, OutputStream os) {

        }

        @Override
        public <T extends SpecificRecord> T deserialize(InputStream is) {
            return null;
        }
    }


    class JacksonBenchmark implements BenchmarkSerializer {

        private ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public String getName() {
            return "Jackson DataBind";
        }

        @Override
        public <T extends SpecificRecord> void serialize(String obj, OutputStream os) {

        }

        @Override
        public <T extends SpecificRecord> T deserialize(InputStream is) {
            return null;
        }
    }

    class BinaryBenchmark implements BenchmarkSerializer {

        private BinarySerializer binarySerializer = new BinarySerializer();

        @Override
        public String getName() {
            return "Self Binary Serializer";
        }

        @Override
        public <T extends SpecificRecord> void serialize(String obj, OutputStream os) {

        }

        @Override
        public <T extends SpecificRecord> T deserialize(InputStream is) {
            return null;
        }
    }

    interface BenchmarkSerializer {

        String getName();

        <T extends SpecificRecord> void serialize(String obj, OutputStream os);

        <T extends SpecificRecord> T deserialize(InputStream is);
    }
}
