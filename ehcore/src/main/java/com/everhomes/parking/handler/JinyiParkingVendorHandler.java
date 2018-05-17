// @formatter:off
package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.parking.*;
import com.everhomes.parking.clearance.ParkingClearanceLog;
import com.everhomes.parking.jinyi.JinyiCard;
import com.everhomes.parking.jinyi.JinyiClearance;
import com.everhomes.parking.jinyi.JinyiJsonEntity;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.*;
import com.everhomes.rest.parking.clearance.ParkingActualClearanceLogDTO;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.MD5Utils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 清华信息港 停车
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "JIN_YI")
public class JinyiParkingVendorHandler extends DefaultParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(JinyiParkingVendorHandler.class);

	private static final String GET_CARD = "parkingjet.open.s2s.parkingfee.month.calcfee.plateno";
	private static final String CREATE_ORDER = "parkingjet.open.s2s.parkingfee.month.order.create";
	private static final String NOTIFY = "parkingjet.open.s2s.parkingfee.month.payresult.notify";
	//申请短期证
	private static final String APPLY_TEMP_CARD = "parkingjet.open.s2s.shorttermcard.apply.order";
	//获取短期证车辆的通行纪录
	private static final String GET_TEMP_CARD_LOGS = "parkingjet.open.s2s.shorttermcard.apply.order.parkingrecord";
	//赠送全免券
	private static final String COUPON_FREE_SEND = "parkingjet.open.s2s.coupon.free.send";

	//获取七天范围的通讯记录,时间限制为半年
	private static final String PARKING_RECORD_SEARCH = "parkingjet.open.s2s.parkingrecord.search";

	//金溢初始一个月
	private static final int MONTH_COUNT = 1;

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	DateTimeFormatter ymd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
        
    	List<ParkingCardDTO> resultList = new ArrayList<>();

		JinyiCard card = getCardInfo(plateNumber);
    	
        ParkingCardDTO parkingCardDTO = new ParkingCardDTO();
		if(null != card){
			//格式yyyy-MM-dd
			String expiredate = card.getMaxuseddate() + " 23:59:59";
			LocalDateTime time = LocalDateTime.parse(expiredate, dtf2);
			Long endTime = Timestamp.valueOf(time).getTime();

			setCardStatus(parkingLot, endTime, parkingCardDTO);

			String plateOwnerName = card.getOwnername();


			ParkingCardType parkingCardType = createDefaultCardType();
			String cardType = parkingCardType.getTypeName();
			
			parkingCardDTO.setOwnerType(parkingLot.getOwnerType());
			parkingCardDTO.setOwnerId(parkingLot.getOwnerId());
			parkingCardDTO.setParkingLotId(parkingLot.getId());
			
			parkingCardDTO.setPlateOwnerName(plateOwnerName);
			parkingCardDTO.setPlateNumber(card.getPlateno());
			parkingCardDTO.setEndTime(endTime);
			parkingCardDTO.setCardType(cardType);
			//金溢 没有返回卡号
//			parkingCardDTO.setCardNumber();
			parkingCardDTO.setIsValid(true);
			
			resultList.add(parkingCardDTO);
		}
        return resultList;
    }

    private JinyiCard getCardInfo(String plateNumber){

		Map<String, String> params = createGeneralParam(GET_CARD, createGetCardParam(plateNumber));

		String url = configProvider.getValue("parking.zijing.url", "");
		String responseJson = Utils.post(url, params);


		JinyiJsonEntity<JinyiCard> jsonEntity = JSONObject.parseObject(responseJson, new TypeReference<JinyiJsonEntity<JinyiCard>>(){});

		if(jsonEntity.isSuccess()) {
			return jsonEntity.getData();
		}

		return null;
    }

    private Map<String, String> createGeneralParam(String methodName, JSONObject json) {

		String appid = configProvider.getValue("parking.zijing.appid", "");
		String appkey = configProvider.getValue("parking.zijing.appkey", "");

		Map<String, String> params = new HashMap<>();
		params.put("methodname", methodName);
		params.put("appid", appid);

		LocalDateTime localDateTime = LocalDateTime.now();
		String timestamp = localDateTime.format(dtf);

		params.put("timestamp", timestamp);
		params.put("version", "1.0");
		params.put("postdata", json.toJSONString());

		List<String> keys = new ArrayList<>();
		keys.addAll(params.keySet());
		Collections.sort(keys);

		StringBuilder sb = new StringBuilder();
		keys.forEach(k -> {
			sb.append(k).append("=").append(params.get(k)).append("&");

		});
		String p = sb.substring(0,sb.length() - 1);

		String md5Sign = MD5Utils.getMD5(p + appkey);
		params.put("sign", md5Sign);

		return params;
	}

	private JSONObject createGetCardParam(String plateNo) {

		String parkingid = configProvider.getValue("parking.zijing.parkingid", "");

		JSONObject json = new JSONObject();
		json.put("parkingid", parkingid);
		json.put("plateno", plateNo);
		json.put("quantity", "1");
		return json;
	}

	private JSONObject createRechargeParam(ParkingRechargeOrder order) {

		String parkingid = configProvider.getValue("parking.zijing.parkingid", "");

		JSONObject json = new JSONObject();
		json.put("parkingid", parkingid);
		json.put("orderid", order.getOrderToken());
		json.put("transid", order.getOrderNo());
		json.put("paidin", order.getPrice());
		json.put("discount", new BigDecimal(0));
		json.put("returninfo", "");
		LocalDateTime localDateTime = LocalDateTime.now();
		String payTime = localDateTime.format(dtf2);
		json.put("paytime", payTime);
		//支付状态 1 成功 2 失败
		json.put("status", "1");

		return json;
	}

//	public static void main(String[] args) {
//		JinyiParkingVendorHandler handler = new JinyiParkingVendorHandler();
//		ParkingClearanceLog log = new ParkingClearanceLog();
//		log.setClearanceTime(new Timestamp(1521514862000L));
//		log.setPlateNumber("湘UK86532");
//		handler.getTempCardLogs(log);
//	}

	private JSONObject createOrderParam(ParkingRechargeOrder order) {

		String parkingid = configProvider.getValue("parking.zijing.parkingid", "");

		JSONObject json = new JSONObject();
		json.put("parkingid", parkingid);
		json.put("plateno", order.getPlateNumber());
		json.put("receivable", order.getPrice());
		json.put("calcid", order.getOrderToken());
		//1209 :微信 1210 :支付宝
		json.put("paymenttype", VendorType.ZHI_FU_BAO.getCode().equals(order.getPaidType()) ? 1210 : 1209);

		return json;
	}

	@Override
	public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
		if(order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode())) {
			return rechargeMonthlyCard(order);
		}
		return false;
	}

	private boolean rechargeMonthlyCard(ParkingRechargeOrder order){

		String url = configProvider.getValue("parking.zijing.url", "");

		JinyiCard card = getCardInfo(order.getPlateNumber());


		if (null != card) {

			String startTime = card.getMaxuseddate() + " 23:59:59";

			//根据查询月卡时的计费id来创建订单
			order.setOrderToken(card.getCalcid());

			//金溢停车先创建订单，然后根据订单id来充值
			Map<String, String> createOrderParams = createGeneralParam(CREATE_ORDER, createOrderParam(order));

			String responseJson = Utils.post(url, createOrderParams);

			JinyiJsonEntity<String> jsonEntity = JSONObject.parseObject(responseJson,
					new TypeReference<JinyiJsonEntity<String>>() {});

			if (jsonEntity.isSuccess()) {
				//根据创建订单返回的id去充值
				String orderId = jsonEntity.getData();

				order.setOrderToken(orderId);
				Map<String, String> notifyParams = createGeneralParam(NOTIFY, createRechargeParam(order));

				String notifyJson = Utils.post(url, notifyParams);

				JinyiJsonEntity<Object> notifyEntity = JSONObject.parseObject(notifyJson,
						new TypeReference<JinyiJsonEntity<Object>>(){});

				//将充值信息存入订单
				JinyiCard newCard = getCardInfo(order.getPlateNumber());

				if (null != newCard) {
					String endTime = newCard.getMaxuseddate() + " 23:59:59";
					LocalDateTime tempStartTime = LocalDateTime.parse(startTime, dtf2);
					tempStartTime = tempStartTime.plusSeconds(1L);
					LocalDateTime tempEndTime = LocalDateTime.parse(endTime, dtf2);

					order.setErrorDescriptionJson(notifyJson);

					order.setStartPeriod(Timestamp.valueOf(tempStartTime));
					order.setEndPeriod(Timestamp.valueOf(tempEndTime));
				}

				if(notifyEntity.isSuccess()) {
					return true;
				}
			}

		}

		return false;
	}


	public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
    	ListCardTypeResponse ret = new ListCardTypeResponse();

		List<ParkingCardType> list = new ArrayList<>();
		list.add(createDefaultCardType());
		ret.setCardTypes(list);

    	return ret;
    }

	public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {

		JinyiCard card = getCardInfo(order.getPlateNumber());

		BigDecimal ratePrice = card.getPaidin();

		checkAndSetOrderPrice(parkingLot, order, ratePrice);

	}

    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot,String plateNumber,String cardNo) {
    	
    	List<ParkingRechargeRateDTO> parkingRechargeRateList = new ArrayList<>();

    	if(!StringUtils.isBlank(plateNumber)) {
    		JinyiCard card = getCardInfo(plateNumber);

    		if (null != card) {
				ParkingRechargeRateDTO rate = new ParkingRechargeRateDTO();
				rate.setOwnerId(parkingLot.getOwnerId());
				rate.setOwnerType(parkingLot.getOwnerType());
				rate.setParkingLotId(parkingLot.getId());
				rate.setRateToken("");

				Map<String, Object> map = new HashMap<>();
				map.put("count", MONTH_COUNT);
				String scope = ParkingNotificationTemplateCode.SCOPE;
				int code = ParkingNotificationTemplateCode.DEFAULT_RATE_NAME;
				String locale = getLocale();
				String rateName = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
				rate.setRateName(rateName);

				ParkingCardType parkingCardType = createDefaultCardType();
				rate.setCardTypeId(parkingCardType.getTypeId());
				rate.setCardType(parkingCardType.getTypeName());
				rate.setMonthCount(new BigDecimal(MONTH_COUNT));
				rate.setPrice(card.getPaidin());

				parkingRechargeRateList.add(rate);
			}
    	}

		return parkingRechargeRateList;
    }

	private String getLocale() {
		User user = UserContext.current().getUser();
		if(user != null && user.getLocale() != null)
			return user.getLocale();

		return Locale.SIMPLIFIED_CHINESE.toString();
	}

	@Override
	public String applyTempCard(ParkingClearanceLog log) {

		Map<String, String> params = createGeneralParam(APPLY_TEMP_CARD, createApplyTempCardParam(log));
		String url = configProvider.getValue("parking.zijing.url", "");
		String responseJson = Utils.post(url, params);


		JinyiJsonEntity<String> jsonEntity = JSONObject.parseObject(responseJson, new TypeReference<JinyiJsonEntity<String>>(){});

		if(jsonEntity.isSuccess()) {
			return jsonEntity.getData();
		}else{//失败的情况是车辆在场了，这时候发全免券
			LOGGER.error("APPLY_TEMP_CARD failed, try COUPON_FREE_SEND! log id = {}, plateno = {}",log.getId(),log.getPlateNumber());
			params = createGeneralParam(COUPON_FREE_SEND,createCouponFreeSendParam(log));
			responseJson = Utils.post(url, params);
			jsonEntity = JSONObject.parseObject(responseJson, new TypeReference<JinyiJsonEntity<String>>(){});
			if(jsonEntity.isSuccess()){
				return jsonEntity.getData();
			}
			//这里失败，表示在场车辆已经有发全免券了。
			return jsonEntity.getMessage();

		}

//		return null;
	}

	private JSONObject createCouponFreeSendParam(ParkingClearanceLog log) {
		JSONObject json = new JSONObject();
		String parkingid = configProvider.getValue("parking.zijing.parkingid", "");
		String marketid  = configProvider.getValue("parking.zijing.marketid", "");
		String userid  = configProvider.getValue("parking.zijing.userid", "");

		json.put("parkingid", parkingid);
		json.put("marketid", marketid);
		json.put("plateno", log.getPlateNumber());
		json.put("userid", userid);
		json.put("remark ", log.getRemarks());

		return json;
	}

	@Override
	public List<ParkingActualClearanceLogDTO> getTempCardLogs(ParkingClearanceLog log) {

//		Map<String, String> params = createGeneralParam(GET_TEMP_CARD_LOGS, createGetTempCardLogsParam(log.getLogToken()));
		Map<String, String> params = createGeneralParam(PARKING_RECORD_SEARCH, createParkingRecordSearch(log));
		String url = configProvider.getValue("parking.zijing.url", "");
		String responseJson = Utils.post(url, params);


		JinyiJsonEntity<List<JinyiClearance>> jsonEntity = JSONObject.parseObject(responseJson, new TypeReference<JinyiJsonEntity<List<JinyiClearance>>>(){});

		if(jsonEntity.isSuccess()) {
			//存入申请记录中
			log.setLogJson(responseJson);
			List<JinyiClearance> actualLogs = jsonEntity.getData();
			if (null != actualLogs) {
				return actualLogs.stream().map(this::convertActualClearanceLogDTO).collect(Collectors.toList());
			}
		}
		return null;
	}

	private ParkingActualClearanceLogDTO convertActualClearanceLogDTO(JinyiClearance jinyiClearance) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		ParkingActualClearanceLogDTO dto = new ParkingActualClearanceLogDTO();
		if (null != jinyiClearance.getEntrytime()) {
			LocalDateTime entryTime = LocalDateTime.parse(jinyiClearance.getEntrytime(), dtf);
			dto.setEntryTime(Timestamp.valueOf(entryTime));
		}
		if (null != jinyiClearance.getExittime()) {
			LocalDateTime exitTime = LocalDateTime.parse(jinyiClearance.getExittime(), dtf);
			dto.setExitTime(Timestamp.valueOf(exitTime));
		}

		return dto;
	}

	private JSONObject createApplyTempCardParam(ParkingClearanceLog log) {
		JSONObject json = new JSONObject();
		String parkingid = configProvider.getValue("parking.zijing.parkingid", "");
		json.put("parkingid", parkingid);
		json.put("plateno", log.getPlateNumber());
		json.put("thirddataid", log.getId());
		Timestamp clearanceTime = log.getClearanceTime();
		LocalDateTime start = clearanceTime.toLocalDateTime();

		LocalDateTime end = clearanceTime.toLocalDateTime();
		//加一天 减一秒
		end = end.plusDays(1L);
		end = end.minusSeconds(1L);
		json.put("effectivedate", start.format(dtf2));
		json.put("expiredate", end.format(dtf2));
		return json;
	}

//	private JSONObject createGetTempCardLogsParam(String logToken) {
//		JSONObject json = new JSONObject();
//		json.put("pj_shorttermcardid", logToken);
//
//		return json;
//	}

	private JSONObject createParkingRecordSearch(ParkingClearanceLog log) {
		JSONObject json = new JSONObject();
		String parkingid = configProvider.getValue("parking.zijing.parkingid", "");
		json.put("parkingid", parkingid);
		json.put("plateno", log.getPlateNumber());
		LocalDateTime clearanceTime = log.getClearanceTime().toLocalDateTime();
		String ymdhms = clearanceTime.format(ymd);
		json.put("entrytime1", ymdhms+" 00:00:00");
		json.put("entrytime2", ymdhms+" 23:59:59");
		return json;
	}
}
