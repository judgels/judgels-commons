package org.iatoki.judgels.commons.models.daos.interfaces;

import org.iatoki.judgels.commons.models.domains.AbstractJidCacheModel;

public interface BaseJidCacheDao<M extends AbstractJidCacheModel> extends Dao<String, M> {

    M createJidCacheModel();
}
