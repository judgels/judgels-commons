package org.iatoki.judgels.commons.models.meta;

import org.iatoki.judgels.commons.models.schema.ReferenceModel;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(MetaAbstractUser.class)
public abstract class MetaAbstractUser extends MetaModel {

    public static volatile SingularAttribute<ReferenceModel, String> userId;

}

