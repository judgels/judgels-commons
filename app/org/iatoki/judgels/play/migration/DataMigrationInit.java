package org.iatoki.judgels.play.migration;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class DataMigrationInit {

    @Inject
    public DataMigrationInit(JPAApi jpaApi, BaseDataMigrationService dataMigrationService) {
        jpaApi.withTransaction(dataMigrationService::checkDatabase);
    }
}
