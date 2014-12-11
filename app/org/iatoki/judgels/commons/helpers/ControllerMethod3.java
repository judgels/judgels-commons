package org.iatoki.judgels.commons.helpers;

import play.api.mvc.Call;
import scala.runtime.AbstractFunction3;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ControllerMethod3<T1, T2, T3> extends AbstractFunction3<T1, T2, T3, Call> {

    private final Object reverseController;
    private String name;
    private final Class<?> c1;
    private final Class<?> c2;
    private final Class<?> c3;

    public ControllerMethod3(Object reverseController, String name, Class<?> c1, Class<?> c2, Class<?> c3) {
        this.reverseController = reverseController;
        this.name = name;
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
    }

    @Override
    public Call apply(T1 arg1, T2 arg2, T3 arg3) {
        try {
            Method method = reverseController.getClass().getMethod(name, c1, c2, c3);
            return (Call) method.invoke(reverseController, arg1, arg2);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
