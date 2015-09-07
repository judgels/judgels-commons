package org.iatoki.judgels.api;

public interface Judgels<T extends JudgelsAPI> {

    T connectWithBasicAuth(String username, String password);

    T connectWithAccessToken(String accessToken);

    T connectAnonymously();
}
