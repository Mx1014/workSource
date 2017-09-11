// @formatter:off
package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.parking.bosigao2.ParkWebService;
import com.everhomes.parking.bosigao2.ParkWebServiceSoap;
import com.everhomes.parking.bosigao2.rest.Bosigao2CardInfo;
import com.everhomes.parking.bosigao2.rest.Bosigao2GetCardCommand;
import com.everhomes.parking.bosigao2.rest.Bosigao2RechargeCommand;
import com.everhomes.parking.bosigao2.rest.Bosigao2ResultEntity;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.parking.*;
import com.everhomes.rest.parking.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 深业 停车
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "BOSIGAO2")
public class Bosigao2ParkingVendorHandler extends DefaultParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(Bosigao2ParkingVendorHandler.class);
	
	private ThreadLocal<SimpleDateFormat> timeFormat = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			return 	new SimpleDateFormat("yyyyMMddHHmmss");
		}
	};

	private static final String RECHARGE = "Parking_MonthlyFee";
	private static final String GET_CARD = "Parking_GetMonthCard";
	private static final String GET_TYPES = "Parking_GetMonthCardDescript";
	
	private static final String FLAG2 = "2"; //2:车牌
	
	@Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
        
    	List<ParkingCardDTO> resultList = new ArrayList<>();

        Bosigao2ResultEntity result = getCard(plateNumber);

        ParkingCardDTO parkingCardDTO = new ParkingCardDTO();
		if(result.isSuccess()){
			Bosigao2CardInfo cardInfo = JSONObject.parseObject(result.getResult().toString(), Bosigao2CardInfo.class);
			String expireDate =  cardInfo.getExpireDate();
			this.checkExpireDateIsNull(expireDate,plateNumber);
			//计算有效期从当天235959秒计算
			long expireTime = Utils.strToLong(expireDate + "235959", Utils.DateStyle.DATE_TIME_STR);
			if (checkExpireTime(parkingLot, expireTime)) {
				return resultList;
			}
			
			String userName = cardInfo.getUserName();
			parkingCardDTO.setOwnerType(parkingLot.getOwnerType());
			parkingCardDTO.setOwnerId(parkingLot.getOwnerId());
			parkingCardDTO.setParkingLotId(parkingLot.getId());
			
			parkingCardDTO.setPlateOwnerName(userName);
			parkingCardDTO.setPlateNumber(cardInfo.getPlateNo());
			//parkingCardDTO.setStartTime(startTime);
			parkingCardDTO.setEndTime(expireTime);
			parkingCardDTO.setCardType(cardInfo.getCardDescript());
			parkingCardDTO.setCardNumber(cardInfo.getCardCode());
			parkingCardDTO.setPlateOwnerPhone(cardInfo.getMobile());
			parkingCardDTO.setIsValid(true);
			
			resultList.add(parkingCardDTO);
		}
        
        return resultList;
    }

    @Override
	public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
    	ListCardTypeResponse ret = new ListCardTypeResponse();
    	
    	Map<String, String> map = new HashMap<>();
		map.put("clientID", configProvider.getValue("parking.shenye.projectId", ""));
		String data = StringHelper.toJsonString(map);
		
		ParkWebService service = new ParkWebService();
		ParkWebServiceSoap port = service.getParkWebServiceSoap();
		String json = port.parkingSystemRequestService("", GET_TYPES, data, "");

		LOGGER.info("Card type from bosigao={}", json);
        
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

		LOGGER.info("Result={}", json);
        
        Bosigao2ResultEntity result = JSONObject.parseObject(json, Bosigao2ResultEntity.class);
        this.checkResultHolderIsNull(result,plateNumber);
        
        return result;
    }
    
    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot,String plateNumber,String cardNo) {
    	List<ParkingRechargeRate> parkingRechargeRateList;

    	if(StringUtils.isBlank(plateNumber)) {
    		parkingRechargeRateList = parkingProvider.listParkingRechargeRates(parkingLot.getOwnerType(), parkingLot.getOwnerId(),
					parkingLot.getId(),null);
    		
    	}else{
    		Bosigao2ResultEntity resultEntity = getCard(plateNumber);           
			Bosigao2CardInfo cardInfo = JSONObject.parseObject(resultEntity.getResult().toString(), Bosigao2CardInfo.class);
    		String cardType = cardInfo.getCardDescript();
    		parkingRechargeRateList = parkingProvider.listParkingRechargeRates(parkingLot.getOwnerType(), parkingLot.getOwnerId(),
					parkingLot.getId(),cardType);
    	}
		List<ParkingRechargeRateDTO> result = parkingRechargeRateList.stream().map(r->{

			ParkingRechargeRateDTO dto = ConvertHelper.convert(r, ParkingRechargeRateDTO.class);
			dto.setRateToken(r.getId().toString());
			dto.setVendorName(ParkingLotVendor.BOSIGAO2.getCode());
			return dto;
		}
		).collect(Collectors.toList());
		
		return result;
    }

    @Override
    public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
		if(order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode()))
			return rechargeMonthlyCard(order);
		return false;
    }

    private boolean rechargeMonthlyCard(ParkingRechargeOrder order){
    	Bosigao2RechargeCommand cmd = new Bosigao2RechargeCommand();
		cmd.setClientID(configProvider.getValue("parking.shenye.projectId", ""));
		cmd.setCardCode("");
		cmd.setPlateNo(order.getPlateNumber());
		cmd.setFlag(FLAG2);
		cmd.setPayMos(String.valueOf(order.getMonthCount().intValue()));
		int amount = (order.getPrice().multiply(new BigDecimal(100))).intValue();
		cmd.setAmount(String.valueOf(amount));
		cmd.setPayDate(timeFormat.get().format(order.getPaidTime()));
		cmd.setChargePaidNo(order.getId().toString());

		Bosigao2ResultEntity cardEntity = getCard(order.getPlateNumber());
		Bosigao2CardInfo cardInfo = JSONObject.parseObject(cardEntity.getResult().toString(), Bosigao2CardInfo.class);
		long startPeriod = Utils.strToLong(cardInfo.getExpireDate() + "235959", Utils.DateStyle.DATE_TIME_STR);
		order.setStartPeriod(new Timestamp(startPeriod + 1000));
		order.setEndPeriod(Utils.getTimestampByAddNatureMonth(startPeriod, order.getMonthCount().intValue()));

		ParkWebService service = new ParkWebService();
		ParkWebServiceSoap port = service.getParkWebServiceSoap();
        String json = port.parkingSystemRequestService("", RECHARGE, cmd.toString(), "");

		order.setErrorDescriptionJson(json);

		Bosigao2ResultEntity result = GsonUtil.fromJson(json, Bosigao2ResultEntity.class);
		checkResultHolderIsNull(result, order.getPlateNumber());
		
		return result.isSuccess();
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

}
