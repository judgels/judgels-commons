package org.iatoki.judgels.play;

import play.twirl.api.Html;

import java.util.function.Function;

public interface HtmlTransformation extends Function<Html, Html> {
    // empty
}
