package org.iatoki.judgels.commons.views;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FormField {

    ReferenceType reference() default ReferenceType.NO_REFERENCE;

    Class accessorEntity() default void.class;

    String formName() default "";

    String defaultValue() default "";

    FormFieldType forceType() default FormFieldType.TEXT;

    boolean list() default true;

    boolean create() default true;

    boolean update() default true;

    boolean view() default true;
}
