package com.ctriposs.baiji.rpc.common.formatter;

import java.io.InputStream;
import java.io.OutputStream;

public interface ContentFormatter {

    String getMediaType();

    String getExtension();

    String getEncoding();

    <T> void serialize(OutputStream outputStream, T obj) throws Exception;

    <T> T deserialize(Class<T> clazz, InputStream inputStream) throws Exception;
}
