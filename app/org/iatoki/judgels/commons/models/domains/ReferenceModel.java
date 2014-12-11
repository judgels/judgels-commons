package org.iatoki.judgels.commons.models.domains;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ReferenceModel extends Model {

    public String name;
    public String description;
}
