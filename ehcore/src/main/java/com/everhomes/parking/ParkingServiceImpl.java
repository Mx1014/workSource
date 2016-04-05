// @formatter:off
package com.everhomes.parking;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.acl.AclProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.parking.CreateParkingRechargeOrderCommand;
import com.everhomes.rest.parking.CreateParkingRechargeRateCommand;
import com.everhomes.rest.parking.DeleteParkingRechargeRateCommand;
import com.everhomes.rest.parking.IssueParkingCardsCommand;
import com.everhomes.rest.parking.ListParkingCardRequestResponse;
import com.everhomes.rest.parking.ListParkingCardRequestsCommand;
import com.everhomes.rest.parking.ListParkingCardsCommand;
import com.everhomes.rest.parking.ListParkingLotsCommand;
import com.everhomes.rest.parking.ListParkingRechargeOrdersCommand;
import com.everhomes.rest.parking.ListParkingRechargeOrdersResponse;
import com.everhomes.rest.parking.ListParkingRechargeRatesCommand;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingCardIssueFlag;
import com.everhomes.rest.parking.ParkingCardRequestDTO;
import com.everhomes.rest.parking.ParkingCardRequestStatus;
import com.everhomes.rest.parking.ParkingLotDTO;
import com.everhomes.rest.parking.ParkingRechargeOrderDTO;
import com.everhomes.rest.parking.ParkingRechargeOrderRechargeStatus;
import com.everhomes.rest.parking.ParkingRechargeOrderStatus;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import com.everhomes.rest.parking.RequestParkingCardCommand;
import com.everhomes.rest.parking.SearchParkingCardRequestsCommand;
import com.everhomes.rest.parking.SearchParkingRechargeOrdersCommand;
import com.everhomes.rest.parking.SetParkingCardIssueFlagCommand;
import com.everhomes.rest.parking.SetParkingCardReserveDaysCommand;
import com.everhomes.rest.techpark.onlinePay.OnlinePayBillCommand;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class ParkingServiceImpl implements ParkingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingServiceImpl.class);

    @Autowired
    private AclProvider aclProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private ParkingProvider parkingProvider;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    @Autowired
    private CoordinationProvider coordinationProvider;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private OrganizationProvider organizationProvider;
    
    @Autowired
	private LocaleStringService localeStringService;
    
    @Override
    public List<ParkingCardDTO> listParkingCards(ListParkingCardsCommand cmd) {
        Long parkingLotId = cmd.getParkingLotId();
           
        List<ParkingCardDTO> cardList = null; 
        ParkingLot parkingLot = parkingProvider.findParkingLotById(parkingLotId);
        if(parkingLot != null) {
            if(cmd.getOwnerId().longValue() != parkingLot.getOwnerId().longValue() 
            		|| !parkingLot.getOwnerType().equalsIgnoreCase(cmd.getOwnerType())){
            	LOGGER.error("ownerId or ownertype is not match with parking ownerId.");
    			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
    					"ownerId or ownertype is not match with parking ownerId.");
            }
            
            String venderName = parkingLot.getVendorName();
            ParkingVendorHandler handler = getParkingVendorHandler(venderName);
            cardList = handler.getParkingCardsByPlate(cmd.getOwnerType(), cmd.getOwnerId(), parkingLotId, cmd.getPlateNumber());
        }
        
        return cardList;
    }
    
    @Override
    public List<ParkingRechargeRateDTO> listParkingRechargeRates(ListParkingRechargeRatesCommand cmd){
    	Long parkingLotId = cmd.getParkingLotId();
           
        ParkingLot parkingLot = parkingProvider.findParkingLotById(parkingLotId);
        if(parkingLot == null) {
        	LOGGER.error("parkingLot is not exist.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    			"parkingLot is not exist.");
        }
        // 检查参数里的ownerType和ownerId是否与查出来停车场里的匹配
        if(cmd.getOwnerId().longValue() != parkingLot.getOwnerId().longValue() 
            	|| !parkingLot.getOwnerType().equalsIgnoreCase(cmd.getOwnerType())){
            LOGGER.error("ownerId or ownertype is not match with parking ownerId.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
    				"ownerId or ownertype is not match with parking ownerId.");
        }
        
        List<ParkingRechargeRateDTO> parkingRechargeRateList = null;
        String venderName = parkingLot.getVendorName();
        ParkingVendorHandler handler = getParkingVendorHandler(venderName);
        parkingRechargeRateList = handler.getParkingRechargeRates(cmd.getOwnerType(), cmd.getOwnerId(), parkingLotId);
        
        return parkingRechargeRateList;
    }
    
    private ParkingVendorHandler getParkingVendorHandler(String vendorName) {
        ParkingVendorHandler handler = null;
        
        if(vendorName != null && vendorName.length() > 0) {
            String handlerPrefix = ParkingVendorHandler.PARKING_VENDOR_PREFIX;
            handler = PlatformContext.getComponent(handlerPrefix + vendorName);
        }
        
        return handler;
    }
    

    @Override
    public List<ParkingLotDTO> listParkingLots(ListParkingLotsCommand cmd){
    	List<ParkingLotDTO> parkingLotList = null;
    	if(cmd.getOwnerId() == null || StringUtils.isBlank(cmd.getOwnerType())){
    		LOGGER.error("ownerId or ownertype is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"ownerId or ownertype is null.");
    	}
    	
    	List<ParkingLot> list = parkingProvider.listParkingLots(cmd.getOwnerType(), cmd.getOwnerId());
    	parkingLotList = list.stream().map(r -> ConvertHelper.convert(r, ParkingLotDTO.class))
    		.collect(Collectors.toList());
    	return parkingLotList;
    }
    
    @Override
	public List<ParkingCardRequestDTO> requestParkingCard(RequestParkingCardCommand cmd) {
		
    	ParkingLot parkingLot = parkingProvider.findParkingLotById(cmd.getParkingLotId());
        if(parkingLot == null) {
        	LOGGER.error("parkingLot is not exist.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    			"parkingLot is not exist.");
        }
        // 检查参数里的ownerType和ownerId是否与查出来停车场里的匹配
        if(cmd.getOwnerId().longValue() != parkingLot.getOwnerId().longValue() 
            	|| !parkingLot.getOwnerType().equalsIgnoreCase(cmd.getOwnerType())){
            LOGGER.error("ownerId or ownertype is not match with parking ownerId.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
    				"ownerId or ownertype is not match with parking ownerId.");
        } 
		
		if(cmd.getPlateNumber().length() != 7) {
			LOGGER.error("the length of plateNumber is wrong.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the length of plateNumber is wrong.");
		}
		List<ParkingCardRequest> list = null;
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
			parkingCardRequest.setRequestorUid(UserContext.current().getLogin().getUserId());
			//设置一些初始状态
			parkingCardRequest.setIssueFlag(ParkingCardIssueFlag.UNISSUED.getCode());
			parkingCardRequest.setStatus(ParkingCardRequestStatus.QUEUEING.getCode());
			parkingCardRequest.setCreatorUid(UserContext.current().getLogin().getUserId());
			parkingCardRequest.setCreateTime(new Timestamp(System.currentTimeMillis()));
			
			parkingProvider.requestParkingCard(parkingCardRequest);
			
			list = parkingProvider.listParkingCardRequests(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId(), 
					cmd.getPlateNumber(),null,Tables.EH_PARKING_CARD_REQUESTS.CREATE_TIME.asc(), null, null);
				//PaginationConfigHelper.getPageSize(configProvider, null)
		} catch(Exception e) {
			LOGGER.error("requestParkingCard is fail.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_SQL_EXCEPTION,
					"requestParkingCard is fail." + e.toString());
		}
		return list.stream().map(r -> ConvertHelper.convert(r, 
				ParkingCardRequestDTO.class)).collect(Collectors.toList());
	}
    
	@Override
    public ListParkingCardRequestResponse listParkingCardRequests(ListParkingCardRequestsCommand cmd){
		
		ParkingLot parkingLot = parkingProvider.findParkingLotById(cmd.getParkingLotId());
        if(parkingLot == null) {
        	LOGGER.error("parkingLot is not exist.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    			"parkingLot is not exist.");
        }
        // 检查参数里的ownerType和ownerId是否与查出来停车场里的匹配
        if(cmd.getOwnerId().longValue() != parkingLot.getOwnerId().longValue() 
            	|| !parkingLot.getOwnerType().equalsIgnoreCase(cmd.getOwnerType())){
            LOGGER.error("ownerId or ownertype is not match with parking ownerId.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
    				"ownerId or ownertype is not match with parking ownerId.");
        } 
		
    	ListParkingCardRequestResponse response = new ListParkingCardRequestResponse();
    	cmd.setPageSize(PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize()));
    	List<ParkingCardRequest> list = parkingProvider.listParkingCardRequests(cmd.getOwnerType(), 
    			cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(),null,
    			Tables.EH_PARKING_CARD_REQUESTS.CREATE_TIME.asc(), cmd.getPageAnchor(), cmd.getPageSize());
    					
    	if(list.size() > 0){
    		response.setRequests(list.stream().map(r -> ConvertHelper.convert(r, ParkingCardRequestDTO.class))
    				.collect(Collectors.toList()));
    		if(list.size() != cmd.getPageSize()){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getId());
        	}
    	}
    	
    	return response;
    }
	
	@Override
	public ParkingRechargeOrderDTO createParkingRechargeOrder(CreateParkingRechargeOrderCommand cmd){
		ParkingRechargeOrderDTO parkingRechargeOrderDTO = new ParkingRechargeOrderDTO();	
		ParkingRechargeOrder parkingRechargeOrder = new ParkingRechargeOrder();
		
		User user = UserContext.current().getUser();
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
		
		ParkingLot parkingLot = parkingProvider.findParkingLotById(cmd.getParkingLotId());
		try{
		parkingRechargeOrder.setOwnerType(cmd.getOwnerType());
		parkingRechargeOrder.setOwnerId(cmd.getOwnerId());
		parkingRechargeOrder.setParkingLotId(cmd.getParkingLotId());
		parkingRechargeOrder.setPlateNumber(cmd.getPlateNumber());
		parkingRechargeOrder.setPlateOwnerName(cmd.getPlateOwnerName());
		parkingRechargeOrder.setPlateOwnerPhone(cmd.getPlateOwnerPhone());
		parkingRechargeOrder.setPayerEnterpriseId(cmd.getPayerEnterpriseId());
		parkingRechargeOrder.setPayerUid(user.getId());
		parkingRechargeOrder.setPayerPhone(userIdentifier.getIdentifierToken());
		parkingRechargeOrder.setVendorName(parkingLot.getVendorName());
		parkingRechargeOrder.setCardNumber(cmd.getCardNumber());
		parkingRechargeOrder.setRateToken(cmd.getRateToken());
		parkingRechargeOrder.setRateName(cmd.getRateName());
		parkingRechargeOrder.setMonthCount(new BigDecimal(cmd.getMonthCount()));
		parkingRechargeOrder.setPrice(cmd.getPrice());
		parkingRechargeOrder.setStatus(ParkingRechargeOrderStatus.UNPAID.getCode());
		parkingRechargeOrder.setRechargeStatus(ParkingRechargeOrderRechargeStatus.UNRECHARGED.getCode());
		parkingRechargeOrder.setCreatorUid(user.getId());
		parkingRechargeOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
		
		int result = parkingProvider.createParkingRechargeOrder(parkingRechargeOrder);
		}catch(Exception e) {
			LOGGER.error("createParkingRechargeOrder is fail.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"createParkingRechargeOrder is fail.");
		}
		
		parkingRechargeOrderDTO = ConvertHelper.convert(parkingRechargeOrder, ParkingRechargeOrderDTO.class);
		parkingRechargeOrderDTO.setPayerName(user.getNickName());
		return parkingRechargeOrderDTO;
	}
	
	@Override
	public ListParkingRechargeOrdersResponse listParkingRechargeOrders(ListParkingRechargeOrdersCommand cmd){
		
		ParkingLot parkingLot = parkingProvider.findParkingLotById(cmd.getParkingLotId());
        if(parkingLot == null) {
        	LOGGER.error("parkingLot is not exist.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    			"parkingLot is not exist.");
        }
        // 检查参数里的ownerType和ownerId是否与查出来停车场里的匹配
        if(cmd.getOwnerId().longValue() != parkingLot.getOwnerId().longValue() 
            	|| !parkingLot.getOwnerType().equalsIgnoreCase(cmd.getOwnerType())){
            LOGGER.error("ownerId or ownertype is not match with parking ownerId.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
    				"ownerId or ownertype is not match with parking ownerId.");
        } 
		
		ListParkingRechargeOrdersResponse response = new ListParkingRechargeOrdersResponse();
		//设置分页
		cmd.setPageSize(PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize()));
		List<ParkingRechargeOrder> list = parkingProvider.listParkingRechargeOrders
    			(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId(), 
    					cmd.getPlateNumber(), cmd.getPageAnchor(), cmd.getPageSize());
    					
    	if(list.size() > 0){
    		response.setOrders(list.stream().map(r -> ConvertHelper.convert(r, ParkingRechargeOrderDTO.class))
    				.collect(Collectors.toList()));
    		if(list.size() != cmd.getPageSize()){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getId());
        	}
    	}
    	
		return response;
	}
	
	@Override
    public ParkingRechargeRateDTO createParkingRechargeRate(CreateParkingRechargeRateCommand cmd){
		ParkingLot parkingLot = parkingProvider.findParkingLotById(cmd.getParkingLotId());
        if(parkingLot == null) {
        	LOGGER.error("parkingLot is not exist.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    			"parkingLot is not exist.");
        }
        // 检查参数里的ownerType和ownerId是否与查出来停车场里的匹配
        if(cmd.getOwnerId().longValue() != parkingLot.getOwnerId().longValue() 
            	|| !parkingLot.getOwnerType().equalsIgnoreCase(cmd.getOwnerType())){
            LOGGER.error("ownerId or ownertype is not match with parking ownerId.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
    				"ownerId or ownertype is not match with parking ownerId.");
        }   
        String venderName = parkingLot.getVendorName();
        ParkingVendorHandler handler = getParkingVendorHandler(venderName);
        
        return handler.createParkingRechargeRate(cmd);
    }
	
	@Override
	public boolean deleteParkingRechargeRate(DeleteParkingRechargeRateCommand cmd){
		ParkingLot parkingLot = parkingProvider.findParkingLotById(cmd.getParkingLotId());
        if(parkingLot == null) {
        	LOGGER.error("parkingLot is not exist.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    			"parkingLot is not exist.");
        }
        // 检查参数里的ownerType和ownerId是否与查出来停车场里的匹配
        if(cmd.getOwnerId().longValue() != parkingLot.getOwnerId().longValue() 
            	|| !parkingLot.getOwnerType().equalsIgnoreCase(cmd.getOwnerType())){
            LOGGER.error("ownerId or ownertype is not match with parking ownerId.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
    				"ownerId or ownertype is not match with parking ownerId.");
        }   
        String venderName = parkingLot.getVendorName();
        ParkingVendorHandler handler = getParkingVendorHandler(venderName);
        handler.deleteParkingRechargeRate(cmd);
		return true;
	}
	
	@Override
	public ListParkingRechargeOrdersResponse searchParkingRechargeOrders(SearchParkingRechargeOrdersCommand cmd){
		ListParkingRechargeOrdersResponse response = new ListParkingRechargeOrdersResponse();
		
		cmd.setPageSize(PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize()));
		List<ParkingRechargeOrder> list = parkingProvider.listParkingRechargeOrders(cmd);
    					
    	if(list.size() > 0){
    		response.setOrders(list.stream().map(r -> ConvertHelper.convert(r, ParkingRechargeOrderDTO.class))
    				.collect(Collectors.toList()));
    		if(list.size() != cmd.getPageSize()){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getId());
        	}
    	}
    	
		return response;
	}

	@Override
	public ListParkingCardRequestResponse searchParkingCardRequests(SearchParkingCardRequestsCommand cmd) {
		ListParkingCardRequestResponse response = new ListParkingCardRequestResponse();
		
		cmd.setPageSize(PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize()));
    	List<ParkingCardRequest> list = parkingProvider.searchParkingCardRequests(cmd);
    	if(list.size() > 0){
    		response.setRequests(list.stream().map(r -> ConvertHelper.convert(r, ParkingCardRequestDTO.class))
    				.collect(Collectors.toList()));
    		if(list.size() != cmd.getPageSize()){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getId());
        	}
    	}
    	
    	return response;
	}
	
	@Override
	public void setParkingCardReserveDays(SetParkingCardReserveDaysCommand cmd){
		ParkingLot parkingLot = parkingProvider.findParkingLotById(cmd.getParkingLotId());
        if(parkingLot == null) {
        	LOGGER.error("parkingLot is not exist.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    			"parkingLot is not exist.");
        }
        // 检查参数里的ownerType和ownerId是否与查出来停车场里的匹配
        if(cmd.getOwnerId().longValue() != parkingLot.getOwnerId().longValue() 
            	|| !parkingLot.getOwnerType().equalsIgnoreCase(cmd.getOwnerType())){
            LOGGER.error("ownerId or ownertype is not match with parking ownerId.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
    				"ownerId or ownertype is not match with parking ownerId.");
        }
        parkingLot.setCardReserveDays(cmd.getCount());
        parkingProvider.setParkingCardReserveDays(parkingLot);
	}
	
	@Override
	public void setParkingCardIssueFlag(SetParkingCardIssueFlagCommand cmd){
		ParkingLot parkingLot = parkingProvider.findParkingLotById(cmd.getParkingLotId());
        if(parkingLot == null) {
        	LOGGER.error("parkingLot is not exist.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    			"parkingLot is not exist.");
        }
        // 检查参数里的ownerType和ownerId是否与查出来停车场里的匹配
        if(cmd.getOwnerId().longValue() != parkingLot.getOwnerId().longValue() 
            	|| !parkingLot.getOwnerType().equalsIgnoreCase(cmd.getOwnerType())){
            LOGGER.error("ownerId or ownertype is not match with parking ownerId.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
    				"ownerId or ownertype is not match with parking ownerId.");
        }
        ParkingCardRequest parkingCardRequest = parkingProvider.findParkingCardRequestById(cmd.getId());
        if(parkingCardRequest == null){
        	LOGGER.error("parkingCardRequest is not exist.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
				"parkingCardRequest is not exist.");
        }
        if(parkingCardRequest.getStatus() != ParkingCardRequestStatus.NOTIFIED.getCode()){
        	LOGGER.error("parkingCardRequest status is not notified.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
				"parkingCardRequest status is not notified.");
        }
        parkingCardRequest.setIssueFlag(ParkingCardIssueFlag.ISSUED.getCode());
        parkingProvider.updateParkingCardRequest(Collections.singletonList(parkingCardRequest));
	}

	@Override
	public void issueParkingCards(IssueParkingCardsCommand cmd) {
		ParkingLot parkingLot = parkingProvider.findParkingLotById(cmd.getParkingLotId());
        if(parkingLot == null) {
        	LOGGER.error("parkingLot is not exist.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    			"parkingLot is not exist.");
        }
        // 检查参数里的ownerType和ownerId是否与查出来停车场里的匹配
        if(cmd.getOwnerId().longValue() != parkingLot.getOwnerId().longValue() 
            	|| !parkingLot.getOwnerType().equalsIgnoreCase(cmd.getOwnerType())){
            LOGGER.error("ownerId or ownertype is not match with parking ownerId.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
    				"ownerId or ownertype is not match with parking ownerId.");
        }
        
    	List<ParkingCardRequest> list = parkingProvider.listParkingCardRequests(null, 
    			null, null, null,ParkingCardRequestStatus.QUEUEING,
    			Tables.EH_PARKING_CARD_REQUESTS.CREATE_TIME.asc(), null, cmd.getCount())
    			.stream().map(r -> {
    				r.setStatus(ParkingCardRequestStatus.NOTIFIED.getCode());
					return r;
    			}).collect(Collectors.toList());
    	
    	parkingProvider.updateParkingCardRequest(list);
	}

	@Override
	public void notifyParkingRechargeOrderPayment(OnlinePayBillCommand cmd) {
		
	}
}
