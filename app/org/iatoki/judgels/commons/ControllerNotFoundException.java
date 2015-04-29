package org.iatoki.judgels.commons;

public final class ControllerNotFoundException extends EntityNotFoundException {

    public ControllerNotFoundException() {
        super();
    }

    public ControllerNotFoundException(String s) {
        super(s);
    }

    public ControllerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ControllerNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getEntityName() {
        return "Controller";
    }
}
