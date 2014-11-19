package org.iatoki.judgels.commons.models.dao;

import org.iatoki.judgels.commons.models.dao.interfaces.Dao;
import org.iatoki.judgels.commons.models.schema.Model;

import java.lang.reflect.ParameterizedType;

public abstract class AbstractDao<K, E extends Model> implements Dao<K, E> {
    private final Class<E> entityClass;

    @SuppressWarnings("unchecked")
    protected AbstractDao() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[1];
    }

    protected Class<E> getEntityClass() {
        return entityClass;
    }
}
