// @formatter:off
package com.everhomes.parking.handler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.everhomes.parking.*;
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
import com.everhomes.parking.wanke.WankeCardInfo;
import com.everhomes.parking.wanke.WankeCardType;
import com.everhomes.parking.wanke.WankeJsonEntity;
import com.everhomes.parking.wanke.WankeTempFee;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "WANKE")
public class WankeParkingVendorHandler extends DefaultParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(WankeParkingVendorHandler.class);

	private static final String RECHARGE = "/Parking/MouthCardRecharge";
	private static final String GET_CARD = "/Parking/CardDataQuery";
	private static final String GET_TYPES = "/Parking/GetMonthCardList";
	private static final String GET_TEMP_FEE = "/Parking/GetCost";
	private static final String PAY_TEMP_FEE = "/Parking/PayCost";

	@Autowired
	private ParkingProvider parkingProvider;
	@Autowired
    private ConfigurationProvider configProvider;
	
	@Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
        
    	List<ParkingCardDTO> resultList = new ArrayList<>();

    	WankeCardInfo card = getCard(plateNumber);

        ParkingCardDTO parkingCardDTO = new ParkingCardDTO();
		if(null != card){
			String expireDate = card.getExpireDate();
			this.checkExpireDateIsNull(expireDate,plateNumber);

			long expireTime = Utils.strToLong(expireDate, Utils.DateStyle.DATE_TIME_STR);

			if (checkExpireTime(parkingLot, expireTime)) {
				return resultList;
			}

			parkingCardDTO.setOwnerType(parkingLot.getOwnerType());
			parkingCardDTO.setOwnerId(parkingLot.getOwnerId());
			parkingCardDTO.setParkingLotId(parkingLot.getId());
			
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
		}
        
        return resultList;
    }

	@Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot,String plateNumber,String cardNo) {
    	List<ParkingRechargeRate> parkingRechargeRateList;

    	if(StringUtils.isBlank(plateNumber)) {
    		parkingRechargeRateList = parkingProvider.listParkingRechargeRates(parkingLot.getOwnerType(), parkingLot.getOwnerId(),
					parkingLot.getId(), null);
    		
    	}else{
    		WankeCardInfo cardInfo = getCard(plateNumber);           
			
    		String cardType = cardInfo.getCardType();
    		parkingRechargeRateList = parkingProvider.listParkingRechargeRates(parkingLot.getOwnerType(), parkingLot.getOwnerId(),
					parkingLot.getId(), cardType);
    	}
    	
    	
		List<WankeCardType> types = getCardType();

		List<ParkingRechargeRateDTO> result = parkingRechargeRateList.stream().map(r->{
			String type = null;
			for(WankeCardType t: types) {
				if(t.getId().equals(r.getCardType())) {
					type = t.getName();
				}
			}

			ParkingRechargeRateDTO dto = ConvertHelper.convert(r, ParkingRechargeRateDTO.class);
			dto.setRateToken(r.getId().toString());
			dto.setVendorName(ParkingLotVendor.WANKE.getCode());
			
			dto.setCardType(type);
			return dto;
		}).collect(Collectors.toList());
		
		return result;
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

		List<WankeCardType> result = new ArrayList<>();
		JSONObject param = new JSONObject();
		String json = post(GET_TYPES, param);
		
		WankeJsonEntity<List<WankeCardType>> entity = JSONObject.parseObject(json, new TypeReference<WankeJsonEntity<List<WankeCardType>>>(){});

		if(entity.isSuccess()){
			if(null != entity.getData())
				result = entity.getData();
		}
		
		return result;
	}
	
	private WankeCardInfo getCard(String plateNumber) {

		WankeCardInfo card = null;
		JSONObject param = new JSONObject();
		
		param.put("plateNo", plateNumber);
		param.put("flag", "2");
		String json = post(GET_CARD, param);

        WankeJsonEntity<WankeCardInfo> entity = JSONObject.parseObject(json, new TypeReference<WankeJsonEntity<WankeCardInfo>>(){});
		if(entity.isSuccess()){
			card = entity.getData();
			if(null != card) {
				//对接方返回的有效期 没有时分秒，这里补上，有效期默认是一天的23时59分59秒
				card.setExpireDate(card.getExpireDate() + "235959");
			}
		}
        
        return card;
    }
	
	private boolean rechargeMonthlyCard(ParkingRechargeOrder order){

		JSONObject param = new JSONObject();

		param.put("plateNo", order.getPlateNumber());
		param.put("flag", "2");
	    param.put("amount", (order.getPrice().multiply(new BigDecimal(100))).intValue()); //单位分
	    param.put("payMons", String.valueOf(order.getMonthCount().intValue()));
	    param.put("chargePaidNo", order.getId());
	    param.put("payTime", Utils.dateToStr(new Date(), Utils.DateStyle.DATE_TIME_STR));
	    param.put("sign", "");

		//将充值信息存入订单
		WankeCardInfo card = getCard(order.getPlateNumber());
		long startPeriod = Utils.strToLong(card.getExpireDate(), Utils.DateStyle.DATE_TIME_STR);
		order.setStartPeriod(new Timestamp(startPeriod + 1000)); //加一秒
		order.setEndPeriod(Utils.getTimestampByAddNatureMonth(startPeriod, order.getMonthCount().intValue()));

		String json = post(RECHARGE, param);

		order.setErrorDescriptionJson(json);

        WankeJsonEntity<Object> entity = JSONObject.parseObject(json, new TypeReference<WankeJsonEntity<Object>>(){});
		return entity.isSuccess();

    }
	
	private boolean payTempCardFee(ParkingRechargeOrder order){

		JSONObject param = new JSONObject();

		param.put("orderNo", order.getOrderToken());
		param.put("amount", (order.getPrice().multiply(new BigDecimal(100))).intValue());
	    param.put("payType", VendorType.WEI_XIN.getCode().equals(order.getPaidType())?1:2);
		String json = post(PAY_TEMP_FEE, param);

        WankeJsonEntity<Object> entity = JSONObject.parseObject(json, new TypeReference<WankeJsonEntity<Object>>(){});
		return entity.isSuccess();

    }

	private WankeTempFee getTempFee(String plateNumber) {

		WankeTempFee tempFee = null;
		JSONObject param = new JSONObject();
		param.put("plateNo", plateNumber);
		
		String json = post(GET_TEMP_FEE, param);
        
        WankeJsonEntity<WankeTempFee> entity = JSONObject.parseObject(json, new TypeReference<WankeJsonEntity<WankeTempFee>>(){});
        
		if(entity.isSuccess()){
			tempFee = entity.getData();
		}
        
        return tempFee;
    }

	@Override
	public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {
		
		WankeTempFee tempFee = getTempFee(plateNumber);
		
		ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
		if(null == tempFee)
			return dto;
		dto.setPlateNumber(plateNumber);
		dto.setEntryTime(Utils.strToLong(tempFee.getEntryTime(), Utils.DateStyle.DATE_TIME));
		dto.setParkingTime(Integer.valueOf(tempFee.getParkingTime()));
		dto.setDelayTime(Integer.valueOf(tempFee.getDelayTime()));
		dto.setPrice(new BigDecimal(tempFee.getAmount()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
		dto.setOrderToken(tempFee.getOrderNo());
		dto.setPayTime(System.currentTimeMillis());
		return dto;
	}

	private String post(String type, JSONObject param) {

		LOGGER.info("Parking info, namespace={}", this.getClass().getName());

		String url = configProvider.getValue("parking.wanke.url", "");

		String json = Utils.post(url + type, param, null);

		return json;
	}

}
