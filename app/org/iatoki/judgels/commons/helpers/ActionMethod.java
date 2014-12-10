package org.iatoki.judgels.commons.helpers;

public abstract class ActionMethod {

    private final String name;

    protected ActionMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
