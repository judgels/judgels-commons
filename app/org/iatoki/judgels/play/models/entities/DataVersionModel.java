package org.iatoki.judgels.play.models.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "judgels_data_version")
public final class DataVersionModel {

    @Id
    @GeneratedValue
    public long id;

    public long version;
}
