package org.iatoki.judgels.commons;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.UUID;

public final class JudgelsUtils {

    private JudgelsUtils() {
        // prevents instantiation
    }

    public static String timestampToFormattedDate(long timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd - HH:mm");
        return formatter.format(timestamp);
    }

    public static String byteCountToFormattedSize(long bytes) {
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
