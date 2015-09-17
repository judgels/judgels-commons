package org.iatoki.judgels.play.controllers;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;
import org.iatoki.judgels.play.LazyHtml;
import org.iatoki.judgels.play.HtmlTemplate;
import org.iatoki.judgels.play.views.html.base.baseLayout;
import org.iatoki.judgels.play.views.html.base.breadcrumbsLayout;
import org.iatoki.judgels.play.views.html.base.headerFooterLayout;
import org.iatoki.judgels.play.views.html.base.singleColumnLayout;
import org.iatoki.judgels.play.views.html.base.twoColumnLayout;
import org.iatoki.judgels.play.views.html.content.contentLayout;
import org.iatoki.judgels.play.views.html.content.mainTabsLayout;
import org.iatoki.judgels.play.views.html.content.mainTitleLayout;
import org.iatoki.judgels.play.views.html.content.secondaryTabsLayout;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Html;

@EntityNotFoundGuard
@UnsupportedOperationGuard
public abstract class AbstractJudgelsController extends Controller {

    protected static void flashInfo(String message) {
        flash("flashInfo", message);
    }

    protected static void flashError(String message) {
        flash("flashError", message);
    }

    protected static boolean formHasErrors(Form form) {
        return form.hasErrors() || form.hasGlobalErrors();
    }

    protected static Result redirectToReferer() {
        return redirect(request().getHeader(REFERER));
    }

    protected static Result lazyOk(LazyHtml content) {
        return getResult(content, Http.Status.OK);
    }

    protected static Result getResult(LazyHtml content, int statusCode) {
        HtmlCompressor htmlCompressor = new HtmlCompressor();
        Html compressedContent = new Html(htmlCompressor.compress(content.render().body()));
        switch (statusCode) {
            case Http.Status.OK:
                return Results.ok(compressedContent);
            case Http.Status.NOT_FOUND:
                return Results.notFound(compressedContent);
            default:
                return Results.badRequest(compressedContent);
        }
    }

    protected Result renderTemplate(HtmlTemplate template) {
        LazyHtml content = template.getContent();

        if (template.hasSecondaryTabs()) {
            content.appendLayout(c -> secondaryTabsLayout.render(template.getSecondaryTabs(), c));
        }

        if (template.hasMainTabs()) {
            content.appendLayout(c -> mainTabsLayout.render(template.getMainTabs(), c));
        }

        if (template.hasMainTitle()) {
            content.appendLayout(c -> mainTitleLayout.render(template.getMainTitle(), template.getMainButtons(), template.getMainBackButton(), c));
        }

        content.appendLayout(c -> contentLayout.render(c));

        if (template.isSingleColumn()) {
            content.appendLayout(c -> singleColumnLayout.render(c));
        } else {
            content.appendLayout(c -> twoColumnLayout.render(template.getSidebarMenus(), c));
        }

        content.appendLayout(c -> breadcrumbsLayout.render(template.getBreadcrumbLinks(), c));
        content.appendLayout(c -> headerFooterLayout.render(c));
        content.appendLayout(c -> baseLayout.render(template.getPageTitle(), c));

        return lazyOk(content);
    }
}
