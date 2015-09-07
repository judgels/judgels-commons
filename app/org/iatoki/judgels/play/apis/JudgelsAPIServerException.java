package org.iatoki.judgels.play.apis;

public abstract class JudgelsAPIServerException extends RuntimeException {

    protected JudgelsAPIServerException(String message) {
        super(message);
    }

    protected JudgelsAPIServerException(Throwable cause) {
        super(cause);
    }
}
