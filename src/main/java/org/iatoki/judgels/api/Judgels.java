package org.iatoki.judgels.api;

public interface Judgels<T extends JudgelsAPI> {

    T connectWithBasicAuth(String clientJid, String clientSecret);

    T connectWithAccessToken(String accessToken);

    T connectAnonymously();
}
