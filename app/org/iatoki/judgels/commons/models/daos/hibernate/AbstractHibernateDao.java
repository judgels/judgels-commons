package org.iatoki.judgels.commons.models.daos.hibernate;

import org.iatoki.judgels.commons.helpers.Page;
import org.iatoki.judgels.commons.helpers.exceptions.InvalidPageNumberException;
import org.iatoki.judgels.commons.models.daos.AbstractJudgelsDao;
import org.iatoki.judgels.commons.models.domains.Model;
import play.db.jpa.JPA;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHibernateDao<E extends Model> extends AbstractJudgelsDao<E> {

    @Override
    public void persist(E entity, String user, String ipAddress) {
        entity.setUserCreate(user);
        entity.setTimeCreate(System.currentTimeMillis());
        entity.setIpCreate(ipAddress);
        JPA.em().persist(entity);
    }

    @Override
    public E edit(E entity, String user, String ipAddress) {
        entity.setUserUpdate(user);
        entity.setTimeUpdate(System.currentTimeMillis());
        entity.setIpUpdate(ipAddress);
        E newEntity = JPA.em().merge(entity);
        return newEntity;
    }

    @Override
    public void remove(E entity) {
        JPA.em().remove(entity);
    }

    @Override
    public E findById(Long id) {
        return JPA.em().find(getEntityClass(), id);
    }

    @Override
    public E findByJid(String jid) {
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(getEntityClass());

        Root<E> root = query.from(getEntityClass());

        return null; //TODO
    }

    @Override
    public List<E> findAll() {
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(getEntityClass());

        query.from(getEntityClass());

        return JPA.em().createQuery(query).getResultList();
    }

    @Override
    public List<E> findAll(List<Field> filters) {
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(getEntityClass());

        Root<E> root = query.from(getEntityClass());

        List<Selection<?>> selection = new ArrayList<>();
        for (Field filter : filters) {
            selection.add(root.get(filter.getName()));
        }

        query.multiselect(selection).from(getEntityClass());

        return JPA.em().createQuery(query).getResultList();
    }

    @Override
    public Page<List<String>> pageString(long page, long pageSize, String sortBy, String order, String filterString, List<Field> filters) throws InvalidPageNumberException {
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<E> rootCount = countQuery.from(getEntityClass());

        List<Predicate> predicates = new ArrayList<>();

        for (Field filter : filters) {
            if (String.class.equals(filter.getType())) {
                predicates.add(cb.like(rootCount.get(filter.getName()), "%" + filterString + "%"));
            }
        }

        Predicate condition = cb.or(predicates.toArray(new Predicate[predicates.size()]));

        countQuery.select(cb.count(rootCount)).where(condition);
        Long count = JPA.em().createQuery(countQuery).getSingleResult();

        if ((page < 0) || (page * pageSize > count)) {
            throw new InvalidPageNumberException("Page number is invalid");
        } else {
            CriteriaQuery<E> query = cb.createQuery(getEntityClass());
            Root<E> root = query.from(getEntityClass());

            List<Selection<?>> selection = new ArrayList<>();
            predicates.clear();

            for (Field filter : filters) {
                selection.add(root.get(filter.getName()));
                if (String.class.equals(filter.getType())) {
                    predicates.add(cb.like(root.get(filter.getName()), "%" + filterString + "%"));
                }
            }

            condition = cb.or(predicates.toArray(new Predicate[predicates.size()]));

            Order orderBy = null;
            if ("asc".equals(order)) {
                orderBy = cb.asc(root.get(sortBy));
            } else {
                orderBy = cb.desc(root.get(sortBy));
            }

            query
                .multiselect(selection)
                .where(condition)
                .orderBy(orderBy);

            List<E> list = JPA.em().createQuery(query).setFirstResult((int) (page * pageSize)).setMaxResults((int) pageSize).getResultList();

            List<List<String>> listData = new ArrayList<>();
            for (E entity : list) {
                List<String> data = new ArrayList<>();
                for (Field filter : filters) {
                    try {
                        data.add(filter.get(entity).toString());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                listData.add(data);
            }

            return new Page<List<String>>(listData, count, page, pageSize);
        }
    }
}
