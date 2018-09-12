package com.everhomes.core.sdk;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

public class SdkRestClient {

    public static final SdkRestClient instance = new SdkRestClient();

    private static final Logger LOGGER = LoggerFactory.getLogger(SdkRestClient.class);
    private CoreSdkSettings setting;
    private CloseableHttpClient httpClient;
    private HttpContext httpClientContext;
    private Gson gson;

    public static SdkRestClient getInstance() {
        return instance;
    }

    private SdkRestClient() {
        setting = CoreSdkSettings.getInstance();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new GsonJacksonDateAdapter());
        builder.registerTypeAdapter(Timestamp.class, new GsonJacksonTimestampAdapter());
        this.gson = builder.create();
    }

    public <T extends RestResponseBase> T restCall(String httpMethod, String commandRelativeUri, Object command, Class<? extends RestResponseBase> responseClz) {
        Map<String, String> params = new HashMap<>();
        StringHelper.toStringMap(null, command, params);
        return this.restCall(httpMethod, commandRelativeUri, params, responseClz);
    }

    public <T extends RestResponseBase> T restCall(String httpMethod, String commandRelativeUri, Map<String, String> params, Class<? extends RestResponseBase> responseClz) {
        if (params == null) {
            params = new HashMap<>();
        }

        params.put("appkey", this.setting.getAppKey());
        String signature = SignatureHelper.computeSignature(params, this.setting.getSecretKey());
        params.put("signature", signature);
        return this.rawCall(httpMethod, commandRelativeUri, params, responseClz);
    }

    public <T extends RestResponseBase> T rawCall(String httpMethod, String commandRelativeUri, Object command, Class<? extends RestResponseBase> responseClz) {
        Map<String, String> params = new HashMap<>();
        StringHelper.toStringMap((String) null, command, params);
        return this.rawCall(httpMethod, commandRelativeUri, params, responseClz);
    }

    @SuppressWarnings("unchecked")
    public <T extends RestResponseBase> T rawCall(String httpMethod, String commandRelativeUri, Map<String, String> params, Class<? extends RestResponseBase> responseClz) {
        assert commandRelativeUri != null;

        if (params == null) {
            params = new HashMap<>();
        }

        CloseableHttpClient client = this.openHttpClient();
        String uri = this.composeFullUri(commandRelativeUri);

        try {
            CloseableHttpResponse res;
            if (!httpMethod.equalsIgnoreCase("POST") && !httpMethod.equalsIgnoreCase("PUT")) {
                RequestBuilder builder = RequestBuilder.create(httpMethod).setUri(uri);

                for (Object o : ((Map) params).entrySet()) {
                    Map.Entry entry = (Map.Entry) o;
                    builder.addParameter(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
                }

                HttpUriRequest uriRequest = builder.build();
                res = client.execute(uriRequest, this.httpClientContext);
            } else {
                HttpPost post = new HttpPost(uri);
                post.setHeader("systag", setting.getSystemTag());
                List<NameValuePair> formparams = new ArrayList<>();
                Iterator var10 = ((Map) params).entrySet().iterator();

                while (var10.hasNext()) {
                    Map.Entry entry = (Map.Entry) var10.next();
                    formparams.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
                }

                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
                post.setEntity(entity);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("REST call request, url={}, params={}", uri, params);
                }

                res = client.execute(post, this.httpClientContext);
            }

            RestResponseBase response;
            try {
                HttpEntity resEntity = res.getEntity();
                String responseBody = EntityUtils.toString(resEntity);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("REST call response, responseBody={}", responseBody);
                }

                response = this.gson.fromJson(responseBody, responseClz);
            } finally {
                res.close();
            }

            return (T) response;
        } catch (ClientProtocolException var17) {
            LOGGER.warn("REST call exception", var17);
            return (T) SimpleConvertHelper.convert(new RestResponseBase("HTTP", 400, (String) null), responseClz);
        } catch (IOException var18) {
            return (T) SimpleConvertHelper.convert(new RestResponseBase("HTTP", 502, (String) null), responseClz);
        }
    }

    public void close() {
        this.closeHttpClient();
    }

    public CoreSdkSettings getSetting() {
        return this.setting;
    }

    public void setSetting(CoreSdkSettings setting) {
        this.setting = setting;
    }

    public String getAppKey() {
        return this.setting.getAppKey();
    }

    public String getSecretKey() {
        return this.setting.getSecretKey();
    }

    private String composeFullUri(String relativeUri) {
        String serviceUri = this.setting.getConnectUrl();
        if (relativeUri == null) {
            return serviceUri;
        } else {
            StringBuilder sb = new StringBuilder(serviceUri);
            if (!serviceUri.endsWith("/")) {
                sb.append("/");
            }

            if (relativeUri.startsWith("/")) {
                sb.append(relativeUri.substring(1));
            } else {
                sb.append(relativeUri);
            }

            return sb.toString();
        }
    }

    private CloseableHttpClient openHttpClient() {
        if (this.isHttpClientOpen()) {
            return this.httpClient;
        } else {
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
            this.httpClient = HttpClients.custom().setConnectionManager(cm).build();
            this.httpClientContext = HttpClientContext.create();
            if (this.httpClient == null) {
                throw new RuntimeException("Unable to create HttpClient object");
            } else {
                return this.httpClient;
            }
        }
    }

    private boolean isHttpClientOpen() {
        return this.httpClient != null;
    }

    private void closeHttpClient() {
        if (this.httpClient != null) {
            assert this.httpClientContext != null;

            try {
                this.httpClient.close();
                this.httpClient = null;
                this.httpClientContext = null;
            } catch (IOException var2) {
                LOGGER.warn("Unalbe to close", var2);
            }
        }
    }
}