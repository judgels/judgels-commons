package org.iatoki.judgels.commons.helpers;

public final class SimpleSectionLayout implements SectionLayout {

    @Override
    public void wrapWithLayout(LazyHtml content, String modelSlug, long modelId) {
        content.appendIdentityTransformation();
    }
}
