package org.iatoki.judgels.api.impls;

import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.iatoki.judgels.api.JudgelsAPIClientException;
import org.iatoki.judgels.api.JudgelsAPICredentials;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

public abstract class AbstractJudgelsAPIImpl {

    private static final String API_URL_PREFIX = "/api/v1";

    private final String baseUrl;
    private final JudgelsAPICredentials credentials;

    protected AbstractJudgelsAPIImpl(String baseUrl, JudgelsAPICredentials credentials) {
        this.baseUrl = baseUrl;
        this.credentials = credentials;
    }

    protected final String sendGetRequest(String path) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(getEndpoint(path));
        return sendRequest(httpClient, httpGet);
    }

    protected final String sendPostRequest(String path) {
        return sendPostRequest(path, null);
    }

    protected final String sendPostRequest(String path, JsonObject body) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(getEndpoint(path));

        if (body != null) {
            try {
                httpPost.setEntity(new StringEntity(body.toString()));
            } catch (UnsupportedEncodingException e) {
                throw new JudgelsAPIClientException(httpPost.getURI(), e);
            }
        }

        return sendRequest(httpClient, httpPost);
    }

    protected final String interpolatePath(String path, Object... args) {
        String interpolatedPath = path;
        for (Object arg : args) {
            interpolatedPath = interpolatedPath.replaceFirst("/:[a-zA-Z]+", "/" + arg.toString());
        }
        return interpolatedPath;
    }

    private String sendRequest(CloseableHttpClient httpClient, HttpRequestBase httpRequest) {
        credentials.applyToHttpRequest(httpRequest);

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpRequest);

            int statusCode = response.getStatusLine().getStatusCode();
            String content = IOUtils.toString(response.getEntity().getContent());
            if (statusCode == HttpStatus.SC_OK) {
                return content;
            } else {
                throw new JudgelsAPIClientException(httpRequest.getURI(), statusCode, content);
            }

        } catch (IOException e) {
            throw new JudgelsAPIClientException(httpRequest.getURI(), e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                // log later
            }
        }
    }

    private URI getEndpoint(String path) {
        try {
            return new URIBuilder(baseUrl).setPath(API_URL_PREFIX + path).build();
        } catch (URISyntaxException e) {
            throw new JudgelsAPIClientException(null, e);
        }
    }
}
