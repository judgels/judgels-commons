package org.iatoki.judgels.play.models.daos;

import org.iatoki.judgels.play.models.entities.AbstractJidCacheModel;

import java.util.List;

public interface BaseJidCacheDao<M extends AbstractJidCacheModel> extends Dao<String, M> {

    M createJidCacheModel();

    boolean existsByJid(String jid);

    M findByJid(String jid);

    List<M> findByJids(List<String> jids);
}
