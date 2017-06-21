// @formatter:off
package com.everhomes.parking;

import com.alibaba.fastjson.JSONObject;
import com.bosigao2.ParkWebService;
import com.bosigao2.ParkWebServiceSoap;
import com.bosigao2.rest.Bosigao2CardInfo;
import com.bosigao2.rest.Bosigao2GetCardCommand;
import com.bosigao2.rest.Bosigao2RechargeCommand;
import com.bosigao2.rest.Bosigao2ResultEntity;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.order.OrderUtil;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.rest.parking.*;
import com.everhomes.rest.order.CommonOrderCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.parking.*;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "BOSIGAO2")
public class Bosigao2ParkingVendorHandler implements ParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(Bosigao2ParkingVendorHandler.class);
	
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");

	private static final String RECHARGE = "Parking_MonthlyFee";
	private static final String GET_CARD = "Parking_GetMonthCard";
	private static final String GET_TYPES = "Parking_GetMonthCardDescript";
	
//	private static final String FLAG1 = "1"; //1:卡号
	private static final String FLAG2 = "2"; //2:车牌
	
	@Autowired
	private ParkingProvider parkingProvider;
	
	@Autowired
	private LocaleStringService localeStringService;
	
	@Autowired
	private LocaleTemplateService localeTemplateService;
	
	@Autowired
    private ConfigurationProvider configProvider;
	@Autowired
    private UserProvider userProvider;
	
	@Autowired
	private OrderUtil commonOrderUtil;
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

        Bosigao2ResultEntity result = getCard(plateNumber);

        ParkingCardDTO parkingCardDTO = new ParkingCardDTO();
		if(result.isSuccess()){
			Bosigao2CardInfo cardInfo = JSONObject.parseObject(result.getResult().toString(), Bosigao2CardInfo.class);
			String expireDate =  cardInfo.getExpireDate();
			this.checkExpireDateIsNull(expireDate,plateNumber);

			long expireTime = strToLong2(expireDate+"235959");
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
			
			String userName = cardInfo.getUserName();
			parkingCardDTO.setOwnerType(ParkingOwnerType.COMMUNITY.getCode());
			parkingCardDTO.setOwnerId(ownerId);
			parkingCardDTO.setParkingLotId(parkingLotId);
			
			parkingCardDTO.setPlateOwnerName(userName);
			parkingCardDTO.setPlateNumber(cardInfo.getPlateNo());
			//parkingCardDTO.setStartTime(startTime);
			parkingCardDTO.setEndTime(expireTime);
			parkingCardDTO.setCardType(cardInfo.getCardDescript());
			parkingCardDTO.setCardNumber(cardInfo.getCardCode());
			parkingCardDTO.setPlateOwnerPhone(cardInfo.getMobile());
			parkingCardDTO.setIsValid(true);
			
			resultList.add(parkingCardDTO);
		}else {
			response.setToastType(ParkingToastType.NOT_CARD_USER.getCode());

		}
        
        return response;
    }

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

    @SuppressWarnings("unchecked")
	public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
    	ListCardTypeResponse ret = new ListCardTypeResponse();
    	
    	Map<String, String> map = new HashMap<>();
		map.put("clientID", configProvider.getValue("parking.shenye.projectId", ""));
		String data = StringHelper.toJsonString(map);
		
		ParkWebService service = new ParkWebService();
		ParkWebServiceSoap port = service.getParkWebServiceSoap();
		String json = port.parkingSystemRequestService("", GET_TYPES, data, "");
        
        if(LOGGER.isDebugEnabled())
			LOGGER.debug("Card type from bosigao={}", json);
        
        Bosigao2ResultEntity result = GsonUtil.fromJson(json, Bosigao2ResultEntity.class);
        List<ParkingCardType> list = new ArrayList<>();
		if(result.isSuccess()){
			Map<String, List<String>> cardTypeMap = (Map<String, List<String>>) result.getResult();
			List<String> types = cardTypeMap.get("cardDescript");
			for(String s: types) {
				ParkingCardType parkingCardType = new ParkingCardType();
				parkingCardType.setTypeId(s);
				parkingCardType.setTypeName(s);
				list.add(parkingCardType);
			}
			ret.setCardTypes(list);
		}
    	return ret;
    }
    
    private Bosigao2ResultEntity getCard(String plateNumber){
    	Bosigao2GetCardCommand cmd = new Bosigao2GetCardCommand();
    	cmd.setClientID(configProvider.getValue("parking.shenye.projectId", ""));
    	cmd.setCardCode("");
    	cmd.setPlateNo(plateNumber);
    	cmd.setFlag(FLAG2);

    	ParkWebService service = new ParkWebService();
    	ParkWebServiceSoap port = service.getParkWebServiceSoap();
        String json = port.parkingSystemRequestService("", GET_CARD, cmd.toString(), "");
        
        if(LOGGER.isDebugEnabled())
			LOGGER.debug("Result={}", json);
        
        Bosigao2ResultEntity result = JSONObject.parseObject(json, Bosigao2ResultEntity.class);
        this.checkResultHolderIsNull(result,plateNumber);
        
        return result;
    }
    
    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(String ownerType, Long ownerId, Long parkingLotId,String plateNumber,String cardNo) {
    	List<ParkingRechargeRate> parkingRechargeRateList = null;
    	List<ParkingRechargeRateDTO> result = null;
    	if(StringUtils.isBlank(plateNumber)) {
    		parkingRechargeRateList = parkingProvider.listParkingRechargeRates(ownerType, ownerId, parkingLotId,null);
    		
    	}else{
    		Bosigao2ResultEntity resultEntity = getCard(plateNumber);           
			Bosigao2CardInfo cardInfo = JSONObject.parseObject(resultEntity.getResult().toString(), Bosigao2CardInfo.class);
    		String cardType = cardInfo.getCardDescript();
    		parkingRechargeRateList = parkingProvider.listParkingRechargeRates(ownerType, ownerId, parkingLotId,cardType);
    	}
		result = parkingRechargeRateList.stream().map(r->{
			ParkingRechargeRateDTO dto = new ParkingRechargeRateDTO();
			dto = ConvertHelper.convert(r, ParkingRechargeRateDTO.class);
			dto.setRateToken(r.getId().toString());
			dto.setVendorName(ParkingLotVendor.BOSIGAO2.getCode());
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
    
    private boolean recharge(ParkingRechargeOrder order){
    	Bosigao2RechargeCommand cmd = new Bosigao2RechargeCommand();
		cmd.setClientID(configProvider.getValue("parking.shenye.projectId", ""));
		cmd.setCardCode("");
		cmd.setPlateNo(order.getPlateNumber());
		cmd.setFlag(FLAG2);
		cmd.setPayMos(order.getMonthCount().intValue()+"");
		cmd.setAmount((order.getPrice().intValue()*100) + "");
		cmd.setPayDate(sdf1.format(order.getPaidTime()));
		cmd.setChargePaidNo(order.getId().toString());
		
		ParkWebService service = new ParkWebService();
		ParkWebServiceSoap port = service.getParkWebServiceSoap();
        String json = port.parkingSystemRequestService("", RECHARGE, cmd.toString(), "");

		Bosigao2ResultEntity result = GsonUtil.fromJson(json, Bosigao2ResultEntity.class);
		checkResultHolderIsNull(result, order.getPlateNumber());
		
		return result.isSuccess();
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
    			LOGGER.error("Rate not found"+cmd.getRateToken());
    			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    					"Rate not found");
    		}else{
    			parkingProvider.deleteParkingRechargeRate(rate);
    		}
    	} catch (Exception e) {
			LOGGER.error("Delete parkingRechargeRate fail, cmd={}", cmd.getRateToken());
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_SQL_EXCEPTION,
    				"Delete parkingRechargeRate fail.");
		}
    }
    
    private void checkResultHolderIsNull(Bosigao2ResultEntity result,String plateNo) {
		if(null == result){
			LOGGER.error("remote request from bosigao2 return null, plateNo={}", plateNo);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"remote request from bosigao2 return null.");
		}
	}
    
    private void checkExpireDateIsNull(String expireDate,String plateNo) {
		if(StringUtils.isBlank(expireDate)){
			LOGGER.error("ExpireDate is null, plateNo={}", plateNo);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"ExpireDate is null.");
		}
	}
    
    private long strToLong(String str) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		long ts;
		try {
			ts = sdf.parse(str).getTime();
		} catch (ParseException e) {
			LOGGER.error("data format is not yyyymmdd.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"data format is not yyyymmdd.");
		}
		return ts;
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

	@Override
	public ParkingTempFeeDTO getParkingTempFee(String ownerType, Long ownerId,
			Long parkingLotId, String plateNumber) {
		// TODO Auto-generated method stub
		return null;
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
