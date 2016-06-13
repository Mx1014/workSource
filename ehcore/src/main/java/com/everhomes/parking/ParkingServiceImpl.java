// @formatter:off
package com.everhomes.parking;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.order.OrderUtil;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.order.CommonOrderCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.CreateParkingRechargeOrderCommand;
import com.everhomes.rest.parking.CreateParkingRechargeRateCommand;
import com.everhomes.rest.parking.DeleteParkingRechargeOrderCommand;
import com.everhomes.rest.parking.DeleteParkingRechargeRateCommand;
import com.everhomes.rest.parking.GetParkingActivityCommand;
import com.everhomes.rest.parking.IsOrderDelete;
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
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.parking.ParkingLotDTO;
import com.everhomes.rest.parking.ParkingNotificationTemplateCode;
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
import com.everhomes.rest.techpark.park.ParkingServiceErrorCode;
import com.everhomes.rest.techpark.punch.PunchServiceErrorCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
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

    SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
    
    @Autowired
	private MessagingService messagingService;
    
    @Autowired
	private LocaleTemplateService localeTemplateService;
    
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
    	if(StringUtils.isBlank(cmd.getPlateNumber())) {
        	LOGGER.error("plateNumber cannot be null.");
        	throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_PLATE_NULL,
					localeStringService.getLocalizedString(String.valueOf(ParkingErrorCode.SCOPE), 
							String.valueOf(ParkingErrorCode.ERROR_PLATE_NULL),
							UserContext.current().getUser().getLocale(),"plateNumber cannot be null."));
        }
        ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), parkingLotId);
        
        List<ParkingRechargeRateDTO> parkingRechargeRateList = null;
        String venderName = parkingLot.getVendorName();
        ParkingVendorHandler handler = getParkingVendorHandler(venderName);
        parkingRechargeRateList = handler.getParkingRechargeRates(cmd.getOwnerType(), cmd.getOwnerId(), parkingLotId,cmd.getPlateNumber(),cmd.getCardNo());
        
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
    		LOGGER.error("ownerId or ownertype cannot be null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"ownerId or ownertype cannot be null.");
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
    			cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(),null, cmd.getPageAnchor(),
    			cmd.getPageSize());
    					
    	if(list.size() > 0){
    		response.setRequests(list.stream().map(r -> ConvertHelper.convert(r, ParkingCardRequestDTO.class))
    				.collect(Collectors.toList()));
    		if(list.size() != cmd.getPageSize()){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getCreateTime().getTime());
        	}
    	}
    	
    	return response;
    }
	
	@Override
	public CommonOrderDTO createParkingRechargeOrder(CreateParkingRechargeOrderCommand cmd){
		
		checkPlateNumber(cmd.getPlateNumber());
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		ParkingRechargeOrder parkingRechargeOrder = new ParkingRechargeOrder();
		
		User user = UserContext.current().getUser();
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
		
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
			LOGGER.error("createParkingRechargeOrder is fail. {}",e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"createParkingRechargeOrder is fail.");
		}
		
		
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
	
	@Override
	public ListParkingRechargeOrdersResponse listParkingRechargeOrders(ListParkingRechargeOrdersCommand cmd){
		
        checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
		
		ListParkingRechargeOrdersResponse response = new ListParkingRechargeOrdersResponse();
		//设置分页
		cmd.setPageSize(PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize()));
		User user = UserContext.current().getUser();

		List<ParkingRechargeOrder> list = parkingProvider.listParkingRechargeOrders
    			(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(),
    					cmd.getPageAnchor(), cmd.getPageSize(),user.getId());
    					
    	if(list.size() > 0){
    		response.setOrders(list.stream().map(r -> ConvertHelper.convert(r, ParkingRechargeOrderDTO.class))
    				.collect(Collectors.toList()));
    		if(list.size() != cmd.getPageSize()){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getRechargeTime().getTime());
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
		Timestamp startDate = null;
		Timestamp endDate = null;
		if(cmd.getStartDate() != null)
			startDate = new Timestamp(cmd.getStartDate());
		if(cmd.getEndDate() != null)
			new Timestamp(cmd.getEndDate());
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		//User user = UserContext.current().getUser();
		List<ParkingRechargeOrder> list = parkingProvider.searchParkingRechargeOrders(cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(), cmd.getPlateOwnerName(),
				cmd.getPlateOwnerPhone(),cmd.getPaidType(), cmd.getPayerName(), cmd.getPayerPhone(), cmd.getPageAnchor(), 
				pageSize,startDate,endDate,cmd.getRechargeStatus()/*,user.getId()*/
				);
    					
    	if(list.size() > 0){
    		response.setOrders(list.stream().map(r -> ConvertHelper.convert(r, ParkingRechargeOrderDTO.class))
    				.collect(Collectors.toList()));
    		if(pageSize != null && list.size() != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getRechargeTime().getTime());
        	}
    	}
    	
		return response;
	}

	@Override
	public ListParkingCardRequestResponse searchParkingCardRequests(SearchParkingCardRequestsCommand cmd) {
		ListParkingCardRequestResponse response = new ListParkingCardRequestResponse();
		Timestamp startDate = null;
		Timestamp endDate = null;
		if(cmd.getStartDate() != null)
			startDate = new Timestamp(cmd.getStartDate());
		if(cmd.getEndDate() != null)
			new Timestamp(cmd.getEndDate());
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
    	List<ParkingCardRequest> list = parkingProvider.searchParkingCardRequests(cmd.getOwnerType(), 
    			cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(), cmd.getPlateOwnerName(), 
    			cmd.getPlateOwnerPhone(), startDate, endDate, 
    			cmd.getStatus(),cmd.getPageAnchor(), pageSize);
    	if(list.size() > 0){
    		response.setRequests(list.stream().map(r -> ConvertHelper.convert(r, ParkingCardRequestDTO.class))
    				.collect(Collectors.toList()));
    		if( pageSize != null && list.size() != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getCreateTime().getTime());
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
        	LOGGER.error("SetParkingCardIssueFlagCommand id cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"SetParkingCardIssueFlagCommand id cannot be null.");
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
        parkingCardRequest.setStatus(ParkingCardRequestStatus.ISSUED.getCode());
        parkingCardRequest.setIssueFlag(ParkingCardIssueFlag.ISSUED.getCode());
        parkingCardRequest.setIssueTime(new Timestamp(System.currentTimeMillis()));
        parkingProvider.updateParkingCardRequest(Collections.singletonList(parkingCardRequest));
	}

	@Override
	public void issueParkingCards(IssueParkingCardsCommand cmd) {
		
		if(cmd.getCount() == null) {
        	LOGGER.error("count cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"count cannot be null.");
        }
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
        
    	List<ParkingCardRequest> list = parkingProvider.listParkingCardRequests(null,null, 
    			null, null, null,ParkingCardRequestStatus.QUEUEING.getCode(),
    			null, null, cmd.getCount())
    			.stream().map(r -> {
    				r.setStatus(ParkingCardRequestStatus.NOTIFIED.getCode());
					return r;
    			}).collect(Collectors.toList());
    	
    	parkingProvider.updateParkingCardRequest(list);
    	Map<String, Object> map = new HashMap<String, Object>();
		String deadline = deadline(parkingLot.getCardReserveDays());
	    map.put("deadline", deadline);
		String scope = ParkingNotificationTemplateCode.SCOPE;
		int code = ParkingNotificationTemplateCode.USER_APPLY_CARD;
		String locale = "zh_CN";
		String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
		list.forEach(applier -> {
			sendMessageToUser(applier.getRequestorUid(), notifyTextForApplicant);
		});
    	
    	
	}

	private String deadline(Integer day) {
		long time = System.currentTimeMillis();

		Timestamp ts = new Timestamp(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(ts);
		calendar.add(Calendar.DATE, day);
		return sdf.format(calendar.getTime());
	}
	
	private void sendMessageToUser(Long userId, String content) {
//		User user = UserContext.current().getUser();
		MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_USER_LOGIN.getUserId());
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
        
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
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
		
		parkingActivity.setCreateTime(new Timestamp(System.currentTimeMillis()));
		parkingActivity.setStartTime(new Timestamp(cmd.getStartTime()));
		parkingActivity.setEndTime(new Timestamp(cmd.getEndTime()));
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
	
    private ParkingLot checkParkingLot(String ownerType,Long ownerId,Long parkingLotId){
    	if(ownerId == null ) {
        	LOGGER.error("ownerId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"ownerId cannot be null.");
        }
    	
    	if(StringUtils.isBlank(ownerType)) {
        	LOGGER.error("ownerType cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"ownerType cannot be null.");
        }
    	
    	if(parkingLotId == null ) {
        	LOGGER.error("parkingLotId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"parkingLotId cannot be null.");
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
        	LOGGER.error("plateNumber cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"plateNumber cannot be null.");
        }
    	/*if(plateNumber.length() != 7) {
			LOGGER.error("the length of plateNumber is wrong.");
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_PLATE_LENGTH,
					localeStringService.getLocalizedString(String.valueOf(ParkingErrorCode.SCOPE), 
							String.valueOf(ParkingErrorCode.ERROR_PLATE_LENGTH),
							UserContext.current().getUser().getLocale(),"the length of plateNumber is wrong."));
		}*/
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
    
	private Timestamp addDays(Long oldPeriod, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Timestamp(oldPeriod));
		calendar.add(Calendar.DATE, days);
		Timestamp time = new Timestamp(calendar.getTimeInMillis());
		
		return time;
	}
	
	private Timestamp addMonth(Long oldPeriod, int month) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Timestamp(oldPeriod));
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
	
	public HttpServletResponse exportParkingRechageOrders(SearchParkingRechargeOrdersCommand cmd,
			HttpServletResponse response){
		Timestamp startDate = null;
		Timestamp endDate = null;
		if(cmd.getStartDate() != null)
			startDate = new Timestamp(cmd.getStartDate());
		if(cmd.getEndDate() != null)
			new Timestamp(cmd.getEndDate());
		//User user = UserContext.current().getUser();

		List<ParkingRechargeOrder> list = parkingProvider.searchParkingRechargeOrders(cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(), cmd.getPlateOwnerName(),
				cmd.getPlateOwnerPhone(),cmd.getPaidType(), cmd.getPayerName(), cmd.getPayerPhone(), cmd.getPageAnchor(), 
				null,startDate,endDate,cmd.getRechargeStatus()/*,user.getId()*/
				);
		Workbook wb = new XSSFWorkbook();
		
		Font font = wb.createFont();   
		font.setFontName("黑体");   
		font.setFontHeightInPoints((short) 16);
		CellStyle style = wb.createCellStyle();
		style.setFont(font);
		
		Sheet sheet = wb.createSheet("parkingRechargeOrders");
		sheet.setDefaultColumnWidth(20);  
		sheet.setDefaultRowHeightInPoints(20); 
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("订单号");
		row.createCell(1).setCellValue("车牌号");
		row.createCell(2).setCellValue("车主名称");
		row.createCell(3).setCellValue("付款人手机号");
		row.createCell(4).setCellValue("充值时间");
		row.createCell(5).setCellValue("充值月数");
		row.createCell(6).setCellValue("充值价格");
		row.createCell(7).setCellValue("付款方式");
		row.createCell(8).setCellValue("充值状态");     
		for(int i=0;i<list.size();i++){
			Row tempRow = sheet.createRow(i + 1);
			ParkingRechargeOrder order = list.get(i);
			tempRow.createCell(0).setCellValue(order.getOrderNo());
			tempRow.createCell(1).setCellValue(order.getPlateNumber());
			tempRow.createCell(2).setCellValue(order.getPlateOwnerName());
			tempRow.createCell(3).setCellValue(order.getPayerPhone());
			tempRow.createCell(4).setCellValue(order.getRechargeTime()==null?"":datetimeSF.format(order.getRechargeTime()));
			tempRow.createCell(5).setCellValue(order.getMonthCount().intValue());
			tempRow.createCell(6).setCellValue(order.getPrice().doubleValue());
			VendorType type = VendorType.fromCode(order.getPaidType());
			tempRow.createCell(7).setCellValue(null==type?"":type.toString());
			tempRow.createCell(8).setCellValue(ParkingRechargeOrderRechargeStatus.fromCode(order.getRechargeStatus()).toString());
			
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			wb.write(out);
			download(out, response);
		} catch (IOException e) {
			LOGGER.error("exportParkingRechageOrders is fail. {}",e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"exportParkingRechageOrders is fail.");
		}
		
		return response;
	}
	
	public HttpServletResponse download(ByteArrayOutputStream out, HttpServletResponse response) {
        try {

            // 清空response
            //response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + System.currentTimeMillis()+".xlsx");
            //response.addHeader("Content-Length", "" + out.);
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(out.toByteArray());
            toClient.flush();
            toClient.close();
            
        } catch (IOException ex) { 
 			LOGGER.error(ex.getMessage());
 			throw RuntimeErrorException.errorWith(ParkingServiceErrorCode.SCOPE,
 					PunchServiceErrorCode.ERROR_PUNCH_ADD_DAYLOG,
 					ex.getLocalizedMessage());
     		 
        }
        return response;
    }

	@Override
	public void deleteParkingRechargeOrder(DeleteParkingRechargeOrderCommand cmd) {
		ParkingRechargeOrder order = parkingProvider.findParkingRechargeOrderById(cmd.getId());
		if(order == null){
			LOGGER.error("order {} is not exist",cmd.getId());
 			throw RuntimeErrorException.errorWith(ParkingServiceErrorCode.SCOPE,
 					ErrorCodes.ERROR_GENERAL_EXCEPTION,
 					"order is not exist");
		}
		order.setIsDelete(IsOrderDelete.DELETED.getCode());
		parkingProvider.deleteParkingRechargeOrder(order);
	}
}
