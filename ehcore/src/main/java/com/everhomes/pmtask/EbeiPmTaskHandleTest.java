package com.everhomes.pmtask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.parking.ketuo.EncryptUtil;
import com.everhomes.pmtask.ebei.EbeiResponseEntity;
import com.everhomes.util.RuntimeErrorException;


public class EbeiPmTaskHandleTest {
	
	public static final String CATEGORY_SEPARATOR = "/";
	
	private static final String LIST_SERVICE_TYPE = "rest/crmFeedBackInfoJoin/serviceTypeList";
	private static final String CREATE_TASK = "/rest/crmFeedBackInfoJoin/uploadFeedBackOrder";
	private static final String LIST_TASK = "/rest/crmFeedBackInfoJoin/feedBackOrderList";
	private static final String GET_TASK_DETAIL = "/rest/crmFeedBackInfoJoin/feedBackOrderDetail";
	private static final String CANCEL_TASK = "/rest/crmFeedBackInfoJoin/cancelOrder";
	private static final String EVALUATE = "/rest/crmFeedBackInfoJoin/evaluateFeedBack";
	private static final String GET_TOKEN = "/rest/ebeiInfo/sysQueryToken";

	
    SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EbeiPmTaskHandleTest.class);

	
	private void listServiceType(String projectId) {
		JSONObject param = new JSONObject();
		param.put("projectId", projectId);
		
		String json = postToEbei(param, LIST_SERVICE_TYPE);
		
		EbeiResponseEntity entity = JSONObject.parseObject(json, EbeiResponseEntity.class);
		
		if(entity.isSuccess()) {
			Object data = entity.getData();
			
		}
		
	}
	
	public static void main(String[] args) {
		JSONObject param = new JSONObject();
		param.put("projectId", "240111044331055940");
		postToEbei(param, null);
//		login();
	}
	
	public static String postToEbei(JSONObject param, String type) {
		CloseableHttpClient httpclient =HttpClients.createDefault();

		HttpPost httpPost = new HttpPost("http://120.24.88.192:13902/ebeitest/rest/crmFeedBackInfoJoin/serviceTypeList");
		StringBuilder result = new StringBuilder();
		
        String data = null;
		
//		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
//		nvps.add(new BasicNameValuePair("projectId", "240111044331055940"));
        
        StringEntity stringEntity = new StringEntity(param.toString(), "utf-8");
		CloseableHttpResponse response = null;
		InputStream instream = null;
		try {
	        stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

			httpPost.setEntity(stringEntity);
			httpPost.setHeader("Content-Type", "application/json");
//			httpPost.addHeader("EBEI_TOKEN", "Kf3j5uHnxjSst37WgYNIrrR//wVj0l1R0unt1KrrwnXnn5f8zmCcnwSrK0pcON3K\r\n");
//			httpPost.addHeader("HTMIMI_USERID", "123456");
			response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			
			if (entity != null) {
				instream = entity.getContent();
				BufferedReader reader = null;
				reader = new BufferedReader(new InputStreamReader(instream,"utf8"));
				String s;
            	
            	while((s = reader.readLine()) != null){
            		result.append(s);
				}
			}
		} catch (IOException e) {
			LOGGER.error("Pmtask request error, param={}", param, e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Pmtask request error.");
		}finally {
            try {
            	instream.close();
				response.close();
			} catch (IOException e) {
				LOGGER.error("Pmtask close instream, response error, param={}", param, e);
			}
        }
		String json = result.toString();
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("Data from Ebei, json={}", json);
		
		return json;
	}

	public static String login() {
		CloseableHttpClient httpclient =HttpClients.createDefault();

		HttpPost httpPost = new HttpPost("http://120.24.88.192:13902/ebeitest/"+ "rest/ebeiInfo/sysQueryToken");
		StringBuilder result = new StringBuilder();
		
        String data = null;
		
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("userId", "123456"));
		CloseableHttpResponse response = null;
		InputStream instream = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF8"));
//			httpPost.addHeader("EBEI_TOKEN", "");
//			httpPost.addHeader("HTMIMI_USERID", "");
			response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			
			if (entity != null) {
				instream = entity.getContent();
				BufferedReader reader = null;
				reader = new BufferedReader(new InputStreamReader(instream,"utf8"));
				String s;
            	
            	while((s = reader.readLine()) != null){
            		result.append(s);
				}
			}
		} catch (IOException e) {
			LOGGER.error("Pmtask request error, param={}", e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Pmtask request error.");
		}finally {
            try {
            	instream.close();
				response.close();
			} catch (IOException e) {
				LOGGER.error("Pmtask close instream, response error, param={}", e);
			}
        }
		String json = result.toString();
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("Data from Ebei, json={}", json);
		
		return json;
	}
}
