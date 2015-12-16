package org.iatoki.judgels.play.migration;

import java.sql.SQLException;

public interface BaseDataMigrationService {

    long getCodeDataVersion();

    void checkDatabase() throws SQLException;
}
