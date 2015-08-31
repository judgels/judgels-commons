package org.iatoki.judgels.play.controllers.apis;

import org.apache.commons.lang3.StringUtils;
import play.mvc.Controller;

public abstract class AbstractJudgelsAPIController extends Controller {

    protected static void setAccessControlOrigin(String domains, String methods, long maxAge) {
        response().setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, domains);
        response().setHeader(ACCESS_CONTROL_ALLOW_METHODS, methods);
        response().setHeader(ACCESS_CONTROL_MAX_AGE, maxAge + "");
        response().setHeader(ACCESS_CONTROL_ALLOW_HEADERS, StringUtils.join(new String[] { ORIGIN, X_REQUESTED_WITH, CONTENT_TYPE, ACCEPT, AUTHORIZATION }, ','));
    }

    protected static String createJsonPResponse(String callback, String json) {
        StringBuilder sb = new StringBuilder(callback);
        sb.append("(").append(json).append(")");
        return sb.toString();
    }
}
