package org.iatoki.judgels.commons.controllers;

import play.mvc.Http;
import play.mvc.Result;

public final class ApplicationController extends BaseController {

    public static Result changeLanguage(String newLang) {
        Http.Context.current().changeLang(newLang);
        response().setCookie("lang", newLang);
        return redirect(request().getHeader("Referer"));
    }

    public static Result showSidebar() {
        response().setCookie("sidebar", "true");
        return redirect(request().getHeader("Referer"));
    }

    public static Result hideSidebar() {
        response().setCookie("sidebar", "false");
        return redirect(request().getHeader("Referer"));
    }

    public static Result enterFullscreen() {
        response().setCookie("fullscreen", "true");
        return redirect(request().getHeader("Referer"));
    }

    public static Result exitFullscreen() {
        response().setCookie("fullscreen", "false");
        return redirect(request().getHeader("Referer"));
    }

    public static Result checkHealth() {
        return ok("");
    }
}
