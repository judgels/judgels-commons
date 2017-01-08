package org.iatoki.judgels.api.impls;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.HttpRequestBase;
import org.iatoki.judgels.api.JudgelsPublicAPI;

public abstract class AbstractJudgelsPublicAPIImpl extends AbstractJudgelsAPIImpl implements JudgelsPublicAPI {

    private String accessToken;

    public AbstractJudgelsPublicAPIImpl(String baseUrl) {
        super(baseUrl);
        this.accessToken = null;
    }

    @Override
    public void useOnBehalfOfUser(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public void useAnonymously() {
        this.accessToken = null;
    }

    protected final void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    protected final void setAuthorization(HttpRequestBase httpRequest) {
        if (accessToken != null) {
            httpRequest.setHeader("Authorization", "Bearer " + Base64.encodeBase64String(accessToken.getBytes()));
        } else {
            httpRequest.removeHeaders("Authorization");
        }
    }
}
