package org.iatoki.judgels.commons;

import com.typesafe.config.Config;

public final class JudgelsProperties {
    private static JudgelsProperties INSTANCE;

    private final String appName;
    private final String appVersion;

    private final Config config;

    private String appTitle;
    private String appCopyright;
    private String githubLink;

    private JudgelsProperties(String appName, String appVersion, Config config) {
        this.appName = appName;
        this.appVersion = appVersion;
        this.config = config;
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

    public synchronized static void buildInstance(String appName, String appVersion, Config config) {
        if (INSTANCE != null) {
            throw new UnsupportedOperationException("JudgelsProperties instance has already been built");
        }
        INSTANCE = new JudgelsProperties(appName, appVersion, config);
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
        return config.getString(key);
    }

    private String requireStringValue(String key) {
        if (!config.hasPath(key)) {
            return null;
        }
        return config.getString(key);
    }
}
