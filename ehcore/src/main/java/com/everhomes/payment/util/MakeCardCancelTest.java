package com.everhomes.payment.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;

public class MakeCardCancelTest {

	private static final String CARD_ISSUER = "10365840";
	
//	public static String getJson() throws Exception{
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//		Gson gson = new Gson();
//		
//		RequestParam requestParam = new RequestParam();
//		requestParam.setAppName("ICCard");
//		requestParam.setVersion("V0.01");
//		requestParam.setClientDt(sdf.format(new Date()));
//		requestParam.setSrcId(CARD_ISSUER);
//		requestParam.setDstId("00000000");
//		requestParam.setMsgType("0002");
//		requestParam.setMsgID(CARD_ISSUER + StringUtils.leftPad(String.valueOf(System.currentTimeMillis()), 24, "0"));
//
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("CardIssuer", CARD_ISSUER);
//		param.put("CardPatternid", "261");
//		param.put("CardNum", "5");
//		param.put("OrigMsgId", "10365840000000000001408698521778");	//原制卡平台流水，即制卡请求的MsgID
//		requestParam.setParam(param);
//		
//		byte[] sign = CertCoder.sign(gson.toJson(requestParam).getBytes(), "e:/xuyuji.keystore", "xuyuji", "123456", "123456");
//		requestParam.setSign(ByteTools.BytesToHexStr(sign));
//		
//		return gson.toJson(requestParam);
//	}
//	
//	public static void main(String[] args) throws Exception {
//		String json = getJson();
//		
//		Client.sendHttp(json);
//	}
}
