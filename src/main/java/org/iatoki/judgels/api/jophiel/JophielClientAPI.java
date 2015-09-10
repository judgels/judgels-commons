package org.iatoki.judgels.api.jophiel;

import org.iatoki.judgels.api.JudgelsClientAPI;

import java.util.List;

public interface JophielClientAPI extends JudgelsClientAPI {

    JophielUser findUserByUsernameAndPassword(String username, String password);

    JophielUser findUserByAccessToken(String accessToken);

    void sendUserActivityMessages(List<JophielUserActivityMessage> activityMessages);

    String getUserIsLoggedInAPIEndpoint();

    String getLinkedClientsAPIEndpoint();
}
