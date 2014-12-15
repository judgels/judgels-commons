package org.iatoki.judgels.commons.models.metas;

import org.iatoki.judgels.commons.models.domains.AbstractModel;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(AbstractModel.class)
public abstract class MetaAbstractModel {

    public static volatile SingularAttribute<AbstractModel, String> userCreate;
    public static volatile SingularAttribute<AbstractModel, Long> timeCreate;
    public static volatile SingularAttribute<AbstractModel, String> userUpdate;
    public static volatile SingularAttribute<AbstractModel, Long> timeUpdate;
    public static volatile SingularAttribute<AbstractModel, String> ipUpdate;
    public static volatile SingularAttribute<AbstractModel, String> ipCreate;

}

