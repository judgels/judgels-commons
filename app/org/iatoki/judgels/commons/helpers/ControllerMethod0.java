package org.iatoki.judgels.commons.helpers;

import play.api.mvc.Call;
import scala.runtime.AbstractFunction0;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ControllerMethod0 extends AbstractFunction0<Call> {

    private final Object reverseController;
    private final String name;

    public ControllerMethod0(Object reverseController, String name) {
        this.name = name;
        this.reverseController = reverseController;
    }

    @Override
    public Call apply() {
        try {
            Method method = reverseController.getClass().getMethod(name);
            return (Call) method.invoke(reverseController);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
