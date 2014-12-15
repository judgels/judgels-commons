package org.iatoki.judgels.commons.models.daos;

import org.iatoki.judgels.commons.models.daos.interfaces.Dao;
import org.iatoki.judgels.commons.models.domains.AbstractModel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AbstractDao<K, E extends AbstractModel> implements Dao<K, E> {
    private final Class<E> entityClass;

    @SuppressWarnings("unchecked")
    protected AbstractDao() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] typeArguments = genericSuperclass.getActualTypeArguments();
        this.entityClass = (Class<E>) typeArguments[0];
    }

    protected Class<E> getEntityClass() {
        return entityClass;
    }
}
