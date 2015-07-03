package org.iatoki.judgels.commons.services;

import org.iatoki.judgels.commons.models.domains.AbstractJidCacheModel;

import java.util.List;
import java.util.Map;

public interface BaseJidCacheService<M extends AbstractJidCacheModel> {

    void putDisplayName(String jid, String displayName, String user, String ipAddress);

    String getDisplayName(String jid);

    Map<String, String> getDisplayNames(List<String> jids);
}
