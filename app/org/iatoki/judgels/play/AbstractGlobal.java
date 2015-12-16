package org.iatoki.judgels.play;

import com.google.api.client.auth.oauth2.Credential;
import org.apache.commons.io.FileUtils;
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

import java.io.File;
import java.io.IOException;

/**
 * @deprecated Not needed anymore
 */
@Deprecated
public abstract class AbstractGlobal extends GlobalSettings {

    @Override
    public void onStart(Application application) {
        super.onStart(application);
        JPA.withTransaction(() -> {
                getDataMigrationService().checkDatabase();
            });

        checkAndCopyAssetsToLocal(application);
        checkAndSetupGoogleAnalytics();
    }

    @Override
    public F.Promise<Result> onBadRequest(Http.RequestHeader requestHeader, String s) {
        if (s.contains("Cannot parse")) {
            return onHandlerNotFound(requestHeader);
        }
        return super.onBadRequest(requestHeader, s);
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

    private void checkAndCopyAssetsToLocal(Application application) {
        checkAndCopyFileToLocal(application, "logo.png");
        checkAndCopyFileToLocal(application, "logo-colored.png");
        checkAndCopyFileToLocal(application, "favicon.ico");
    }

    private void checkAndCopyFileToLocal(Application application, String fileName) {
        File assetFile = new File(application.getFile("external-assets"), fileName);
        if (!assetFile.exists()) {
            assetFile.getParentFile().mkdirs();
            try {
                FileUtils.copyInputStreamToFile(getClass().getResourceAsStream("/public/lib/playcommons/images/" + fileName), assetFile);
            } catch (IOException e) {
                throw new IllegalStateException("Cannot create file " + fileName + ".");
            }
        }
    }

    private void checkAndSetupGoogleAnalytics() {
        if (JudgelsPlayProperties.getInstance().isUsingGoogleServiceAccount()) {
            Credential credential = GoogleServiceAuth.createGoogleServiceAuthCredentials(JudgelsPlayProperties.getInstance().getGoogleServiceAccountClientId(), JudgelsPlayProperties.getInstance().getGoogleServiceAccountClientEmail(), JudgelsPlayProperties.getInstance().getGoogleServiceAccountPrivateKeyId(), JudgelsPlayProperties.getInstance().getGoogleServiceAccountPrivateKey());
            GoogleAnalytics.buildInstance(credential, JudgelsPlayProperties.getInstance().getAppName() + "/" + JudgelsPlayProperties.getInstance().getAppVersion());
        }
    }

    protected abstract BaseDataMigrationService getDataMigrationService();
}
