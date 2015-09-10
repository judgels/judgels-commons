package org.iatoki.judgels.api.jophiel.impls;

import com.google.gson.Gson;
import org.iatoki.judgels.api.impls.AbstractJudgelsPublicAPIImpl;
import org.iatoki.judgels.api.jophiel.JophielPublicAPI;
import org.iatoki.judgels.api.jophiel.JophielUser;

public final class JophielPublicAPIImpl extends AbstractJudgelsPublicAPIImpl implements JophielPublicAPI {

    public JophielPublicAPIImpl(String baseUrl) {
        super(baseUrl);
    }

    @Override
    public JophielUser findUserByJid(String userJid) {
        String responseBody = sendGetRequest(interpolatePath("/users/:userJid", userJid));
        return new Gson().fromJson(responseBody, JophielUser.class);
    }

    @Override
    public JophielUser findUserByUsername(String username) {
        String responseBody = sendGetRequest(interpolatePath("/users/username/:username", username));
        return new Gson().fromJson(responseBody, JophielUser.class);
    }

    @Override
    public String getUserAutocompleteAPIEndpoint() {
        return getEndpoint("/users/autocomplete");
    }
}
