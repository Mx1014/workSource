package com.everhomes.wanke;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.RuntimeErrorException;

public class Test {
	private static final Logger LOGGER = LoggerFactory.getLogger(MaShenServiceConfVendorHandler.class);
	private static CloseableHttpClient httpClient = null;
	static{ 
		SSLContext sslcontext = null;
		try {
			sslcontext = SSLContext.getInstance("SSLv3");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	// 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
		X509TrustManager trustManager = new X509TrustManager() {
			@Override
			public void checkClientTrusted(
					java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(
					java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		try {
			sslcontext.init(null, new TrustManager[] { trustManager }, null);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
        httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
	}
	
	public static void main(String[] args) {
		String param = "/token/get?appId=15725632&appSecret=b4b994d5ed0a9990" ;
		MashenResponseEntity entity = httpGet(param);
		if(!entity.isSuccess()) {
			LOGGER.error("Response of Mashen, result={}.", entity);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Request of Mashen failed");
		}
		 Map<String, Object> map = entity.getData();
	        String accessToken = (String) map.get("accessToken");
		String param1 = "/token/getJsapiToken?accessToken=" + accessToken;
		MashenResponseEntity entity1= httpGet(param1);
		System.out.println(entity1);
	}
	
	private static MashenResponseEntity httpGet(String param){
		String url = "https://api.open.imasheng.com/openapi";
		if(StringUtils.isBlank(url)) {
			LOGGER.error("Wanke.mashen.url not found, url={}", url);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Wanke.mashen.url not found");
		}
		HttpGet request = new HttpGet(url + param);
		HttpResponse rsp = null;
		try{
			rsp = httpClient.execute(request);
		}catch(Exception e){
			LOGGER.error("HTTP client execute exception, param={}", param, e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"HTTP client execute exception.");
		}
		
		StatusLine status = rsp.getStatusLine();
		if(status.getStatusCode() == 200){
			String rspText;
			try {
				rspText = EntityUtils.toString(rsp.getEntity(), "UTF-8");
			} catch (Exception e) {
				LOGGER.error("HTTP client EntityUtils toString exception, param={}", param, e);
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"HTTP client EntityUtils toString exception");
			}
			if(LOGGER.isDebugEnabled())
				LOGGER.debug("Mashen response info :rspText={}", rspText);
			
			//MashenResponseEntity resp = (MashenResponseEntity) StringHelper.fromJsonString(rspText, MashenResponseEntity.class);
			MashenResponseEntity resp = JSON.parseObject(rspText, MashenResponseEntity.class);
			
			return resp;
		}else{
			LOGGER.error("Mashen HTTP request response status is not 200, status, rspText={}.", status, rsp.getEntity());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Mashen HTTP request response status is not 200");
		}
	}
}
