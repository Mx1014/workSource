// @formatter:off
package com.everhomes.parking;

import java.net.URL;
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

import com.bosigao.cxf.Service1;
import com.bosigao.cxf.Service1Soap;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.organization.pm.pay.ResultHolder;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import com.everhomes.rest.techpark.onlinePay.OnlinePayBillCommand;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

// "BOSIGAO"需与ParkingLotVendor.BOSIGAO的枚举值保持一致
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "BOSIGAO")
public class BosigaoParkingVendorHandler implements ParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(BosigaoParkingVendorHandler.class);

	@Autowired
	private ParkingProvider parkingProvider;
	
    @Override
    public List<ParkingCardDTO> getParkingCardsByPlate(String ownerType, Long ownerId, Long parkingLotId,
        String plateNumber) {
        
    	List<ParkingCardDTO> resultList = new ArrayList<ParkingCardDTO>();
    	URL wsdlURL = Service1.WSDL_LOCATION;
		
		Service1 ss = new Service1(wsdlURL, Service1.SERVICE);
        Service1Soap port = ss.getService1Soap12();
        LOGGER.info("verifyRechargedPlate");
        String json = port.getCardInfo("", plateNumber, "2", "sign");
        
        ResultHolder resultHolder = GsonUtil.fromJson(json, ResultHolder.class);
        this.checkResultHolderIsNull(resultHolder,plateNumber);
        
        if(LOGGER.isDebugEnabled())
			LOGGER.error("resultHolder="+resultHolder.isSuccess());

        ParkingCardDTO parkingCardDTO = new ParkingCardDTO();
		if(resultHolder.isSuccess()){
			Map<String,Object> data = (Map<String, Object>) resultHolder.getData();
			Map<String,Object> card = (Map<String, Object>) data.get("card");
			Boolean validStatus =  (Boolean) card.get("valid");
			this.checkValidStatusIsNull(validStatus,plateNumber);

			if(LOGGER.isDebugEnabled())
				LOGGER.error("validStatus="+validStatus);

			if(!validStatus){
				parkingCardDTO.setIsValid(false);
				resultList.add(parkingCardDTO);
				return resultList;
			}
			else if(validStatus){
				String plateOwnerName = (String) card.get("userName");
				String carNumber = (String) card.get("carNumber");
				
				String validEnd = (String) card.get("validEnd");
				String cardNumber = (String) card.get("cardCode");
				String cardType = (String) card.get("cardDescript");
				String plateOwnerPhone = (String) card.get("mobile");
				Timestamp endTime = strToTimestamp(validEnd);

				parkingCardDTO.setPlateOwnerName(plateOwnerName);
				parkingCardDTO.setPlateNumber(carNumber);
				parkingCardDTO.setEndTime(endTime);
				//parkingCardDTO.setCardType(cardType);
				parkingCardDTO.setCardNumber(cardNumber);
				parkingCardDTO.setPlateOwnerPhone(plateOwnerPhone);
				parkingCardDTO.setIsValid(true);
				
				if(LOGGER.isDebugEnabled())
					LOGGER.error("successcommand="+parkingCardDTO.toString());
				resultList.add(parkingCardDTO);
				return resultList;
			}
		}
        
        return resultList;
    }

    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(String ownerType, Long ownerId, Long parkingLotId) {
        
		List<ParkingRechargeRate> parkingRechargeRateList = parkingProvider.listParkingRechargeRates(ownerType, ownerId, parkingLotId);
		
		List<ParkingRechargeRateDTO> result = parkingRechargeRateList.stream().map(r->
			ConvertHelper.convert(r, ParkingRechargeRateDTO.class)
		).collect(Collectors.toList());
		
		return result;
    }

    @Override
    public void notifyParkingRechargeOrderPayment(OnlinePayBillCommand cmd) {
        // TODO Auto-generated method stub
        
    }
    
    private void checkResultHolderIsNull(ResultHolder resultHolder,String plateNo) {
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
    
    private Timestamp strToTimestamp(String str) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		Timestamp ts = null;
		try {
			ts = new Timestamp(sdf.parse(str).getTime());
		} catch (ParseException e) {
			LOGGER.error("validityPeriod data format is not yyyymmdd.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"validityPeriod data format is not yyyymmdd.");
		}
		
		return ts;
	}
}
