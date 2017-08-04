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
import com.everhomes.util.StringHelper;
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
		GuoMaoEMSResponseEntity<List<String>> response = JSONObject.parseObject(jsonResult,new TypeReference<GuoMaoEMSResponseEntity<List<String>>>(){});
		if(response.isSuccess()){
			generateCreateOrderParams(expressOrder, expressCompany, response);
		}
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
				"createOrder get billNo failed, response = "+jsonResult);
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
		params.put("method", "ems.inland.mms.mailnum.apply");
		//接口级别参数
		params.put("count", "1");//获取一个快递单号就行了
		params.put("bizcode", STANDARD_BIZ_CODE);
		String pkEmsSignature = sign(params, expressCompany.getAppSecret(), UTF8_CHARACTER_SET);
		params.put("sign", pkEmsSignature);
		return params;
	}
	
	private Map<String, String> generateCreateOrderParams(ExpressOrder expressOrder, ExpressCompany expressCompany, GuoMaoEMSResponseEntity<List<String>> response) {
		//系统级别参数
		Map<String, String> params = getGeneralParams(expressCompany);
		params.put("method", "ems.inland.waybill.create.normal");
		//接口级别参数
		params.put("waybill", generateWayBill(expressOrder, response));//
		params.put("size", "1");//
		
		String pkEmsSignature = sign(params, expressCompany.getAppSecret(), UTF8_CHARACTER_SET);
		params.put("sign", pkEmsSignature);
		return params;
	}
	
	private String generateWayBill(ExpressOrder expressOrder, GuoMaoEMSResponseEntity<List<String>> response) {
		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("txLogisticID","");//物流订单号(一票多单必填)
		params.put("orderNo",expressOrder.getOrderNo());//电商订单号 TODO
		params.put("mailNum",response.getMailnums().get(0));
//		params.put("subMails","");//一票多单子单号，以“,”(半角逗号)分隔，非一票多单不填
//		params.put("ypdjpayment","");//一票多件付费方式（1-集中主单计费 	2-平均重量计费 3-分单免首重4-主分单单独计费）
		params.put("orderType",1);//普通订单，1
		params.put("serviceType",1);//标准快递，与本系统枚举有冲突，所有直接填写1，国贸EMS的标准快递
//		params.put("remark","");//备注
//		params.put("weight","");//实际重量，单位：克
//		params.put("volumeWeight","");//体积重量，单位：克
//		params.put("feeWeight","");//计费重量，单位：克
		params.put("insuredAmount",expressOrder.getInsuredPrice().floatValue()*100);//保险金额，单位：分
		params.put("insureType","2");//所负责任，2-保价，3-保险
		// --------------发件人-------------------
		Map<String, Object> senderMap = new HashMap<String, Object>();
		senderMap.put("name", expressOrder.getSendName());
//		senderMap.put("postCode", "");
		senderMap.put("phone", expressOrder.getSendPhone());
		senderMap.put("mobile", expressOrder.getSendPhone());
		senderMap.put("prov", expressOrder.getSendProvince());
		senderMap.put("city", expressOrder.getSendCity());
		senderMap.put("address", expressOrder.getSendCounty()+expressOrder.getSendDetailAddress());
		params.put("sender",StringHelper.toJsonString(senderMap));
		// --------------收件人-------------------
		Map<String, Object> receiverMap = new HashMap<String, Object>();
		senderMap.put("name", expressOrder.getReceiveName());
//		senderMap.put("postCode", "");
		senderMap.put("phone", expressOrder.getReceivePhone());
		senderMap.put("mobile", expressOrder.getReceivePhone());
		senderMap.put("prov", expressOrder.getReceiveProvince());
		senderMap.put("city", expressOrder.getReceiveCity());
		senderMap.put("address", expressOrder.getReceiveCounty()+expressOrder.getReceiveDetailAddress());
		params.put("receiver",StringHelper.toJsonString(receiverMap));
		
//		params.put("items",""); //商品信息	（XML节点名称items）
//		params.put("cargoType",""); //内件性质（文件、物品）
//		params.put("custCode",""); //EMS客户代码
		params.put("deliveryTime",DATE_TIME_FORMATTER.format(Instant.now())); //投递时间(yyyy-mm-dd hh:mm:ss)[时区是？UTC？以下所有时间都有同样问题。回复：标准时间格式，北京时间，GMT+0800]
//		params.put("receiverPay","");//收件人付费
//		params.put("collectionMoney","");//代收货款
//		params.put("revertBill","");//是否返单，0：返单，1：不返单
//		params.put("revertMailNo","");//反向运单号
//		params.put("postage","");//邮费
//		params.put("postageUppercase","");//大写邮费金额
//		params.put("sendType","");//寄递类型，0：单程寄递，1：双程后置去程，2：双程后置返程(公安项目专用)
//		params.put("commodityMoney","");//商品金额(工本费)
//		params.put("state","");//自定义标识
//		params.put("orgCode",""); //机构号码
//		params.put("blank1","");
//		params.put("blank2","");
//		params.put("blank3","");
//		params.put("blank4","");
//		params.put("blank5","");
		return StringHelper.toJsonString(params);
	}

	private Map<String, String> getGeneralParams(ExpressCompany expressCompany){
		Map<String, String> params = new HashMap<String, String>();
		//系统参数取值
		params.put("timestamp", DATE_TIME_FORMATTER.format(Instant.now()));
		params.put("version", "V0.1");
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
			if (value != null && value.toString().length() > 0) {
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

	@Override
	public void getOrderStatus(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		// TODO Auto-generated method stub
		
	}

}
