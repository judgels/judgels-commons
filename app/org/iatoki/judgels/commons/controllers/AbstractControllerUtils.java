package org.iatoki.judgels.commons.controllers;

import org.iatoki.judgels.commons.InternalLink;
import org.iatoki.judgels.commons.LazyHtml;
import org.iatoki.judgels.commons.views.html.layouts.baseLayout;
import org.iatoki.judgels.commons.views.html.layouts.breadcrumbsLayout;
import org.iatoki.judgels.commons.views.html.layouts.headerFooterLayout;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import java.util.List;

public abstract class AbstractControllerUtils {

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
                return Results.ok(content.render(0));
            case Http.Status.NOT_FOUND:
                return Results.notFound(content.render(0));
            default:
                return Results.badRequest(content.render(0));
        }
    }
}
