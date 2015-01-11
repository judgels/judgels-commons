package org.iatoki.judgels.commons.models.daos.hibernate;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.iatoki.judgels.commons.models.daos.AbstractDao;
import org.iatoki.judgels.commons.models.domains.AbstractModel;
import play.db.jpa.JPA;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractHibernateDao<K, M extends AbstractModel> extends AbstractDao<K, M> {

    protected AbstractHibernateDao(Class<M> modelClass) {
        super(modelClass);
    }

    @Override
    public void persist(M model, String user, String ipAddress) {
        model.userCreate = user;
        model.timeCreate = System.currentTimeMillis();
        model.ipCreate = ipAddress;

        model.userUpdate = user;
        model.timeUpdate = model.timeCreate;
        model.ipUpdate = ipAddress;

        JPA.em().persist(model);
    }

    @Override
    public M edit(M model, String user, String ipAddress) {
        model.userUpdate = user;
        model.timeUpdate = System.currentTimeMillis();
        model.ipUpdate = ipAddress;

        return JPA.em().merge(model);
    }

    @Override
    public final void remove(M model) {
        JPA.em().remove(model);
    }

    @Override
    public final boolean existsById(K id) {
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<M> root = query.from(getModelClass());

        query
                .select(cb.count(root))
                .where(cb.equal(root.get("id"), id));

        return JPA.em().createQuery(query).getSingleResult() > 0;
    }

    @Override
    public final M findById(K id) {
        return JPA.em().find(getModelClass(), id);
    }

    @Override
    public final List<M> findAll() {
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<M> query = cb.createQuery(getModelClass());

        query.from(getModelClass());

        return JPA.em().createQuery(query).getResultList();
    }

    @Override
    public long countByFilters(String filterString, Map<String, String> filterColumns) {
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<M> root = query.from(getModelClass());

        List<Predicate> byStringPredicates = Lists.transform(getColumnsFilterableByString(), c -> cb.like(root.get(c), "%" + filterString + "%"));
        Predicate byString = cb.or(byStringPredicates.toArray(new Predicate[byStringPredicates.size()]));

        List<Predicate> byColumnPredicates = filterColumns.entrySet().stream().map(e -> cb.equal(root.get(e.getKey()), e.getValue())).collect(Collectors.toList());
        Predicate byColumn = cb.and(byColumnPredicates.toArray(new Predicate[byColumnPredicates.size()]));

        query
                .select(cb.count(root))
                .where(cb.and(byString, byColumn));

        return JPA.em().createQuery(query).getSingleResult();
    }

    @Override
    public List<M> findSortedByFilters(String orderBy, String orderDir, String filterString, Map<String, String> filterColumns, long offset, long limit) {
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<M> query = cb.createQuery(getModelClass());
        Root<M> root = query.from(getModelClass());

        List<Predicate> byStringPredicates = Lists.transform(getColumnsFilterableByString(), c -> cb.like(root.get(c), "%" + filterString + "%"));
        Predicate byString = cb.or(byStringPredicates.toArray(new Predicate[byStringPredicates.size()]));

        List<Predicate> byColumnPredicates = filterColumns.entrySet().stream().map(e -> cb.equal(root.get(e.getKey()), e.getValue())).collect(Collectors.toList());
        Predicate byColumn = cb.and(byColumnPredicates.toArray(new Predicate[byColumnPredicates.size()]));


        Order order;
        if ("asc".equals(orderDir)) {
            order = cb.asc(root.get(orderBy));
        } else {
            order = cb.desc(root.get(orderBy));
        }

        query
                .where(cb.and(byString, byColumn))
                .orderBy(order);

        return JPA.em().createQuery(query).setFirstResult((int) offset).setMaxResults((int) limit).getResultList();
    }

    protected List<SingularAttribute<M, String>> getColumnsFilterableByString() {
        return ImmutableList.of();
    }

}
