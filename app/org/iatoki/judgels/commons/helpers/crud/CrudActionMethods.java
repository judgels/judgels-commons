package org.iatoki.judgels.commons.helpers.crud;

import org.iatoki.judgels.commons.helpers.ActionMethod0;
import org.iatoki.judgels.commons.helpers.ActionMethod1;
import org.iatoki.judgels.commons.helpers.ActionMethod4;

public final class CrudActionMethods {

    public static final ActionMethod0 CREATE = new ActionMethod0("create");
    public static final ActionMethod0 DO_CREATE = new ActionMethod0("doCreate");

    public static final ActionMethod1<Long> UPDATE = new ActionMethod1<>("update", long.class);
    public static final ActionMethod1<Long> DO_UPDATE = new ActionMethod1<>("doUpdate", long.class);

    public static final ActionMethod1<Long> VIEW = new ActionMethod1<>("view", long.class);

    public static final ActionMethod1<Long> DELETE = new ActionMethod1<>("delete", long.class);

    public static final ActionMethod4<Long, String, String, String> LIST = new ActionMethod4<>("list", long.class, String.class, String.class, String.class);

    private CrudActionMethods() {
        // prevent instantiation
    }
}
