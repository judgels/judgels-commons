package org.iatoki.judgels.commons.controllers;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

public final class ApplicationController extends Controller {

    public static Result changeLanguage(String newLang) {
        Http.Context.current().changeLang(newLang);
        response().setCookie("lang", newLang);
        return redirect(request().getHeader("Referer"));
    }

}
