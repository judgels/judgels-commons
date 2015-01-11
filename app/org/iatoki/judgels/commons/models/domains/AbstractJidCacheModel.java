package org.iatoki.judgels.commons.models.domains;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractJidCacheModel extends AbstractModel {
    @Id
    public String jid;

    public String displayName;
}
