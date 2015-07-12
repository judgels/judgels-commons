package org.iatoki.judgels.play.models.domains;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractJudgelsModel extends AbstractModel {

    @Id
    @GeneratedValue
    public long id;

    @Column(unique = true)
    public String jid;
}
