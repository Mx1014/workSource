package com.everhomes.pmtask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.parking.ketuo.EncryptUtil;
import com.everhomes.pmtask.ebei.EbeiResponseEntity;
import com.everhomes.pmtask.ebei.EbeiServiceType;
import com.everhomes.util.RuntimeErrorException;


public class EbeiPmTaskHandle {
	
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
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EbeiPmTaskHandle.class);

	CloseableHttpClient httpclient = null;
	
	@PostConstruct
	public void init() {
		httpclient = HttpClients.createDefault();
	}

	private static void listServiceType(String projectId) {
		JSONObject param = new JSONObject();
		param.put("projectId", projectId);
		
		String json = postToEbei(param, LIST_SERVICE_TYPE, null);
		
		EbeiResponseEntity<EbeiServiceType> entity = JSONObject.parseObject(json, new TypeReference<EbeiResponseEntity<EbeiServiceType>>(){});
		
		if(entity.isSuccess()) {
			EbeiServiceType type = entity.getData();
			List<EbeiServiceType> types = type.getItems();
		}
		
	}
	
	public static void main(String[] args) {
		listServiceType("240111044331055940");
	}
	
	public static String postToEbei(JSONObject param, String method, Map<String, String> headers) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String url = "http://120.24.88.192:13902/ebeitest/";
		HttpPost httpPost = new HttpPost(url + method);
		CloseableHttpResponse response = null;
		
		String json = null;
		
		try {
			StringEntity stringEntity = new StringEntity(param.toString(), "utf8");
			httpPost.setEntity(stringEntity);
//			httpPost.addHeader("EBEI_TOKEN", "");
//			httpPost.addHeader("HTMIMI_USERID", "");
			
			response = httpclient.execute(httpPost);
			
			int status = response.getStatusLine().getStatusCode();
			if(status == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				
				if (entity != null) {
					json = EntityUtils.toString(entity, "utf8");
				}
			}
			
		} catch (IOException e) {
			LOGGER.error("Pmtask request error, param={}", param, e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Pmtask request error.");
		}finally {
            try {
				response.close();
			} catch (IOException e) {
				LOGGER.error("Pmtask close instream, response error, param={}", param, e);
			}
        }
		
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("Data from Ebei, json={}", json);
		
		return json;
	}

	
	@PreDestroy
	public void destroy() {
		if(null != httpclient) {
			try {
				httpclient.close();
			} catch (IOException e) {
				LOGGER.error("Pmtask close httpclient, response error, httpclient={}", httpclient, e);
			}
		}
	}
}
