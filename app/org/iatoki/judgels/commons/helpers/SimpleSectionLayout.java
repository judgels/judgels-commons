package org.iatoki.judgels.commons.helpers;

import play.twirl.api.Html;
import org.iatoki.judgels.commons.views.html.crud.simpleSectionView;

public final class SimpleSectionLayout implements SectionLayout {

    @Override
    public void wrapWithLayout(LazyHtml content, Html heading, String modelSlug, long modelId) {
        content.appendTransformation(c -> simpleSectionView.render(heading, c));
    }
}
