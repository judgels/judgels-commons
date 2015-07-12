package org.iatoki.judgels.play.services;

import org.iatoki.judgels.play.models.domains.AbstractJidCacheModel;

import java.util.List;
import java.util.Map;

public interface BaseJidCacheService<M extends AbstractJidCacheModel> {

    void putDisplayName(String jid, String displayName, String user, String ipAddress);

    String getDisplayName(String jid);

    Map<String, String> getDisplayNames(List<String> jids);
}
