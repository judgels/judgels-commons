package org.iatoki.judgels.api;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.HttpRequestBase;

public final class JudgelsAPIOAuth2Credentials implements JudgelsAPICredentials {

    private final String accessToken;

    public JudgelsAPIOAuth2Credentials(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public void applyToHttpRequest(HttpRequestBase httpRequest) {
        httpRequest.setHeader("Authorization", "Bearer " + Base64.encodeBase64String(accessToken.getBytes()));
    }

    public String getAccessToken() {
        return accessToken;
    }
}
