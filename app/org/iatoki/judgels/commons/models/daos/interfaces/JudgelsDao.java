package org.iatoki.judgels.commons.models.daos.interfaces;

import org.iatoki.judgels.commons.Page;
import org.iatoki.judgels.commons.InvalidPageNumberException;
import org.iatoki.judgels.commons.models.domains.AbstractModel;

import java.lang.reflect.Field;
import java.util.List;

public interface JudgelsDao<E extends AbstractModel> extends Dao<Long, E> {

    E findByJid(String jid);

    Page<List<String>> pageString(long page, long pageSize, String sortBy, String order, String filterString, List<Field> filters) throws InvalidPageNumberException;

}
