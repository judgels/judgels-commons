package org.iatoki.judgels.commons.models.daos.hibernate;

import org.iatoki.judgels.commons.models.daos.AbstractDao;
import org.iatoki.judgels.commons.models.domains.AbstractModel;
import play.db.jpa.JPA;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHibernateDao<K, E extends AbstractModel> extends AbstractDao<K, E> {

    @Override
    public E edit(E entity, String user, String ipAddress) {
        entity.userUpdate = user;
        entity.timeUpdate = System.currentTimeMillis();
        entity.ipUpdate = ipAddress;
        return JPA.em().merge(entity);
    }

    @Override
    public void remove(E entity) {
        JPA.em().remove(entity);
    }

    @Override
    public E findById(K id) {
        return JPA.em().find(getEntityClass(), id);
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

}
