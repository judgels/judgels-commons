package org.iatoki.judgels.commons.controllers.crud;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.iatoki.judgels.commons.helpers.WrappedContents;
import org.iatoki.judgels.commons.helpers.crud.CrudAction;
import org.iatoki.judgels.commons.helpers.crud.CrudActionMethods;
import org.iatoki.judgels.commons.helpers.crud.CrudUtils;
import org.iatoki.judgels.commons.helpers.crud.SectionLayout;
import org.iatoki.judgels.commons.helpers.crud.SimpleSectionLayout;
import org.iatoki.judgels.commons.helpers.crud.FormField;
import org.iatoki.judgels.commons.helpers.crud.FormFieldUtils;
import org.iatoki.judgels.commons.models.daos.DaoFactory;
import org.iatoki.judgels.commons.models.daos.interfaces.JudgelsDao;
import org.iatoki.judgels.commons.models.domains.Model;
import org.iatoki.judgels.commons.views.html.crud.createView;
import org.iatoki.judgels.commons.views.html.crud.headerWrapperView;
import play.api.mvc.Call;
import play.data.Form;
import play.mvc.Http;
import play.mvc.Result;
import play.twirl.api.Html;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

public abstract class BasicCrudController<M extends Model, D extends JudgelsDao<M>> extends CrudController {

    private final Class<M> modelClass;
    private final D dao;

    private SectionLayout createLayout;

    @SuppressWarnings("unchecked")
    protected BasicCrudController() {
        Class<?>[] genericClasses = getGenericClasses();

        this.modelClass = (Class<M>) genericClasses[0];
        this.dao = DaoFactory.getInstance().getDao((Class<D>) genericClasses[1]);

        this.createLayout = new SimpleSectionLayout();
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
        Call call = CrudActionMethods.CREATE.generateCall(getReverseController());
        Html html = createView.render(form, call, fields.build(), getModelSlug());

        return getResult(wrapCreateContent(html), Http.Status.ACCEPTED);
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

    protected final WrappedContents wrapCreateContent(Html content) {
        WrappedContents result = new WrappedContents(content);
        result = createLayout.wrapWithLayout(result);
        result = wrapWithCrudHeader(result);
        result = wrapWithTemplate(result);

        return result;
    }

    private WrappedContents wrapWithCrudHeader(WrappedContents content) {
        return content.wrapWithTransformation(c -> headerWrapperView.render(getModelSlug(), c));
    }

    private Class<?>[] getGenericClasses() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] typeArgs = genericSuperclass.getActualTypeArguments();
        return Arrays.copyOf(typeArgs, typeArgs.length, Class[].class);
    }
}
