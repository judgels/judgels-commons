package org.iatoki.judgels.commons.models.dao;

import org.iatoki.judgels.commons.models.dao.interfaces.Dao;

import java.util.HashMap;
import java.util.Map;

public final class DaoSingletons {
    private static DaoSingletons INSTANCE;

    private final Map<Class, Dao> singletons;

    private DaoSingletons() {
        singletons = new HashMap<>();
    }

    public void putDao(Class clazz, Dao dao) {
        singletons.put(clazz, dao);
    }

    @SuppressWarnings("unchecked")
    public <T extends Dao> T getDao(Class<T> clazz) {
        return (T) singletons.get(clazz);
    }

    public static DaoSingletons getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DaoSingletons();
        }
        return INSTANCE;
    }
}
