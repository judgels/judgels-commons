package org.iatoki.judgels.play.models.daos;

public interface DataVersionDao {

    long getVersion();

    void update(long version);
}
