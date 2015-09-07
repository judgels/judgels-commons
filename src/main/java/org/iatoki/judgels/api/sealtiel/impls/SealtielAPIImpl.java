package org.iatoki.judgels.api.sealtiel.impls;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.iatoki.judgels.api.JudgelsAPICredentials;
import org.iatoki.judgels.api.impls.AbstractJudgelsAPIImpl;
import org.iatoki.judgels.api.sealtiel.SealtielAPI;
import org.iatoki.judgels.api.sealtiel.SealtielMessage;

public final class SealtielAPIImpl extends AbstractJudgelsAPIImpl implements SealtielAPI {

    public SealtielAPIImpl(String baseUrl, JudgelsAPICredentials credentials) {
        super(baseUrl, credentials);
    }

    @Override
    public SealtielMessage fetchMessage() {
        String response = sendPostRequest("/messages/fetch");

        if (response.isEmpty()) {
            return null;
        }

        return new Gson().fromJson(response, SealtielMessage.class);
    }

    @Override
    public void acknowledgeMessage(long messageId) {
        sendPostRequest(interpolatePath("/messages/:messageId/acknowledge", messageId));
    }

    @Override
    public void extendMessageTimeout(long messageId) {
        sendPostRequest(interpolatePath("/messages/:messageId/extendTimeout", messageId));
    }

    @Override
    public void sendMessage(String targetClientJid, String messageType, String message) {
        JsonObject body = new JsonObject();

        body.addProperty("targetClientJid", targetClientJid);
        body.addProperty("messageType", messageType);
        body.addProperty("message", message);
        body.addProperty("priority", 3);

        sendPostRequest("/messages/send", body);
    }

    @Override
    public void sendLowPriorityMessage(String targetClientJid, String messageType, String message) {
        JsonObject body = new JsonObject();

        body.addProperty("targetClientJid", targetClientJid);
        body.addProperty("messageType", messageType);
        body.addProperty("message", message);
        body.addProperty("priority", 1);

        sendPostRequest("/messages/send", body);
    }
}
