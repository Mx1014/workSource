// @formatter:off
package com.everhomes.parking;

import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bosigao.cxf.Service1;
import com.bosigao.cxf.Service1Soap;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.organization.pm.pay.ResultHolder;
import com.everhomes.rest.parking.CreateParkingRechargeRateCommand;
import com.everhomes.rest.parking.DeleteParkingRechargeRateCommand;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingLotVendor;
import com.everhomes.rest.parking.ParkingOwnerType;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import com.everhomes.rest.techpark.onlinePay.OnlinePayBillCommand;
import com.everhomes.rest.techpark.onlinePay.PayStatus;
import com.everhomes.rest.techpark.onlinePay.RechargeStatus;
import com.everhomes.rest.techpark.park.RechargeInfoDTO;
import com.everhomes.rest.techpark.park.RechargeSuccessResponse;
import com.everhomes.techpark.park.RechargeInfo;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
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
				
				parkingCardDTO.setOwnerType(ParkingOwnerType.COMMUNITY.getCode());
				parkingCardDTO.setOwnerId(ownerId);
				parkingCardDTO.setParkingLotId(parkingLotId);
				parkingCardDTO.setPlateOwnerName(plateOwnerName);
				parkingCardDTO.setPlateNumber(carNumber);
				//parkingCardDTO.setStartTime(startTime);
				parkingCardDTO.setEndTime(endTime);
				parkingCardDTO.setCardType(cardType);
				parkingCardDTO.setCardNumber(cardNumber);
				parkingCardDTO.setPlateOwnerPhone(plateOwnerPhone);
				parkingCardDTO.setIsValid(true);
				
				if(LOGGER.isDebugEnabled())
					LOGGER.error("successcommand="+parkingCardDTO.toString());
				resultList.add(parkingCardDTO);
				
				/** ---------以下为假数据 start -------------**/
				ParkingCardDTO test = new ParkingCardDTO();
				test = ConvertHelper.convert(parkingCardDTO, ParkingCardDTO.class);
				test.setPlateOwnerName("测试卡");
				test.setCardNumber("2133");
				resultList.add(test);
				/**---------- 以下为假数据 end  --------------**/
				
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
//    	ParkingRechargeOrder order = parkingProvider.findParkingRechargeOrderById(Long.parseLong(cmd.getOrderNo()));
//		if(info.getRechargeStatus() != RechargeStatus.SUCCESS.getCode()) {
//			if(cmd.getPayStatus().toLowerCase().equals("fail")) {
//				LOGGER.error("pay failed.orderNo ="+cmd.getOrderNo());
//			}
//			else {
//				String carNumber = info.getPlateNumber();
//				String cost = (int)(info.getRechargeAmount()*100) +"";
//				String flag = "2"; //停车场系统接口的传入参数，2表示是车牌号
//				String payTime = info.getRechargeTime().toString();
//				String validStart = timestampToStr(info.getOldValidityperiod());
//				String validEnd = timestampToStr(info.getNewValidityperiod());
//				
//				URL wsdlURL = Service1.WSDL_LOCATION;
//				
//				Service1 ss = new Service1(wsdlURL, SERVICE_NAME);
//		        Service1Soap port = ss.getService1Soap12();
//		        LOGGER.info("refreshParkingSystem");
//		        
//		        String json = port.cardPayMoney("", carNumber, flag, cost, validStart, validEnd, payTime, "sign");
//				
//				ResultHolder resultHolder = GsonUtil.fromJson(json, ResultHolder.class);
//				this.checkResultHolderIsNull(resultHolder,carNumber);
//				
//				if(resultHolder.isSuccess()){
//					updateRechargeOrder(Long.valueOf(cmd.getOrderNo()));
//				}
//			}
//		}
//		RechargeSuccessResponse rechargeResponse = getRechargeStatus(Long.valueOf(cmd.getOrderNo()));
//		return rechargeResponse;        
    }
    
    @Override
    public ParkingRechargeRateDTO createParkingRechargeRate(CreateParkingRechargeRateCommand cmd){
    	User user = UserContext.current().getUser();
    	
    	ParkingRechargeRate parkingRechargeRate = new ParkingRechargeRate();
    	parkingRechargeRate.setOwnerType(cmd.getOwnerType());
    	parkingRechargeRate.setOwnerId(cmd.getOwnerId());
    	parkingRechargeRate.setParkingLotId(cmd.getParkingLotId());
    	parkingRechargeRate.setRateName(cmd.getRateName());
    	parkingRechargeRate.setMonthCount(cmd.getMonthCount());
    	parkingRechargeRate.setPrice(cmd.getPrice());
    	parkingRechargeRate.setCreatorUid(user.getId());
    	parkingProvider.createParkingRechargeRate(parkingRechargeRate);
    	return ConvertHelper.convert(parkingRechargeRate, ParkingRechargeRateDTO.class);
    }
    
    @Override
    public void deleteParkingRechargeRate(DeleteParkingRechargeRateCommand cmd){
    	try {
    		parkingProvider.findParkingRechargeRatesById(Long.parseLong(cmd.getRateToken()));
		} catch (Exception e) {
			LOGGER.error("delete parkingRechargeRate fail."+cmd.getRateToken());
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_SQL_EXCEPTION,
    				"delete parkingRechargeRate fail."+cmd.getRateToken());
		}
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
    
//    private RechargeInfoDTO onlinePayBill(OnlinePayBillCommand cmd) {
//		
//		RechargeInfo order = new RechargeInfo();
//		//fail
//		if(cmd.getPayStatus().toLowerCase().equals("fail"))
//			order = this.onlinePayBillFail(cmd);
//		//success
//		if(cmd.getPayStatus().toLowerCase().equals("success"))
//			order = this.onlinePayBillSuccess(cmd);
//
//		RechargeInfoDTO info = ConvertHelper.convert(order, RechargeInfoDTO.class);
//		return info;
//	}
    
//    private ParkingRechargeOrder onlinePayBillFail(OnlinePayBillCommand cmd) {
//		
//		if(LOGGER.isDebugEnabled())
//			LOGGER.error("onlinePayBillFail");
//		this.checkOrderNoIsNull(cmd.getOrderNo());
//		Long orderId = Long.parseLong(cmd.getOrderNo());
//		
//		ParkingRechargeOrder order = checkOrder(orderId);
//				
//		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
//		updateOrderStatus(order, currentTimestamp, PayStatus.INACTIVE.getCode(), RechargeStatus.INACTIVE.getCode());
//		order.setPaidTime(cmd.getPayTime());
//		return order;
//	}
	
//	private RechargeInfo onlinePayBillSuccess(OnlinePayBillCommand cmd) {
//		
//		if(LOGGER.isDebugEnabled())
//			LOGGER.error("onlinePayBillSuccess");
//		this.checkOrderNoIsNull(cmd.getOrderNo());
//		this.checkVendorTypeIsNull(cmd.getVendorType());
//		this.checkPayAmountIsNull(cmd.getPayAmount());
//		
//		Long orderId = this.convertOrderNoToOrderId(cmd.getOrderNo());
//		RechargeInfo order = this.checkOrder(orderId);
//		
//		double payAmount = new Double(cmd.getPayAmount());
//		
//		Long payTime = System.currentTimeMillis();
//		Timestamp payTimeStamp = new Timestamp(payTime);
//		
//		this.checkVendorTypeFormat(cmd.getVendorType());
//		
//		if(order.getPaymentStatus().byteValue() == PayStatus.WAITING_FOR_PAY.getCode()) {
//			order.setRechargeAmount(payAmount);
//			this.updateOrderStatus(order, payTimeStamp, PayStatus.PAID.getCode(), RechargeStatus.UPDATING.getCode());
//			
//		}
//		
//		return order;
//	}
	
	private void checkOrderNoIsNull(String orderNo) {
		
		if(orderNo == null || orderNo.trim().equals("")){
			LOGGER.error("orderNo is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"orderNo is null or empty.");
		}

	}
	
	private ParkingRechargeOrder checkOrder(Long orderId) {
		
    	ParkingRechargeOrder order = parkingProvider.findParkingRechargeOrderById(orderId);
		
		if(order == null){
			LOGGER.error("the order {} not found.",orderId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the order not found.");
		}
		return order;
	}
}
