package org.iatoki.judgels.play.models.entities;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractJidCacheModel extends AbstractJudgelsModel {

    public String displayName;
}
