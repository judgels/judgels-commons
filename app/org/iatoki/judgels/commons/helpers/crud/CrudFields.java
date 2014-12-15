package org.iatoki.judgels.commons.helpers.crud;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.iatoki.judgels.commons.models.daos.DaoFactory;
import org.iatoki.judgels.commons.models.domains.AbstractModel;
import org.iatoki.judgels.commons.models.domains.Models;
import org.iatoki.judgels.commons.models.domains.AbstractReferenceModel;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CrudFields {

    private static final Map<Class<?>, CrudFieldType> DEFAULT_FORM_FIELD_TYPES = ImmutableMap.of(
            boolean.class, CrudFieldType.CHECKBOX,
            File.class, CrudFieldType.FILE,
            Date.class, CrudFieldType.DATE
    );

    private CrudFields() {
        // prevent instantiation
    }

    public static CrudField fromJavaField(Field field) {
        CrudFieldType type = getType(field);
        Map<String, String> htmlArgs = getHtmlArgs(field);
        List<String> options = getOptions(field);

        return new CrudField(field.getName(), type, htmlArgs, options);
    }

    private static CrudFieldType getType(Field field) {
        if (field.isAnnotationPresent(CrudFieldCustomType.class)) {
            return field.getAnnotation(CrudFieldCustomType.class).value();
        } else {
            return getDefaultType(field.getType());
        }
    }

    private static CrudFieldType getDefaultType(Class<?> clazz) {
        for (Map.Entry<Class<?>, CrudFieldType> entry : DEFAULT_FORM_FIELD_TYPES.entrySet()) {
            if (entry.getKey().equals(clazz)) {
                return entry.getValue();
            }
        }

        return CrudFieldType.TEXT;
    }

    private static Map<String, String> getHtmlArgs(Field field) {
        Map<String, String> htmlArgs = Maps.newHashMap();

        if (field.isAnnotationPresent(CrudFieldCustomDefaultValue.class)) {
            htmlArgs.put("_value", field.getAnnotation(CrudFieldCustomDefaultValue.class).value());
        }

        htmlArgs.put("_label", Models.getFieldSlug(field));

        return htmlArgs;
    }

    private static List<String> getOptions(Field field) {
        if (!field.isAnnotationPresent(CrudFieldCustomOptions.class)) {
            return null;
        }

        CrudFieldCustomOptions optionsInfo = field.getAnnotation(CrudFieldCustomOptions.class);
        switch (optionsInfo.source()) {
            case ENUM:
                return getOptionsFromEnum(optionsInfo.data());
            case TABLE:
                return getOptionsFromTable(optionsInfo.data());
            default:
                throw new IllegalStateException("Options source " + optionsInfo.source().toString() + " unknown");
        }
    }

    private static List<String> getOptionsFromEnum(Class<?> clazz) {
        Preconditions.checkArgument(clazz.isEnum());

        return Stream.of(clazz.getEnumConstants())
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private static List<String> getOptionsFromTable(Class clazz) {
        @SuppressWarnings("unchecked")
        List<AbstractModel> models = DaoFactory.getInstance().getDao(clazz).findAll();

        ImmutableList.Builder<String> data = ImmutableList.builder();
        for (AbstractModel model : models) {
            if (model.getClass().isAssignableFrom(AbstractReferenceModel.class)) {
                String referenceName = ((AbstractReferenceModel) model).name;
                data.add(referenceName);
            } else {
                throw new ClassCastException();
            }
        }

        return data.build();
    }
}
