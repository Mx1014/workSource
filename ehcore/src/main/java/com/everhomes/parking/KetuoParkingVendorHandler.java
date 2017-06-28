// @formatter:off
package com.everhomes.parking;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.everhomes.parking.ketuo.*;
import com.everhomes.rest.parking.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.parking.ketuo.KetuoTempFee;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.util.RuntimeErrorException;

@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "KETUO")
public class KetuoParkingVendorHandler implements ParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(KetuoParkingVendorHandler.class);
	
	private ThreadLocal<SimpleDateFormat> timeFormat = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};
	private ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyyMMdd");
		}
	};

	private static final String RECHARGE = "/api/pay/CardRecharge";
	private static final String GET_CARD = "/api/pay/GetCarCardInfo";
	private static final String GET_TYPES = "/api/pay/GetCarTypeList";
	private static final String GET_CARd_RULE = "/api/pay/GetCardRule";
	private static final String GET_TEMP_FEE = "/api/pay/GetParkingPaymentInfo";
	private static final String PAY_TEMP_FEE = "/api/pay/PayParkingFee";
	private static final String RULE_TYPE = "1"; //只显示ruleType = 1时的充值项

	@Autowired
	private ParkingProvider parkingProvider;
	@Autowired
	private LocaleTemplateService localeTemplateService;
	@Autowired
    private ConfigurationProvider configProvider;
	
	@Override
    public GetParkingCardsResponse getParkingCardsByPlate(String ownerType, Long ownerId,
    		Long parkingLotId, String plateNumber) {
        
    	List<ParkingCardDTO> resultList = new ArrayList<>();
		GetParkingCardsResponse response = new GetParkingCardsResponse();
		response.setCards(resultList);

		KetuoCard card = getCard(plateNumber);

        ParkingCardDTO parkingCardDTO = new ParkingCardDTO();
		if(null != card){
			String expireDate = card.getValidTo();
			this.checkExpireDateIsNull(expireDate,plateNumber);

			long expireTime = Utils.strToDate(expireDate);
			long now = System.currentTimeMillis();
			long cardReserveTime = 0;
			
	    	ParkingLot parkingLot = parkingProvider.findParkingLotById(parkingLotId);
	    	Byte isSupportRecharge = parkingLot.getIsSupportRecharge();
	    	if(ParkingSupportRechargeStatus.SUPPORT.getCode() == isSupportRecharge)	{
	    		Integer cardReserveDay = parkingLot.getCardReserveDays();
	    		//存的天数
	    		cardReserveTime = cardReserveDay * 24 * 60 * 60 * 1000L;
	    	}
			
			if(expireTime + cardReserveTime < now){
				response.setToastType(ParkingToastType.CARD_EXPIRED.getCode());

				return response;
			}
			parkingCardDTO.setOwnerType(ParkingOwnerType.COMMUNITY.getCode());
			parkingCardDTO.setOwnerId(ownerId);
			parkingCardDTO.setParkingLotId(parkingLotId);
			
			parkingCardDTO.setPlateNumber(plateNumber);
			parkingCardDTO.setPlateOwnerPhone("");
			//parkingCardDTO.setStartTime(startTime);
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
			parkingCardDTO.setIsValid(true);
			
			resultList.add(parkingCardDTO);
		}else {
			response.setToastType(ParkingToastType.NOT_CARD_USER.getCode());
		}
        
        return response;
    }

    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(String ownerType, Long ownerId, Long parkingLotId,String plateNumber,String cardNo) {
    	List<ParkingRechargeRateDTO> result = null;
    	List<KetuoCardRate> list = new ArrayList<>();
		List<KetuoCardType> types = getCardType();

    	if(StringUtils.isBlank(plateNumber)) {
    		for(KetuoCardType k: types) {
    			for(KetuoCardRate rate: getCardRule(k.getCarType())) {
    				if(RULE_TYPE.equals(rate.getRuleType())) {
    					rate.setCarType(k.getCarType());
    					rate.setTypeName(k.getTypeName());
    					list.add(rate);
    				}
    			}
    		}
    	}else{
    		KetuoCard cardInfo = getCard(plateNumber);
    		if(null != cardInfo) {
    			String cardType = cardInfo.getCarType();
    			String typeName = null;
    			for(KetuoCardType kt: types) {
					if(cardType.equals(kt.getCarType())) {
						typeName = kt.getTypeName();
						break;
					}
				}
    			for(KetuoCardRate rate: getCardRule(cardType)) {
    				if(RULE_TYPE.equals(rate.getRuleType())) {
    					rate.setCarType(cardType);
    					rate.setTypeName(typeName);
    					list.add(rate);
    				}
    			}
    		}
    	}
    	result = list.stream().map( r -> {
			ParkingRechargeRateDTO dto = new ParkingRechargeRateDTO();
			dto.setOwnerId(ownerId);
			dto.setOwnerType(ownerType);
			dto.setParkingLotId(parkingLotId);
			dto.setRateToken(r.getRuleId());
			Map<String, Object> map = new HashMap<String, Object>();
		    map.put("count", r.getRuleAmount());
			String scope = ParkingNotificationTemplateCode.SCOPE;
			int code = ParkingNotificationTemplateCode.DEFAULT_RATE_NAME;
			String locale = "zh_CN";
			String rateName = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
			dto.setRateName(rateName);
			dto.setCardType(r.getTypeName());
			dto.setMonthCount(new BigDecimal(r.getRuleAmount()));
			dto.setPrice(new BigDecimal(Integer.parseInt(r.getRuleMoney()) / 100));
			dto.setVendorName(ParkingLotVendor.KETUO.getCode());
			return dto;
		}).collect(Collectors.toList());
		
		return result;
    }

    @Override
    public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {

    	return recharge(order);
//    	if(order.getRechargeStatus() != ParkingRechargeOrderRechargeStatus.RECHARGED.getCode()) {
//			if(payStatus.toLowerCase().equals("fail")) {
//				LOGGER.error("pay failed, orderNo={}", order.getId());
//			}else {
//				if(recharge(order)){
//					dbProvider.execute((TransactionStatus transactionStatus) -> {
//						order.setRechargeStatus(ParkingRechargeOrderRechargeStatus.RECHARGED.getCode());
//						order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
//						parkingProvider.updateParkingRechargeOrder(order);
//
//						String key = "parking-recharge" + order.getId();
//						String value = String.valueOf(order.getId());
//				        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
//				        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
//
//				        LOGGER.error("Delete parking order key, key={}", key);
//				        redisTemplate.delete(key);
//
//			        return null;
//					});
//				}
//			}
//		}
    }
    
    @Override
    public ParkingRechargeRateDTO createParkingRechargeRate(CreateParkingRechargeRateCommand cmd){
    	LOGGER.error("Not support create parkingRechargeRate.");
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE,
				"Not support create parkingRechargeRate.");
    }
    
    @Override
    public void deleteParkingRechargeRate(DeleteParkingRechargeRateCommand cmd){
    	LOGGER.error("Not support delete parkingRechargeRate.");
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE,
				"Not support delete parkingRechargeRate.");
    }
    
    private void checkExpireDateIsNull(String expireDate,String plateNo) {
		if(StringUtils.isBlank(expireDate)){
			LOGGER.error("ExpireDate is null, plateNo={}", plateNo);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"ExpireDate is null.");
		}
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
	
	private List<KetuoCardType> getCardType() {
		List<KetuoCardType> result = new ArrayList<>();
		JSONObject param = new JSONObject();
		String json = postToKetuo(param, GET_TYPES);
		KetuoJsonEntity<KetuoCardType> entity = JSONObject.parseObject(json, new TypeReference<KetuoJsonEntity<KetuoCardType>>(){});

		if(entity.isSuccess()){
			if(null != entity.getData())
				result = entity.getData();
		}
		
		return result;
	}
	
	private List<KetuoCardRate> getCardRule(String carType) {
		List<KetuoCardRate> result = new ArrayList<>();
		JSONObject param = new JSONObject();
		param.put("carType", carType);
		String json = postToKetuo(param, GET_CARd_RULE);
		
		KetuoJsonEntity<KetuoCardRate> entity = JSONObject.parseObject(json, new TypeReference<KetuoJsonEntity<KetuoCardRate>>(){});
		if(entity.isSuccess()){
			if(null != entity.getData())
				result = entity.getData();
		}
		return result;
	}
	
	private KetuoCard getCard(String plateNumber) {
		KetuoCard card = null;
		JSONObject param = new JSONObject();

		//储能月卡车没有 归属地区分,接口对接，传参需要截取地区，比如 粤B88888, 传参B88888 eg.
		plateNumber = plateNumber.substring(1, plateNumber.length());
		param.put("plateNo", plateNumber);
		String json = postToKetuo(param, GET_CARD);
        
		KetuoJsonEntity<KetuoCard> entity = JSONObject.parseObject(json, new TypeReference<KetuoJsonEntity<KetuoCard>>(){});
		if(entity.isSuccess()){
			List<KetuoCard> list = entity.getData();
			if(null != list && !list.isEmpty()) {
				card = list.get(0);
			}
		}
        
        return card;
    }
	
	private boolean rechargeMonthlyCard(ParkingRechargeOrder order){

		JSONObject param = new JSONObject();
		String plateNumber = order.getPlateNumber();

		KetuoCard card = getCard(plateNumber);
		String oldValidEnd = card.getValidTo();
		Long time = Utils.strToDate(oldValidEnd);

		//储能月卡充值开始时间 只加一秒
		Timestamp tempStart = Utils.addSeconds(time, 1);
		Timestamp tempEnd = Utils.getTimestampByAddMonth(time, order.getMonthCount().intValue());

		String validStart = timeFormat.get().format(tempStart);
		String validEnd = timeFormat.get().format(tempEnd);

		order.setStartPeriod(tempStart);
		order.setStartPeriod(tempEnd);

		param.put("cardId", card.getCardId());
		//修改科托ruleType 固定为1 表示月卡车
		param.put("ruleType", RULE_TYPE);
	    param.put("ruleAmount", String.valueOf(order.getMonthCount().intValue()));
	    // 支付金额（分）
	    param.put("payMoney", order.getPrice().intValue() * 100);
	    //续费开始时间 yyyy-MM-dd HH:mm:ss 每月第一天的 0点0分0秒
		param.put("startTime", validStart);
		//续费结束时间 yyyy-MM-dd HH:mm:ss 每月最后一天的23点59分59秒
		param.put("endTime", validEnd);
		String json = postToKetuo(param, RECHARGE);

		JSONObject jsonObject = JSONObject.parseObject(json);
		Object obj = jsonObject.get("resCode");
		if(null != obj ) {
			int resCode = (int) obj;
			if(resCode == 0)
				return true;
		}
		return false;
    }
	
	private boolean payTempCardFee(ParkingRechargeOrder order){

		JSONObject param = new JSONObject();
		param.put("orderNo", order.getOrderToken());
		param.put("amount", order.getPrice().intValue() * 100);
	    param.put("discount", 0);
	    param.put("payType", VendorType.WEI_XIN.getCode().equals(order.getPaidType())?4:5);
		String json = postToKetuo(param, PAY_TEMP_FEE);

        JSONObject jsonObject = JSONObject.parseObject(json);
		Object obj = jsonObject.get("resCode");
		if(null != obj ) {
			int resCode = (int) obj;
			if(resCode == 0)
				return true;
		}
		return false;
    }

	@Override
	public boolean recharge(ParkingRechargeOrder order){
		if(order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode()))
			return rechargeMonthlyCard(order);
		return payTempCardFee(order);
    }
	
	private String postToKetuo(JSONObject param, String type) {

		String url = configProvider.getValue("parking.chuneng.url", "") + type;
        String key = configProvider.getValue("parking.chuneng.key", "");
        String user = configProvider.getValue("parking.chuneng.user", "");
        String pwd = configProvider.getValue("parking.chuneng.pwd", "");

        String data = null;
		String iv = dateFormat.get().format(new Date());

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
		if(null != cardInfo) {
			String cardType = cardInfo.getCarType();
			for(KetuoCardRate rate: getCardRule(cardType)) {
				if(rate.getRuleId().equals(order.getRateToken())) {
					ketuoCardRate = rate;
				}
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
		String json = postToKetuo(param, GET_TEMP_FEE);
        
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
	public ParkingTempFeeDTO getParkingTempFee(String ownerType, Long ownerId,
			Long parkingLotId, String plateNumber) {
		KetuoTempFee tempFee = getTempFee(plateNumber);
		
		ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
		if(null == tempFee)
			return dto;
		dto.setPlateNumber(plateNumber);
		dto.setEntryTime(Utils.strToDate(tempFee.getEntryTime()));
		dto.setPayTime(Utils.strToDate(tempFee.getPayTime()));
		dto.setParkingTime(tempFee.getElapsedTime());
		dto.setDelayTime(tempFee.getDelayTime());
		dto.setPrice(new BigDecimal(tempFee.getPayable() / 100));
		dto.setOrderToken(tempFee.getOrderNo());
		return dto;
	}

	@Override
	public OpenCardInfoDTO getOpenCardInfo(GetOpenCardInfoCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParkingCarLockInfoDTO getParkingCarLockInfo(GetParkingCarLockInfoCommand cmd) {
		return null;
	}

	@Override
	public void lockParkingCar(LockParkingCarCommand cmd) {

	}

	@Override
	public GetParkingCarNumsResponse getParkingCarNums(GetParkingCarNumsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
}
