// @formatter:off
package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.flow.FlowProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.parking.*;
import com.everhomes.parking.ketuo.*;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.MD5Utils;
import com.everhomes.util.RuntimeErrorException;
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

/**
 * 科兴 正中会 停车对接
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "KETUO2")
public class KetuoKexingParkingVendorHandler extends KetuoParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(KetuoKexingParkingVendorHandler.class);

	@Autowired
	private LocaleStringService localeStringService;
	@Autowired
	private AddressProvider addressProvider;
	@Autowired
	private FlowProvider flowProvider;

	private static final String GET_PARKINGS = "/api/find/GetParkingLotList";
	private static final String GET_FREE_SPACE_NUM = "/api/find/GetFreeSpaceNum";
	private static final String GET_CAR_LOCATION = "/api/find/GetCarLocInfo";
	private static final String GET_PARKING_CAR_INFO = "/api/wec/GetParkingCarInfo";
	private static final String ADD_MONTH_CARD = "/api/card/AddMonthCarCardNo_KX";
	//科兴的需求，充值过期的月卡时，需要计算费率，标记为自定义custom的费率，其他停车场不建议做这样的功能。
	private static final String EXPIRE_CUSTOM_RATE_TOKEN = "custom_expired";

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

//	@Override
//	public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot,
//																String plateNumber, String cardNo) {
//		List<KetuoCardRate> list = new ArrayList<>();
//		List<KetuoCardType> types = getCardType();
//
//		if(StringUtils.isBlank(plateNumber)) {
//			for(KetuoCardType k: types) {
//				populateRateInfo(k.getCarType(), k, list);
//			}
//		}else{
//			KetuoCard cardInfo = getCard(plateNumber);
//			if(null != cardInfo) {
//				long now = System.currentTimeMillis();
//				long expireTime = strToLong(cardInfo.getValidTo());
//
//				String carType = cardInfo.getCarType();
//				KetuoCardType type = null;
//				for(KetuoCardType kt: types) {
//					if(carType.equals(kt.getCarType())) {
//						type = kt;
//						break;
//					}
//				}
//
//				if(expireTime < now) {
//					KetuoCardRate ketuoCardRate = getExpiredRate(cardInfo, parkingLot, now);
//
//					ketuoCardRate.setCarType(carType);
//					ketuoCardRate.setTypeName(type.getTypeName());
//
//					if (null != ketuoCardRate) {
//						list.add(ketuoCardRate);
//					}
//				}else {
//					populateRateInfo(carType, type, list);
//				}
//			}
//		}
//
//		return list.stream().map(r -> convertParkingRechargeRateDTO(parkingLot, r)).collect(Collectors.toList());
//	}

	/**
	 * 查询 过期月卡充值信息
	 * @param cmd
	 * @return
	 */
	@Override
	public ParkingExpiredRechargeInfoDTO getExpiredRechargeInfo(ParkingLot parkingLot, GetExpiredRechargeInfoCommand cmd) {

		ParkingExpiredRechargeInfoDTO dto = null;
		List<KetuoCardType> types = getCardType();

		KetuoCard cardInfo = getCard(cmd.getPlateNumber());
		if(null != cardInfo) {
			long now = System.currentTimeMillis();
			long expireTime = strToLong(cardInfo.getValidTo());

			String carType = cardInfo.getCarType();
			KetuoCardType type = null;
			for(KetuoCardType kt: types) {
				if(carType.equals(kt.getCarType())) {
					type = kt;
					break;
				}
			}

			if(expireTime < now) {
				dto = getExpiredRate(cmd.getPlateNumber(), cardInfo, parkingLot, now, type);
			}
		}

		return dto;
	}

	@Override
	public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
		if (order.getOrderType().equals(ParkingOrderType.RECHARGE.getCode())) {
			if(order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode())) {
				return rechargeMonthlyCard(order);
			}
		}else {
			return openMonthCard(order);
		}
		return false;
	}

	private ParkingExpiredRechargeInfoDTO getExpiredRate(String plateNumber, KetuoCard cardInfo, ParkingLot parkingLot, long now, KetuoCardType type) {
		ParkingExpiredRechargeInfoDTO dto = null;
		Long startPeriod = null;

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
					KetuoCardRate ketuoCardRate = new KetuoCardRate();

					long expireTime = strToLong(cardInfo.getValidTo());
					//卡的有效期
					Calendar cardCalendar = Calendar.getInstance();
					cardCalendar.setTimeInMillis(expireTime);
					//当前时间
					Calendar currentCalendar = Calendar.getInstance();
					currentCalendar.setTimeInMillis(now);

					//查询车在场信息，来判断车是否在场
					KetuoCarInfo carInfo = getKetuoCarInfo(plateNumber);
					if (null != carInfo) {
						long entryTime = strToLong(carInfo.getEntryTime());
						//车进场时间
						Calendar entryCalendar = Calendar.getInstance();
						entryCalendar.setTimeInMillis(entryTime);

						if (entryTime <= expireTime) {

							//当卡的有效期是月的最后一天，只考虑月卡有效期是每月的最后一天 23:59:59,如果月卡有效期不是这个格式的时间，
							// 则是停车场返回的月卡有效期的问题
							if(Utils.isLastDayOfMonth(cardCalendar)){
								//计算要补充的月数，当前月减去月卡有效期月份 加上后台设置的要预交的月数
								rechargeMonthCount = currentCalendar.get(Calendar.MONTH) - cardCalendar.get(Calendar.MONTH)
										+ rechargeMonthCount - 1;

								startPeriod = Utils.getLongByAddSecond(expireTime, 1);
								//如果车在场，且车入场时间是在车有效期之前，时间用卡的有效期
								//因为是用卡的有效期开始计算钱，注意这里一定是要计算成整月的钱
								calculatePrice(cardCalendar, rate, ketuoCardRate, freeMoney, rechargeMonthCount,
										ParkingCardExpiredRechargeType.ALL.getCode());
							}

						}else if(entryTime > expireTime) {
							rechargeMonthCount = currentCalendar.get(Calendar.MONTH) - entryCalendar.get(Calendar.MONTH)
									+ rechargeMonthCount;
							//如果车在场，且车入场时间是在车有效期之前，时间用车进场时间
							calculatePrice(entryCalendar, rate, ketuoCardRate, freeMoney, rechargeMonthCount,
									parkingLot.getExpiredRechargeType());

							if (parkingLot.getExpiredRechargeType() == ParkingCardExpiredRechargeType.ALL.getCode()) {
								startPeriod = Utils.getFirstDayOfMonth(entryTime);
							}else {
								startPeriod = entryTime;
							}
						}
					}else {
						//如果车不在场，时间用当期日期
						calculatePrice(currentCalendar, rate, ketuoCardRate, freeMoney, rechargeMonthCount,
								parkingLot.getExpiredRechargeType());

						if (parkingLot.getExpiredRechargeType() == ParkingCardExpiredRechargeType.ALL.getCode()) {
							startPeriod = Utils.getFirstDayOfMonth(now);
						}else {
							startPeriod = Utils.getNewDay(now);
						}
					}

					//费率
					ketuoCardRate.setRuleId(EXPIRE_CUSTOM_RATE_TOKEN);
					ketuoCardRate.setRuleAmount(String.valueOf(rechargeMonthCount));
					ketuoCardRate.setCarType(type.getCarType());
					ketuoCardRate.setTypeName(type.getTypeName());

					ParkingRechargeRateDTO rateDTO = convertParkingRechargeRateDTO(parkingLot, ketuoCardRate);
					dto = ConvertHelper.convert(rateDTO, ParkingExpiredRechargeInfoDTO.class);

					dto.setStartPeriod(startPeriod);

					dto.setEndPeriod(Utils.getLongByAddNatureMonth(Utils.getlastDayOfMonth(now), parkingLot.getExpiredRechargeMonthCount() -1));

					//计算优惠
					if (null != parkingLot.getMonthlyDiscountFlag()) {
						if (ParkingConfigFlag.SUPPORT.getCode() == parkingLot.getMonthlyDiscountFlag()) {
							dto.setOriginalPrice(dto.getPrice());
							BigDecimal newPrice = dto.getPrice().multiply(new BigDecimal(parkingLot.getMonthlyDiscount()))
									.divide(new BigDecimal(10), CARD_RATE_RETAIN_DECIMAL, RoundingMode.HALF_UP);
							dto.setPrice(newPrice);
						}
					}
				}
			}
		}
		return dto;
	}

	private void calculatePrice(Calendar calendar, KetuoCardRate rate, KetuoCardRate newRate, Integer freeMoney,
								Integer rechargeMonthCount, byte expiredRechargeType) {
		if(expiredRechargeType == ParkingCardExpiredRechargeType.ALL.getCode()) {
			//实际价格减去优惠金额，因为是一个月的费率，直接减。
			Integer actualPrice = Integer.valueOf(rate.getRuleMoney()) - freeMoney;
			newRate.setRuleMoney(String.valueOf(actualPrice * rechargeMonthCount));

		}else {

			int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			int today = calendar.get(Calendar.DAY_OF_MONTH);
			Integer actualPrice = Integer.valueOf(rate.getRuleMoney()) - freeMoney;
			newRate.setRuleMoney(String.valueOf(actualPrice * (rechargeMonthCount - 1)
					+ actualPrice * (maxDay - today + 1) / DAY_COUNT ));

		}
	}

	@Override
	public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {
		String plateNumber = order.getPlateNumber();

		KetuoCard cardInfo = getCard(plateNumber);

		if(EXPIRE_CUSTOM_RATE_TOKEN.equals(order.getRateToken())) {
			List<KetuoCardType> types = getCardType();
			String carType = cardInfo.getCarType();

			KetuoCardType type = null;
			for(KetuoCardType kt: types) {
				if(carType.equals(kt.getCarType())) {
					type = kt;
					break;
				}
			}
			ParkingExpiredRechargeInfoDTO dto = getExpiredRate(order.getPlateNumber(), cardInfo, parkingLot, System.currentTimeMillis(), type);

			if (order.getPrice().compareTo(dto.getPrice()) != 0) {
				LOGGER.error("Invalid order price, orderPrice={}, ratePrice={}", order.getPrice(), dto.getPrice());
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"Invalid order price.");
			}

			order.setRateName(EXPIRE_CUSTOM_RATE_TOKEN);
			order.setOriginalPrice(dto.getOriginalPrice());
			order.setPrice(dto.getPrice());
			order.setStartPeriod(new Timestamp(dto.getStartPeriod()));
//			order.setCarPresenceFlag(ParkingCarPresenceFlag.PRESENCE.getCode());
		}else {
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

			BigDecimal ratePrice = new BigDecimal(ketuoCardRate.getRuleMoney()).divide(new BigDecimal(100), CARD_RATE_RETAIN_DECIMAL, RoundingMode.HALF_UP);

			checkAndSetOrderPrice(parkingLot, order, ratePrice);

			//正中会对接停车长支持优惠
			order.setPrice(new BigDecimal(order.getPrice().intValue() * 100 - (freeMoney * order.getMonthCount().intValue()))
							.divide(new BigDecimal(100), CARD_RATE_RETAIN_DECIMAL, RoundingMode.HALF_UP));
			order.setOriginalPrice(new BigDecimal(order.getOriginalPrice().intValue() * 100 - (freeMoney * order.getMonthCount().intValue()))
					.divide(new BigDecimal(100), CARD_RATE_RETAIN_DECIMAL, RoundingMode.HALF_UP));
		}

	}

	private boolean openMonthCard(ParkingRechargeOrder order){

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

		Integer payMoney = (order.getPrice().multiply(new BigDecimal(100))).intValue() - Integer.parseInt(ketuoCardRate.getRuleMoney())
				* (order.getMonthCount().intValue() - 1);

		ParkingCardRequest request;
		if (null != order.getCardRequestId()) {
			request = parkingProvider.findParkingCardRequestById(order.getCardRequestId());

		}else {
			request = getParkingCardRequestByOrder(order);
		}

		if(addMonthCard(order, payMoney, request)) {
			Integer count = order.getMonthCount().intValue();

			LOGGER.debug("Parking addMonthCard,count={}", count);

			if(count > 1) {
				ParkingRechargeOrder tempOrder = ConvertHelper.convert(order, ParkingRechargeOrder.class);
				tempOrder.setMonthCount(new BigDecimal(count -1) );
				tempOrder.setPrice(new BigDecimal(((tempOrder.getPrice().multiply(new BigDecimal(100))).intValue() - payMoney))
						.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
				if(rechargeMonthlyCard(order, tempOrder)) {
					updateFlowStatus(request);
					return true;
				}
			}else {
				updateFlowStatus(request);
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

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

		JSONObject param = new JSONObject();
		String plateNumber = tempOrder.getPlateNumber();

		KetuoCard card = getCard(plateNumber);
		String oldValidEnd = card.getValidTo();
		//获取月卡有效时间
		Long expireTime = strToLong(oldValidEnd);

		//计算免费金额
		Integer freeMoney = card.getFreeMoney() * tempOrder.getMonthCount().intValue();
		//充值月数
		int ruleAmount = tempOrder.getMonthCount().intValue();
		//计算充值  有效期的 月数，正常充值时取ruleAmount，
		int addMonthCount = ruleAmount;
//		if(expireTime < now) {
//
//		}

		if(EXPIRE_CUSTOM_RATE_TOKEN.equals(tempOrder.getRateToken())) {
			ParkingLot parkingLot = parkingProvider.findParkingLotById(tempOrder.getParkingLotId());

			addMonthCount = parkingLot.getExpiredRechargeMonthCount();

			Calendar calendar = Calendar.getInstance();
			//获取当前时间
			long now = calendar.getTimeInMillis();
			//过期充值时，如果是当月的最后一天，则需要减1
			if (Utils.isLastDayOfMonth(calendar)) {
				addMonthCount -= 1;
			}

			//当卡过期时，后台设置支持充值时，充值时间从今天开始（不论当前车是否在停车场，只是计算费用不同而已，
			// 参见 getExpiredRate方法计算过期费用，需求6.0 rp）
			String date = sdf2.format(new Date(now)) + " 00:00:00";
			try {
				//下面的Utils.addSecond方法会统一加一秒，所以这里减一秒时间
				expireTime = sdf1.parse(date).getTime() - 1000L;
			} catch (ParseException e) {
				LOGGER.info("date={}, time={}", date, expireTime, e);
			}

			if(parkingLot.getExpiredRechargeType() == ParkingCardExpiredRechargeType.ACTUAL.getCode()) {

				int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				int today = calendar.get(Calendar.DAY_OF_MONTH);
				Integer actualPrice = card.getFreeMoney();
				Integer monthCount = tempOrder.getMonthCount().intValue();
				param.put("freeMoney", actualPrice * (monthCount - 1)
						+ actualPrice * (maxDay - today + 1) / DAY_COUNT);
			}
		}

		//计算月卡充值有效期
		Timestamp tempStart = Utils.addSecond(expireTime, 1);
		Timestamp tempEnd = Utils.getTimestampByAddNatureMonth(expireTime, addMonthCount);
		String validStart = sdf1.format(tempStart);
		String validEnd = sdf1.format(tempEnd);

		param.put("cardId", card.getCardId());
		//修改科托ruleType 固定为1 表示月卡车
		param.put("ruleType", RULE_TYPE);
		param.put("ruleAmount", String.valueOf(ruleAmount));
		// 支付金额（分）
		param.put("payMoney", (tempOrder.getPrice().multiply(new BigDecimal(100))).intValue());
		//续费开始时间 yyyy-MM-dd HH:mm:ss 每月第一天的 0点0分0秒
		param.put("startTime", validStart);
		//续费结束时间 yyyy-MM-dd HH:mm:ss 每月最后一天的23点59分59秒
		param.put("endTime", validEnd);
		if (null != originalOrder.getInvoiceType()) {
			ParkingInvoiceType parkingInvoiceType = parkingProvider.findParkingInvoiceTypeById(originalOrder.getInvoiceType());
			if (null != parkingInvoiceType) {
				param.put("invType", parkingInvoiceType.getInvoiceToken());
			}
		}else {
			param.put("invType", "-1");
		}
		param.put("freeMoney", freeMoney);
		param.put("payType", VendorType.WEI_XIN.getCode().equals(originalOrder.getPaidType()) ? 4 : 5);

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

	private boolean addMonthCard(ParkingRechargeOrder order, Integer money, ParkingCardRequest request){

		JSONObject param = new JSONObject();
		String plateNo = order.getPlateNumber();
		plateNo = plateNo.substring(1, plateNo.length());

		param.put("plateNo", plateNo);
		param.put("money", money);
		param.put("payType", VendorType.WEI_XIN.getCode().equals(order.getPaidType()) ? 4 : 5);

		if (null != request) {
			param.put("userName", request.getPlateOwnerName());
			param.put("userTel", request.getPlateOwnerPhone());
			param.put("company", request.getPlateOwnerEntperiseName());

			if (null != request.getAddressId()) {
				Address address = addressProvider.findAddressById(request.getAddressId());
				if (null != address) {
					param.put("doorplate", address.getAddress());
				}
			}
			if (null != request.getInvoiceType()) {
				order.setInvoiceType(request.getInvoiceType());
				ParkingInvoiceType parkingInvoiceType = parkingProvider.findParkingInvoiceTypeById(request.getInvoiceType());
				if (null != parkingInvoiceType) {
					param.put("invType", parkingInvoiceType.getInvoiceToken());
				}
			}else {
				param.put("invType", "-1");
			}
		}

		String json = post(param, ADD_MONTH_CARD);

		JSONObject jsonObject = JSONObject.parseObject(json);
		Object obj = jsonObject.get("resCode");
		if(null != obj ) {
			int resCode = (int) obj;
			if(resCode == 0) {
				return true;
			}
		}

		//当开卡月数为一时,在这里把开卡周期设置成当前月的最后一天23:59:59
		Calendar calendar = Calendar.getInstance();
		int d = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, d);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		order.setEndPeriod(new Timestamp(calendar.getTimeInMillis()));

		return false;
	}

	@Override
	public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {
		//TODO: 正中会没有临时车
		boolean flag = configProvider.getBooleanValue("parking.order.amount", false);
		if (flag) {
			if (plateNumber.startsWith("粤B")) {
				ParkingTempFeeDTO dto = new ParkingTempFeeDTO();

				dto.setPlateNumber(plateNumber);
				dto.setEntryTime(strToLong("2017-10-27 00:00:00"));
				dto.setPayTime(System.currentTimeMillis());
				dto.setParkingTime(200);
				dto.setDelayTime(15);
				dto.setPrice(new BigDecimal(1000));

				dto.setOrderToken("100");
				return dto;
			}
		}

		return null;
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

		String parkingId = configProvider.getValue("parking.kexing.searchCar.parkId", "");
		String url = configProvider.getValue("parking.kexing.searchCar.url", "");

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

		String appkey = configProvider.getValue("parking.kexing.searchCar.appkey", "");
		String appId = configProvider.getValue("parking.kexing.searchCar.appId", "");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		StringBuilder sb = new StringBuilder();
		Set<Map.Entry<String, Object>> entries = param.entrySet();
		for (Map.Entry entry: entries) {
			sb.append(entry.getValue());
		}

		String str = sb.append(sdf.format(new Date())).append(appkey).toString();
		String key = MD5Utils.getMD5(str);

		JSONObject params = new JSONObject();
		params.put("appId", appId);
		params.put("key", key );

		for (Map.Entry entry: entries) {
			params.put((String)entry.getKey(), entry.getValue());
		}

		return params;
	}

	private List<KetuoParking> getKetuoParkings() {

		String appkey = configProvider.getValue("parking.kexing.searchCar.appkey", "");
		String appId = configProvider.getValue("parking.kexing.searchCar.appId", "");
		String url = configProvider.getValue("parking.kexing.searchCar.url", "");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		String str = sdf.format(new Date()) + appkey;
		String key = MD5Utils.getMD5(str);

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

		String parkingId = configProvider.getValue("parking.kexing.searchCar.parkId", "");
		String url = configProvider.getValue("parking.kexing.searchCar.url", "");

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

	private KetuoCarInfo getKetuoCarInfo(String plateNumber) {

		//TODO：测试
		boolean flag = configProvider.getBooleanValue("parking.order.amount", false);
		if (flag) {
			if (plateNumber.equals("粤B6723M")) {
				KetuoCarInfo carInfo1 = new KetuoCarInfo();
				carInfo1.setPlateNo(plateNumber);
				carInfo1.setEntryTime("2017-09-05 07:48:24");
				carInfo1.setParkingTime(91143);
				return carInfo1;
			}

			if (plateNumber.equals("粤B905DG")) {
				KetuoCarInfo carInfo1 = new KetuoCarInfo();
				carInfo1.setPlateNo(plateNumber);
				carInfo1.setEntryTime("2017-08-05 07:48:24");
				carInfo1.setParkingTime(91143);
				return carInfo1;
			}

			if (plateNumber.equals("粤BK8929")) {
				return null;
			}
		}

		KetuoCarInfo carInfo = null;
		try{
			String parkingId = configProvider.getValue("parking.kexing.searchCar.parkId", "");
			String url = configProvider.getValue("parking.kexing.searchCar.url", "");

			LinkedHashMap<String, Object> param = new LinkedHashMap<>();
			param.put("parkId", parkingId);
			param.put("plateNo", plateNumber);
			param.put("pageIndex", "1");

			JSONObject params = createRequestParam(param);
			String json = Utils.post(url + GET_PARKING_CAR_INFO, params);

			KetuoJsonEntity<KetuoCarInfo> entity = JSONObject.parseObject(json, new TypeReference<KetuoJsonEntity<KetuoCarInfo>>(){});

			if(entity.isSuccess()){
				List<KetuoCarInfo> list = entity.getData();
				if(null != list && !list.isEmpty()) {
					carInfo = list.get(0);
				}
			}
		}catch (Exception e){
			LOGGER.error("Parking request getKetuoCarInfo error", e);
		}

		return carInfo;
	}
}
