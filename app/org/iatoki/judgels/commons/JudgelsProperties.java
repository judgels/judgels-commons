package org.iatoki.judgels.commons;

import play.Configuration;

public final class JudgelsProperties {
    private static JudgelsProperties INSTANCE;

    private final String appName;
    private final String appVersion;

    private final Configuration conf;
    private final String confLocation;

    private String appTitle;
    private String appCopyright;
    private String githubLink;

    private JudgelsProperties(String appName, String appVersion, Configuration conf, String confLocation) {
        this.appName = appName;
        this.appVersion = appVersion;
        this.conf = conf;
        this.confLocation = confLocation;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getAppTitle() {
        return appTitle;
    }

    public String getAppCopyright() {
        return appCopyright;
    }

    public String getGithubLink() {
        return githubLink;
    }

    public synchronized static void buildInstance(String appName, String appVersion, Configuration conf, String confLocation) {
        if (INSTANCE != null) {
            throw new UnsupportedOperationException("JudgelsProperties instance has already been built");
        }
        INSTANCE = new JudgelsProperties(appName, appVersion, conf, confLocation);
        INSTANCE.build();
    }

    public static JudgelsProperties getInstance() {
        if (INSTANCE == null) {
            throw new UnsupportedOperationException("JudgelsProperties instance has not been built");
        }
        return INSTANCE;
    }

    private void build() {
        this.appTitle = requireStringValue("application.title");
        this.appCopyright = requireStringValue("application.copyright");
        this.githubLink = requireStringValue("link.github");
    }

    private String getStringValue(String key) {
        return conf.getString(key);
    }

    private String requireStringValue(String key) {
        String value = getStringValue(key);
        if (value == null) {
            throw new RuntimeException("Missing " + key + " property in " + confLocation);
        }
        return value;
    }
}
