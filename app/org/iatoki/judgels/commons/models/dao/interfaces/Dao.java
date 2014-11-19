package org.iatoki.judgels.commons.models.dao.interfaces;

import org.iatoki.judgels.commons.models.schema.Model;
import org.iatoki.judgels.commons.models.schema.AbstractUser;

import java.util.List;

public interface Dao<K, E extends Model> {

    void persist(E entity, AbstractUser user, String ipAddress);

    E edit(E entity, AbstractUser user, String ipAddress);

    void remove(E entity);

    E findById(K id);

    List<E> findAll();

}
