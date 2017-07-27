// @formatter:off
package com.everhomes.express;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.express.guomao.GuoMaoChinaPostResponseEntity;
import com.everhomes.express.guomao.GuoMaoEMSResponseEntity;
import com.everhomes.parking.Utils;
import com.everhomes.rest.express.GetExpressLogisticsDetailResponse;
import com.everhomes.util.RuntimeErrorException;
//后面的3为表eh_express_companies中父id为0的行的id， 国贸 EMS
@Component(ExpressHandler.EXPRESS_HANDLER_PREFIX+"2")
public class GuoMaoEmsHandler implements ExpressHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(GuoMaoEmsHandler.class);
	//日期格式
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.systemDefault());

	//EMS标准快递代号，目前只用标准快递
	private static final String STANDARD_BIZ_CODE = "06";
	private static final String UTF8_CHARACTER_SET = "UTF-8";
	
	@Override
	public String getBillNo(ExpressOrder expressOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetExpressLogisticsDetailResponse getExpressLogisticsDetail(ExpressCompany expressCompany, String billNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createOrder(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		Map<String, String> params = generateGetMailNumParams(expressOrder, expressCompany);
		String jsonResult = sendApplicationXwwwFormUrlencodedRequest(params, expressCompany.getOrderUrl());
		GuoMaoEMSResponseEntity<List<String>> response = JSONObject.parseObject(jsonResult,new TypeReference<GuoMaoEMSResponseEntity<List<String>>(){});
	}


	@Override
	public void updateOrderStatus(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		// TODO Auto-generated method stub
		
	}
	
	private String sendApplicationXwwwFormUrlencodedRequest(Map<String, String> params, String url){
		Map<String,String> heads = new HashMap<String,String>();
		heads.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		heads.put("Accept", "text/xml,text/javascript,text/html");
		return Utils.post(url, params, heads);
	}
	
	private Map<String, String> generateGetMailNumParams(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		//系统级别参数
		Map<String, String> params = getGeneralParams(expressCompany);
		//接口级别参数
		params.put("count", "1");//获取一个快递单号就行了
		params.put("bizcode", STANDARD_BIZ_CODE);
		String pkEmsSignature = sign(params, expressCompany.getAppSecret(), UTF8_CHARACTER_SET);
		params.put("sign", pkEmsSignature);
		return params;
//		return generateStringParam(params);
	}
	
	private Map<String, String> getGeneralParams(ExpressCompany expressCompany){
		Map<String, String> params = new HashMap<String, String>();
		//系统参数取值
		params.put("timestamp", DATE_TIME_FORMATTER.format(Instant.now()));
		params.put("version", "V0.1");
		params.put("method", "ems.inland.mms.mailnum.apply");
//		params.put("partner_id", ""); // 可不填写
		params.put("format", "json");
		params.put("app_key", expressCompany.getAppKey());
		params.put("charset", UTF8_CHARACTER_SET);
		params.put("authorization", expressCompany.getAuthorization());
		return params;
	}

	public static String sign(Map<String, String> params, String appSecret, String charset) {
		// 得到“生成签名的字符串”
		String content = getSortParams(params) + appSecret;
		// 对“生成签名的字符串”进行MD5，并进行BASE64操作，根据请求编码得到签名
		if (charset == null || "".equals(charset)) {
			charset = "";
		}
		String sign = "";
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			sign = Base64.getEncoder().encodeToString(md5.digest(content.getBytes(charset)));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("guomao ems sign excetion = {}", e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"guomao ems sign excetion = "+e);
		
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("guomao ems sign excetion = {}", e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"guomao ems sign excetion = "+e);
		}
		return sign;
	}
	
	public static String getSortParams(Map<String, String> params) {
		// 删掉sign参数
		params.remove("sign");
		String contnt = "";
		Set<String> keySet = params.keySet();
		List<String> keyList = new ArrayList<String>();
		for (String key : keySet) {
			String value = params.get(key);
			// 将值为空的参数排除
			if (value != null && value.length() > 0) {
				keyList.add(key);
			}
		}
		sort(keyList);
		// 将参数和参数值按照排序顺序拼装成字符串
		for (int i = 0; i < keyList.size(); i++) {
			String key = keyList.get(i);
			contnt += key + params.get(key);
		}
		return contnt;
	}
	
	public static void sort(List<String> keyList){
		Collections.sort(keyList, (o1,o2)->{
			int length = Math.min(o1.length(), o2.length());
			for (int i = 0; i < length; i++) {
				char c1 = o1.charAt(i);
				char c2 = o2.charAt(i);
				int r = c1 - c2;
				if (r != 0) {
					// char值小的排前边
					return r;
				}
			}
			// 2个字符串关系是str1.startsWith(str2)==true
			// 取str2排前边
			return o1.length() - o2.length();
		});
	}

}
