package org.iatoki.judgels.commons;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public final class JudgelsUtils {

    private JudgelsUtils() {
        // prevents instantiation
    }

    public static String formatDate(Date date) {
        return formatDate(date.getTime());
    }

    public static String formatDate(long timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm");
        return formatter.format(timestamp);
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
        return StringEscapeUtils.escapeHtml4(string).replaceAll("\r\n", "<br />");
    }

    public static String getUserDisplayName(String username, String name) {
        return username + " (" + name + ")";
    }

    public static void updateUserJidCache(AbstractJidCacheService<?> jidCacheService) {
        if (IdentityUtils.getUserJid() != null) {
            jidCacheService.putDisplayName(IdentityUtils.getUserJid(), JudgelsUtils.getUserDisplayName(IdentityUtils.getUsername(), IdentityUtils.getUserRealName()), IdentityUtils.getUserJid(), IdentityUtils.getIpAddress());
        }
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
}
