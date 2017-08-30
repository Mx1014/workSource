// @formatter:off
package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.parking.*;
import com.everhomes.parking.ketuo.*;
import com.everhomes.rest.parking.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 科兴 正中会 停车对接
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "KETUO2")
public class KetuoKexingParkingVendorHandler extends KetuoParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(KetuoKexingParkingVendorHandler.class);

	@Autowired
    private ConfigurationProvider configProvider;
	@Autowired
	private LocaleStringService localeStringService;

	private static final String GET_PARKINGS = "/api/find/GetParkingLotList";
	private static final String GET_FREE_SPACE_NUM = "/api/find/GetFreeSpaceNum";
	private static final String GET_CAR_LOCATION = "/api/find/GetCarLocInfo";
	private static final String ADD_MONTH_CARD = "/api/card/AddMonthCarCardNo_KX";
	//科兴的需求，充值过期的月卡时，需要计算费率，标记为自定义custom的费率，其他停车场不建议做这样的功能。
	static final String EXPIRE_CUSTOM_RATE_TOKEN = "custom";

	String appId = "1";
	String appkey = "b20887292a374637b4a9d6e9f940b1e6";
	String url = "http://220.160.111.114:8099";
	Integer parkingId = 1;

//	//支持开卡，返回true
//	public boolean getOpenCardFlag() {
//		return true;
//	}
//
//	//是否支持过期缴费, 目前只支持科兴过期缴费，科兴的过期月卡缴费规则，比较复杂，而且依赖第三方停车系统，不建议这样做
//	protected boolean getExpiredRechargeFlag() {
//		return true;
//	}

	public KetuoRequestConfig getKetuoRequestConfig() {

		String url = configProvider.getValue("parking.kexing.url", "");
		String key = configProvider.getValue("parking.kexing.key", "");
		String user = configProvider.getValue("parking.kexing.user", "");
		String pwd = configProvider.getValue("parking.kexing.pwd", "");

		KetuoRequestConfig config = new KetuoRequestConfig();
		config.setUrl(url);
		config.setKey(key);
		config.setUser(user);
		config.setPwd(pwd);

		return config;
	}

	@Override
	public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot,
																String plateNumber, String cardNo) {
		List<KetuoCardRate> list = new ArrayList<>();
		List<KetuoCardType> types = getCardType();

		if(StringUtils.isBlank(plateNumber)) {
			for(KetuoCardType k: types) {
				populateRateInfo(k.getCarType(), k.getTypeName(), list);
			}
		}else{
			KetuoCard cardInfo = getCard(plateNumber);
			if(null != cardInfo) {
				long now = System.currentTimeMillis();
				long expireTime = strToLong(cardInfo.getValidTo());

				if(expireTime < now) {
					KetuoCardRate ketuoCardRate = getExpiredRate(cardInfo, parkingLot, now);

					if (null != ketuoCardRate) {
						list.add(ketuoCardRate);
					}
				}else {
					String carType = cardInfo.getCarType();
					String typeName = null;
					for(KetuoCardType kt: types) {
						if(carType.equals(kt.getCarType())) {
							typeName = kt.getTypeName();
							break;
						}
					}
					populateRateInfo(carType, typeName, list);

				}
			}
		}

		return list.stream().map(r -> convertParkingRechargeRateDTO(parkingLot, r)).collect(Collectors.toList());
	}

	@Override
	public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
		if (order.getOrderType().equals(ParkingOrderType.RECHARGE.getCode())) {
			if(order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode())) {
				return rechargeMonthlyCard(order);
			}
			return payTempCardFee(order);
		}else {
			return addMonthCard(order);
		}
	}

	private KetuoCardRate getExpiredRate(KetuoCard cardInfo, ParkingLot parkingLot, long now) {
		KetuoCardRate ketuoCardRate = null;

		if(parkingLot.getExpiredRechargeFlag() == ParkingConfigFlag.SUPPORT.getCode()) {

			Integer rechargeMonthCount = parkingLot.getExpiredRechargeMonthCount();
			Integer freeMoney = null != cardInfo.getFreeMoney() ? cardInfo.getFreeMoney() : 0;
			if(rechargeMonthCount <= 0) {
				LOGGER.error("ParkingLot rechargeMonthCount less than 0, parkingLot={}", parkingLot);
				throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_PLATE_EXIST,
						"ParkingLot rechargeMonthCount less than 0");
			}
			List<KetuoCardRate> rates = getCardRule(cardInfo.getCarType());
			if(rates.size() != 0) {
				KetuoCardRate rate = null;
				for(KetuoCardRate kr: rates) {
					if(Integer.valueOf(kr.getRuleAmount()) == 1)
						rate = kr;
				}
				if (null != rate) {
					//默认查询一个月的费率，如果不存在，就返回null。
					ketuoCardRate = new KetuoCardRate();

					if(parkingLot.getExpiredRechargeType() == ParkingCardExpiredRechargeType.ALL.getCode()) {
						//实际价格减去优惠金额，因为是一个月的费率，直接减。
						Integer actualPrice = Integer.valueOf(rate.getRuleMoney()) - freeMoney;
						ketuoCardRate.setRuleMoney(String.valueOf(actualPrice * rechargeMonthCount));

					}else {
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeInMillis(now);
						int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
						int today = calendar.get(Calendar.DAY_OF_MONTH);
						Integer actualPrice = Integer.valueOf(rate.getRuleMoney()) - freeMoney;
						ketuoCardRate.setRuleMoney(String.valueOf(actualPrice * (rechargeMonthCount - 1)
								+ actualPrice * (maxDay - today + 1) / DAY_COUNT ));

					}

					ketuoCardRate.setRuleId(EXPIRE_CUSTOM_RATE_TOKEN);
//    				rate.setCarType(carType);
					ketuoCardRate.setRuleAmount(String.valueOf(parkingLot.getExpiredRechargeMonthCount()));
//    				rate.setRuleName(ruleName);
//    				rate.setRuleType(ruleType);
//    				rate.setTypeName(typeName);
				}
			}
		}
		return ketuoCardRate;
	}

	@Override
	public void updateParkingRechargeOrderRate(ParkingRechargeOrder order) {
		String plateNumber = order.getPlateNumber();
		if(EXPIRE_CUSTOM_RATE_TOKEN.equals(order.getRateToken())) {
			order.setRateName(EXPIRE_CUSTOM_RATE_TOKEN);

		}else {
			KetuoCard cardInfo = getCard(plateNumber);
			KetuoCardRate ketuoCardRate = null;
			String cardType = CAR_TYPE;
			Integer freeMoney = 0;
			if(null != cardInfo) {
				cardType = cardInfo.getCarType();
				freeMoney = cardInfo.getFreeMoney();
			}
			for(KetuoCardRate rate: getCardRule(cardType)) {
				if(rate.getRuleId().equals(order.getRateToken())) {
					ketuoCardRate = rate;
				}
			}
			if(null == ketuoCardRate) {
				LOGGER.error("Rate not found, cmd={}", order);
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"Rate not found.");
			}
			order.setRateName(ketuoCardRate.getRuleName());

			order.setPrice(new BigDecimal(order.getPrice().intValue() * 100 - (freeMoney * order.getMonthCount().intValue()))
							.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
		}

	}

	boolean addMonthCard(ParkingRechargeOrder order){

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		//充值记录的开始时间
		Timestamp tempStart = new Timestamp(calendar.getTimeInMillis());
		order.setStartPeriod(tempStart);

		KetuoCardRate ketuoCardRate = null;
		for(KetuoCardRate rate: getCardRule(CAR_TYPE)) {
			if(rate.getRuleId().equals(order.getRateToken())) {
				ketuoCardRate = rate;
			}
		}
		if(null == ketuoCardRate) {
			LOGGER.error("Rate not found, cmd={}", order);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Rate not found.");
		}

		Integer payMoney = order.getPrice().intValue() * 100 - Integer.parseInt(ketuoCardRate.getRuleMoney())
				* (order.getMonthCount().intValue() - 1);

		if(addMonthCard(order.getPlateNumber(), payMoney)) {
			Integer count = order.getMonthCount().intValue();

			LOGGER.debug("Parking addMonthCard,count={}", count);

			if(count > 1) {
				ParkingRechargeOrder tempOrder = ConvertHelper.convert(order, ParkingRechargeOrder.class);
				tempOrder.setMonthCount(new BigDecimal(count -1) );
				tempOrder.setPrice(new BigDecimal((tempOrder.getPrice().intValue() * 100 - payMoney) / 100));
				if(rechargeMonthlyCard(order, tempOrder)) {
					updateFlowStatus(order);
					return true;
				}
			}else {
				updateFlowStatus(order);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean rechargeMonthlyCard(ParkingRechargeOrder originalOrder){

		ParkingRechargeOrder tempOrder = ConvertHelper.convert(originalOrder, ParkingRechargeOrder.class);

		return rechargeMonthlyCard(originalOrder, tempOrder);

	}

	private boolean rechargeMonthlyCard(ParkingRechargeOrder originalOrder, ParkingRechargeOrder tempOrder) {

		JSONObject param = new JSONObject();
		String plateNumber = tempOrder.getPlateNumber();

		KetuoCard card = getCard(plateNumber);
		String oldValidEnd = card.getValidTo();
		Long expireTime = strToLong(oldValidEnd);
		long now = System.currentTimeMillis();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		if(expireTime < now) {
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			String date = sdf2.format(new Date(now)) + " 00:00:00";
			try {
				//减一秒，当卡过期时，还支持充值时，充值时间从今天开始
				expireTime = sdf1.parse(date).getTime() - 1000L;
			} catch (ParseException e) {
				LOGGER.info("date={}, time={}", date, expireTime, e);
			}
		}

		Timestamp tempStart = Utils.addSecond(expireTime, 1);
		Timestamp tempEnd = Utils.getTimestampByAddNatureMonth(expireTime, tempOrder.getMonthCount().intValue());
		String validStart = sdf1.format(tempStart);
		String validEnd = sdf1.format(tempEnd);

		param.put("cardId", card.getCardId());
		//修改科托ruleType 固定为1 表示月卡车
		param.put("ruleType", RULE_TYPE);
		param.put("ruleAmount", String.valueOf(tempOrder.getMonthCount().intValue()));
		// 支付金额（分）
		param.put("payMoney", tempOrder.getPrice().intValue() * 100);
		//续费开始时间 yyyy-MM-dd HH:mm:ss 每月第一天的 0点0分0秒
		param.put("startTime", validStart);
		//续费结束时间 yyyy-MM-dd HH:mm:ss 每月最后一天的23点59分59秒
		param.put("endTime", validEnd);
		param.put("freeMoney", card.getFreeMoney() * tempOrder.getMonthCount().intValue());

		if(EXPIRE_CUSTOM_RATE_TOKEN.equals(tempOrder.getRateToken())) {
			ParkingLot parkingLot = parkingProvider.findParkingLotById(tempOrder.getParkingLotId());
			if(parkingLot.getExpiredRechargeType() == ParkingCardExpiredRechargeType.ACTUAL.getCode()) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(now);
				int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				int today = calendar.get(Calendar.DAY_OF_MONTH);
				Integer actualPrice = card.getFreeMoney();
				Integer monthCount = tempOrder.getMonthCount().intValue();
				param.put("freeMoney", actualPrice * (monthCount - 1)
						+ actualPrice * (maxDay - today + 1) / DAY_COUNT);
			}
		}
		String json = post(param, RECHARGE);

		//将充值信息存入订单
		originalOrder.setErrorDescriptionJson(json);
		if (null == originalOrder.getStartPeriod()) {
			originalOrder.setStartPeriod(tempStart);
		}
		originalOrder.setEndPeriod(tempEnd);

		JSONObject jsonObject = JSONObject.parseObject(json);
		Object obj = jsonObject.get("resCode");
		if(null != obj ) {
			int resCode = (int) obj;
			if(resCode == 0) {
				return true;
			}
		}
		return false;
	}

	private boolean addMonthCard(String plateNo, Integer money){

		JSONObject param = new JSONObject();
		plateNo = plateNo.substring(1, plateNo.length());

		param.put("plateNo", plateNo);
		param.put("money", money);
		String json = post(param, ADD_MONTH_CARD);

		JSONObject jsonObject = JSONObject.parseObject(json);
		Object obj = jsonObject.get("resCode");
		if(null != obj ) {
			int resCode = (int) obj;
			if(resCode == 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ParkingFreeSpaceNumDTO getFreeSpaceNum(GetFreeSpaceNumCommand cmd) {

		ParkingFreeSpaceNumDTO dto = ConvertHelper.convert(cmd, ParkingFreeSpaceNumDTO.class);

		KexingFreeSpaceNum kexingFreeSpaceNum = getKexingFreeSpaceNum();

		if (null != kexingFreeSpaceNum) {
			dto.setFreeSpaceNum(kexingFreeSpaceNum.getFreeSpaceNum());

			return dto;
		}
		return null;
	}

	private KexingFreeSpaceNum getKexingFreeSpaceNum() {

		LinkedHashMap<String, Object> param = new LinkedHashMap<>();
		param.put("parkId", parkingId);

		JSONObject params = createRequestParam(param);
		String json = Utils.post(url + GET_FREE_SPACE_NUM, params);

		KetuoJsonEntity<KexingFreeSpaceNum> entity = JSONObject.parseObject(json, new TypeReference<KetuoJsonEntity<KexingFreeSpaceNum>>(){});

		if(entity.isSuccess()){
			List<KexingFreeSpaceNum> list = entity.getData();
			if(null != list && !list.isEmpty()) {
				return list.get(0);
			}
		}
		return null;
	}

	private JSONObject createRequestParam(LinkedHashMap<String, Object> param) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		StringBuilder sb = new StringBuilder();
		Set<Map.Entry<String, Object>> entries = param.entrySet();
		for (Map.Entry entry: entries) {
			sb.append(entry.getValue());
		}

		String str = sb.append(sdf.format(new Date())).append(appkey).toString();
		String key = Utils.md5(str);

		JSONObject params = new JSONObject();
		params.put("appId", appId );
		params.put("key", key );

		for (Map.Entry entry: entries) {
			params.put((String)entry.getKey(), entry.getValue());
		}

		return params;
	}

	private List<KetuoParking> getKetuoParkings() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		String str = sdf.format(new Date()) + appkey;
		String key = Utils.md5(str);

		JSONObject params = new JSONObject();
		params.put("appId", appId );
		params.put("key", key );
		String json = Utils.post(url + GET_PARKINGS, params);

		KetuoJsonEntity<KetuoParking> entity = JSONObject.parseObject(json, new TypeReference<KetuoJsonEntity<KetuoParking>>(){});
		if(entity.isSuccess()){
			List<KetuoParking> list = entity.getData();
			if(null != list && !list.isEmpty()) {
				return list;
			}
		}
		return new ArrayList<>();
	}

	@Override
	public ParkingCarLocationDTO getCarLocation(ParkingLot parkingLot, GetCarLocationCommand cmd) {
		ParkingCarLocationDTO dto = ConvertHelper.convert(cmd, ParkingCarLocationDTO.class);

		KetuoCarLocation ketuoCarLocation = getCarLocation(cmd.getPlateNumber());

		if (null != ketuoCarLocation) {
			dto.setSpaceNo(ketuoCarLocation.getSpaceNo());
			dto.setParkingName(parkingLot.getName());
			dto.setFloorName(ketuoCarLocation.getFloorName());
			dto.setLocation(ketuoCarLocation.getArea());

			String scope = ParkingNotificationTemplateCode.SCOPE;
			int code = ParkingNotificationTemplateCode.DEFAULT_MINUTE_UNIT;
			String locale = Locale.SIMPLIFIED_CHINESE.toString();
			String unit = localeStringService.getLocalizedString(scope, String.valueOf(code), locale, "");
			dto.setParkingTime(ketuoCarLocation.getParkTime() + unit);

			String entryTime = ketuoCarLocation.getInTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			try {
				dto.setEntryTime(sdf.parse(entryTime).getTime());
			} catch (ParseException e) {
				LOGGER.error("Parking parse entryTime error, cmd={}, ketuoCarLocation={}", cmd, ketuoCarLocation);
			}
			dto.setCarImageUrl(ketuoCarLocation.getCarImage());

			return dto;
		}
		return null;
	}

	private KetuoCarLocation getCarLocation(String plateNumber) {
		LinkedHashMap<String, Object> param = new LinkedHashMap<>();
		param.put("parkId", parkingId);
		param.put("plateNo", plateNumber);

		JSONObject params = createRequestParam(param);
		String json = Utils.post(url + GET_CAR_LOCATION, params);

		KetuoJsonEntity<KetuoCarLocation> entity = JSONObject.parseObject(json, new TypeReference<KetuoJsonEntity<KetuoCarLocation>>(){});

		if(entity.isSuccess()){
			List<KetuoCarLocation> list = entity.getData();
			if(null != list && !list.isEmpty()) {
				return list.get(0);
			}
		}
		return null;
	}
}
