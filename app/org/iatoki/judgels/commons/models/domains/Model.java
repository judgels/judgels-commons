package org.iatoki.judgels.commons.models.domains;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Model {

    private String userCreate;

    private long timeCreate;

    private String ipCreate;

    private String userUpdate;

    private long timeUpdate;

    private String ipUpdate;

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public long getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(long timeCreate) {
        this.timeCreate = timeCreate;
    }

    public String getIpCreate() {
        return ipCreate;
    }

    public void setIpCreate(String ipCreate) {
        this.ipCreate = ipCreate;
    }

    public String getUserUpdate() {
        return userUpdate;
    }

    public void setUserUpdate(String userUpdate) {
        this.userUpdate = userUpdate;
    }

    public long getTimeUpdate() {
        return timeUpdate;
    }

    public void setTimeUpdate(long timeUpdate) {
        this.timeUpdate = timeUpdate;
    }

    public String getIpUpdate() {
        return ipUpdate;
    }

    public void setIpUpdate(String ipUpdate) {
        this.ipUpdate = ipUpdate;
    }
}
