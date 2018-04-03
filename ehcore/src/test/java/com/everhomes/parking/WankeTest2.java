package com.everhomes.parking;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.parking.wanke.WankeJsonEntity;
import com.everhomes.util.RuntimeErrorException;

public class WankeTest2 {
	private static final String RECHARGE = "/Parking/MouthCardRecharge";
	private static final String GET_CARD = "/Parking/CardDataQuery";
	private static final String GET_TYPES = "/Parking/GetMonthCardList";
	private static final String GET_TEMP_FEE = "/Parking/GetCost";
	private static final String PAY_TEMP_FEE = "/Parking/PayCost";
    public static void main(String[] args) {
		JSONObject param = new JSONObject();
		
		param.put("plateNo", "桂KS6909");

		param.put("flag", "2");
//	    param.put("amount", 300*100);
//	    param.put("payMons", 1);
//	    param.put("chargePaidNo", 100);
//	    param.put("payTime", "20170110105501");
//	    param.put("sign", stringMD5("浙AS0616&2&30000&1&100&20170110105501"));
//		param.put("orderNo", "1701101105590023");
//		param.put("amount", 36000);
//	    param.put("payType", 1);

		System.out.println(param.toJSONString());
		String json = postToWanke(param, GET_CARD);
		
		String s = "{\"errorCode\":1,\"errorMsg\":\"未查询到该用户信息\",\"data\":\"null\"}";
		
		WankeJsonEntity<Object> entity = JSONObject.parseObject(s, new TypeReference<WankeJsonEntity<Object>>(){});
		System.out.println(json);
		
	}
    
	public static String postToWanke(JSONObject param, String type) {
		HttpPost httpPost = new HttpPost("http://140.207.16.122:7021" + type);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String json = null;
		
		CloseableHttpResponse response = null;
		
		try {
			StringEntity stringEntity = new StringEntity(param.toString(), "utf8");
			httpPost.setEntity(stringEntity);
			response = httpclient.execute(httpPost);
			
			int status = response.getStatusLine().getStatusCode();
			
			if(status == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				
				if (entity != null) {
					json = EntityUtils.toString(entity);
				}
			}
			
		} catch (IOException e) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Parking request error.");
		}finally {
            try {
				response.close();
			} catch (IOException e) {
			}
        }
		
		return json;
	}
}
