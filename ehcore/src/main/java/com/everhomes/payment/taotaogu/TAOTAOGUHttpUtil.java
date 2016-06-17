package com.everhomes.payment.taotaogu;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.cert.Cert;
import com.everhomes.cert.CertProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.payment.VendorConstant;
import com.google.gson.Gson;

/**
 * TAOTAOGU 制卡post请求方法
 * @author 
 *
 */
public class TAOTAOGUHttpUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(TAOTAOGUHttpUtil.class);
	
	private static ConfigurationProvider configProvider;
	private static String url;
	
	static{
		configProvider = PlatformContext.getComponent("configurationProviderImpl");
		url = configProvider.getValue("taotaogu.card.url", "");
	}

	public static ResponseEntiy post(Map vendorDataMap,String msgType,Map<String, Object> param) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpPost request = new HttpPost(url);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		String msg = getJson(vendorDataMap,msgType,param);
		pairs.add(new BasicNameValuePair("msg", msg));
		request.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
		HttpResponse rsp = httpClient.execute(request);
		StatusLine status = rsp.getStatusLine();
		if(status.getStatusCode() == 200){
			String rspText = EntityUtils.toString(rsp.getEntity(), "UTF-8");
			Gson gson = new Gson();
			ResponseEntiy resp = gson.fromJson(rspText, ResponseEntiy.class);
			
			return resp;	
		}
		return null;
	}
	
	private static String getJson(Map vendorDataMap,String msgType,Map<String, Object> param) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Gson gson = new Gson();
		
		Map<String, Object> requestParam = new HashMap<String, Object>();
		String appName = (String) vendorDataMap.get(VendorConstant.APP_NAME);
		String version = (String) vendorDataMap.get(VendorConstant.VERSION);
		String dstId = (String) vendorDataMap.get(VendorConstant.DSTID);
		String brandCode = (String) vendorDataMap.get(VendorConstant.BRANCH_CODE);
		requestParam.put("AppName", appName);
		requestParam.put("Version",version);
		requestParam.put("ClientDt",sdf.format(new Date()));
		requestParam.put("SrcId",brandCode);
		requestParam.put("DstId",dstId);
		requestParam.put("MsgType",msgType);
		requestParam.put("MsgID",brandCode + StringUtils.leftPad(String.valueOf(System.currentTimeMillis()), 24, "0"));
		requestParam.put("Sign", "");

		requestParam.put("Param",param);
		byte[] data = gson.toJson(requestParam).getBytes();
		
		CertProvider certProvider = PlatformContext.getComponent("certProviderImpl");
		
		Cert cert = certProvider.findCertByName(configProvider.getValue(VendorConstant.KEY_STORE, ""));
		InputStream in = new ByteArrayInputStream(cert.getData());
		
		String pass = cert.getCertPass();
		String[] passArr = pass.split(",");
		
		byte[] sign = CertCoder.sign(data, in,passArr[0], passArr[1], passArr[2]);
		requestParam.put("Sign",ByteTools.BytesToHexStr(sign));
		
		return gson.toJson(requestParam);
	}
}
