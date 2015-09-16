package org.iatoki.judgels.play.models.daos.impls;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import org.iatoki.judgels.play.models.daos.JudgelsDao;
import org.iatoki.judgels.play.models.entities.AbstractJudgelsModel;
import org.iatoki.judgels.play.models.entities.AbstractJudgelsModel_;
import play.db.jpa.JPA;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;

public abstract class AbstractJudgelsJedisHibernateDao<M extends AbstractJudgelsModel> extends AbstractJudgelsHibernateDao<M> implements JudgelsDao<M> {

    private final JedisPool jedisPool;

    protected AbstractJudgelsJedisHibernateDao(JedisPool jedisPool, Class<M> modelClass) {
        super(modelClass);
        this.jedisPool = jedisPool;
    }

    @Override
    public void persist(M model, String user, String ipAddress) {
        super.persist(model, user, ipAddress);

        String jsonModel = new Gson().toJson(model);
        jedisPool.getResource().set(getModelClass().getCanonicalName() + model.id, jsonModel);
        jedisPool.getResource().set(model.jid, jsonModel);
    }

    @Override
    public void persist(M model, int childIndex, String user, String ipAddress) {
        super.persist(model, user, ipAddress);

        String jsonModel = new Gson().toJson(model);
        jedisPool.getResource().set(getModelClass().getCanonicalName() + model.id, jsonModel);
        jedisPool.getResource().set(model.jid, jsonModel);
    }

    @Override
    public M edit(M model, String user, String ipAddress) {
        M ret = super.edit(model, user, ipAddress);

        String jsonModel = new Gson().toJson(ret);
        jedisPool.getResource().set(getModelClass().getCanonicalName() + ret.id, jsonModel);
        jedisPool.getResource().set(model.jid, jsonModel);
        return ret;
    }

    @Override
    public final void remove(M model) {
        jedisPool.getResource().del(getModelClass().getCanonicalName() + model.id);
        jedisPool.getResource().del(model.jid);

        super.remove(model);
    }

    @Override
    public final boolean existsById(Long id) {
        if (jedisPool.getResource().exists(getModelClass().getCanonicalName() + id)) {
            return true;
        }

        return super.existsById(id);
    }

    @Override
    public final M findById(Long id) {
        if (jedisPool.getResource().exists(getModelClass().getCanonicalName() + id)) {
            return new Gson().fromJson(jedisPool.getResource().get(getModelClass().getCanonicalName() + id), getModelClass());
        }

        return super.findById(id);
    }

    @Override
    public final boolean existsByJid(String jid) {
        if (jedisPool.getResource().exists(jid)) {
            return true;
        }

        return super.existsByJid(jid);
    }

    @Override
    public final M findByJid(String jid) {
        if (jedisPool.getResource().exists(jid)) {
            return new Gson().fromJson(jedisPool.getResource().get(jid), getModelClass());
        }

        M model = super.findByJid(jid);

        jedisPool.getResource().set(jid, new Gson().toJson(model));

        return model;
    }

    @Override
    public final List<M> getAll() {
        Jedis jedis = jedisPool.getResource();
        List<M> results = super.getAll();

        for (M model : results) {
            String jsonModel = new Gson().toJson(model);
            jedis.set(getModelClass().getCanonicalName() + model.id, jsonModel);
            jedis.set(model.jid, jsonModel);
        }

        return results;
    }

    @Override
    public List<M> getByJids(Collection<String> jids) {
        Jedis jedis = jedisPool.getResource();
        List<String> queriedJids;
        ImmutableList.Builder<M> resultsBuilder = ImmutableList.builder();

        ImmutableList.Builder<String> queriedJidsBuilder = ImmutableList.builder();
        for (String jid : jids) {
            if (jedis.exists(jid)) {
                resultsBuilder.add(new Gson().fromJson(jedis.get(jid), getModelClass()));
            } else {
                queriedJidsBuilder.add(jid);
            }
        }
        queriedJids = queriedJidsBuilder.build();

        resultsBuilder.addAll(processGetByJids(queriedJids));

        return resultsBuilder.build();
    }

    private List<M> processGetByJids(Collection<String> jids) {
        if (jids.isEmpty()) {
            return ImmutableList.of();
        }

        Jedis jedis = jedisPool.getResource();
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<M> query = cb.createQuery(getModelClass());

        Root<M> root = query.from(getModelClass());

        query.where(root.get(AbstractJudgelsModel_.jid).in(jids));

        List<M> results = JPA.em().createQuery(query).getResultList();

        for (M model : results) {
            String jsonModel = new Gson().toJson(model);
            jedis.set(getModelClass().getCanonicalName() + model.id, jsonModel);
            jedis.set(model.jid, jsonModel);
        }

        return results;
    }
}
