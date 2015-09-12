package org.iatoki.judgels.api.sandalphon;

public final class SandalphonProblemStatementRenderRequestParam {

    private String problemSecret;
    private long currentMillis;
    private String statementLanguage;
    private String switchStatementLanguageUrl;
    private String postSubmitUrl;
    private String reasonNotAllowedToSubmit;
    private String allowedGradingLanguages;

    public String getProblemSecret() {
        return problemSecret;
    }

    public SandalphonProblemStatementRenderRequestParam setProblemSecret(String problemSecret) {
        this.problemSecret = problemSecret;
        return this;
    }

    public long getCurrentMillis() {
        return currentMillis;
    }

    public SandalphonProblemStatementRenderRequestParam setCurrentMillis(long currentMillis) {
        this.currentMillis = currentMillis;
        return this;
    }

    public String getStatementLanguage() {
        return statementLanguage;
    }

    public SandalphonProblemStatementRenderRequestParam setStatementLanguage(String statementLanguage) {
        this.statementLanguage = statementLanguage;
        return this;
    }

    public String getSwitchStatementLanguageUrl() {
        return switchStatementLanguageUrl;
    }

    public SandalphonProblemStatementRenderRequestParam setSwitchStatementLanguageUrl(String switchStatementLanguageUrl) {
        this.switchStatementLanguageUrl = switchStatementLanguageUrl;
        return this;
    }

    public String getPostSubmitUrl() {
        return postSubmitUrl;
    }

    public SandalphonProblemStatementRenderRequestParam setPostSubmitUrl(String postSubmitUrl) {
        this.postSubmitUrl = postSubmitUrl;
        return this;
    }

    public String getReasonNotAllowedToSubmit() {
        return reasonNotAllowedToSubmit;
    }

    public SandalphonProblemStatementRenderRequestParam setReasonNotAllowedToSubmit(String reasonNotAllowedToSubmit) {
        this.reasonNotAllowedToSubmit = reasonNotAllowedToSubmit;
        return this;
    }

    public String getAllowedGradingLanguages() {
        return allowedGradingLanguages;
    }

    public SandalphonProblemStatementRenderRequestParam setAllowedGradingLanguages(String allowedGradingLanguages) {
        this.allowedGradingLanguages = allowedGradingLanguages;
        return this;
    }
}
