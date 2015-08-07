package org.iatoki.judgels.play.services.impls;

import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.iatoki.judgels.play.models.daos.DataVersionDao;
import org.iatoki.judgels.play.models.daos.impls.DataVersionHibernateDao;
import org.iatoki.judgels.play.services.BaseDataMigrationService;
import play.db.jpa.JPA;

import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractBaseDataMigrationServiceImpl implements BaseDataMigrationService {

    private DataVersionDao dataVersionDao;

    public AbstractBaseDataMigrationServiceImpl() {
        this.dataVersionDao = new DataVersionHibernateDao(Persistence.createEntityManagerFactory("playCommonsPersistenceUnit").createEntityManager());
    }

    @Override
    public final void checkDatabase() throws SQLException {
        checkTable();
        long databaseVersion = dataVersionDao.getVersion();
        if (databaseVersion != getCodeDataVersion()) {
            onUpgrade(databaseVersion, getCodeDataVersion());
            dataVersionDao.update(getCodeDataVersion());
        }
    }

    private void checkTable() throws SQLException {
        String tableName = "judgels_data_version";

        SessionImpl session = (SessionImpl) JPA.em().unwrap(Session.class);
        Connection connection = session.getJdbcConnectionAccess().obtainConnection();
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SHOW TABLES LIKE '" + tableName + "'");

        if (!resultSet.next()) {
            statement.execute("CREATE TABLE " + tableName + "("
                    + "id bigint(20) NOT NULL AUTO_INCREMENT,"
                    + "version bigint(20) NOT NULL,"
                    + "PRIMARY KEY (id)"
                    + ")");
            statement.executeUpdate("INSERT INTO `judgels_data_version` (`version`) VALUES (" + getCodeDataVersion() + ");");
        }

        resultSet.close();
        statement.close();
    }

    protected abstract void onUpgrade(long databaseVersion, long codeDatabaseVersion) throws SQLException;
}
