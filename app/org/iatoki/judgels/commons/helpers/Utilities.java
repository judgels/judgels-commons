package org.iatoki.judgels.commons.helpers;

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

    @SuppressWarnings("unchecked")
    public static <T> T castStringtoCertainClass(Class<T> type, String s) {
        if ((boolean.class.equals(type)) || (Boolean.class.equals(type))) {
            return (T) Boolean.valueOf(s);
        } else {
            return type.cast(s);
        }
    }

}
