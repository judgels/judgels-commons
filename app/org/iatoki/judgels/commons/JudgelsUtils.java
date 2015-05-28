package org.iatoki.judgels.commons;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import play.mvc.Http;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public final class JudgelsUtils {

    private JudgelsUtils() {
        // prevents instantiation
    }

    public static String formatDetailedDateTime(long timestamp) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss XXX");
        return formatter.format(zonedDateTime);
    }

    public static String formatISOUTCDateTime(long timestamp) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return formatter.format(zonedDateTime);
    }

    public static String formatBytesCount(long bytes) {
        return FileUtils.byteCountToDisplaySize(bytes);
    }

    public static String generateNewSecret() {
        return messageDigest(UUID.randomUUID().toString(), "MD5");
    }

    public static String hashSHA256(String s) {
        return messageDigest(s, "SHA-256");
    }

    public static String hashMD5(String s) {
        return messageDigest(s, "MD5");
    }

    public static String escapeHtmlString(String string) {
        return StringEscapeUtils.escapeHtml4(string).replaceAll("\r\n", "<br />").replaceAll("\n", "<br />");
    }

    public static String getUserDisplayName(String username, String name) {
        return username + " (" + name + ")";
    }

    public static void updateUserJidCache(AbstractJidCacheService<?> jidCacheService) {
        if (IdentityUtils.getUserJid() != null) {
            jidCacheService.putDisplayName(IdentityUtils.getUserJid(), JudgelsUtils.getUserDisplayName(IdentityUtils.getUsername(), IdentityUtils.getUserRealName()), IdentityUtils.getUserJid(), IdentityUtils.getIpAddress());
        }
    }

    public static String prettifyUserDisplayName(String displayName) {
        String[] parts = displayName.split("\\(|\\)");
        if (parts.length == 1) {
            return "<span class=\"username\">" + parts[0] + "</span>";
        } else {
            return "<span class=\"username\">" + parts[0] + "</span>" + " <span class=\"user-real-name\">(" + parts[1] + ")</span>";
        }
    }

    public static String getOnlyUsername(String displayName) {
        int spacePos = displayName.indexOf(' ');
        if (spacePos == -1) {
            return displayName;
        }
        return displayName.substring(0, spacePos);
    }

    public static String getOnlyRealName(String displayName) {
        int spacePos = displayName.indexOf(' ');
        if (spacePos == -1) {
            return displayName;
        }
        return displayName.substring(spacePos + 2, displayName.length() - 1);
    }

    public static boolean isSidebarShown(Http.Request request) {
        return ((request.cookie("sidebar") == null) || (request.cookie("sidebar").value().equals("true")));
    }

    public static boolean isFullscreen(Http.Request request) {
        return ((request.cookie("fullscreen") != null) && (request.cookie("fullscreen").value().equals("true")));
    }

    public static boolean hasViewPoint() {
        return Http.Context.current().session().containsKey("viewpoint");
    }

    public static String getViewPoint() {
        return getFromSession("viewpoint");
    }

    public static void setViewPointInSession(String userJid) {
        putInSession("viewpoint", userJid);
    }

    public static void removeViewPoint() {
        Http.Context.current().session().remove("viewpoint");
    }

    private static String messageDigest(String s, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] hash = md.digest(s.getBytes("UTF-8"));
            return new String(Hex.encodeHex(hash));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static void putInSession(String key, String value) {
        Http.Context.current().session().put(key, value);
    }

    private static String getFromSession(String key) {
        return Http.Context.current().session().get(key);
    }
}
