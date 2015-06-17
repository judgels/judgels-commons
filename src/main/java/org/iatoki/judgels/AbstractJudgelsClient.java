package org.iatoki.judgels;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public abstract class AbstractJudgelsClient {

    private final String baseUrl;
    private final String clientJid;
    private final String clientSecret;

    public AbstractJudgelsClient(String baseUrl, String clientJid, String clientSecret) {
        this.baseUrl = baseUrl;
        this.clientJid = clientJid;
        this.clientSecret = clientSecret;
    }

    protected abstract String getClientName();

    protected URI getEndpoint(String path) {
        return getEndpoint(path, null);
    }

    protected URI getEndpoint(String path, List<NameValuePair> params) {
        try {
            URIBuilder uriBuilder = new URIBuilder(baseUrl);
            uriBuilder.setPath(path);
            if (params != null) {
                uriBuilder.setParameters(params);
            }

            return uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(getClientName() + " Url: " + baseUrl + path + "[" + params + "] is malformed.");
        }
    }

    protected CloseableHttpClient getHttpClient() {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(clientJid, clientSecret);
        provider.setCredentials(AuthScope.ANY, credentials);

        return HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
    }

    protected String executeHttpRequest(CloseableHttpClient httpClient, HttpRequestBase request) throws IOException {
        CloseableHttpResponse response = httpClient.execute(request);
        try {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return IOUtils.toString(response.getEntity().getContent());
            }

            return null;
        } finally {
            response.close();
            httpClient.close();
        }
    }
}
