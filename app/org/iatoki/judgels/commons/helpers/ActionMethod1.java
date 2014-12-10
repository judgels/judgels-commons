package org.iatoki.judgels.commons.helpers;

import play.api.mvc.Call;
import scala.Function1;
import scala.runtime.AbstractFunction1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ActionMethod1<T> extends ActionMethod {

    private final Class<?> c1;

    public ActionMethod1(String name, Class<?> c1) {
        super(name);
        this.c1 = c1;
    }

    public Call generateCall(Object reverseController, T arg) {
        try {
            Method method = reverseController.getClass().getMethod(getName(), c1);
            return (Call) method.invoke(reverseController, arg);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Function1<T, Call> generateCallFunction(Object reverseController) {
        return new AbstractFunction1<T, Call>() {
            @Override
            public Call apply(T arg) {
                return generateCall(reverseController, arg);
            }
        };
    }
}
