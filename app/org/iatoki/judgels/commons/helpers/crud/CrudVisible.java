package org.iatoki.judgels.commons.helpers.crud;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CrudVisible {

    CrudAction[] value() default {
            CrudAction.CREATE,
            CrudAction.UPDATE,
            CrudAction.VIEW,
            CrudAction.LIST
    };
}
