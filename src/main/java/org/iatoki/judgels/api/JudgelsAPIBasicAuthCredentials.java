package org.iatoki.judgels.api;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.HttpRequestBase;

public final class JudgelsAPIBasicAuthCredentials implements JudgelsAPICredentials {

    private final String clientJid;
    private final String clientSecret;

    public JudgelsAPIBasicAuthCredentials(String clientJid, String clientSecret) {
        this.clientJid = clientJid;
        this.clientSecret = clientSecret;
    }

    @Override
    public void applyToHttpRequest(HttpRequestBase httpRequest) {
        String credentials = clientJid + ":" + clientSecret;
        httpRequest.setHeader("Authorization", "Basic " + Base64.encodeBase64String(credentials.getBytes()));
    }

    public String getClientJid() {
        return clientJid;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
