package org.iatoki.judgels.commons.helpers.crud;

import java.util.List;
import java.util.Map;

public final class FormField {
    private final String name;
    private final FormFieldType type;
    private final Map<String, String> htmlArgs;
    private final List<String> options;

    public FormField(String name, FormFieldType type, Map<String, String> htmlArgs, List<String> options) {
        this.name = name;
        this.type = type;
        this.htmlArgs = htmlArgs;
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public FormFieldType getType() {
        return type;
    }

    public Map<String, String> getHtmlArgs() {
        return htmlArgs;
    }

    public List<String> getOptions() {
        return options;
    }

    public FormField toStatic() {
        return new FormField(name, FormFieldType.STATIC, htmlArgs, options);
    }
}
