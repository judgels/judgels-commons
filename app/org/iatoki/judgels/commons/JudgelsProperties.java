package org.iatoki.judgels.commons;

public final class JudgelsProperties {
    private static JudgelsProperties INSTANCE;

    private String appName;
    private String appVersion;

    private JudgelsProperties(String appName, String appVersion) {
        this.appName = appName;
        this.appVersion = appVersion;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public synchronized static void buildInstance(String appName, String appVersion) {
        if (INSTANCE != null) {
            throw new IllegalStateException();
        }
        INSTANCE = new JudgelsProperties(appName, appVersion);
    }

    public static JudgelsProperties getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException();
        }
        return INSTANCE;
    }
}
