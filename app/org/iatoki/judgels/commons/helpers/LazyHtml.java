package org.iatoki.judgels.commons.helpers;

import com.google.common.collect.Lists;
import play.twirl.api.Html;

import java.util.List;

public final class LazyHtml {

    private final Html baseContent;
    private final List<ContentTransformation> transformations;

    public LazyHtml(Html baseContent) {
        this.baseContent = baseContent;
        this.transformations = Lists.newArrayList();
    }

    public void appendTransformation(ContentTransformation transformation) {
        transformations.add(transformation);
    }

    public void appendIdentityTransformation() {
        transformations.add(content -> content);
    }

    public Html render(int level) {
        Html content = baseContent;

        for (ContentTransformation transformation : transformations) {
            content = transformation.apply(content);
        }

        return content;
    }
}
