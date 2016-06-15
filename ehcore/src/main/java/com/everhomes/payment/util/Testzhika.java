package com.everhomes.payment.util;

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

import com.everhomes.payment.taotaogu.ByteTools;
import com.google.gson.Gson;

public class Testzhika {
	public static final String URL = "http://test.ippit.cn:30821/iccard/service";
	
	protected static boolean test() throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		
		HttpPost request = new HttpPost(URL);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		String s = getJson();
		System.out.println(s);
		pairs.add(new BasicNameValuePair("msg", s));
		request.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
		HttpResponse rsp = httpClient.execute(request);
		StatusLine status = rsp.getStatusLine();
		String rspText = EntityUtils.toString(rsp.getEntity(), "UTF-8");
		
		
		System.out.println(rspText);
		
		return true;
	}
	
	public static void main(String[] args) {
		try {
			test();
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
		requestParam.put("MsgType","0000");
		requestParam.put("MsgID","10002900" + StringUtils.leftPad(String.valueOf(System.currentTimeMillis()), 24, "0"));
		requestParam.put("Sign", "");

		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("BranchCode", "10002900");
//		param.put("CardId", "5882572900500000182");
//		param.put("AcctType", "00");
		param.put("BranchCode", "10002900");
		param.put("CardPatternid", "887093");
		param.put("CardNum", "1");
		param.put("DeliveryUser", "");
		param.put("DeliveryContact", "");
		requestParam.put("Param",param);
		byte[] data = gson.toJson(requestParam).getBytes();
		
		byte[] sign = CertCoder.sign(data, "E:\\一卡通接口\\jxd.keystore","jxd", "123456", "123456");
		requestParam.put("Sign",ByteTools.BytesToHexStr(sign));
		
		return gson.toJson(requestParam);
	}

}
