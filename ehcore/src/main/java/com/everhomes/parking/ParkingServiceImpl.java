// @formatter:off
package com.everhomes.parking;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.everhomes.acl.AclProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.order.OrderUtil;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.order.CommonOrderCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.parking.CreateParkingRechargeOrderCommand;
import com.everhomes.rest.parking.CreateParkingRechargeRateCommand;
import com.everhomes.rest.parking.DeleteParkingRechargeRateCommand;
import com.everhomes.rest.parking.GetParkingActivityCommand;
import com.everhomes.rest.parking.IssueParkingCardsCommand;
import com.everhomes.rest.parking.ListParkingCardRequestResponse;
import com.everhomes.rest.parking.ListParkingCardRequestsCommand;
import com.everhomes.rest.parking.ListParkingCardsCommand;
import com.everhomes.rest.parking.ListParkingLotsCommand;
import com.everhomes.rest.parking.ListParkingRechargeOrdersCommand;
import com.everhomes.rest.parking.ListParkingRechargeOrdersResponse;
import com.everhomes.rest.parking.ListParkingRechargeRatesCommand;
import com.everhomes.rest.parking.ParkingActivityDTO;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingCardIssueFlag;
import com.everhomes.rest.parking.ParkingCardRequestDTO;
import com.everhomes.rest.parking.ParkingCardRequestStatus;
import com.everhomes.rest.parking.ParkingLotDTO;
import com.everhomes.rest.parking.ParkingOwnerType;
import com.everhomes.rest.parking.ParkingRechargeOrderDTO;
import com.everhomes.rest.parking.ParkingRechargeOrderRechargeStatus;
import com.everhomes.rest.parking.ParkingRechargeOrderStatus;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import com.everhomes.rest.parking.RequestParkingCardCommand;
import com.everhomes.rest.parking.SearchParkingCardRequestsCommand;
import com.everhomes.rest.parking.SearchParkingRechargeOrdersCommand;
import com.everhomes.rest.parking.SetParkingActivityCommand;
import com.everhomes.rest.parking.SetParkingCardIssueFlagCommand;
import com.everhomes.rest.parking.SetParkingCardReserveDaysCommand;
import com.everhomes.rest.user.IdentifierType;
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
    
    @Autowired
	private OrderUtil commonOrderUtil;
    
    @Override
    public List<ParkingCardDTO> listParkingCards(ListParkingCardsCommand cmd) {
    	checkPlateNumber(cmd.getPlateNumber());
        Long parkingLotId = cmd.getParkingLotId();
        ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), parkingLotId);

        String venderName = parkingLot.getVendorName();
        ParkingVendorHandler handler = getParkingVendorHandler(venderName);
        List<ParkingCardDTO> cardList = handler.getParkingCardsByPlate(cmd.getOwnerType(), cmd.getOwnerId(), parkingLotId, cmd.getPlateNumber());
        
        return cardList;
    }
    
    @Override
    public List<ParkingRechargeRateDTO> listParkingRechargeRates(ListParkingRechargeRatesCommand cmd){
    	Long parkingLotId = cmd.getParkingLotId();
           
        ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), parkingLotId);
        
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
	public ParkingCardRequestDTO requestParkingCard(RequestParkingCardCommand cmd) {
		
    	checkPlateNumber(cmd.getPlateNumber());
    	ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
		
    	String vendor = parkingLot.getVendorName();
    	ParkingVendorHandler handler = getParkingVendorHandler(vendor);
    	
		return handler.getRequestParkingCard(cmd);
	}
    
	@Override
    public ListParkingCardRequestResponse listParkingCardRequests(ListParkingCardRequestsCommand cmd){
		
        checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
		
    	ListParkingCardRequestResponse response = new ListParkingCardRequestResponse();
    	cmd.setPageSize(PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize()));
    	User user = UserContext.current().getUser();
    	
    	List<ParkingCardRequest> list = parkingProvider.listParkingCardRequests(user.getId(),cmd.getOwnerType(), 
    			cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(),null,
    			"CREATE_TIME asc", cmd.getPageAnchor(), cmd.getPageSize());
    					
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
	public CommonOrderDTO createParkingRechargeOrder(CreateParkingRechargeOrderCommand cmd){
		
		checkPlateNumber(cmd.getPlateNumber());
        checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

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
		parkingRechargeOrder.setOrderNo(createOrderNo(System.currentTimeMillis()));
		parkingRechargeOrder.setNewExpiredTime(addMonth(cmd.getExpiredTime(), cmd.getMonthCount()));
		parkingRechargeOrder.setOldExpiredTime(addDays(cmd.getExpiredTime(), 1));
		
		parkingProvider.createParkingRechargeOrder(parkingRechargeOrder);
		}catch(Exception e) {
			LOGGER.error("createParkingRechargeOrder is fail.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"createParkingRechargeOrder is fail.");
		}
		
		
		//调用统一处理订单接口，返回统一订单格式
		CommonOrderCommand orderCmd = new CommonOrderCommand();
		orderCmd.setBody(parkingRechargeOrder.getRateName());
		orderCmd.setOrderNo(parkingRechargeOrder.getId().toString());
		orderCmd.setOrderType(OrderType.OrderTypeEnum.Parking.getPycode());
		orderCmd.setSubject("停车充值订单简要描述");
		orderCmd.setTotalFee(parkingRechargeOrder.getPrice());
		CommonOrderDTO dto = null;
		try {
			dto = commonOrderUtil.convertToCommonOrderTemplate(orderCmd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dto;
		
//		parkingRechargeOrderDTO = ConvertHelper.convert(parkingRechargeOrder, ParkingRechargeOrderDTO.class);
//		parkingRechargeOrderDTO.setPayerName(user.getNickName());
//		return parkingRechargeOrderDTO;
	}
	
	@Override
	public ListParkingRechargeOrdersResponse listParkingRechargeOrders(ListParkingRechargeOrdersCommand cmd){
		
        checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
		
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
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
 
        String venderName = parkingLot.getVendorName();
        ParkingVendorHandler handler = getParkingVendorHandler(venderName);
        
        return handler.createParkingRechargeRate(cmd);
    }
	
	@Override
	public boolean deleteParkingRechargeRate(DeleteParkingRechargeRateCommand cmd){
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
 
        String venderName = parkingLot.getVendorName();
        ParkingVendorHandler handler = getParkingVendorHandler(venderName);
        handler.deleteParkingRechargeRate(cmd);
		return true;
	}
	
	@Override
	public ListParkingRechargeOrdersResponse searchParkingRechargeOrders(SearchParkingRechargeOrdersCommand cmd){
		ListParkingRechargeOrdersResponse response = new ListParkingRechargeOrdersResponse();
		
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		
		List<ParkingRechargeOrder> list = parkingProvider.searchParkingRechargeOrders(cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(), cmd.getPlateOwnerName(),
				cmd.getPlateOwnerPhone(), cmd.getPayerName(), cmd.getPayerPhone(), cmd.getPageAnchor(), 
				pageSize,strToTimestamp(cmd.getStartDate()),strToTimestamp(cmd.getEndDate()),cmd.getRechargeStatus()
				);
    					
    	if(list.size() > 0){
    		response.setOrders(list.stream().map(r -> ConvertHelper.convert(r, ParkingRechargeOrderDTO.class))
    				.collect(Collectors.toList()));
    		if(pageSize != null && list.size() != pageSize){
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
		
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
    	List<ParkingCardRequest> list = parkingProvider.searchParkingCardRequests(cmd.getOwnerType(), 
    			cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(), cmd.getPlateOwnerName(), 
    			cmd.getPlateOwnerPhone(), strToTimestamp(cmd.getStartDate()), strToTimestamp(cmd.getEndDate()), 
    			cmd.getStatus(),cmd.getPageAnchor(), pageSize);
    	if(list.size() > 0){
    		response.setRequests(list.stream().map(r -> ConvertHelper.convert(r, ParkingCardRequestDTO.class))
    				.collect(Collectors.toList()));
    		if( pageSize != null && list.size() != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getId());
        	}
    	}
    	
    	return response;
	}
	
	@Override
	public void setParkingCardReserveDays(SetParkingCardReserveDaysCommand cmd){
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
		
		//设置厂商月卡 保留时间 各个厂商 可能不一样
        parkingLot.setCardReserveDays(cmd.getCount());
        parkingProvider.setParkingCardReserveDays(parkingLot);
	}
	
	@Override
	public void setParkingCardIssueFlag(SetParkingCardIssueFlagCommand cmd){
		
        checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
        if(cmd.getId() == null){
        	LOGGER.error("id is not null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"id is not null.");
        }
        
        ParkingCardRequest parkingCardRequest = parkingProvider.findParkingCardRequestById(cmd.getId());
        if(parkingCardRequest == null){
        	LOGGER.error("parkingCardRequest {} is not exist.",cmd.getId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
				"parkingCardRequest is not exist.");
        }
        if(parkingCardRequest.getStatus() != ParkingCardRequestStatus.NOTIFIED.getCode()){
        	LOGGER.error("parkingCardRequest {} status is not notified.",cmd.getId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
				"parkingCardRequest status is not notified.");
        }
        //设置已领取状态和 领取时间
        parkingCardRequest.setIssueFlag(ParkingCardIssueFlag.ISSUED.getCode());
        parkingCardRequest.setIssueTime(new Timestamp(System.currentTimeMillis()));
        parkingProvider.updateParkingCardRequest(Collections.singletonList(parkingCardRequest));
	}

	@Override
	public void issueParkingCards(IssueParkingCardsCommand cmd) {
		
		if(cmd.getCount() == null) {
        	LOGGER.error("count is not null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"count is not null.");
        }
        checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
        
    	List<ParkingCardRequest> list = parkingProvider.listParkingCardRequests(null,null, 
    			null, null, null,ParkingCardRequestStatus.QUEUEING,
    			"CREATE_TIME asc", null, cmd.getCount())
    			.stream().map(r -> {
    				r.setStatus(ParkingCardRequestStatus.NOTIFIED.getCode());
					return r;
    			}).collect(Collectors.toList());
    	
    	parkingProvider.updateParkingCardRequest(list);
	}

	@Override
	public void notifyParkingRechargeOrderPayment(PayCallbackCommand cmd) {
		
		OrderEmbeddedHandler orderEmbeddedHandler = this.getOrderHandler(cmd.getOrderType());
		LOGGER.debug("OrderEmbeddedHandler="+orderEmbeddedHandler.getClass().getName());
		if(cmd.getPayStatus().equalsIgnoreCase("success"))
			orderEmbeddedHandler.paySuccess(cmd);
		if(cmd.getPayStatus().equalsIgnoreCase("fail"))
			orderEmbeddedHandler.payFail(cmd);
		
	}
	
	@Override
	public ParkingActivityDTO setParkingActivity(SetParkingActivityCommand cmd){
		ParkingActivity parkingActivity = new ParkingActivity();
		
		User user = UserContext.current().getUser();
		
		parkingActivity.setCreateTime(strToTimestamp(cmd.getStartTime()));
		parkingActivity.setEndTime(strToTimestamp(cmd.getEndTime()));
		parkingActivity.setOwnerId(cmd.getOwnerId());
		parkingActivity.setOwnerType(cmd.getOwnerType());
		parkingActivity.setParkingLotId(cmd.getParkingLotId());
		parkingActivity.setTopCount(cmd.getTopCount());
		parkingActivity.setCreatorUid(user.getId());
		parkingProvider.setParkingActivity(parkingActivity);
		
		return ConvertHelper.convert(parkingActivity, ParkingActivityDTO.class);
	}
	
	@Override
	public ParkingActivityDTO getParkingActivity(GetParkingActivityCommand cmd) {
		ParkingActivityDTO dto = null;
		
		ParkingActivity activity = parkingProvider.getParkingActivity(cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getParkingLotId());
		if(activity != null)
			dto = ConvertHelper.convert(activity, ParkingActivityDTO.class);
		return dto;
	}
	
    private Timestamp strToTimestamp(String str) {
    	if(StringUtils.isBlank(str))
    		return null;
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
    
    private ParkingLot checkParkingLot(String ownerType,Long ownerId,Long parkingLotId){
    	if(ownerId == null ) {
        	LOGGER.error("ownerId is not null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"ownerId is not null.");
        }
    	
    	if(StringUtils.isBlank(ownerType)) {
        	LOGGER.error("ownerType is not null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"ownerType is not null.");
        }
    	
    	if(parkingLotId == null ) {
        	LOGGER.error("parkingLotId is not null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"parkingLotId is not null.");
        }
    	
    	ParkingLot parkingLot = parkingProvider.findParkingLotById(parkingLotId);
        if(parkingLot == null) {
        	LOGGER.error("parkingLot is not exist {}.",parkingLotId);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    			"parkingLot is not exist.");
        }
        // 检查参数里的ownerType和ownerId是否与查出来停车场里的匹配
        if(ownerId != null && ownerId.longValue() != parkingLot.getOwnerId().longValue()) {
        	LOGGER.error("ownerId {} is not match with parkingLot ownerId.",ownerId);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
    				"ownerId is not match with parkingLot ownerId.");
        }
        if(ParkingOwnerType.fromCode(parkingLot.getOwnerType()) != ParkingOwnerType.fromCode(ownerType)){
            LOGGER.error("ownertype {} is not match with parkingLot ownertype.",ownerType);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
    				"ownertype is not match with parkingLot ownertype.");
        }
        return parkingLot;
    }
    
    private void checkPlateNumber(String plateNumber){
    	if(StringUtils.isBlank(plateNumber)) {
        	LOGGER.error("plateNumber is not null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"plateNumber is not null.");
        }
    }
    
    @Scheduled(cron="0 0 2 * * ? ")
   	@Override
   	public void invalidApplier() {
   		LOGGER.info("update invalid appliers.");
   		List<ParkingLot> list = parkingProvider.listParkingLots(null, null);
   		for(ParkingLot parkingLot:list){
   			Integer days = parkingLot.getCardReserveDays();
   			long time = System.currentTimeMillis() - days * 24 * 60 * 60 * 1000;
   			parkingProvider.updateInvalidAppliers(new Timestamp(time),parkingLot.getId());
   		}
   		
   	}
    
    private Long createOrderNo(Long time) {
		String bill = String.valueOf(time) + (int) (Math.random()*1000);
		return Long.valueOf(bill);
	}
    
	private Timestamp addDays(String oldPeriod, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(strToTimestamp(oldPeriod));
		calendar.add(Calendar.DATE, days);
		Timestamp time = new Timestamp(calendar.getTimeInMillis());
		
		return time;
	}
	
	private Timestamp addMonth(String oldPeriod, int month) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(strToTimestamp(oldPeriod));
		calendar.add(Calendar.MONTH, month);
		Timestamp newPeriod = new Timestamp(calendar.getTimeInMillis());
		
		return newPeriod;
	}
	
	private OrderEmbeddedHandler getOrderHandler(String orderType) {
		return PlatformContext.getComponent(OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX+this.getOrderTypeCode(orderType));
	}
	
	private String getOrderTypeCode(String orderType) {
		Integer code = OrderType.OrderTypeEnum.getCodeByPyCode(orderType);
		if(code==null){
			LOGGER.error("Invalid parameter,orderType not found in OrderType.orderType="+orderType);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameter,orderType not found in OrderType");
		}
		LOGGER.debug("orderTypeCode="+code);
		return String.valueOf(code);
	}
}
