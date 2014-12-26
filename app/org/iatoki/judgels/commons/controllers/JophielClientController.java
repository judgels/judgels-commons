package org.iatoki.judgels.commons.controllers;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.SerializeException;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.openid.connect.sdk.AuthenticationErrorResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationRequest;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationResponseParser;
import com.nimbusds.openid.connect.sdk.AuthenticationSuccessResponse;
import com.nimbusds.openid.connect.sdk.Nonce;
import com.nimbusds.openid.connect.sdk.OIDCAccessTokenResponse;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public final class JophielClientController extends Controller {

    public static Result login(String returnUri) {
        URI endpoint = getEndpoint("auth");
        ResponseType responseType = new ResponseType(ResponseType.Value.CODE);
        Scope scope = Scope.parse("openid");
        ClientID clientId = getClientId();
        URI redirectUri = getRedirectUri();

        State state = new State(returnUri);
        Nonce nonce = new Nonce();

        AuthenticationRequest authRequest = new AuthenticationRequest(endpoint, responseType, scope, clientId, redirectUri, state, nonce);

        URI authRequestUri;
        try {
            authRequestUri = authRequest.toURI();
        } catch (SerializeException e) {
            throw new RuntimeException(e);
        }

        return redirect(authRequestUri.toString());
    }

    public static Result verify() {
        URI authResponseRequestUri;

        try {
            authResponseRequestUri = new URI(request().uri());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        AuthenticationResponse authResponse;
        try {
            authResponse = AuthenticationResponseParser.parse(authResponseRequestUri);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (authResponse instanceof AuthenticationErrorResponse) {
            throw new RuntimeException("Authentication error");
        }

        AuthenticationSuccessResponse successResponse = (AuthenticationSuccessResponse) authResponse;

        AuthorizationCode authCode = successResponse.getAuthorizationCode();
        String returnUri = successResponse.getState().getValue();

        URI endpoint = getEndpoint("token");
        ClientAuthentication clientAuth = new ClientSecretBasic(getClientId(), getClientSecret());
        AuthorizationCodeGrant grant = new AuthorizationCodeGrant(authCode, getRedirectUri());
        Scope scope = new Scope("openid");

        TokenRequest tokenRequest = new TokenRequest(endpoint, clientAuth, grant, scope);

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

        OIDCAccessTokenResponse accessTokenResponse;
        try {
            accessTokenResponse = OIDCAccessTokenResponse.parse(httpResponse);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        AccessToken accessToken = accessTokenResponse.getAccessToken();
        JWT idToken = accessTokenResponse.getIDToken();

        ReadOnlyJWTClaimsSet claimsSet;
        try {
            claimsSet = idToken.getJWTClaimsSet();
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }

        String username = claimsSet.getSubject();
        long expirationTime = System.currentTimeMillis() + Long.valueOf(accessTokenResponse.getCustomParams().get("expire_in").toString());

        session().clear();
        session("username", username);
        session("accessToken", accessToken.toString());
        session("expirationTime", "" + expirationTime);

        return redirect(returnUri);
    }

    public static Result logout(String returnUri) {
        session().clear();
        return redirect(returnUri);
    }

    private static URI getRedirectUri() {
        try {
            return new URI(routes.JophielClientController.verify().absoluteURL(request()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static ClientID getClientId() {
        String clientId = Play.application().configuration().getString("jophiel.clientId");
        if (clientId == null) {
            throw new IllegalStateException("jophiel.clientId not found in configuration");
        }
        return new ClientID(clientId);
    }

    private static Secret getClientSecret() {
        String clientSecret = Play.application().configuration().getString("jophiel.clientSecret");
        if (clientSecret == null) {
            throw new IllegalStateException("jophiel.clientSecret not found in configuration");
        }
        return new Secret(clientSecret);
    }

    private static URI getEndpoint(String service) {
        String baseUrl = Play.application().configuration().getString("jophiel.baseUrl");
        if (baseUrl == null) {
            throw new IllegalStateException("jophiel.baseUrl not found in configuration");
        }

        try {
            return new URI(baseUrl + "/" + service);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("jophiel.baseUrl malformed in configuration");
        }
    }
}
