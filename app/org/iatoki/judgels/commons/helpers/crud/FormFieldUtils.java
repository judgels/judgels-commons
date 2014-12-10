package org.iatoki.judgels.commons.helpers.crud;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.iatoki.judgels.commons.models.daos.DaoFactory;
import org.iatoki.judgels.commons.models.domains.Model;
import org.iatoki.judgels.commons.models.domains.ReferenceModel;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;

public final class FormFieldUtils {

    private static final Map<Class<?>, FormFieldType> DEFAULT_FORM_FIELD_TYPES = ImmutableMap.of(
            boolean.class, FormFieldType.CHECKBOX,
            File.class, FormFieldType.FILE,
            Date.class, FormFieldType.DATE
    );

    private FormFieldUtils() {
        // prevent instantiation
    }


    public static FormField createFromJavaField(Field field) {
        FormFieldType type = getType(field);
        Map<String, String> htmlArgs = getHtmlArgs(field);
        List<String> options = getOptions(field);

        return new FormField(field.getName(), type, htmlArgs, options);
    }

    private static FormFieldType getType(Field field) {
        if (field.isAnnotationPresent(FormFieldCustomType.class)) {
            return field.getAnnotation(FormFieldCustomType.class).value();
        } else {
            return getDefaultType(field.getType());
        }
    }

    private static FormFieldType getDefaultType(Class<?> clazz) {
        for (Map.Entry<Class<?>, FormFieldType> entry : DEFAULT_FORM_FIELD_TYPES.entrySet()) {
            if (entry.getKey().equals(clazz)) {
                return entry.getValue();
            }
        }

        return FormFieldType.TEXT;
    }

    private static Map<String, String> getHtmlArgs(Field field) {
        ImmutableMap.Builder<String, String> htmlArgs = ImmutableMap.builder();

        if (field.isAnnotationPresent(FormFieldCustomDefaultValue.class)) {
            htmlArgs.put("_value", field.getAnnotation(FormFieldCustomDefaultValue.class).value());
        }

        String modelSlug = StringUtils.lowerCase(field.getDeclaringClass().getSimpleName());
        htmlArgs.put("_label", modelSlug + ".field." + field.getName());

        return htmlArgs.build();
    }

    private static List<String> getOptions(Field field) {
        if (!field.isAnnotationPresent(FormFieldCustomOptions.class)) {
            return null;
        }

        FormFieldCustomOptions optionsInfo = field.getAnnotation(FormFieldCustomOptions.class);
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

        ImmutableList.Builder<String> data = ImmutableList.builder();

        for (Object value : clazz.getEnumConstants()) {
            data.add(value.toString());
        }

        return data.build();
    }

    private static List<String> getOptionsFromTable(Class clazz) {
        @SuppressWarnings("unchecked")
        List<Model> models = DaoFactory.getInstance().getDao(clazz).findAll();

        ImmutableList.Builder<String> data = ImmutableList.builder();
        for (Model model : models) {
            if (model.getClass().isAssignableFrom(ReferenceModel.class)) {
                String referenceName = ((ReferenceModel) model).getName();
                data.add(referenceName);
            } else {
                throw new ClassCastException();
            }
        }

        return data.build();
    }
}
