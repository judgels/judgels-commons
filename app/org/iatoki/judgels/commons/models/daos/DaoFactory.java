package org.iatoki.judgels.commons.models.daos;

import org.iatoki.judgels.commons.models.daos.interfaces.Dao;

import java.util.HashMap;
import java.util.Map;

public final class DaoFactory {
    private static DaoFactory INSTANCE;

    private final Map<Class, Dao> daos;

    private DaoFactory() {
        daos = new HashMap<>();
    }

    public void putDao(Class clazz, Dao dao) {
        daos.put(clazz, dao);
    }

    @SuppressWarnings("unchecked")
    public <T extends Dao> T getDao(Class<T> clazz) {
        return (T) daos.get(clazz);
    }

    public static DaoFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DaoFactory();
        }
        return INSTANCE;
    }
}
