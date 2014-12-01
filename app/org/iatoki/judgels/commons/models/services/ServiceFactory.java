package org.iatoki.judgels.commons.models.services;

import java.util.HashMap;
import java.util.Map;

public final class ServiceFactory {
    private static ServiceFactory INSTANCE;

    private final Map<Class, AbstractService> services;

    private ServiceFactory() {
        services = new HashMap<>();
    }

    public void putService(Class clazz, AbstractService service) {
        services.put(clazz, service);
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractService> T getService(Class<T> clazz) {
        return (T) services.get(clazz);
    }

    public static ServiceFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServiceFactory();
        }
        return INSTANCE;
    }
}
