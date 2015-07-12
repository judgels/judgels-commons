package org.iatoki.judgels.play.controllers;

import org.iatoki.judgels.play.InternalLink;
import org.iatoki.judgels.play.LazyHtml;
import org.iatoki.judgels.play.views.html.layouts.baseLayout;
import org.iatoki.judgels.play.views.html.layouts.breadcrumbsLayout;
import org.iatoki.judgels.play.views.html.layouts.headerFooterLayout;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import java.util.List;

public abstract class AbstractJudgelsControllerUtils {

    public void appendBreadcrumbsLayout(LazyHtml content, List<InternalLink> links) {
        content.appendLayout(c -> breadcrumbsLayout.render(links, c));
    }

    public abstract void appendSidebarLayout(LazyHtml content);

    public void appendTemplateLayout(LazyHtml content, String title) {
        content.appendLayout(c -> headerFooterLayout.render(c));
        content.appendLayout(c -> baseLayout.render(title, c));
    }

    public Result lazyOk(LazyHtml content) {
        return getResult(content, Http.Status.OK);
    }

    public Result getResult(LazyHtml content, int statusCode) {
        switch (statusCode) {
            case Http.Status.OK:
                return Results.ok(content.render());
            case Http.Status.NOT_FOUND:
                return Results.notFound(content.render());
            default:
                return Results.badRequest(content.render());
        }
    }
}
