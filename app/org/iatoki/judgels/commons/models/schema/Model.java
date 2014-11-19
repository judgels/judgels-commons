package org.iatoki.judgels.commons.models.schema;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

@MappedSuperclass
public abstract class Model {

    @ManyToOne
    @JoinColumn(name = "user_create")
    private AbstractUser userCreate;

    @Column(name = "time_create")
    private Timestamp timeCreate;

    @Column(name = "ip_create")
    private String ipCreate;

    @ManyToOne
    @JoinColumn(name = "user_update")
    private AbstractUser userUpdate;

    @Column(name = "time_update")
    private Timestamp timeUpdate;

    @Column(name = "ip_update")
    private String ipUpdate;

    public AbstractUser getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(AbstractUser userCreate) {
        this.userCreate = userCreate;
    }

    public Timestamp getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(Timestamp timeCreate) {
        this.timeCreate = timeCreate;
    }

    public String getIpCreate() {
        return ipCreate;
    }

    public void setIpCreate(String ipCreate) {
        this.ipCreate = ipCreate;
    }

    public AbstractUser getUserUpdate() {
        return userUpdate;
    }

    public void setUserUpdate(AbstractUser userUpdate) {
        this.userUpdate = userUpdate;
    }

    public Timestamp getTimeUpdate() {
        return timeUpdate;
    }

    public void setTimeUpdate(Timestamp timeUpdate) {
        this.timeUpdate = timeUpdate;
    }

    public String getIpUpdate() {
        return ipUpdate;
    }

    public void setIpUpdate(String ipUpdate) {
        this.ipUpdate = ipUpdate;
    }
}
