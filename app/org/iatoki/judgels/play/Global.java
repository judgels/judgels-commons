package org.iatoki.judgels.play;

import org.iatoki.judgels.play.services.BaseDataMigrationService;
import org.iatoki.judgels.play.views.html.layouts.baseLayout;
import org.iatoki.judgels.play.views.html.layouts.centerLayout;
import org.iatoki.judgels.play.views.html.layouts.headerFooterLayout;
import org.iatoki.judgels.play.views.html.layouts.headingLayout;
import org.iatoki.judgels.play.views.html.layouts.messageView;
import play.Application;
import play.GlobalSettings;
import play.db.jpa.JPA;
import play.i18n.Messages;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

public abstract class Global extends GlobalSettings {

    private final BaseDataMigrationService dataMigrationService;

    public Global(BaseDataMigrationService dataMigrationService) {
        this.dataMigrationService = dataMigrationService;
    }

    @Override
    public void onStart(Application application) {
        super.onStart(application);
        JPA.withTransaction(() -> {
            dataMigrationService.checkDatabase();
        });
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
