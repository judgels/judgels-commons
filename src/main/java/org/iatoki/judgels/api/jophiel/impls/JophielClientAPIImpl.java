package org.iatoki.judgels.api.jophiel.impls;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.HttpStatus;
import org.iatoki.judgels.api.JudgelsAPIClientException;
import org.iatoki.judgels.api.impls.AbstractJudgelsClientAPIImpl;
import org.iatoki.judgels.api.jophiel.JophielClientAPI;
import org.iatoki.judgels.api.jophiel.JophielUser;
import org.iatoki.judgels.api.jophiel.JophielUserActivityMessage;

import java.util.List;

public final class JophielClientAPIImpl extends AbstractJudgelsClientAPIImpl implements JophielClientAPI {

    private final String baseUrl;

    public JophielClientAPIImpl(String baseUrl, String clientJid, String clientSecret) {
        super(baseUrl, clientJid, clientSecret);
        this.baseUrl = baseUrl;
    }

    @Override
    public JophielUser findUserByUsernameAndPassword(String username, String password) {
        try {
            JsonObject body = new JsonObject();

            body.addProperty("username", username);
            body.addProperty("password", password);

            return sendPostRequest("/users/usernamePassword", body).asObjectFromJson(JophielUser.class);
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
            return sendGetRequest("/users/accessToken", ImmutableMap.of("accessToken", accessToken)).asObjectFromJson(JophielUser.class);
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
    public String getUserEditProfileEndpoint() {
        return baseUrl + "/profile";
    }

    @Override
    public String getUserSearchProfileEndpoint() {
        return baseUrl + "/profiles/search";
    }

    @Override
    public String getRegisterEndpoint() {
        return baseUrl + "/register";
    }

    @Override
    public String getUserIsLoggedInAPIEndpoint() {
        return getEndpoint("/user/isLoggedIn");
    }

    @Override
    public String getLinkedClientsAPIEndpoint() {
        return getEndpoint("/clients/linked");
    }
}
