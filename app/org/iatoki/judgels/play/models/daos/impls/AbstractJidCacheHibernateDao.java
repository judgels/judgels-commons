package org.iatoki.judgels.play.models.daos.impls;

import org.iatoki.judgels.play.models.daos.BaseJidCacheDao;
import org.iatoki.judgels.play.models.entities.AbstractJidCacheModel;

public abstract class AbstractJidCacheHibernateDao<M extends AbstractJidCacheModel> extends AbstractJudgelsHibernateDao<M> implements BaseJidCacheDao<M> {

    protected AbstractJidCacheHibernateDao(Class<M> modelClass) {
        super(modelClass);
    }
}
