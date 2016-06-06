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

import com.google.gson.Gson;

public class Test {
	public static final String URL = "http://test.ippit.cn:30821/iccard/service";
	
//	protected static boolean orderLogin() throws Exception {
//		CloseableHttpClient httpClient = HttpClients.createDefault();
//
//		HttpPost request = new HttpPost(
//				"http://test.ippit.cn:8010/orderform/iips2/order/login");
//		JSONObject json = new JSONObject();
//		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
//		Date now = new Date();
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// 可以方便地修改日期格式
//		String timeStr = dateFormat.format(now);
//		//timeStr = "20160223152525";
//		json.put("chnl_type", "WEB");
//		json.put("chnl_id", "12345678");
//		json.put("chnl_sn", "10011");
//		json.put("merch_id", "862900000000001");
//		json.put("termnl_id", "00011071");
//	
//		
//		String msg = json.toString();
//		msg = Base64.encodeBase64String(CertCoder.encryptByPublicKey(msg.getBytes(), "E:\\一卡通接口\\server.cer"));
//		System.out.println("加密" + msg);
//		
//		byte[] r= CertCoder.sign(msg.getBytes(), "E:\\一卡通接口\\client.pfx",null, "123456");
//		String sign = Base64.encodeBase64String(r);
//		System.out.println(sign);
//		
//		pairs.add(new BasicNameValuePair("msg", msg));
//		pairs.add(new BasicNameValuePair("sign", sign));
//		
//		request.setEntity(new UrlEncodedFormEntity(pairs, "GBK"));
//		HttpResponse rsp = httpClient.execute(request);
//		StatusLine status = rsp.getStatusLine();
//		String rspText = EntityUtils.toString(rsp.getEntity(), "GBK");
//		System.out.println(rspText);
//		
//		
//		int a = rspText.indexOf("msg=");
//		int b = rspText.indexOf("&sign=");
//		msg = rspText.substring(a + 4, b);
//		sign = rspText.substring(b + 6);
////		boolean bSign = CertCoder.verifySign(msg.getBytes(), Base64.decodeBase64(sign), "D:/server.cer");
////		r = CertCoder.decryptByPrivateKey(Base64.decodeBase64(msg), "D:/client.pfx", null, "123456");
////		
////		String r1 = new String(r, "GBK");
////		System.out.println(bSign + rspText + "\n" + r1);
//		return true;
//	}

	
	
	protected static boolean test() throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		
		HttpPost request = new HttpPost(URL);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		String s = getJson();
		System.out.println(s);
		pairs.add(new BasicNameValuePair("msg", s));
		request.setEntity(new UrlEncodedFormEntity(pairs, "GBK"));
		HttpResponse rsp = httpClient.execute(request);
		StatusLine status = rsp.getStatusLine();
		String rspText = EntityUtils.toString(rsp.getEntity(), "GBK");
		
		
		System.out.println(rspText);
		
		return true;
	}
	
	public static void main(String[] args) {
		try {
		//	orderLogin();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getJson() throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Gson gson = new Gson();
		
		Map<String, Object> requestParam = new HashMap<String, Object>();
		requestParam.put("AppName", "ICCard");
		requestParam.put("Version","V0.01");
		requestParam.put("ClientDt",sdf.format(new Date()));
		requestParam.put("SrcId","10002900");
		requestParam.put("DstId","00000000");
		requestParam.put("MsgType","1010");
		requestParam.put("MsgID","10002900" + StringUtils.leftPad(String.valueOf(System.currentTimeMillis()), 24, "0"));
		requestParam.put("Sign", "");

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("BranchCode", "10002900");
		param.put("CardId", "5882572900500000182");
		param.put("AcctType", "00");
		requestParam.put("Param",param);
		byte[] data = gson.toJson(requestParam).getBytes();
		
		byte[] sign = CertCoder.sign(data, "E:\\一卡通接口\\jxd.keystore","jxd", "123456", "123456");
		requestParam.put("Sign",ByteTools.BytesToHexStr(sign));
		
		return gson.toJson(requestParam);
	}

}
