package org.iatoki.judgels.commons;

import org.iatoki.judgels.commons.views.html.layouts.baseLayout;
import org.iatoki.judgels.commons.views.html.layouts.centerLayout;
import org.iatoki.judgels.commons.views.html.layouts.headerFooterLayout;
import org.iatoki.judgels.commons.views.html.layouts.headingLayout;
import org.iatoki.judgels.commons.views.html.layouts.messageView;
import play.GlobalSettings;
import play.api.mvc.EssentialFilter;
import play.filters.gzip.GzipFilter;
import play.i18n.Messages;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

public abstract class Global extends GlobalSettings {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends EssentialFilter> Class<T>[] filters() {
        return new Class[]{GzipFilter.class};
    }

    @Override
    public F.Promise<Result> onHandlerNotFound(Http.RequestHeader requestHeader) {
        return F.Promise.promise(() -> {
                  LazyHtml content = new LazyHtml(messageView.render(Messages.get("commons.pageNotFound.message")));
                  content.appendLayout(c -> headingLayout.render(Messages.get("commons.pageNotFound"), c));
                  content.appendLayout(c -> centerLayout.render(c));
                  content.appendLayout(c -> headerFooterLayout.render(c));
                  content.appendLayout(c -> baseLayout.render("commons.pageNotFound", c));
                  return Results.notFound(content.render());
              }
        );
    }
}
