package org.iatoki.judgels.play.services;

import java.sql.SQLException;

public interface BaseDataMigrationService {

    void checkDatabase() throws SQLException;

    long getCodeDataVersion();
}
