// @formatter:off
package com.everhomes.parking.handler.jieshun;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.handler.DefaultParkingVendorHandler;
import com.everhomes.parking.handler.Utils;
import com.everhomes.parking.jieshun.JieShunResponse;
import com.everhomes.rest.parking.GetExpiredRechargeInfoCommand;
import com.everhomes.rest.parking.ListCardTypeCommand;
import com.everhomes.rest.parking.ListCardTypeResponse;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingCardExpiredRechargeType;
import com.everhomes.rest.parking.ParkingCardType;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.parking.ParkingExpiredRechargeInfoDTO;
import com.everhomes.rest.parking.ParkingOrderType;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import com.everhomes.rest.parking.ParkingRechargeType;
import com.everhomes.rest.parking.ParkingTempFeeDTO;
import com.everhomes.rest.parking.jieshun.CardType;
import com.everhomes.rest.parking.jieshun.ErrorCodeEnum;
import com.everhomes.rest.parking.jieshun.FeeItem;
import com.everhomes.rest.parking.jieshun.MonthCardInfo;
import com.everhomes.rest.parking.jieshun.ServiceIdEnum;
import com.everhomes.rest.parking.jieshun.TempFeeInfo;
import com.everhomes.sms.DateUtil;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

/**
 * @Author 明波[mingbo.huang@zuolin.com]
 * @Date 2018/09/05 产品功能 #36688 停车缴费V6.6.3（捷顺停车系统对接）
 */
public abstract class JieShunVendorHandler extends DefaultParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(JieShunVendorHandler.class);
	
	private final TimeUnit TOKEN_OUTDATE_TIMEUNIT = TimeUnit.MINUTES;
	private final TimeUnit  CARD_TYPE_OUTDATE_TIMEUNIT = TimeUnit.SECONDS;

	@Autowired
	BigCollectionProvider bigCollectionProvider;

	@Autowired
	ConfigurationProvider configurationProvider;

	@Autowired
	ParkingProvider parkingProvider;

	public abstract String getParkingVendorName();

	@Override
	public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {

		MonthCardInfo cardInfo = getCardInfo(plateNumber);
		List<ParkingCardDTO> resultList = new ArrayList<>();
		if (cardInfo != null) {
			ParkingCardDTO parkingCardDTO = new ParkingCardDTO();

			// 格式yyyyMMddHHmmss
			Long endTime = getEndTimeStampByDate(cardInfo.getEndDate());
			setCardStatus(parkingLot, endTime, parkingCardDTO);// 这里设置过期可用，正常可用

			parkingCardDTO.setOwnerType(parkingLot.getOwnerType());
			parkingCardDTO.setOwnerId(parkingLot.getOwnerId());
			parkingCardDTO.setParkingLotId(parkingLot.getId());

			parkingCardDTO.setPlateOwnerName(cardInfo.getPersonName());// 车主名称
			parkingCardDTO.setPlateNumber(cardInfo.getCarNo());// 车牌号
			parkingCardDTO.setEndTime(endTime);

//            parkingCardDTO.setCardTypeId(cardInfo.getCardId());
			parkingCardDTO.setCardType(cardInfo.getCardType());
			parkingCardDTO.setCardNumber(cardInfo.getPhysicalNo()); // 物理卡 号码
			resultList.add(parkingCardDTO);
		}
		return resultList;
	}

	@Override
	public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot, String plateNumber,
			String cardNo) {

		// 这里直接从捷顺取
		List<CardType> types = getCardTypesByPlateNumber(plateNumber);
		if (CollectionUtils.isEmpty(types)) {
			return new ArrayList<>();
		}

		List<ParkingRechargeRateDTO> dtos = new ArrayList<>(20);
		for (CardType type : types) {
			List<FeeItem> feeItems = type.getFeeItems();
			for (FeeItem feeItem : feeItems) {
				Integer monthPeriod = feeItem.getMonthPeriod();
				String money = feeItem.getMoney();
				dtos.add(convertToRateDTO(type.getCardType(), monthPeriod, money, parkingLot));
			}
		}

		return dtos;
	}

	@Override
	public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {

		List<ParkingCardType> cardTypes = new ArrayList<>(20);
		List<CardType> types = getCardTypesByPlateNumber(null);
		if (!CollectionUtils.isEmpty(types)) {
			types.stream().forEach(r -> {
				ParkingCardType type = new ParkingCardType();
				type.setTypeId(r.getCardType());
				type.setTypeName(r.getCardType());
				cardTypes.add(type);
			});
		}

		ListCardTypeResponse resp = new ListCardTypeResponse();
		resp.setCardTypes(cardTypes);
		return resp;
	}

	@Override
	public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
		if (order.getOrderType().equals(ParkingOrderType.RECHARGE.getCode())) {
			if (order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode())) {
				return rechargeMonthlyCard(order);
			} else if (order.getRechargeType().equals(ParkingRechargeType.TEMPORARY.getCode())) {
				return payTempCardFee(order);
			}
		} else if (order.getOrderType().equals(ParkingOrderType.OPEN_CARD.getCode())) {
			return openMonthCard(order);
		}
		LOGGER.info("unknown type = " + order.getRechargeType());
		return false;
	}
	
    @Override
	public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {
		ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
		TempFeeInfo tempFeeInfo = getParkingTempFee(plateNumber);
		if (null == tempFeeInfo) {
			return dto;
		}

		dto.setPrice(tempFeeInfo.getTotalFee());
		dto.setPlateNumber(plateNumber);
		dto.setEntryTime(tempFeeInfo.getStartTime().getTime());
		long now = System.currentTimeMillis();
		dto.setPayTime(now);
		dto.setParkingTime(tempFeeInfo.getServiceTime()/60);
		dto.setOriginalPrice(tempFeeInfo.getServiceFee());
		dto.setOrderToken(tempFeeInfo.getOrderNo()); 
		dto.setDelayTime(tempFeeInfo.getFreeMinute());// 临时车缴费成功了之后，有多长预留的出场时间？一般15到30分，可以在车场配置的
		return dto;
	}
    
	@Override
	public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {
		
	}
	
	@Override
	public ParkingExpiredRechargeInfoDTO getExpiredRechargeInfo(ParkingLot parkingLot,
			GetExpiredRechargeInfoCommand cmd) {

		// 不支持按天计算
		Integer requestMonthCount = parkingLot.getExpiredRechargeMonthCount() == null ? REQUEST_MONTH_COUNT
				: parkingLot.getExpiredRechargeMonthCount();
		Byte requestRechargeType = parkingLot.getExpiredRechargeType() == null
				? ParkingCardExpiredRechargeType.ALL.getCode()
				: parkingLot.getExpiredRechargeType();
		if (requestRechargeType == ParkingCardExpiredRechargeType.ACTUAL.getCode()) {
			throwError(ParkingErrorCode.ERROR_RECHARGE_BY_DAY_NOT_SUPPORTED, "expired recharge by day not supported");
		}

		// 获取月卡的信息
		MonthCardInfo card = getCardInfo(cmd.getPlateNumber());
		if (null == card) {
			throwError(ParkingErrorCode.ERROR_CARD_INFO_NOT_FOUND, "card info not found");
		}

		// 获取费率信息
		List<CardType> cardTypes = getCardTypesByCardInfo(card);
		if (CollectionUtils.isEmpty(cardTypes)) {
			throwError(ParkingErrorCode.ERROR_CARD_TYPES_NOT_FOUND, "card types not found");
		}

		// 筛选出该月卡的且相应月份的月卡
		FeeItem feeItem = filterCardTypeByMonth(requestMonthCount, card.getCardType(), cardTypes.get(0));
		if (null == feeItem) {
			throwError(ParkingErrorCode.ERROR_CARD_FEE_ITEM_NOT_FOUND, "fee of current card type not found");
		}

		// 获取开始和结束时间
		long newStartTime = getMonthlyRechargeStartTime(card.getEndDate());
		long newEndTime = getMonthlyRechargeEndTime(newStartTime, requestMonthCount);

		// 返回
		return convertToExpiredRechargeInfoDTO(newStartTime, newEndTime, card.getCardType(), feeItem, parkingLot);
	}
	

	private FeeItem filterCardTypeByMonth(Integer expiredRechargeMonthCount, String cardTypeName,
			CardType cardTyp) {
		if (!cardTyp.getCardType().equals(cardTypeName)) {
			return null;
		}

		for (FeeItem feeItem : cardTyp.getFeeItems()) {
			if (feeItem.getMonthPeriod().equals(expiredRechargeMonthCount)) {
				return feeItem;
			}
		}

		return null;
	}
	

	private ParkingExpiredRechargeInfoDTO convertToExpiredRechargeInfoDTO(long newStartTime, long newEndTime,
			String cardType, FeeItem feeItem, ParkingLot parkingLot) {
		ParkingExpiredRechargeInfoDTO dto = new ParkingExpiredRechargeInfoDTO();
		dto.setOwnerId(parkingLot.getOwnerId());
		dto.setOwnerType(parkingLot.getOwnerType());
		dto.setParkingLotId(parkingLot.getId());
		dto.setRateName(feeItem.getMonthPeriod() + "个月");
		dto.setMonthCount(new BigDecimal(feeItem.getMonthPeriod()));
		dto.setPrice(new BigDecimal(feeItem.getMoney()));
		dto.setOriginalPrice(dto.getPrice());
		dto.setCardTypeId(cardType);
		dto.setCardTypeName(cardType);
		dto.setStartPeriod(newStartTime);
		dto.setEndPeriod(newEndTime);
		return dto;
	}

	private TempFeeInfo getParkingTempFee(String plateNumber) {
		Map<String, String> atrributeMap = new HashMap<>();
		atrributeMap.put("carNo", convertToJieShunCarNo(plateNumber));
		JieShunResponse resp = getParkingTempFeePost(atrributeMap);
		if (!isSuccess(resp) || resp.isEmpty()) {
			return null;
		}
		/*  
		 *  ０：正常，正常订单
			１：安装验证失败，前端设备异常
			２：未入场
			５：非临时卡
			６：未设置收费标准，前端停车场异常
			９：已缴费，超时滞留时间内
			１０：正常免费时间段内
			１１：打折免费时间段内
			１２：打折全免时间段内
			１３：打折减免时间段内
			２０：超时收费不能使用卡券
			９９９９：其它未知错误
		 * */

		JSONObject attr = resp.getFirstItemAttribute();
		String retCode = attr.getString("retcode");
		if ("9999".equals(retCode) 
				|| "2".equals(retCode)
				|| "5".equals(retCode)) {
			return null;
		}

		TempFeeInfo feeInfo = new TempFeeInfo();
		feeInfo.setOrderNo(attr.getString("orderNo"));
		feeInfo.setCarNo(attr.getString("carNo"));
		feeInfo.setStartTime(DateUtil.parseTimestamp(attr.getString("startTime")));
		feeInfo.setCreateTime(DateUtil.parseTimestamp(attr.getString("createTime")));
		feeInfo.setServiceTime(attr.getInteger("serviceTime"));
		feeInfo.setEndTime(DateUtil.parseTimestamp(attr.getString("endTime")));
		feeInfo.setServiceFee(new BigDecimal(null == attr.getString("serviceFee") ? "0.00" :  attr.getString("serviceFee")));
		feeInfo.setTotalFee(new BigDecimal(null == attr.getString("totalFee") ? "0.00" : attr.getString("totalFee")));
		feeInfo.setValidTimeLen(attr.getInteger("validTimeLen"));
		feeInfo.setFreeMinute(attr.getInteger("freeMinute"));
		feeInfo.setSurplusMinute(attr.getInteger("surplusMinute"));
		feeInfo.setChargeType(attr.getString("chargeType"));
		return feeInfo;
	}

	private JieShunResponse getParkingTempFeePost(Map<String, String> atrributeMap) {
		atrributeMap.put("businesserCode", getCid());
		atrributeMap.put("parkCode", getParkCode());
		atrributeMap.put("orderType", "VNP");
		return postFuncUrl(atrributeMap, ServiceIdEnum.QUERY_TEMP_FEE_ORDER);
	}

	private Boolean openMonthCard(ParkingRechargeOrder order) {
		throwError(ParkingErrorCode.ERROR_CURRENT_RECHARGE_TYPE_NOT_SUPPORTED, "current recharge type not supported");
		return null;
	}

	private Boolean payTempCardFee(ParkingRechargeOrder order) {

		JieShunResponse resp = notifyTempFeeOrderPost(order.getOrderToken());
		if (null == resp) {
			return false;
		}

		order.setErrorDescriptionJson(resp.getTotalJson());
		order.setErrorDescription(resp.getMessage());

		if (!isSuccess(resp)) {
			return false;
		}

		return true;
	}

	private JieShunResponse notifyTempFeeOrderPost(String orderToken) {
		Map<String, String> atrributeMap = new HashMap<>(10);
		atrributeMap.put("orderNo", orderToken);
		atrributeMap.put("tradeStatus", "0");
		return postFuncUrl(atrributeMap, ServiceIdEnum.NOTIFY_TEMP_FEE_RECHARGE);
	}

	private Boolean rechargeMonthlyCard(ParkingRechargeOrder order) {
		MonthCardInfo cardInfo = getCardInfo(order.getPlateNumber());
		if (cardInfo == null) {
			return false;
		}

		// 对过期充值打印信息
		checkOutDatedRecharge(cardInfo.getEndDate(), order);

		// 获取开始和结束时间
		long newStart = getMonthlyRechargeStartTime(cardInfo.getEndDate());
		long newEnd = getMonthlyRechargeEndTime(newStart, order.getMonthCount().intValue());

		// 保存到order中
		Timestamp startStamp = new Timestamp(newStart);
		Timestamp endStamp = new Timestamp(newEnd);
		order.setStartPeriod(startStamp);
		order.setEndPeriod(endStamp);

		// 发送请求，获取充值信息
//		String startDate = DateUtil.dateToStr(startStamp, "yyyy-MM-dd");
		String endDate = DateUtil.dateToStr(endStamp, "yyyy-MM-dd");
		JieShunResponse resp = monthlyRechargePost(order.getPlateNumber(), cardInfo.getBeginDate(), endDate, order.getMonthCount().intValue(), order.getPrice().toPlainString());
		if (null == resp) {
			LOGGER.error("rechargeMonthlyCard resp is error",
					"carNo:" + order.getPlateNumber() + " start:" + newStart + " end:" + newEnd);
			return false;
		}

		// 保存到order中
		order.setErrorDescriptionJson(resp.getTotalJson());
		order.setErrorDescription(resp.getMessage());
		if (isSuccess(resp)) {
			return true;
		}

		return false;
	}

	private JieShunResponse monthlyRechargePost(String plateNumber, String startDate, String endDate, Integer month, String money) {
		Map<String, String> atrributeMap = new HashMap<>();
		atrributeMap.put("parkCode", getParkCode());
		atrributeMap.put("carNo", convertToJieShunCarNo(plateNumber));
		atrributeMap.put("newBeginDate", startDate);
		atrributeMap.put("newEndDate", endDate);
		atrributeMap.put("month", ""+month);
		atrributeMap.put("money", money);
		return postFuncUrl(atrributeMap, ServiceIdEnum.NOTIFY_MONTHLIY_CARD_RECHARGE);
	}

	public long getMonthlyRechargeEndTime(long newStart, int monthCount) {
		Timestamp timestampEnd = Utils.getTimestampByAddThirtyDays(newStart, monthCount);
		return timestampEnd.getTime();
	}

	// 充值回调用时可检查是否过期，并打印信息定位
	private void checkOutDatedRecharge(String endDate, ParkingRechargeOrder order) {
		if (isOutDatedRecharge(endDate)) {
			LOGGER.error("outDated recharge order:" + StringHelper.toJsonString(order) + " endDate:" + endDate);
		}
	}

	public boolean isOutDatedRecharge(String endDate) {
		return false;
	}

	public long getMonthlyRechargeStartTime(String endDate) {
		Timestamp timestamp = DateUtil.parseTimestamp(endDate + " 23:59:59");
		long endTime = timestamp.getTime();
		long now = System.currentTimeMillis();
		if (now/1000 > endTime/1000) { // 比较时按秒计
			return now;
		}

		return endTime+1000; //多加1秒
	}

	private List<CardType> getCardTypesByPlateNumber(String plateNumber) {
		return getCardTypes(plateNumber, null);
	}
	
	private List<CardType> getCardTypesByCardInfo(MonthCardInfo currentMonthCard) {
		return getCardTypes(null, currentMonthCard);
	}
	
	private List<CardType> getCardTypes(String plateNumber, MonthCardInfo currentMonthCard) {
		
		// 获取当前车牌号的月卡类型
		if (null == currentMonthCard && !StringUtils.isEmpty(plateNumber)) {
			currentMonthCard = getCardInfo(plateNumber);
		}
		
		
		List<CardType> totalTypes = getTotalCardTypes();
		if (CollectionUtils.isEmpty(totalTypes)) {
			return null;
		}

		// 根据当前车牌的月卡类型进行过滤
		if (currentMonthCard != null) {
			String currentMonthCardType = currentMonthCard.getCardType();
			totalTypes = totalTypes.stream().filter(r -> r.getCardType().equals(currentMonthCardType))
					.collect(Collectors.toList());
		}

		return totalTypes;
	}

	private List<CardType> getTotalCardTypes() {
		
		String key = getCardTypesKey();
		JSONArray cardTypeArray = null;
		//先从缓存取
		String cardTypesJson = getRedisValue(key);
		if (!StringUtils.isBlank(cardTypesJson)) {
			cardTypeArray = JSONObject.parseArray(cardTypesJson);
		} 
		
		if (null == cardTypeArray) {
			//缓存缺少时，从第三方请求
			JieShunResponse resp = getCardTypePost();
			if (!isSuccess(resp) || resp.isEmpty()) {
				return null;
			}
			cardTypeArray = resp.getFirstSubItemsJSONArray();
			setRedisValue(key, cardTypeArray.toJSONString(), getCardTypeRedisOutDateTime(), CARD_TYPE_OUTDATE_TIMEUNIT);
		}


		List<CardType> totalTypes = new ArrayList<>(20); // 保存所有月卡类型
		for (int i = 0; i < cardTypeArray.size(); i++) {
			JSONObject typeItem = cardTypeArray.getJSONObject(i);
			String cardType = getAttributes(typeItem).getString("cardType");

			JSONArray subItems = getSubItemsArray(typeItem);
			List<FeeItem> feeItems = new ArrayList<>(10);
			for (int j = 0; j < subItems.size(); j++) {
				JSONObject attr = getAttributes(subItems.getJSONObject(j));
				FeeItem feeItem = new FeeItem();
				feeItem.setMonthPeriod(attr.getInteger("monthPeriod"));
				feeItem.setMoney(attr.getString("money"));
				feeItems.add(feeItem);
			}

			CardType type = new CardType();
			type.setCardType(cardType);
			type.setFeeItems(feeItems);
			totalTypes.add(type);
		}
		
		return totalTypes;
	}

	private JSONArray getSubItemsArray(JSONObject item) {
		return item.getJSONArray("subItems");
	}

	private JSONObject getAttributes(JSONObject item) {
		return item.getJSONObject("attributes");
	}

	private JSONArray getDataItemsArray(JSONObject item) {
		return item.getJSONArray("dataItems");
	}

	private JieShunResponse getCardTypePost() {
		Map<String, String> atrributeMap = new HashMap<>();
		atrributeMap.put("parkCode", getParkCode());
		return postFuncUrl(atrributeMap, ServiceIdEnum.QUERY_CARD_TYPES_AND_FEES);
	}

	private ParkingRechargeRateDTO convertToRateDTO(String cardType, Integer monthPeriod, String money,
			ParkingLot parkingLot) {
		ParkingRechargeRateDTO dto = new ParkingRechargeRateDTO();
		dto.setOwnerType(parkingLot.getOwnerType());
		dto.setOwnerId(parkingLot.getOwnerId());
		dto.setParkingLotId(parkingLot.getId());
		dto.setVendorName(getParkingVendorName());
//		dto.setRateToken();
		dto.setRateName(monthPeriod + "个月");
		dto.setMonthCount(new BigDecimal(monthPeriod));
		dto.setPrice(new BigDecimal(money));
		dto.setCardType(cardType);
		dto.setOriginalPrice(dto.getPrice());
		return dto;
	}

	private Long getEndTimeStampByDate(String endDate) {
		Timestamp timeStamp = DateUtil.parseTimestamp(endDate + " 23:59:59");
		return null == timeStamp ? System.currentTimeMillis() : timeStamp.getTime();
	}

	/**
	 * 根据卡号
	 * 
	 * @param plateNumber
	 * @return
	 */
	MonthCardInfo getCardInfo(String plateNumber) {
		
		Map<String, String> atrributeMap = new HashMap<>();
		atrributeMap.put("carNo", convertToJieShunCarNo(plateNumber));
		JieShunResponse resp = getCardInfoPost(atrributeMap);
		if (!isSuccess(resp) || resp.isEmpty()) {
			return null;
		}

		MonthCardInfo cardInfo = new MonthCardInfo();
		JSONObject personJSON = resp.getFirstItemAttribute();
		cardInfo.setPersonName(personJSON.getString("personName"));
		cardInfo.setIdentityCode(personJSON.getString("identityCode"));
		cardInfo.setTelephone(personJSON.getString("telephone"));

		JSONObject cardJSON = resp.getFirstSubItemAttribute();
		cardInfo.setCarNo(getCarNoFromJieShun(cardJSON.getString("carNo")));
		cardInfo.setBeginDate(cardJSON.getString("beginDate"));
		cardInfo.setEndDate(cardJSON.getString("endDate"));
		cardInfo.setCardType(cardJSON.getString("cardType"));
		cardInfo.setPackageInfo(cardJSON.getString("package"));
		cardInfo.setPhysicalNo(cardJSON.getString("physicalNo"));
		
		return cardInfo;
	}

	private String convertToJieShunCarNo(String plateNumber) {
		return plateNumber.substring(0, 1) + "-" + plateNumber.substring(1);
	}

	private String getCarNoFromJieShun(String carNo) {
		return carNo.replaceFirst("-", "");
	}

	private JieShunResponse getCardInfoPost(Map<String, String> atrributeMap) {
		atrributeMap.put("areaCode", getParkCode());
		return postFuncUrl(atrributeMap, ServiceIdEnum.QUERY_CARD_INFO);
	}

	private boolean isSuccess(JieShunResponse resp) {
		if (null != resp && ErrorCodeEnum.SUCCESS.getCode().equals(resp.getResultCode())) {
			return true;
		}
		return false;
	}

	private JieShunResponse postFuncUrl(Map<String, String> atrributeMap, ServiceIdEnum serviceIdEnum) {
		Map<String, String> configMap = getCommonConfigMap();

		// 获取令牌
		String token = getToken(false);
		// 生成MD5签名
		String paramJson = buildRequestParam(atrributeMap, serviceIdEnum.getCode());
		String sn = makeMd5Sign(paramJson + configMap.get("signKey"));

		// 构造参数
		Map<String, String> tmap = new HashMap<>();
		tmap.put("cid", configMap.get("cid"));
		tmap.put("v", configMap.get("version"));
		tmap.put("p", paramJson);
		tmap.put("sn", sn);
		tmap.put("tn", token);

		// 获取结果
		String funcUrl = configMap.get("funcUrl");
		JieShunResponse resp = post(funcUrl, tmap);
		if (null == resp) {
			return null;
		}

		// 如果因为token失效，应该强制更新token并且再请求一次
		if (isTokenInvalid(resp)) {
			tmap.put("tn", getToken(true));
			resp = post(funcUrl, tmap);
		}

		return resp;
	}

	private String buildRequestParam(Map<String, String> atrributeMap, String serviceId) {

		JSONObject atrributes = new JSONObject();
		for (Map.Entry<String, String> entry : atrributeMap.entrySet()) {
			atrributes.put(entry.getKey(), entry.getValue());
		}

		JSONObject json = new JSONObject();
		json.put("serviceId", serviceId);
		json.put("requestType", "DATA");
		json.put("attributes", atrributes);
		return json.toJSONString();
	}

	private JieShunResponse post(String url, Map<String, String> tmap) {
		return convertToJieShunResponse(Utils.post(url, tmap));
	}

	private String makeMd5Sign(String body) {
		MessageDigest md5Tool;
		byte[] md5Data = null;
		try {
			md5Tool = MessageDigest.getInstance("MD5");
			md5Data = md5Tool.digest(body.getBytes("UTF-8"));
		} catch (Exception e) {
			throwError(ParkingErrorCode.ERROR_FETCH_TOKEN, "error get token from third");
		}
		return toHexString(md5Data);
	}

	private JieShunResponse convertToJieShunResponse(String result) {
		if (StringUtils.isBlank(result)) {
			return null;
		}

		JSONObject json = JSONObject.parseObject(result);
		if (null == json) {
			return null;
		}

		JieShunResponse resp = new JieShunResponse();
		resp.setTotalJson(result);
		resp.setResultCode(json.getInteger("resultCode"));
		resp.setMessage(json.getString("message"));
		resp.setToken(json.getString("token"));

		JSONArray dataItemsArray = getDataItemsArray(json);
		if (null != dataItemsArray && dataItemsArray.size() > 0) {
			resp.setItemsJSONArray(dataItemsArray);
			JSONObject firstItemJSON = resp.getItemsJSONArray().getJSONObject(0);
			resp.setFirstItemAttribute(getAttributes(firstItemJSON));
			JSONArray subItemsArray = getSubItemsArray(firstItemJSON);
			if (null != subItemsArray && subItemsArray.size() > 0) {
				resp.setFirstSubItemsJSONArray(subItemsArray);
				resp.setFirstSubItemAttribute(getAttributes(subItemsArray.getJSONObject(0)));
			}
		}

		return resp;
	}

	/**
	 * 查看返回的结果是否提示token失效了
	 * 
	 * @param result
	 * @return
	 */
	private boolean isTokenInvalid(JieShunResponse resp) {
		return ErrorCodeEnum.TOKEN_OUTDATED_OR_INVALID.getCode().equals(resp.getResultCode());
	}

	private String getToken(boolean needForceUpdate) {
		String key = getTokenKey();
		if (!needForceUpdate) { // 如果不需要强制更新token则从缓存获取
			// 从缓存中获取token
			String value = getRedisValue(key);
			if (value != null) {
				return value;
			}
		}

		// 从第三方获取token
		String token = postLoginForToken();
		if (null == token) {
			throwError(ParkingErrorCode.ERROR_FETCH_TOKEN, "error get token from third");
		}

		// 将token保存到缓存中，因第三方是120分钟，所以这里设置为100分钟
		setRedisValue(key, token, getTokenRedisOutDateTime(), TOKEN_OUTDATE_TIMEUNIT);
		return token;
	}
	
	private String getRedisValue(String key) {
		return  getRedisTemplate(key).opsForValue().get(key);
	}
	
	private void setRedisValue(String key, String value, long time, TimeUnit timeUnit) {
		getRedisTemplate(key).opsForValue().set(key, value, time, timeUnit);
	}
	
	@SuppressWarnings("unchecked")
	private RedisTemplate<String, String> getRedisTemplate(String key) {
		Accessor acc = bigCollectionProvider.getMapAccessor(key, "");
		return acc.getTemplate(new StringRedisSerializer());
	}

	private String postLoginForToken() {
		Map<String, String> configMap = getCommonConfigMap();

		// 构造参数
		Map<String, String> tmap = new HashMap<>();
		tmap.put("cid", configMap.get("cid"));
		tmap.put("usr", configMap.get("usr"));
		tmap.put("psw", configMap.get("psw"));

		// 获取结果
		JieShunResponse resp = post(configMap.get("loginUrl"), tmap);
		if (!isSuccess(resp)) {
			return null;
		}

		return resp.getToken();
	}

	private static String toHexString(byte[] bytes) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			buffer.append(String.format("%02X", bytes[i]));
		}
		return buffer.toString();
	}

	private void throwError(int errorCode, String errorMsg) {
		throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE_JIESHUN, errorCode, errorMsg);
	}

	/**
	 * 返回包含如下key的map loginUrl funcUrl cid usr psw version
	 * 
	 * @return
	 */
	protected Map<String, String> getCommonConfigMap() {

		// 通用前缀，用于获取通用配置
		String commonPrefix = getBaseConfigPrefix();

		// 停车场特定前缀，用于获取停车场特有配置
		String specificPrefix = getSpecificConfigPrefix();

		// 通用登陆url
		String loginUrl = configurationProvider.getValue(commonPrefix + "loginUrl", "");

		// 通用功能url
		String funcUrl = configurationProvider.getValue(commonPrefix + "funcUrl", "");

		// 通用接口版本号
		String version = configurationProvider.getValue(commonPrefix + "version", "");

		// 客户号
		String cid = configurationProvider.getValue(specificPrefix + "cid", "");

		// 账号
		String usr = configurationProvider.getValue(specificPrefix + "usr", "");

		// 密码
		String psw = configurationProvider.getValue(specificPrefix + "psw", "");

		// 签名signKey
		String signKey = configurationProvider.getValue(specificPrefix + "signKey", "");

		// 组装
		Map<String, String> configTmpMap = new HashMap<>(10);
		configTmpMap.put("loginUrl", loginUrl);
		configTmpMap.put("funcUrl", funcUrl);
		configTmpMap.put("version", version);
		configTmpMap.put("cid", cid);
		configTmpMap.put("usr", usr);
		configTmpMap.put("psw", psw);
		configTmpMap.put("signKey", signKey);
		return configTmpMap;
	}

	private String getParkCode() {
		return configurationProvider.getValue(getSpecificConfigPrefix() + "parkCode", "");
	}
	
	private String getCid() {
		return configurationProvider.getValue(getSpecificConfigPrefix() + "cid", "");
	}

	private String getTokenKey() {
		return getSpecificConfigPrefix() + "-tokenKey";
	}
	
	private String getCardTypesKey() {
		return getSpecificConfigPrefix() + "-cardTypeKeys";
	}

	private String getSpecificConfigPrefix() {
		return getBaseConfigPrefix() + getParkingVendorName() + ".";
	}

	private String getBaseConfigPrefix() {
		return "parking.jieshun.";
	}
	
	private Long getTokenRedisOutDateTime() {
		return Long.parseLong(configurationProvider.getValue(getBaseConfigPrefix() + "tokenRedisTime", "100"));
	}
	
	private long getCardTypeRedisOutDateTime() {
		return Long.parseLong(configurationProvider.getValue(getBaseConfigPrefix() + "cardTypeRedisTime", "60"));
	}
	
}
