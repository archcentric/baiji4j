package com.ctriposs.baiji.rpc.server.plugin.metadata;

import com.ctriposs.baiji.rpc.common.formatter.ContentFormatter;
import com.ctriposs.baiji.rpc.server.*;
import com.ctriposs.baiji.rpc.server.util.UrlUtil;
import com.ctriposs.baiji.specific.SpecificRecord;
import com.ctriposs.baiji.util.VersionUtils;
import com.google.common.net.HttpHeaders;
import org.apache.http.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.util.*;

/**
 * Created by yqdong on 2014/9/25.
 */
public class OperationMetadataPage implements MetadataPage {

    private final String _formatExt;
    private final String _operationName;

    public OperationMetadataPage(String formatExt, String operationName) {
        _formatExt = formatExt;
        _operationName = operationName;
    }

    @Override
    public void render(ServiceHost host, HttpRequestWrapper request, HttpResponseWrapper response) throws Exception {
        String page = HtmlTemplates.operationPageTemplate;

        ContentFormatter formatter = host.getConfig().contentFormatConfig.getFormatterFromExt(_formatExt);
        if (formatter == null) {
            response.setStatus(HttpStatus.SC_NOT_FOUND);
            response.setHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
            OutputStreamWriter writer = new OutputStreamWriter(response.getResponseStream());
            writer.write("Unknown content format: " + _formatExt);
            writer.flush();
            response.sendResponse();
            return;
        }

        ServiceMetadata metadata = host.getServiceMetaData();
        OperationHandler operationHandler = metadata.getOperationHandler(_operationName);
        if (operationHandler == null) {
            response.setStatus(HttpStatus.SC_NOT_FOUND);
            response.setHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
            OutputStreamWriter writer = new OutputStreamWriter(response.getResponseStream());
            writer.write("Unknown operation: " + _operationName);
            writer.flush();
            response.sendResponse();
            return;
        }

        page = page.replace("{ServiceName}", metadata.getServiceName());
        page = page.replace("{ServiceNamespace}", metadata.getServiceNamespace());
        page = page.replace("{Baiji4jVersion}", VersionUtils.getPackageVersion(ServiceHost.class));
        page = page.replace("{CodeGenVersion}", metadata.getCodeGeneratorVersion());
        String contextPath = request.contextPath();
        if (contextPath == null) {
            contextPath = "";
        }
        page = page.replace("{ContextPath}",
                contextPath.endsWith("/") ? contextPath.substring(0, contextPath.length() - 1) : contextPath);
        page = page.replace("{OperationName}", _operationName);
        page = page.replace("{OperationNameLower}", _operationName.toLowerCase());
        page = page.replace("{ExtLower}", _formatExt.toLowerCase());
        page = page.replace("{ExtUpper}", _formatExt.toUpperCase());
        page = page.replace("{ContentType}", formatter.getContentType());

        String metadataUrl = UrlUtil.getAbsoluteUrl(request.requestUrl(), request.requestPath(), "~/metadata");
        page = page.replace("{MetadataUrl}", metadataUrl);

        String requestHost = "";
        try {
            requestHost = new URI(request.requestUrl()).getHost();
        } catch (Exception ex) {
        }
        page = page.replace("{RequestHost}", requestHost != null ? requestHost : "");

        SpecificRecord requestObj = operationHandler.getEmptyRequestInstance();
        fillTestData(requestObj, new HashSet<Class<?>>());
        String requestContent = getSerializedContent(requestObj, formatter);
        page = page.replace("{RequestContent}", requestContent);

        SpecificRecord responseObj = operationHandler.getEmptyResponseInstance();
        fillTestData(responseObj, new HashSet<Class<?>>());
        String responseContent = getSerializedContent(responseObj, formatter);
        page = page.replace("{ResponseContent}", responseContent);

        response.setHeader(HttpHeaders.CONTENT_TYPE, "text/html");
        OutputStreamWriter writer = new OutputStreamWriter(response.getResponseStream());
        writer.write(page);
        writer.flush();
        response.sendResponse();
    }

    private void fillTestData(SpecificRecord record, Set<Class<?>> processingClasses) {
        if (processingClasses.contains(record.getClass())) {
            return;
        }
        processingClasses.add(record.getClass());

        for (Field field : record.getClass().getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            Object value = null;
            Class<?> type = field.getType();
            if (type == boolean.class || type == Boolean.class) {
                value = Boolean.FALSE;
            } else if (type == int.class || type == Integer.class) {
                value = 0;
            } else if (type == long.class || type == Long.class) {
                value = 0L;
            } else if (type == double.class || type == Double.class) {
                value = 0.0;
            } else if (type == String.class) {
                value = "String";
            } else if (type == byte[].class) {
                value = new byte[]{1, 2, 3, 4, 5, 6};
            } else if (type == Calendar.class) {
                value = Calendar.getInstance();
            } else if (List.class.isAssignableFrom(type)) {
                value = new ArrayList(0);
            } else if (Map.class.isAssignableFrom(type)) {
                value = new HashMap(0);
            } else if (type.isEnum()) {
                Object[] enumValues = type.getEnumConstants();
                value = enumValues != null && enumValues.length != 0 ? enumValues[0] : null;
            } else if (SpecificRecord.class.isAssignableFrom(type)) {
                try {
                    value = type.newInstance();
                    fillTestData((SpecificRecord) value, processingClasses);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    value = type.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            field.setAccessible(true);
            try {
                field.set(record, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private String getSerializedContent(SpecificRecord record, ContentFormatter formatter) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            formatter.serialize(stream, record);
            return new String(stream.toByteArray(), "UTF-8");
        } catch (IOException e) {
            return "[Generated content failed.]";
        }
    }
}
