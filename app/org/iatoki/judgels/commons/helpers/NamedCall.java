package org.iatoki.judgels.commons.helpers;

import play.api.mvc.Call;

public final class NamedCall {
    private String name;
    private Call call;

    public NamedCall(String name, Call call) {
        this.name = name;
        this.call = call;
    }

    public String getName() {
        return name;
    }

    public Call getCall() {
        return call;
    }
}
