package org.iatoki.judgels.commons.models.metas;

import org.iatoki.judgels.commons.models.domains.Model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Model.class)
public abstract class MetaModel {

    public static volatile SingularAttribute<Model, String> userCreate;
    public static volatile SingularAttribute<Model, Long> timeCreate;
    public static volatile SingularAttribute<Model, String> userUpdate;
    public static volatile SingularAttribute<Model, Long> timeUpdate;
    public static volatile SingularAttribute<Model, String> ipUpdate;
    public static volatile SingularAttribute<Model, String> ipCreate;

}

