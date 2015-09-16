package org.iatoki.judgels.play.models.daos.impls;

import org.iatoki.judgels.play.models.daos.BaseJidCacheDao;
import org.iatoki.judgels.play.models.entities.AbstractJidCacheModel;
import redis.clients.jedis.JedisPool;

public abstract class AbstractJidCacheJedisHibernateDao<M extends AbstractJidCacheModel> extends AbstractJudgelsJedisHibernateDao<M> implements BaseJidCacheDao<M> {

    protected AbstractJidCacheJedisHibernateDao(JedisPool jedisPool, Class<M> modelClass) {
        super(jedisPool, modelClass);
    }
}
