package org.iatoki.judgels.play.models.domains;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractReferenceModel extends AbstractModel {

    public String name;
    public String description;
}
