package org.iatoki.judgels.play.models.daos.interfaces;

import org.iatoki.judgels.play.models.domains.AbstractModel;

import java.util.List;

public interface JudgelsDao<M extends AbstractModel> extends Dao<Long, M> {

    void persist(M model, int childIndex, String user, String ipAddress);

    boolean existsByJid(String jid);

    M findByJid(String jid);

    List<M> findByJids(List<String> jids);
}