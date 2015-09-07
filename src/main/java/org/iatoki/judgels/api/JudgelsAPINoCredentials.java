package org.iatoki.judgels.api;

import org.apache.http.client.methods.HttpRequestBase;

public final class JudgelsAPINoCredentials implements JudgelsAPICredentials {

    @Override
    public void applyToHttpRequest(HttpRequestBase httpRequest) {
        // nothing
    }
}
