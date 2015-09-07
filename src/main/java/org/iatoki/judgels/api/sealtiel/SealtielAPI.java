package org.iatoki.judgels.api.sealtiel;

import org.iatoki.judgels.api.JudgelsAPI;

public interface SealtielAPI extends JudgelsAPI {

    SealtielMessage fetchMessage();

    void acknowledgeMessage(long messageId);

    void extendMessageTimeout(long messageId);

    void sendMessage(String targetClientJid, String messageType, String message);

    void sendLowPriorityMessage(String targetClientJid, String messageType, String message);
}
