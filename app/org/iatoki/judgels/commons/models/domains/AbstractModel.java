package org.iatoki.judgels.commons.models.domains;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractModel {

    public String userCreate;

    public long timeCreate;

    public String ipCreate;

    public String userUpdate;

    public long timeUpdate;

    public String ipUpdate;
}
