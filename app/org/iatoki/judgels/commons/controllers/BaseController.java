package org.iatoki.judgels.commons.controllers;

import org.iatoki.judgels.commons.helpers.NamedCall;
import org.iatoki.judgels.commons.helpers.LazyHtml;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.List;

public abstract class BaseController extends Controller {

    private String pageTitle;

    protected BaseController() {
        this.pageTitle = Messages.get("welcome");
    }

    protected final String getPageTitle() {
        return pageTitle;
    }

    protected final void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    protected abstract void wrapWithTemplate(LazyHtml content, List<NamedCall> breadcrumbs);

    protected abstract Object getReverseController();

    protected Result getResult(LazyHtml content, int statusCode) {
        Result result = null;
        switch (statusCode) {
            case Http.Status.OK:
                result = ok(content.render(0));
                break;
            case Http.Status.NOT_FOUND:
                result = notFound(content.render(0));
                break;
            default:
                result = badRequest(content.render(0));
                break;
        }
        return result;
    }
}
