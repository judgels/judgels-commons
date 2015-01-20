package org.iatoki.judgels.commons.models.daos.interfaces;

import org.iatoki.judgels.commons.models.domains.AbstractJidCacheModel;

import java.util.List;

public interface BaseJidCacheDao<M extends AbstractJidCacheModel> extends Dao<String, M> {

    M createJidCacheModel();

    boolean existsByJid(String jid);

    M findByJid(String jid);

    List<M> findByJids(List<String> jids);
}
