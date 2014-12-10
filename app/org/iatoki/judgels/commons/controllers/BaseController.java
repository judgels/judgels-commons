package org.iatoki.judgels.commons.controllers;

import org.iatoki.judgels.commons.helpers.WrappedContents;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;

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

    protected abstract WrappedContents wrapWithTemplate(WrappedContents content);

    protected abstract Object getReverseController();

    protected Result getResult(WrappedContents content, int statusCode) {
        return ok(content.render(0));
    }
}
