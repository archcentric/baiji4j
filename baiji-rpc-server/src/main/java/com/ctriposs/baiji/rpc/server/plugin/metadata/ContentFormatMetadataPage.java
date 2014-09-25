package com.ctriposs.baiji.rpc.server.plugin.metadata;

import com.ctriposs.baiji.rpc.common.formatter.ContentFormatter;
import com.ctriposs.baiji.rpc.server.*;
import com.ctriposs.baiji.rpc.server.util.UrlUtil;
import com.ctriposs.baiji.util.VersionUtils;
import com.google.common.net.HttpHeaders;

import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by yqdong on 2014/9/25.
 */
public class ContentFormatMetadataPage implements MetadataPage {

    private final String _formatExt;

    public ContentFormatMetadataPage(String formatExt) {
        _formatExt = formatExt;
    }

    @Override
    public void render(ServiceHost host, HttpRequestWrapper request, HttpResponseWrapper response) throws Exception {
        String page = HtmlTemplates.contentFormatPageTemplate;

        ServiceMetadata metadata = host.getServiceMetaData();
        page = page.replace("{ServiceName}", metadata.getServiceName());
        page = page.replace("{ServiceNamespace}", metadata.getServiceNamespace());
        page = page.replace("{Baiji4jVersion}", VersionUtils.getPackageVersion(ServiceHost.class));
        page = page.replace("{CodeGenVersion}", metadata.getCodeGeneratorVersion());

        ContentFormatter formatter = host.getConfig().contentFormatConfig.getFormatterFromExt(_formatExt);
        String message, operations;
        if (formatter == null) {
            message = "Unknown content format: " + _formatExt;
            operations = "";
        } else {
            message = "The following operations are supported. " +
                    "For more information please view the <a href=\"../../metadata\">Service Documentation</a>.";
            StringBuilder operationListBuilder = new StringBuilder();
            for (Map.Entry<RequestPath, OperationHandler> entry : metadata.getOperationHandlers().entrySet()) {
                operationListBuilder.append(getOperationListEntry(entry.getValue().getMethodName()));
            }
            operations = operationListBuilder.toString();
        }

        page = page.replace("{Message}", message);
        page = page.replace("{Operations}", operations);

        response.setHeader(HttpHeaders.CONTENT_TYPE, "text/html");
        OutputStreamWriter writer = new OutputStreamWriter(response.getResponseStream());
        writer.write(page);
        writer.flush();
        response.sendResponse();
    }

    private String getOperationListEntry(String operationName) {
        return String.format("<li><a href=\"?op=%s\">%s</a></li></li>", operationName, operationName);
    }
}
