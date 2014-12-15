package org.iatoki.judgels.commons.controllers.crud;

import com.google.common.collect.Lists;
import org.iatoki.judgels.commons.helpers.ControllerMethod0;
import org.iatoki.judgels.commons.helpers.ControllerMethod1;
import org.iatoki.judgels.commons.helpers.ControllerMethod4;
import org.iatoki.judgels.commons.helpers.LazyHtml;
import org.iatoki.judgels.commons.helpers.Page;
import org.iatoki.judgels.commons.helpers.SectionLayout;
import org.iatoki.judgels.commons.helpers.SimpleSectionLayout;
import org.iatoki.judgels.commons.helpers.Utilities;
import org.iatoki.judgels.commons.helpers.crud.CrudAction;
import org.iatoki.judgels.commons.helpers.crud.CrudActions;
import org.iatoki.judgels.commons.helpers.crud.CrudField;
import org.iatoki.judgels.commons.helpers.crud.CrudFields;
import org.iatoki.judgels.commons.helpers.exceptions.InvalidPageNumberException;
import org.iatoki.judgels.commons.models.daos.DaoFactory;
import org.iatoki.judgels.commons.models.daos.interfaces.JudgelsDao;
import org.iatoki.judgels.commons.models.domains.Model;
import org.iatoki.judgels.commons.models.domains.Models;
import org.iatoki.judgels.commons.views.html.crud.createView;
import org.iatoki.judgels.commons.views.html.crud.headerWrapperView;
import org.iatoki.judgels.commons.views.html.crud.listView;
import org.iatoki.judgels.commons.views.html.crud.updateView;
import org.iatoki.judgels.commons.views.html.crud.viewView;
import play.api.mvc.Call;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Http;
import play.mvc.Result;
import play.twirl.api.Html;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BasicCrudController<M extends Model, D extends JudgelsDao<M>> extends CrudController {

    private final Class<M> modelClass;
    private final D dao;

    private final ControllerMethod0 createMethod;
    private final ControllerMethod0 doCreateMethod;
    private final ControllerMethod1<Long> updateMethod;
    private final ControllerMethod1<Long> doUpdateMethod;
    private final ControllerMethod1<Long> viewMethod;
    private final ControllerMethod1<Long> deleteMethod;
    private final ControllerMethod4<Long, String, String, String> listMethod;

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

        this.createMethod = new ControllerMethod0(getReverseController(), "create");
        this.doCreateMethod = new ControllerMethod0(getReverseController(), "doCreate");
        this.updateMethod = new ControllerMethod1<>(getReverseController(), "update", long.class);
        this.doUpdateMethod = new ControllerMethod1<>(getReverseController(), "doUpdate", long.class);
        this.viewMethod = new ControllerMethod1<>(getReverseController(), "view", long.class);
        this.deleteMethod = new ControllerMethod1<>(getReverseController(), "delete", long.class);
        this.listMethod = new ControllerMethod4<>(getReverseController(), "list", long.class, String.class, String.class, String.class);

        SectionLayout defaultLayout = new SimpleSectionLayout();

        this.createLayout = defaultLayout;
        this.viewLayout = defaultLayout;
        this.updateLayout = defaultLayout;
        this.listLayout = defaultLayout;

        this.pageSize = 20;
    }

    protected Html htmlCreate(Form<M> form) {
        List<CrudField> fields = Models.getFields(modelClass).stream()
            .filter(field -> CrudActions.isAppliedTo(CrudAction.CREATE, field))
            .map(CrudFields::fromJavaField)
            .collect(Collectors.toList());

        Call call = doCreateMethod.apply();
        Html content = createView.render(form, call, fields, Models.getModelSlug(modelClass));

        return content;
    }

    @Override
    public Result create() {
        Form<M> form = Form.form(modelClass);
        Html content = htmlCreate(form);

        return getResult(wrapCreateContent(content), Http.Status.OK);
    }

    protected boolean beforeDoCreate(Form<M> form) {
        return true;
    }

    @Transactional
    public Result doCreate() {
        Form<M> form = Form.form(modelClass).bindFromRequest();

        if (beforeDoCreate(form)) {
            M newModel = Models.newModel(modelClass);
            Models.getFields(modelClass).stream()
                .filter(field -> CrudActions.isAppliedTo(CrudAction.CREATE, field))
                .forEach(field -> newModel.setReflectively(field, form.data().get(field.getName())));

            dao.persist(newModel, Utilities.getUserIdFromSession(session()), Utilities.getIpAddressFromRequest(request()));

            return redirect(listMethod.apply(0L, "id", "asc", ""));
        } else {
            Html content = htmlCreate(form);

            return getResult(wrapCreateContent(content), Http.Status.OK);
        }

    }

    @Transactional
    public Result view(long modelId) {
        List<CrudField> fields = Models.getFields(modelClass).stream()
                .filter(field -> CrudActions.isAppliedTo(CrudAction.VIEW, field))
                .map(CrudFields::fromJavaField)
                .collect(Collectors.toList());

        M model = dao.findById(modelId);
        Form<M> form = Form.form(modelClass).fill(model);
        Html content = viewView.render(form, fields, Models.getModelSlug(modelClass));

        return getResult(wrapViewContent(content, modelId), Http.Status.OK);
    }

    protected Html htmlUpdate(Form<M> form, long modelId) {
        List<CrudField> fields = Models.getFields(modelClass).stream()
                .filter(field -> CrudActions.isAppliedTo(CrudAction.UPDATE, field))
                .map(CrudFields::fromJavaField)
                .collect(Collectors.toList());

        Call call = doUpdateMethod.apply(modelId);
        Html content = updateView.render(form, call, fields, Models.getModelSlug(modelClass));

        return content;
    }

    @Transactional
    public Result update(long modelId) {
        M model = dao.findById(modelId);
        Form<M> form = Form.form(modelClass).fill(model);

        Html content = htmlUpdate(form, modelId);

        return getResult(wrapUpdateContent(content, modelId), Http.Status.OK);
    }

    protected boolean beforeDoUpdate(Form<M> form) {
        return true;
    }

    @Transactional
    public Result doUpdate(long modelId) {
        Form<M> form = Form.form(modelClass).bindFromRequest();

        if (beforeDoUpdate(form)) {
            M model = dao.findById(modelId);
            Models.getFields(modelClass).stream()
                    .filter(field -> CrudActions.isAppliedTo(CrudAction.UPDATE, field))
                    .forEach(field -> model.setReflectively(field, form.data().get(field.getName())));

            dao.edit(model, Utilities.getUserIdFromSession(session()), Utilities.getIpAddressFromRequest(request()));

            return redirect(listMethod.apply(0L, "id", "asc", ""));
        } else {
            Html content = htmlUpdate(form, modelId);

            return getResult(wrapUpdateContent(content, modelId), Http.Status.OK);
        }
    }

    @Override
    @Transactional
    public Result delete(long modelId) {
        M model = dao.findById(modelId);
        dao.remove(model);

        return redirect(listMethod.apply(0L, "id", "asc", ""));
    }

    @Override
    @Transactional
    public Result list(long page, String sortBy, String order, String filterString) {
        List<Field> filters = Models.getFields(modelClass).stream()
                .filter(field -> CrudActions.isAppliedTo(CrudAction.LIST, field))
                .collect(Collectors.toList());

        List<String> header = Lists.transform(filters, Models::getFieldSlug);

        try {
            Page<List<String>> pages = dao.pageString(page, pageSize, sortBy, order, filterString, filters);
            Html html = listView.render(header, modelClass.getSimpleName(), pages, sortBy, order, filterString, listMethod, viewMethod, updateMethod, deleteMethod);

            return getResult(wrapListContent(html), play.mvc.Http.Status.OK);
        } catch (InvalidPageNumberException e) {
            Html html = Html.apply("");

            return getResult(wrapListContent(html), Http.Status.NOT_FOUND);
        }
    }

    public final int getPageSize() {
        return pageSize;
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

    protected final LazyHtml wrapCreateContent(Html content) {
        return wrapCrudContent(createLayout, content, 0);
    }

    protected final LazyHtml wrapViewContent(Html content, long modelId) {
        return wrapCrudContent(viewLayout, content, modelId);
    }

    protected final LazyHtml wrapUpdateContent(Html content, long modelId) {
        return wrapCrudContent(updateLayout, content, modelId);
    }

    protected final LazyHtml wrapListContent(Html content) {
        return wrapCrudContent(listLayout, content, 0);
    }

    private LazyHtml wrapCrudContent(SectionLayout layout, Html content, long modelId) {
        LazyHtml result = new LazyHtml(content);
        layout.wrapWithLayout(result, Models.getModelSlug(modelClass), modelId);
        wrapWithCrudHeader(result);
        wrapWithTemplate(result);

        return result;
    }

    private void wrapWithCrudHeader(LazyHtml content) {
        content.appendTransformation(c -> headerWrapperView.render(Models.getModelSlug(modelClass), listMethod.apply(0L, "id", "asc", ""), createMethod.apply(), c));
    }

    private Class<?>[] getGenericClasses() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] typeArgs = genericSuperclass.getActualTypeArguments();
        return Arrays.copyOf(typeArgs, typeArgs.length, Class[].class);
    }
}
