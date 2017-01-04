package org.iatoki.judgels.play.models.daos;

import org.iatoki.judgels.play.models.entities.AbstractModel;

import java.util.Collection;
import java.util.List;

public interface JudgelsDao<M extends AbstractModel> extends Dao<Long, M> {

    void persist(M model, int childIndex, String user, String ipAddress);

    boolean existsByJid(String jid);

    M findByJid(String jid);

    List<M> getByJids(Collection<String> jids);
}
