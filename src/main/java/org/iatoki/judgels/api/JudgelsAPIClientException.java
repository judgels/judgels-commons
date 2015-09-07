package org.iatoki.judgels.api;

import java.net.URI;

public final class JudgelsAPIClientException extends RuntimeException {

    private final URI endpoint;
    private final int statusCode;
    private final String errorMessage;

    public JudgelsAPIClientException(URI endpoint, int statusCode, String errorMessage) {
        this.endpoint = endpoint;
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    public JudgelsAPIClientException(URI endpoint, Throwable cause) {
        this.endpoint = endpoint;
        this.statusCode = -1;
        this.errorMessage = cause.getMessage();
    }

    public URI getEndpoint() {
        return endpoint;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String getMessage() {
        return "API call to " + endpoint + " failed with status code " + statusCode + " and error message \"" + errorMessage + "\".";
    }
}
