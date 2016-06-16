package com.everhomes.payment.taotaogu;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
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

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.cert.Cert;
import com.everhomes.cert.CertProvider;
import com.everhomes.payment.VendorConstant;
import com.google.gson.Gson;

/**
 * TAOTAOGU 订单登录和post请求方法
 * @author 
 *
 */
public class TAOTAOGUOrderHttpUtil {
	
	public static Map orderLogin() throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		Gson gson = new Gson();
		HttpPost request = new HttpPost(VendorConstant.ORDER_URL+"/iips2/order/login");
		JSONObject json = new JSONObject();
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// 可以方便地修改日期格式
		String timeStr = dateFormat.format(now);
		//timeStr = "20160223152525";
		json.put("chnl_type", VendorConstant.CHNL_TYPE);
		json.put("chnl_id", VendorConstant.CHNL_ID);
		json.put("chnl_sn", System.currentTimeMillis());
		json.put("merch_id", VendorConstant.MERCH_ID);
		json.put("termnl_id", VendorConstant.TERMNL_ID);
	
		CertProvider certProvider =  PlatformContext.getComponent("certProviderImpl");
		Cert serverCer = certProvider.findCertByName(VendorConstant.SERVER_CER);
		InputStream serverCerIn = new ByteArrayInputStream(serverCer.getData());
		Cert clientPfx = certProvider.findCertByName("sunwen_client.pfx");
		InputStream clientPfxIn = new ByteArrayInputStream(clientPfx.getData());
		
		String msg = json.toString();
		msg = Base64.encodeBase64String(OrderCertCoder.encryptByPublicKey(msg.getBytes(), serverCerIn));
		
		byte[] r=  OrderCertCoder.sign(msg.getBytes(), clientPfxIn,null, clientPfx.getCertPass());
		String sign = Base64.encodeBase64String(r);
		
		pairs.add(new BasicNameValuePair("msg", msg));
		pairs.add(new BasicNameValuePair("sign", sign));
		
		request.setEntity(new UrlEncodedFormEntity(pairs, "GBK"));
		HttpResponse rsp = httpClient.execute(request);
		StatusLine status = rsp.getStatusLine();
		String rspText = EntityUtils.toString(rsp.getEntity(), "GBK");
		
		if(rspText.indexOf("return_code") != -1){
			int a = rspText.indexOf("\"return_code\":\"");
			int b = rspText.indexOf("\"",a+15);
			String returnCode = rspText.substring(a + 15,b);
			if(!"00".equals(returnCode))
				return null;
		}
		int a = rspText.indexOf("msg=");
		int b = rspText.indexOf("&sign=");
		msg = rspText.substring(a + 4, b);
		sign = rspText.substring(b + 6);
		boolean bSign = OrderCertCoder.verifySign(msg.getBytes(), Base64.decodeBase64(sign), serverCerIn);
		r = OrderCertCoder.decryptByPrivateKey(Base64.decodeBase64(msg), clientPfxIn, null, clientPfx.getCertPass());
		
		String r1 = new String(r, "GBK");
		
		Map result = gson.fromJson(r1, Map.class);
		return result;
	}

	public static Map post(String method,String token,String aesKey,JSONObject json) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		Gson gson = new Gson();
		HttpPost request = new HttpPost(VendorConstant.ORDER_URL+method);
		
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			
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
		
		if(rspText.indexOf("return_code") != -1){
			int a = rspText.indexOf("\"return_code\":\"");
			int b = rspText.indexOf("\"",a+15);
			String returnCode = rspText.substring(a + 15,b);
			if(!"00".equals(returnCode))
				return null;
		}
		int a = rspText.indexOf("msg=");
		int b = rspText.indexOf("&sign=");
		msg = rspText.substring(a + 4, b);
		String sign = rspText.substring(b + 6);
		
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
