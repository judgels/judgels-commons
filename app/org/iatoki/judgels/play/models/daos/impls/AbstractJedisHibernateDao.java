package org.iatoki.judgels.play.models.daos.impls;

import com.google.gson.Gson;
import org.iatoki.judgels.play.models.entities.AbstractModel;
import play.db.jpa.JPA;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.List;

public abstract class AbstractJedisHibernateDao<K, M extends AbstractModel> extends AbstractHibernateDao<K, M> {

    private final JedisPool jedisPool;

    protected AbstractJedisHibernateDao(JedisPool jedisPool, Class<M> modelClass) {
        super(modelClass);
        this.jedisPool = jedisPool;
    }

    @Override
    public void persist(M model, String user, String ipAddress) {
        super.persist(model, user, ipAddress);

        jedisPool.getResource().set(jedisKey(model), new Gson().toJson(model));
    }

    @Override
    public M edit(M model, String user, String ipAddress) {
        M ret = super.edit(model, user, ipAddress);

        jedisPool.getResource().set(jedisKey(model), new Gson().toJson(ret));
        return ret;
    }

    @Override
    public void remove(M model) {
        jedisPool.getResource().del(jedisKey(model));

        JPA.em().remove(model);
    }

    @Override
    public final boolean existsById(K id) {
        if (jedisPool.getResource().exists(getModelClass().getCanonicalName() + id)) {
            return true;
        }

        return super.existsById(id);
    }

    @Override
    public final M findById(K id) {
        Jedis jedis = jedisPool.getResource();
        if (jedis.exists(getModelClass().getCanonicalName() + id)) {
            return new Gson().fromJson(jedis.get(getModelClass().getCanonicalName() + id), getModelClass());
        }

        return super.findById(id);
    }

    @Override
    public final List<M> getAll() {
        List<M> models = super.getAll();

        for (M model : models) {
            jedisPool.getResource().set(jedisKey(model), new Gson().toJson(model));
        }
        return models;
    }

    private String jedisKey(M model) {
        try {
            for (Field field : getModelClass().getFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    return getModelClass().getCanonicalName() + field.get(model);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return "";
    }
}
