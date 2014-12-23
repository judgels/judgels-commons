package org.iatoki.judgels.commons;

import play.mvc.Http;

public final class Utilities {

    private Utilities() {

    }

    public static String getUserIdFromSession() {
        return "";
    }

    public static String getIpAddressFromRequest() {
        return Http.Context.current().request().remoteAddress();
    }
}
