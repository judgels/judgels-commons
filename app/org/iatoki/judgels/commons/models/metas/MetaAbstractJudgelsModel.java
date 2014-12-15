package org.iatoki.judgels.commons.models.metas;

import org.iatoki.judgels.commons.models.domains.AbstractModel;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(AbstractModel.class)
public abstract class MetaAbstractJudgelsModel {

    public static volatile SingularAttribute<AbstractModel, Long> id;
    public static volatile SingularAttribute<AbstractModel, String> jid;

}

