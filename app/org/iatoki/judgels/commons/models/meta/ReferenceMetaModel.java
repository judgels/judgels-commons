package org.iatoki.judgels.commons.models.meta;

import org.iatoki.judgels.commons.models.schema.ReferenceModel;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ReferenceModel.class)
public abstract class ReferenceMetaModel extends MetaModel {

    public static volatile SingularAttribute<ReferenceModel, String> name;
    public static volatile SingularAttribute<ReferenceModel, String> description;

}

