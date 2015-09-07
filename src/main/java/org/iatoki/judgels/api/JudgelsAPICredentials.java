package org.iatoki.judgels.api;

import org.apache.http.client.methods.HttpRequestBase;

public interface JudgelsAPICredentials {

    void applyToHttpRequest(HttpRequestBase httpRequest);
}
