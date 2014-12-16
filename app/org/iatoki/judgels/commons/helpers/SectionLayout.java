package org.iatoki.judgels.commons.helpers;

import play.twirl.api.Html;

public interface SectionLayout {
    void wrapWithLayout(LazyHtml content, Html heading, String modelSlug, long modelId);
}
