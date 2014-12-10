package org.iatoki.judgels.commons.controllers.crud;

import org.iatoki.judgels.commons.controllers.BaseController;
import play.mvc.Result;

public abstract class CrudController extends BaseController {

    public abstract Result create();
}
