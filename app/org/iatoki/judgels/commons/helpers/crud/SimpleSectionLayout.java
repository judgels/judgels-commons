package org.iatoki.judgels.commons.helpers.crud;

import org.iatoki.judgels.commons.helpers.WrappedContents;

public final class SimpleSectionLayout implements SectionLayout {

    @Override
    public WrappedContents wrapWithLayout(WrappedContents content) {
        return content.wrapWithIdentityTransformation();
    }
}
