package org.iatoki.judgels.commons.controllers;

import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.SerializeException;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationErrorResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationRequest;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationSuccessResponse;
import com.nimbusds.openid.connect.sdk.OIDCAccessTokenResponse;
import org.iatoki.judgels.commons.JophielClientService;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public final class JophielClientController extends Controller {

    public static Result auth(String returnUri) {

        AuthenticationRequest authRequest = JophielClientService.generateAuthRequest(getRedirectUri(), returnUri);

        URI uri;
        try {
            uri = authRequest.toURI();
        } catch (SerializeException e) {
            throw new RuntimeException(e);
        }

        return redirect(uri.toString());
    }

    public static Result login() {
        URI requestUri;

        try {
            requestUri = new URI(request().uri());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        AuthenticationResponse authResponse = JophielClientService.parseAuthResponse(requestUri);

        if (authResponse instanceof AuthenticationErrorResponse) {
            throw new RuntimeException("Authentication error");
        }

        AuthenticationSuccessResponse successResponse = (AuthenticationSuccessResponse) authResponse;

        AuthorizationCode code = successResponse.getAuthorizationCode();
        String returnUri = successResponse.getState().getValue();

        TokenRequest tokenRequest = JophielClientService.generateTokenRequest(getRedirectUri(), code);

        HTTPRequest httpRequest;
        try {
            httpRequest = tokenRequest.toHTTPRequest();
        } catch (SerializeException e) {
            throw new RuntimeException(e);
        }

        HTTPResponse httpResponse;
        try {
            httpResponse = httpRequest.send();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        OIDCAccessTokenResponse accessTokenResponse = JophielClientService.parseAccessTokenResponse(httpResponse);

        session().clear();
        session("username", JophielClientService.parseUsername(accessTokenResponse));
        session("accessToken", JophielClientService.parseAccessToken(accessTokenResponse).toString());
        session("expirationTime", "" + JophielClientService.parseExpirationTimeMillis(accessTokenResponse));

        return redirect(returnUri);
    }

    public static Result logout(String returnUri) {
        session().clear();
        return redirect(returnUri);
    }

    private static URI getRedirectUri() {
        try {
            return new URI(routes.JophielClientController.login().absoluteURL(request()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
