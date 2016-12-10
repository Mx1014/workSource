// @formatter:off
package com.everhomes.parking;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.parking.etcp.ETCPJsonRsult;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.parking.CreateParkingRechargeOrderCommand;
import com.everhomes.rest.parking.CreateParkingRechargeRateCommand;
import com.everhomes.rest.parking.DeleteParkingRechargeRateCommand;
import com.everhomes.rest.parking.GetOpenCardInfoCommand;
import com.everhomes.rest.parking.ListCardTypeCommand;
import com.everhomes.rest.parking.ListCardTypeResponse;
import com.everhomes.rest.parking.OpenCardInfoDTO;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import com.everhomes.rest.parking.ParkingTempFeeDTO;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.RuntimeErrorException;

// "ETCP"需与ParkingLotVendor.ETCP的枚举值保持一致
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "ETCP")
public class EtcpParkingVendorHandler implements ParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(EtcpParkingVendorHandler.class);

	@Autowired
	private ParkingProvider parkingProvider;
	@Autowired
	private UserProvider userProvider;

    @SuppressWarnings("unchecked")
	@Override
    public List<ParkingCardDTO> getParkingCardsByPlate(String ownerType, Long ownerId, Long parkingLotId,
        String plateNumber) {
    	
    	List<ParkingCardDTO> resultList = new ArrayList<ParkingCardDTO>();
    	
		User user = UserContext.current().getUser();
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
		String mobile = userIdentifier.getIdentifierToken();
		
		String json = "";
		ETCPJsonRsult result = GsonUtil.fromJson(json, ETCPJsonRsult.class);
		
		if(result.getCode().equals("0")){
			Map<String,Object> data =  (Map<String, Object>) result.getData();
			List cars = (List) data.get("cars");
			if(!cars.stream().anyMatch(r -> ((Map) r).get("plate_number").equals(plateNumber))){
				LOGGER.error("plateNumber is not match with user.");
	    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
	    				"plateNumber is not match with user.");
			}
			List locations = (List) data.get("locations");
			
			String plateOwnerPhone = (String) data.get("mobile");
			String realName = (String) data.get("real_name");
			
			
			
			resultList = (List<ParkingCardDTO>) locations.stream().map(r ->{
				Map<String,Object> map = (Map<String,Object>)r;
				String locationNumber = (String) map.get("location_number");
				String startTime = (String) map.get("startTime");
				String endTime = (String) map.get("endTime");
				String rateName = (String) map.get("rateName");
				String price = (String) map.get("price");
				
				ParkingCardDTO dto = new ParkingCardDTO();
				dto.setOwnerType(ownerType);
				dto.setOwnerId(ownerId);
				dto.setCardNumber(locationNumber);
//				dto.setStartTime(startTime);
				Timestamp expiredTime = strToTimeStamp(endTime);
//				dto.setEndTime(endTime);
				dto.setPlateNumber(plateNumber);
				dto.setPlateOwnerName(realName);
				dto.setPlateOwnerPhone(plateOwnerPhone);
				dto.setParkingLotId(parkingLotId);
				//dto.setCardType(cardType);
				long currentTime = System.currentTimeMillis();
				if(currentTime < expiredTime.getTime())
					dto.setIsValid(true);
				else
					dto.setIsValid(false);
				return dto;
			}).collect(Collectors.toList());
    	}
		
        return resultList;
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(String ownerType, Long ownerId, Long parkingLotId,String plateNumber,String cardNo) {
    	List<ParkingRechargeRateDTO> resultList = new ArrayList<ParkingRechargeRateDTO>();
    	
    	String json = "";

    	ETCPJsonRsult result = GsonUtil.fromJson(json, ETCPJsonRsult.class);
    	if(result.getCode().equals("0")){
    		List data = (List) result.getData();
    		data.stream().forEach(r ->{
    			Map<String,Object> areaRates = (Map<String,Object>)r;
    			List rates = (List) areaRates.get("rate");
    			String areaId = (String) areaRates.get("areaId");
    			String areaName = (String) areaRates.get("name");
    			List<ParkingRechargeRateDTO> temp = (List<ParkingRechargeRateDTO>) rates.stream().map(rate ->{
    				Map<String,Object> rateMap = (Map<String,Object>)rate;
    				ParkingRechargeRateDTO dto = new ParkingRechargeRateDTO();
    				dto.setOwnerType(ownerType);
    				dto.setOwnerId(ownerId);
    				dto.setParkingLotId(parkingLotId);
    				
    				String rateId = (String) rateMap.get("rateId");
    				String rateName = (String) rateMap.get("name");
    				String price = (String) rateMap.get("price");
    				dto.setRateToken(rateId);
    				dto.setRateName(rateName);
    				dto.setPrice(new BigDecimal(price));
    				dto.setAreaId(Long.parseLong(areaId));
    				return dto;
    			});
    			resultList.addAll(temp);
    		});
    	}
    	return resultList;
    }

    @Override
	public ParkingRechargeRateDTO createParkingRechargeRate(
			CreateParkingRechargeRateCommand cmd) {
		LOGGER.error("not support create parkingRechageRate.");
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE,
				"not support create parkingRechageRate.");
	}

	@Override
	public void deleteParkingRechargeRate(DeleteParkingRechargeRateCommand cmd) {
		LOGGER.error("not support delete parkingRechageRate.");
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE,
				"not support delete parkingRechageRate.");
	}

	@Override
	public void notifyParkingRechargeOrderPayment(ParkingRechargeOrder order,
			String payStatus) {
		
	}
	
	private Timestamp strToTimeStamp(String s){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Timestamp time = null;
		try {
			time = new Timestamp(sdf.parse(s).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}

	@Override
	public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateParkingRechargeOrderRate(ParkingRechargeOrder order) {
		// TODO Auto-generated method stub
		
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
}
