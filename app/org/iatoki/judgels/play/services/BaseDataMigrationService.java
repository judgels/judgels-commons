package org.iatoki.judgels.play.services;

import java.sql.SQLException;

public interface BaseDataMigrationService {

    long getCodeDataVersion();

    void checkDatabase() throws SQLException;
}
