package org.iatoki.judgels.commons.helpers;

import play.api.mvc.Call;
import scala.runtime.AbstractFunction1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ControllerMethod1<T> extends AbstractFunction1<T, Call> {

    private final Object reverseController;
    private final String name;
    private final Class<?> c1;

    public ControllerMethod1(Object reverseController, String name, Class<?> c1) {
        this.reverseController = reverseController;
        this.name = name;
        this.c1 = c1;
    }

    @Override
    public Call apply(T arg) {
        try {
            Method method = reverseController.getClass().getMethod(name, c1);
            return (Call) method.invoke(reverseController, arg);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
