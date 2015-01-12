package org.iatoki.judgels.commons;

import org.iatoki.judgels.commons.models.daos.interfaces.BaseJidCacheDao;
import org.iatoki.judgels.commons.models.domains.AbstractJidCacheModel;

public abstract class AbstractJidCacheService<M extends AbstractJidCacheModel> {
    private BaseJidCacheDao<M> jidCacheDao;

    public final void setDao(BaseJidCacheDao<M> jidCacheDao) {
        this.jidCacheDao = jidCacheDao;
    }

    public final void putDisplayName(String jid, String displayName, String user, String ipAddress) {
        if (jidCacheDao.existsByJid(jid)) {
            editDisplayName(jid, displayName, user, ipAddress);
        } else {
            createDisplayName(jid, displayName, user, ipAddress);
        }
    }

    public final String getDisplayName(String jid) {

        if (!jidCacheDao.existsByJid(jid)) {
            return jid;
        } else {
            M jidCacheModel = jidCacheDao.findByJid(jid);
            return jidCacheModel.displayName;
        }
    }

    private void createDisplayName(String jid, String displayName, String user, String ipAddress) {
        M jidCacheModel = jidCacheDao.createJidCacheModel();

        jidCacheModel.jid = jid;
        jidCacheModel.displayName = displayName;

        jidCacheDao.persist(jidCacheModel, user, ipAddress);
    }

    private void editDisplayName(String jid, String displayName, String user, String ipAddress) {
        M jidCacheModel = jidCacheDao.findByJid(jid);

        jidCacheModel.jid = jid;
        jidCacheModel.displayName = displayName;

        jidCacheDao.edit(jidCacheModel, user, ipAddress);
    }
}
