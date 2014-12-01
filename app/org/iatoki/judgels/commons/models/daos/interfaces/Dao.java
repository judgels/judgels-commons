package org.iatoki.judgels.commons.models.daos.interfaces;

import org.iatoki.judgels.commons.helpers.Page;
import org.iatoki.judgels.commons.models.domains.Model;

import java.lang.reflect.Field;
import java.util.List;

public interface Dao<K, E extends Model> {

    void persist(E entity, String user, String ipAddress);

    E edit(E entity, String user, String ipAddress);

    void remove(E entity);

    E findById(K id);

    List<E> findAll();

    List<E> findAll(List<Field> filters);

    Page<List<String>> pageString(long page, long pageSize, String sortBy, String order, String filterString, List<Field> filters);

}
