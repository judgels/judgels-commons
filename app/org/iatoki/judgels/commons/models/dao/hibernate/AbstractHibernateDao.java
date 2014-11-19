package org.iatoki.judgels.commons.models.dao.hibernate;

import org.iatoki.judgels.commons.models.dao.AbstractDao;
import org.iatoki.judgels.commons.models.schema.Model;
import org.iatoki.judgels.commons.models.schema.AbstractUser;
import play.db.jpa.JPA;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.sql.Timestamp;
import java.util.List;

public abstract class AbstractHibernateDao<K, E extends Model> extends AbstractDao<K, E> {

    @Override
    public void persist(E entity, AbstractUser user, String ipAddress) {
        entity.setUserCreate(user);
        entity.setTimeCreate(new Timestamp(System.currentTimeMillis()));
        entity.setIpCreate(ipAddress);
        JPA.em().persist(entity);
    }

    @Override
    public E edit(E entity, AbstractUser user, String ipAddress) {
        entity.setUserUpdate(user);
        entity.setTimeUpdate(new Timestamp(System.currentTimeMillis()));
        entity.setIpUpdate(ipAddress);
        entity = JPA.em().merge(entity);
        return entity;
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
}
