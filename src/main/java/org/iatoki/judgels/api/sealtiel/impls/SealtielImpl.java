package org.iatoki.judgels.api.sealtiel.impls;

import org.iatoki.judgels.api.JudgelsAPIBasicAuthCredentials;
import org.iatoki.judgels.api.JudgelsAPINoCredentials;
import org.iatoki.judgels.api.JudgelsAPIOAuth2Credentials;
import org.iatoki.judgels.api.sealtiel.Sealtiel;
import org.iatoki.judgels.api.sealtiel.SealtielAPI;

public class SealtielImpl implements Sealtiel {

    private final String baseUrl;

    public SealtielImpl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public SealtielAPI connectWithBasicAuth(String username, String password) {
        return new SealtielAPIImpl(baseUrl, new JudgelsAPIBasicAuthCredentials(username, password));
    }

    @Override
    public SealtielAPI connectWithAccessToken(String accessToken) {
        return new SealtielAPIImpl(baseUrl, new JudgelsAPIOAuth2Credentials(accessToken));
    }

    @Override
    public SealtielAPI connectAnonymously() {
        return new SealtielAPIImpl(baseUrl, new JudgelsAPINoCredentials());
    }
}
