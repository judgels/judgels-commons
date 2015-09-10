package org.iatoki.judgels.api.jophiel.impls;

import org.iatoki.judgels.api.impls.AbstractJudgelsPublicAPIImpl;
import org.iatoki.judgels.api.jophiel.JophielPublicAPI;
import org.iatoki.judgels.api.jophiel.JophielUser;

public final class JophielPublicAPIImpl extends AbstractJudgelsPublicAPIImpl implements JophielPublicAPI {

    public JophielPublicAPIImpl(String baseUrl) {
        super(baseUrl);
    }

    @Override
    public JophielUser findUserByJid(String userJid) {
        return sendGetRequest(interpolatePath("/users/:userJid", userJid)).asObjectFromJson(JophielUser.class);
    }

    @Override
    public JophielUser findUserByUsername(String username) {
        return sendGetRequest(interpolatePath("/users/username/:username", username)).asObjectFromJson(JophielUser.class);
    }

    @Override
    public String getUserAutocompleteAPIEndpoint() {
        return getEndpoint("/users/autocomplete");
    }
}
