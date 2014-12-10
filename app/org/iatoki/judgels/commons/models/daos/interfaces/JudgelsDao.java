package org.iatoki.judgels.commons.models.daos.interfaces;

import org.iatoki.judgels.commons.models.domains.Model;

public interface JudgelsDao<E extends Model> extends Dao<Long, E> {

    E findByJid(String jid);
}
