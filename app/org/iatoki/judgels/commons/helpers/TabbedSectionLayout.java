package org.iatoki.judgels.commons.helpers;

import com.google.common.collect.Lists;
import org.iatoki.judgels.commons.views.html.crud.tabbedSectionView;
import play.twirl.api.Html;

import java.util.List;
import java.util.function.Function;

public final class TabbedSectionLayout implements SectionLayout {

    private final List<Function<Long, NamedCall>> tabs;

    public TabbedSectionLayout(List<Function<Long, NamedCall>> tabs) {
        this.tabs = tabs;
    }

    @Override
    public void wrapWithLayout(LazyHtml content, Html heading, String modelSlug, long modelId) {
        List<NamedCall> actualTabs = Lists.transform(tabs, tab -> tab.apply(modelId));
        content.appendTransformation(c -> tabbedSectionView.render(actualTabs, modelSlug, heading, c));
    }
}
