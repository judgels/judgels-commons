package org.iatoki.judgels.commons.models.daos;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.iatoki.judgels.commons.models.daos.interfaces.Dao;

import java.util.Map;

public final class DaoFactory {
    private static DaoFactory INSTANCE;

    private final Map<Class<? extends Dao<?, ?>>, Dao<?, ?>> daos;

    private DaoFactory() {
        daos = Maps.newHashMap();
    }

    public void putDao(Class<? extends Dao<?, ?>> clazz, Dao<?, ?> dao) {
        daos.put(clazz, dao);
    }

    @SuppressWarnings("unchecked")
    public <T extends Dao<?, ?>> T getDao(Class<T> clazz) {
        Preconditions.checkArgument(daos.containsKey(clazz));

        return (T) daos.get(clazz);
    }

    public static DaoFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DaoFactory();
        }
        return INSTANCE;
    }
}
