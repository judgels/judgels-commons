package org.iatoki.judgels.api.impls;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.iatoki.judgels.api.JudgelsAPIClientException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public abstract class AbstractJudgelsAPIImpl {

    private static final String API_URL_PREFIX = "/api/v1";

    private final String baseUrl;

    protected AbstractJudgelsAPIImpl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    protected abstract void setAuthorization(HttpRequestBase httpRequest);

    protected final String sendGetRequest(String path) {
        return sendGetRequest(path, ImmutableMap.of());
    }

    protected final String sendGetRequest(String path, Map<String, String> params) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(getEndpoint(path, params));
        return sendRequest(httpClient, httpGet);
    }

    protected final String sendPostRequest(String path) {
        return sendPostRequest(path, null);
    }

    protected final String sendPostRequest(String path, JsonElement body) {
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

    protected final String getEndpoint(String path) {
        return getEndpoint(path, ImmutableMap.of());
    }

    protected final String getEndpoint(String path, Map<String, String> params) {
        ImmutableList.Builder<NameValuePair> nameValuePairsBuilder = ImmutableList.builder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            nameValuePairsBuilder.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        List<NameValuePair> nameValuePairs = nameValuePairsBuilder.build();

        try {
            return new URIBuilder(baseUrl).setPath(API_URL_PREFIX + path).setParameters(nameValuePairs).build().toString();
        } catch (URISyntaxException e) {
            throw new JudgelsAPIClientException(null, e);
        }
    }

    private String sendRequest(CloseableHttpClient httpClient, HttpRequestBase httpRequest) {
        setAuthorization(httpRequest);

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
}
