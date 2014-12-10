package org.iatoki.judgels.commons.models.daos;

import org.iatoki.judgels.commons.models.daos.interfaces.JudgelsDao;
import org.iatoki.judgels.commons.models.domains.Model;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AbstractJudgelsDao<E extends Model> implements JudgelsDao<E> {
    private final Class<E> entityClass;

    @SuppressWarnings("unchecked")
    protected AbstractJudgelsDao() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] typeArguments = genericSuperclass.getActualTypeArguments();
        this.entityClass = (Class<E>) typeArguments[0];
    }

    protected Class<E> getEntityClass() {
        return entityClass;
    }
}
