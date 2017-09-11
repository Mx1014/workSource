// @formatter:off
package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.parking.*;
import com.everhomes.parking.innospring.InnoSpringCardInfo;
import com.everhomes.parking.innospring.InnoSpringCardRate;
import com.everhomes.parking.innospring.InnoSpringCardType;
import com.everhomes.parking.innospring.InnoSpringTempFee;
import com.everhomes.rest.parking.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 创源 停车
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "INNOSPRING")
public class InnoSpringParkingVendorHandler extends DefaultParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(InnoSpringParkingVendorHandler.class);

	private static final String RECHARGE = "70111002";
	private static final String GET_CARD = "70111003";
	private static final String GET_RATES = "70111001";
	private static final String GET_TEMP_FEE = "70111005";
	private static final String PAY_TEMP_FEE = "70111004";

	@Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {

    	List<ParkingCardDTO> resultList = new ArrayList<>();

		InnoSpringCardInfo card = getCard(plateNumber);

        ParkingCardDTO parkingCardDTO = new ParkingCardDTO();
		if(null != card){
			String expireDate = card.getEnd_time();
			this.checkExpireDateIsNull(expireDate,plateNumber);
			//有效期到当天23点59分59秒
			long expireTime = strToLong(expireDate + "235959");
			if (checkExpireTime(parkingLot, expireTime)) {
				return resultList;
			}
			parkingCardDTO.setOwnerType(parkingLot.getOwnerType());
			parkingCardDTO.setOwnerId(parkingLot.getOwnerId());
			parkingCardDTO.setParkingLotId(parkingLot.getId());

			parkingCardDTO.setPlateNumber(plateNumber);
			parkingCardDTO.setPlateOwnerPhone("");
			parkingCardDTO.setEndTime(expireTime);

			InnoSpringCardType cardType = createDefaultCardType();
			parkingCardDTO.setCardTypeId(cardType.getCardTypeId());
			parkingCardDTO.setCardType(cardType.getCardTypeName());
			parkingCardDTO.setCardNumber(card.getCar_id());
			parkingCardDTO.setIsValid(true);

			resultList.add(parkingCardDTO);
		}

        return resultList;
    }

    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot,String plateNumber,String cardNo) {
    	List<ParkingRechargeRateDTO> result;

		List<InnoSpringCardRate> rates = getCardRule();
		InnoSpringCardType cardType = createDefaultCardType();
    	result = rates.stream().map( r -> {
			ParkingRechargeRateDTO dto = new ParkingRechargeRateDTO();
			dto.setOwnerType(parkingLot.getOwnerType());
			dto.setOwnerId(parkingLot.getOwnerId());
			dto.setParkingLotId(parkingLot.getId());
			dto.setRateToken("");
			Integer monthCount = convertCardType(r.getCard_type());
			Map<String, Object> map = new HashMap<>();
		    map.put("count", monthCount);
			String scope = ParkingNotificationTemplateCode.SCOPE;
			int code = ParkingNotificationTemplateCode.DEFAULT_RATE_NAME;
			String locale = getLocale();
			String rateName = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
			dto.setRateName(rateName);
			dto.setCardType(cardType.getCardTypeName());
			dto.setMonthCount(new BigDecimal(monthCount));
			dto.setPrice(new BigDecimal(r.getFee()));
			dto.setVendorName(ParkingLotVendor.INNOSPRING.getCode());
			return dto;
		}).collect(Collectors.toList());

		return result;
    }

	private String getLocale() {
		User user = UserContext.current().getUser();
		if(user != null && user.getLocale() != null)
			return user.getLocale();

		return Locale.SIMPLIFIED_CHINESE.toString();
	}

    private Integer convertCardType(String cardType) {
		switch (cardType) {
			case "1": return 1;
			case "2": return 3;
			case "3": return 6;
			case "4": return 12;
			default:  return 1;
		}
	}

    @Override
    public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
		if(order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode()))
			return rechargeMonthlyCard(order);
		return payTempCardFee(order);
    }

    @Override
    public ParkingRechargeRateDTO createParkingRechargeRate(CreateParkingRechargeRateCommand cmd){
    	LOGGER.error("Not support create parkingRechageRate.");
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE,
				"Not support create parkingRechageRate.");
    }

    @Override
    public void deleteParkingRechargeRate(DeleteParkingRechargeRateCommand cmd){
    	LOGGER.error("Not support delete parkingRechageRate.");
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE,
				"Not support delete parkingRechageRate.");
    }

    private void checkExpireDateIsNull(String expireDate,String plateNo) {
		if(StringUtils.isBlank(expireDate)){
			LOGGER.error("ExpireDate is null, plateNo={}", plateNo);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"ExpireDate is null.");
		}
	}

    private long strToLong(String str) {
    	return Utils.strToLong(str, Utils.DateStyle.DATE_TIME_STR);
	}

	public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
    	ListCardTypeResponse ret = new ListCardTypeResponse();

        List<ParkingCardType> resultTypes = new ArrayList<>();

		List<InnoSpringCardType> list = getCardType();
		list.forEach(c -> {
			ParkingCardType parkingCardType = new ParkingCardType();
			parkingCardType.setTypeId(c.getCardTypeId());
			parkingCardType.setTypeName(c.getCardTypeName());
			resultTypes.add(parkingCardType);
		});

		ret.setCardTypes(resultTypes);
    	return ret;
    }

	private List<InnoSpringCardType> getCardType() {
		List<InnoSpringCardType> result = new ArrayList<>();
		result.add(createDefaultCardType());
		return result;
	}

	private InnoSpringCardType createDefaultCardType() {
		//创源停车 没有月卡类型，默认一个月卡类型
		InnoSpringCardType cardType = new InnoSpringCardType();
		cardType.setCardTypeId("月卡");
		cardType.setCardTypeName("月卡");

		return cardType;
	}

	private List<InnoSpringCardRate> getCardRule() {
		List<InnoSpringCardRate> result = new ArrayList<>();
		JSONObject param = new JSONObject();
		String version = configProvider.getValue("parking.innospring.version", "");
		String licensekey = configProvider.getValue("parking.innospring.licensekey", "");
		String parkName = configProvider.getValue("parking.innospring.parkName", "");
		param.put("version", version);
		param.put("licensekey", licensekey);
		param.put("park_name", parkName);

		String json = post(createRequestParam(GET_RATES, param));

		String entityJson = parseJson(json);

		if(null != entityJson) {
			result = JSONArray.parseArray(entityJson, InnoSpringCardRate.class);
		}
		return result;
	}

	private JSONObject createRequestParam(String type, JSONObject command) {

		JSONObject header = new JSONObject();
		header.put("SERVICE_ID", type);

		JSONObject childParam = new JSONObject();
		childParam.put("REQ_MSG_HDR", header);
		childParam.put("REQ_COMM_DATA", command);

		JSONArray array = new JSONArray();
		array.add(childParam);

		JSONObject param = new JSONObject();
		param.put("REQUESTS",array);

		LOGGER.info("param={}", param);

		return param;
	}

	private InnoSpringCardInfo getCard(String plateNumber) {
		InnoSpringCardInfo card = null;

		JSONObject param = new JSONObject();
		String version = configProvider.getValue("parking.innospring.version", "");
		String licensekey = configProvider.getValue("parking.innospring.licensekey", "");
		param.put("version", version);
		param.put("licensekey", licensekey);
		param.put("car_id", plateNumber);

		String json = post(createRequestParam(GET_CARD, param));

		String entityJson = parseJson(json);

		if(null != entityJson) {
			List<InnoSpringCardInfo> list = JSONArray.parseArray(entityJson, InnoSpringCardInfo.class);
			card = bubble(list);
		}

        return card;
    }

	private InnoSpringCardInfo bubble(List<InnoSpringCardInfo> list) {
		int size = list.size();
		if (size == 0)
			return null;
		for (int i = size - 1; i > 0 ; i--) {
			for (int j = 0; j < i; j++) {
				if (Long.valueOf(list.get(j).getEnd_time()) > Long.valueOf(list.get(j+1).getEnd_time())) {
					InnoSpringCardInfo temp = list.get(j);
					list.set(j, list.get(j+1));
					list.set(j+1, temp);
				}
			}
		}
		return list.get(size - 1);
	}

	private String parseJson(String json) {
		JSONObject obj = JSONObject.parseObject(json);
		JSONArray arr = obj.getJSONArray("ANSWERS");
		JSONObject data = arr.getJSONObject(0);
		String commData = data.getString("ANS_COMM_DATA");
		JSONObject headData = data.getJSONObject("ANS_MSG_HDR");
		if ("0".equals(headData.getString("MSG_CODE"))) {
			return commData;
		}
		return null;
	}

	private boolean rechargeMonthlyCard(ParkingRechargeOrder order){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		JSONObject param = new JSONObject();

		String plateNumber = order.getPlateNumber();

		InnoSpringCardInfo card = getCard(plateNumber);
		String oldValidEnd = card.getEnd_time();
		Long time = strToLong(oldValidEnd + "235959");
		Timestamp newTime = Utils.addSecond(time, 1);
		String validStart = sdf.format(newTime);

		String version = configProvider.getValue("parking.innospring.version", "");
		String licensekey = configProvider.getValue("parking.innospring.licensekey", "");
		String parkName = configProvider.getValue("parking.innospring.parkName", "");
		param.put("version", version);
		param.put("licensekey", licensekey);
		param.put("car_id", order.getPlateNumber());
	    param.put("park_name", parkName);
	    param.put("start_date", validStart);
	    param.put("buy_num", String.valueOf(order.getMonthCount().intValue()));

		JSONObject requestParam = createRequestParam(RECHARGE, param);

		String json = post(requestParam);

		//将充值信息存入订单
		order.setErrorDescriptionJson(json);
		order.setStartPeriod(newTime);
		order.setEndPeriod(Utils.getTimestampByAddNatureMonth(time, order.getMonthCount().intValue()));

		String entityJson = parseJson(json);

		if(null != entityJson) {
			JSONArray arr = JSONArray.parseArray(entityJson);
			JSONObject obj = arr.getJSONObject(0);
			String ret = obj.getString("ret");
			if ("1".equals(ret)) {
				return true;
			}
		}
		return false;
    }

	private boolean payTempCardFee(ParkingRechargeOrder order){
		String version = configProvider.getValue("parking.innospring.version", "");
		String licensekey = configProvider.getValue("parking.innospring.licensekey", "");
		JSONObject param = new JSONObject();
		param.put("version", version);
		param.put("licensekey", licensekey);
	    param.put("car_id", order.getPlateNumber());
	    param.put("amt", order.getPrice().setScale(2, RoundingMode.HALF_UP));

		JSONObject newParam = createRequestParam(PAY_TEMP_FEE, param);
		String json = post(newParam);

		String entityJson = parseJson(json);

		if(null != entityJson) {
			JSONArray arr = JSONArray.parseArray(entityJson);
			JSONObject obj = arr.getJSONObject(0);
			String ret = obj.getString("ret");
			if ("1".equals(ret)) {
				return true;
			}
		}
		return false;
    }

	public String post(JSONObject param) {

		LOGGER.info("Parking info, namespace={}", this.getClass().getName());

		String serverUrl = configProvider.getValue("parking.innospring.serverUrl", "");
		return Utils.post(serverUrl, param);
	}

	private InnoSpringTempFee getTempFee(String plateNumber) {
		InnoSpringTempFee tempFee = null;
		String version = configProvider.getValue("parking.innospring.version", "");
		String licensekey = configProvider.getValue("parking.innospring.licensekey", "");
		JSONObject param = new JSONObject();
		param.put("version", version);
		param.put("licensekey", licensekey);
		param.put("car_id", plateNumber);

		JSONObject newParam = createRequestParam(GET_TEMP_FEE, param);
		String json = post(newParam);

		String entityJson = parseJson(json);

		if(null != entityJson) {
			List<InnoSpringTempFee> list = JSONArray.parseArray(entityJson, InnoSpringTempFee.class);
			tempFee = list.get(0);
		}
		return tempFee;
	}

	@Override
	public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {
		InnoSpringTempFee tempFee = getTempFee(plateNumber);

		ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
		if(null == tempFee)
			return dto;
		dto.setPlateNumber(plateNumber);
		dto.setEntryTime(strToLong(tempFee.getEntry_time()));
		dto.setPayTime(strToLong(tempFee.getPay_time()));
		dto.setParkingTime(Integer.valueOf(tempFee.getElapsed_time()));
		dto.setDelayTime(Integer.valueOf(tempFee.getDelay_time()));
		dto.setPrice(new BigDecimal(tempFee.getPayable()));
		dto.setOrderToken(tempFee.getOrder_no());
		return dto;
	}

}
