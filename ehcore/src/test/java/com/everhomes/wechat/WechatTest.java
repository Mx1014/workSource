package com.everhomes.wechat;


import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.wx.WeChatService;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class WechatTest extends CoreServerTestCase {

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private WeChatService wechatService;

	@Test
	public void testWxHttpGet(){
		String res = httpGet("http://members.3322.org/dyndns/getip", "");
		System.out.println(res);

	}


	@Test
	public void testRestCall(){
//		String s = wechatService.testRestCall("", null, null);
//		System.out.println(s);

	}


	/**
	 * 放在com.everhomes.wx.WXAuthController中的方法，没法注入，直接copy出来
	 * @param url
	 * @param safeUrl
	 * @return
	 */
	private String httpGet(String url, String safeUrl) {
		CloseableHttpClient httpclient = null;

		CloseableHttpResponse response = null;
		String result = null;
		try {
			httpclient = HttpClients.createDefault();

			HttpGet httpGet = new HttpGet(url);

			//使用测试环境没有固定ip代理访问，从有外网ip的服务器访问微信，从而实现固定id
			String wechatProxyHost = configurationProvider.getValue("wechat.proxy.host", null);
			int wechatProxyPort = configurationProvider.getIntValue("wechat.proxy.port", 0);
			if(wechatProxyHost != null && wechatProxyPort != 0){
				HttpHost proxy = new HttpHost(wechatProxyHost, wechatProxyPort);
				RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy).build();
				httpGet.setConfig(requestConfig);
			}

			response = httpclient.execute(httpGet);

			int status = response.getStatusLine().getStatusCode();
			if(status != 200){
				System.out.println("Failed to get the http result, url=" +safeUrl + ", status={} " + response.getStatusLine());
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"Failed to get the http result");
			} else {
				HttpEntity resEntity = response.getEntity();
				String charset = getContentCharSet(resEntity);
				result = EntityUtils.toString(resEntity, charset);
				System.out.println("Get http result, charset=" +charset+", url=" + safeUrl + ", result= " + result);
			}
		} catch (Exception e) {
			System.out.println("Failed to get the http result, url= " + safeUrl +  e);
		} finally {
			if(response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if(httpclient != null) {
				try {
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	public static String getContentCharSet(final HttpEntity entity) throws ParseException {
		if (entity == null) {
			throw new IllegalArgumentException("HTTP entity may not be null");
		}
		String charset = null;
		if (entity.getContentType() != null) {
			HeaderElement values[] = entity.getContentType().getElements();
			if (values.length > 0) {
				NameValuePair param = values[0].getParameterByName("charset" );
				if (param != null) {
					charset = param.getValue();
				}
			}
		}

		if(charset == null || charset.length() == 0){
			charset = "UTF-8";
		}

		return charset;
	}
}
