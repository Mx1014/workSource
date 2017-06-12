// @formatter:off
package com.everhomes.parking;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.everhomes.rest.parking.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.parking.wanke.WankeCardInfo;
import com.everhomes.parking.wanke.WankeCardType;
import com.everhomes.parking.wanke.WankeJsonEntity;
import com.everhomes.parking.wanke.WankeTempFee;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "WANKE")
public class WankeParkingVendorHandler implements ParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(WankeParkingVendorHandler.class);
	
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
	
	private static final String RECHARGE = "/Parking/MouthCardRecharge";
	private static final String GET_CARD = "/Parking/CardDataQuery";
	private static final String GET_TYPES = "/Parking/GetMonthCardList";
	private static final String GET_TEMP_FEE = "/Parking/GetCost";
	private static final String PAY_TEMP_FEE = "/Parking/PayCost";

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Autowired
	private ParkingProvider parkingProvider;
	@Autowired
	private LocaleTemplateService localeTemplateService;
	@Autowired
    private ConfigurationProvider configProvider;
	@Autowired
    private BigCollectionProvider bigCollectionProvider;
	@Autowired
    private DbProvider dbProvider;
	
	@Override
    public GetParkingCardsResponse getParkingCardsByPlate(String ownerType, Long ownerId,
    		Long parkingLotId, String plateNumber) {
        
    	List<ParkingCardDTO> resultList = new ArrayList<ParkingCardDTO>();
		GetParkingCardsResponse response = new GetParkingCardsResponse();
		response.setCards(resultList);

    	WankeCardInfo card = getCard(plateNumber);

        ParkingCardDTO parkingCardDTO = new ParkingCardDTO();
		if(null != card){
			String expireDate = card.getExpireDate();
			this.checkExpireDateIsNull(expireDate,plateNumber);

			long expireTime = strToLong2(expireDate);
			long now = System.currentTimeMillis();
			long cardReserveTime = 0;
			
	    	ParkingLot parkingLot = parkingProvider.findParkingLotById(parkingLotId);
	    	Byte isSupportRecharge = parkingLot.getIsSupportRecharge();
	    	if(ParkingSupportRechargeStatus.SUPPORT.getCode() == isSupportRecharge)	{
	    		Integer cardReserveDay = parkingLot.getCardReserveDays();
	    		cardReserveTime = cardReserveDay * 24 * 60 * 60 * 1000L;

	    	}
			
			if(expireTime + cardReserveTime < now){
				response.setToastType(ParkingToastType.CARD_EXPIRED.getCode());

				return response;
			}
			parkingCardDTO.setOwnerType(ParkingOwnerType.COMMUNITY.getCode());
			parkingCardDTO.setOwnerId(ownerId);
			parkingCardDTO.setParkingLotId(parkingLotId);
			
			String plateOwnerName = card.getUserName();
			String plateOwnerPhone = card.getMobile();
			parkingCardDTO.setPlateOwnerName(plateOwnerName);
			parkingCardDTO.setPlateNumber(plateNumber);
			parkingCardDTO.setPlateOwnerPhone(plateOwnerPhone);

			parkingCardDTO.setEndTime(expireTime);
			
			String type = null;
			List<WankeCardType> types = getCardType();
			for(WankeCardType t: types) {
				if(t.getId().equals(card.getCardType())) {
					type = t.getName();
				}
			}
			parkingCardDTO.setCardType(type);
			parkingCardDTO.setCardNumber(card.getCardNo());
			parkingCardDTO.setIsValid(true);
			
			resultList.add(parkingCardDTO);
		}else {
			response.setToastType(ParkingToastType.NOT_CARD_USER.getCode());

		}
        
        return response;
    }

	@Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(String ownerType, Long ownerId, Long parkingLotId,String plateNumber,String cardNo) {
    	List<ParkingRechargeRate> parkingRechargeRateList = null;
    	List<ParkingRechargeRateDTO> result = null;
    	if(StringUtils.isBlank(plateNumber)) {
    		parkingRechargeRateList = parkingProvider.listParkingRechargeRates(ownerType, ownerId, parkingLotId, null);
    		
    	}else{
    		WankeCardInfo cardInfo = getCard(plateNumber);           
			
    		String cardType = cardInfo.getCardType();
    		parkingRechargeRateList = parkingProvider.listParkingRechargeRates(ownerType, ownerId, parkingLotId, cardType);
    	}
    	
    	
		List<WankeCardType> types = getCardType();
    	
		result = parkingRechargeRateList.stream().map(r->{
			String type = null;
			for(WankeCardType t: types) {
				if(t.getId().equals(r.getCardType())) {
					type = t.getName();
				}
			}
			
			ParkingRechargeRateDTO dto = new ParkingRechargeRateDTO();
			dto = ConvertHelper.convert(r, ParkingRechargeRateDTO.class);
			dto.setRateToken(r.getId().toString());
			dto.setVendorName(ParkingLotVendor.WANKE.getCode());
			
			dto.setCardType(type);
			return dto;
		}
		).collect(Collectors.toList());
		
		return result;
    }

    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    @Override
    public void notifyParkingRechargeOrderPayment(ParkingRechargeOrder order, String payStatus) {
    	if(order.getRechargeStatus() != ParkingRechargeOrderRechargeStatus.RECHARGED.getCode()) {
			if(payStatus.toLowerCase().equals("fail")) {
				LOGGER.error("pay failed, orderNo={}", order.getId());
			}
			else {
				if(recharge(order)){
					dbProvider.execute((TransactionStatus transactionStatus) -> {
						order.setRechargeStatus(ParkingRechargeOrderRechargeStatus.RECHARGED.getCode());
						order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
						parkingProvider.updateParkingRechargeOrder(order);
						
						String key = "parking-recharge" + order.getId();
						String value = String.valueOf(order.getId());
				        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
				        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
				      
				        LOGGER.error("Delete parking order key, key={}", key);
				        redisTemplate.delete(key);
			        
			        return null;
					});
				}
			}
		}
    }
    
    @Override
    public ParkingRechargeRateDTO createParkingRechargeRate(CreateParkingRechargeRateCommand cmd){
    	User user = UserContext.current().getUser();
    	
    	ParkingRechargeRate parkingRechargeRate = new ParkingRechargeRate();
    	parkingRechargeRate.setOwnerType(cmd.getOwnerType());
    	parkingRechargeRate.setOwnerId(cmd.getOwnerId());
    	parkingRechargeRate.setParkingLotId(cmd.getParkingLotId());
    	parkingRechargeRate.setCardType(cmd.getCardType());
    	/*费率 名称默认设置 by sw*/
    	Map<String, Object> map = new HashMap<String, Object>();
	    map.put("count", cmd.getMonthCount().intValue());
		String scope = ParkingNotificationTemplateCode.SCOPE;
		int code = ParkingNotificationTemplateCode.DEFAULT_RATE_NAME;
		String locale = "zh_CN";
		String rateName = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
    	parkingRechargeRate.setRateName(rateName);
    	parkingRechargeRate.setMonthCount(cmd.getMonthCount());
    	parkingRechargeRate.setPrice(cmd.getPrice());
    	parkingRechargeRate.setCreatorUid(user.getId());
    	parkingRechargeRate.setCreateTime(new Timestamp(System.currentTimeMillis()));
    	parkingRechargeRate.setStatus(ParkingRechargeRateStatus.ACTIVE.getCode());
    	parkingProvider.createParkingRechargeRate(parkingRechargeRate);
    	return ConvertHelper.convert(parkingRechargeRate, ParkingRechargeRateDTO.class);
    }
    
    @Override
    public void deleteParkingRechargeRate(DeleteParkingRechargeRateCommand cmd){
    	try {
    		ParkingRechargeRate rate = parkingProvider.findParkingRechargeRatesById(Long.parseLong(cmd.getRateToken()));
    		if(rate == null){
    			LOGGER.error("remote search pay order return null.rateId="+cmd.getRateToken());
    			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    					"remote search pay order return null.");
    		}else{
    			parkingProvider.deleteParkingRechargeRate(rate);
    		}
    	} catch (Exception e) {
			LOGGER.error("delete parkingRechargeRate fail."+cmd.getRateToken());
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_SQL_EXCEPTION,
    				"delete parkingRechargeRate fail."+cmd.getRateToken());
		}
    }
    
    private void checkExpireDateIsNull(String expireDate,String plateNo) {
		if(StringUtils.isBlank(expireDate)){
			LOGGER.error("ExpireDate is null, plateNo={}", plateNo);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"ExpireDate is null.");
		}
	}
    
//    private long strToLong(String str) {
//
//		long ts;
//		try {
//			ts = sdf1.parse(str).getTime();
//		} catch (ParseException e) {
//			LOGGER.error("data format is not yyyyMMddHHmmss, str={}", str);
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//					"data format is not yyyyMMddHHmmss.");
//		}
//		return ts;
//	}
    
    private Long strToLong2(String str) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		Long ts = null;
		try {
			ts = sdf.parse(str).getTime();
		} catch (ParseException e) {
			LOGGER.error("validityPeriod data format is not yyyymmdd.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"validityPeriod data format is not yyyymmdd.");
		}
		
		return ts;
	}
    
    private Long strToLong3(String str) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Long ts = null;
		try {
			ts = sdf.parse(str).getTime();
		} catch (ParseException e) {
			LOGGER.error("validityPeriod data format is not yyyymmdd.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"validityPeriod data format is not yyyymmdd.");
		}
		
		return ts;
	}
	
//	@Scheduled(cron="0 0 0/2 * * ? ")
//	@Override
//	public void refreshParkingRechargeOrderStatus() {
//		LOGGER.info("refresh recharge status.");
//		List<ParkingRechargeOrder> orderList = parkingProvider.findWaitingParkingRechargeOrders(ParkingLotVendor.KETUO);
//		orderList.stream().map(order -> {
//			
//			if(recharge(order)){
//				order.setRechargeStatus(ParkingRechargeOrderRechargeStatus.RECHARGED.getCode());
//				order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
//				parkingProvider.updateParkingRechargeOrder(order);
//			}
//			return null;
//		});
//	}
    
	public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
    	ListCardTypeResponse ret = new ListCardTypeResponse();
    	
        List<ParkingCardType> resultTypes = new ArrayList<>();

		List<WankeCardType> list = getCardType();
		for(WankeCardType t: list) {
			
			ParkingCardType parkingCardType = new ParkingCardType();
			parkingCardType.setTypeId(t.getId());
			parkingCardType.setTypeName(t.getName());
			resultTypes.add(parkingCardType);
		}
		ret.setCardTypes(resultTypes);	
    	return ret;
    }
	
	private List<WankeCardType> getCardType() {

		String url = configProvider.getValue("parking.wanke.url", "");
		List<WankeCardType> result = new ArrayList<>();
		JSONObject param = new JSONObject();
		String json = HttpUtils.post(url + GET_TYPES, param, null);
		
		WankeJsonEntity<List<WankeCardType>> entity = JSONObject.parseObject(json, new TypeReference<WankeJsonEntity<List<WankeCardType>>>(){});

		if(entity.isSuccess()){
			if(null != entity.getData())
				result = entity.getData();
		}
		
		return result;
	}
	
	private WankeCardInfo getCard(String plateNumber) {

		String url = configProvider.getValue("parking.wanke.url", "");

		WankeCardInfo card = null;
		JSONObject param = new JSONObject();
		
		param.put("plateNo", plateNumber);
		param.put("flag", "2");
		String json = HttpUtils.post(url + GET_CARD, param, null);
        
		LOGGER.info("Result={}, param={}", json, param);
        
        WankeJsonEntity<WankeCardInfo> entity = JSONObject.parseObject(json, new TypeReference<WankeJsonEntity<WankeCardInfo>>(){});
		if(entity.isSuccess()){
			card = entity.getData();
			if(null != card)
				card.setExpireDate(card.getExpireDate()+"235959");
		}
        
        return card;
    }
	
	private boolean rechargeMonthlyCard(ParkingRechargeOrder order){

		String url = configProvider.getValue("parking.wanke.url", "");

		JSONObject param = new JSONObject();

		param.put("plateNo", order.getPlateNumber());
		param.put("flag", "2");
	    param.put("amount", order.getPrice().intValue()*100);
	    param.put("payMons", String.valueOf(order.getMonthCount().intValue()));
	    param.put("chargePaidNo", order.getId());
	    param.put("payTime", sdf1.format(new Date()));
	    param.put("sign", "");
	    
		String json = HttpUtils.post(url + RECHARGE, param, null);
        
		LOGGER.info("Result={}, param={}", json, param);
        
        WankeJsonEntity<Object> entity = JSONObject.parseObject(json, new TypeReference<WankeJsonEntity<Object>>(){});
		return entity.isSuccess();
		
//		JSONObject jsonObject = JSONObject.parseObject(json);
//		Object obj = jsonObject.get("errorCode");
//		if(null != obj ) {
//			int resCode = (int) obj;
//			if(resCode == 0)
//				return true;
//			
//		}
//		return false;
    }
	
	private boolean payTempCardFee(ParkingRechargeOrder order){

		String url = configProvider.getValue("parking.wanke.url", "");

		JSONObject param = new JSONObject();

		param.put("orderNo", order.getOrderToken());
		param.put("amount", order.getPrice().intValue() * 100);
	    param.put("payType", VendorType.WEI_XIN.getCode().equals(order.getPaidType())?1:2);
		String json = HttpUtils.post(url + PAY_TEMP_FEE, param, null);

		LOGGER.info("Result={}, param={}", json, param);
        
        WankeJsonEntity<Object> entity = JSONObject.parseObject(json, new TypeReference<WankeJsonEntity<Object>>(){});
		return entity.isSuccess();
		
//        JSONObject jsonObject = JSONObject.parseObject(json);
//		Object obj = jsonObject.get("errorCode");
//		if(null != obj ) {
//			int resCode = (Integer) obj;
//			if(resCode == 0)
//				return true;
//			
//		}
//		return false;
    }

	private boolean recharge(ParkingRechargeOrder order){
		if(order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode()))
			return rechargeMonthlyCard(order);
		return payTempCardFee(order);
    }
	
	@Override
	public void updateParkingRechargeOrderRate(ParkingRechargeOrder order) {
		ParkingRechargeRate rate = parkingProvider.findParkingRechargeRatesById(Long.parseLong(order.getRateToken()));
		if(null == rate) {
			LOGGER.error("Rate not found, cmd={}", order);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Rate not found.");
		}
		order.setRateName(rate.getRateName());
		
	}

	private WankeTempFee getTempFee(String plateNumber) {

		String url = configProvider.getValue("parking.wanke.url", "");

		WankeTempFee tempFee = null;
		JSONObject param = new JSONObject();
		param.put("plateNo", plateNumber);
		
		String json = HttpUtils.post(url + GET_TEMP_FEE, param, null);
        
        if(LOGGER.isDebugEnabled())
			LOGGER.debug("Result={}, param={}", json, param);
        
        WankeJsonEntity<WankeTempFee> entity = JSONObject.parseObject(json, new TypeReference<WankeJsonEntity<WankeTempFee>>(){});
        
		if(entity.isSuccess()){
			tempFee = entity.getData();
		}
        
        return tempFee;
    }

	@Override
	public ParkingTempFeeDTO getParkingTempFee(String ownerType, Long ownerId, Long parkingLotId, String plateNumber) {
		
		WankeTempFee tempFee = getTempFee(plateNumber);
		
		ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
		if(null == tempFee)
			return dto;
		dto.setPlateNumber(plateNumber);
		dto.setEntryTime(strToLong3(tempFee.getEntryTime()));
		dto.setParkingTime(Integer.valueOf(tempFee.getParkingTime()));
		dto.setDelayTime(Integer.valueOf(tempFee.getDelayTime()));
		dto.setPrice(new BigDecimal(Integer.valueOf(tempFee.getAmount()) / 100));
		dto.setOrderToken(tempFee.getOrderNo());
		dto.setPayTime(System.currentTimeMillis());
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
