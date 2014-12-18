package org.iatoki.judgels.commons;

import play.mvc.Http;

public final class Utilities {

    private Utilities() {

    }

    public static String getUserIdFromSession(Http.Session session) {
        return "";
    }

    public static String getIpAddressFromRequest(Http.Request request) {
        return request.remoteAddress();
    }
}
