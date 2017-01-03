package org.iatoki.judgels.api.impls;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.iatoki.judgels.api.JudgelsAPIClientException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Map;

public abstract class AbstractJudgelsAPIImpl {

    private static final String API_URL_PREFIX = "/api/v1";

    private final String baseUrl;

    protected AbstractJudgelsAPIImpl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    protected abstract void setAuthorization(HttpRequestBase httpRequest);

    protected final JudgelsAPIRawResponseBody sendGetRequest(String path) {
        return sendGetRequest(path, null);
    }

    protected final JudgelsAPIRawResponseBody sendGetRequest(String path, Map<String, String> params) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(constructDefaultRequestConfig()).build();
        HttpGet httpGet = new HttpGet(getEndpoint(path, params));
        return sendRequest(httpClient, httpGet);
    }

    protected final JudgelsAPIRawResponseBody sendPostRequest(String path) {
        return sendPostRequest(path, null);
    }

    protected final JudgelsAPIRawResponseBody sendPostRequest(String path, JsonElement body) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(constructDefaultRequestConfig()).build();
        HttpPost httpPost = new HttpPost(getEndpoint(path));

        if (body != null) {
            try {
                httpPost.setEntity(new StringEntity(body.toString()));
            } catch (UnsupportedEncodingException e) {
                throw new JudgelsAPIClientException(httpPost, e);
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

    protected final String getEndpoint(String path) {
        return getEndpoint(path, ImmutableMap.of());
    }

    protected final String getEndpoint(String path, Map<String, String> params) {
        try {
            URIBuilder uriBuilder = new URIBuilder(baseUrl).setPath(API_URL_PREFIX + path);

            if (params != null) {
                for (Map.Entry<String, String> param : params.entrySet()) {
                    uriBuilder.addParameter(param.getKey(), param.getValue());
                }
            }

            return uriBuilder.build().toString();
        } catch (URISyntaxException e) {
            throw new JudgelsAPIClientException(null, e);
        }
    }

    private RequestConfig constructDefaultRequestConfig() {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(30 * 1000)
                .setSocketTimeout(30 * 1000)
                .setConnectTimeout(30 * 1000)
                .build();
    }

    private JudgelsAPIRawResponseBody sendRequest(CloseableHttpClient httpClient, HttpRequestBase httpRequest) {
        setAuthorization(httpRequest);

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpRequest);

            int statusCode = response.getStatusLine().getStatusCode();
            InputStream responseBody = response.getEntity().getContent();

            if (statusCode == HttpStatus.SC_OK) {
                return new JudgelsAPIRawResponseBody(httpClient, httpRequest, responseBody);
            } else {
                throw new JudgelsAPIClientException(httpRequest, statusCode, IOUtils.toString(responseBody));
            }

        } catch (IOException e) {
            throw new JudgelsAPIClientException(httpRequest, e);
        }
    }
}
