package com.everhomes.oauth2;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Clients {
	private static final Logger LOGGER = LoggerFactory.getLogger(Clients.class);
	private CloseableHttpClient httpClient;
	private HttpContext httpClientContext;

	public Clients() {
	}

	private boolean isHttpClientOpen() {
		return this.httpClient != null;
	}

	private CloseableHttpClient openHttpClient() {
		if (isHttpClientOpen())
			return this.httpClient;
		

		HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		this.httpClient = HttpClients.custom().setConnectionManager(cm).build();
		this.httpClientContext = HttpClientContext.create();

		if (this.httpClient == null)
			throw new RuntimeException("Unable to create HttpClient object");
		return this.httpClient;
	}

	public String restCall(String method,String commandRelativeUri, Map<String, String> params,String name,String value) {
		assert (commandRelativeUri != null);

		CloseableHttpClient client = openHttpClient();
		String uri = commandRelativeUri;

		try {
			CloseableHttpResponse res;
			if (method.equalsIgnoreCase("POST")
					|| method.equalsIgnoreCase("PUT")) {
				HttpPost post = new HttpPost(uri);
				if(name!=null&&!name.equals("")&&value!=null&&!value.equals(""))
					post.addHeader(name, value);
				post.addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
				if(params!=null){
					List<NameValuePair> formparams = new ArrayList<NameValuePair>();
					for (Map.Entry<String, String> entry : params.entrySet()) {
						formparams.add(new BasicNameValuePair(entry.getKey(), entry
								.getValue()));
					}
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
					post.setEntity(entity);
				}
				res = client.execute(post, this.httpClientContext);
			} else {
				RequestBuilder builder = RequestBuilder.create(
						method.toString()).setUri(uri);
				if(name!=null&&!name.equals("")&&value!=null&&!value.equals(""))
					builder.addHeader(name, value);
				builder.addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
				if(params!=null){
					for (Map.Entry<String, String> entry : params.entrySet()) {
						builder.addParameter(new BasicNameValuePair(entry.getKey(),
								entry.getValue()));
					}
				}
				HttpUriRequest uriRequest = builder.build();
				res = client.execute(uriRequest, this.httpClientContext);
			}

			try {
				HttpEntity resEntity = res.getEntity();
				String responseBody = EntityUtils.toString(resEntity);
				return responseBody;
			} finally {
				res.close();
			}
		} catch (ClientProtocolException e) {
			LOGGER.warn("Unexpected exception", e);
		} catch (IOException e) {
			LOGGER.warn("IOException", e);
		}
		return null;
	}
}
