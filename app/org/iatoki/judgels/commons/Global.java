package org.iatoki.judgels.commons;

import com.google.inject.Guice;
import com.google.inject.Injector;
import play.GlobalSettings;
import play.api.mvc.EssentialFilter;
import play.filters.csrf.CSRFFilter;
import play.filters.gzip.GzipFilter;
import play.filters.headers.SecurityHeadersFilter;

public abstract class Global extends GlobalSettings {

    private static final Injector INJECTOR = createInjector();

    @Override
    @SuppressWarnings("unchecked")
    public <T extends EssentialFilter> Class<T>[] filters() {
        return new Class[]{CSRFFilter.class, GzipFilter.class, SecurityHeadersFilter.class};
    }

    @Override
    public <A> A getControllerInstance(Class<A> controllerClass) throws Exception {
        System.out.println("AA " + controllerClass);
        return INJECTOR.getInstance(controllerClass);
    }

    private static Injector createInjector() {
        return Guice.createInjector();
    }

}
