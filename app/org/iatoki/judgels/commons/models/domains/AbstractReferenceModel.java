package org.iatoki.judgels.commons.models.domains;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractReferenceModel extends AbstractModel {

    public String name;
    public String description;
}
