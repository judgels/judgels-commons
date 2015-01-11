package org.iatoki.judgels.commons;

import org.apache.commons.lang3.RandomStringUtils;
import org.iatoki.judgels.commons.models.JidPrefix;
import org.iatoki.judgels.commons.models.domains.AbstractJudgelsModel;

import java.util.UUID;

public final class JidService {
    private static JidService INSTANCE;

    private JidService() {
        // prevent instantiation
    }

    public static JidService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JidService();
        }
        return INSTANCE;
    }

    public <M extends AbstractJudgelsModel> String generateNewJid(Class<M> modelClass) {
        if (!modelClass.isAnnotationPresent(JidPrefix.class)) {
            throw new IllegalStateException("Model " + modelClass.getSimpleName() + " must have JidPrefix annotation");
        }

        String prefix = "JID" +  modelClass.getAnnotation(JidPrefix.class).value();
        String suffix = RandomStringUtils.randomAlphanumeric(20);

        return prefix + suffix;
    }

    public String parsePrefix(String jid) {
        int jidLength = "JID".length();
        int prefixLength = 4;

        return jid.substring(jidLength, jidLength + prefixLength);
    }
}
