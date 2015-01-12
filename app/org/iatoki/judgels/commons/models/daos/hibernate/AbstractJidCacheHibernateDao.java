package org.iatoki.judgels.commons.models.daos.hibernate;

import org.iatoki.judgels.commons.models.daos.interfaces.BaseJidCacheDao;
import org.iatoki.judgels.commons.models.domains.AbstractJidCacheModel;
import org.iatoki.judgels.commons.models.domains.AbstractJidCacheModel_;
import play.db.jpa.JPA;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public abstract class AbstractJidCacheHibernateDao<M extends AbstractJidCacheModel> extends AbstractHibernateDao<String, M> implements BaseJidCacheDao<M> {

    protected AbstractJidCacheHibernateDao(Class<M> modelClass) {
        super(modelClass);
    }

    @Override
    public final boolean existsByJid(String jid) {
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<M> root = query.from(getModelClass());

        query
                .select(cb.count(root))
                .where(cb.equal(root.get(AbstractJidCacheModel_.jid), jid));

        return JPA.em().createQuery(query).getSingleResult() > 0;
    }

    @Override
    public final M findByJid(String jid) {
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<M> query = cb.createQuery(getModelClass());

        Root<M> root = query.from(getModelClass());

        query.where(cb.equal(root.get(AbstractJidCacheModel_.jid), jid));

        return JPA.em().createQuery(query).getSingleResult();
    }
}
