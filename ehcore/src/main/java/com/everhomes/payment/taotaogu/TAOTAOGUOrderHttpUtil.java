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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.cert.Cert;
import com.everhomes.cert.CertProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.payment.VendorConstant;
import com.google.gson.Gson;

/**
 * TAOTAOGU 订单登录和post请求方法
 * @author 
 *
 */
public class TAOTAOGUOrderHttpUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(TAOTAOGUHttpUtil.class);

	private static ConfigurationProvider configProvider;
	private static String url;
	
	static{
		configProvider = PlatformContext.getComponent("configurationProviderImpl");
		url = configProvider.getValue("taotaogu.order.url", "");
	}
	
	public static Map orderLogin(Map vendorDataMap) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		Gson gson = new Gson();
		HttpPost request = new HttpPost(url+"/iips2/order/login");
		JSONObject json = new JSONObject();
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// 可以方便地修改日期格式
		String timeStr = dateFormat.format(now);
		
		String chnl_type = (String) vendorDataMap.get(VendorConstant.CHNL_TYPE);
		String chnl_id = (String) vendorDataMap.get(VendorConstant.CHNL_ID);
		String merch_id = (String) vendorDataMap.get(VendorConstant.MERCH_ID);
		String termnl_id = (String) vendorDataMap.get(VendorConstant.TERMNL_ID);
		json.put("chnl_type", chnl_type);
		json.put("chnl_id", chnl_id);
		json.put("chnl_sn", System.currentTimeMillis());
		json.put("merch_id", merch_id);
		json.put("termnl_id", termnl_id);
	
		CertProvider certProvider =  PlatformContext.getComponent("certProviderImpl");
		Cert serverCer = certProvider.findCertByName(configProvider.getValue(VendorConstant.SERVER_CER, ""));
		InputStream serverCerIn = new ByteArrayInputStream(serverCer.getData());
		Cert clientPfx = certProvider.findCertByName(configProvider.getValue(VendorConstant.CLIENT_PFX, ""));
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
		LOGGER.debug(rspText);
		LOGGER.debug("---------------------------------------------------------------------");
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
		HttpPost request = new HttpPost(url+method);
		
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
