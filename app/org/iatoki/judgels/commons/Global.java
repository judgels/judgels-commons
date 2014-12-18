package org.iatoki.judgels.commons;

import play.GlobalSettings;
import play.api.mvc.EssentialFilter;
import play.filters.csrf.CSRFFilter;
import play.filters.gzip.GzipFilter;

public abstract class Global extends GlobalSettings {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends EssentialFilter> Class<T>[] filters() {
        return new Class[]{CSRFFilter.class, GzipFilter.class};
    }
}
