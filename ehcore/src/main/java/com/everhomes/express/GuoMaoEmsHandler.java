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
import com.everhomes.express.guomao.GuoMaoEMSResponseEntity;
import com.everhomes.parking.Utils;
import com.everhomes.rest.express.GetExpressLogisticsDetailResponse;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
//后面的3为表eh_express_companies中父id为0的行的id， 国贸 EMS
@Component(ExpressHandler.EXPRESS_HANDLER_PREFIX+"2")
public class GuoMaoEmsHandler implements ExpressHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(GuoMaoEmsHandler.class);
	//日期格式
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.systemDefault());

	//EMS标准快递代号，目前只用标准快递
	private static final String UTF8_CHARACTER_SET = "UTF-8";
	private static final String PK_POST_CODE = "100000";
	
	@Override
	public String getBillNo(ExpressOrder expressOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetExpressLogisticsDetailResponse getExpressLogisticsDetail(ExpressCompany expressCompany, String billNo) {
		Map<String, String> params = generateGetLogisticsparams(expressCompany, billNo);
//		String jsonResult = sendApplicationXwwwFormUrlencodedRequest(params, expressCompany.getOrderUrl());
//		GuoMaoEMSResponseEntity<List<String>> response = JSONObject.parseObject(jsonResult,new TypeReference<GuoMaoEMSResponseEntity<List<String>>>(){});
//		if(!response.isSuccess()){
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
//					"createOrder failed, response = "+jsonResult);
//		}
		return null;
	}

	private Map<String, String> generateGetLogisticsparams(ExpressCompany expressCompany, String billNo) {
		//系统级别参数
		Map<String, Object> params = generateGeneralParams(expressCompany);
		params.put("method", "ems.inland.trace.query");
		//接口级别参数
		params.put("mailNo", billNo);
		String pkEmsSignature = sha256Sign(params, expressCompany.getAppSecret(), UTF8_CHARACTER_SET);
		params.put("sign", pkEmsSignature);
		
		return convertStringMap(params);
	}

	@Override
	public void createOrder(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		Map<String, String> params = generateCreateOrderparams(expressOrder, expressCompany);
		String jsonResult = sendApplicationXwwwFormUrlencodedRequest(params, expressCompany.getOrderUrl());
		GuoMaoEMSResponseEntity<List<String>> response = JSONObject.parseObject(jsonResult,new TypeReference<GuoMaoEMSResponseEntity<List<String>>>(){});
		if(!response.isSuccess()){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"createOrder failed, response = "+jsonResult);
		}
	}


	@Override
	public void updateOrderStatus(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		Map<String, String> params = generateCancellOrderparams(expressOrder, expressCompany);
		String jsonResult = sendApplicationXwwwFormUrlencodedRequest(params, expressCompany.getOrderUrl());
		GuoMaoEMSResponseEntity<List<String>> response = JSONObject.parseObject(jsonResult,new TypeReference<GuoMaoEMSResponseEntity<List<String>>>(){});
		if(!response.isSuccess()){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"updateOrderStatus failed, response = "+jsonResult);
		}
	}
	
	private Map<String, String> generateCancellOrderparams(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		//系统级别参数
		Map<String, Object> params = generateGeneralParams(expressCompany);
		params.put("method", "ems.inland.waybill.got.cancellation");
		//接口级别参数
		params.put("txLogisticID", expressOrder.getOrderNo());
		params.put("cancelCode", "28");//撤单原因码13：上门揽收不及时放弃 19：客户自交寄 20：客户转交其他公司 16：已联系客户并确认重复下单 8：超禁限 9：超规格 28：客户取消订单 
		//12：因资费原因放弃 22：客户更改取件地址 23：EMS到达时限慢 24：测试单 25：客户要求到付 26：其他
		String pkEmsSignature = sha256Sign(params, expressCompany.getAppSecret(), UTF8_CHARACTER_SET);
		params.put("sign", pkEmsSignature);
		return convertStringMap(params);
	}

	private String sendApplicationXwwwFormUrlencodedRequest(Map<String, String> params, String url){
		Map<String,String> heads = new HashMap<String,String>();
		heads.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		heads.put("Accept", "text/xml,text/javascript,text/html");
		return Utils.post(url, params, heads);
	}
	
	private Map<String, String> generateCreateOrderparams(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		//系统级别参数
		Map<String, Object> params = generateGeneralParams(expressCompany);
		params.put("method", "ems.inland.waybill.got");
		//接口级别参数
		params.put("gotInfo", getSubParams(expressOrder));
		String pkEmsSignature = sha256Sign(params, expressCompany.getAppSecret(), UTF8_CHARACTER_SET);
		params.put("sign", pkEmsSignature);
		
		return convertStringMap(params);
	}
	
	private static Map<String,String> convertStringMap(Map<String, Object> params){
		Map<String, String> stringMap = new HashMap<String, String>();
		for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			Object value = params.get(key);
			if(value instanceof Map){
				stringMap.put(key, StringHelper.toJsonString(value));
			}else{
				stringMap.put(key, value.toString());
			}
		}
		return stringMap;
	}
	
	private Map<String, Object> getSubParams(ExpressOrder expressOrder) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("txLogisticID", expressOrder.getOrderNo());
		params.put("collect", getSendAddress(expressOrder));
		params.put("sender", getSendAddress(expressOrder));
		params.put("receiver", getReceiveAddress(expressOrder));
		params.put("orderType", "1");//一般情况下为1，寄件人付费
		if(expressOrder.getInsuredPrice() != null){
			params.put("insuredAmount", (long)(expressOrder.getInsuredPrice().floatValue()*100));//
		}
		return params;
	}
	
	private static Map<String,String> getSendAddress(ExpressOrder o){
		return getAddress(o.getSendName(), o.getSendPhone(), o.getSendProvince(), o.getSendCity(), o.getSendCounty(), o.getSendDetailAddress());
	}
	
	private static Map<String,String> getReceiveAddress(ExpressOrder o){
		return getAddress(o.getReceiveName(), o.getReceivePhone(), o.getReceiveProvince(), o.getReceiveCity(), o.getReceiveCounty(), o.getReceiveDetailAddress());
	}
	
	private static Map<String,String> getAddress(String name,String phone,String prov,String city,String county,String address) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("postCode", PK_POST_CODE);
		params.put("phone", phone);
		params.put("prov", prov);
		params.put("city", city);
		params.put("county", county);
		params.put("address", address);
		return params;
	}

	private Map<String, Object> generateGeneralParams(ExpressCompany expressCompany){
		Map<String, Object> params = new HashMap<String, Object>();
		//系统参数取值
		params.put("timestamp", DATE_TIME_FORMATTER.format(Instant.now()));
		params.put("version", "V3.01");
//		params.put("partner_id", ""); // 可不填写
		params.put("format", "json");
		params.put("app_key", expressCompany.getAppKey());
		params.put("charset", UTF8_CHARACTER_SET);
		params.put("authorization", expressCompany.getAuthorization());
		return params;
	}

	public static String sha256Sign(Map<String, Object> params, String appSecret, String charset) {
		// 得到“生成签名的字符串”
		String content = getSortParams(params) + appSecret;
		// 对“生成签名的字符串”进行MD5，并进行BASE64操作，根据请求编码得到签名
		if (charset == null || "".equals(charset)) {
			charset = "";
		}
		String sign = "";
		try {
			MessageDigest md5 = MessageDigest.getInstance("SHA-256");
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
	
	public static String getSortParams(Map<String, Object> params) {
		// 删掉sign参数
		params.remove("sign");
		String contnt = "";
		Set<String> keySet = params.keySet();
		List<String> keyList = new ArrayList<String>();
		for (String key : keySet) {
			Object value = params.get(key);
			// 将值为空的参数排除
			if (value != null && value.toString().length() > 0) {
				keyList.add(key);
			}
		}
		sort(keyList);
		// 将参数和参数值按照排序顺序拼装成字符串
		for (int i = 0; i < keyList.size(); i++) {
			String key = keyList.get(i);
			Object value = params.get(key);
			if(value instanceof Map){
				contnt += key + StringHelper.toJsonString(value);
			}else{
				contnt += key + value;
			}
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

	@Override
	public void getOrderStatus(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		// TODO Auto-generated method stub
		
	}

}
