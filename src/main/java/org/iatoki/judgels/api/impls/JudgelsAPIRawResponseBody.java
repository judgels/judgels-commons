package org.iatoki.judgels.api.impls;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpRequestBase;
import org.iatoki.judgels.api.JudgelsAPIClientException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

public final class JudgelsAPIRawResponseBody {

    private final HttpRequestBase httpRequest;
    private final InputStream responseBody;

    public JudgelsAPIRawResponseBody(HttpRequestBase httpRequest, InputStream responseBody) {
        this.httpRequest = httpRequest;
        this.responseBody = responseBody;
    }

    public String asString() {
        try {
            return IOUtils.toString(responseBody);
        } catch (IOException e) {
            throw new JudgelsAPIClientException(httpRequest, e);
        }
    }

    public InputStream asRawInputStream() {
        return responseBody;
    }

    public <T> T asObjectFromJson(Type type) {
        try {
            return new Gson().fromJson(asString(), type);
        } catch (JsonSyntaxException e) {
            throw new JudgelsAPIClientException(httpRequest, e);
        }
    }
}
