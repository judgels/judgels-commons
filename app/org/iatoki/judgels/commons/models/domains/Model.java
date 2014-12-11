package org.iatoki.judgels.commons.models.domains;

import org.apache.commons.lang3.StringUtils;
import org.iatoki.judgels.commons.helpers.Utilities;

import javax.persistence.MappedSuperclass;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@MappedSuperclass
public abstract class Model {

    public String userCreate;
    public long timeCreate;
    public String ipCreate;
    public String userUpdate;
    public long timeUpdate;
    public String ipUpdate;

    public final void setReflectively(Field field, String valueAsString) {
        String capitalizedFieldName = StringUtils.capitalize(field.getName());
        try {
            Method setterMethod = getClass().getMethod("set" + capitalizedFieldName, field.getType());
            setterMethod.invoke(this, Utilities.castStringtoCertainClass(field.getType(), valueAsString));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
