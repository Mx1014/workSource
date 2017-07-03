// @formatter:off
package com.everhomes.parking;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.parking.*;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.util.DownloadUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.SortField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.order.OrderUtil;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowAutoStepDTO;
import com.everhomes.rest.flow.FlowCaseDetailDTO;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.order.CommonOrderCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class ParkingServiceImpl implements ParkingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingServiceImpl.class);

    private SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    @Autowired
    private ParkingProvider parkingProvider;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private ConfigurationProvider configProvider;
    @Autowired
	private OrderUtil commonOrderUtil;
    @Autowired
	private MessagingService messagingService;
    @Autowired
	private LocaleTemplateService localeTemplateService;
    @Autowired
    private CoordinationProvider coordinationProvider;
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private ContentServerService contentServerService;
    @Autowired
	private FlowService flowService;
    @Autowired
	private FlowProvider flowProvider;
    @Autowired
    private FlowCaseProvider flowCaseProvider;
    @Autowired
    private BigCollectionProvider bigCollectionProvider;
    @Autowired
    private OrganizationProvider organizationProvider;
	@Autowired
	private ScheduleProvider scheduleProvider;
    
    @Override
    public List<ParkingCardDTO> listParkingCards(ListParkingCardsCommand cmd) {

		GetParkingCardsCommand getParkingCardsCommand = ConvertHelper.convert(cmd, GetParkingCardsCommand.class);
		GetParkingCardsResponse response = getParkingCards(getParkingCardsCommand);
        
        return response.getCards();
    }

	public GetParkingCardsResponse getParkingCards(GetParkingCardsCommand cmd) {

		checkPlateNumber(cmd.getPlateNumber());
		Long parkingLotId = cmd.getParkingLotId();
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), parkingLotId);

		String venderName = parkingLot.getVendorName();
		ParkingVendorHandler handler = getParkingVendorHandler(venderName);

		GetParkingCardsResponse response = handler.getParkingCardsByPlate(cmd.getOwnerType(), cmd.getOwnerId(), parkingLotId, cmd.getPlateNumber());

		Long organizationId = cmd.getOrganizationId();
		User user = UserContext.current().getUser();
		Long userId = user.getId();
		String plateOwnerName = user.getNickName();

		if(null != organizationId) {
			OrganizationMember organizationMember = organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, organizationId);
			if(null != organizationMember) {
				plateOwnerName = organizationMember.getContactName();
			}
		}

		for(ParkingCardDTO card: response.getCards()) {
			if(StringUtils.isBlank(card.getPlateOwnerName())) {
				card.setPlateOwnerName(plateOwnerName);
			}
		}

		return response;
	}

	public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
    	
    	Long parkingLotId = cmd.getParkingLotId();
        ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), parkingLotId);

        String venderName = parkingLot.getVendorName();
        ParkingVendorHandler handler = getParkingVendorHandler(venderName);
        
        ListCardTypeResponse response = handler.listCardType(cmd);
        
    	return response;
    }
    
    @Override
    public List<ParkingRechargeRateDTO> listParkingRechargeRates(ListParkingRechargeRatesCommand cmd){
    	
    	Long parkingLotId = cmd.getParkingLotId();
        ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), parkingLotId);
        
        String venderName = parkingLot.getVendorName();
        ParkingVendorHandler handler = getParkingVendorHandler(venderName);
        
        List<ParkingRechargeRateDTO> parkingRechargeRateList = handler.getParkingRechargeRates(cmd.getOwnerType(), cmd.getOwnerId(),
        		parkingLotId, cmd.getPlateNumber(), cmd.getCardNo());
        
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
    	
    	if(cmd.getOwnerId() == null || StringUtils.isBlank(cmd.getOwnerType())){
    		LOGGER.error("OwnerId or ownertype cannot be null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"OwnerId or ownertype cannot be null.");
    	}
    	User user = UserContext.current().getUser();
    	List<ParkingLot> list = parkingProvider.listParkingLots(cmd.getOwnerType(), cmd.getOwnerId());
    	
    	List<ParkingLotDTO> parkingLotList = list.stream().map(r -> {
    		ParkingLotDTO dto = ConvertHelper.convert(r, ParkingLotDTO.class);
    		
        	Flow flow = flowService.getEnabledFlow(user.getNamespaceId(), ParkingFlowConstant.PARKING_RECHARGE_MODULE, 
        			FlowModuleType.NO_MODULE.getCode(), r.getId(), FlowOwnerType.PARKING.getCode());
        	
        	if(null == flow)
        		dto.setFlowMode(ParkingRequestFlowType.FORBIDDEN.getCode());
        	else {
        		String tag1 = flow.getStringTag1();
            	Integer flowMode = Integer.valueOf(tag1);
            	dto.setFlowMode(flowMode);
        	}
        	
    		return dto;
    	}).collect(Collectors.toList());
    	
    	return parkingLotList;
    }
    
    @Scheduled(cron="0 1 0 * * ? ")
	public void createParkingStatistics(){
		if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE){
			//执行任务区
			long now = System.currentTimeMillis();
			Timestamp startDate = getBeginOfDay(now);
			Timestamp endDate = getEndOfDay(now);

			this.coordinationProvider.getNamedLock(CoordinationLocks.PARKING_STATISTICS.getCode()).tryEnter(()-> {

				List<ParkingStatistic> list = parkingProvider.listParkingStatistics(null, null, null, startDate);
				if(list.size() != 0)
					return ;
				List<ParkingLot> lots = parkingProvider.listParkingLots(null, null);

				lots.forEach(l -> {
					List<ParkingRechargeOrder> orders = parkingProvider.searchParkingRechargeOrders(l.getOwnerType(), l.getOwnerId(), l.getId(),
							null, null, null, startDate, endDate, null, null, null, null, null);
					BigDecimal totalAmount = new BigDecimal(0);
					for(ParkingRechargeOrder o: orders) {
						if(ParkingRechargeOrderRechargeStatus.RECHARGED.getCode() == o.getRechargeStatus()) {
							totalAmount = totalAmount.add(o.getPrice());
						}
					}

					ParkingStatistic parkingStatistic = new ParkingStatistic();
					parkingStatistic.setNamespaceId(l.getNamespaceId());
					parkingStatistic.setOwnerId(l.getOwnerId());
					parkingStatistic.setOwnerType(l.getOwnerType());
					parkingStatistic.setParkingLotId(l.getId());
					parkingStatistic.setCreateTime(new Timestamp(now));
					parkingStatistic.setAmount(totalAmount);
					parkingStatistic.setDateStr(startDate);

					parkingProvider.createParkingStatistic(parkingStatistic);
				});

			});
		}
		
	}
    
    private static Timestamp getBeginOfDay(Long time){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return new Timestamp(calendar.getTimeInMillis());
	}
	
	private static Timestamp getEndOfDay(Long time){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return new Timestamp(calendar.getTimeInMillis());
	}
    
    @Override
	public ParkingCardRequestDTO requestParkingCard(RequestParkingCardCommand cmd) {
		
    	checkPlateNumber(cmd.getPlateNumber());
    	ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
		
    	String vendor = parkingLot.getVendorName();
    	ParkingVendorHandler handler = getParkingVendorHandler(vendor);

		GetParkingCardsResponse resp = handler.getParkingCardsByPlate(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId(),
        		cmd.getPlateNumber());
        User user = UserContext.current().getUser();
        int cardListSize = resp.getCards().size();
		if(cardListSize > 0){
			LOGGER.error("PlateNumber card is existed, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_PLATE_EXIST,
					"PlateNumber card is existed");
		}

		String ownerType = FlowOwnerType.PARKING.getCode();
    	Flow flow = flowService.getEnabledFlow(user.getNamespaceId(), ParkingFlowConstant.PARKING_RECHARGE_MODULE, 
    			FlowModuleType.NO_MODULE.getCode(), parkingLot.getId(), ownerType);
		Long flowId = flow.getFlowMainId();
		String tag1 = flow.getStringTag1();
		
        if(cardListSize == 0){
        	
        	ParkingFlow parkingFlow = parkingProvider.getParkingRequestCardConfig(parkingLot.getOwnerType(), 
        			parkingLot.getOwnerId(), parkingLot.getId(), flowId);
        	
        	List<ParkingCardRequest> requestlist = parkingProvider.listParkingCardRequests(user.getId(), cmd.getOwnerType(), 
        			cmd.getOwnerId(), cmd.getParkingLotId(), null, null,
        			ParkingCardRequestStatus.INACTIVE.getCode(), flowId, null, null);
        	
        	int requestlistSize = requestlist.size();
        	if(null != parkingFlow && parkingFlow.getMaxRequestNumFlag() == ParkingSupportRequestConfigStatus.SUPPORT.getCode()
        			&& requestlistSize >= parkingFlow.getMaxRequestNum()){
        		LOGGER.error("The card request is rather than max request num, cmd={}", cmd);
    			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_MAX_REQUEST_NUM,
    					"The card request is rather than max request num.");
        	}
        	
        	requestlist = parkingProvider.listParkingCardRequests(user.getId(), cmd.getOwnerType(), 
        			cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(), null,
        			ParkingCardRequestStatus.INACTIVE.getCode(), flowId, null, null);
        	
        	requestlistSize = requestlist.size();
        	if(requestlistSize > 0){
        		LOGGER.error("PlateNumber is already applied, cmd={}", cmd);
    			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_PLATE_APPLIED,
    					"plateNumber is already applied.");
        	}
        	
        }
        ParkingCardRequestDTO parkingCardRequestDTO = new ParkingCardRequestDTO();
        ParkingCardRequest parkingCardRequest = new ParkingCardRequest();
        dbProvider.execute((TransactionStatus status) -> {
    		
    		parkingCardRequest.setOwnerId(cmd.getOwnerId());
    		parkingCardRequest.setOwnerType(cmd.getOwnerType());
    		parkingCardRequest.setParkingLotId(parkingLot.getId());
    		parkingCardRequest.setRequestorEnterpriseId(cmd.getRequestorEnterpriseId());
    		parkingCardRequest.setPlateNumber(cmd.getPlateNumber());
    		parkingCardRequest.setPlateOwnerEntperiseName(cmd.getPlateOwnerEntperiseName());
    		parkingCardRequest.setPlateOwnerName(cmd.getPlateOwnerName());
    		parkingCardRequest.setPlateOwnerPhone(cmd.getPlateOwnerPhone());
    		parkingCardRequest.setRequestorUid(user.getId());
    		//设置一些初始状态
    		parkingCardRequest.setIssueFlag(ParkingCardIssueFlag.UNISSUED.getCode());
    		if(ParkingRequestFlowType.QUEQUE.getCode() == Integer.valueOf(tag1))
    			parkingCardRequest.setStatus(ParkingCardRequestStatus.QUEUEING.getCode());
    		else 
    			parkingCardRequest.setStatus(ParkingCardRequestStatus.AUDITING.getCode());

    		parkingCardRequest.setCreatorUid(user.getId());
    		parkingCardRequest.setCreateTime(new Timestamp(System.currentTimeMillis()));
    		
    		parkingCardRequest.setCarBrand(cmd.getCarBrand());
    		parkingCardRequest.setCarColor(cmd.getCarColor());
    		parkingCardRequest.setCarSerieName(cmd.getCarSerieName());
    		parkingCardRequest.setCarSerieId(cmd.getCarSerieId());
    		if(null != cmd.getCarSerieId()) {
    			ParkingCarSerie carSerie = parkingProvider.findParkingCarSerie(cmd.getCarSerieId());
    			if(null != carSerie) {
        			ParkingCarSerie secondCarSerie = parkingProvider.findParkingCarSerie(carSerie.getParentId());

    				ParkingCarSerie carBrand = parkingProvider.findParkingCarSerie(secondCarSerie.getParentId());
    				parkingCardRequest.setCarSerieName(carSerie.getName());
    				if(null != carBrand)
    					parkingCardRequest.setCarBrand(carBrand.getName());
    			}
    		}

    		parkingProvider.requestParkingCard(parkingCardRequest);
    		
    		addAttachments(cmd.getAttachments(), user.getId(), parkingCardRequest.getId(), ParkingAttachmentType.PARKING_CARD_REQUEST.getCode());

    		//TODO: 新建flowcase
    		CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
    		createFlowCaseCommand.setApplyUserId(user.getId());
    		createFlowCaseCommand.setFlowMainId(flowId);
    		createFlowCaseCommand.setFlowVersion(flow.getFlowVersion());
    		createFlowCaseCommand.setReferId(parkingCardRequest.getId());
    		createFlowCaseCommand.setReferType(EntityType.PARKING_CARD_REQUEST.getCode());
    		createFlowCaseCommand.setContent("车牌号码：" + parkingCardRequest.getPlateNumber() + "\n"
    				+ "车主电话：" + parkingCardRequest.getPlateOwnerPhone());
			createFlowCaseCommand.setCurrentOrganizationId(cmd.getRequestorEnterpriseId());

			if (UserContext.getCurrentNamespaceId().equals(999983)) {
				createFlowCaseCommand.setTitle("停车月卡申请");
			}

        	FlowCase flowCase = flowService.createFlowCase(createFlowCaseCommand);
    		
    		parkingCardRequest.setFlowId(flowCase.getFlowMainId());
    		parkingCardRequest.setFlowVersion(flowCase.getFlowVersion());
    		parkingCardRequest.setFlowCaseId(flowCase.getId());
    		parkingProvider.updateParkingCardRequest(parkingCardRequest);
    		return null;
		});
		parkingCardRequestDTO = ConvertHelper.convert(parkingCardRequest, ParkingCardRequestDTO.class);
		
		Integer count = parkingProvider.waitingCardCount(cmd.getOwnerType(), cmd.getOwnerId(),
					cmd.getParkingLotId(), parkingCardRequest.getCreateTime());
		parkingCardRequestDTO.setRanking(count);
		
		return parkingCardRequestDTO;
    	
	}
    
    public ParkingCardRequestDTO getRequestParkingCardDetail(GetRequestParkingCardDetailCommand cmd) {
    	
    	ParkingCardRequest parkingCardRequest = parkingProvider.findParkingCardRequestById(cmd.getId());
    	
    	ParkingCardRequestDTO dto = ConvertHelper.convert(parkingCardRequest, ParkingCardRequestDTO.class);
    	
    	if(null != parkingCardRequest.getCarSerieId()) {
			ParkingCarSerie carSerie = parkingProvider.findParkingCarSerie(parkingCardRequest.getCarSerieId());
			if(null != carSerie) {
    			ParkingCarSerie secondCarSerie = parkingProvider.findParkingCarSerie(carSerie.getParentId());
    			if(null != secondCarSerie) {
    				ParkingCarSerie carBrand = parkingProvider.findParkingCarSerie(secondCarSerie.getParentId());
    				dto.setCarSerieName(carSerie.getName());
    				if(null != carBrand)
    					dto.setCarBrand(carBrand.getName());
    			}
			}
		}
    	
    	List<ParkingAttachment> attachments = parkingProvider.listParkingAttachments(parkingCardRequest.getId(), 
    			ParkingAttachmentType.PARKING_CARD_REQUEST.getCode());
    	
		List<ParkingAttachmentDTO> attachmentDtos =  attachments.stream().map(r -> {
			ParkingAttachmentDTO attachmentDto = ConvertHelper.convert(r, ParkingAttachmentDTO.class);
			
			String contentUrl = getResourceUrlByUir(r.getContentUri(), 
	                EntityType.USER.getCode(), r.getCreatorUid());
			attachmentDto.setContentUrl(contentUrl);
			attachmentDto.setInformationType(r.getDataType());
			return attachmentDto;
		}).collect(Collectors.toList());
    	
		dto.setAttachments(attachmentDtos);
		
		Integer count = parkingProvider.waitingCardCount(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getParkingLotId(), parkingCardRequest.getCreateTime());
		dto.setRanking(count + 1);
    	return dto;
    }
    
    private String getResourceUrlByUir(String uri, String ownerType, Long ownerId) {
        String url = null;
        if(null != uri && uri.length() > 0) {
            try{
                url = contentServerService.parserUri(uri, ownerType, ownerId);
            }catch(Exception e){
                LOGGER.error("Failed to parse uri, uri=, ownerType=, ownerId=", uri, ownerType, ownerId, e);
            }
        }
        
        return url;
    }
    
    private void addAttachments(List<AttachmentDescriptor> list, Long userId, Long ownerId, String ownerType){
		if(!CollectionUtils.isEmpty(list)){
			for(AttachmentDescriptor ad: list){
				if(null != ad){
					ParkingAttachment attachment = new ParkingAttachment();
					attachment.setContentType(ad.getContentType());
					attachment.setContentUri(ad.getContentUri());
					attachment.setCreateTime(new Timestamp(System.currentTimeMillis()));
					attachment.setCreatorUid(userId);
					attachment.setOwnerId(ownerId);
					attachment.setOwnerType(ownerType);
					attachment.setDataType(ad.getInformationType());
					parkingProvider.createParkingAttachment(attachment);
				}
			}
		}
	}
    
	@Override
    public ListParkingCardRequestResponse listParkingCardRequests(ListParkingCardRequestsCommand cmd){
		
        checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
		
    	ListParkingCardRequestResponse response = new ListParkingCardRequestResponse();
    	cmd.setPageSize(PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize()));
    	User user = UserContext.current().getUser();
    	
    	List<ParkingCardRequest> list = parkingProvider.listParkingCardRequests(user.getId(), cmd.getOwnerType(), 
    			cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(), null, cmd.getPageAnchor(),
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
		
		if(null == cmd.getMonthCount() || cmd.getMonthCount() ==0) {
			LOGGER.error("Invalid MonthCount, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Invalid MonthCount.");
		}
		
		return createParkingOrder(cmd, ParkingRechargeType.MONTHLY.getCode());
	}
	
	@Override
	public CommonOrderDTO createParkingTempOrder(CreateParkingTempOrderCommand cmd) {
		checkOrderToken(cmd.getOrderToken());
		CreateParkingRechargeOrderCommand param = new CreateParkingRechargeOrderCommand();
		param.setOwnerType(cmd.getOwnerType());
		param.setOwnerId(cmd.getOwnerId());
		param.setParkingLotId(cmd.getParkingLotId());
		param.setPlateNumber(cmd.getPlateNumber());
		param.setPayerEnterpriseId(cmd.getPayerEnterpriseId());
		param.setPrice(cmd.getPrice());
		
		return createParkingOrder(param, ParkingRechargeType.TEMPORARY.getCode());

	}
	
	private CommonOrderDTO createParkingOrder(CreateParkingRechargeOrderCommand cmd, Byte rechargeType) {
		checkPlateNumber(cmd.getPlateNumber());
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		String vendor = parkingLot.getVendorName();
    	ParkingVendorHandler handler = getParkingVendorHandler(vendor);
    	
    	ParkingRechargeOrder parkingRechargeOrder = new ParkingRechargeOrder();
		
		User user = UserContext.current().getUser();
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
		
		OrganizationMember organizationMember = organizationProvider.findOrganizationMemberByOrgIdAndUId(user.getId(), cmd.getPayerEnterpriseId());
		if(null != organizationMember) {
			if(null == cmd.getPlateOwnerName())
				cmd.setPlateOwnerName(organizationMember.getContactName());
		}
		
		parkingRechargeOrder.setRechargeType(rechargeType);
		parkingRechargeOrder.setOwnerType(cmd.getOwnerType());
		parkingRechargeOrder.setOwnerId(cmd.getOwnerId());
		parkingRechargeOrder.setParkingLotId(parkingLot.getId());
		parkingRechargeOrder.setPlateNumber(cmd.getPlateNumber());
		parkingRechargeOrder.setPlateOwnerName(null != cmd.getPlateOwnerName()?cmd.getPlateOwnerName():user.getNickName());
		parkingRechargeOrder.setPlateOwnerPhone(cmd.getPlateOwnerPhone());
		
		parkingRechargeOrder.setPayerEnterpriseId(cmd.getPayerEnterpriseId());
		parkingRechargeOrder.setPayerUid(user.getId());
		parkingRechargeOrder.setPayerPhone(userIdentifier.getIdentifierToken());
		parkingRechargeOrder.setCreatorUid(user.getId());
		Long now = System.currentTimeMillis();
		parkingRechargeOrder.setCreateTime(new Timestamp(now));
		
		parkingRechargeOrder.setVendorName(parkingLot.getVendorName());
		parkingRechargeOrder.setCardNumber(cmd.getCardNumber());
		
		parkingRechargeOrder.setStatus(ParkingRechargeOrderStatus.UNPAID.getCode());
		parkingRechargeOrder.setRechargeStatus(ParkingRechargeOrderRechargeStatus.UNRECHARGED.getCode());
		
		parkingRechargeOrder.setOrderNo(createOrderNo(System.currentTimeMillis()));
		
		parkingRechargeOrder.setPrice(cmd.getPrice());
		if(rechargeType.equals(ParkingRechargeType.TEMPORARY.getCode())) {
    		ParkingTempFeeDTO dto = handler.getParkingTempFee(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber());
			if(null != dto && null != dto.getPrice() && 0 != dto.getPrice().compareTo(cmd.getPrice())) {
				LOGGER.error("Overdue fees, cmd={}", cmd);
				throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_TEMP_FEE,
						"Overdue fees");
			}
			parkingRechargeOrder.setOrderToken(dto.getOrderToken());
		}
    	//查询rate
    	else if(rechargeType.equals(ParkingRechargeType.MONTHLY.getCode())) {
    		parkingRechargeOrder.setRateToken(cmd.getRateToken());
    		parkingRechargeOrder.setMonthCount(new BigDecimal(cmd.getMonthCount()));
    		handler.updateParkingRechargeOrderRate(parkingRechargeOrder);
    		
    	}
		
		parkingProvider.createParkingRechargeOrder(parkingRechargeOrder);	
		
		//调用统一处理订单接口，返回统一订单格式
		CommonOrderCommand orderCmd = new CommonOrderCommand();
		orderCmd.setBody(ParkingRechargeType.fromCode(parkingRechargeOrder.getRechargeType()).toString());
		orderCmd.setOrderNo(parkingRechargeOrder.getId().toString());
		orderCmd.setOrderType(OrderType.OrderTypeEnum.PARKING.getPycode());
		orderCmd.setSubject("停车充值订单");
		
		boolean flag = configProvider.getBooleanValue("parking.order.amount", false);
		if(flag) {
			orderCmd.setTotalFee(new BigDecimal(0.02).setScale(2, RoundingMode.FLOOR));
		} else {
			orderCmd.setTotalFee(parkingRechargeOrder.getPrice());
		}

		CommonOrderDTO dto = null;
		try {
			dto = commonOrderUtil.convertToCommonOrderTemplate(orderCmd);
		} catch (Exception e) {
			LOGGER.error("convertToCommonOrder is fail.",e);
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

		List<ParkingRechargeOrder> list = parkingProvider.listParkingRechargeOrders(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getParkingLotId(), cmd.getPlateNumber(), user.getId(), cmd.getPageAnchor(), cmd.getPageSize());

		int size = list.size();
    	if(size > 0){
    		response.setOrders(list.stream().map(r -> ConvertHelper.convert(r, ParkingRechargeOrderDTO.class))
    				.collect(Collectors.toList()));
    		if(size != cmd.getPageSize()){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(size-1).getRechargeTime().getTime());
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
		checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
		Timestamp startDate = null;
		Timestamp endDate = null;
		if(null != cmd.getStartDate())
			startDate = new Timestamp(cmd.getStartDate());
		if(null != cmd.getEndDate())
			endDate = new Timestamp(cmd.getEndDate());
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		List<ParkingRechargeOrder> list = parkingProvider.searchParkingRechargeOrders(cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(), cmd.getPlateOwnerName(),
				cmd.getPayerPhone(), startDate, endDate, cmd.getRechargeType(), cmd.getPaidType(), cmd.getCardNumber(),
				cmd.getPageAnchor(), pageSize);
    	int size = list.size(); 				
    	if(size > 0){
    		response.setOrders(list.stream().map(r -> ConvertHelper.convert(r, ParkingRechargeOrderDTO.class))
    				.collect(Collectors.toList()));
    		if(pageSize != null && size != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(size-1).getCreateTime().getTime());
        	}
    	}
    	
    	BigDecimal totalAmount = parkingProvider.countParkingRechargeOrders(cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(), cmd.getPlateOwnerName(),
				cmd.getPayerPhone(), startDate, endDate, cmd.getRechargeType(), cmd.getPaidType());
    	response.setTotalAmount(totalAmount);
    	
		return response;
	}

	@Override
	public ListParkingCardRequestResponse searchParkingCardRequests(SearchParkingCardRequestsCommand cmd) {
		ListParkingCardRequestResponse response = new ListParkingCardRequestResponse();
		Timestamp startDate = null;
		Timestamp endDate = null;
		if(null != cmd.getStartDate())
			startDate = new Timestamp(cmd.getStartDate());
		if(null != cmd.getEndDate())
			endDate = new Timestamp(cmd.getEndDate());
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		Flow flow = flowProvider.getFlowById(cmd.getFlowId());
		SortField order = null;
		//排序
		if (null != flow) {
			Integer flowMode = Integer.valueOf(flow.getStringTag1());
			if (ParkingCardRequestStatus.AUDITING.getCode() == cmd.getStatus()) {
				order = Tables.EH_PARKING_CARD_REQUESTS.CREATE_TIME.asc();
			}else if (ParkingCardRequestStatus.QUEUEING.getCode() == cmd.getStatus()) {
				if (ParkingRequestFlowType.QUEQUE.getCode().equals(flowMode)) {
					order = Tables.EH_PARKING_CARD_REQUESTS.CREATE_TIME.asc();
				}else {
					order = Tables.EH_PARKING_CARD_REQUESTS.AUDIT_SUCCEED_TIME.asc();
				}
			}else if (ParkingCardRequestStatus.PROCESSING.getCode() == cmd.getStatus()) {
				order = Tables.EH_PARKING_CARD_REQUESTS.ISSUE_TIME.desc();
			}else if (ParkingCardRequestStatus.SUCCEED.getCode() == cmd.getStatus()) {
				order = Tables.EH_PARKING_CARD_REQUESTS.PROCESS_SUCCEED_TIME.desc();
			}else if (ParkingCardRequestStatus.OPENED.getCode() == cmd.getStatus()) {
				order = Tables.EH_PARKING_CARD_REQUESTS.OPEN_CARD_TIME.desc();
			}else if (ParkingCardRequestStatus.INACTIVE.getCode() == cmd.getStatus()) {
				order = Tables.EH_PARKING_CARD_REQUESTS.CANCEL_TIME.desc();
			}

		}

    	List<ParkingCardRequest> list = parkingProvider.searchParkingCardRequests(cmd.getOwnerType(), 
    			cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(), cmd.getPlateOwnerName(), 
    			cmd.getPlateOwnerPhone(), startDate, endDate, cmd.getStatus(), cmd.getCarBrand(), 
    			cmd.getCarSerieName(), cmd.getPlateOwnerEntperiseName(), cmd.getFlowId(), order, cmd.getPageAnchor(), pageSize);
    	
    	Long userId = UserContext.current().getUser().getId();
    	int size = list.size();
    	if(size > 0){
    		response.setRequests(list.stream().map(r -> {
    			ParkingCardRequestDTO dto = ConvertHelper.convert(r, ParkingCardRequestDTO.class);
    			
    			FlowCaseDetailDTO flowCaseDetailDTO = flowService.getFlowCaseDetail(r.getFlowCaseId()
                		, userId
                		, FlowUserType.PROCESSOR
                		, false);
    			
    			dto.setButtons(flowCaseDetailDTO.getButtons());
    			return dto;
    		}).collect(Collectors.toList()));
    		
    		if(size != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(size-1).getCreateTime().getTime());
        	}
    	}
    	return response;
	}

	@Override
	public void setParkingLotConfig(SetParkingLotConfigCommand cmd){
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
		
		if(null == cmd.getIsSupportRecharge()){
        	LOGGER.error("IsSupportRecharge cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"IsSupportRecharge cannot be null.");
        }
		if(ParkingSupportRechargeStatus.SUPPORT.getCode() == cmd.getIsSupportRecharge()) {
			
			parkingLot.setCardReserveDays(cmd.getReserveDay());
	        parkingLot.setRechargeMonthCount(cmd.getRechargeMonthCount());
	        parkingLot.setRechargeType(cmd.getRechargeType());
		}
		
		parkingLot.setIsSupportRecharge(cmd.getIsSupportRecharge());
        parkingProvider.setParkingLotConfig(parkingLot);
	}
	
	@Override
	public void setParkingCardIssueFlag(SetParkingCardIssueFlagCommand cmd){
		
        checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
        if(null == cmd.getId()){
        	LOGGER.error("Id cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Id cannot be null.");
        }
        
        ParkingCardRequest parkingCardRequest = parkingProvider.findParkingCardRequestById(cmd.getId());
        if(null == parkingCardRequest){
        	LOGGER.error("ParkingCardRequest not found, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
				"ParkingCardRequest not found");
        }
//        if(parkingCardRequest.getStatus() != ParkingCardRequestStatus.NOTIFIED.getCode()){
//        	LOGGER.error("ParkingCardRequest status is not notified, cmd={}", cmd);
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
//				"ParkingCardRequest status is not notified.");
//        }
        //设置已领取状态和 领取时间
//        parkingCardRequest.setStatus(ParkingCardRequestStatus.ISSUED.getCode());
        parkingCardRequest.setIssueFlag(ParkingCardIssueFlag.ISSUED.getCode());
        parkingCardRequest.setIssueTime(new Timestamp(System.currentTimeMillis()));
        parkingProvider.updateParkingCardRequest(Collections.singletonList(parkingCardRequest));
	}

	@Override
	public void issueParkingCards(IssueParkingCardsCommand cmd) {
		
		Byte status = cmd.getStatus();
		Integer count = cmd.getCount();
		Long flowId = cmd.getFlowId();
		if(null == count) {
        	LOGGER.error("Count cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Count cannot be null.");
        }
		if(null == status) {
        	LOGGER.error("Status cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Status cannot be null.");
        }
		
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
		
		ParkingFlow parkingFlow = parkingProvider.getParkingRequestCardConfig(cmd.getOwnerType(), cmd.getOwnerId(), 
				parkingLot.getId(), flowId);
		
		Integer issuedCount = parkingProvider.countParkingCardRequest(cmd.getOwnerType(), cmd.getOwnerId(), 
				parkingLot.getId(), flowId, ParkingCardRequestStatus.SUCCEED.getCode(), null);
		
		Integer totalCount = 0;
		if(null != parkingFlow)
			totalCount = parkingFlow.getMaxIssueNum();
		Integer surplusCount = totalCount - issuedCount;
		
		if(null != parkingFlow && parkingFlow.getMaxIssueNumFlag() == ParkingSupportRequestConfigStatus.SUPPORT.getCode()) {
			if(status == ParkingCardRequestStatus.QUEUEING.getCode()) {
				if(count > surplusCount) {
					LOGGER.error("Count is rather than surplusCount.");
		    		throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_ISSUE_CARD_SURPLUS_NUM,
		    				"Count is rather than surplusCount.");
				}
			}else {
				if(count > surplusCount) {
					LOGGER.error("Count is rather than surplusCount.");
		    		throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_PROCESS_CARD_SURPLUS_NUM,
		    				"Count is rather than surplusCount.");
				}
			}
		}
		
		if(status == ParkingCardRequestStatus.QUEUEING.getCode()) {
			Integer quequeCount = parkingProvider.countParkingCardRequest(cmd.getOwnerType(), cmd.getOwnerId(), 
					parkingLot.getId(), flowId, null, ParkingCardRequestStatus.QUEUEING.getCode());

			if(count > quequeCount) {
				LOGGER.error("Count is rather than quequeCount.");
	    		throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_ISSUE_CARD_QUEQUE_NUM,
	    				"Count is rather than quequeCount.");
			}
		}else {
			Integer processingCount = parkingProvider.countParkingCardRequest(cmd.getOwnerType(), cmd.getOwnerId(), 
					parkingLot.getId(), flowId, null, ParkingCardRequestStatus.PROCESSING.getCode());
			if(count > processingCount) {
				LOGGER.error("Count is rather than processingCount.");
	    		throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_PROCESS_CARD_QUEQUE_NUM,
	    				"Count is rather than processingCount.");
			}
		}
		
		
		dbProvider.execute((TransactionStatus transactionStatus) -> {
			Flow flow = flowProvider.findSnapshotFlow(flowId, FlowConstants.FLOW_CONFIG_START);
	        String tag1 = flow.getStringTag1();
			StringBuilder strBuilder = new StringBuilder();
			List<ParkingCardRequest> list = null;
			if(status == ParkingCardRequestStatus.QUEUEING.getCode()) {
				list = parkingProvider.listParkingCardRequests(null, cmd.getOwnerType(), 
		    			cmd.getOwnerId(), cmd.getParkingLotId(), null, ParkingCardRequestStatus.QUEUEING.getCode(),
		    			null, flowId, null, cmd.getCount());
		    	
				if(ParkingRequestFlowType.QUEQUE.getCode() == Integer.valueOf(tag1)) {

					setParkingCardRequestsStatus(list, strBuilder, ParkingCardRequestStatus.PROCESSING.getCode());
				}else {
					
					setParkingCardRequestsStatus(list, strBuilder, ParkingCardRequestStatus.SUCCEED.getCode());
				}
			}else {
				list = parkingProvider.listParkingCardRequests(null, cmd.getOwnerType(), 
		    			cmd.getOwnerId(), cmd.getParkingLotId(), null, ParkingCardRequestStatus.PROCESSING.getCode(),
		    			null, flowId, null, cmd.getCount());
				setParkingCardRequestsStatus(list, strBuilder, ParkingCardRequestStatus.SUCCEED.getCode());
			}
	    	
	    	parkingProvider.updateParkingCardRequest(list);
	    	
	    	list.forEach(q -> {
	    		FlowCase flowCase = flowCaseProvider.getFlowCaseById(q.getFlowCaseId());
	    		FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
	    		stepDTO.setFlowCaseId(q.getFlowCaseId());
	    		stepDTO.setFlowMainId(q.getFlowId());
	    		stepDTO.setFlowVersion(q.getFlowVersion());
	    		stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
	    		stepDTO.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
	    		stepDTO.setStepCount(flowCase.getStepCount());
	    		flowService.processAutoStep(stepDTO);
	    	});
	    	
	    	if(LOGGER.isDebugEnabled()) {
	    	    LOGGER.debug("Issue parking cards, requestIds=[{}]", strBuilder.toString());
	    	}
	    	Integer namespaceId = UserContext.getCurrentNamespaceId();
	    	Map<String, Object> map = new HashMap<String, Object>();
			String deadline = deadline(parkingLot.getCardReserveDays());
		    map.put("deadline", deadline);
			String scope = ParkingNotificationTemplateCode.SCOPE;
			int code = ParkingNotificationTemplateCode.USER_APPLY_CARD;
			String locale = "zh_CN";
			String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(namespaceId, scope, code, locale, map, "");
			list.forEach(applier -> {
				sendMessageToUser(applier.getRequestorUid(), notifyTextForApplicant);
			});
			return null;
		});
    	
	}

	private void setParkingCardRequestsStatus(List<ParkingCardRequest> list, StringBuilder strBuilder, Byte status) {
		if(ParkingCardRequestStatus.PROCESSING.getCode() == status) {
			list.stream().forEach(r -> {
				r.setIssueTime(new Timestamp(System.currentTimeMillis()));
				r.setStatus(status);
				if(strBuilder.length() > 0) {
				    strBuilder.append(", ");
				}
				strBuilder.append(r.getId());
			});
		}else {
			list.stream().forEach(r -> {
				r.setProcessSucceedTime(new Timestamp(System.currentTimeMillis()));
				r.setStatus(status);
				if(strBuilder.length() > 0) {
				    strBuilder.append(", ");
				}
				strBuilder.append(r.getId());
			});
		}
		
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
		
		LOGGER.debug("OrderEmbeddedHandler={}", orderEmbeddedHandler.getClass().getName());
		
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
		if(null != activity)
			dto = ConvertHelper.convert(activity, ParkingActivityDTO.class);
		return dto;
	}
	
    private ParkingLot checkParkingLot(String ownerType,Long ownerId,Long parkingLotId){
    	if(null == ownerId) {
        	LOGGER.error("OwnerId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OwnerId cannot be null.");
        }
    	
    	if(StringUtils.isBlank(ownerType)) {
        	LOGGER.error("OwnerType cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OwnerType cannot be null.");
        }
    	
    	if(null == parkingLotId) {
        	LOGGER.error("ParkingLotId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"ParkingLotId cannot be null.");
        }
    	
    	ParkingLot parkingLot = parkingProvider.findParkingLotById(parkingLotId);
        if(null == parkingLot) {
        	LOGGER.error("ParkingLot not found, parkingLotId={}", parkingLotId);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    			"ParkingLot not found");
        }
        // 检查参数里的ownerType和ownerId是否与查出来停车场里的匹配
        if(null != ownerId && ownerId.longValue() != parkingLot.getOwnerId().longValue()) {
        	LOGGER.error("OwnerId is not match with parkingLot ownerId, ownerId={}", ownerId);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
    				"OwnerId is not match with parkingLot ownerId.");
        }
        if(ParkingOwnerType.fromCode(parkingLot.getOwnerType()) != ParkingOwnerType.fromCode(ownerType)){
            LOGGER.error("Ownertype is not match with parkingLot ownertype, ownerType={}", ownerType);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
    				"Ownertype is not match with parkingLot ownertype.");
        }
        return parkingLot;
    }
    
    private void checkPlateNumber(String plateNumber){
    	if(StringUtils.isBlank(plateNumber)) {
        	LOGGER.error("PlateNumber cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"PlateNumber cannot be null.");
        }
    }
    
    private void checkOrderToken(String orderToken){
    	if(StringUtils.isBlank(orderToken)) {
        	LOGGER.error("OrderToken cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OrderToken cannot be null.");
        }
    }
    
//    @Scheduled(cron="0 0 2 * * ? ")
//   	@Override
//   	public void invalidApplier() {
//   		LOGGER.info("update invalid appliers.");
//   		List<ParkingLot> list = parkingProvider.listParkingLots(null, null);
//   		for(ParkingLot parkingLot:list){
//   			Integer days = parkingLot.getCardReserveDays();
//   			long time = System.currentTimeMillis() - days * 24 * 60 * 60 * 1000;
//   			parkingProvider.updateInvalidAppliers(new Timestamp(time),parkingLot.getId());
//   		}
//   		
//   	}
    
	private OrderEmbeddedHandler getOrderHandler(String orderType) {
		return PlatformContext.getComponent(OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX+this.getOrderTypeCode(orderType));
	}
	
	private String getOrderTypeCode(String orderType) {
		Integer code = OrderType.OrderTypeEnum.getCodeByPyCode(orderType);
		if(null == code){
			LOGGER.error("Invalid parameter, orderType not found, orderType={}", orderType);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameter, orderType not found");
		}
		return String.valueOf(code);
	}
	@Override
	public HttpServletResponse exportParkingRechageOrders(SearchParkingRechargeOrdersCommand cmd, HttpServletResponse response){
		Timestamp startDate = null;
		Timestamp endDate = null;
		if(cmd.getStartDate() != null)
			startDate = new Timestamp(cmd.getStartDate());
		if(cmd.getEndDate() != null)
			new Timestamp(cmd.getEndDate());

		List<ParkingRechargeOrder> list = parkingProvider.searchParkingRechargeOrders(cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(), cmd.getPlateOwnerName(),
				cmd.getPayerPhone(), startDate, endDate, cmd.getRechargeType(), cmd.getPaidType(), cmd.getCardNumber(),
				cmd.getPageAnchor(), cmd.getPageSize());
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
		row.createCell(2).setCellValue("用户名");
		row.createCell(3).setCellValue("手机号");
		row.createCell(4).setCellValue("缴费时间");
		row.createCell(5).setCellValue("缴费月数");
		row.createCell(6).setCellValue("金额");
		row.createCell(7).setCellValue("支付方式");
		row.createCell(8).setCellValue("缴费类型"); 
		for(int i=0;i<list.size();i++){
			Row tempRow = sheet.createRow(i + 1);
			ParkingRechargeOrder order = list.get(i);
			tempRow.createCell(0).setCellValue(String.valueOf(order.getOrderNo()));
			tempRow.createCell(1).setCellValue(order.getPlateNumber());
			tempRow.createCell(2).setCellValue(order.getPlateOwnerName());
			tempRow.createCell(3).setCellValue(order.getPayerPhone());
			tempRow.createCell(4).setCellValue(order.getRechargeTime()==null?"":datetimeSF.format(order.getRechargeTime()));
			tempRow.createCell(5).setCellValue(null == order.getMonthCount()?"":order.getMonthCount().toString());
			tempRow.createCell(6).setCellValue(order.getPrice().doubleValue());
			VendorType type = VendorType.fromCode(order.getPaidType());
			tempRow.createCell(7).setCellValue(null==type?"":type.getDescribe());
			tempRow.createCell(8).setCellValue(ParkingRechargeType.fromCode(order.getRechargeType()).getDescribe());
			
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			wb.write(out);
			DownloadUtils.download(out, response);
		} catch (IOException e) {
			LOGGER.error("exportParkingRechageOrders is fail. {}",e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"exportParkingRechageOrders is fail.");
		}
		
		return response;
	}
	
	@Override
	public void deleteParkingRechargeOrder(DeleteParkingRechargeOrderCommand cmd) {
		ParkingRechargeOrder order = parkingProvider.findParkingRechargeOrderById(cmd.getId());
		if(null == order){
			LOGGER.error("Order not found, cmd={}", cmd);
 			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
 					ErrorCodes.ERROR_GENERAL_EXCEPTION,
 					"Order not found");
		}
		order.setIsDelete(IsOrderDelete.DELETED.getCode());
		parkingProvider.updateParkingRechargeOrder(order);
	}
	
	private Long createOrderNo(Long time) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//		String prefix = sdf.format(new Date());
		String suffix = String.valueOf(generateRandomNumber(3));

		return Long.valueOf(String.valueOf(time) + suffix);
	}

	/**
	 *
	 * @param n 创建n位随机数
	 * @return
	 */
	private long generateRandomNumber(int n){
		return (long)((Math.random() * 9 + 1) * Math.pow(10, n-1));
	}

	@Override
	public ParkingTempFeeDTO getParkingTempFee(GetParkingTempFeeCommand cmd) {
		checkPlateNumber(cmd.getPlateNumber());
    	ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
		
    	String vendor = parkingLot.getVendorName();
    	ParkingVendorHandler handler = getParkingVendorHandler(vendor);
    	
    	ParkingTempFeeDTO dto = handler.getParkingTempFee(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber());
		return dto;
	}

	@Override
	public ListParkingCarSeriesResponse listParkingCarSeries(ListParkingCarSeriesCommand cmd) {
		
		ListParkingCarSeriesResponse response = new ListParkingCarSeriesResponse();
//		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		Integer pageSize = cmd.getPageSize();

		List<ParkingCarSerie> list = parkingProvider.listParkingCarSeries(cmd.getParentId(), cmd.getPageAnchor(), pageSize);
    	int size = list.size(); 				
    	if(size > 0){
    		response.setCarSeries(list.stream().map(r -> ConvertHelper.convert(r, ParkingCarSerieDTO.class))
    				.collect(Collectors.toList()));
    		if(null != pageSize) {
    			if(size != pageSize){
            		response.setNextPageAnchor(null);
            	}else{
            		response.setNextPageAnchor(list.get(size-1).getId());
            	}	
    		}
    	}
    	
		return response;
	}

	@Override
	public ParkingRequestCardConfigDTO getParkingRequestCardConfig(HttpServletRequest request, GetParkingRequestCardConfigCommand cmd) {
		
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
    	
		User user = UserContext.current().getUser();
		Long flowId = cmd.getFlowId();
    	if(null == flowId) {
        	Flow flow = flowService.getEnabledFlow(user.getNamespaceId(), ParkingFlowConstant.PARKING_RECHARGE_MODULE, 
        			FlowModuleType.NO_MODULE.getCode(), cmd.getParkingLotId(), FlowOwnerType.PARKING.getCode());

        	flowId = flow.getFlowMainId();
//        	LOGGER.error("FlowId cannot be null.");
//    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//    				"FlowId cannot be null.");
        }
		
		ParkingFlow parkingFlow = parkingProvider.getParkingRequestCardConfig(cmd.getOwnerType(), cmd.getOwnerId(), parkingLot.getId(), flowId);
		
		ParkingRequestCardConfigDTO dto = ConvertHelper.convert(parkingFlow, ParkingRequestCardConfigDTO.class);
		
		String host =  configProvider.getValue(UserContext.getCurrentNamespaceId(), "home.url", "");

		if(null != parkingFlow) {
//			GetSignatureCommandResponse result = userService.getSignature();
//			StringBuilder sb = new StringBuilder();
//			sb.append("&id=").append(result.getId());
//			sb.append("&signature=").append(result.getSignature());
//			sb.append("&appKey=").append(result.getAppKey());
//			sb.append("&timeStamp=").append(result.getTimeStamp());
//			sb.append("&randomNum=").append(result.getRandomNum());
			dto.setCardAgreementUrl(host + "/web/lib/html/park_payment_review.html?configId=" + parkingFlow.getId());

		}
		return dto;
	}

	@Override
	public void setParkingRequestCardConfig(SetParkingRequestCardConfigCommand cmd) {
		
    	ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
    	Long flowId = cmd.getFlowId();
    	if(null == flowId) {
        	LOGGER.error("FlowId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"FlowId cannot be null.");
        }
    	
		Integer namespaceId = UserContext.current().getUser().getNamespaceId();
		ParkingFlow parkingFlow = parkingProvider.getParkingRequestCardConfig(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId(), flowId);
		if(null == parkingFlow) {
			parkingFlow = new ParkingFlow();
			parkingFlow.setNamespaceId(namespaceId);
			parkingFlow.setOwnerId(cmd.getOwnerId());
			parkingFlow.setOwnerType(cmd.getOwnerType());
			parkingFlow.setParkingLotId(parkingLot.getId());
			parkingFlow.setCardAgreement(cmd.getCardAgreement());
			parkingFlow.setCardRequestTip(cmd.getCardRequestTip());
			parkingFlow.setMaxIssueNum(cmd.getMaxIssueNum());
			parkingFlow.setRequestMonthCount(cmd.getRequestMonthCount());
			parkingFlow.setRequestRechargeType(cmd.getRequestRechargeType());
			parkingFlow.setFlowId(flowId);
			parkingFlow.setMaxRequestNum(cmd.getMaxRequestNum());
			parkingFlow.setCardAgreementFlag(cmd.getCardAgreementFlag());
			parkingFlow.setCardRequestTipFlag(cmd.getCardRequestTipFlag());
			parkingFlow.setMaxIssueNumFlag(cmd.getMaxIssueNumFlag());
			parkingFlow.setMaxRequestNumFlag(cmd.getMaxRequestNumFlag());
			parkingProvider.createParkingRequestCardConfig(parkingFlow);
		}else {
			parkingFlow.setCardAgreement(cmd.getCardAgreement());
			parkingFlow.setCardRequestTip(cmd.getCardRequestTip());
			parkingFlow.setMaxIssueNum(cmd.getMaxIssueNum());
			parkingFlow.setRequestMonthCount(cmd.getRequestMonthCount());
			parkingFlow.setRequestRechargeType(cmd.getRequestRechargeType());
			parkingFlow.setMaxRequestNum(cmd.getMaxRequestNum());
			parkingFlow.setCardAgreementFlag(cmd.getCardAgreementFlag());
			parkingFlow.setCardRequestTipFlag(cmd.getCardRequestTipFlag());
			parkingFlow.setMaxIssueNumFlag(cmd.getMaxIssueNumFlag());
			parkingFlow.setMaxRequestNumFlag(cmd.getMaxRequestNumFlag());
			parkingProvider.updatetParkingRequestCardConfig(parkingFlow);

		}
	}

	@Override
	public OpenCardInfoDTO getOpenCardInfo(GetOpenCardInfoCommand cmd) {
		
		checkPlateNumber(cmd.getPlateNumber());
    	ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
		
    	String vendor = parkingLot.getVendorName();
    	ParkingVendorHandler handler = getParkingVendorHandler(vendor);
    	
    	OpenCardInfoDTO dto = handler.getOpenCardInfo(cmd);		
		
		return dto;
	}

	@Override
	public SurplusCardCountDTO getSurplusCardCount(GetParkingRequestCardConfigCommand cmd) {
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
    	
		Long flowId = cmd.getFlowId();
    	if(null == flowId) {
        	LOGGER.error("FlowId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"FlowId cannot be null.");
        }
		
		ParkingFlow parkingFlow = parkingProvider.getParkingRequestCardConfig(cmd.getOwnerType(), cmd.getOwnerId(), 
				parkingLot.getId(), flowId);
		
		Integer count = parkingProvider.countParkingCardRequest(cmd.getOwnerType(), cmd.getOwnerId(), 
				parkingLot.getId(), flowId, ParkingCardRequestStatus.SUCCEED.getCode(), null);
		
		Integer totalCount = 0;
		Byte maxIssueNumFlag = 0;
		if(null != parkingFlow) {
			maxIssueNumFlag = parkingFlow.getMaxIssueNumFlag();
			totalCount = parkingFlow.getMaxIssueNum();
		}
			
		SurplusCardCountDTO dto = new SurplusCardCountDTO();
		
		dto.setTotalCount(totalCount);
		dto.setSurplusCount(totalCount - count);
		dto.setMaxIssueNumFlag(maxIssueNumFlag);
		return dto;
	}

	@Override
	public ParkingRequestCardAgreementDTO getParkingRequestCardAgreement(GetParkingRequestCardAgreementCommand cmd) {

		ParkingRequestCardAgreementDTO dto = new ParkingRequestCardAgreementDTO();
		ParkingFlow parkingFlow = parkingProvider.findParkingRequestCardConfig(cmd.getConfigId());
		
		if(null != parkingFlow)
			dto.setAgreement(parkingFlow.getCardAgreement());
		return dto;
	}

    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

	@Override
	public ParkingCardDTO getRechargeResult(GetRechargeResultCommand cmd) {
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		ParkingRechargeOrder order = parkingProvider.findParkingRechargeOrderById(cmd.getOrderId());
		
		if(null == order) {
			LOGGER.error("Order not found, cmd={}", cmd);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Order not found.");
		}
		
		Byte orderStatus = order.getRechargeStatus();
		if(orderStatus == ParkingRechargeOrderRechargeStatus.RECHARGED.getCode()) {
			ListParkingCardsCommand listParkingCardsCommand = new ListParkingCardsCommand(); 
			listParkingCardsCommand.setOwnerId(cmd.getOwnerId());
			listParkingCardsCommand.setOwnerType(cmd.getOwnerType());
			listParkingCardsCommand.setParkingLotId(order.getParkingLotId());
			listParkingCardsCommand.setPlateNumber(order.getPlateNumber());
			List<ParkingCardDTO> cards = listParkingCards(listParkingCardsCommand);
			return cards.get(0);
		}
		
		String key = "parking-recharge" + order.getId();
		String value = String.valueOf(order.getId());
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
      
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        
//        Object value = .get(key);
        long now = System.currentTimeMillis();
        long eTime = now + 5 * 1000;
        long sTime = eTime;
//        NamedLock lock =coordinationProvider.getNamedLock(CoordinationLocks.PARKING_RECHARGE.getCode() + "_" + order.getId());
//        lock.enter(()-> {
////			tempss.put(order.getId(), order);
//        	lock.setLockAcquireTimeoutSeconds(5);
			valueOperations.set(key, value);

			LOGGER.error("wait order notify, cmd={}, startTime={}", cmd, eTime);

    		while(orderStatus == ParkingRechargeOrderRechargeStatus.UNRECHARGED.getCode() 
    				&& null != valueOperations.get(key) && eTime >= sTime) {
    			
    			try {
//    				lock.wait(5000);
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    			sTime = System.currentTimeMillis();
    		}
    		
    		if(sTime >= eTime) {
    			LOGGER.error("Get recharge result time out, cmd={}", cmd);
        		throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_REQUEST_SERVER,
        				"Get recharge result time out.");
    		}
    		
    		ListParkingCardsCommand listParkingCardsCommand = new ListParkingCardsCommand(); 
			listParkingCardsCommand.setOwnerId(cmd.getOwnerId());
			listParkingCardsCommand.setOwnerType(cmd.getOwnerType());
			listParkingCardsCommand.setParkingLotId(order.getParkingLotId());
			listParkingCardsCommand.setPlateNumber(order.getPlateNumber());
			List<ParkingCardDTO> cards = listParkingCards(listParkingCardsCommand);
			return cards.get(0);
		
	}

	@Override
	public void synchronizedData(ListParkingCardRequestsCommand cmd) {
		
		Integer namesapceId = UserContext.getCurrentNamespaceId();
		User user = UserContext.current().getUser();
		
		String ownerType = FlowOwnerType.PARKING.getCode();
    	Flow flow = flowService.getEnabledFlow(namesapceId, ParkingFlowConstant.PARKING_RECHARGE_MODULE, 
    			FlowModuleType.NO_MODULE.getCode(), cmd.getParkingLotId(), ownerType);
    	
    	if(null == flow) {
    		LOGGER.error("Enabled flow not found, cmd={}", cmd);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Enabled flow not found.");
    	}
    	
		Long flowId = flow.getFlowMainId();
		
		List<ParkingCardRequest> list = parkingProvider.listParkingCardRequests(null, cmd.getOwnerType(), 
    			cmd.getOwnerId(), cmd.getParkingLotId(), null, (byte)1, null, null, null, null);
		
		for(ParkingCardRequest request: list) {

			CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
			createFlowCaseCommand.setApplyUserId(user.getId());
			createFlowCaseCommand.setFlowMainId(flowId);
			createFlowCaseCommand.setFlowVersion(flow.getFlowVersion());
			createFlowCaseCommand.setReferId(request.getId());
			createFlowCaseCommand.setReferType(EntityType.PARKING_CARD_REQUEST.getCode());
			createFlowCaseCommand.setContent("车牌号码：" + request.getPlateNumber() + "\n"
					+ "车主电话：" + request.getPlateOwnerPhone());
	    	
	    	FlowCase flowCase = flowService.createFlowCase(createFlowCaseCommand);
			
	    	request.setFlowId(flowCase.getFlowMainId());
	    	request.setFlowVersion(flowCase.getFlowVersion());
	    	request.setFlowCaseId(flowCase.getId());
	    	request.setStatus(ParkingCardRequestStatus.QUEUEING.getCode());
			parkingProvider.updateParkingCardRequest(request);
		}
		
		
	}

	@Override
	public ParkingCarLockInfoDTO getParkingCarLockInfo(GetParkingCarLockInfoCommand cmd) {

		checkPlateNumber(cmd.getPlateNumber());
		Long parkingLotId = cmd.getParkingLotId();
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), parkingLotId);

		String venderName = parkingLot.getVendorName();
		ParkingVendorHandler handler = getParkingVendorHandler(venderName);

		ParkingCarLockInfoDTO dto = handler.getParkingCarLockInfo(cmd);

		if (null != dto) {
			dto.setOwnerId(cmd.getOwnerId());
			dto.setOwnerType(cmd.getOwnerType());
			dto.setParkingLotId(cmd.getParkingLotId());
			dto.setPlateNumber(cmd.getPlateNumber());
			dto.setParkingLotName(parkingLot.getName());

			Long organizationId = cmd.getOrganizationId();
			User user = UserContext.current().getUser();
			Long userId = user.getId();
			String plateOwnerName = user.getNickName();

			if(null != organizationId) {
				OrganizationMember organizationMember = organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, organizationId);
				if(null != organizationMember) {
					plateOwnerName = organizationMember.getContactName();
				}
			}
			if(StringUtils.isBlank(dto.getPlateOwnerName())) {
				dto.setPlateOwnerName(plateOwnerName);
			}
		}

		return dto;
	}

	@Override
	public void lockParkingCar(LockParkingCarCommand cmd) {
		checkPlateNumber(cmd.getPlateNumber());
		Long parkingLotId = cmd.getParkingLotId();
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), parkingLotId);

		String venderName = parkingLot.getVendorName();
		ParkingVendorHandler handler = getParkingVendorHandler(venderName);

		handler.lockParkingCar(cmd);
	}

	@Override
	public GetParkingCarNumsResponse getParkingCarNums(GetParkingCarNumsCommand cmd) {
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
		
		String vendor = parkingLot.getVendorName();
    	ParkingVendorHandler handler = getParkingVendorHandler(vendor);
    	
    	GetParkingCarNumsResponse response = handler.getParkingCarNums(cmd);
    	
		return response;
	}

}
