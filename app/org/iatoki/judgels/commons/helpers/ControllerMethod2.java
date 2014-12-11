package org.iatoki.judgels.commons.helpers;

import play.api.mvc.Call;
import scala.runtime.AbstractFunction2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ControllerMethod2<T1, T2> extends AbstractFunction2<T1, T2, Call> {

    private final Object reverseController;
    private final String name;
    private final Class<?> c1;
    private final Class<?> c2;

    public ControllerMethod2(Object reverseController, String name, Class<?> c1, Class<?> c2) {
        this.reverseController = reverseController;
        this.name = name;
        this.c1 = c1;
        this.c2 = c2;
    }

    @Override
    public Call apply(T1 arg1, T2 arg2) {
        try {
            Method method = reverseController.getClass().getMethod(name, c1, c2);
            return (Call) method.invoke(reverseController, arg1, arg2);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
