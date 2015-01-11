package org.iatoki.judgels.commons.models.daos.interfaces;

import org.iatoki.judgels.commons.models.domains.AbstractModel;

import java.util.List;
import java.util.Map;

public interface Dao<K, M extends AbstractModel> {

    void persist(M model, String user, String ipAddress);

    M edit(M model, String user, String ipAddress);

    void remove(M model);

    boolean existsById(K id);

    M findById(K id);

    List<M> findAll();

    long countByFilters(String filterString, Map<String, String> filterColumns);

    List<M> findSortedByFilters(String orderBy, String orderDir, String filterString, Map<String, String> filterColumns, long offset, long limit);
}
