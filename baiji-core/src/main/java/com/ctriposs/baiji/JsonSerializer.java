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

    private static final ConcurrentMap<Class<?>, SpecificJsonReader> _readerCache =
            new ConcurrentHashMap<>();

    private static final ConcurrentMap<Class<?>, SpecificJsonWriter> _writerCache =
            new ConcurrentHashMap<>();

    @Override
    public <T extends SpecificRecord> void serialize(T obj, OutputStream stream) throws IOException {
        SpecificJsonWriter<T> writer = getWriter(obj);
        writer.writeR(obj.getSchema(), obj, new JsonEncoder(obj.getSchema(), stream));
    }

    @Override
    public <T extends SpecificRecord> T deserialize(Class<T> objClass, InputStream stream) throws IOException {
        SpecificJsonReader<T> reader = (SpecificJsonReader) getReader(objClass);
        /*SpecificRecord record;
        try {
            record = objClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        SpecificJsonReader<T> reader = new SpecificJsonReader<>();*/
        return reader.read(null, readStream(stream));
    }

    private static <T extends SpecificRecord> SpecificJsonWriter<T> getWriter(T obj) {
        Class clazz = obj.getClass();
        SpecificJsonWriter<T> writer = _writerCache.get(clazz);
        if (writer == null) {
            writer = new SpecificJsonWriter<>();
            SpecificJsonWriter<T> existedWriter = _writerCache.putIfAbsent(clazz, writer);
            if (existedWriter != null) {
                writer = existedWriter;
            }
        }
        return writer;
    }

    private static <T extends SpecificRecord> SpecificJsonReader<T> getReader(Class<T> clazz) {
        SpecificJsonReader<T> datumReader = _readerCache.get(clazz);
        if (datumReader == null) {
            SpecificRecord record;
            try {
                record = clazz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            datumReader = new SpecificJsonReader<T>(record.getSchema());
            SpecificJsonReader<T> existedReader = _readerCache.putIfAbsent(clazz, datumReader);
            if (existedReader != null) {
                datumReader = existedReader;
            }
        }

        return datumReader;
    }

    public static String readStream(InputStream is) throws IOException {
        char[] buffer = new char[4096];
        StringBuffer sb = new StringBuffer();
        Reader reader = new BufferedReader(new InputStreamReader(is));
        int n;

        while ((n = reader.read(buffer)) != -1) {
            sb.append(buffer, 0, n);
        }

        return sb.toString();
    }
}
