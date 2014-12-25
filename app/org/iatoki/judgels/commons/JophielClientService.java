package org.iatoki.judgels.commons;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.openid.connect.sdk.AuthenticationRequest;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationResponseParser;
import com.nimbusds.openid.connect.sdk.Nonce;
import com.nimbusds.openid.connect.sdk.OIDCAccessTokenResponse;
import play.Play;

import java.net.URI;
import java.net.URISyntaxException;

public final class JophielClientService {

    private JophielClientService() {
        // prevent instantiation
    }

    public static AuthenticationRequest generateAuthRequest(URI redirectUri, String returnUri) {
        URI endpoint = getEndpoint("auth");
        ResponseType responseType = new ResponseType(ResponseType.Value.CODE);
        Scope scope = Scope.parse("openid");
        ClientID clientId = getClientId();

        State state = new State(returnUri);
        Nonce nonce = new Nonce();

        return new AuthenticationRequest(endpoint, responseType, scope, clientId, redirectUri, state, nonce);
    }

    public static AuthenticationResponse parseAuthResponse(URI requestUri) {

        try {
            return AuthenticationResponseParser.parse(requestUri);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static TokenRequest generateTokenRequest(URI redirectUri, AuthorizationCode code) {
        URI endpoint = getEndpoint("token");
        ClientAuthentication clientAuth = new ClientSecretBasic(getClientId(), getClientSecret());
        AuthorizationCodeGrant grant = new AuthorizationCodeGrant(code, redirectUri);
        Scope scope = new Scope("openid");

        return new TokenRequest(endpoint, clientAuth, grant, scope);
    }

    public static OIDCAccessTokenResponse parseAccessTokenResponse(HTTPResponse httpResponse) {
        try {
            return OIDCAccessTokenResponse.parse(httpResponse);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static AccessToken parseAccessToken(OIDCAccessTokenResponse accessTokenResponse) {
        return accessTokenResponse.getAccessToken();
    }

    public static String parseUsername(OIDCAccessTokenResponse accessTokenResponse) {
        JWT idToken = accessTokenResponse.getIDToken();

        ReadOnlyJWTClaimsSet claimsSet;
        try {
            claimsSet = idToken.getJWTClaimsSet();
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }

        return claimsSet.getSubject();
    }

    public static long parseExpirationTimeMillis(OIDCAccessTokenResponse accessTokenResponse) {
        return System.currentTimeMillis() + Long.valueOf(accessTokenResponse.getCustomParams().get("expire_in").toString());
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

    private static URI getEndpoint(String subDir) {
        String baseUrl = Play.application().configuration().getString("jophiel.baseUrl");
        if (baseUrl == null) {
            throw new IllegalStateException("jophiel.baseUrl not found in configuration");
        }

        try {
            return new URI(baseUrl + "/" + subDir);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("jophiel.baseUrl malformed in configuration");
        }
    }
}
