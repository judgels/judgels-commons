package org.iatoki.judgels.commons;

public final class AWSFileURL {

    private String URL;

    private long expireTime;

    public AWSFileURL(String URL, long expireTime) {
        this.URL = URL;
        this.expireTime = expireTime;
    }

    public String getURL() {
        return URL;
    }

    public long getExpireTime() {
        return expireTime;
    }
}
