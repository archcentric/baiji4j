package com.ctriposs.baiji.rpc.server.util;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by yqdong on 2014/9/25.
 */
public final class UrlUtil {

    private UrlUtil() {
    }

    /**
     * Get the absolute URL of a relative URL.
     *
     * A relativeUrl beginning with "/" indicates it is relative to the root of the site.
     * A relativeUrl beginning with "~/" indicates it is relative to the root of the application.
     *
     * @param fullUrl A full request URL. e.g.: http://localhost:8080/baiji/test.json
     * @param requestPath The request path related to the application. e.g.: /test.json
     * @param relativeUrl The target URL to be resolved. e.g.: ~/metadata
     * @return The absolute URL. e.g. http://localhost:8080/baiji/metadata
     * @throws URISyntaxException fullUrl or targetUrl is not in valid URL format.
     */
    public static String getAbsoluteUrl(String fullUrl, String requestPath,
                                        String relativeUrl)
            throws URISyntaxException {
        int questionMarkIndex = fullUrl.indexOf('?');
        if (questionMarkIndex != -1) {
            fullUrl = fullUrl.substring(0, questionMarkIndex);
        }
        URI fullUri = new URI(fullUrl);
        if (relativeUrl.startsWith("/")) {
            return fullUri.relativize(new URI(relativeUrl)).toString();
        } else if (relativeUrl.startsWith("~/")) {
            String baseUrl = fullUrl.endsWith(requestPath)
                    ? fullUrl.substring(0, fullUrl.length() - requestPath.length()) : fullUrl;
            return baseUrl + relativeUrl.substring(1);
        } else {
            return fullUrl.substring(0, fullUrl.lastIndexOf("/") + 1) + relativeUrl;
        }
    }
}
