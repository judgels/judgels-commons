package org.iatoki.judgels.play.models.daos;

import org.iatoki.judgels.play.models.entities.AbstractModel;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Dao<K, M extends AbstractModel> {

    void persist(M model, String user, String ipAddress);

    M edit(M model, String user, String ipAddress);

    void flush();

    void remove(M model);

    boolean existsById(K id);

    M findById(K id);

    List<M> findAll();

    long countByFilters(String filterString);

    @Deprecated
    long countByFilters(String filterString, Map<String, String> filterColumns);

    long countByFilters(String filterString, Map<SingularAttribute<? super M, String>, String> filterColumns, Map<SingularAttribute<? super M, String>, ? extends Collection<String>> filterColumnsIn);

    List<M> findSortedByFilters(String orderBy, String orderDir, String filterString, long offset, long limit);

    @Deprecated
    List<M> findSortedByFilters(String orderBy, String orderDir, String filterString, Map<String, String> filterColumns, long offset, long limit);

    List<M> findSortedByFilters(String orderBy, String orderDir, String filterString, Map<SingularAttribute<? super M, String>, String> filterColumns, Map<SingularAttribute<? super M, String>, ? extends Collection<String>> filterColumnsIn, long offset, long limit);
}
