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
public class IndexMetadataPage implements MetadataPage {
    @Override
    public void render(ServiceHost host, HttpRequestWrapper request, HttpResponseWrapper response) throws Exception {
        String page = HtmlTemplates.indexPageTemplate;

        ServiceMetadata metadata = host.getServiceMetaData();
        page = page.replace("{ServiceName}", metadata.getServiceName());
        page = page.replace("{ServiceNamespace}", metadata.getServiceNamespace());
        page = page.replace("{Baiji4jVersion}", VersionUtils.getPackageVersion(ServiceHost.class));
        page = page.replace("{CodeGenVersion}", metadata.getCodeGeneratorVersion());

        StringBuilder operationListBuilder = new StringBuilder();
        for (Map.Entry<RequestPath, OperationHandler> entry : metadata.getOperationHandlers().entrySet()) {
            operationListBuilder.append(getOperationListEntry(entry.getValue().getMethodName(), host, request));
        }
        page = page.replace("{Operations}", operationListBuilder.toString());

        response.setHeader(HttpHeaders.CONTENT_TYPE, "text/html");
        OutputStreamWriter writer = new OutputStreamWriter(response.getResponseStream());
        writer.write(page);
        writer.flush();
        response.sendResponse();
    }

    private String getOperationListEntry(String operationName, ServiceHost host, HttpRequestWrapper request) {
        StringBuilder builder = new StringBuilder();
        builder.append("<tr>");
        builder.append(String.format("<th>%s</th>", operationName));
        for (ContentFormatter formatter : host.getConfig().contentFormatConfig.getRegisteredFormatters().values()) {
            String ext = formatter.getExtension();
            String opFormatUrl;
            try {
                opFormatUrl = UrlUtil.getAbsoluteUrl(request.requestUrl(), request.requestPath(),
                        String.format("~/%s/metadata?op=%s", ext, operationName));
            } catch (URISyntaxException e) {
                opFormatUrl = "#";
            }
            builder.append(String.format("<td><a href=\"%s\">%s</a></td>", opFormatUrl, ext.toUpperCase()));
        }
        builder.append("</tr>");
        return builder.toString();
    }
}
