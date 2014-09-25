package com.ctriposs.baiji.rpc.server.plugin.metadata;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by yqdong on 2014/9/25.
 */
public final class HtmlTemplates {

    public static final String indexPageTemplate;
    public static final String contentFormatPageTemplate;
    public static final String operationPageTemplate;

    private HtmlTemplates() {
    }

    static {
        indexPageTemplate = loadTemplateFromResource("template/index.html");
        contentFormatPageTemplate = loadTemplateFromResource("template/contentformat.html");
        operationPageTemplate = loadTemplateFromResource("template/operation.html");
    }

    private static String loadTemplateFromResource(String name) {
        InputStream stream = HtmlTemplates.class.getResourceAsStream(name);
        if (stream == null) {
            return "";
        }

        char[] buffer = new char[256];
        StringBuilder template = new StringBuilder();

        try {
            InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
            int length;
            while ((length = reader.read(buffer)) > 0) {
                template.append(buffer, 0, length);
            }
        } catch (Exception ex) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }

        return template.toString();
    }
}
