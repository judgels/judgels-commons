package org.iatoki.judgels.commons.helpers;

import com.google.common.collect.ImmutableList;
import play.twirl.api.Html;

import java.util.List;

public final class WrappedContents {

    private final Html baseContent;
    private final List<ContentTransformation> transformations;

    private WrappedContents(Html baseContent, List<ContentTransformation> transformations) {
        this.baseContent = baseContent;
        this.transformations = transformations;
    }

    public WrappedContents(Html baseContent) {
        this.baseContent = baseContent;
        this.transformations = ImmutableList.of();
    }

    public WrappedContents wrapWithTransformation(ContentTransformation transformation) {
        ImmutableList.Builder<ContentTransformation> newTransformations = ImmutableList.builder();
        newTransformations.addAll(transformations);
        newTransformations.add(transformation);
        return new WrappedContents(baseContent, newTransformations.build());
    }

    public WrappedContents wrapWithIdentityTransformation() {
        return wrapWithTransformation(content -> content);
    }

    public Html render(int level) {
        Html content = baseContent;

        for (ContentTransformation transformation : transformations) {
            content = transformation.apply(content);
        }

        return content;
    }
}
