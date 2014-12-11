package org.iatoki.judgels.commons.controllers.crud;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.iatoki.judgels.commons.helpers.Page;
import org.iatoki.judgels.commons.helpers.Utilities;
import org.iatoki.judgels.commons.helpers.WrappedContents;
import org.iatoki.judgels.commons.helpers.crud.CrudAction;
import org.iatoki.judgels.commons.helpers.crud.CrudActionMethods;
import org.iatoki.judgels.commons.helpers.crud.CrudUtils;
import org.iatoki.judgels.commons.helpers.crud.FormField;
import org.iatoki.judgels.commons.helpers.crud.FormFieldUtils;
import org.iatoki.judgels.commons.helpers.crud.SectionLayout;
import org.iatoki.judgels.commons.helpers.crud.SimpleSectionLayout;
import org.iatoki.judgels.commons.helpers.exceptions.InvalidPageNumberException;
import org.iatoki.judgels.commons.models.daos.DaoFactory;
import org.iatoki.judgels.commons.models.daos.interfaces.JudgelsDao;
import org.iatoki.judgels.commons.models.domains.Model;
import org.iatoki.judgels.commons.views.html.crud.createView;
import org.iatoki.judgels.commons.views.html.crud.headerWrapperView;
import org.iatoki.judgels.commons.views.html.crud.listView;
import org.iatoki.judgels.commons.views.html.crud.updateView;
import org.iatoki.judgels.commons.views.html.crud.viewView;
import play.api.mvc.Call;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Http;
import play.mvc.Result;
import play.twirl.api.Html;
import scala.Function1;
import scala.Function4;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public abstract class BasicCrudController<M extends Model, D extends JudgelsDao<M>> extends CrudController {

    private final Class<M> modelClass;
    private final D dao;

    private SectionLayout createLayout;
    private SectionLayout viewLayout;
    private SectionLayout updateLayout;
    private SectionLayout listLayout;

    private final int pageSize;

    @SuppressWarnings("unchecked")
    protected BasicCrudController() {
        Class<?>[] genericClasses = getGenericClasses();

        this.modelClass = (Class<M>) genericClasses[0];
        this.dao = DaoFactory.getInstance().getDao((Class<D>) genericClasses[1]);

        this.createLayout = new SimpleSectionLayout();
        this.viewLayout = new SimpleSectionLayout();
        this.updateLayout = new SimpleSectionLayout();
        this.listLayout = new SimpleSectionLayout();

        this.pageSize = 20;
    }

    @Override
    public Result create() {
        ImmutableList.Builder<FormField> fields = ImmutableList.builder();
        for (Field field : getModelClass().getDeclaredFields()) {
            if (CrudUtils.isVisibleInAction(field, CrudAction.CREATE)) {
                fields.add(FormFieldUtils.createFromJavaField(field));
            }
        }

        Form<M> form = Form.form(getModelClass());
        Call call = CrudActionMethods.DO_CREATE.generateCall(getReverseController());
        Html html = createView.render(form, call, fields.build(), getModelSlug());

        return getResult(wrapCreateContent(html), Http.Status.OK);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public Result doCreate() {
        DynamicForm data = DynamicForm.form().bindFromRequest();

        M newModel;
        try {
            newModel = modelClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return internalServerError();
        }

        for (Field field : modelClass.getDeclaredFields()) {
            if (CrudUtils.isVisibleInAction(field, CrudAction.CREATE)) {
                setFieldValue(newModel, field, data.get(field.getName()));
            }
        }

        dao.persist(newModel, Utilities.getUserIdFromSession(session()), Utilities.getIpAddressFromRequest(request()));

        return redirect(CrudActionMethods.LIST.generateCall(getReverseController(), 0L, "id", "asc", ""));
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public Result view(long key) {

        ImmutableList.Builder<FormField> fields = ImmutableList.builder();
        for (Field field : modelClass.getDeclaredFields()) {
            if (CrudUtils.isVisibleInAction(field, CrudAction.VIEW)) {
                fields.add(FormFieldUtils.createFromJavaField(field).toStatic());
            }
        }

        M model = modelClass.cast(dao.findById(key));

        Form<M> form = Form.form(modelClass);
        form = form.fill(model);

        Html html = viewView.render(form, fields.build(), getModelSlug());

        return getResult(wrapViewContent(html), Http.Status.OK);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public Result update(long key) {
        DynamicForm data = DynamicForm.form().bindFromRequest();

        M model = modelClass.cast(dao.findById(key));

        ImmutableList.Builder<FormField> fields = ImmutableList.builder();
        for (Field field : modelClass.getDeclaredFields()) {
            if (CrudUtils.isVisibleInAction(field, CrudAction.UPDATE)) {
                fields.add(FormFieldUtils.createFromJavaField(field));
            }
        }

        Form<M> form = Form.form(modelClass);
        form = form.fill(model);
        Call call = CrudActionMethods.DO_UPDATE.generateCall(getReverseController(), key);
        Html html = updateView.render(form, call, fields.build(), getModelSlug());

        return getResult(wrapUpdateContent(html), Http.Status.OK);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public Result doUpdate(long key) {
        DynamicForm data = DynamicForm.form().bindFromRequest();

        M model = modelClass.cast(dao.findById(key));

        for (Field field : modelClass.getDeclaredFields()) {
            if (CrudUtils.isVisibleInAction(field, CrudAction.UPDATE)) {
                setFieldValue(model, field, data.get(field.getName()));
            }
        }

        dao.edit(model, Utilities.getUserIdFromSession(session()), Utilities.getIpAddressFromRequest(request()));

        return redirect(CrudActionMethods.LIST.generateCall(getReverseController(), 0L, "id", "asc", ""));
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public Result delete(long key) {

        M model = modelClass.cast(dao.findById(key));
        dao.remove(model);

        return redirect(CrudActionMethods.LIST.generateCall(getReverseController(), 0L, "id", "asc", ""));
    }

    @Override
    @Transactional
    public Result list(long page, String sortBy, String order, String filterString) {
        ImmutableList.Builder<String> header = ImmutableList.builder();
        ImmutableList.Builder<Field> filters = ImmutableList.builder();

        for (Field field : modelClass.getDeclaredFields()) {
            if (CrudUtils.isVisibleInAction(field, CrudAction.LIST)) {
                header.add(field.getName());
                filters.add(field);
            }
        }

        try {
            Page<List<String>> pages = dao.pageString(page, pageSize, sortBy, order, filterString, filters.build());
            Function4<java.lang.Long, String, String, String, Call> listFunction = CrudActionMethods.LIST.generateCallFunction(getReverseController());
            Function1<java.lang.Long, Call> viewFunction = CrudActionMethods.VIEW.generateCallFunction(getReverseController());
            Function1<java.lang.Long, Call> updateFunction = CrudActionMethods.UPDATE.generateCallFunction(getReverseController());
            Function1<java.lang.Long, Call> deleteFunction = CrudActionMethods.DELETE.generateCallFunction(getReverseController());

            Html html = listView.render(header.build(), modelClass.getSimpleName(), pages, sortBy, order, filterString, listFunction, viewFunction, updateFunction, deleteFunction);

            return getResult(wrapListContent(html), play.mvc.Http.Status.OK);
        } catch (InvalidPageNumberException e) {
            Html html = Html.apply("");

            return getResult(wrapListContent(html), Http.Status.NOT_FOUND);
        }
    }

    public final int getPageSize() {
        return pageSize;
    }

    protected final String getModelSlug() {
        return StringUtils.lowerCase(getModelClass().getSimpleName());
    }

    protected final Class<M> getModelClass() {
        return modelClass;
    }

    protected final D getDao() {
        return dao;
    }

    protected final void setCreateLayout(SectionLayout layout) {
        this.createLayout = layout;
    }

    protected final void setViewLayout(SectionLayout layout) {
        this.viewLayout = layout;
    }

    protected final void setUpdateLayout(SectionLayout layout) {
        this.updateLayout = layout;
    }

    protected final void setListLayout(SectionLayout layout) {
        this.listLayout = layout;
    }

    protected final WrappedContents wrapCreateContent(Html content) {
        return wrapCrudContent(createLayout, content);
    }

    protected final WrappedContents wrapViewContent(Html content) {
        return wrapCrudContent(viewLayout, content);
    }

    protected final WrappedContents wrapUpdateContent(Html content) {
        return wrapCrudContent(updateLayout, content);
    }

    protected final WrappedContents wrapListContent(Html content) {
        return wrapCrudContent(listLayout, content);
    }

    private WrappedContents wrapCrudContent(SectionLayout layout, Html content) {
        WrappedContents result = new WrappedContents(content);
        result = layout.wrapWithLayout(result);
        result = wrapWithCrudHeader(result);
        result = wrapWithTemplate(result);

        return result;
    }

    protected final void setFieldValue(Model model, Field field, String value) {
        String capitalizedFieldName = StringUtils.capitalize(field.getName());
        try {
            Method setterMethod = modelClass.getMethod("set" + capitalizedFieldName, field.getType());
            setterMethod.invoke(model, Utilities.castStringtoCertainClass(field.getType(), value));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private WrappedContents wrapWithCrudHeader(WrappedContents content) {
        Function4<java.lang.Long, String, String, String, Call> listFunction = CrudActionMethods.LIST.generateCallFunction(getReverseController());
        Call createFunction = CrudActionMethods.CREATE.generateCall(getReverseController());

        return content.wrapWithTransformation(c -> headerWrapperView.render(getModelSlug(), listFunction, createFunction, c));
    }

    private Class<?>[] getGenericClasses() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] typeArgs = genericSuperclass.getActualTypeArguments();
        return Arrays.copyOf(typeArgs, typeArgs.length, Class[].class);
    }
}
