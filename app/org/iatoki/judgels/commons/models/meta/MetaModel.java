package org.iatoki.judgels.commons.models.meta;

import org.iatoki.judgels.commons.models.schema.Model;
import org.iatoki.judgels.commons.models.schema.AbstractUser;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.sql.Timestamp;

@StaticMetamodel(Model.class)
public abstract class MetaModel {

    public static volatile SingularAttribute<Model, AbstractUser> userCreate;
    public static volatile SingularAttribute<Model, Timestamp> timeCreate;
    public static volatile SingularAttribute<Model, AbstractUser> userUpdate;
    public static volatile SingularAttribute<Model, Timestamp> timeUpdate;
    public static volatile SingularAttribute<Model, String> ipUpdate;
    public static volatile SingularAttribute<Model, String> ipCreate;

}

