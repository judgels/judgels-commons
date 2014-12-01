package org.iatoki.judgels.commons.models.metas;

import org.iatoki.judgels.commons.models.domains.ReferenceModel;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ReferenceModel.class)
public abstract class MetaReferenceModel extends MetaModel {

    public static volatile SingularAttribute<ReferenceModel, String> name;
    public static volatile SingularAttribute<ReferenceModel, String> description;

}

