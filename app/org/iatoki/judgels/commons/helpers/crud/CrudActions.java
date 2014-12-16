package org.iatoki.judgels.commons.helpers.crud;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.Arrays;

public final class CrudActions {

    private CrudActions() {
        // prevents instantiation
    }

    public static boolean isAppliedTo(CrudAction action, Field field) {
        if (field.isAnnotationPresent(Id.class)) {
            return action == CrudAction.LIST || action == CrudAction.VIEW;
        }

        CrudVisible visibilities = field.getAnnotation(CrudVisible.class);
        return (visibilities != null) && (Arrays.asList(visibilities.value()).contains(action));
    }
}
