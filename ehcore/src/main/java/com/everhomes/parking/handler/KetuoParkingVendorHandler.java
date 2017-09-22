// @formatter:off
package com.everhomes.parking.handler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.everhomes.flow.FlowCase;
import com.everhomes.parking.*;
import com.everhomes.parking.ketuo.*;
import com.everhomes.rest.parking.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.parking.ketuo.KetuoTempFee;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.util.RuntimeErrorException;

/**
 * 停车对接
 */
public class KetuoParkingVendorHandler extends DefaultParkingVendorHandler implements ParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(KetuoParkingVendorHandler.class);

	static final String RECHARGE = "/api/pay/CardRecharge";
	private static final String GET_CARD = "/api/pay/GetCarCardInfo";
	private static final String GET_TYPES = "/api/pay/GetCarTypeList";
	private static final String GET_CARd_RULE = "/api/pay/GetCardRule";
	private static final String GET_TEMP_FEE = "/api/pay/GetParkingPaymentInfo";
	private static final String PAY_TEMP_FEE = "/api/pay/PayParkingFee";
	//只显示ruleType = 1时的充值项
	static final String RULE_TYPE = "1";
	//科托系统：按30天计算
	static final int DAY_COUNT = 30;
	//月租车 : 2
	static final String CAR_TYPE = "2";
    
	@Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
        
    	List<ParkingCardDTO> resultList = new ArrayList<>();

    	KetuoCard card = getCard(plateNumber);

		if(null != card){
			String expireDate = card.getValidTo();
			this.checkExpireDateIsNull(expireDate,plateNumber);

			long expireTime = strToLong(expireDate);

			if (checkExpireTime(parkingLot, expireTime)) {
				return resultList;
			}

			ParkingCardDTO parkingCardDTO = convertCardInfo(parkingLot);

			if (null != card.getName()) {
				String plateOwnerName = card.getName();
				parkingCardDTO.setPlateOwnerName(plateOwnerName);
			}

			parkingCardDTO.setPlateNumber(plateNumber);
			parkingCardDTO.setPlateOwnerPhone("");

			parkingCardDTO.setEndTime(expireTime);
			List<KetuoCardType> types = getCardType();
			for(KetuoCardType kt: types) {
				if(card.getCarType().equals(kt.getCarType())) {
					parkingCardDTO.setCardType(kt.getTypeName());
					break;
				}
			}
			parkingCardDTO.setCardTypeId(card.getCarType());
			parkingCardDTO.setCardNumber(card.getCardId().toString());

			if(null != card.getFreeMoney() && card.getFreeMoney() != 0) {
				parkingCardDTO.setFreeAmount(card.getFreeMoney() / 100 + "元/月");
			}
			if (null != card.getIsAllow()) {
				parkingCardDTO.setIsSupportOnlinePaid(card.getIsAllow().byteValue());
			}

			resultList.add(parkingCardDTO);
		}
        
        return resultList;
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

		return list.stream().map(r -> convertParkingRechargeRateDTO(parkingLot, r)).collect(Collectors.toList());
    }

	void populateRateInfo(String carType, String typeName, List<KetuoCardRate> result) {
		for(KetuoCardRate rate: getCardRule(carType)) {
			if(RULE_TYPE.equals(rate.getRuleType())) {
				rate.setCarType(carType);
				rate.setTypeName(typeName);
				result.add(rate);
			}
		}
	}

	ParkingRechargeRateDTO convertParkingRechargeRateDTO(ParkingLot parkingLot, KetuoCardRate rate) {
		ParkingRechargeRateDTO dto = new ParkingRechargeRateDTO();
		dto.setOwnerId(parkingLot.getOwnerId());
		dto.setOwnerType(parkingLot.getOwnerType());
		dto.setParkingLotId(parkingLot.getId());
		dto.setRateToken(rate.getRuleId());
		Map<String, Object> map = new HashMap<>();
		map.put("count", rate.getRuleAmount());
		String scope = ParkingNotificationTemplateCode.SCOPE;
		int code = ParkingNotificationTemplateCode.DEFAULT_RATE_NAME;
		String locale = Locale.SIMPLIFIED_CHINESE.toString();
		String rateName = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
		dto.setRateName(rateName);
		dto.setCardType(rate.getTypeName());
		dto.setMonthCount(new BigDecimal(rate.getRuleAmount()));
		dto.setPrice(new BigDecimal(rate.getRuleMoney()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
		dto.setVendorName(ParkingLotVendor.KETUO2.getCode());
		return dto;
	}

    @Override
    public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
        if(order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode()))
            return rechargeMonthlyCard(order);
        return payTempCardFee(order);
    }
    
    private void checkExpireDateIsNull(String expireDate,String plateNo) {
		if(StringUtils.isBlank(expireDate)){
			LOGGER.error("ExpireDate is null, plateNo={}", plateNo);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"ExpireDate is null.");
		}
	}
    
	long strToLong(String str) {
		return Utils.strToLong(str, Utils.DateStyle.DATE_TIME);
	}
	
	public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
    	ListCardTypeResponse ret = new ListCardTypeResponse();
    	
        List<ParkingCardType> resultTypes = new ArrayList<>();

		List<KetuoCardType> list = getCardType();
		for(KetuoCardType t: list) {
			ParkingCardType parkingCardType = new ParkingCardType();
			parkingCardType.setTypeId(t.getCarType());
			parkingCardType.setTypeName(t.getTypeName());
			resultTypes.add(parkingCardType);
		}
		ret.setCardTypes(resultTypes);	
    	return ret;
    }
	
	List<KetuoCardType> getCardType() {
		List<KetuoCardType> result = new ArrayList<>();
		JSONObject param = new JSONObject();
		String json = post(param, GET_TYPES);
		KetuoJsonEntity<KetuoCardType> entity = JSONObject.parseObject(json, new TypeReference<KetuoJsonEntity<KetuoCardType>>(){});

		if(entity.isSuccess()){
			if(null != entity.getData()) {
				result = entity.getData();
			}
		}
		
		return result;
	}
	
	List<KetuoCardRate> getCardRule(String carType) {
		List<KetuoCardRate> result = new ArrayList<>();
		JSONObject param = new JSONObject();
		param.put("carType", carType);
		String json = post(param, GET_CARd_RULE);
		
		KetuoJsonEntity<KetuoCardRate> entity = JSONObject.parseObject(json, new TypeReference<KetuoJsonEntity<KetuoCardRate>>(){});
		if(entity.isSuccess()){
			if(null != entity.getData()) {
				result = entity.getData();
			}
		}
		return result;
	}
	
	KetuoCard getCard(String plateNumber) {
		KetuoCard card = null;
		JSONObject param = new JSONObject();
		
		//科托月卡车没有 归属地区分
    	plateNumber = plateNumber.substring(1, plateNumber.length());
		param.put("plateNo", plateNumber);
		String json = post(param, GET_CARD);
        
		KetuoJsonEntity<KetuoCard> entity = JSONObject.parseObject(json, new TypeReference<KetuoJsonEntity<KetuoCard>>(){});
		if(entity.isSuccess()){
			List<KetuoCard> list = entity.getData();
			if(null != list && !list.isEmpty()) {
				card = list.get(0);
			}
		}
        
        return card;
    }
	
    boolean payTempCardFee(ParkingRechargeOrder order){

		JSONObject param = new JSONObject();
		param.put("orderNo", order.getOrderToken());
		param.put("amount", (order.getPrice().multiply(new BigDecimal(100))).intValue());
	    param.put("discount", 0);
	    param.put("payType", VendorType.WEI_XIN.getCode().equals(order.getPaidType())?4:5);
		String json = post(param, PAY_TEMP_FEE);

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

	protected boolean rechargeMonthlyCard(ParkingRechargeOrder order) {

		JSONObject param = new JSONObject();
		String plateNumber = order.getPlateNumber();

		KetuoCard card = getCard(plateNumber);
		String oldValidEnd = card.getValidTo();
		Long expireTime = strToLong(oldValidEnd);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Timestamp tempStart = Utils.addSecond(expireTime, 1);
		Timestamp tempEnd = Utils.getTimestampByAddNatureMonth(expireTime, order.getMonthCount().intValue());
		String validStart = sdf1.format(tempStart);
		String validEnd = sdf1.format(tempEnd);

		param.put("cardId", card.getCardId());
		//修改科托ruleType 固定为1 表示月卡车
		param.put("ruleType", RULE_TYPE);
		param.put("ruleAmount", String.valueOf(order.getMonthCount().intValue()));
		// 支付金额（分）
		param.put("payMoney", (order.getPrice().multiply(new BigDecimal(100))).intValue());
		//续费开始时间 yyyy-MM-dd HH:mm:ss 每月第一天的 0点0分0秒
		param.put("startTime", validStart);
		//续费结束时间 yyyy-MM-dd HH:mm:ss 每月最后一天的23点59分59秒
		param.put("endTime", validEnd);

		String json = post(param, RECHARGE);

		//将充值信息存入订单
		order.setErrorDescriptionJson(json);
        order.setStartPeriod(tempStart);
		order.setEndPeriod(tempEnd);

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

	protected String post(JSONObject param, String type) {

		KetuoRequestConfig config = getKetuoRequestConfig();
		String url = config.getUrl() + type;
		String key = config.getKey();
		String user = config.getUser();
		String pwd = config.getPwd();

		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

		String iv = sdf2.format(new Date());
        String data = null;
		try {
			data = EncryptUtil.getEncString(param, key, iv);
		} catch (Exception e) {
			LOGGER.error("Parking encrypt param error, param={}, key={}, iv={}", param, key, iv, e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Parking encrypt param error.");
		}

		Map<String, String> params = new HashMap<>();
		params.put("data", data);
		Map<String, String> headers = new HashMap<>();
		headers.put("user", user);
		headers.put("pwd", pwd);

		LOGGER.info("Parking info, namespace={}", this.getClass().getName());
		return Utils.post(url, params, headers);
	}

	@Override
	public void updateParkingRechargeOrderRate(ParkingRechargeOrder order) {
		String plateNumber = order.getPlateNumber();

		KetuoCard cardInfo = getCard(plateNumber);
		KetuoCardRate ketuoCardRate = null;
		String cardType = CAR_TYPE;

		if(null != cardInfo) {
			long expireTime = strToLong(cardInfo.getValidTo());
			ParkingLot parkingLot = parkingProvider.findParkingLotById(order.getParkingLotId());
			if (!checkExpireTime(parkingLot, expireTime)) {
				cardType = cardInfo.getCarType();
			}
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
	}

	private KetuoTempFee getTempFee(String plateNumber) {
		KetuoTempFee tempFee = null;
		JSONObject param = new JSONObject();
		param.put("plateNo", plateNumber);
		String json = post(param, GET_TEMP_FEE);
        
		KetuoJsonEntity<KetuoTempFee> entity = JSONObject.parseObject(json, new TypeReference<KetuoJsonEntity<KetuoTempFee>>(){});
		if(entity.isSuccess()){
			List<KetuoTempFee> list = entity.getData();
			if(null != list && !list.isEmpty()) {
				tempFee = list.get(0);
			}
		}
        return tempFee;
    }

	@Override
	public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {
		KetuoTempFee tempFee = getTempFee(plateNumber);
		
		ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
		if(null == tempFee) {
			return dto;
		}
		dto.setPlateNumber(plateNumber);
		dto.setEntryTime(strToLong(tempFee.getEntryTime()));
		dto.setPayTime(strToLong(tempFee.getPayTime()));
		dto.setParkingTime(tempFee.getElapsedTime());
		dto.setDelayTime(tempFee.getDelayTime());
		dto.setPrice(new BigDecimal(tempFee.getPayable()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));

		dto.setOrderToken(tempFee.getOrderNo());
		return dto;
	}

	@Override
	public OpenCardInfoDTO getOpenCardInfo(GetOpenCardInfoCommand cmd) {

		ParkingCardRequest parkingCardRequest = parkingProvider.findParkingCardRequestById(cmd.getParkingRequestId());

		FlowCase flowCase = flowCaseProvider.getFlowCaseById(parkingCardRequest.getFlowCaseId());

		ParkingFlow parkingFlow = parkingProvider.getParkingRequestCardConfig(cmd.getOwnerType(), cmd.getOwnerId(), 
				cmd.getParkingLotId(), flowCase.getFlowMainId());

		Integer requestMonthCount = REQUEST_MONTH_COUNT;
		Byte requestRechargeType = REQUEST_RECHARGE_TYPE;
		
		if(null != parkingFlow) {
			requestMonthCount = parkingFlow.getRequestMonthCount();
			requestRechargeType = parkingFlow.getRequestRechargeType();
		}
		
		OpenCardInfoDTO dto = new OpenCardInfoDTO();

		//月租车
		List<KetuoCardRate> rates = getCardRule(CAR_TYPE);
		if(null != rates && !rates.isEmpty()) {
			
			KetuoCardRate rate = null;
			for(KetuoCardRate r: rates) {
				if(Integer.valueOf(r.getRuleAmount()) == 1) {
					rate = r;
					break;
				}
			}

			if (null == rate) {
				//TODO:
				return null;
			}

			List<KetuoCardType> types = getCardType();
			String typeName = null;
			for(KetuoCardType kt: types) {
				if(CAR_TYPE.equals(kt.getCarType())) {
					typeName = kt.getTypeName();
					break;
				}
			}

			dto.setOwnerId(cmd.getOwnerId());
			dto.setOwnerType(cmd.getOwnerType());
			dto.setParkingLotId(cmd.getParkingLotId());
			dto.setRateToken(rate.getRuleId());
			Map<String, Object> map = new HashMap<String, Object>();
		    map.put("count", rate.getRuleAmount());
			String scope = ParkingNotificationTemplateCode.SCOPE;
			int code = ParkingNotificationTemplateCode.DEFAULT_RATE_NAME;
			String locale = "zh_CN";
			String rateName = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
			dto.setRateName(rateName);
			dto.setCardType(typeName);
			dto.setMonthCount(new BigDecimal(rate.getRuleAmount()));
			dto.setPrice(new BigDecimal(rate.getRuleMoney()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));

			dto.setPlateNumber(cmd.getPlateNumber());
			long now = System.currentTimeMillis();
			dto.setOpenDate(now);
			dto.setExpireDate(Utils.getLongByAddNatureMonth(now, requestMonthCount));
			if(requestRechargeType == ParkingCardExpiredRechargeType.ALL.getCode()) {
				dto.setPayMoney(dto.getPrice().multiply(new BigDecimal(requestMonthCount)));
			}else {
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(now);
				int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				int today = calendar.get(Calendar.DAY_OF_MONTH);

				BigDecimal price = dto.getPrice().multiply(new BigDecimal(requestMonthCount-1))
						.add(dto.getPrice().multiply(new BigDecimal(maxDay-today+1))
								.divide(new BigDecimal(DAY_COUNT), 2, RoundingMode.HALF_UP));
				dto.setPayMoney(price);
			}

			dto.setOrderType(ParkingOrderType.OPEN_CARD.getCode());
		}

		return dto;
	}

	protected KetuoRequestConfig getKetuoRequestConfig() {
		return null;
	}

}
