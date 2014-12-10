package org.iatoki.judgels.commons.controllers.crud;

import org.iatoki.judgels.commons.controllers.BaseController;
import play.mvc.Result;

public abstract class CrudController extends BaseController {

    public abstract Result create();

    public abstract Result view(long key);

    public abstract Result update(long key);

    public abstract Result delete(long key);

    public abstract Result list(long page, String sortBy, String order, String filterString);
}
