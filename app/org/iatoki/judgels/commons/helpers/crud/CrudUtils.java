package org.iatoki.judgels.commons.helpers.crud;

import java.lang.reflect.Field;
import java.util.Arrays;

public final class CrudUtils {

    private CrudUtils() {
        // prevent instantiation
    }

    public static boolean isVisibleInAction(Field field, CrudAction action) {
        CrudVisible visibilities = field.getAnnotation(CrudVisible.class);
        return visibilities != null && Arrays.asList(visibilities.value()).contains(action);
    }
}
