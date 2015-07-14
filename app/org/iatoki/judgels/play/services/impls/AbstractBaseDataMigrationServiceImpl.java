package org.iatoki.judgels.play.services.impls;

import org.iatoki.judgels.play.models.daos.DataVersionDao;
import org.iatoki.judgels.play.models.daos.impls.DataVersionHibernateDao;
import org.iatoki.judgels.play.services.BaseDataMigrationService;

import java.sql.SQLException;

public abstract class AbstractBaseDataMigrationServiceImpl implements BaseDataMigrationService {

    private final DataVersionDao dataVersionDao;

    public AbstractBaseDataMigrationServiceImpl() {
        this.dataVersionDao = new DataVersionHibernateDao();
    }

    @Override
    public final void checkDatabase() throws SQLException {
        long databaseVersion = dataVersionDao.getVersion();
        if (databaseVersion != getCodeDataVersion()) {
            onUpgrade(databaseVersion, getCodeDataVersion());
            dataVersionDao.update(getCodeDataVersion());
        }
    }

    protected abstract void onUpgrade(long databaseVersion, long codeDatabaseVersion) throws SQLException;
}
