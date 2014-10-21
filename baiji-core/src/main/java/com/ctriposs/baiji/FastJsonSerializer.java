package com.ctriposs.baiji;

import com.ctriposs.baiji.specific.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FastJsonSerializer implements Serializer {

    private static final ConcurrentMap<Class<?>, SpecificFastJsonReader> _readerCache =
            new ConcurrentHashMap<>();

    private static final ConcurrentMap<Class<?>, SpecificFastJsonWriter> _writerCache =
            new ConcurrentHashMap<>();

    @Override
    public <T extends SpecificRecord> void serialize(T obj, OutputStream stream) throws IOException {
        SpecificFastJsonWriter<T> writer = getWriter(obj);
        writer.write(obj.getSchema(), obj, stream);
    }

    @Override
    public <T extends SpecificRecord> T deserialize(Class<T> objClass, InputStream stream) throws IOException {
        SpecificFastJsonReader<T> reader = (SpecificFastJsonReader) getReader(objClass);
        return reader.read(null, stream);
    }

    private static <T extends SpecificRecord> SpecificFastJsonWriter<T> getWriter(T obj) {
        Class clazz = obj.getClass();
        SpecificFastJsonWriter<T> writer = _writerCache.get(clazz);
        if (writer == null) {
            writer = new SpecificFastJsonWriter<>();
            SpecificFastJsonWriter<T> existedWriter = _writerCache.putIfAbsent(clazz, writer);
            if (existedWriter != null) {
                writer = existedWriter;
            }
        }
        return writer;
    }

    private static <T extends SpecificRecord> SpecificFastJsonReader<T> getReader(Class<T> clazz) {
        SpecificFastJsonReader<T> datumReader = _readerCache.get(clazz);
        if (datumReader == null) {
            SpecificRecord record;
            try {
                record = clazz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            datumReader = new SpecificFastJsonReader<T>(record.getSchema());
            SpecificFastJsonReader<T> existedReader = _readerCache.putIfAbsent(clazz, datumReader);
            if (existedReader != null) {
                datumReader = existedReader;
            }
        }

        return datumReader;
    }

    public void clearCache() {
        _readerCache.clear();
        _writerCache.clear();
    }
}
