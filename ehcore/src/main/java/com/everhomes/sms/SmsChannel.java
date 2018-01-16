package com.everhomes.sms;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmsChannel {

    private static final Logger logger = LoggerFactory.getLogger(SmsChannel.class);

    public enum HttpMethod { GET, POST, PATCH, PUT, DELETE;}

    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final String EMPTY = "";

    private RequestConfig requestConfig;
    private boolean isSecure;

    private Map<String, String> headers;
    private String url;
    private HttpMethod method;
    private Map<String, Object> bodyMap;
    private String bodyStr;

    private Charset charset;
    private ContentType contentType;

    SmsChannel(boolean isSecure) {
        this.isSecure = isSecure;
        this.headers = new HashMap<>();
        this.bodyMap = new HashMap<>();
        this.method = HttpMethod.POST;
        this.charset = UTF_8;
        this.contentType = ContentType.APPLICATION_FORM_URLENCODED.withCharset(UTF_8);
    }

    public RspMessage send() {
        CloseableHttpClient client = getHttpClient();
        try {
            HttpUriRequest request = createRequest();
            if (logger.isDebugEnabled()) {
                logRequest(request);
            }

            HttpResponse response = client.execute(request);
            String result = EntityUtils.toString(response.getEntity());

            if (logger.isDebugEnabled()) {
                logResponse(response, result);
            }
            return createRspMessage(result, response);
        } catch (Throwable e) {
            logger.error("Send sms error", e);
            return createErrorRspMessage(e.getMessage(), -1);
        } finally {
            try { client.close(); } catch (Exception ignored) { }
        }
    }

    private void logResponse(HttpResponse response, String result) {
        try {
            logger.debug("Send sms response status line = {}", response.getStatusLine());
            for (Header header : response.getAllHeaders()) {
                logger.debug("Send sms response header [ {} : {}]", header.getName(), header.getValue());
            }
            logger.debug("Send sms response body = {}", result);
        } catch (Exception e) {
            //
        }
    }

    private void logRequest(HttpUriRequest request) {
        try {
            logger.debug("Send sms request line = {}", request.getRequestLine());
            for (Header header : request.getAllHeaders()) {
                logger.debug("Send sms request Header[ {} : {}]", header.getName(), header.getValue());
            }
            if (request instanceof HttpEntityEnclosingRequest) {
                HttpEntityEnclosingRequest req = (HttpEntityEnclosingRequest) request;
                logger.debug("Send sms request entity = {} ", EntityUtils.toString(req.getEntity(), charset));
            }
        } catch (IOException | ParseException e) {
            //
        }
    }

    private RspMessage createRspMessage(String result, HttpResponse response) {
        Map<String, String> headers = new HashMap<>();
        for (Header header : response.getAllHeaders()) {
            headers.put(header.getName(), header.getValue());
        }
        return new RspMessage(result, response.getStatusLine().getStatusCode(), headers);
    }

    private RspMessage createErrorRspMessage(String result, Integer code) {
        return new RspMessage(result, code, null);
    }

    private CloseableHttpClient getHttpClient() {
        HttpClientBuilder builder;
        if (isSecure) {
            builder = HttpClients.custom().setConnectionManager(createConnectionManager());
        } else {
            builder = HttpClients.custom().setConnectionManager(new PoolingHttpClientConnectionManager());
        }
        return builder.build();
    }

    private RequestConfig getRequestConfig() {
        return requestConfig == null ? RequestConfig.DEFAULT : requestConfig;
    }

    private HttpUriRequest createRequest() {
        EntityBuilder entityBuilder = EntityBuilder.create();

        if (StringUtils.isNotEmpty(bodyStr)) {
            entityBuilder.setText(bodyStr);
        } else if (MapUtils.isNotEmpty(bodyMap)) {
            List<NameValuePair> params = new ArrayList<>();
            bodyMap.forEach((key, val) -> params.add(new BasicNameValuePair(key, String.valueOf(val))));
            entityBuilder.setParameters(params);
        } else {
            entityBuilder.setText(EMPTY);
        }
        entityBuilder.setContentType(contentType);

        HttpEntity httpEntity = entityBuilder.build();

        RequestBuilder requestBuilder = RequestBuilder.create(method.name());
        requestBuilder.setUri(url);
        requestBuilder.setEntity(httpEntity);
        requestBuilder.setConfig(getRequestConfig());

        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(requestBuilder::addHeader);
        }
        return requestBuilder.build();
    }

    private SSLContext createSSLContext() {
        SSLContextBuilder builder = SSLContexts.custom();
        try {
            builder.loadTrustMaterial(null, (chian, type) -> true);
            return builder.build();
        } catch (Exception e) {
            logger.error("build ssl context failed", e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "create ssl context error,internal error");
        }
    }

    public SmsChannel setUrl(String url) {
        this.url = url;
        return this;
    }

    public SmsChannel setMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    public SmsChannel setBodyMap(Map<String, Object> bodyMap) {
        this.bodyMap = bodyMap;
        return this;
    }

    public SmsChannel setBodyStr(String bodyStr) {
        this.bodyStr = bodyStr;
        return this;
    }

    public SmsChannel setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public SmsChannel setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
        return this;
    }

    public SmsChannel setContentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    public SmsChannel setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public SmsChannel addHeader(String key, String value) {
        if (this.headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
        return this;
    }

    public SmsChannel addParameter(String key, Object value) {
        if (this.bodyMap == null) {
            bodyMap = new HashMap<>();
        }
        bodyMap.put(key, value);
        return this;
    }

    private PoolingHttpClientConnectionManager createConnectionManager() {
        SSLContext sslContext = createSSLContext();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }

            @Override
            public void verify(String host, SSLSocket ssl) throws IOException {

            }

            @Override
            public void verify(String host, X509Certificate cert) throws SSLException {

            }

            @Override
            public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {

            }
        });
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create().register("https", sslsf).build();
        return new PoolingHttpClientConnectionManager(socketFactoryRegistry);
    }
}
