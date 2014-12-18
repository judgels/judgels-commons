package org.iatoki.judgels.commons.models.daos.interfaces;

import org.iatoki.judgels.commons.models.domains.AbstractModel;

import java.lang.reflect.Field;
import java.util.List;

public interface Dao<K, E extends AbstractModel> {

    K persist(E entity, String user, String ipAddress);

    E edit(E entity, String user, String ipAddress);

    void remove(E entity);

    E findById(K id);

    List<E> findAll();

    List<E> findAll(List<Field> filters);

}
