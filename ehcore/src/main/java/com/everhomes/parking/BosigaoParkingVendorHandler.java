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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bosigao.cxf.Service1;
import com.bosigao.cxf.Service1Soap;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.organization.pm.pay.ResultHolder;
import com.everhomes.rest.parking.CreateParkingRechargeRateCommand;
import com.everhomes.rest.parking.DeleteParkingRechargeRateCommand;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingCardIssueFlag;
import com.everhomes.rest.parking.ParkingCardRequestDTO;
import com.everhomes.rest.parking.ParkingCardRequestStatus;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.parking.ParkingLotVendor;
import com.everhomes.rest.parking.ParkingOwnerType;
import com.everhomes.rest.parking.ParkingRechargeOrderRechargeStatus;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import com.everhomes.rest.parking.RequestParkingCardCommand;
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
	
	@Autowired
	private LocaleStringService localeStringService;
	
    @SuppressWarnings("unchecked")
	@Override
    public List<ParkingCardDTO> getParkingCardsByPlate(String ownerType, Long ownerId,
    		Long parkingLotId, String plateNumber) {
        
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
			
			String plateOwnerName = (String) card.get("userName");
			String carNumber = (String) card.get("carNumber");
			
			String validEnd = (String) card.get("validEnd");
			String cardNumber = (String) card.get("cardCode");
			String cardType = (String) card.get("cardDescript");
			String plateOwnerPhone = (String) card.get("mobile");
			Timestamp endTime = strToTimestamp(validEnd);
			
			if(!validStatus){
				return resultList;
			}
			parkingCardDTO.setOwnerType(ParkingOwnerType.COMMUNITY.getCode());
			parkingCardDTO.setOwnerId(ownerId);
			parkingCardDTO.setParkingLotId(parkingLotId);
			parkingCardDTO.setPlateOwnerName(plateOwnerName);
			parkingCardDTO.setPlateNumber(carNumber);
			//parkingCardDTO.setStartTime(startTime);
			parkingCardDTO.setEndTime(endTime.getTime());
			parkingCardDTO.setCardType(cardType);
			parkingCardDTO.setCardNumber(cardNumber);
			parkingCardDTO.setPlateOwnerPhone(plateOwnerPhone);
			
				parkingCardDTO.setIsValid(true);
				resultList.add(parkingCardDTO);
				
		}
        
        return resultList;
    }

    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(String ownerType, Long ownerId, Long parkingLotId,String plateNumber,String cardNo) {
        URL wsdlURL = Service1.WSDL_LOCATION;
        Service1 ss = new Service1(wsdlURL, Service1.SERVICE);
        Service1Soap port = ss.getService1Soap12();
        LOGGER.info("verifyRechargedPlate");
        String json = port.getCardInfo("", plateNumber, "2", "sign");
        
        ResultHolder resultHolder = GsonUtil.fromJson(json, ResultHolder.class);
        this.checkResultHolderIsNull(resultHolder,plateNumber);
        Map<String,Object> data = (Map<String, Object>) resultHolder.getData();
		Map<String,Object> card = (Map<String, Object>) data.get("card");
		Boolean validStatus =  (Boolean) card.get("valid");
		this.checkValidStatusIsNull(validStatus,plateNumber);

		String cardType = (String) card.get("cardDescript");
		List<ParkingRechargeRate> parkingRechargeRateList = parkingProvider.listParkingRechargeRates(ownerType, ownerId, parkingLotId,cardType);
		
		List<ParkingRechargeRateDTO> result = parkingRechargeRateList.stream().map(r->{
			ParkingRechargeRateDTO dto = new ParkingRechargeRateDTO();
			dto = ConvertHelper.convert(r, ParkingRechargeRateDTO.class);
			dto.setRateName(dto.getMonthCount().intValue()+"个月");
			dto.setRateToken(r.getId().toString());
			dto.setVendorName(ParkingLotVendor.BOSIGAO.getCode());
			return dto;
		}
		).collect(Collectors.toList());
		
		return result;
    }

    @Override
    public void notifyParkingRechargeOrderPayment(ParkingRechargeOrder order,String payStatus) {
    	if(order.getRechargeStatus() != ParkingRechargeOrderRechargeStatus.RECHARGED.getCode()) {
			if(payStatus.toLowerCase().equals("fail")) {
				LOGGER.error("pay failed.orderNo ="+order.getId());
			}
			else {
				String carNumber = order.getPlateNumber();
				String cost = (order.getPrice().intValue()*100) + "";
				String flag = "2"; //停车场系统接口的传入参数，2表示是车牌号
				String payTime = order.getPaidTime().toString();
				String validStart = timestampToStr(order.getOldExpiredTime());
				String validEnd = timestampToStr(order.getNewExpiredTime());
				
				URL wsdlURL = Service1.WSDL_LOCATION;
				
				Service1 ss = new Service1(wsdlURL, Service1.SERVICE);
		        Service1Soap port = ss.getService1Soap12();
		        LOGGER.info("refreshParkingSystem");
		        
		        String json = port.cardPayMoney("", carNumber, flag, cost, validStart, validEnd, payTime, "sign");
				
				ResultHolder resultHolder = GsonUtil.fromJson(json, ResultHolder.class);
				checkResultHolderIsNull(resultHolder,carNumber);
				
				if(resultHolder.isSuccess()){
					order.setRechargeStatus(ParkingRechargeOrderRechargeStatus.RECHARGED.getCode());
					order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
					parkingProvider.updateParkingRechargeOrder(order);
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
    
	private String timestampToStr(Timestamp time) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String str = sdf.format(time);
		return str;
	}
	
	@Override
	public ParkingCardRequestDTO getRequestParkingCard(RequestParkingCardCommand cmd) {
        List<ParkingCardDTO> cardList = getParkingCardsByPlate(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId(),
        		cmd.getPlateNumber());
        User user = UserContext.current().getUser();
		if(cardList.size()>0){
			LOGGER.error("the plateNumber card is existed .");
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_PLATE_EXIST,
					localeStringService.getLocalizedString(String.valueOf(ParkingErrorCode.SCOPE), 
							String.valueOf(ParkingErrorCode.ERROR_PLATE_EXIST),
							UserContext.current().getUser().getLocale(),"the plateNumber card is existed."));
		}

        if(cardList.size() == 0){
        	
        	List<ParkingCardRequest> list = parkingProvider.listParkingCardRequests(user.getId(),cmd.getOwnerType(), 
        			cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(),null,
        			ParkingCardRequestStatus.INACTIVE.getCode(), null, null);
        	if(list.size()>0){
        		LOGGER.error("the plateNumber is already applied.");
    			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_PLATE_APPLIED,
    					localeStringService.getLocalizedString(String.valueOf(ParkingErrorCode.SCOPE), 
    							String.valueOf(ParkingErrorCode.ERROR_PLATE_APPLIED),
    							UserContext.current().getUser().getLocale(),"the plateNumber is already applied."));
        	}
        }
		
		ParkingCardRequestDTO parkingCardRequestDTO = new ParkingCardRequestDTO();
		try {
			ParkingCardRequest parkingCardRequest = new ParkingCardRequest();
			parkingCardRequest.setOwnerId(cmd.getOwnerId());
			parkingCardRequest.setOwnerType(cmd.getOwnerType());
			parkingCardRequest.setParkingLotId(cmd.getParkingLotId());
			parkingCardRequest.setRequestorEnterpriseId(cmd.getRequestorEnterpriseId());
			parkingCardRequest.setPlateNumber(cmd.getPlateNumber());
			parkingCardRequest.setPlateOwnerEntperiseName(cmd.getPlateOwnerEntperiseName());
			parkingCardRequest.setPlateOwnerName(cmd.getPlateOwnerName());
			parkingCardRequest.setPlateOwnerPhone(cmd.getPlateOwnerPhone());
			parkingCardRequest.setRequestorUid(user.getId());
			//设置一些初始状态
			parkingCardRequest.setIssueFlag(ParkingCardIssueFlag.UNISSUED.getCode());
			parkingCardRequest.setStatus(ParkingCardRequestStatus.QUEUEING.getCode());
			parkingCardRequest.setCreatorUid(user.getId());
			parkingCardRequest.setCreateTime(new Timestamp(System.currentTimeMillis()));
			
			parkingProvider.requestParkingCard(parkingCardRequest);
			
			parkingCardRequestDTO = ConvertHelper.convert(parkingCardRequest, ParkingCardRequestDTO.class);
			
			Integer count = parkingProvider.waitingCardCount(cmd.getOwnerType(), 
					cmd.getOwnerId(), cmd.getParkingLotId(), parkingCardRequest.getCreateTime());
			parkingCardRequestDTO.setRanking(count);
		} catch(Exception e) {
			LOGGER.error("requestParkingCard is fail.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_SQL_EXCEPTION,
					"requestParkingCard is fail." + e.toString());
		}
		return parkingCardRequestDTO;
	}
	
	@Scheduled(cron="0 0 0/2 * * ? ")
	@Override
	public void refreshParkingRechargeOrderStatus() {
		LOGGER.info("refresh recharge status.");
		List<ParkingRechargeOrder> orderList = parkingProvider.findWaitingParkingRechargeOrders(ParkingLotVendor.BOSIGAO);
		orderList.stream().map(order -> {
			String carNumber = order.getPlateNumber();
			String cost = order.getPrice().intValue() + "";
			String flag = "2"; //停车场系统接口的传入参数，2表示是车牌号
			String payTime = order.getPaidTime().toString();
			String validStart = timestampToStr(order.getOldExpiredTime());
			String validEnd = timestampToStr(order.getNewExpiredTime());
			
			URL wsdlURL = Service1.WSDL_LOCATION;
			
			Service1 ss = new Service1(wsdlURL, Service1.SERVICE);
	        Service1Soap port = ss.getService1Soap12();
	        LOGGER.info("refreshParkingSystem");
	        
	        String json = port.cardPayMoney("", carNumber, flag, cost, validStart, validEnd, payTime, "sign");
			
			ResultHolder resultHolder = GsonUtil.fromJson(json, ResultHolder.class);
			checkResultHolderIsNull(resultHolder,carNumber);
			
			if(resultHolder.isSuccess()){
				order.setRechargeStatus(ParkingRechargeOrderRechargeStatus.RECHARGED.getCode());
				order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
				parkingProvider.updateParkingRechargeOrder(order);
			}
			return null;
		});
	}
}
