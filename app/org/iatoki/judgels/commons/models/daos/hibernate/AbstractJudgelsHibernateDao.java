package org.iatoki.judgels.commons.models.daos.hibernate;

import org.iatoki.judgels.commons.JidService;
import org.iatoki.judgels.commons.models.daos.interfaces.JudgelsDao;
import org.iatoki.judgels.commons.models.domains.AbstractJudgelsModel;
import org.iatoki.judgels.commons.models.domains.AbstractJudgelsModel_;
import play.db.jpa.JPA;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public abstract class AbstractJudgelsHibernateDao<M extends AbstractJudgelsModel> extends AbstractHibernateDao<Long, M> implements JudgelsDao<M> {

    protected AbstractJudgelsHibernateDao(Class<M> modelClass) {
        super(modelClass);
    }

    @Override
    public void persist(M model, String user, String ipAddress) {
        model.jid = JidService.getInstance().generateNewJid(getModelClass());
        super.persist(model, user, ipAddress);
    }

    @Override
    public boolean existsByJid(String jid) {
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<M> root = query.from(getModelClass());

        query
                .select(cb.count(root))
                .where(cb.equal(root.get(AbstractJudgelsModel_.jid), jid));

        return JPA.em().createQuery(query).getSingleResult() > 0;
    }

    @Override
    public M findByJid(String jid) {
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<M> query = cb.createQuery(getModelClass());

        Root<M> root = query.from(getModelClass());

        query.where(cb.equal(root.get(AbstractJudgelsModel_.jid), jid));

        return JPA.em().createQuery(query).getSingleResult();
    }
}
