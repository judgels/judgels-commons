package org.iatoki.judgels.commons.controllers;

import org.apache.commons.lang3.StringUtils;
import org.iatoki.judgels.commons.helpers.Page;
import org.iatoki.judgels.commons.helpers.Utilities;
import org.iatoki.judgels.commons.models.daos.DaoFactory;
import org.iatoki.judgels.commons.models.daos.interfaces.Dao;
import org.iatoki.judgels.commons.models.domains.Model;
import org.iatoki.judgels.commons.models.domains.ReferenceModel;
import org.iatoki.judgels.commons.views.FormField;
import org.iatoki.judgels.commons.views.ReferenceType;
import org.iatoki.judgels.commons.views.TwirlFormField;
import org.iatoki.judgels.commons.views.html.crud.createView;
import org.iatoki.judgels.commons.views.html.crud.listView;
import org.iatoki.judgels.commons.views.html.crud.updateView;
import org.iatoki.judgels.commons.views.html.crud.viewView;
import play.api.mvc.Call;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.twirl.api.Html;
import scala.Function1;
import scala.Function4;
import scala.runtime.AbstractFunction1;
import scala.runtime.AbstractFunction4;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public abstract class CrudController<M extends Model, D extends Dao> extends Controller {

    private static final String STATIC = "static";
    private static final String TEXTAREA = "textarea";
    private static final String PASSWORD = "password";
    private static final String HTML = "html";
    private static final String CODE = "code";

    private static final String LIST = "list";
    private static final String CREATE = "create";
    private static final String UPDATE = "update";
    private static final String DELETE = "delete";
    private static final String DO_CREATE = "doCreate";
    private static final String DO_UPDATE = "doUpdate";
    private static final String VIEW = "view";

    private final Class<M> modelClass;
    private final Class<D> daoClass;
    private int pageSize;

    @SuppressWarnings("unchecked")
    protected CrudController() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        modelClass = (Class<M>) genericSuperclass.getActualTypeArguments()[0];
        daoClass = (Class<D>) genericSuperclass.getActualTypeArguments()[1];
        pageSize = 20;
    }

    protected Class<M> getModelClass() {
        return modelClass;
    }

    protected Class<D> getDaoClass() {
        return daoClass;
    }

    protected abstract Object getReverseClass();

    protected abstract Result renderInsideLayout(int statusCode, String title, DynamicForm data, Html html);

    public Result create() {
        DynamicForm data = DynamicForm.form().bindFromRequest();

        List<TwirlFormField> twirlFormFieldList = new ArrayList<>();
        Field[] fields = modelClass.getDeclaredFields();

        for (Field field : fields) {
            FormField formField = field.getAnnotation(FormField.class);
            if ((formField != null) && (formField.create())) {
                TwirlFormField twirlFormField = convertJavaFieldtoTwirlField(field, formField);
                if (twirlFormField != null) {
                    twirlFormFieldList.add(twirlFormField);
                }
            }
        }

        Call call = generateCallFromString(DO_CREATE, new Class[0], new Object[0]);
        Form<M> form = Form.form(modelClass);
        Html html = createView.render(form, call, twirlFormFieldList);

        return renderInsideLayout(Http.Status.ACCEPTED, StringUtils.capitalize(CREATE), data, html);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public Result list(long page, String sortBy, String order, String filterString) {
        DynamicForm data = DynamicForm.form().bindFromRequest();

        List<String> header = new ArrayList<>();
        List<Field> filters = new ArrayList<>();
        Field[] fields = modelClass.getDeclaredFields();

        for (Field field : fields) {
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                header.add("Id");
                filters.add(field);
            } else {
                FormField formField = field.getAnnotation(FormField.class);
                if ((formField != null) && (formField.list())) {
                    header.add(formField.formName());
                    filters.add(field);
                }
            }
        }

        Page<List<String>> pages = DaoFactory.getInstance().getDao(daoClass).pageString(page, pageSize, sortBy, order, filterString, filters);
        Function4<Long, String, String, String, play.mvc.Call> listFunction = this.<Long, String, String, String>generateFunction4CallFromString(LIST, new Class[]{long.class, String.class, String.class, String.class});
        Function1<Object, play.mvc.Call> viewFunction = this.<Object>generateFunction1CallFromString(VIEW, new Class[]{long.class});
        Function1<Object, play.mvc.Call> updateFunction = this.<Object>generateFunction1CallFromString(UPDATE, new Class[]{long.class});
        Function1<Object, play.mvc.Call> deleteFunction = this.<Object>generateFunction1CallFromString(DELETE, new Class[]{long.class});
        Html html = listView.render(header, modelClass.getSimpleName(), pages, sortBy, order, filterString, listFunction, viewFunction, updateFunction, deleteFunction);

        return renderInsideLayout(Http.Status.ACCEPTED, StringUtils.capitalize(LIST), data, html);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public Result view(long key) {
        DynamicForm data = DynamicForm.form().bindFromRequest();

        List<TwirlFormField> twirlFormFieldList = new ArrayList<>();
        Field[] fields = modelClass.getDeclaredFields();

        D dao = DaoFactory.getInstance().getDao(daoClass);
        M model = modelClass.cast(dao.findById(key));

        for (Field field : fields) {
            FormField formField = field.getAnnotation(FormField.class);
            if ((formField != null) && (formField.view())) {
                TwirlFormField twirlFormField = convertJavaFieldtoStaticTwirlField(field, formField);
                if (twirlFormField != null) {
                    twirlFormFieldList.add(twirlFormField);
                }
            }
        }

        Form<M> form = Form.form(modelClass);
        form = form.fill(model);
        Html html = viewView.render(form, twirlFormFieldList);

        return renderInsideLayout(Http.Status.ACCEPTED, StringUtils.capitalize(VIEW), data, html);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public Result update(long key) {
        DynamicForm data = DynamicForm.form().bindFromRequest();

        List<TwirlFormField> twirlFormFieldList = new ArrayList<>();
        Field[] fields = modelClass.getDeclaredFields();

        D dao = DaoFactory.getInstance().getDao(daoClass);
        M model = modelClass.cast(dao.findById(key));

        for (Field field : fields) {
            FormField formField = field.getAnnotation(FormField.class);
            if ((formField != null) && (formField.update())) {
                TwirlFormField twirlFormField = convertJavaFieldtoTwirlField(field, formField);
                if (twirlFormField != null) {
                    twirlFormFieldList.add(twirlFormField);
                }
            }
        }

        Call call = generateCallFromString(DO_UPDATE, new Class[]{long.class}, new Object[]{key});
        Form<M> form = Form.form(modelClass);
        form = form.fill(model);

        Html html = updateView.render(form, call, twirlFormFieldList);

        return renderInsideLayout(Http.Status.ACCEPTED, StringUtils.capitalize(UPDATE), data, html);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public Result delete(long key) {

        D dao = DaoFactory.getInstance().getDao(daoClass);
        M model = modelClass.cast(dao.findById(key));
        dao.remove(model);

        return redirect(generateCallFromString(LIST, new Class[]{long.class, String.class, String.class, String.class}, new Object[]{0, "id", "asc", ""}));
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public Result doCreate() {
        DynamicForm data = DynamicForm.form().bindFromRequest();

        Field[] fields = modelClass.getDeclaredFields();

        try {
            M model = modelClass.newInstance();
            for (Field field : fields) {
                FormField formField = field.getAnnotation(FormField.class);
                if ((formField != null) && (formField.create())) {
                    String fieldName = field.getName();
                    String sentencedFieldName = StringUtils.capitalize(fieldName);
                    Method setMethod = modelClass.getMethod("set" + sentencedFieldName, field.getType());
                    setMethod.invoke(model, Utilities.castStringtoCertainClass(field.getType(), data.get(fieldName)));
                }
            }

            DaoFactory.getInstance().getDao(daoClass).persist(model, Utilities.getUserIdFromSession(session()), Utilities.getIpAddressFromRequest(request()));

            return redirect(generateCallFromString(LIST, new Class[]{long.class, String.class, String.class, String.class}, new Object[]{0, "id", "asc", ""}));
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return internalServerError();
        }
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public Result doUpdate(long key) {
        DynamicForm data = DynamicForm.form().bindFromRequest();

        Field[] fields = modelClass.getDeclaredFields();
        D dao = DaoFactory.getInstance().getDao(daoClass);
        M model = modelClass.cast(dao.findById(key));

        try {
            for (Field field : fields) {
                FormField formField = field.getAnnotation(FormField.class);
                if ((formField != null) && (formField.create())) {
                    String fieldName = field.getName();
                    String sentencedFieldName = StringUtils.capitalize(fieldName);
                    Method setMethod = modelClass.getMethod("set" + sentencedFieldName, field.getType());
                    setMethod.invoke(model, Utilities.castStringtoCertainClass(field.getType(), data.get(fieldName)));
                }
            }

            dao.edit(model, Utilities.getUserIdFromSession(session()), Utilities.getIpAddressFromRequest(request()));

            return redirect(generateCallFromString(LIST, new Class[]{long.class, String.class, String.class, String.class}, new Object[]{0, "id", "asc", ""}));
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return internalServerError();
        }
    }

    private Call generateCallFromString(String methodName, Class<?> [] parameterTypes, Object [] objects) {
        Call call = null;
        Object reverseClass = CrudController.this.getReverseClass();
        try {
            Method method = reverseClass.getClass().getMethod(methodName, parameterTypes);
            call = (Call) method.invoke(reverseClass, objects);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return call;
    }

    private <A> Function1<A, play.mvc.Call> generateFunction1CallFromString(String methodName, Class<?> [] parameterTypes) {
        Function1<A, play.mvc.Call> function1 = new AbstractFunction1<A, play.mvc.Call>() {
            @Override
            public play.mvc.Call apply(A a) {
                play.mvc.Call call = null;
                Object reverseClass = CrudController.this.getReverseClass();
                try {
                    Method method = reverseClass.getClass().getMethod(methodName, parameterTypes);
                    call = (play.mvc.Call) method.invoke(reverseClass, a);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return call;

            }
        };
        return function1;
    }

    private <A, B, C, D> Function4<A, B, C, D, play.mvc.Call> generateFunction4CallFromString(String methodName, Class<?> [] parameterTypes) {
        Function4<A, B, C, D, play.mvc.Call> function4 = new AbstractFunction4<A, B, C, D, play.mvc.Call>() {
            @Override
            public Call apply(A v1, B v2, C v3, D v4) {
                Call call = null;
                Object reverseClass = CrudController.this.getReverseClass();
                try {
                    Method method = reverseClass.getClass().getMethod(methodName, parameterTypes);
                    call = (Call) method.invoke(reverseClass, v1, v2, v3, v4);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return call;

            }
        };
        return function4;
    }

    @SuppressWarnings("unchecked")
    private TwirlFormField convertJavaFieldtoStaticTwirlField(Field field, FormField formField) {
        TwirlFormField twirlFormField = null;
        if (formField != null) {
            String type = "static";

            twirlFormField = new TwirlFormField(type, field.getName(), formField.formName(), formField.defaultValue());
        }

        return twirlFormField;
    }

    @SuppressWarnings("unchecked")
    private TwirlFormField convertJavaFieldtoTwirlField(Field field, FormField formField) {
        TwirlFormField twirlFormField = null;
        if (formField != null) {
            if ((ReferenceType.ENUM.equals(formField.reference())) && (formField.accessorEntity() != null)) {
                List<String> references = new ArrayList<>();
                Object[] enumValues = formField.accessorEntity().getEnumConstants();
                for (Object enumValue : enumValues) {
                    references.add(enumValue.toString());
                }

                twirlFormField = new TwirlFormField(field.getType().getCanonicalName(), field.getName(), formField.formName(), formField.defaultValue(), references);
            } else if ((ReferenceType.TABLE.equals(formField.reference())) && (formField.accessorEntity() != null)) {
                List<String> references = new ArrayList<>();
                List<Model> modelReferences = DaoFactory.getInstance().getDao(formField.accessorEntity()).findAll();
                for (Model m : modelReferences) {
                    if (m.getClass().isAssignableFrom(ReferenceModel.class)) {
                        String referenceName = ((ReferenceModel) m).getName();
                        references.add(referenceName);
                    } else {
                        throw new ClassCastException();
                    }
                }
                twirlFormField = new TwirlFormField(field.getType().getCanonicalName(), field.getName(), formField.formName(), formField.defaultValue(), references);
            } else {
                String type = field.getType().getCanonicalName();
                switch (formField.forceType()) {
                    case STATIC:
                        type = STATIC;
                        break;
                    case TEXTAREA:
                        type = TEXTAREA;
                        break;
                    case PASSWORD:
                        type = PASSWORD;
                        break;
                    case HTML:
                        type = HTML;
                        break;
                    case CODE:
                        type = CODE;
                        break;
                    default:
                        break;
                }

                twirlFormField = new TwirlFormField(type, field.getName(), formField.formName(), formField.defaultValue());
            }
        }

        return twirlFormField;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
