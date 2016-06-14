package com.everhomes.payment.taotaogu;

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

public class TAOTAOGUHttpUtil {
	static final String URL = "http://test.ippit.cn:30821/iccard/service";
	
	public static ResponseEntiy post(String brandCode,String msgType,Map<String, Object> param) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpPost request = new HttpPost(URL);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		String msg = getJson(brandCode,msgType,param);
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
	
	private static String getJson(String brandCode,String msgType,Map<String, Object> param) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Gson gson = new Gson();
		
		Map<String, Object> requestParam = new HashMap<String, Object>();
		requestParam.put("AppName", "ICCard");
		requestParam.put("Version","V0.01");
		requestParam.put("ClientDt",sdf.format(new Date()));
		requestParam.put("SrcId",brandCode);
		requestParam.put("DstId","00000000");
		requestParam.put("MsgType",msgType);
		requestParam.put("MsgID",brandCode + StringUtils.leftPad(String.valueOf(System.currentTimeMillis()), 24, "0"));
		requestParam.put("Sign", "");

		requestParam.put("Param",param);
		byte[] data = gson.toJson(requestParam).getBytes();
		
		byte[] sign = CertCoder.sign(data, "E:\\一卡通接口\\jxd.keystore","jxd", "123456", "123456");
		requestParam.put("Sign",ByteTools.BytesToHexStr(sign));
		
		return gson.toJson(requestParam);
	}
	public static void main(String[] args) {
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("BranchCode", "10002900");
			//param.put("CardId", "5882572900500000182");
			param.put("CardId", "5882572900500005884");
			param.put("AcctType", "00");
			param.put("SubAcctType", "");
			post("10002900","1010",param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
