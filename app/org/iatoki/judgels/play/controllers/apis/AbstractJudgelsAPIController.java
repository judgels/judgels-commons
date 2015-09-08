package org.iatoki.judgels.play.controllers.apis;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.iatoki.judgels.play.JudgelsAppClient;
import org.iatoki.judgels.play.apis.JudgelsAPIBadRequestException;
import org.iatoki.judgels.play.apis.JudgelsAPIUnauthorizedException;
import org.iatoki.judgels.play.apis.JudgelsAppClientAPIIdentity;
import org.iatoki.judgels.play.services.JudgelsAppClientService;
import play.mvc.Controller;

@JudgelsAPIGuard
public abstract class AbstractJudgelsAPIController extends Controller {

    protected static JudgelsAppClientAPIIdentity authenticateAsJudgelsAppClient(JudgelsAppClientService clientService) {
        if (!request().hasHeader("Authorization")) {
            throw new JudgelsAPIUnauthorizedException("Requires authentication.");
        }

        String authorization = request().getHeader("Authorization");

        if (!authorization.startsWith("Basic ")) {
            throw new JudgelsAPIUnauthorizedException("Requires authentication.");
        }

        String credentialsString = new String(Base64.decodeBase64(authorization.substring("Basic ".length())));
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(credentialsString);

        String clientJid = credentials.getUserName();
        String clientSecret = credentials.getPassword();

        if (!clientService.clientExistsByJid(clientJid)) {
            throw new JudgelsAPIUnauthorizedException("Bad credentials.");
        }

        JudgelsAppClient client = clientService.findClientByJid(clientJid);

        if (!client.getSecret().equals(clientSecret)) {
            throw new JudgelsAPIUnauthorizedException("Bad credentials.");
        }

        return new JudgelsAppClientAPIIdentity(client.getJid(), client.getName());
    }

    protected static <T> T parseRequestBody(Class<T> clazz) {
        try {
            return new Gson().fromJson(request().body().asText(), clazz);
        } catch (JsonSyntaxException e) {
            throw new JudgelsAPIBadRequestException("Bad JSON body.");
        }
    }

    protected static void setAccessControlOrigin(String domains, String methods, long maxAge) {
        response().setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, domains);
        response().setHeader(ACCESS_CONTROL_ALLOW_METHODS, methods);
        response().setHeader(ACCESS_CONTROL_MAX_AGE, maxAge + "");
        response().setHeader(ACCESS_CONTROL_ALLOW_HEADERS, StringUtils.join(new String[] {ORIGIN, X_REQUESTED_WITH, CONTENT_TYPE, ACCEPT, AUTHORIZATION}, ','));
    }

    protected static String createJsonPResponse(String callback, String json) {
        StringBuilder sb = new StringBuilder(callback);
        sb.append("(").append(json).append(")");
        return sb.toString();
    }
}
