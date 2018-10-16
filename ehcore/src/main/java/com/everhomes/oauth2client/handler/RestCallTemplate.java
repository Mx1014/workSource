// @formatter:off

package com.everhomes.oauth2client.handler;

import com.everhomes.oauth2client.HttpErrorEntity;
import com.everhomes.oauth2client.HttpResponseEntity;
import com.google.gson.Gson;

import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.*;

import javax.xml.transform.Source;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by xq.tian on 2017/3/10.
 */
public class RestCallTemplate {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private RestTemplate template;

    private String url;
    private HttpMethod httpMethod = HttpMethod.POST;
    private Class<?> responseType;
    private Class<?> errorType;
    private Map<String, Object> variables = new HashMap<>();
    private HttpHeaders headers = new HttpHeaders();
    private Object body;
    private Gson gson = new Gson();

    private RestEntityHandler restEntityHandler;
    private RestHasErrorHandler hasErrorHandler;
    private RestErrorHandler restErrorHandler;

    public interface RestErrorHandler<E> {
        void error(HttpErrorEntity<E> e);
    }

    public interface RestEntityHandler<E> {
        void success(HttpResponseEntity<E> e);
    }

    public interface RestHasErrorHandler {
        boolean hasError(HttpStatus statusCode);
    }

    private RestCallTemplate(RestTemplate template) {
        this.template = template;
    }

    public static RestCallTemplate url(String url) {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);

        RestTemplate template = new RestTemplate(requestFactory);

        template.setErrorHandler(new NoneResponseErrorHandler());

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new StringHttpMessageConverter(DEFAULT_CHARSET));
        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        messageConverters.add(new ResourceHttpMessageConverter());
        messageConverters.add(new SourceHttpMessageConverter<Source>());
        messageConverters.add(new AllEncompassingFormHttpMessageConverter());

        if (romePresent) {
            // messageConverters.add(new AtomFeedHttpMessageConverter());
            // messageConverters.add(new RssChannelHttpMessageConverter());
        }
        if (jaxb2Present) {
            messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
        }
        if (jackson2Present) {
            messageConverters.add(new MappingJackson2HttpMessageConverter());
        }
        else if (jacksonPresent) {
            messageConverters.add(new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter());
        }

        template.setMessageConverters(messageConverters);

        RestCallTemplate restTemplate = new RestCallTemplate(template);
        restTemplate.url = url;

        restTemplate.hasErrorHandler =
                statusCode -> (statusCode.series() == HttpStatus.Series.CLIENT_ERROR ||
                statusCode.series() == HttpStatus.Series.SERVER_ERROR);

        return restTemplate;
    }

    public RestCallTemplate method(String method) {
        this.httpMethod = getHttpMethod(method);
        return this;
    }

    public RestCallTemplate errorHandler(RestHasErrorHandler hasErrorHandler, RestErrorHandler restErrorHandler) {
        this.hasErrorHandler = hasErrorHandler;
        this.restErrorHandler = restErrorHandler;
        return this;
    }

    public <E> RestCallTemplate onSuccess(RestEntityHandler<E> restEntityHandler) {
        this.restEntityHandler = restEntityHandler;
        return this;
    }
    public <E> RestCallTemplate onError(RestErrorHandler<E> restErrorHandler) {
        this.restErrorHandler = restErrorHandler;
        return this;
    }

    public RestCallTemplate errorHandler(RestErrorHandler restErrorHandler) {
        this.restErrorHandler = restErrorHandler;
        return this;
    }

    private HttpMethod getHttpMethod(String method) {
        if (method != null) {
            for (HttpMethod httpMethod : HttpMethod.values()) {
                if (httpMethod.name().equalsIgnoreCase(method)) {
                    return httpMethod;
                }
            }
        }
        return HttpMethod.POST;
    }

    public RestCallTemplate body(Object body) {
        this.body = body;
        return this;
    }

    public RestCallTemplate var(String name, Object o) {
        if (o == null) {
            return this;
        }
        this.variables.put(name, o);
        return this;
    }

    public RestCallTemplate header(String name, String...value) {
        if (value == null) {
            return this;
        }
        this.headers.put(name, new ArrayList<>(Arrays.asList(value)));
        return this;
    }

    public RestCallTemplate headers(Map<String, String> headers) {
        if (headers == null) {
            return this;
        }
        headers.forEach((k, v) -> this.headers.put(k, Collections.singletonList(v)));
        return this;
    }

    public RestCallTemplate respType(Class<?> responseType) {
        this.responseType = responseType;
        return this;
    }

    public RestCallTemplate errorType(Class<?> errorType) {
        this.errorType = errorType;
        return this;
    }

    public <R> HttpResponseEntity<R> get() {
        return execute(HttpMethod.GET);
    }

    public <R> HttpResponseEntity<R> post() {
        return execute(HttpMethod.POST);
    }

    public <R> HttpResponseEntity<R> execute() {
        return execute(httpMethod);
    }

    private <R> HttpResponseEntity<R> execute(HttpMethod method) {
        HttpEntity<Object> httpEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> result = template.exchange(url, method, httpEntity, String.class, variables);
        // handle error
        if (hasError(result.getStatusCode())) {
            Object errorObj;
            if (errorType == null || errorType == String.class) {
                errorObj = result.getBody();
            } else {
                Gson gson = new Gson();
                errorObj = gson.fromJson(result.getBody(), errorType);
            }

            HttpErrorEntity errorEntity = new HttpErrorEntity<>(errorObj, headers, result.getStatusCode());
            if (restErrorHandler != null) {
                restErrorHandler.error(errorEntity);// should throw some exception
            }
        }
        // 全部返回String, 再转一遍
        if (responseType == String.class) {
            return (HttpResponseEntity<R>) new HttpResponseEntity<>(result.getBody(), result.getHeaders(), result.getStatusCode());
        }
        R r = gson.fromJson(result.getBody(), (Class<R>) responseType);
        HttpResponseEntity<R> responseEntity = new HttpResponseEntity<>(r, result.getHeaders(), result.getStatusCode());
        if (restEntityHandler != null) {
            restEntityHandler.success(responseEntity);
        }
        return responseEntity;
    }

    public ResponseEntity<byte[]> byteDataExecute() {
        HttpEntity httpEntity = new HttpEntity(body, headers);
        this.hasErrorHandler = statusCode -> false;
        return template.exchange(url, httpMethod, httpEntity, byte[].class, variables);
    }

    public static QueryStringBuilder queryStringBuilder() {
        return new QueryStringBuilder();
    }

    private boolean hasError(HttpStatus statusCode) {
        return hasErrorHandler.hasError(statusCode);
    }

    public static class QueryStringBuilder {

        private QueryStringBuilder() { }

        private Map<String, List<Object>> queryMap = new HashMap<>();

        public QueryStringBuilder var(String name, Object o) {
            if (o == null) {
                return this;
            }
            List<Object> value = queryMap.computeIfAbsent(name, k -> new ArrayList<>());
            value.add(o);
            return this;
        }

        public QueryStringBuilder vars(Map<String, Object> vars) {
            if (vars == null) {
                return this;
            }
            vars.forEach((k, v) -> {
                List<Object> value = queryMap.computeIfAbsent(k, r -> new ArrayList<>());
                value.add(v);
            });
            return this;
        }

        public String build() {
            StringBuilder querySb = new StringBuilder();
            this.queryMap.forEach((k, v) -> {
                if (v.size() == 1) {
                    querySb.append(String.format("%s=%s&", k, v.get(0)));
                } else {
                    for (int i = 0; i < v.size(); i++) {
                        querySb.append(String.format("%s[%s]=%s&", k, i, v.get(i)));
                    }
                }
            });
            String query = querySb.toString();
            if (query.endsWith("&")) {
                query = query.substring(0, query.length() - 1);
            }
            return query;
        }

        public String build(String url) {
            return url + (url.length() > 0 ? "?" : "") + this.build();
        }
    }

    public static String urlEncode(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static class NoneResponseErrorHandler implements ResponseErrorHandler {

        /**
         * Delegates to {@link #hasError(HttpStatus)} with the response status code.
         */
        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return hasError(getHttpStatusCode(response));
        }

        private HttpStatus getHttpStatusCode(ClientHttpResponse response) throws IOException {
            HttpStatus statusCode;
            try {
                statusCode = response.getStatusCode();
            }
            catch (IllegalArgumentException ex) {
                throw new UnknownHttpStatusCodeException(response.getRawStatusCode(),
                        response.getStatusText(), response.getHeaders(), getResponseBody(response), getCharset(response));
            }
            return statusCode;
        }

        /**
         * Template method called from {@link #hasError(ClientHttpResponse)}.
         * <p>The default implementation checks if the given status code is
         * {@link org.springframework.http.HttpStatus.Series#CLIENT_ERROR CLIENT_ERROR}
         * or {@link org.springframework.http.HttpStatus.Series#SERVER_ERROR SERVER_ERROR}.
         * Can be overridden in subclasses.
         * @param statusCode the HTTP status code
         * @return {@code true} if the response has an error; {@code false} otherwise
         */
        protected boolean hasError(HttpStatus statusCode) {
            return (statusCode.series() == HttpStatus.Series.CLIENT_ERROR ||
                    statusCode.series() == HttpStatus.Series.SERVER_ERROR);
        }

        /**
         * This default implementation throws a {@link HttpClientErrorException} if the response status code
         * is {@link org.springframework.http.HttpStatus.Series#CLIENT_ERROR}, a {@link HttpServerErrorException}
         * if it is {@link org.springframework.http.HttpStatus.Series#SERVER_ERROR},
         * and a {@link RestClientException} in other cases.
         */
        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            //;
        }

        private byte[] getResponseBody(ClientHttpResponse response) {
            try {
                InputStream responseBody = response.getBody();

                if (responseBody != null) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
                    StreamUtils.copy(responseBody, out);
                    return out.toByteArray();
                }
            }
            catch (IOException ex) {
                // ignore
            }
            return new byte[0];
        }

        private Charset getCharset(ClientHttpResponse response) {
            HttpHeaders headers = response.getHeaders();
            MediaType contentType = headers.getContentType();
            return contentType == null ? DEFAULT_CHARSET :
                    (contentType.getCharSet() == null ? DEFAULT_CHARSET : contentType.getCharSet());
        }
    }

    private static boolean romePresent =
            ClassUtils.isPresent("com.sun.syndication.feed.WireFeed", RestTemplate.class.getClassLoader());

    private static final boolean jaxb2Present =
            ClassUtils.isPresent("javax.xml.bind.Binder", RestTemplate.class.getClassLoader());

    private static final boolean jackson2Present =
            ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", RestTemplate.class.getClassLoader()) &&
                    ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", RestTemplate.class.getClassLoader());

    private static final boolean jacksonPresent =
            ClassUtils.isPresent("org.codehaus.jackson.map.ObjectMapper", RestTemplate.class.getClassLoader()) &&
                    ClassUtils.isPresent("org.codehaus.jackson.JsonGenerator", RestTemplate.class.getClassLoader());

    public static void main(String[] args) {
        System.out.println(new java.lang.String(RestCallTemplate.url("https://www.baidu.com").byteDataExecute().getBody()));
    }
}
