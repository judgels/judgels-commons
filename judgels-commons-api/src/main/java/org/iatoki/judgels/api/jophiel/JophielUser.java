package org.iatoki.judgels.api.jophiel;

public final class JophielUser {

    private String jid;
    private String username;
    private String name;
    private String profilePictureUrl;

    public JophielUser(String jid, String username, String profilePictureUrl) {
        this.jid = jid;
        this.username = username;
        this.profilePictureUrl = profilePictureUrl;
    }

    public JophielUser(String jid, String username, String name, String profilePictureUrl) {
        this.jid = jid;
        this.username = username;
        this.name = name;
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getJid() {
        return jid;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
}
