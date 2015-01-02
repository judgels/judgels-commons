package org.iatoki.judgels.commons;

public class AttributeNotAvailableException extends RuntimeException {
    public AttributeNotAvailableException(String attribute) {
        super("Attribute " + attribute + " is not available");
    }
}
