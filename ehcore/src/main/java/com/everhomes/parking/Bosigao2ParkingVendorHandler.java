// @formatter:off
package com.everhomes.parking;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.bosigao2.ParkWebService;
import com.bosigao2.ParkWebServiceSoap;
import com.bosigao2.rest.CardInfo;
import com.bosigao2.rest.GetCardCommand;
import com.bosigao2.rest.RechargeCommand;
import com.bosigao2.rest.ResultEntity;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.organization.pm.pay.GsonUtil;
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
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import com.everhomes.rest.parking.ParkingRechargeRateStatus;
import com.everhomes.rest.parking.RequestParkingCardCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "BOSIGAO2")
public class Bosigao2ParkingVendorHandler implements ParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(Bosigao2ParkingVendorHandler.class);
	
	private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");

	private static final String RECHARGE = "Parking_MonthlyFee";
	private static final String GET_CARD = "Parking_GetMonthCard";
	private static final String GET_TYPES = "Parking_GetMonthCardDescript";
	
	private static final String FLAG1 = "1"; //1:卡号
	private static final String FLAG2 = "2"; //2:车牌
	
	private static ParkWebService service = new ParkWebService();
	private static ParkWebServiceSoap port = service.getParkWebServiceSoap();
	
	@Autowired
	private ParkingProvider parkingProvider;
	
	@Autowired
	private LocaleStringService localeStringService;
	
	@Autowired
	private LocaleTemplateService localeTemplateService;
	
	@Autowired
    private ConfigurationProvider configProvider;
	
	{
		
	}
	
	@Override
    public List<ParkingCardDTO> getParkingCardsByPlate(String ownerType, Long ownerId,
    		Long parkingLotId, String plateNumber) {
        
    	List<ParkingCardDTO> resultList = new ArrayList<ParkingCardDTO>();
    	
        ResultEntity result = getCard(plateNumber);

        ParkingCardDTO parkingCardDTO = new ParkingCardDTO();
		if(result.isSuccess()){
			CardInfo cardInfo = (CardInfo) result.getResult();
			String expireDate =  cardInfo.getExpireDate();
			this.checkExpireDateIsNull(expireDate,plateNumber);

			long expireTime = strToLong(expireDate);
			long now = System.currentTimeMillis();
			
			if(expireTime < now){
				return resultList;
			}
			parkingCardDTO.setOwnerType(ParkingOwnerType.COMMUNITY.getCode());
			parkingCardDTO.setOwnerId(ownerId);
			parkingCardDTO.setParkingLotId(parkingLotId);
			parkingCardDTO.setPlateOwnerName(StringUtils.isBlank(cardInfo.getUserName())?configProvider.getValue("parking.default.nickname", ""):cardInfo.getUserName());
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

    @SuppressWarnings("unchecked")
	public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
    	ListCardTypeResponse ret = new ListCardTypeResponse();
    	
    	Map<String, String> map = new HashMap<>();
		map.put("clientID", configProvider.getValue("parking.shenye.projectId", ""));
		String data = StringHelper.toJsonString(map);
		
		String json = port.parkingSystemRequestService("", GET_TYPES, data, "");
        
        if(LOGGER.isDebugEnabled())
			LOGGER.debug("Card type from bosigao={}", json);
        
        ResultEntity result = GsonUtil.fromJson(json, ResultEntity.class);

		if(result.isSuccess()){
			ret.setCardTypes((List<String>)result.getResult());
		}
    	return ret;
    }
    
    private ResultEntity getCard(String plateNumber){
    	GetCardCommand cmd = new GetCardCommand();
    	cmd.setClientID(configProvider.getValue("parking.shenye.projectId", ""));
    	cmd.setCardCode("");
    	cmd.setPlateNo(plateNumber);
    	cmd.setFlag(FLAG2);

        String json = port.parkingSystemRequestService("", GET_CARD, cmd.toString(), "");
        
        if(LOGGER.isDebugEnabled())
			LOGGER.debug("Result={}", json);
        
        ResultEntity result = GsonUtil.fromJson(json, ResultEntity.class);
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
    		ResultEntity resultEntity = getCard(plateNumber);           
			CardInfo cardInfo = (CardInfo) resultEntity.getResult();
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

    @Override
    public void notifyParkingRechargeOrderPayment(ParkingRechargeOrder order, String payStatus) {
    	if(order.getRechargeStatus() != ParkingRechargeOrderRechargeStatus.RECHARGED.getCode()) {
			if(payStatus.toLowerCase().equals("fail")) {
				LOGGER.error("pay failed, orderNo={}", order.getId());
			}
			else {
				if(recharge(order)){
					order.setRechargeStatus(ParkingRechargeOrderRechargeStatus.RECHARGED.getCode());
					order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
					parkingProvider.updateParkingRechargeOrder(order);
				}
			}
		}
    }
    
    private boolean recharge(ParkingRechargeOrder order){
    	RechargeCommand cmd = new RechargeCommand();
		cmd.setClientID(configProvider.getValue("parking.shenye.projectId", ""));
		cmd.setCardCode(order.getCardNumber());
		cmd.setPlateNo("");
		cmd.setFlag(FLAG1);
		cmd.setPayMos(order.getMonthCount().toString());
		cmd.setAmount((order.getPrice().intValue()*100) + "");
		cmd.setPayDate(sdf1.format(order.getPaidTime()));
		cmd.setChargePaidNo(order.getId().toString());
		
        String json = port.parkingSystemRequestService("", RECHARGE, cmd.toString(), "");

		ResultEntity result = GsonUtil.fromJson(json, ResultEntity.class);
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
    
    private void checkResultHolderIsNull(ResultEntity result,String plateNo) {
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
	public ParkingCardRequestDTO getRequestParkingCard(RequestParkingCardCommand cmd) {
        List<ParkingCardDTO> cardList = getParkingCardsByPlate(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId(),
        		cmd.getPlateNumber());
        User user = UserContext.current().getUser();
		if(cardList.size()>0){
			LOGGER.error("PlateNumber card is existed .");
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_PLATE_EXIST,
					"PlateNumber card is existed.");
		}

        if(cardList.size() == 0){
        	List<ParkingCardRequest> list = parkingProvider.listParkingCardRequests(user.getId(), cmd.getOwnerType(), 
        			cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(), null,
        			ParkingCardRequestStatus.INACTIVE.getCode(), null, null);
        	if(list.size()>0){
        		LOGGER.error("PlateNumber is already applied.");
    			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_PLATE_APPLIED,
    					"PlateNumber is already applied.");
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
			
			Integer count = parkingProvider.waitingCardCount(cmd.getOwnerType(), cmd.getOwnerId(), 
					cmd.getParkingLotId(), parkingCardRequest.getCreateTime());
			
			parkingCardRequestDTO.setRanking(count);
		} catch(Exception e) {
			LOGGER.error("ApplyParkingCard is fail, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_SQL_EXCEPTION,
					"ApplyParkingCard is fail.");
		}
		return parkingCardRequestDTO;
	}
	
	@Scheduled(cron="0 0 0/2 * * ? ")
	@Override
	public void refreshParkingRechargeOrderStatus() {
		LOGGER.info("refresh recharge status.");
		List<ParkingRechargeOrder> orderList = parkingProvider.findWaitingParkingRechargeOrders(ParkingLotVendor.BOSIGAO2);
		orderList.stream().map(order -> {
			
			if(recharge(order)){
				order.setRechargeStatus(ParkingRechargeOrderRechargeStatus.RECHARGED.getCode());
				order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
				parkingProvider.updateParkingRechargeOrder(order);
			}
			return null;
		});
	}
}
