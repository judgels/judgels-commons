package org.iatoki.judgels.commons.config;

import com.google.inject.AbstractModule;
import org.reflections.Reflections;

import javax.inject.Named;
import java.util.Set;

public abstract class JudgelsAbstractModule extends AbstractModule {

    @Override
    protected final void configure() {
        manualBinding();
        bindDaos();
        bindServices();
    }

    protected abstract String getDaosImplPackage();

    protected abstract String getServicesImplPackage();

    protected void manualBinding() {
    }

    private void bindDaos() {
        bindByNamedAnnotations(getDaosImplPackage());
    }

    private void bindServices() {
        bindByNamedAnnotations(getServicesImplPackage());
    }

    private void bindByNamedAnnotations(String packageName) {
        Reflections scanner = new Reflections(packageName);
        Set<Class<?>> impls = scanner.getTypesAnnotatedWith(Named.class);
        for (Class<?> impl : impls) {
            for (Class<?> interfaze : impl.getInterfaces()) {
                bind(interfaze).to(interfaze.getClass().cast(impl));
            }
        }
    }
}
