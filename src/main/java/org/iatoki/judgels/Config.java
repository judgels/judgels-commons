package org.iatoki.judgels;

public final class Config {
    private final com.typesafe.config.Config config;

    public Config(com.typesafe.config.Config config) {
        this.config = config;
    }

    public String requireString(String key) {
        return config.getString(key);
    }

    public String getString(String key) {
        if (!config.hasPath(key)) {
            return null;
        }
        return config.getString(key);
    }

    public int requireInt(String key) {
        return config.getInt(key);
    }

    public Integer getInt(String key) {
        if (!config.hasPath(key)) {
            return null;
        }
        return config.getInt(key);
    }

    public boolean requireBoolean(String key) {
        return config.getBoolean(key);
    }

    public Boolean getBoolean(String key) {
        if (!config.hasPath(key)) {
            return null;
        }
        return config.getBoolean(key);
    }
}
