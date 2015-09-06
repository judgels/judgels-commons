package org.iatoki.judgels.play.controllers.api;

import org.apache.commons.lang3.StringUtils;
import play.mvc.Http;

public final class JudgelsAPIControllerUtils implements Http.HeaderNames {

    private JudgelsAPIControllerUtils() {
        // prevent instantiation
    }

    public static void setAccessControlOrigin(String domains, String methods, long maxAge) {
        Http.Context.current().response().setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, domains);
        Http.Context.current().response().setHeader(ACCESS_CONTROL_ALLOW_METHODS, methods);
        Http.Context.current().response().setHeader(ACCESS_CONTROL_MAX_AGE, maxAge + "");
        Http.Context.current().response().setHeader(ACCESS_CONTROL_ALLOW_HEADERS, StringUtils.join(new String[] {ORIGIN, X_REQUESTED_WITH, CONTENT_TYPE, ACCEPT, AUTHORIZATION}, ','));
    }

    public static String createJsonPResponse(String callback, String json) {
        StringBuilder sb = new StringBuilder(callback);
        sb.append("(").append(json).append(")");
        return sb.toString();
    }
}
