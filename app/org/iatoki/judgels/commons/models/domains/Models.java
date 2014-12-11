package org.iatoki.judgels.commons.models.domains;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public final class Models {

    private Models() {
        // prevent instantiation
    }

    public static String getModelSlug(Class<?> clazz) {
        return StringUtils.lowerCase(clazz.getSimpleName());
    }

    public static String getFieldSlug(Field field) {
        return getModelSlug(field.getDeclaringClass()) + ".field." + field.getName();
    }

    public static <M> M newModel(Class<M> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiate model " + clazz.getSimpleName());
        }
    }

    public static List<Field> getFields(Class<?> clazz) {
        return Arrays.asList(clazz.getDeclaredFields());
    }
}
