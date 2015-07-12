package org.iatoki.judgels.play.models.daos;

import org.iatoki.judgels.play.models.daos.interfaces.Dao;
import org.iatoki.judgels.play.models.domains.AbstractModel;

public abstract class AbstractDao<K, M extends AbstractModel> implements Dao<K, M> {
    private final Class<M> modelClass;

    protected AbstractDao(Class<M> modelClass) {
        this.modelClass = modelClass;
    }

    protected final Class<M> getModelClass() {
        return modelClass;
    }
}
