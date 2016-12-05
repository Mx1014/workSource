package com.everhomes.parking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.parking.ketuo.EncryptUtil;
import com.everhomes.util.RuntimeErrorException;

public class Test {

	private static CloseableHttpClient httpclient = HttpClients.createDefault();
	private static final String RECHARGE = "/api/pay/CardRecharge";
	private static final String GET_CARD = "/api/pay/GetCarCardInfo";
	private static final String GET_TYPES = "/api/pay/GetCarTypeList";
	private static final String GET_CARd_RULE = "/api/pay/GetCardRule";
	private static final String GET_TEMP_FEE = "/api/pay/GetParkingPaymentInfo";
	private static final String PAY_TEMP_FEE = "/api/pay/PayParkingFee";
	private static final String RULE_TYPE = "1"; //只显示ruleType = 1时的充值项
	
	public static void main(String[] args) {
		JSONObject param = new JSONObject();
		param.put("carType", "2");
		String json = Test.post(param, GET_CARd_RULE);
        System.out.println(json);
		
	}
	
	
	
	
	
	public static String post(JSONObject param, String type) {
		HttpPost httpPost = new HttpPost("http://113.108.41.29:8099" + type);
		StringBuilder result = new StringBuilder();
		
        String key = "F7A0B971B199FD2A52468575";
        String iv = "20161202";
        String user = "ktapi";
        String pwd = "0306C3";
        String data = null;
		try {
			data = EncryptUtil.getEncString(param, key, iv);
		} catch (Exception e) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Parking encrypt param error.");
		}
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("data", data));
		CloseableHttpResponse response = null;
		InputStream instream = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF8"));
			httpPost.addHeader("user", user);
			httpPost.addHeader("pwd", pwd);
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
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Parking request error.");
		}finally {
            try {
            	instream.close();
				response.close();
			} catch (IOException e) {
			}
        }
		String json = result.toString();
		
		return json;
	}
}
