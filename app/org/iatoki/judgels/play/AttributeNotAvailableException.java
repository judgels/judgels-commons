package org.iatoki.judgels.play;

public class AttributeNotAvailableException extends RuntimeException {
    public AttributeNotAvailableException(String attribute) {
        super("Attribute " + attribute + " is not available");
    }
}
