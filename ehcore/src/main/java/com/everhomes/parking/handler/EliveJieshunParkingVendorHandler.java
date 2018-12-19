// @formatter:off
package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.parking.clearance.ParkingClearanceLog;
import com.everhomes.parking.elivejieshun.EliveJieShunDataItems;
import com.everhomes.parking.elivejieshun.EliveJieShunLogonReponse;
import com.everhomes.parking.elivejieshun.EliveJieShunSignTools;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.*;
import com.everhomes.rest.parking.clearance.ParkingActualClearanceLogDTO;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 住总停车
 */
// "ELIVE_JIESHUN"需与ParkingLotVendor.ELIVE_JIESHUN的枚举值保持一致
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "ELIVE_JIESHUN")
public class EliveJieshunParkingVendorHandler extends DefaultParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(EliveJieshunParkingVendorHandler.class);

	@Autowired
	public BigCollectionProvider bigCollectionProvider;

	private static final String PARKING_ELIVE_JIESHUN_TICKENT = "PARKING_ELIVE_JIESHUN_TICKENT";//存token
	private static final String PARKING_ELIVE_JIESHUN_RATE = "PARKING_ELIVE_JIESHUN_RATE";//存车辆的费率，免得两次请求一直搞

	private JSONObject transformAttributes(Object attributes){
		return JSONObject.parseObject(String.valueOf(attributes));
	}

	private JSONArray transformSubItems(Object subItems){
		return JSONObject.parseArray(String.valueOf(subItems));
	}

	@Override
	public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
		EliveJieShunLogonReponse cardInfo = getCardInfo(plateNumber);
		List<ParkingCardDTO> resultList = new ArrayList<>();
		if(!isRequestDataItemsSuccess(cardInfo)){
			return resultList;
		}
		ParkingCardDTO parkingCardDTO = new ParkingCardDTO();
		// 格式yyyyMMddHHmmss
		JSONArray jsonArrayitems = transformSubItems(cardInfo.getDataItems());
		if(jsonArrayitems==null || jsonArrayitems.size()==0){
			return resultList;
		}
		JSONArray jsonArray = jsonArrayitems.getJSONObject(0).getJSONArray("subItems");
		if(jsonArray==null || jsonArray.size()==0){
			return resultList;
		}
		for (Object o : jsonArray) {
			JSONObject jsonObject = (JSONObject)o;
			JSONObject attributes = jsonObject.getJSONObject("attributes");
			if(!transformPlateNumberToThirdpartFormat(plateNumber).equals(attributes.getString("carNo"))) {
				continue;
			}
			String validEnd=attributes.getString("endTime");
			Long endTime = Utils.strToLong(validEnd, Utils.DateStyle.DATE);
			parkingCardDTO.setPlateNumber(plateNumber);// 车牌号

			setCardStatus(parkingLot, endTime, parkingCardDTO);// 这里设置过期可用，正常可用
			parkingCardDTO.setEndTime(endTime);

			parkingCardDTO.setOwnerType(parkingLot.getOwnerType());
			parkingCardDTO.setOwnerId(parkingLot.getOwnerId());
			parkingCardDTO.setParkingLotId(parkingLot.getId());

			JSONObject ownerAttributes = jsonArrayitems.getJSONObject(0).getJSONObject("attributes");
			if(ownerAttributes!=null) {
				parkingCardDTO.setPlateOwnerName(ownerAttributes.getString("personName"));// 车主名称
			}

			parkingCardDTO.setCardTypeId(attributes.getString("cardType"));
			parkingCardDTO.setCardType(attributes.getString("cardType"));
			parkingCardDTO.setCardNumber(attributes.getString("cardId"));
			resultList.add(parkingCardDTO);
			saveRateToRedis(plateNumber,attributes.toJSONString());
			return resultList;
		}
		return resultList;
	}

	private boolean isRequestDataItemsSuccess(EliveJieShunLogonReponse cardInfo) {
		return cardInfo != null && cardInfo.isSuccess() && cardInfo.getDataItems()!=null;
	}

	private boolean isRequestSuccess(EliveJieShunLogonReponse delayMonthCardResult) {
		return delayMonthCardResult != null && delayMonthCardResult.isSuccess();
	}

	private boolean isErrorToken(EliveJieShunLogonReponse cardInfo) {
		return cardInfo != null && cardInfo.isErrorToken();
	}

	private String transformPlateNumberToThirdpartFormat(String plateNumber){
		return plateNumber==null || plateNumber.length()<2?plateNumber:(plateNumber.substring(0,1)+'-'+plateNumber.substring(1));
	}

	private String transformPlateNumberToOurFormat(String plateNumber){
		return plateNumber==null?plateNumber:plateNumber.replace("-","");
	}

	private EliveJieShunLogonReponse getCardInfo(String plateNumber) {
		String areaCode = configProvider.getValue("parking.elivejieshun.areaCode", "");
//		String areaCode = "0000006666";

		JSONObject jsonObject=new JSONObject();
		jsonObject.put("requestType","DATA");
		jsonObject.put("serviceId","3c.base.querypersonsbycar");
		JSONObject attributes=new JSONObject();
		attributes.put("areaCode",areaCode);
		attributes.put("carNo",transformPlateNumberToThirdpartFormat(plateNumber));
		jsonObject.put("attributes",attributes);
		String result = post(jsonObject.toJSONString());
		EliveJieShunLogonReponse response =
				JSONObject.parseObject(result, new TypeReference
						<EliveJieShunLogonReponse>() {
				});
//		if(!isRequestMonthCardSuccess(response) && isErrorToken(response)){
//			result = postRefreshToken(plateNumber);
//			response = JSONObject.parseObject(result, new TypeReference
//				<EliveJieShunLogonReponse<String,List<EliveJieShunDataItems<String,String,String>>>>() {});
//		}
		return response;
	}

//	private String postRefreshToken(String jsonString) {
//		return post(jsonString,true);
//	}
//	private String post(String jsonString){
//		return post(jsonString,false);
//	}

	private String post(String jsonString) {
		String funurl = configProvider.getValue("parking.elivejieshun.funurl", "");
		String v = configProvider.getValue("parking.elivejieshun.v", "");
		String cid = configProvider.getValue("parking.elivejieshun.cid", "");
		String signKey = configProvider.getValue("parking.elivejieshun.signKey", "");

//		String funurl = "http://112.74.43.203/jsaims/as";
//		String v =  "2";
//		String cid =  "jsdstest";

		Map<String,Object> map = new HashMap();
		map.put("cid",cid);
		map.put("v",v);
		map.put("tn", getToken(false));
		map.put("sn", EliveJieShunSignTools.getSn(jsonString+signKey));
		map.put("p",URLEncoder.encode(jsonString));
		StringBuffer urlbuffer = new StringBuffer(funurl).append("?");
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			urlbuffer.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
		}
		String result = Utils.get((Map<String, Object>) null,urlbuffer.substring(0,urlbuffer.length()-1).toString());
		JSONObject jsonObject = JSONObject.parseObject(result);
		if(jsonObject!=null && jsonObject.getInteger("resultCode")==6){
			map.put("tn", getToken(true));
			StringBuffer urlbuffer2 = new StringBuffer(funurl).append("?");
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				urlbuffer2.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
			}
			result = Utils.get((Map<String, Object>) null,urlbuffer2.substring(0,urlbuffer2.length()-1).toString());
			jsonObject = JSONObject.parseObject(result);
		}
		boolean flag = configProvider.getBooleanValue("parking.elive.debug", false);
		if(flag){
			map.put("tn", getToken(true));
			StringBuffer urlbuffer2 = new StringBuffer(funurl).append("?");
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				urlbuffer2.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
			}
			result = Utils.get((Map<String, Object>) null,urlbuffer2.substring(0,urlbuffer2.length()-1).toString());
			jsonObject = JSONObject.parseObject(result);
		}
//		if(jsonObject!=null && jsonObject.getInteger("resultCode")!=0){
//			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_GET_TOKEN,
//					jsonObject.getInteger("resultCode")+','+jsonObject.getString("message"));
//		}
		return result;
	}


	@Override
	public ParkingExpiredRechargeInfoDTO getExpiredRechargeInfo(ParkingLot parkingLot, GetExpiredRechargeInfoCommand cmd) {
		List<ParkingCardDTO> parkingCardLists = listParkingCardsByPlate(parkingLot, cmd.getPlateNumber());
		if(parkingCardLists==null || parkingCardLists.size()==0){
			return null;
		}
		ParkingCardDTO cardInfo = parkingCardLists.get(0);
		if (cardInfo == null) {
			return null;
		}
		List<ParkingRechargeRateDTO> parkingRechargeRates = getParkingRechargeRates(parkingLot, null, null);
		if(parkingRechargeRates==null || parkingRechargeRates.size()==0){
			return null;
		}

		ParkingRechargeRateDTO targetRateDTO = null;
		String cardTypeId = cardInfo.getCardTypeId();
		for (ParkingRechargeRateDTO rateDTO : parkingRechargeRates) {
			if (rateDTO.getCardTypeId().equals(cardTypeId) && rateDTO.getMonthCount().intValue()==parkingLot.getExpiredRechargeMonthCount()) {
				targetRateDTO = rateDTO;
				break;
			}
		}

		if (null == targetRateDTO) {
			parkingRechargeRates.sort((r1,r2)->r1.getMonthCount().compareTo(r2.getMonthCount()));
			for (ParkingRechargeRateDTO rateDTO : parkingRechargeRates) {
				if (rateDTO.getCardTypeId().equals(cardTypeId)) {
					targetRateDTO = rateDTO;
					break;
				}
			}
		}
		if (null == targetRateDTO) {
			return null;
		}

		ParkingExpiredRechargeInfoDTO dto = ConvertHelper.convert(targetRateDTO,ParkingExpiredRechargeInfoDTO.class);
		dto.setCardTypeName(targetRateDTO.getCardType());

		if (cardInfo != null  && cardInfo.getEndTime() != null) {
			Integer requestMonthCount = REQUEST_MONTH_COUNT;
			Byte requestRechargeType = REQUEST_RECHARGE_TYPE;

			if(null != parkingLot) {
				requestMonthCount = parkingLot.getExpiredRechargeMonthCount();
				requestRechargeType = parkingLot.getExpiredRechargeType();
			}
			long newStartTime = cardInfo.getEndTime();
			long now = System.currentTimeMillis();
			if(now>newStartTime){
				newStartTime = now;
			}
			if(requestRechargeType == ParkingCardExpiredRechargeType.ACTUAL.getCode()){
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				int today = calendar.get(Calendar.DAY_OF_MONTH);
				BigDecimal price = dto.getPrice().multiply(new BigDecimal(requestMonthCount-1))
						.add(dto.getPrice().multiply(new BigDecimal(maxDay-today+1))
								.divide(new BigDecimal(maxDay), OPEN_CARD_RETAIN_DECIMAL, RoundingMode.HALF_UP));
				dto.setPrice(price);
			}else{
				dto.setPrice(targetRateDTO.getPrice().divide(targetRateDTO.getMonthCount(),OPEN_CARD_RETAIN_DECIMAL, RoundingMode.HALF_UP)
						.multiply(new BigDecimal(parkingLot.getExpiredRechargeMonthCount())));
			}
			dto.setStartPeriod(newStartTime);
			Timestamp rechargeEndTimestamp = Utils.getTimestampByAddNatureMonth(newStartTime, parkingLot.getExpiredRechargeMonthCount());
			dto.setEndPeriod(rechargeEndTimestamp.getTime());
			dto.setMonthCount(new BigDecimal(parkingLot.getExpiredRechargeMonthCount()));
			dto.setRateName(parkingLot.getExpiredRechargeMonthCount()+configProvider.getValue("parking.default.rateName","个月"));

		}
		return dto;
	}

	@Override
	public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot, String plateNumber, String cardNo) {
		if(StringUtils.isEmpty(plateNumber)){
			return null;
		}
		List<ParkingRechargeRateDTO> rateByPlateNumber = getRateByPlateNumberFromRedis(parkingLot,plateNumber);
		if(rateByPlateNumber==null){
			return getRateByPlateNumberFromThird(parkingLot,plateNumber);
		}
		return rateByPlateNumber;
	}

	@Override
	public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
		if (order.getOrderType().equals(ParkingOrderType.RECHARGE.getCode())) {
			if(order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode())) {
				return rechargeMonthlyCard(order);
			}else if(order.getRechargeType().equals(ParkingRechargeType.TEMPORARY.getCode())) {
				return payTempCardFee(order);
			}
		} else if(order.getOrderType().equals(ParkingOrderType.OPEN_CARD.getCode())) {
//			return openMonthCard(order);
		}
		LOGGER.info("unknown type = " + order.getRechargeType());
		return false;
	}

	private Boolean payTempCardFee(ParkingRechargeOrder order) {
		String parkCode = configProvider.getValue("parking.elivejieshun.parkCode", "");

		JSONObject params=new JSONObject();
		params.put("requestType","DATA");
		params.put("serviceId","3c.pay.notifyorderresult");
		JSONObject paramattrs=new JSONObject();
		paramattrs.put("orderNo",order.getOrderToken());
		paramattrs.put("tradeStatus",0);
		paramattrs.put("isCallBack",0);
		VendorType type = VendorType.fromCode(order.getPaidType());
		paramattrs.put("payType",type==VendorType.WEI_XIN?"WX":"ZFB");
		paramattrs.put("payTypeName",type.getDescribe());
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.DateStyle.DATE_TIME);
		paramattrs.put("payTime",sdf.format(order.getPaidTime()));
		params.put("attributes",paramattrs);
		String result = post(params.toJSONString());
		EliveJieShunLogonReponse response =
				JSONObject.parseObject(result, new TypeReference
						<EliveJieShunLogonReponse>() {
				});

		order.setErrorDescriptionJson(result);
		order.setErrorDescription(response!=null?response.getMessage():null);
		return isRequestSuccess(response);
	}

	private Boolean rechargeMonthlyCard(ParkingRechargeOrder order) {
		EliveJieShunLogonReponse cardInfo
				= getCardInfo(order.getPlateNumber());
		if(!isRequestDataItemsSuccess(cardInfo)){
			return false;
		}
		JSONArray jsonArrayitems = transformSubItems(cardInfo.getDataItems());
		if(jsonArrayitems==null || jsonArrayitems.size()==0){
			return false;
		}
		JSONArray jsonArray = jsonArrayitems.getJSONObject(0).getJSONArray("subItems");
		if(jsonArray==null || jsonArray.size()==0){
			return false;
		}
		for (Object o : jsonArray) {
			JSONObject jsonObject = (JSONObject) o;
			JSONObject attributes = jsonObject.getJSONObject("attributes");
			if (!transformPlateNumberToThirdpartFormat(order.getPlateNumber()).equals(attributes.getString("carNo"))) {
				continue;
			}
			String validEnd = attributes.getString("endTime");

			Timestamp timestampStart = new Timestamp(Utils.strToLong(validEnd, Utils.DateStyle.DATE));
			Long now = System.currentTimeMillis();
			if(now>timestampStart.getTime()){
				timestampStart = new Timestamp(now);
			}
			Timestamp timestampEnd = Utils.getTimestampByAddNatureMonth(timestampStart.getTime(), order.getMonthCount().intValue());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			String newStart = sdf.format(timestampStart);
			String newEnd = sdf.format(timestampEnd);

			String parkCode = configProvider.getValue("parking.elivejieshun.parkCode", "");

			JSONObject params=new JSONObject();
			params.put("requestType","DATA");
			params.put("serviceId","3c.card.carddelay");
			JSONObject paramattrs=new JSONObject();
			paramattrs.put("parkCode",parkCode);
			paramattrs.put("month",order.getMonthCount().intValue());
			paramattrs.put("money",order.getPrice().toString());
			paramattrs.put("newBeginDate",newStart);
			paramattrs.put("newEndDate",newEnd);
			paramattrs.put("cardId",attributes.getString("cardId"));
			params.put("attributes",paramattrs);
			String result = post(params.toJSONString());
			EliveJieShunLogonReponse response =
					JSONObject.parseObject(result, new TypeReference
							<EliveJieShunLogonReponse>() {
					});
//			if(!isRequestDelayMonthCardSuccess(response) && isErrorToken(response)){
//				result = postRefreshToken(params.toJSONString());
//				response = JSONObject.parseObject(result, new TypeReference
//								<EliveJieShunLogonReponse>() {
//						});
//			}
			//将充值信息存入订单
			order.setErrorDescriptionJson(result);
			order.setStartPeriod(timestampStart);
			order.setEndPeriod(timestampEnd);
			order.setErrorDescription(response!=null?response.getMessage():null);

			return isRequestSuccess(response);
		}
		return false;
	}


	@Override
	public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
		return null;
	}

	@Override
	public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {
		order.setOriginalPrice(order.getPrice());
	}

//	/**
//	 *
//	 * @return 强制刷新从第三方获取token，并缓存到redis，返回token
//	 */
//	private String getRefreshedToken(){
//		return getToken(true);
//	}
//
//	/**
//	 *
//	 * @return 从redis获取token，存在获取到过期的token失败的请，请调用者判断调用 #getRefreshedToken
//	 */
//	private String getToken(){
//		return getToken(false);
//	}

	/**
	 *
	 * @param parkingLot 停车场信息
	 * @param plateNumber 我方格式的车牌(没有'-')
	 * @return 费率信息是从第三方请求,此车牌的费率信息
	 */
	private List<ParkingRechargeRateDTO> getRateByPlateNumberFromThird(ParkingLot parkingLot,String plateNumber) {
		return getRateByPlateNumber(parkingLot,plateNumber,true);
	}

	/**
	 * @param parkingLot 停车场信息
	 * @param plateNumber 我方格式的车牌(没有'-')
	 * @return 费率信息是从redis请求,此车牌的费率信息
	 */
	private List<ParkingRechargeRateDTO> getRateByPlateNumberFromRedis(ParkingLot parkingLot,String plateNumber) {
		return getRateByPlateNumber(parkingLot,plateNumber,false);
	}

	/**
	 *
	 * @param parkingLot 停车场信息
	 * @param plateNumber 我方格式的车牌(没有'-')
	 * @param isRequestFromThird 费率信息是否从第三方请求（获取月卡的时候已经将费率存在redis，如果redis存储已经失效，可以从第三方获取）
	 * @return 此车牌的费率信息
	 */
	private List<ParkingRechargeRateDTO> getRateByPlateNumber(ParkingLot parkingLot,String plateNumber,boolean isRequestFromThird) {
		Object o = null;
		if(isRequestFromThird){
			EliveJieShunLogonReponse cardInfo = getCardInfo(plateNumber);
			if(!isRequestDataItemsSuccess(cardInfo)){
				return null;
			}
			JSONArray jsonArrayitems = JSONObject.parseArray(String.valueOf(cardInfo.getDataItems()));
			JSONArray jsonArray = jsonArrayitems.getJSONObject(0).getJSONArray("subItems");
//			String subItems = cardInfo.getDataItems().get(0).getSubItems();
//			JSONArray jsonArray = JSONObject.parseArray(subItems);
			if(jsonArray==null || jsonArray.size()==0){
				return null;
			}

			for (Object item : jsonArray) {
				JSONObject jsonObject = (JSONObject) item;
				JSONObject attributes = jsonObject.getJSONObject("attributes");
				if (!transformPlateNumberToThirdpartFormat(plateNumber).equals(attributes.getString("carNo"))) {
					continue;
				}
				o=attributes;
				break;
			}
		}else {
			String rediskey = UserContext.getCurrentNamespaceId() + PARKING_ELIVE_JIESHUN_RATE + plateNumber;
			Accessor acc = this.bigCollectionProvider.getMapAccessor(rediskey, "");
			final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
			RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
			o = redisTemplate.opsForValue().get(rediskey);
			if (o == null) {
				return null;
			}
		}
		JSONObject attr = JSONObject.parseObject(o.toString());
		JSONObject pkg=	JSONObject.parseObject(attr.getString("package"));
		List<ParkingRechargeRateDTO> list = new ArrayList();
		pkg.forEach((key,value)->{
			ParkingRechargeRateDTO dto = new ParkingRechargeRateDTO();
			dto.setPrice(new BigDecimal(value.toString()));
			dto.setMonthCount(new BigDecimal(key));
			dto.setRateName(dto.getMonthCount()+"个月");
			dto.setParkingLotId(parkingLot.getId());
			dto.setCardType(attr.getString("cardType"));
			dto.setCardTypeId(attr.getString("cardType"));
			dto.setOwnerType(parkingLot.getOwnerType());
			dto.setOwnerId(parkingLot.getOwnerId());
			list.add(dto);
		});
		if(isRequestFromThird){
			saveRateToRedis(plateNumber,attr.toJSONString());
		}
		return list;
	}

	/**
	 * 存储费率信息到redis
	 *
	 * @param plateNumber 我方格式的车牌(没有'-')
	 * @param attributes 费率信息
	 */
	private void saveRateToRedis(String plateNumber,String attributes){
		String rediskey = UserContext.getCurrentNamespaceId()+PARKING_ELIVE_JIESHUN_RATE+plateNumber;
		Accessor acc = this.bigCollectionProvider.getMapAccessor(rediskey, "");
		final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
		Integer lifecycle = configProvider.getIntValue("parking.elivejieshun.ratelifecycle", 60);
		redisTemplate.opsForValue().set(rediskey, attributes,lifecycle, TimeUnit.SECONDS);
	}

	/**
	 *
	 * @param refreshFlag 是否从第三方获取token,并缓存到redis
	 * @return token
	 */
	private String getToken(boolean refreshFlag){
		String rediskey =UserContext.getCurrentNamespaceId()+PARKING_ELIVE_JIESHUN_TICKENT;
		Accessor acc = this.bigCollectionProvider.getMapAccessor(rediskey, "");
		final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
		Integer lifecycle = configProvider.getIntValue("parking.elivejieshun.tokenlifecycle", 6500);
		if(refreshFlag) {
			String token = requestToken();
			redisTemplate.opsForValue().set(rediskey, token.toString(),lifecycle, TimeUnit.SECONDS);
			return token;
		}
		Object redisToken = redisTemplate.opsForValue().get(rediskey);
		if(redisToken != null) {
			return redisToken.toString();
		}
		String token = requestToken();
		if(token == null) {
			return null;
		}
		//这里存储6500秒，因为你大爷的，对面只给两个小时有效token时间
		redisTemplate.opsForValue().set(rediskey, token.toString(),lifecycle, TimeUnit.SECONDS);
		return token;


	}

	/**
	 *
	 * @return 从第三方获取token
	 */
	private String requestToken() {
		String loginurl = configProvider.getValue("parking.elivejieshun.loginurl", "");
		String cid = configProvider.getValue("parking.elivejieshun.cid", "");
		String usr = configProvider.getValue("parking.elivejieshun.usr", "");
		String psw = configProvider.getValue("parking.elivejieshun.psw", "");

//		String loginurl = "http://112.74.43.203/jsaims/login";
//		String cid = "jsdstest";
//		String usr = "jsdstest";
//		String psw = "888888";

		Map<String,Object> map = new HashMap();
		map.put("cid",cid);
		map.put("usr",usr);
		map.put("psw",psw);

		int i = 0;
		for (;;) {
			String result = Utils.get(map, loginurl);
			EliveJieShunLogonReponse response = JSONObject.parseObject(result, new TypeReference<EliveJieShunLogonReponse<?,?>>() {
			});
			if (!response.isSuccess()) {
				i++;
				LOGGER.error("get token from elive failed, try again times {}",i);
				if(i>2) {
					throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_GET_TOKEN,
							"获取token失败");
				}
				continue;
			}
			return response.getToken();
		}
	}

	@Override
	public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {
		/**
		 *
		 {
		 "serviceId":"3c.pay.querycarbycarno",
		 "requestType":"DATA",
		 "attributes":{
		 "parkCode": "g3v3_1",
		 "carNo": "粤B-211222"
		 }
		 }

		 {
		 "serviceId":"3c.pay.createorderbycarno",
		 "requestType":"DATA",
		 "attributes":{
		 "businesserCode": "test",
		 "parkCode": "g3v3_1",
		 "orderType": "VNP",
		 "carNo": "粤B-211222"
		 }
		 }
		 */
//		String parkCode = configProvider.getValue("parking.elivejieshun.parkCode", "");
//
//		JSONObject params=new JSONObject();
//		params.put("requestType","DATA");
//		params.put("serviceId","3c.pay.querycarbycarno");
//		JSONObject paramattrs=new JSONObject();
//		paramattrs.put("parkCode",parkCode);
//		paramattrs.put("carNo",transformPlateNumberToThirdpartFormat(plateNumber));
//		params.put("attributes",paramattrs);
//		String result = post(params.toJSONString());
//		EliveJieShunLogonReponse<String,List<EliveJieShunDataItems<String,String,String>>> response =
//				JSONObject.parseObject(result, new TypeReference
//						<EliveJieShunLogonReponse<String,List<EliveJieShunDataItems<String,String,String>>>>() {
//				});
//		if(isRequestDelayMonthCardSuccess(response)){
//			return null;
//		}

		String parkCode = configProvider.getValue("parking.elivejieshun.parkCode", "");
		String businesserCode = configProvider.getValue("parking.elivejieshun.businesserCode", "");

		JSONObject params=new JSONObject();
		params.put("requestType","DATA");
		params.put("serviceId","3c.pay.createorderbycarno");
		JSONObject paramattrs=new JSONObject();
		paramattrs.put("businesserCode",businesserCode);
		paramattrs.put("parkCode",parkCode);
		paramattrs.put("orderType","VNP");
		paramattrs.put("carNo",transformPlateNumberToThirdpartFormat(plateNumber));
		params.put("attributes",paramattrs);
		String result = post(params.toJSONString());
		EliveJieShunLogonReponse response =
				JSONObject.parseObject(result, new TypeReference
						<EliveJieShunLogonReponse>() {
				});
		ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
		if(!isRequestSuccess(response)){
			return dto;
		}

		JSONArray jsonArrayitems = JSONObject.parseArray(String.valueOf(response.getDataItems()));
		JSONObject jsonAttributes = jsonArrayitems.getJSONObject(0).getJSONObject("attributes");

//		String attributes = response.getDataItems().get(0).getAttributes();
//		JSONObject jsonAttributes = JSONObject.parseObject(attributes);
		if(jsonAttributes==null || jsonAttributes.size()==0 || 0!=jsonAttributes.getInteger("retcode")){
			return dto;
		}

		dto.setPrice(new BigDecimal(jsonAttributes.getString("serviceFee")));
		dto.setRemainingTime(jsonAttributes.getInteger("surplusMinute"));
		dto.setPlateNumber(plateNumber);
		dto.setEntryTime(Utils.strToLong(jsonAttributes.getString("startTime"),Utils.DateStyle.DATE_TIME));
		dto.setPayTime(Utils.strToLong(jsonAttributes.getString("endTime"),Utils.DateStyle.DATE_TIME));
		dto.setParkingTime(jsonAttributes.getInteger("serviceTime")/60);
		dto.setDelayTime(jsonAttributes.getInteger("freeMinute"));
		//feestate	缴费状态	int	0=无数据;2=免缴费;3=已缴费未超时;4=已缴费需续费;5=需要缴费
		//这里需要干嘛，缴费完成，回调的时候，读取这个token，向停车场缴费需要这个参数。参考 payTempCardFee()
		dto.setOrderToken(jsonAttributes.getString("orderNo"));
//		dto.setRemainingTime(tempCard.getTimeOut());
		return dto;


	}

	@Override
	public ParkingFreeSpaceNumDTO getFreeSpaceNum(GetFreeSpaceNumCommand cmd) {
		/**
		 {
		 "serviceId":"3c.park.queryparkspace",
		 "requestType":"DATA",
		 "attributes":{
		 "parkCodes": "g3v3_1"
		 }
		 }
		 */
		String parkCode = configProvider.getValue("parking.elivejieshun.parkCode", "");

		JSONObject params=new JSONObject();
		params.put("requestType","DATA");
		params.put("serviceId","3c.park.queryparkspace");
		JSONObject paramattrs=new JSONObject();
		paramattrs.put("parkCodes",parkCode);
		params.put("attributes",paramattrs);
		String result = post(params.toJSONString());
		EliveJieShunLogonReponse response =
				JSONObject.parseObject(result, new TypeReference
						<EliveJieShunLogonReponse>() {
				});
		if(!isRequestSuccess(response) || response.getDataItems()==null){
			return null;
		}
		JSONArray array = JSONObject.parseArray(String.valueOf(response.getDataItems()));
		if(array==null || array.size()<1){
			return null;
		}
		JSONObject jsonObject =array.getJSONObject(0);
		ParkingFreeSpaceNumDTO dto = ConvertHelper.convert(cmd,ParkingFreeSpaceNumDTO.class);
		dto.setFreeSpaceNum(jsonObject.getJSONObject("attributes").getInteger("restSpace"));
		return dto;
	}

	@Override
	public List<ParkingActualClearanceLogDTO> getTempCardLogs(ParkingClearanceLog r) {
//		String areaCode = configProvider.getValue("parking.elivejieshun.areaCode", "");
//		String personCode = configProvider.getValue("parking.elivejieshun.personCode", "");
//		String parkCode = configProvider.getValue("parking.elivejieshun.parkCode", "");

		EliveJieShunLogonReponse inRecordResponse = queryParkInRecord(r);
		if(isOutedParkingLot(inRecordResponse)){
			EliveJieShunLogonReponse outRecordResponse = queryParkOutRecord(r);
			return generateClearanceLogList(inRecordResponse,outRecordResponse);
		}
		return generateClearanceLogList(inRecordResponse,null);
	}

	private List<ParkingActualClearanceLogDTO> generateClearanceLogList(EliveJieShunLogonReponse inRecordResponse,
																		EliveJieShunLogonReponse outRecordResponse) {
		if(!isRequestDataItemsSuccess(inRecordResponse)){
			return null;
		}
		List<ParkingActualClearanceLogDTO> dtos = new ArrayList<>();

		JSONArray dataItems = transformSubItems(inRecordResponse.getDataItems());
		for (Object dataItem : dataItems) {
			ParkingActualClearanceLogDTO dto = new ParkingActualClearanceLogDTO();
			JSONObject jsonDataItem = JSONObject.parseObject(String.valueOf(dataItem));
			String inTime = jsonDataItem.getJSONObject("attributes").getString("inTime");
			dto.setEntryTime(Utils.strToTimeStamp(inTime,Utils.DateStyle.DATE_TIME));
			dtos.add(dto);
		}
		if(!isRequestDataItemsSuccess(outRecordResponse)){
			return dtos;
		}
		int i = 0;
		JSONArray dataItemsOut = JSONObject.parseArray(String.valueOf(outRecordResponse.getDataItems()));
		for (Object dataItem :dataItemsOut) {
			JSONObject jsonDataItem = JSONObject.parseObject(String.valueOf(dataItem));
			String outTime = jsonDataItem.getJSONObject("attributes").getString("outTime");
			ParkingActualClearanceLogDTO dto = dtos.get(i++);
			if(dto==null){
				dto=new ParkingActualClearanceLogDTO();
			}
			dto.setExitTime(Utils.strToTimeStamp(outTime,Utils.DateStyle.DATE_TIME));
		}
		return dtos;
	}

	private boolean isOutedParkingLot(EliveJieShunLogonReponse inRecordResponse) {
		if(!isRequestDataItemsSuccess(inRecordResponse)){
			return false;
		}
		JSONArray dateItems = JSONObject.parseArray(String.valueOf(inRecordResponse.getDataItems()));
		if(dateItems==null||dateItems.size()==0){
			return false;
		}
		JSONObject attr = JSONObject.parseObject(String.valueOf(dateItems.get(0)));
		Integer isout = attr.getJSONObject("attributes").getInteger("isOut");
		if(isout==null){
			return false;
		}
		return isout==1;
	}

	private EliveJieShunLogonReponse queryParkOutRecord(ParkingClearanceLog r){
		return queryParkRecord(r,"3c.park.queryparkout");
	}

	private EliveJieShunLogonReponse queryParkInRecord(ParkingClearanceLog r){
		return queryParkRecord(r,"3c.park.queryparkinrecord");
	}

	private EliveJieShunLogonReponse queryParkRecord(ParkingClearanceLog r, String serviceId){
		String parkCode = configProvider.getValue("parking.elivejieshun.parkCode", "");

		JSONObject params=new JSONObject();
		params.put("requestType","DATA");
		params.put("serviceId",serviceId);

		JSONObject paramattrs=new JSONObject();
		paramattrs.put("parkCode",parkCode);
		paramattrs.put("carNo",transformPlateNumberToThirdpartFormat(r.getPlateNumber()));
//		paramattrs.put("carNo","粤-Y22222");
		if("3c.park.queryparkinrecord".equals(serviceId)) {
			paramattrs.put("beginTime", generateClearanceStartTime(r.getClearanceTime()));
			paramattrs.put("endTime", generateClearanceEndTime(r.getClearanceTime()));
		}else if("3c.park.queryparkout".equals(serviceId)) {
			paramattrs.put("beginDate", generateClearanceStartTime(r.getClearanceTime()));
			paramattrs.put("endDate", generateClearanceEndTime(r.getClearanceTime()));
		}
		paramattrs.put("pageIndex",1);
		paramattrs.put("pageSize",100);
		params.put("attributes",paramattrs);

		String result = post(params.toJSONString());

		EliveJieShunLogonReponse response =
				JSONObject.parseObject(result, new TypeReference
						<EliveJieShunLogonReponse>() {
				});
		return response;
	}

	private String getInviteLog(ParkingClearanceLog r){
		String areaCode = configProvider.getValue("parking.elivejieshun.areaCode", "");
		String personCode = configProvider.getValue("parking.elivejieshun.personCode", "");
		String businesserCode = configProvider.getValue("parking.elivejieshun.businesserCode", "");

		JSONObject params=new JSONObject();
		params.put("requestType","DATA");
		params.put("serviceId","3c.visitor.queryhistroyinvite");

		JSONObject paramattrs=new JSONObject();
//		paramattrs.put("visitorId",r.getLogToken());
		paramattrs.put("visitorType",3);
		paramattrs.put("personCode",personCode);
		paramattrs.put("areaCode",areaCode);
		paramattrs.put("beginTime",generateClearanceStartTime(r.getClearanceTime()));
		paramattrs.put("endTime",generateClearanceEndTime(r.getClearanceTime()));
		paramattrs.put("pageIndex",1);
		paramattrs.put("pageSize",10);
		params.put("attributes",paramattrs);

		String result = post(params.toJSONString());
		return  result;
	}
	DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	@Override
	public String applyTempCard(ParkingClearanceLog log) {

		String result = post(generateTempCardParams(log).toJSONString());

		EliveJieShunLogonReponse response =
				JSONObject.parseObject(result, new TypeReference
						<EliveJieShunLogonReponse>() {
				});
		if(!isRequestSuccess(response)){
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_INVITE_FAILD,
					"发起访邀失败,"+response.getMessage());
		}
//		JSONObject attrubiter = JSONObject.parseObject(response.getAttributes());
//		return attrubiter.getString("visitorId");
		return String.valueOf(response);
	}

	private JSONObject generateTempCardParams(ParkingClearanceLog log) {
		String areaCode = configProvider.getValue("parking.elivejieshun.areaCode", "");
		String personCode = configProvider.getValue("parking.elivejieshun.personCode", "");
		String businesserCode = configProvider.getValue("parking.elivejieshun.businesserCode", "");

		JSONObject params=new JSONObject();
		params.put("requestType","DATA");
		params.put("serviceId","3c.visitor.invite");

		JSONObject paramattrs=new JSONObject();
		paramattrs.put("areaCode",areaCode);
		paramattrs.put("personCode",personCode);
		paramattrs.put("businesserCode",businesserCode);
		paramattrs.put("visitorType",3);

		JSONObject timeDesc = new JSONObject();
		timeDesc.put("sd", generateClearanceStartTime(log.getClearanceTime()));
		timeDesc.put("ed", generateClearanceEndTime(log.getClearanceTime()));
//		timeDesc.put("st", null);
//		timeDesc.put("et", null);
		timeDesc.put("w", "0000000");
		paramattrs.put("timeDesc",timeDesc.toJSONString());

		JSONArray dataItems = new JSONArray();
		JSONObject items = new JSONObject();
		JSONObject itemsAttr = new JSONObject();
		itemsAttr.put("visitorName","TEST");
		itemsAttr.put("visitorTel","TEST");
		itemsAttr.put("carNo",transformPlateNumberToThirdpartFormat(log.getPlateNumber()));
		items.put("attributes",itemsAttr);
		dataItems.add(items);
		params.put("dataItems",dataItems);

		params.put("attributes",paramattrs);
		return params;
	}

	private String generateClearanceStartTime(Timestamp clearanceTime) {
		LocalDateTime start = clearanceTime.toLocalDateTime();
		return start.format(dtf2)+" 00:00:00";
//		return "2018-03-24 00:00:00";
	}

	private String generateClearanceEndTime(Timestamp clearanceTime) {
		LocalDateTime end = clearanceTime.toLocalDateTime();
		return end.format(dtf2)+" 23:59:59";
//		return "2018-04-23 00:00:00";
	}
	@Override
	public void refreshToken() {
		this.getToken(true);
	}
}
