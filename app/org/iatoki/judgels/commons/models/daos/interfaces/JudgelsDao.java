package org.iatoki.judgels.commons.models.daos.interfaces;

import org.iatoki.judgels.commons.models.domains.AbstractModel;

import java.util.List;
import java.util.Map;

public interface JudgelsDao<M extends AbstractModel> extends Dao<Long, M> {

    boolean existsByJid(String jid);

    M findByJid(String jid);

    List<M> findByJids(List<String> jids);
}