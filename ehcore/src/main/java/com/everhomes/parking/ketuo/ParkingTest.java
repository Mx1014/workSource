package com.everhomes.parking.ketuo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSONObject;

public class ParkingTest {
	static String key = "F7A0B971B199FD2A1017CEC5";
	
	public static String GET_CAR_CARD_INFO = "GetCarCardInfo";
	//GetParkingPaymentInfo
	public static void main(String[] args) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost("http://220.160.111.114:9099/api/pay/GetCardRule");
            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            
            JSONObject param = new JSONObject();
//            param.put("cardNo", "20");
//            param.put("plateNo", "AJQ001");
//            param.put("carType", "2");
//            param.put("cardId", "20");
//            param.put("ruleType", "1");
//            param.put("ruleAmount", "1");
//            param.put("payMoney", "2000");
//            param.put("startTime", "2016-09-01 00:00:00");
//            param.put("endTime", "2016-09-30 23:59:59");
//            param.put("startTime", "20160101 00:00:00");
//            param.put("endTime", "20160909 00:00:00");
            String data = null;
			try {
				data = EncryptUtil.getEncString(param, key, "20160921");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			nvps.add(new BasicNameValuePair("data", data));
            try {
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF8"));
				httpPost.addHeader("user", "ktapi");
				httpPost.addHeader("pwd", "0306A9");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            CloseableHttpResponse response2 = null;
			try {
				response2 = httpclient.execute(httpPost);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            try {
                System.out.println(response2.getStatusLine());
                HttpEntity entity = response2.getEntity();
                if (entity != null) {
                	InputStream instream = null;
					try {
						instream = entity.getContent();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	BufferedReader reader = null;
					try {
						reader = new BufferedReader(new InputStreamReader(instream,"utf8"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	String s;
                	StringBuilder sb = new StringBuilder();
                	try {
						while((s = reader.readLine()) != null){
							sb.append(s);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	System.out.println(sb.toString());
                	try {
						instream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
                
            } finally {
                try {
					response2.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        } finally {
            try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
}
