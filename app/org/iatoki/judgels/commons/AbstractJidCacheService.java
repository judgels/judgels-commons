package org.iatoki.judgels.commons;

import org.iatoki.judgels.commons.models.daos.interfaces.BaseJidCacheDao;
import org.iatoki.judgels.commons.models.domains.AbstractJidCacheModel;

public abstract class AbstractJidCacheService<M extends AbstractJidCacheModel> {
    private BaseJidCacheDao<M> jidCacheDao;

    public final void setDao(BaseJidCacheDao<M> jidCacheDao) {
        this.jidCacheDao = jidCacheDao;
    }

    public final void putDisplayName(String jid, String displayName, String user, String ipAddress) {
        M jidCacheModel = jidCacheDao.createJidCacheModel();

        jidCacheModel.jid = jid;
        jidCacheModel.displayName = displayName;

        jidCacheDao.persist(jidCacheModel, user, ipAddress);
    }

    public final String getDisplayName(String jid) {
        M jidCacheModel = jidCacheDao.findById(jid);

        if (jidCacheModel == null) {
            return jid;
        } else {
            return jidCacheModel.displayName;
        }
    }
}
