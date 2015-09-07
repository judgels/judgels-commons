package org.iatoki.judgels.play.services;

import org.iatoki.judgels.play.JudgelsAppClient;

public interface JudgelsAppClientService {

    boolean clientExistsByJid(String clientJid);

    JudgelsAppClient findClientByJid(String clientJid);
}
