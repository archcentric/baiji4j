package com.ctriposs.baiji;


import com.ctriposs.baiji.generic.DatumReader;
import com.ctriposs.baiji.generic.DatumWriter;
import com.ctriposs.baiji.io.JsonDecoder;
import com.ctriposs.baiji.io.JsonEncoder;
import com.ctriposs.baiji.specific.SpecificJsonReader;
import com.ctriposs.baiji.specific.SpecificJsonWriter;
import com.ctriposs.baiji.specific.SpecificRecord;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JsonSerializer implements Serializer {

    private static final ConcurrentMap<Class<?>, DatumReader> _readerCache =
            new ConcurrentHashMap<Class<?>, DatumReader>();

    private static final ConcurrentMap<Class<?>, DatumWriter> _writerCache =
            new ConcurrentHashMap<Class<?>, DatumWriter>();

    @Override
    public <T extends SpecificRecord> void serialize(T obj, OutputStream stream) throws IOException {
        DatumWriter<T> writer = getWriter(obj);
        writer.write(obj, new JsonEncoder(obj.getSchema(), stream));
    }

    @Override
    public <T extends SpecificRecord> T deserialize(Class<T> objClass, InputStream stream) throws IOException {
        SpecificJsonReader<T> reader = (SpecificJsonReader) getReader(objClass);
        return reader.read(null, readStream(stream));
    }

    private static <T extends SpecificRecord> DatumWriter<T> getWriter(T obj) {
        Class clazz = obj.getClass();
        DatumWriter<T> writer = _writerCache.get(clazz);
        if (writer == null) {
            writer = new SpecificJsonWriter<T>(obj.getSchema());
            DatumWriter<T> existedWriter = _writerCache.putIfAbsent(clazz, writer);
            if (existedWriter != null) {
                writer = existedWriter;
            }
        }
        return writer;
    }

    private static <T extends SpecificRecord> DatumReader<T> getReader(Class<T> clazz) {
        DatumReader<T> datumReader = _readerCache.get(clazz);
        if (datumReader == null) {
            SpecificRecord record;
            try {
                record = clazz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            datumReader = new SpecificJsonReader<T>(record.getSchema());
            DatumReader<T> existedReader = _readerCache.putIfAbsent(clazz, datumReader);
            if (existedReader != null) {
                datumReader = existedReader;
            }
        }

        return datumReader;
    }

    private static String readStream(InputStream is) throws IOException {
        char[] buffer = new char[2048];
        StringBuffer sb = new StringBuffer();
        Reader reader = new BufferedReader(new InputStreamReader(is));
        int n;

        while ((n = reader.read(buffer)) != -1) {
            sb.append(buffer, 0, n);
        }

        return sb.toString();
    }
}
