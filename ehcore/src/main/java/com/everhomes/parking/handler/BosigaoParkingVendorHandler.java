// @formatter:off
package com.everhomes.parking.handler;

import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.everhomes.parking.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.parking.bosigao.cxf.Service1;
import com.everhomes.parking.bosigao.cxf.Service1Soap;
import com.everhomes.parking.bosigao.cxf.rest.BosigaoCardInfo;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.organization.pm.pay.ResultHolder;
import com.everhomes.rest.parking.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

// "BOSIGAO"需与ParkingLotVendor.BOSIGAO的枚举值保持一致
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "BOSIGAO")
@Deprecated
public class BosigaoParkingVendorHandler extends AbstractCommonParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(BosigaoParkingVendorHandler.class);

	@Autowired
	private ParkingProvider parkingProvider;
	@Autowired
	private LocaleTemplateService localeTemplateService;

	@Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
        
    	List<ParkingCardDTO> resultList = new ArrayList<>();

		BosigaoCardInfo card = getCardInfo(plateNumber);
    	
        ParkingCardDTO parkingCardDTO = new ParkingCardDTO();
		if(null != card){

			String validEnd = card.getValidEnd();
			Long endTime = strToLong2(validEnd+"235959");
			if (checkExpireTime(parkingLot, endTime)) {
				return resultList;
			}
			
			String plateOwnerName = card.getUserName();
			String carNumber = card.getCarNumber();
			
			String cardNumber = card.getCardCode();
			String cardType = card.getCardDescript();
			String plateOwnerPhone = card.getMobile();

			parkingCardDTO.setOwnerType(parkingLot.getOwnerType());
			parkingCardDTO.setOwnerId(parkingLot.getOwnerId());
			parkingCardDTO.setParkingLotId(parkingLot.getId());
			
			parkingCardDTO.setPlateOwnerName(plateOwnerName);
			parkingCardDTO.setPlateNumber(carNumber);
			//parkingCardDTO.setStartTime(startTime);
			parkingCardDTO.setEndTime(endTime);
			parkingCardDTO.setCardType(cardType);
			parkingCardDTO.setCardNumber(cardNumber);
			parkingCardDTO.setPlateOwnerPhone(plateOwnerPhone);
			parkingCardDTO.setIsValid(true);
			
			resultList.add(parkingCardDTO);
		}
        return resultList;
    }

    private BosigaoCardInfo getCardInfo(String plateNumber){
    	
    	URL wsdlURL = Service1.WSDL_LOCATION;
		Service1 ss = new Service1(wsdlURL, Service1.SERVICE);
        Service1Soap port = ss.getService1Soap12();
        
        String json = port.getCardInfo("", plateNumber, "2", "sign");
        
        if(LOGGER.isDebugEnabled())
			LOGGER.debug("Parking bosigao json={}", json);
        
        ResultHolder resultHolder = JSONObject.parseObject(json, ResultHolder.class);
        this.checkResultHolderIsNull(resultHolder, plateNumber);
        
        BosigaoCardInfo card = null;
        
        if(resultHolder.isSuccess()){
        	String cardJson = JSONObject.parseObject(resultHolder.getData().toString()).get("card").toString();
            if(LOGGER.isDebugEnabled())
    			LOGGER.debug("Parking bosigao cardJson={}", cardJson);
            
            card = JSONObject.parseObject(cardJson, BosigaoCardInfo.class);
        }
    	return card;
    }

    public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
    	ListCardTypeResponse ret = new ListCardTypeResponse();
    	URL wsdlURL = Service1.WSDL_LOCATION;
    	 
		Service1 ss = new Service1(wsdlURL, Service1.SERVICE);
        Service1Soap port = ss.getService1Soap12();
        String json = port.getAllCardDescript();
        
        if(LOGGER.isDebugEnabled())
			LOGGER.debug("Card type from bosigao={}", json);
        JSONObject obj = JSONObject.parseObject(json);
        boolean flag = obj.getBoolean("success");
        @SuppressWarnings("unchecked")
		List<String> types = obj.getObject("cardDescript", List.class);
        List<ParkingCardType> list = new ArrayList<>();
		if(flag) {
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
    
    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot, String plateNumber, String cardNo) {
    	
    	List<ParkingRechargeRate> parkingRechargeRateList = new ArrayList<>();
    	
    	if(StringUtils.isBlank(plateNumber)) {
    		parkingRechargeRateList = parkingProvider.listParkingRechargeRates(parkingLot.getOwnerType(), parkingLot.getOwnerId(),
					parkingLot.getId(), null);
    	}else{
    		BosigaoCardInfo card = getCardInfo(plateNumber);
    		String cardType = card.getCardDescript();
    		parkingRechargeRateList = parkingProvider.listParkingRechargeRates(parkingLot.getOwnerType(), parkingLot.getOwnerId(),
					parkingLot.getId(), cardType);
    	}
    	
    	List<ParkingRechargeRateDTO> result = parkingRechargeRateList.stream().map(r->{
			ParkingRechargeRateDTO dto = new ParkingRechargeRateDTO();
			dto = ConvertHelper.convert(r, ParkingRechargeRateDTO.class);
			dto.setRateToken(r.getId().toString());
			dto.setVendorName(ParkingLotVendor.BOSIGAO.getCode());
			return dto;
		}).collect(Collectors.toList());
		
		return result;
    }

    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    @Override
    public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {

		return recharge(order);

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
    
    private void checkResultHolderIsNull(ResultHolder resultHolder, String plateNo) {
		if(resultHolder == null){
			LOGGER.error("remote search pay order return null.plateNo="+plateNo);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"remote search pay order return null.");
		}
	}
    
    private void checkValidStatusIsNull(Boolean validStatus,String plateNo) {
		if(validStatus == null){
			LOGGER.error("validStatus is null.plateNo="+plateNo);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"validStatus is null.");
		}
	}

	@Override
	public boolean recharge(ParkingRechargeOrder order){

		String carNumber = order.getPlateNumber();
		String cost = String.valueOf((order.getPrice().intValue() * 100));
		String flag = "2"; //停车场系统接口的传入参数，2表示是车牌号
		String payTime = order.getPaidTime().toString();

		BosigaoCardInfo card = getCardInfo(carNumber);
		String oldValidEnd = card.getValidEnd();
		Long time = strToLong(oldValidEnd);

		String validStart = timestampToStr(addDays(time, 1));
		String validEnd = timestampToStr(new Timestamp(Utils.getLongByAddNatureMonth(time, order.getMonthCount().intValue())));

		URL wsdlURL = Service1.WSDL_LOCATION;
		Service1 ss = new Service1(wsdlURL, Service1.SERVICE);
        Service1Soap port = ss.getService1Soap12();

        String json = port.cardPayMoney("", carNumber, flag, cost, validStart, validEnd, payTime, "sign");

		ResultHolder resultHolder = GsonUtil.fromJson(json, ResultHolder.class);
		checkResultHolderIsNull(resultHolder,carNumber);

		return resultHolder.isSuccess();
	}
	
    private Long strToLong(String str) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
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
    
	private String timestampToStr(Timestamp time) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String str = sdf.format(time);
		return str;
	}
	
	private Timestamp addDays(Long oldPeriod, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(oldPeriod);
		calendar.add(Calendar.DATE, days);
		Timestamp time = new Timestamp(calendar.getTimeInMillis());
		
		return time;
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
	public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {
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
