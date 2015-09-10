package org.iatoki.judgels.play.apis;

public final class JudgelsAPINotFoundException extends JudgelsAPIServerException {

    public JudgelsAPINotFoundException() {
        super("Not found.");
    }
}
