// @formatter:off
package com.everhomes.express;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.express.guomao.GuoMaoEMSLogisticsItem;
import com.everhomes.express.guomao.GuoMaoEMSLogisticsResponse;
import com.everhomes.express.guomao.GuoMaoEMSResponseEntity;
import com.everhomes.express.guomao.util.GuoMaoEMSSignHelper;
import com.everhomes.parking.Utils;
import com.everhomes.rest.express.ExpressLogisticsStatus;
import com.everhomes.rest.express.ExpressOrderStatus;
import com.everhomes.rest.express.ExpressPayType;
import com.everhomes.rest.express.ExpressTraceDTO;
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
	
	@Autowired
	private ExpressOrderProvider expressOrderProvider;
	
	@Override
	public String getBillNo(ExpressOrder expressOrder) {
		return null;
	}

	@Override
	public GetExpressLogisticsDetailResponse getExpressLogisticsDetail(ExpressCompany expressCompany, String billNo) {
		Map<String, String> params = generateGetLogisticsparams(expressCompany, billNo);
		String jsonResult = request(params, expressCompany.getOrderUrl());
		GuoMaoEMSLogisticsResponse<List<GuoMaoEMSLogisticsItem>> emsResp = JSONObject.parseObject(jsonResult,new TypeReference<GuoMaoEMSLogisticsResponse<List<GuoMaoEMSLogisticsItem>>>(){});
		return converMailToResponse(emsResp, expressCompany, billNo);
	}
	
	private GetExpressLogisticsDetailResponse converMailToResponse(GuoMaoEMSLogisticsResponse<List<GuoMaoEMSLogisticsItem>> emsResp,ExpressCompany expressCompany, String billNo) {
		GetExpressLogisticsDetailResponse response = new GetExpressLogisticsDetailResponse();
		response.setBillNo(billNo);
		response.setExpressCompany(expressCompany.getName());
		response.setExpressLogo(expressCompany.getLogo());
		response.setTraces(new ArrayList<ExpressTraceDTO>());
		if(emsResp.isSuccess()){
			List<GuoMaoEMSLogisticsItem> list = emsResp.getTraces();
			if(list != null && list.size() > 0){
				for (GuoMaoEMSLogisticsItem item : list) {
					ExpressTraceDTO dto = new ExpressTraceDTO();
					dto.setAcceptAddress(item.getAcceptAddress()+" "+ item.getRemark());
					dto.setAcceptTime(item.getAcceptTime());
					response.getTraces().add(dto);
				}
			
				String statusvalue = list.get(list.size()-1).getCode();
				if("10".equals(statusvalue)){
					response.setLogisticsStatus(ExpressLogisticsStatus.RECEIVED.getCode());
				}else {
					response.setLogisticsStatus(ExpressLogisticsStatus.IN_TRANSIT.getCode());
				}
				
				if (list.size() > 1) {
					GuoMaoEMSLogisticsItem first = list.get(0);
					GuoMaoEMSLogisticsItem last = list.get(list.size()-1);
					response.setConsumeTime(getDeltaTime(first, last));
				}
			}
		}
		
		return response;
	}
	
	private String getDeltaTime(GuoMaoEMSLogisticsItem first, GuoMaoEMSLogisticsItem last) {
		LocalDateTime firstDatetime = LocalDateTime.parse(first.getAcceptTime(), DATE_TIME_FORMATTER);
		LocalDateTime lastDatetime = LocalDateTime.parse(last.getAcceptTime(), DATE_TIME_FORMATTER);
		Instant finstant = firstDatetime.atZone(ZoneOffset.systemDefault()).toInstant();
		Instant linstant = lastDatetime.atZone(ZoneOffset.systemDefault()).toInstant();
		long deltaHours = finstant.until(linstant, ChronoUnit.HOURS);
		//返回格式x.y表示x天y小时
		return deltaHours/24+"."+deltaHours%24;
	}

	private Map<String, String> generateGetLogisticsparams(ExpressCompany expressCompany, String billNo) {
		//系统级别参数
		Map<String, Object> params = generateGeneralParams(expressCompany);
		params.put("method", "ems.inland.trace.query");
		//接口级别参数
		params.put("mailNo", billNo);
		String pkEmsSignature = GuoMaoEMSSignHelper.sha256Sign(params, expressCompany.getAppSecret(), UTF8_CHARACTER_SET);
		params.put("sign", pkEmsSignature);
		
		return convertStringMap(params);
	}

	@Override
	public void createOrder(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		Map<String, String> params = generateCreateOrderparams(expressOrder, expressCompany);
		String jsonResult = request(params, expressCompany.getOrderUrl());
		GuoMaoEMSResponseEntity<List<String>> response = JSONObject.parseObject(jsonResult,new TypeReference<GuoMaoEMSResponseEntity<List<String>>>(){});
		if(!response.isSuccess()){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"createOrder failed, response = "+jsonResult);
		}
	}


	@Override
	public void updateOrderStatus(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		Map<String, String> params = generateUpdateOrderParams(expressOrder, expressCompany);
		String jsonResult = request(params, expressCompany.getOrderUrl());
		GuoMaoEMSResponseEntity<List<String>> response = JSONObject.parseObject(jsonResult,new TypeReference<GuoMaoEMSResponseEntity<List<String>>>(){});
		if(!response.isSuccess()){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"updateOrderStatus failed, response = "+jsonResult);
		}
	}
	
	private Map<String, String> generateUpdateOrderParams(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		if(ExpressOrderStatus.fromCode(expressOrder.getStatus()) != ExpressOrderStatus.CANCELLED){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"invaild expressOrder status = "+expressOrder.getStatus());
		}
		//系统级别参数
		Map<String, Object> params = generateGeneralParams(expressCompany);
		params.put("method", "ems.inland.waybill.got.cancellation");
		//接口级别参数
		params.put("txLogisticID", expressOrder.getOrderNo());
		params.put("cancelCode", "28");//撤单原因码13：上门揽收不及时放弃 19：客户自交寄 20：客户转交其他公司 16：已联系客户并确认重复下单 8：超禁限 9：超规格 28：客户取消订单 
		//12：因资费原因放弃 22：客户更改取件地址 23：EMS到达时限慢 24：测试单 25：客户要求到付 26：其他
		String pkEmsSignature = GuoMaoEMSSignHelper.sha256Sign(params, expressCompany.getAppSecret(), UTF8_CHARACTER_SET);
		params.put("sign", pkEmsSignature);
		return convertStringMap(params);
	}

	private String request(Map<String, String> params, String url){
		Map<String,String> heads = new HashMap<String,String>();
		heads.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		heads.put("Accept", "text/xml,text/javascript,text/html");
		return Utils.post(url, params, heads);
	}
	
	private Map<String, String> generateCreateOrderparams(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		if(ExpressPayType.fromCode(expressOrder.getPayType()) != ExpressPayType.OFFLINE){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"invaild params pay type = "+expressOrder.getPayType());
		}
		//系统级别参数
		Map<String, Object> params = generateGeneralParams(expressCompany);
		params.put("method", "ems.inland.waybill.got");
		//接口级别参数
		params.put("gotInfo", getSubParams(expressOrder));
		String pkEmsSignature = GuoMaoEMSSignHelper.sha256Sign(params, expressCompany.getAppSecret(), UTF8_CHARACTER_SET);
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

	@Override
	public void getOrderStatus(ExpressOrder expressOrder, ExpressCompany expressCompany) {
	}

	@Override
	public void orderStatusCallback(ExpressOrder expressOrder, ExpressCompany expressCompany, Map<String,String> params) {
		String sign = GuoMaoEMSSignHelper.sha256Sign(ConvertToObjectMap(params), expressCompany.getAppSecret(), UTF8_CHARACTER_SET);
		if(!sign.equals(params.get("sign"))){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"sign check failed, sign = "+params.get("sign"));
		}
		if(!"ems.inland.waybill.got.statuspush".equals(params.get("method"))){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"unsupport method = "+params.get("method"));
		}
		String status = params.get("status");
		if(status==null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"unsupport status = "+status);
		}
		if("1".equals(status)){
			String mailNum = params.get("mailNum");
			if(mailNum == null){
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"mailNum = "+mailNum);
			}
			LOGGER.info("update order status = {}, billno = {}", status,mailNum);
			expressOrder.setStatus(ExpressOrderStatus.FINISHED.getCode());
			expressOrder.setBillNo(mailNum);
			expressOrderProvider.updateExpressOrder(expressOrder);
		}
		if(status.toUpperCase().contains("F")){
			expressOrder.setStatus(ExpressOrderStatus.CANCELLED.getCode());
			expressOrder.setStatusDesc(status+params.get("desc"));
			expressOrderProvider.updateExpressOrder(expressOrder);
		}
		
	}

	private Map<String, Object> ConvertToObjectMap(Map<String, String> params) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			objectMap.put(key, params.get(key));
		}
		return objectMap;
	}

}
