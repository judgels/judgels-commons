package org.iatoki.judgels.play.controllers.apis;

import play.mvc.Controller;

public abstract class AbstractJudgelsAPIController extends Controller {

    protected static void setAccessControlOrigin(String domains, String methods, long maxAge) {
        response().setHeader("Access-Control-Allow-Origin", domains);
        response().setHeader("Access-Control-Allow-Methods", methods);
        response().setHeader("Access-Control-Max-Age", maxAge + "");
        response().setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
    }

    protected static String createJsonPResponse(String callback, String json) {
        StringBuilder sb = new StringBuilder(callback);
        sb.append("(").append(json).append(")");
        return sb.toString();
    }
}
