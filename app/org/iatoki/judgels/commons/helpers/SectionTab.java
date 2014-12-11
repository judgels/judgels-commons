package org.iatoki.judgels.commons.helpers;

import play.api.mvc.Call;

import java.util.function.Function;

public final class SectionTab {
    private final String name;
    private final Function<Long, Call> callGenerator;

    public SectionTab(String name, Function<Long, Call> callGenerator) {
        this.name = name;
        this.callGenerator = callGenerator;
    }

    public String getName() {
        return name;
    }

    public Call getCall(long modelId) {
        return callGenerator.apply(modelId);
    }
}
