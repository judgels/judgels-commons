package org.iatoki.judgels.commons.helpers;

import play.api.mvc.Call;
import scala.Function0;
import scala.runtime.AbstractFunction0;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ActionMethod0 extends ActionMethod {

    public ActionMethod0(String name) {
        super(name);
    }

    public Call generateCall(Object reverseController) {
        try {
            Method method = reverseController.getClass().getMethod(getName());
            return (Call) method.invoke(reverseController);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Function0<Call> generateCallFunction(Object reverseController) {
        return new AbstractFunction0<Call>() {
            @Override
            public Call apply() {
                return generateCall(reverseController);
            }
        };
    }
}
