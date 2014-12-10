package org.iatoki.judgels.commons.controllers;

import org.iatoki.judgels.commons.models.daos.interfaces.JudgelsDao;
import org.iatoki.judgels.commons.models.domains.Model;
import play.mvc.Controller;

public abstract class CrudControllerJagoParah<M extends Model, D extends JudgelsDao<M>> extends Controller {
//
//    private final Class<M> modelClass;
//    private final Class<D> daoClass;
//
//    private int pageSize;
//
//    @SuppressWarnings("unchecked")
//    protected CrudControllerJagoParah() {
//
//        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
//        Type[] typeArguments = genericSuperclass.getActualTypeArguments();
//
//        this.modelClass = (Class<M>) typeArguments[0];
//        this.daoClass = (Class<D>) typeArguments[1];
//
//        this.pageSize = 20;
//    }
//
//    protected final Class<M> getModelClass() {
//        return modelClass;
//    }
//
//    protected final Class<D> getDaoClass() {
//        return daoClass;
//    }
//
//    protected abstract Object getReverseController();
//
//    protected abstract String getSlug();
//
//    protected abstract Result renderInsideSection(int statusCode, String title, DynamicForm data, Html html);
//
//    public Result create() {
//        DynamicForm data = DynamicForm.form().bindFromRequest();
//
//        ImmutableList.Builder<FormField> fields = ImmutableList.builder();
//        for (Field field : modelClass.getDeclaredFields()) {
//            if (CrudUtils.isVisibleInAction(field, CrudAction.CREATE)) {
//                fields.add(FormFieldUtils.createFromJavaField(field));
//            }
//        }
//
//        Form<M> form = Form.form(modelClass);
//        Call call = CrudActionMethods.DO_CREATE.generateCall(getReverseController());
//        Html html = createView.render(form, call, fields.build());
//
//        return renderInsideSection(Http.Status.ACCEPTED, "Apasich", data, html);
//    }
//
//    @SuppressWarnings("unchecked")
//    @Transactional
//    public Result list(long page, String sortBy, String order, String filterString) {
//        DynamicForm data = DynamicForm.form().bindFromRequest();
//
//        List<String> header = new ArrayList<>();
//        List<Field> filters = new ArrayList<>();
//
//        for (Field field : modelClass.getDeclaredFields()) {
//            if (CrudUtils.isVisibleInAction(field, CrudAction.LIST)) {
//                header.add(FormFieldUtils.getLabel(field));
//                filters.add(field);
//            }
//        }
//
//        Page<List<String>> pages = DaoFactory.getInstance().getDao(daoClass).pageString(page, pageSize, sortBy, order, filterString, filters);
//        Function4<Long, String, String, String, Call> listFunction = CrudActionMethods.LIST.generateCallFunction(getReverseController());
//        Function1<Long, Call> viewFunction = CrudActionMethods.VIEW.generateCallFunction(getReverseController());
//        Function1<Long, Call> updateFunction = CrudActionMethods.UPDATE.generateCallFunction(getReverseController());
//        Function1<Long, Call> deleteFunction = CrudActionMethods.DELETE.generateCallFunction(getReverseController());
//
//        Html html = listView.render(header, modelClass.getSimpleName(), pages, sortBy, order, filterString, listFunction, viewFunction, updateFunction, deleteFunction);
//
//        return renderInsideSection(Http.Status.ACCEPTED, StringUtils.capitalize(CrudActionMethods.LIST.getName()), data, html);
//    }
//
//    @SuppressWarnings("unchecked")
//    @Transactional
//    public Result view(long key) {
//        DynamicForm data = DynamicForm.form().bindFromRequest();
//
//        ImmutableList.Builder<FormField> fields = ImmutableList.builder();
//        for (Field field : modelClass.getDeclaredFields()) {
//            if (CrudUtils.isVisibleInAction(field, CrudAction.VIEW)) {
//                fields.add(FormFieldUtils.createFromJavaField(field).toStatic());
//            }
//        }
//
//
//        D dao = DaoFactory.getInstance().getDao(daoClass);
//        M model = modelClass.cast(dao.findById(key));
//
//        Form<M> form = Form.form(modelClass);
//        form = form.fill(model);
//
//        Call call = CrudActionMethods.DO_CREATE.generateCall(getReverseController());
//        Html html = viewView.render(form, fields.build());
//
//        return renderInsideSection(Http.Status.ACCEPTED, StringUtils.capitalize(CrudActionMethods.VIEW.getName()), data, html);
//    }
//
//    @SuppressWarnings("unchecked")
//    @Transactional
//    public Result update(long key) {
//        DynamicForm data = DynamicForm.form().bindFromRequest();
//
//        D dao = DaoFactory.getInstance().getDao(daoClass);
//        M model = modelClass.cast(dao.findById(key));
//
//        ImmutableList.Builder<FormField> fields = ImmutableList.builder();
//        for (Field field : modelClass.getDeclaredFields()) {
//            if (CrudUtils.isVisibleInAction(field, CrudAction.UPDATE)) {
//                fields.add(FormFieldUtils.createFromJavaField(field));
//            }
//        }
//
//        Form<M> form = Form.form(modelClass);
//        form = form.fill(model);
//        Call call = CrudActionMethods.DO_UPDATE.generateCall(getReverseController(), key);
//        Html html = updateView.render(form, call, fields.build());
//
//        return renderInsideSection(Http.Status.ACCEPTED, StringUtils.capitalize(CrudActionMethods.UPDATE.getName()), data, html);
//    }
//
//    @SuppressWarnings("unchecked")
//    @Transactional
//    public Result delete(long key) {
//
//        D dao = DaoFactory.getInstance().getDao(daoClass);
//        M model = modelClass.cast(dao.findById(key));
//        dao.remove(model);
//
//        return redirect(CrudActionMethods.LIST.generateCall(getReverseController(), 0l, "id", "asc", ""));
//    }
//
//    @SuppressWarnings("unchecked")
//    @Transactional
//    public Result doCreate() {
//        DynamicForm data = DynamicForm.form().bindFromRequest();
//
//        M newModel;
//        try {
//            newModel = modelClass.newInstance();
//        } catch (InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            return internalServerError();
//        }
//
//        for (Field field : modelClass.getDeclaredFields()) {
//            if (CrudUtils.isVisibleInAction(field, CrudAction.CREATE)) {
//                setFieldValue(newModel, field, data.get(field.getName()));
//            }
//        }
//
//        DaoFactory.getInstance().getDao(daoClass).persist(newModel, Utilities.getUserIdFromSession(session()), Utilities.getIpAddressFromRequest(request()));
//
//        return redirect(CrudActionMethods.LIST.generateCall(getReverseController(), 0l, "id", "asc", ""));
//    }
//
//    @SuppressWarnings("unchecked")
//    @Transactional
//    public Result doUpdate(long key) {
//        DynamicForm data = DynamicForm.form().bindFromRequest();
//
//        D dao = DaoFactory.getInstance().getDao(daoClass);
//        M model = modelClass.cast(dao.findById(key));
//
//        for (Field field : modelClass.getDeclaredFields()) {
//            if (CrudUtils.isVisibleInAction(field, CrudAction.CREATE)) {
//                setFieldValue(model, field, data.get(field.getName()));
//            }
//        }
//
//        dao.edit(model, Utilities.getUserIdFromSession(session()), Utilities.getIpAddressFromRequest(request()));
//
//        return redirect(CrudActionMethods.LIST.generateCall(getReverseController(), 0l, "id", "asc", ""));
//    }
//
//    public final int getPageSize() {
//        return pageSize;
//    }
//
//    public final void setPageSize(int pageSize) {
//        this.pageSize = pageSize;
//    }
//
//    private void setFieldValue(Model model, Field field, String value) {
//        String capitalizedFieldName = StringUtils.capitalize(field.getName());
//        try {
//            Method setterMethod = modelClass.getMethod("set" + capitalizedFieldName, field.getType());
//            setterMethod.invoke(model, Utilities.castStringtoCertainClass(field.getType(), value));
//        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//            throw new RuntimeException(e);
//        }
//    }


}
