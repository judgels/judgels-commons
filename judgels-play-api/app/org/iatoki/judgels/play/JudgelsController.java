package org.iatoki.judgels.play;

import javax.inject.Singleton;

import play.mvc.Result;

@Singleton
public final class JudgelsController extends AbstractJudgelsController {
    public Result changeLanguage(String newLang) {
        ctx().changeLang(newLang);
        return redirect(request().getHeader("Referer"));
    }

    public Result showSidebar() {
        response().setCookie("sidebar", "true");
        return redirect(request().getHeader("Referer"));
    }

    public Result hideSidebar() {
        response().setCookie("sidebar", "false");
        return redirect(request().getHeader("Referer"));
    }

    public Result enterFullscreen() {
        response().setCookie("fullscreen", "true");
        return redirect(request().getHeader("Referer"));
    }

    public Result exitFullscreen() {
        response().setCookie("fullscreen", "false");
        return redirect(request().getHeader("Referer"));
    }

    public Result checkHealth() {
        return ok("");
    }
}
