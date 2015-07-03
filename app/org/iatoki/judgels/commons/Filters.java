package org.iatoki.judgels.commons;

import play.api.mvc.EssentialFilter;
import play.filters.gzip.GzipFilter;
import play.http.HttpFilters;

import javax.inject.Inject;

public final class Filters implements HttpFilters {

    private final GzipFilter gzipFilter;

    @Inject
    public Filters(GzipFilter gzipFilter) {
        this.gzipFilter = gzipFilter;
    }

    @Override
    public EssentialFilter[] filters() {
        return new EssentialFilter[] { gzipFilter };
    }
}
