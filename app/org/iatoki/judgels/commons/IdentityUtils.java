package org.iatoki.judgels.commons;

import play.mvc.Http;

public final class IdentityUtils {

    private IdentityUtils() {
        // prevent instantiation
    }

    public static String getUserJid() {
        return ""; // TODO: get real user jid
    }

    public static String getUsername() {
        String username = Http.Context.current().session().get("username");
        if (username == null) {
            username = "guest";
        }

        return username;
    }

    public static String getUserRealName() {
        return "Lorem Ipsum"; //TODO: get real user real name
    }

    public static String getIpAddress() {
        return Http.Context.current().request().remoteAddress();
    }
}
