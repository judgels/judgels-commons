package org.iatoki.judgels.api.jophiel.impls;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.apache.http.HttpStatus;
import org.iatoki.judgels.api.JudgelsAPIClientException;
import org.iatoki.judgels.api.impls.AbstractJudgelsClientAPIImpl;
import org.iatoki.judgels.api.jophiel.JophielClientAPI;
import org.iatoki.judgels.api.jophiel.JophielUser;
import org.iatoki.judgels.api.jophiel.JophielUserActivityMessage;

import java.util.List;

public final class JophielClientAPIImpl extends AbstractJudgelsClientAPIImpl implements JophielClientAPI {

    public JophielClientAPIImpl(String baseUrl, String clientJid, String clientSecret) {
        super(baseUrl, clientJid, clientSecret);
    }

    @Override
    public JophielUser findUserByUsernameAndPassword(String username, String password) {
        try {
            String responseBody = sendGetRequest("/users/usernamePassword", ImmutableMap.of("username", username, "password", password));
            return new Gson().fromJson(responseBody, JophielUser.class);
        } catch (JudgelsAPIClientException e) {
            if (e.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                return null;
            } else {
                throw e;
            }
        }
    }

    @Override
    public JophielUser findUserByAccessToken(String accessToken) {
        try {
            String responseBody = sendGetRequest("/users/accessToken", ImmutableMap.of("accessToken", accessToken));
            return new Gson().fromJson(responseBody, JophielUser.class);
        } catch (JudgelsAPIClientException e) {
            if (e.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                return null;
            } else {
                throw e;
            }
        }
    }

    @Override
    public void sendUserActivityMessages(List<JophielUserActivityMessage> activityMessages) {
        JsonElement requestBody = new Gson().toJsonTree(activityMessages);
        sendPostRequest("/activities", requestBody);
    }

    @Override
    public String getUserIsLoggedInAPIEndpoint() {
        return getEndpoint("/users/me/isLoggedIn");
    }

    @Override
    public String getLinkedClientsAPIEndpoint() {
        return getEndpoint("/clients/linked");
    }
}
