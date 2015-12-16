package org.iatoki.judgels.play.migration;

public interface DataVersionDao {

    long getVersion();

    void update(long version);
}
