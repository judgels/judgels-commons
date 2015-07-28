package org.iatoki.judgels.play.models.daos.impls;

import org.iatoki.judgels.play.models.daos.DataVersionDao;
import org.iatoki.judgels.play.models.entities.DataVersionModel;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public final class DataVersionHibernateDao implements DataVersionDao {

    private final EntityManager entityManager;

    public DataVersionHibernateDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public long getVersion() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<DataVersionModel> query = cb.createQuery(DataVersionModel.class);
        query.from(DataVersionModel.class);

        List<DataVersionModel> dataVersionModels = entityManager.createQuery(query).getResultList();

        if (dataVersionModels.isEmpty()) {
            return 0;
        } else {
            return dataVersionModels.get(0).version;
        }
    }

    @Override
    public void update(long version) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<DataVersionModel> query = cb.createQuery(DataVersionModel.class);
        query.from(DataVersionModel.class);

        List<DataVersionModel> dataVersionModels = entityManager.createQuery(query).getResultList();

        if (dataVersionModels.isEmpty()) {
            DataVersionModel dataVersionModel = new DataVersionModel();
            dataVersionModel.version = version;

            entityManager.persist(dataVersionModel);
        } else {
            DataVersionModel dataVersionModel =  dataVersionModels.get(0);
            dataVersionModel.version = version;

            entityManager.merge(dataVersionModel);
        }
    }
}
