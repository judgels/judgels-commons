package org.iatoki.judgels.commons.models.metas;

import org.iatoki.judgels.commons.models.domains.AbstractReferenceModel;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(AbstractReferenceModel.class)
public abstract class MetaAbstractReferenceModel extends MetaAbstractModel {

    public static volatile SingularAttribute<AbstractReferenceModel, String> name;
    public static volatile SingularAttribute<AbstractReferenceModel, String> description;

}

