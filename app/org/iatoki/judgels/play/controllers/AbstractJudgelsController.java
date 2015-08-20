package org.iatoki.judgels.play.controllers;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

@EntityNotFoundGuard
@UnsupportedOperationGuard
public abstract class AbstractJudgelsController extends Controller {

    protected static boolean formHasErrors(Form form) {
        return form.hasErrors() || form.hasGlobalErrors();
    }

    protected static Result redirectToReferer() {
        return redirect(request().getHeader("Referer"));
    }
}
