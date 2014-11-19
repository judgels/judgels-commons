package org.iatoki.judgels.commons.controllers;

import org.iatoki.judgels.commons.models.ModelField;
import org.iatoki.judgels.commons.models.ReferenceType;
import org.iatoki.judgels.commons.models.TwirlFormField;
import org.iatoki.judgels.commons.models.dao.DaoSingletons;
import org.iatoki.judgels.commons.models.dao.interfaces.Dao;
import org.iatoki.judgels.commons.models.schema.Model;
import org.iatoki.judgels.commons.models.schema.ReferenceModel;
import org.iatoki.judgels.commons.views.html.crud.add;
import play.api.mvc.Call;
import play.mvc.Controller;
import play.mvc.Result;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public abstract class CrudController<M extends Model, D extends Dao> extends Controller {

    private final Class<M> modelClass;
    private final Class<D> daoClass;

    protected Class<M> getModelClass() {
        return modelClass;
    }

    protected Class<D> getDaoClass() {
        return daoClass;
    }

    protected abstract Object getReverseClass();

    @SuppressWarnings("unchecked")
    protected CrudController() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        modelClass = (Class<M>) genericSuperclass.getActualTypeArguments()[0];
        daoClass = (Class<D>) genericSuperclass.getActualTypeArguments()[1];
    }

    public Result add() {
        List<TwirlFormField> twirlFormFieldList = new ArrayList<>();
        Field[] fields = modelClass.getDeclaredFields();

        for (Field field: fields) {
            TwirlFormField twirlFormField = convertJavaFieldtoTwirlField(field);
            twirlFormFieldList.add(twirlFormField);
        }

        Call call = generateCallFromString("doAdd");

        return ok(add.render(call, twirlFormFieldList));
    }

    public Result list() {
        return null;
    }

    public Result view() {
        return null;
    }

    public Result search() {
        return null;
    }

    public Result update() {
        return null;
    }

    public Result delete() {
        return null;
    }

    public Result doAdd() {
        return null;
    }

    public Result doUpdate() {
        return null;
    }

    public Result doDelete() {
        return null;
    }

    private Call generateCallFromString(String s) {
        Call call = null;
        Object reverseClass = this.getReverseClass();
        try {
            Method method = reverseClass.getClass().getMethod(s);
            call = (Call) method.invoke(reverseClass);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return call;
    }

    @SuppressWarnings("unchecked")
    private TwirlFormField convertJavaFieldtoTwirlField(Field field) {
        TwirlFormField twirlFormField = null;
        ModelField modelField = field.getAnnotation(ModelField.class);
        if (modelField != null) {
            if ((ReferenceType.ENUM.equals(modelField.reference())) && (modelField.accessorEntity() != null)) {
                List<String> references = new ArrayList<>();
                Object[] enumValues = modelField.accessorEntity().getEnumConstants();
                for (Object enumValue: enumValues) {
                    references.add(enumValue.toString());
                }
                twirlFormField = new TwirlFormField(field.getType().getCanonicalName(), field.getName(), modelField.formName(), modelField.defaultValue(), references);
            } else if ((ReferenceType.TABLE.equals(modelField.reference())) && (modelField.accessorEntity() != null)) {
                List<String> references = new ArrayList<>();
                List<Model> modelReferences = DaoSingletons.getInstance().getDao(modelField.accessorEntity()).findAll();
                for (Model m : modelReferences) {
                    if (m.getClass().isAssignableFrom(ReferenceModel.class)) {
                        String referenceName = ((ReferenceModel) m).getName();
                        references.add(referenceName);
                    } else {
                        throw new ClassCastException();
                    }
                }
                twirlFormField = new TwirlFormField(field.getType().getCanonicalName(), field.getName(), modelField.formName(), modelField.defaultValue(), references);
            } else {
                twirlFormField = new TwirlFormField(field.getType().getCanonicalName(), field.getName(), modelField.formName(), modelField.defaultValue());
            }
        }

        return twirlFormField;
    }

}
