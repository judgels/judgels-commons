package org.iatoki.judgels.play.models.daos.impls;

import org.iatoki.judgels.play.models.daos.DataVersionDao;
import org.iatoki.judgels.play.models.entities.DataVersionModel;
import play.db.jpa.JPA;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Singleton
@Named("dataVersionDao")
public final class DataVersionHibernateDao implements DataVersionDao {

    @Override
    public long getVersion() {
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<DataVersionModel> query = cb.createQuery(DataVersionModel.class);
        query.from(DataVersionModel.class);

        List<DataVersionModel> dataVersionModels = JPA.em().createQuery(query).getResultList();

        if (dataVersionModels.isEmpty()) {
            return 0;
        } else {
            return dataVersionModels.get(0).version;
        }
    }

    @Override
    public void update(long version) {
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<DataVersionModel> query = cb.createQuery(DataVersionModel.class);
        query.from(DataVersionModel.class);

        List<DataVersionModel> dataVersionModels = JPA.em().createQuery(query).getResultList();

        if (dataVersionModels.isEmpty()) {
            DataVersionModel dataVersionModel = new DataVersionModel();
            dataVersionModel.version = version;

            JPA.em().persist(dataVersionModel);
        } else {
            DataVersionModel dataVersionModel =  dataVersionModels.get(0);
            dataVersionModel.version = version;

            JPA.em().merge(dataVersionModel);
        }
    }
}
