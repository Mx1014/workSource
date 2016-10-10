// @formatter:off
package com.everhomes.parking;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.bosigao.cxf.Service1;
import com.bosigao.cxf.Service1Soap;
import com.bosigao.cxf.rest.CardInfo;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.order.OrderUtil;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.organization.pm.pay.ResultHolder;
import com.everhomes.rest.order.CommonOrderCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.parking.CreateParkingRechargeOrderCommand;
import com.everhomes.rest.parking.CreateParkingRechargeRateCommand;
import com.everhomes.rest.parking.DeleteParkingRechargeRateCommand;
import com.everhomes.rest.parking.ListCardTypeCommand;
import com.everhomes.rest.parking.ListCardTypeResponse;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingCardIssueFlag;
import com.everhomes.rest.parking.ParkingCardRequestDTO;
import com.everhomes.rest.parking.ParkingCardRequestStatus;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.parking.ParkingLotVendor;
import com.everhomes.rest.parking.ParkingNotificationTemplateCode;
import com.everhomes.rest.parking.ParkingOwnerType;
import com.everhomes.rest.parking.ParkingRechargeOrderRechargeStatus;
import com.everhomes.rest.parking.ParkingRechargeOrderStatus;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import com.everhomes.rest.parking.ParkingRechargeRateStatus;
import com.everhomes.rest.parking.RequestParkingCardCommand;
import com.everhomes.rest.techpark.park.GetAllCardDescriptDTO;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
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
	
	@Autowired
	private LocaleTemplateService localeTemplateService;
	
	@Autowired
    private ConfigurationProvider configProvider;
	
	@Autowired
    private UserProvider userProvider;
	
	@Autowired
	private OrderUtil commonOrderUtil;
	
	@Override
    public List<ParkingCardDTO> getParkingCardsByPlate(String ownerType, Long ownerId,
    		Long parkingLotId, String plateNumber) {
        
    	List<ParkingCardDTO> resultList = new ArrayList<ParkingCardDTO>();
    	
    	CardInfo card = getCardInfo(plateNumber);
    	
        ParkingCardDTO parkingCardDTO = new ParkingCardDTO();
		if(null != card){
			
//			Boolean validStatus = card.getValid();
//			this.checkValidStatusIsNull(validStatus,plateNumber);
//			
//			if(!validStatus){
//				return resultList;
//			}
			String validEnd = card.getValidEnd();
			Long endTime = strToLong(validEnd);
			long now = System.currentTimeMillis();
			
			if(endTime < now){
				return resultList;
			}
			
			String plateOwnerName = card.getUserName();
			String carNumber = card.getCarNumber();
			
			String cardNumber = card.getCardCode();
			String cardType = card.getCardDescript();
			String plateOwnerPhone = card.getMobile();
			
			
			parkingCardDTO.setOwnerType(ParkingOwnerType.COMMUNITY.getCode());
			parkingCardDTO.setOwnerId(ownerId);
			parkingCardDTO.setParkingLotId(parkingLotId);
			parkingCardDTO.setPlateOwnerName(StringUtils.isBlank(plateOwnerName)?configProvider.getValue("parking.default.nickname", ""):plateOwnerName);
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

    private CardInfo getCardInfo(String plateNumber){
    	
    	URL wsdlURL = Service1.WSDL_LOCATION;
		Service1 ss = new Service1(wsdlURL, Service1.SERVICE);
        Service1Soap port = ss.getService1Soap12();
        
        String json = port.getCardInfo("", plateNumber, "2", "sign");
        
        if(LOGGER.isDebugEnabled())
			LOGGER.debug("Parking bosigao json={}", json);
        
        ResultHolder resultHolder = JSONObject.parseObject(json, ResultHolder.class);
        this.checkResultHolderIsNull(resultHolder, plateNumber);
        
        CardInfo card = null;
        
        if(resultHolder.isSuccess()){
        	String cardJson = JSONObject.parseObject(resultHolder.getData().toString()).get("card").toString();
            if(LOGGER.isDebugEnabled())
    			LOGGER.debug("Parking bosigao cardJson={}", cardJson);
            
            card = JSONObject.parseObject(cardJson, CardInfo.class);
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
        
        GetAllCardDescriptDTO cardDescriptDTO = GsonUtil.fromJson(json, GetAllCardDescriptDTO.class);

		if(cardDescriptDTO.isSuccess()){
			ret.setCardTypes(cardDescriptDTO.getCardDescript());
		}
    	return ret;
    }
    
    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(String ownerType, Long ownerId, Long parkingLotId,String plateNumber,String cardNo) {
    	List<ParkingRechargeRate> parkingRechargeRateList = null;
    	List<ParkingRechargeRateDTO> result = null;
    	if(StringUtils.isBlank(plateNumber)) {
    		parkingRechargeRateList = parkingProvider.listParkingRechargeRates(ownerType, ownerId, parkingLotId,null);
    		
    	}else{
    		
    		CardInfo card = getCardInfo(plateNumber);
    		String cardType = card.getCardDescript();
    		parkingRechargeRateList = parkingProvider.listParkingRechargeRates(ownerType, ownerId, parkingLotId,cardType);
    	}
		result = parkingRechargeRateList.stream().map(r->{
			ParkingRechargeRateDTO dto = new ParkingRechargeRateDTO();
			dto = ConvertHelper.convert(r, ParkingRechargeRateDTO.class);
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
				LOGGER.error("Parking pay failed, order={}", order);
			}
			else {
				ResultHolder resultHolder = recharge(order);
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
    
	@Override
	public ParkingCardRequestDTO getRequestParkingCard(RequestParkingCardCommand cmd) {
        List<ParkingCardDTO> cardList = getParkingCardsByPlate(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId(),
        		cmd.getPlateNumber());
        User user = UserContext.current().getUser();
		if(cardList.size()>0){
			LOGGER.error("PlateNumber card is existed, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_PLATE_EXIST,
					"PlateNumber card is existed");
		}

        if(cardList.size() == 0){
        	List<ParkingCardRequest> list = parkingProvider.listParkingCardRequests(user.getId(),cmd.getOwnerType(), 
        			cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(),null,
        			ParkingCardRequestStatus.INACTIVE.getCode(), null, null);
        	if(list.size()>0){
        		LOGGER.error("PlateNumber is already applied.");
    			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_PLATE_APPLIED,
    					"the plateNumber is already applied.");
        	}
        }
		
		ParkingCardRequestDTO parkingCardRequestDTO = new ParkingCardRequestDTO();
		
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
		
		return parkingCardRequestDTO;
	}
	
	@Scheduled(cron="0 0 0/2 * * ? ")
	@Override
	public void refreshParkingRechargeOrderStatus() {
		LOGGER.info("Refresh recharge status.");
		List<ParkingRechargeOrder> orderList = parkingProvider.findWaitingParkingRechargeOrders(ParkingLotVendor.BOSIGAO);
		orderList.stream().map(order -> {
			
			ResultHolder resultHolder = recharge(order);
			if(resultHolder.isSuccess()){
				order.setRechargeStatus(ParkingRechargeOrderRechargeStatus.RECHARGED.getCode());
				order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
				parkingProvider.updateParkingRechargeOrder(order);
			}
			return null;
		});
	}

	@Override
	public CommonOrderDTO createParkingRechargeOrder(CreateParkingRechargeOrderCommand cmd, ParkingLot parkingLot) {
		
		ParkingRechargeOrder parkingRechargeOrder = new ParkingRechargeOrder();
		
		User user = UserContext.current().getUser();
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
		
		parkingRechargeOrder.setOwnerType(cmd.getOwnerType());
		parkingRechargeOrder.setOwnerId(cmd.getOwnerId());
		parkingRechargeOrder.setParkingLotId(parkingLot.getId());
		parkingRechargeOrder.setPlateNumber(cmd.getPlateNumber());
		parkingRechargeOrder.setPlateOwnerName(cmd.getPlateOwnerName());
		parkingRechargeOrder.setPlateOwnerPhone(cmd.getPlateOwnerPhone());
		parkingRechargeOrder.setPayerEnterpriseId(cmd.getPayerEnterpriseId());
		parkingRechargeOrder.setPayerUid(user.getId());
		parkingRechargeOrder.setPayerPhone(userIdentifier.getIdentifierToken());
		parkingRechargeOrder.setVendorName(parkingLot.getVendorName());
		parkingRechargeOrder.setCardNumber(cmd.getCardNumber());
		
		ParkingRechargeRate rate = parkingProvider.findParkingRechargeRatesById(Long.parseLong(cmd.getRateToken()));
		parkingRechargeOrder.setRateToken(rate.getId().toString());
		parkingRechargeOrder.setRateName(rate.getRateName());
		parkingRechargeOrder.setMonthCount(rate.getMonthCount());
		parkingRechargeOrder.setPrice(rate.getPrice());
		
		parkingRechargeOrder.setStatus(ParkingRechargeOrderStatus.UNPAID.getCode());
		parkingRechargeOrder.setRechargeStatus(ParkingRechargeOrderRechargeStatus.UNRECHARGED.getCode());
		parkingRechargeOrder.setCreatorUid(user.getId());
		parkingRechargeOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
		parkingRechargeOrder.setOrderNo(createOrderNo(System.currentTimeMillis()));
		parkingRechargeOrder.setNewExpiredTime(addMonth(cmd.getExpiredTime(), cmd.getMonthCount()));
		parkingRechargeOrder.setOldExpiredTime(addDays(cmd.getExpiredTime(), 1));
		
		parkingProvider.createParkingRechargeOrder(parkingRechargeOrder);	
		
		//调用统一处理订单接口，返回统一订单格式
		CommonOrderCommand orderCmd = new CommonOrderCommand();
		orderCmd.setBody(parkingRechargeOrder.getRateName());
		orderCmd.setOrderNo(parkingRechargeOrder.getId().toString());
		orderCmd.setOrderType(OrderType.OrderTypeEnum.PARKING.getPycode());
		orderCmd.setSubject("停车充值订单简要描述");
		orderCmd.setTotalFee(parkingRechargeOrder.getPrice());
		CommonOrderDTO dto = null;
		try {
			dto = commonOrderUtil.convertToCommonOrderTemplate(orderCmd);
		} catch (Exception e) {
			LOGGER.error("convertToCommonOrder is fail. {}",e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"convertToCommonOrder is fail.");
		}
		return dto;
	}
	
	private ResultHolder recharge(ParkingRechargeOrder order){
		
		String carNumber = order.getPlateNumber();
		String cost = String.valueOf((order.getPrice().intValue() * 100));
		String flag = "2"; //停车场系统接口的传入参数，2表示是车牌号
		String payTime = order.getPaidTime().toString();
		
		CardInfo card = getCardInfo(carNumber);
		String oldValidEnd = card.getValidEnd();
		Long time = strToLong(oldValidEnd);
		
		String validStart = timestampToStr(addDays(time, 1));
		String validEnd = timestampToStr(addMonth(time, order.getMonthCount().intValue()));
		
		URL wsdlURL = Service1.WSDL_LOCATION;
		Service1 ss = new Service1(wsdlURL, Service1.SERVICE);
        Service1Soap port = ss.getService1Soap12();
        
        String json = port.cardPayMoney("", carNumber, flag, cost, validStart, validEnd, payTime, "sign");
		
		ResultHolder resultHolder = GsonUtil.fromJson(json, ResultHolder.class);
		checkResultHolderIsNull(resultHolder,carNumber);
		
		return resultHolder;
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
    
	private String timestampToStr(Timestamp time) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String str = sdf.format(time);
		return str;
	}
	
    private Long createOrderNo(Long time) {
		String bill = String.valueOf(time) + (int) (Math.random()*1000);
		return Long.valueOf(bill);
	}
    
	private Timestamp addDays(Long oldPeriod, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(oldPeriod);
		calendar.add(Calendar.DATE, days);
		Timestamp time = new Timestamp(calendar.getTimeInMillis());
		
		return time;
	}
	
	private Timestamp addMonth(Long oldPeriod, int month) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(oldPeriod);
		
		int day = calendar.get(Calendar.DAY_OF_MONTH); 
		if(day == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){
			calendar.add(Calendar.MONTH, month);
			int d = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, d);
		}else{
			calendar.add(Calendar.MONTH, month-1);
			int d = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, d);
		}
		
		Timestamp newPeriod = new Timestamp(calendar.getTimeInMillis());
		
		return newPeriod;
	}
	
}
