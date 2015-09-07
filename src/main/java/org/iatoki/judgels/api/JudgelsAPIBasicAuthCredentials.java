package org.iatoki.judgels.api;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.HttpRequestBase;

public final class JudgelsAPIBasicAuthCredentials implements JudgelsAPICredentials {

    private final String username;
    private final String password;

    public JudgelsAPIBasicAuthCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void applyToHttpRequest(HttpRequestBase httpRequest) {
        String credentials = username + ":" + password;
        httpRequest.setHeader("Authorization", "Basic " + Base64.encodeBase64String(credentials.getBytes()));
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
