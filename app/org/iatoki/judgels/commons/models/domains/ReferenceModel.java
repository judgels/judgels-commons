package org.iatoki.judgels.commons.models.domains;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ReferenceModel extends Model {

    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
