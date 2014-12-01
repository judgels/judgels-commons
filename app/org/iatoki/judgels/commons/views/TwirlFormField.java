package org.iatoki.judgels.commons.views;

import java.util.List;

public final class TwirlFormField {

    private final String fieldType;

    private final String fieldKey;

    private final String fieldName;

    private final String fieldDefaultValue;

    private List<String> fieldReferences;

    public TwirlFormField(String fieldType, String fieldKey, String fieldName, String fieldDefaultValue) {
        this.fieldType = fieldType;
        this.fieldKey = fieldKey;
        this.fieldName = fieldName;
        this.fieldDefaultValue = fieldDefaultValue;
    }

    public TwirlFormField(String fieldType, String fieldKey, String fieldName, String fieldDefaultValue, List<String> fieldReferences) {
        this.fieldType = fieldType;
        this.fieldKey = fieldKey;
        this.fieldName = fieldName;
        this.fieldDefaultValue = fieldDefaultValue;
        this.fieldReferences = fieldReferences;
    }

    public String getFieldType() {
        return fieldType;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldDefaultValue() {
        return fieldDefaultValue;
    }

    public List<String> getFieldReferences() {
        return fieldReferences;
    }
}
