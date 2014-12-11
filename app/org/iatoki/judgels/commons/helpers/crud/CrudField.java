package org.iatoki.judgels.commons.helpers.crud;

import java.util.List;
import java.util.Map;

public final class CrudField {
    private final String name;
    private final CrudFieldType type;
    private final Map<String, String> htmlArgs;
    private final List<String> options;

    public CrudField(String name, CrudFieldType type, Map<String, String> htmlArgs, List<String> options) {
        this.name = name;
        this.type = type;
        this.htmlArgs = htmlArgs;
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public CrudFieldType getType() {
        return type;
    }

    public Map<String, String> getHtmlArgs() {
        return htmlArgs;
    }

    public List<String> getOptions() {
        return options;
    }

    public CrudField toStatic() {
        return new CrudField(name, CrudFieldType.STATIC, htmlArgs, options);
    }
}
