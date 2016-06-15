package com.everhomes.payment.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;

import com.everhomes.payment.taotaogu.AESCoder;
import com.everhomes.payment.taotaogu.SHA1;
import com.google.gson.Gson;

public class TAOTAOGUOrderHttpUtil {
	
	public static Map orderLogin() throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpPost request = new HttpPost(
				"http://test.ippit.cn:8010/orderform/iips2/order/login");
		JSONObject json = new JSONObject();
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// 可以方便地修改日期格式
		String timeStr = dateFormat.format(now);
		//timeStr = "20160223152525";
		json.put("chnl_type", "WEB");
		json.put("chnl_id", "12345678");
		json.put("chnl_sn", System.currentTimeMillis());
		json.put("merch_id", "862900000000001");
		json.put("termnl_id", "00011071");
	
		
		String msg = json.toString();
		msg = Base64.encodeBase64String(com.ipp.order.utils.CertCoder.encryptByPublicKey(msg.getBytes(), "E:\\一卡通接口\\server.cer"));
		
		byte[] r=  com.ipp.order.utils.CertCoder.sign(msg.getBytes(), "E:\\一卡通接口\\client.pfx",null, "123456");
		String sign = Base64.encodeBase64String(r);
		
		pairs.add(new BasicNameValuePair("msg", msg));
		pairs.add(new BasicNameValuePair("sign", sign));
		
		request.setEntity(new UrlEncodedFormEntity(pairs, "GBK"));
		HttpResponse rsp = httpClient.execute(request);
		StatusLine status = rsp.getStatusLine();
		String rspText = EntityUtils.toString(rsp.getEntity(), "GBK");
		
		int a = rspText.indexOf("msg=");
		int b = rspText.indexOf("&sign=");
		msg = rspText.substring(a + 4, b);
		sign = rspText.substring(b + 6);
		boolean bSign = com.ipp.order.utils.CertCoder.verifySign(msg.getBytes(), Base64.decodeBase64(sign), "E:\\一卡通接口\\server.cer");
		r = com.ipp.order.utils.CertCoder.decryptByPrivateKey(Base64.decodeBase64(msg), "E:\\一卡通接口\\client.pfx", null, "123456");
		
		String r1 = new String(r, "GBK");
		System.out.println(r1);
		Gson gson = new Gson();
		Map result = gson.fromJson(r1, Map.class);
		return result;
	}

	
	public static void main(String[] args) {
		try {
			orderLogin();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Map post() throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpPost request = new HttpPost(
				"http://test.ippit.cn:8010/orderform/iips2/order/tokenrequest");
		
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		JSONObject json = new JSONObject();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// 可以方便地修改日期格式
		String timeStr = dateFormat.format(now);
		//timeStr = "20160223152525";
		json.put("chnl_type", "WEB");
		json.put("chnl_id", "12345678");
		json.put("chnl_sn", System.currentTimeMillis());
		json.put("card_id", "5882572900500005884");
		json.put("reserved", "00011071");
		json.put("request_time", timeStr);		

		String token = "578416bb28e11fc633555ff5c41582fb";
		String aesKey = "fd2434d34ba3f670342e21c376ccd006";		
		
		pairs.add(new BasicNameValuePair("token", token));
		String msg = Base64.encodeBase64String(AESCoder.encrypt(json.toString().getBytes("GBK"), aesKey.getBytes()));
		pairs.add(new BasicNameValuePair("msg", msg));
		pairs.add(new BasicNameValuePair("sign", SHA1.EnCodeSHA1(msg + aesKey + token)));
		
		request.setEntity(new UrlEncodedFormEntity(pairs, "GBK"));
		HttpResponse rsp = httpClient.execute(request);
		@SuppressWarnings("unused")
		StatusLine status = rsp.getStatusLine();
		String rspText = EntityUtils.toString(rsp.getEntity(), "GBK");
		System.out.println(rspText);
		
		
		int a = rspText.indexOf("msg=");
		int b = rspText.indexOf("&sign=");
		msg = rspText.substring(a + 4, b);
		String sign = rspText.substring(b + 6);
		Gson gson = new Gson();
		Map map = gson.fromJson(msg, Map.class);
		String newSign = SHA1.EnCodeSHA1(msg + aesKey + token);
		System.out.println(msg + "\n" + sign + "\n" + newSign);
		String data = (String) map.get("data");
		String data1 = new String (AESCoder.decrypt(Base64.decodeBase64(data), aesKey.getBytes()), "GBK");
		System.out.println(data1);
		
		Map result = gson.fromJson(data1, Map.class);
		return result;
	}
}
