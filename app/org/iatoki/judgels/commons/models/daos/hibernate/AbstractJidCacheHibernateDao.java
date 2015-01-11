package org.iatoki.judgels.commons.models.daos.hibernate;

import org.iatoki.judgels.commons.models.daos.interfaces.BaseJidCacheDao;
import org.iatoki.judgels.commons.models.domains.AbstractJidCacheModel;

public abstract class AbstractJidCacheHibernateDao<M extends AbstractJidCacheModel> extends AbstractHibernateDao<String, M> implements BaseJidCacheDao<M> {

    protected AbstractJidCacheHibernateDao(Class<M> modelClass) {
        super(modelClass);
    }

}
