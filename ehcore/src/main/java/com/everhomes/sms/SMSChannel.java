package com.everhomes.sms;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.RuntimeErrorException;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class SMSChannel {
	private static final Logger logger = LoggerFactory.getLogger(SMSChannel.class);
	private final HttpClientBuilder builder;
	private RequestConfig config = null;

	private final Map<String, String> headers;

	public SMSChannel(boolean isSecure) {
		headers = new HashMap<>();
		if (isSecure) {
			builder = HttpClients.custom().setConnectionManager(createConnectionManager());
		} else {
			builder = HttpClients.custom().setConnectionManager(new PoolingHttpClientConnectionManager());
		}
	}

	public RspMessage sendMessage(String uri, String method, Map<String, String> body, Map<String, String> headers) {
		CloseableHttpClient client = builder.setDefaultRequestConfig(config == null ? RequestConfig.DEFAULT : config)
				.build();
		if (headers != null)
			this.headers.putAll(headers);

		try {
			CloseableHttpResponse rsp = client.execute(createRequest(uri, method, body), new HttpClientContext());
			assert (rsp != null);
			String result = EntityUtils.toString(rsp.getEntity());
			if (rsp.getStatusLine().getStatusCode() >= 300) {
				logger.error("send sms message error.error reason is {}", result);
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"send sms message error.httpCode=" + rsp.getStatusLine().getStatusCode());
			}
			MultiValueMap mut = new MultiValueMap();
			for (Header header : rsp.getAllHeaders()) {
				mut.put(header.getName(), header.getValue());
			}
			RspMessage rspMessage = new RspMessage(result, mut);
			return rspMessage;
		} catch (IOException e) {
			logger.error("send sms message error", e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"send sms message error");
		} finally {
			try {
				client.close();
			} catch (Exception e) {
				// TODO
			}
		}
	}

	public SMSChannel setTimeout(int time) {
		config = RequestConfig.custom().setConnectTimeout(time).setSocketTimeout(time).build();
		return this;
	}

	public SMSChannel addHeaders(Map<String, String> headers) {
		this.headers.putAll(headers);
		return this;
	}

	public SMSChannel addHeader(String key, String value) {
		headers.put(key, value);
		return this;
	}

	public SMSChannel basicAuth(String username, String password) {
		String value = Base64.encode(String.format("%s:%s", username, password).getBytes(Charset.forName("utf-8")));
		headers.put("Authorization", String.format("Basic %s", value));
		return this;
	}

	private HttpUriRequest createRequest(String url, String method, Map<String, String> body) {
		RequestBuilder rbudiler = RequestBuilder.create(method);
		this.headers.forEach((key, val) -> {
			rbudiler.addHeader(key, val);
		});
		// assert entityBuilder has some value
		EntityBuilder entityBuilder = null;
		if (MapUtils.isNotEmpty(body)) {
			entityBuilder = EntityBuilder.create();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			body.forEach((jsonKey, jsonVal) -> {
				params.add(new BasicNameValuePair(jsonKey, jsonVal));
			});
			entityBuilder.setParameters(params);
		}
		if (entityBuilder == null) {
			return rbudiler.setUri(url).build();
		}
		return rbudiler.setEntity(entityBuilder.build()).setUri(url).build();
	}

	private SSLContext createSSLContext() {
		SSLContextBuilder builder = SSLContexts.custom();
		try {
			builder.loadTrustMaterial(null, (chian, type) -> {
				return true;
			});
			return builder.build();
		} catch (Exception e) {
			logger.error("build ssl context failed", e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"create ssl context error,internal error");
		}
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

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("https", sslsf).build();

		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		return cm;
	}
}
