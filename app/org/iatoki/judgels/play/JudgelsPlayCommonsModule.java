package org.iatoki.judgels.play;

import com.google.inject.AbstractModule;
import org.iatoki.judgels.Config;
import org.iatoki.judgels.play.asset.LocalAssetsInit;
import org.iatoki.judgels.play.general.GeneralConfigSource;
import org.iatoki.judgels.play.migration.DataMigrationInit;

public final class JudgelsPlayCommonsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Config.class).annotatedWith(GeneralConfigSource.class).toInstance(ApplicationConfig.getInstance());

        bind(LocalAssetsInit.class).asEagerSingleton();
        bind(DataMigrationInit.class).asEagerSingleton();
    }
}
