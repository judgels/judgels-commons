package org.iatoki.judgels.commons.helpers;

import org.iatoki.judgels.commons.views.html.crud.tabWrapperView;

import java.util.List;

public final class TabbedSectionLayout implements SectionLayout {

    private final List<SectionTab> tabs;

    public TabbedSectionLayout(List<SectionTab> tabs) {
        this.tabs = tabs;
    }

    @Override
    public void wrapWithLayout(LazyHtml content, String modelSlug, long modelId) {
        content.appendTransformation(c -> tabWrapperView.render(tabs, modelSlug, modelId, c));
    }
}
