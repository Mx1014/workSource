// @formatter:off
package com.everhomes.parking;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.asset.PaymentConstants;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.bus.LocalBusOneshotSubscriber;
import com.everhomes.bus.LocalBusOneshotSubscriberBuilder;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.gorder.sdk.order.GeneralOrderService;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.order.*;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.parking.handler.DefaultParkingVendorHandler;
import com.everhomes.parking.vip_parking.DingDingParkingLockHandler;
import com.everhomes.pay.order.*;
import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.portal.PlatformContextNoWarnning;
import com.everhomes.print.SiyinPrintBusinessPayeeAccount;
import com.everhomes.print.SiyinPrintOrder;
import com.everhomes.rentalv2.*;
import com.everhomes.rentalv2.utils.RentalUtils;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.activity.ActivityRosterPayVersionFlag;
import com.everhomes.rest.asset.TargetDTO;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.promotion.order.controller.CreatePurchaseOrderRestResponse;
import com.everhomes.rest.promotion.order.controller.CreateRefundOrderRestResponse;
import com.everhomes.rest.promotion.merchant.GetPayUserByMerchantIdCommand;
import com.everhomes.rest.promotion.merchant.GetPayUserListByMerchantCommand;
import com.everhomes.rest.promotion.merchant.GetPayUserListByMerchantDTO;
import com.everhomes.rest.promotion.merchant.ListPayUsersByMerchantIdsCommand;
import com.everhomes.rest.promotion.merchant.controller.GetMerchantListByPayUserIdRestResponse;
import com.everhomes.rest.promotion.merchant.controller.GetPayAccountByMerchantIdRestResponse;
import com.everhomes.rest.promotion.merchant.controller.GetPayerInfoByMerchantIdRestResponse;
import com.everhomes.rest.promotion.merchant.controller.ListPayUsersByMerchantIdsRestResponse;
import com.everhomes.rest.promotion.order.BusinessPayerType;
import com.everhomes.rest.promotion.order.CreateMerchantOrderResponse;
import com.everhomes.rest.promotion.order.CreatePurchaseOrderCommand;
import com.everhomes.rest.promotion.order.CreateRefundOrderCommand;
import com.everhomes.rest.promotion.order.GoodDTO;
import com.everhomes.rest.promotion.order.MerchantPaymentNotificationCommand;
import com.everhomes.rest.promotion.order.OrderDescriptionEntity;
import com.everhomes.rest.promotion.order.PayerInfoDTO;
import com.everhomes.rest.promotion.order.PurchaseOrderCommandResponse;
import com.everhomes.rest.order.*;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayMethodDTO;
import com.everhomes.rest.order.PaymentParamsDTO;
import com.everhomes.rest.parking.*;
import com.everhomes.rest.pay.controller.CreateOrderRestResponse;
import com.everhomes.rest.pmtask.PmTaskErrorCode;
import com.everhomes.rest.print.PayPrintGeneralOrderCommand;
import com.everhomes.rest.print.PayPrintGeneralOrderResponse;
import com.everhomes.rest.print.PrintErrorCode;
import com.everhomes.rest.print.PrintJobTypeType;
import com.everhomes.rest.rentalv2.*;

import com.everhomes.server.schema.Tables;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.util.*;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.Condition;
import org.jooq.SortField;
import org.jooq.TableField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.*;
import com.everhomes.general.order.GeneralOrderBizHandler;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.general.order.CreateOrderBaseInfo;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.officecubicle.ListOfficeCubicleAccountDTO;
import com.everhomes.rest.organization.VendorType;

import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;


@Component
public class ParkingServiceImpl implements ParkingService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ParkingServiceImpl.class);
	public static final String BIZ_ORDER_NUM_SPILT = "_";//业务订单分隔符
	public static final String BIZ_ACCOUNT_PRE = "NS";//账号前缀

	@Autowired
	private List<ParkingVendorHandler> allListeners;
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
	private OrganizationProvider organizationProvider;
	@Autowired
	private AppProvider appProvider;
	@Autowired
	private LocalBusOneshotSubscriberBuilder localBusSubscriberBuilder;
//	@Autowired
//	private PayService payService;
	@Autowired
	private RentalCommonServiceImpl rentalCommonService;
	@Autowired
	private Rentalv2Provider rentalv2Provider;
	@Autowired
	private DingDingParkingLockHandler dingDingParkingLockHandler;
	@Autowired
	private UserPrivilegeMgr userPrivilegeMgr;
	@Autowired
	private CoordinationProvider coordinationProvider;
	@Autowired
	public CommunityProvider communityProvider;
	@Autowired
	public com.everhomes.paySDK.api.PayService sdkPayService;
	@Autowired
	public ParkingBusinessPayeeAccountProvider parkingBusinessPayeeAccountProvider;
	@Autowired
	public ParkingOrderEmbeddedV2Handler parkingOrderEmbeddedV2Handler;
	@Autowired
	public NamespaceProvider namespaceProvider;
	@Autowired
	private ParkingHubProvider parkingHubProvider;
	@Autowired
	protected GeneralOrderService orderService;
	@Autowired
	private LocaleStringService localeService;
	@Autowired
	private com.everhomes.gorder.sdk.order.GeneralOrderService payServiceV2;
	@Autowired
	private ServiceModuleAppService serviceModuleAppService;
	@Value("${server.contextPath:}")
    private String contextPath;
	@Override
	public List<ParkingCardDTO> listParkingCards(ListParkingCardsCommand cmd) {

		return getParkingCardsByGeneral(cmd, (byte)1);
	}

	@Override
	public List<ParkingCardDTO> getParkingCards(ListParkingCardsCommand cmd) {

		return getParkingCardsByGeneral(cmd, (byte)2);
	}

	private List<ParkingCardDTO> getParkingCardsByGeneral(ListParkingCardsCommand cmd, byte flag) {

		checkPlateNumber(cmd.getPlateNumber());
		Long parkingLotId = cmd.getParkingLotId();
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), parkingLotId);

		String vendorName = parkingLot.getVendorName();
		ParkingVendorHandler handler = getParkingVendorHandler(vendorName);

		List<ParkingCardDTO> cards = handler.listParkingCardsByPlate(parkingLot, cmd.getPlateNumber());

		if (flag == (byte)1) {
			cards = cards.stream().filter(r -> r.getCardStatus() == (ParkingCardStatus.NORMAL.getCode()))
					.collect(Collectors.toList());
		}

		if (!cards.isEmpty()) {
			Long organizationId = cmd.getOrganizationId();
			User user = UserContext.current().getUser();
			Long userId = user.getId();
			String plateOwnerName = user.getNickName();

			if(null != organizationId) {
				OrganizationMember organizationMember = organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, organizationId);
				if(null != organizationMember) {
					plateOwnerName = organizationMember.getContactName();
				}
			}

			for(ParkingCardDTO card: cards) {
				if (ParkingConfigFlag.SUPPORT.getCode() == parkingLot.getMonthlyDiscountFlag()){
					card.setMonthlyDiscount(parkingLot.getMonthlyDiscount());
				}
				if(StringUtils.isBlank(card.getPlateOwnerName())) {
					card.setPlateOwnerName(plateOwnerName);
				}
			}
		}

		return cards;
	}

	public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {

		Long parkingLotId = cmd.getParkingLotId();
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), parkingLotId);

		String vendorName = parkingLot.getVendorName();
		ParkingVendorHandler handler = getParkingVendorHandler(vendorName);

		ListCardTypeResponse response = handler.listCardType(cmd);

		return response;
	}

	@Override
	public List<ParkingRechargeRateDTO> listParkingRechargeRates(ListParkingRechargeRatesCommand cmd){

		Long parkingLotId = cmd.getParkingLotId();
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), parkingLotId);

		String vendorName = parkingLot.getVendorName();
		ParkingVendorHandler handler = getParkingVendorHandler(vendorName);

		List<ParkingRechargeRateDTO> parkingRechargeRateList = handler.getParkingRechargeRates(parkingLot,
				cmd.getPlateNumber(), cmd.getCardNo());

		if (null != parkingLot.getMonthlyDiscountFlag()) {
			if (ParkingConfigFlag.SUPPORT.getCode() == parkingLot.getMonthlyDiscountFlag()) {
				parkingRechargeRateList.forEach(r -> {
					r.setOriginalPrice(r.getPrice());
					BigDecimal newPrice = r.getPrice().multiply(new BigDecimal(parkingLot.getMonthlyDiscount()))
							.divide(new BigDecimal(10), DefaultParkingVendorHandler.CARD_RATE_RETAIN_DECIMAL, RoundingMode.HALF_UP);
					r.setPrice(newPrice);
				});
			}
		}

		return parkingRechargeRateList.stream().filter(r -> r.getPrice().compareTo(new BigDecimal(0)) == 1).collect(Collectors.toList());
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
			LOGGER.error("OwnerId or ownerType cannot be null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"OwnerId or ownerType cannot be null.");
		}
//		User user = UserContext.current().getUser();
		List<ParkingLot> list = parkingProvider.listParkingLots(cmd.getOwnerType(), cmd.getOwnerId());
		ParkingSourceRequestType requestType = ParkingSourceRequestType.fromCode(cmd.getSourceRequestType());

		List<ParkingLotDTO> parkingLotList = list.stream().map(r -> {
			ParkingLotDTO dto = ConvertHelper.convert(r, ParkingLotDTO.class);

			if (r.getVipParkingFlag() == ParkingConfigFlag.SUPPORT.getCode()) {
				String homeUrl = configProvider.getValue(ConfigConstants.HOME_URL, "");
				String detailUrl = configProvider.getValue(ConfigConstants.RENTAL_ORDER_DETAIL_URL, "");

				RentalResourceType type = rentalv2Provider.findRentalResourceTypes(UserContext.getCurrentNamespaceId(),
						RentalV2ResourceType.VIP_PARKING.getCode());

				detailUrl = String.format(detailUrl, RentalV2ResourceType.VIP_PARKING.getCode(), type.getId(),
						RuleSourceType.RESOURCE.getCode(), dto.getId());
				dto.setVipParkingUrl(homeUrl + detailUrl);
			}
			if (r.getDefaultData() != null && r.getDefaultData().length() >0){
				dto.setData(Arrays.asList(r.getDefaultData().split(",")));
			}
			if (r.getDefaultPlate() != null && r.getDefaultPlate().length() >0){
				String[] plate = r.getDefaultPlate().split(",");
				dto.setProvince(plate[0]);
				dto.setCity(plate[1]);
			}
			dto.setFlowId(null);
			dto.setFlowMode(ParkingRequestFlowType.FORBIDDEN.getCode());
			if(ParkingConfigFlag.fromCode(r.getMonthCardFlag()) == ParkingConfigFlag.SUPPORT) {
				Flow flow = flowService.getEnabledFlow(UserContext.getCurrentNamespaceId(), ParkingFlowConstant.PARKING_RECHARGE_MODULE,
						FlowModuleType.NO_MODULE.getCode(), r.getId(), FlowOwnerType.PARKING.getCode());
				//当没有设置工作流的时候，表示是禁用模式
				if (flow!=null){
					//模式由发布的时候决定，这里不根据工作流的名称和stringTag1字段决定。
					// 如果没有工作流或者工作流主版本不匹配，则不启用月卡申请模式
//					String tag1 = flow.getStringTag1();
//					Integer flowMode = Integer.valueOf(tag1);
					dto.setFlowMode(r.getFlowMode());
					dto.setFlowId(flow.getFlowMainId());
					LOGGER.info("parking enabled flow, flow={}", flow);
					Flow mainFlow = flowProvider.getFlowById(flow.getFlowMainId());
					LOGGER.info("parking main flow, flow={}", mainFlow);
					//当获取到的工作流与 main工作流中的版本不一致时，表示获取到的工作流不是编辑后最新的工作流（工作流没有启用），是禁用模式
//					if (null != mainFlow && mainFlow.getFlowVersion().intValue() != flow.getFlowVersion().intValue()) {
//						dto.setFlowMode(ParkingRequestFlowType.FORBIDDEN.getCode());
//						dto.setFlowId(null);
//					}
				}
			}
			if(requestType == ParkingSourceRequestType.BACKGROUND){
				dto.setFlowMode(r.getFlowMode());
			}
			
			if (isReachMonthCardRequestMaxNum(r.getOwnerType(), r.getOwnerId(), r.getId())) {
				dto.setMonthCardMaxRequestFlag(TrueOrFalseFlag.TRUE.getCode());
			}

			return dto;
		}).filter(dto->!(requestType != ParkingSourceRequestType.BACKGROUND && isColseAllFunction(dto))).collect(Collectors.toList());

		return parkingLotList;
	}

	private boolean isColseAllFunction(ParkingLotDTO dto) {
		GetParkingBussnessStatusCommand convert = ConvertHelper.convert(dto, GetParkingBussnessStatusCommand.class);
		convert.setParkingLotId(dto.getId());
		GetParkingBussnessStatusResponse bussnessStatusResponse = getParkingBussnessStatus(convert);
		if(bussnessStatusResponse==null){
			return false;
		}
		for (ParkingFuncDTO funcDTO : bussnessStatusResponse.getDockingFuncLists()) {
			ParkingConfigFlag parkingConfigFlag = ParkingConfigFlag.fromCode(funcDTO.getEnableFlag());
			if(parkingConfigFlag == ParkingConfigFlag.SUPPORT){
				return false;
			}
		}

		for (ParkingFuncDTO funcDTO : bussnessStatusResponse.getFuncLists()) {
			ParkingConfigFlag parkingConfigFlag = ParkingConfigFlag.fromCode(funcDTO.getEnableFlag());
			if(parkingConfigFlag == ParkingConfigFlag.SUPPORT){
				return false;
			}
		}
		ParkingConfigFlag enableMonthCard = ParkingConfigFlag.fromCode(bussnessStatusResponse.getEnableMonthCard());
		Byte monthCardFlow = bussnessStatusResponse.getMonthCardFlow();
		if(monthCardFlow!=null) {
			ParkingRequestFlowType flowType = ParkingRequestFlowType.fromCode(Integer.valueOf(monthCardFlow));
			if (enableMonthCard == ParkingConfigFlag.SUPPORT && flowType != ParkingRequestFlowType.FORBIDDEN) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ParkingCardRequestDTO requestParkingCard(RequestParkingCardCommand cmd) {

		checkPlateNumber(cmd.getPlateNumber());
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		String vendor = parkingLot.getVendorName();
		ParkingVendorHandler handler = getParkingVendorHandler(vendor);

		List<ParkingCardDTO> cards = handler.listParkingCardsByPlate(parkingLot, cmd.getPlateNumber());
		User user = UserContext.current().getUser();
		int cardListSize = cards.size();
		if(cardListSize > 0){
			LOGGER.error("PlateNumber card is existed, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_PLATE_EXIST,
					"PlateNumber card is existed");
		}

		String ownerType = FlowOwnerType.PARKING.getCode();
		Flow flow = flowService.getEnabledFlow(user.getNamespaceId(), ParkingFlowConstant.PARKING_RECHARGE_MODULE,
				FlowModuleType.NO_MODULE.getCode(), parkingLot.getId(), ownerType);
		if (flow == null) {
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE_CLEARANCE, ParkingErrorCode.ERROR_NO_WORK_FLOW_ENABLED,
					"请启用工作流");
		}
		Long flowId = flow.getFlowMainId();
		Integer requestFlowType = parkingLot.getFlowMode();

		if(cardListSize == 0){
			//当查询的月卡信息不存在时，检查申请条件
			checkRequestCondition(cmd, flowId);
		}
		ParkingCardRequest parkingCardRequest = buildParkingCardRequest(cmd, Integer.valueOf(requestFlowType));
		ListCardTypeResponse listCardTypeResponse = handler.listCardType(ConvertHelper.convert(cmd,ListCardTypeCommand.class));
		if(listCardTypeResponse!=null&&listCardTypeResponse.getCardTypes()!=null){
			for (ParkingCardType parkingCardType : listCardTypeResponse.getCardTypes()) {
				if(parkingCardType!=null
						&&parkingCardRequest!=null
						&&parkingCardRequest.getCardTypeId()!=null
						&&parkingCardRequest.getCardTypeId().equals(parkingCardType.getTypeId())){
					parkingCardRequest.setCardTypeName(parkingCardType.getTypeName());
					break;
				}
			}
		}

		dbProvider.execute((TransactionStatus status) -> {

			parkingProvider.requestParkingCard(parkingCardRequest);

			addAttachments(cmd.getAttachments(), user.getId(), parkingCardRequest.getId(), ParkingAttachmentType.PARKING_CARD_REQUEST.getCode());

			FlowCase flowCase = createFlowCase(parkingCardRequest, flow, user.getId());

			parkingCardRequest.setFlowId(flowId);
			parkingCardRequest.setFlowCaseId(flowCase.getId());
			parkingProvider.updateParkingCardRequest(parkingCardRequest);

			createParkingUserInvoice(cmd.getInvoiceType(), parkingLot, user);

			return null;
		});
		ParkingCardRequestDTO parkingCardRequestDTO = ConvertHelper.convert(parkingCardRequest, ParkingCardRequestDTO.class);

		Integer count = parkingProvider.waitingCardCount(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getParkingLotId(), parkingCardRequest.getCreateTime());
		parkingCardRequestDTO.setRanking(count);

		return parkingCardRequestDTO;

	}

	private FlowCase createFlowCase(ParkingCardRequest parkingCardRequest, Flow flow, Long userId) {

		CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
		createFlowCaseCommand.setApplyUserId(userId);
		createFlowCaseCommand.setFlowMainId(flow.getFlowMainId());
		createFlowCaseCommand.setFlowVersion(flow.getFlowVersion());
		createFlowCaseCommand.setReferId(parkingCardRequest.getId());
		createFlowCaseCommand.setReferType(EntityType.PARKING_CARD_REQUEST.getCode());
		createFlowCaseCommand.setContent("车牌号码：" + parkingCardRequest.getPlateNumber() + "\n"
				+ "车主电话：" + parkingCardRequest.getPlateOwnerPhone());
		createFlowCaseCommand.setCurrentOrganizationId(parkingCardRequest.getRequestorEnterpriseId());

		if (UserContext.getCurrentNamespaceId().equals(999983)) {
			createFlowCaseCommand.setTitle("停车月卡申请");
		}
		createFlowCaseCommand.setServiceType("停车月卡申请");
		FlowCase flowCase = flowService.createFlowCase(createFlowCaseCommand);

		return flowCase;
	}

	private ParkingCardRequest buildParkingCardRequest(RequestParkingCardCommand cmd, Integer requestFlowType) {

		Long userId = UserContext.currentUserId();

		ParkingCardRequest parkingCardRequest = ConvertHelper.convert(cmd, ParkingCardRequest.class);

		parkingCardRequest.setRequestorUid(userId);
		parkingCardRequest.setCreatorUid(userId);
		parkingCardRequest.setCreateTime(new Timestamp(System.currentTimeMillis()));
		//设置一些初始状态
		parkingCardRequest.setIssueFlag(ParkingCardIssueFlag.UNISSUED.getCode());
		if(ParkingRequestFlowType.QUEQUE.getCode().equals(requestFlowType)) {
			parkingCardRequest.setStatus(ParkingCardRequestStatus.QUEUEING.getCode());
		}else {
			parkingCardRequest.setStatus(ParkingCardRequestStatus.AUDITING.getCode());
		}
		//当车系id不为空时，表示不是手填车系和品牌
		if(null != cmd.getCarSerieId()) {
			ParkingCarSerie carSerie = parkingProvider.findParkingCarSerie(cmd.getCarSerieId());
			if(null != carSerie) {
				ParkingCarSerie parentCarSerie = parkingProvider.findParkingCarSerie(carSerie.getParentId());
				if (null != parentCarSerie) {
					ParkingCarSerie carBrand = parkingProvider.findParkingCarSerie(parentCarSerie.getParentId());
					if(null != carBrand) {
						parkingCardRequest.setCarSerieName(carSerie.getName());
						parkingCardRequest.setCarBrand(carBrand.getName());
					}
				}
			}
		}

		return parkingCardRequest;
	}

	private void checkRequestCondition(RequestParkingCardCommand cmd, Long flowId) {

		Long userId = UserContext.currentUserId();

		ParkingFlow parkingFlow = parkingProvider.getParkingRequestCardConfig(cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getParkingLotId(), flowId);

		List<ParkingCardRequest> requestList = parkingProvider.listParkingCardRequests(userId, cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getParkingLotId(), null, null,
				ParkingCardRequestStatus.INACTIVE.getCode(), flowId, null, null);

		int requestListSize = requestList.size();
		if(null != parkingFlow && parkingFlow.getMaxRequestNumFlag() == ParkingConfigFlag.SUPPORT.getCode()
				&& requestListSize >= parkingFlow.getMaxRequestNum()){
			LOGGER.error("The card request is rather than max request num, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_MAX_REQUEST_NUM,
					"The card request is rather than max request num.");
		}

		requestList = parkingProvider.listParkingCardRequests(userId, cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(), null,
				ParkingCardRequestStatus.INACTIVE.getCode(), flowId, null, null);

		requestListSize = requestList.size();
		if(requestListSize > 0){
			LOGGER.error("PlateNumber is already applied, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_PLATE_APPLIED,
					"plateNumber is already applied.");
		}
	}

	public ParkingCardRequestDTO getRequestParkingCardDetail(GetRequestParkingCardDetailCommand cmd) {

		ParkingCardRequest parkingCardRequest = parkingProvider.findParkingCardRequestById(cmd.getId());

		ParkingCardRequestDTO dto = ConvertHelper.convert(parkingCardRequest, ParkingCardRequestDTO.class);

		List<ParkingAttachment> attachments = parkingProvider.listParkingAttachments(parkingCardRequest.getId(),
				ParkingAttachmentType.PARKING_CARD_REQUEST.getCode());

		List<ParkingAttachmentDTO> attachmentDTOs =  attachments.stream().map(r -> {
			ParkingAttachmentDTO attachmentDto = ConvertHelper.convert(r, ParkingAttachmentDTO.class);

			String contentUrl = getResourceUrlByUir(r.getContentUri(),
					EntityType.USER.getCode(), r.getCreatorUid());
			attachmentDto.setContentUrl(contentUrl);
			attachmentDto.setInformationType(r.getDataType());
			return attachmentDto;
		}).collect(Collectors.toList());

		dto.setAttachments(attachmentDTOs);

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
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
			//应用管理权限校验
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), PrivilegeConstants.PARKING_APPLY_MANAGERMENT, cmd.getAppId(), null,cmd.getCurrentProjectId());//月卡申请权限
		}
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
	public PreOrderDTO createParkingRechargeOrderV2(CreateParkingRechargeOrderCommand cmd){

		if(null == cmd.getMonthCount() || cmd.getMonthCount() ==0) {
			LOGGER.error("Invalid MonthCount, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_LOSE,
					"Invalid MonthCount.");
		}
		cmd.setPlateNumber(cmd.getPlateNumber().toUpperCase());
		return (PreOrderDTO) createGeneralOrder(cmd, ParkingRechargeType.MONTHLY.getCode(), ActivityRosterPayVersionFlag.V2);
	}

	@Override
	public CreateParkingGeneralOrderResponse createParkingRechargeGeneralOrder(CreateParkingRechargeOrderCommand cmd){

		if(null == cmd.getMonthCount() || cmd.getMonthCount() ==0) {
			LOGGER.error("Invalid MonthCount, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Invalid MonthCount.");
		}
		cmd.setPlateNumber(cmd.getPlateNumber().toUpperCase());
		return (CreateParkingGeneralOrderResponse) createGeneralOrder(cmd, ParkingRechargeType.MONTHLY.getCode(), ActivityRosterPayVersionFlag.V3);
	}
	
	@Override
	public PreOrderDTO createParkingTempOrderV2(CreateParkingTempOrderCommand cmd) {
		checkOrderToken(cmd.getOrderToken());
		CreateParkingRechargeOrderCommand param = new CreateParkingRechargeOrderCommand();
		param.setOwnerType(cmd.getOwnerType());
		param.setOwnerId(cmd.getOwnerId());
		param.setParkingLotId(cmd.getParkingLotId());
		param.setPlateNumber(cmd.getPlateNumber().toUpperCase());
		param.setPayerEnterpriseId(cmd.getPayerEnterpriseId());
		param.setPrice(cmd.getPrice());
		param.setClientAppName(cmd.getClientAppName());//todoed
		param.setPaymentType(cmd.getPaymentType());

		return (PreOrderDTO) createGeneralOrder(param, ParkingRechargeType.TEMPORARY.getCode(), ActivityRosterPayVersionFlag.V2);

	}

	@Override
	public CreateParkingGeneralOrderResponse createParkingTempGeneralOrder(CreateParkingTempOrderCommand cmd) {
		checkOrderToken(cmd.getOrderToken());
		CreateParkingRechargeGeneralOrderCommand param = new CreateParkingRechargeGeneralOrderCommand();
		param.setOwnerType(cmd.getOwnerType());
		param.setOwnerId(cmd.getOwnerId());
		param.setParkingLotId(cmd.getParkingLotId());
		param.setPlateNumber(cmd.getPlateNumber().toUpperCase());
		param.setPayerEnterpriseId(cmd.getPayerEnterpriseId());
		param.setPrice(cmd.getPrice());
		param.setClientAppName(cmd.getClientAppName());
		return (CreateParkingGeneralOrderResponse) createGeneralOrder(param, ParkingRechargeType.TEMPORARY.getCode(), ActivityRosterPayVersionFlag.V3);

	}
	@Override
	public CommonOrderDTO createParkingRechargeOrder(CreateParkingRechargeOrderCommand cmd){

		if(null == cmd.getMonthCount() || cmd.getMonthCount() ==0) {
			LOGGER.error("Invalid MonthCount, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_LOSE,
					"Invalid MonthCount.");
		}

		return (CommonOrderDTO) createGeneralOrder(cmd, ParkingRechargeType.MONTHLY.getCode(), ActivityRosterPayVersionFlag.V1);
	}

	@Override
	public CommonOrderDTO createParkingTempOrder(CreateParkingTempOrderCommand cmd) {
		checkOrderToken(cmd.getOrderToken());
		CreateParkingRechargeOrderCommand param = new CreateParkingRechargeOrderCommand();
		param.setOwnerType(cmd.getOwnerType());
		param.setOwnerId(cmd.getOwnerId());
		param.setParkingLotId(cmd.getParkingLotId());
		param.setPlateNumber(cmd.getPlateNumber().toUpperCase());
		param.setPayerEnterpriseId(cmd.getPayerEnterpriseId());
		param.setPrice(cmd.getPrice());
		param.setClientAppName(cmd.getClientAppName());

		return (CommonOrderDTO) createGeneralOrder(param, ParkingRechargeType.TEMPORARY.getCode(), ActivityRosterPayVersionFlag.V1);

	}

	private Object createGeneralOrder(CreateParkingRechargeOrderCommand cmd, Byte rechargeType, ActivityRosterPayVersionFlag version) {
		String plateNumber = cmd.getPlateNumber().trim();
		checkPlateNumber(plateNumber);
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		String vendor = parkingLot.getVendorName();
		ParkingVendorHandler handler = getParkingVendorHandler(vendor);

		ParkingRechargeOrder parkingRechargeOrder = new ParkingRechargeOrder();

		User user = UserContext.current().getUser();
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());

		OrganizationMember organizationMember = organizationProvider.findOrganizationMemberByUIdAndOrgId(user.getId(), cmd.getPayerEnterpriseId());
		if(null != organizationMember) {
			if(null == cmd.getPlateOwnerName())
				cmd.setPlateOwnerName(organizationMember.getContactName());
		}

		parkingRechargeOrder.setOrderType(null != cmd.getOrderType() ? cmd.getOrderType() : ParkingOrderType.RECHARGE.getCode());
		parkingRechargeOrder.setRechargeType(rechargeType);
		parkingRechargeOrder.setOwnerType(cmd.getOwnerType());
		parkingRechargeOrder.setOwnerId(cmd.getOwnerId());
		parkingRechargeOrder.setParkingLotId(parkingLot.getId());
		parkingRechargeOrder.setPlateNumber(plateNumber);
		parkingRechargeOrder.setPlateOwnerName(null != cmd.getPlateOwnerName()?cmd.getPlateOwnerName():user.getNickName());
		parkingRechargeOrder.setPlateOwnerPhone(cmd.getPlateOwnerPhone());

		parkingRechargeOrder.setPayerEnterpriseId(cmd.getPayerEnterpriseId());
		parkingRechargeOrder.setPayerUid(user.getId());
		parkingRechargeOrder.setPayerPhone(userIdentifier==null?null:userIdentifier.getIdentifierToken());
		parkingRechargeOrder.setCreatorUid(user.getId());
		Long now = System.currentTimeMillis();
		parkingRechargeOrder.setCreateTime(new Timestamp(now));
		parkingRechargeOrder.setRechargeTime(new Timestamp(now));

		parkingRechargeOrder.setVendorName(parkingLot.getVendorName());
		parkingRechargeOrder.setCardNumber(cmd.getCardNumber());

		parkingRechargeOrder.setStatus(ParkingRechargeOrderStatus.UNPAID.getCode());

		parkingRechargeOrder.setOrderNo(createUnPaidOrderNo());
		parkingRechargeOrder.setPaidVersion(version.getCode());
		parkingRechargeOrder.setInvoiceType(cmd.getInvoiceType());
		if(rechargeType.equals(ParkingRechargeType.TEMPORARY.getCode())) {
			ParkingTempFeeDTO dto = handler.getParkingTempFee(parkingLot, plateNumber);

			if (null == dto || null == dto.getPrice()) {
				LOGGER.error("Parking request temp fee failed, cmd={}", cmd);
				throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_REQUEST_SERVER,
						"Parking request temp fee failed");
			}

			BigDecimal tempFee = dto.getPrice();
			if (null != parkingLot.getTempFeeDiscountFlag()) {
				if (ParkingConfigFlag.SUPPORT.getCode() == parkingLot.getTempFeeDiscountFlag()) {
					tempFee = dto.getPrice().multiply(new BigDecimal(parkingLot.getTempFeeDiscount()))
							.divide(new BigDecimal(10), DefaultParkingVendorHandler.TEMP_FEE_RETAIN_DECIMAL, RoundingMode.HALF_UP);
				}
			}
			if(0 != tempFee.compareTo(cmd.getPrice())) {
				LOGGER.error("Overdue fees, cmd={}", cmd);
				throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_TEMP_FEE,
						"Overdue fees");
			}

			parkingRechargeOrder.setOriginalPrice(dto.getPrice());
			parkingRechargeOrder.setPrice(cmd.getPrice());
			parkingRechargeOrder.setOrderToken(dto.getOrderToken());
			parkingRechargeOrder.setParkingTime(dto.getParkingTime());
			parkingRechargeOrder.setStartPeriod(new Timestamp(dto.getEntryTime()));
			parkingRechargeOrder.setEndPeriod(new Timestamp(dto.getPayTime()));
			parkingRechargeOrder.setDelayTime(dto.getDelayTime());
		}else if(rechargeType.equals(ParkingRechargeType.MONTHLY.getCode())) {
			//查询rate
			parkingRechargeOrder.setRateToken(cmd.getRateToken());
			parkingRechargeOrder.setMonthCount(new BigDecimal(cmd.getMonthCount()));
			//先设置客户端传进来的价格，在updateParkingRechargeOrderRate方法中校验价格,根据费率设置originalPrice
			parkingRechargeOrder.setPrice(cmd.getPrice());

			if (parkingRechargeOrder.getOrderType() == ParkingOrderType.RECHARGE.getCode()) {
				
				handler.updateParkingRechargeOrderRate(parkingLot, parkingRechargeOrder);

			}else {
				//TODO:开卡校验
				parkingRechargeOrder.setOriginalPrice(cmd.getPrice());
			}
		}

		dbProvider.execute(status -> {
			parkingProvider.createParkingRechargeOrder(parkingRechargeOrder);

			createParkingUserInvoice(cmd.getInvoiceType(), parkingLot, user);

			return null;
		});
		switch (version){
		case V1:
			parkingRechargeOrder.setInvoiceStatus((byte)0);
			parkingRechargeOrder.setPaySource(ParkingPaySourceType.APP.getCode());
			parkingProvider.updateParkingRechargeOrder(parkingRechargeOrder);
			return convertOrderDTOForV1(parkingRechargeOrder, rechargeType);
		case V2:
			return convertOrderDTOForV2(parkingRechargeOrder, cmd.getClientAppName(),parkingLot,cmd.getPaymentType());
		case V3:
			return convertOrderDTOForV3(parkingRechargeOrder, cmd.getClientAppName(),parkingLot,cmd.getPaymentType(),cmd.getPayerEnterpriseId());
		default:
			return null;
		}
//		if (ActivityRosterPayVersionFlag.V1 == version) {
//			parkingRechargeOrder.setInvoiceStatus((byte)0);
//			parkingRechargeOrder.setPaySource(ParkingPaySourceType.APP.getCode());
//			parkingProvider.updateParkingRechargeOrder(parkingRechargeOrder);
//			return convertOrderDTOForV1(parkingRechargeOrder, rechargeType);
//		}else if (ActivityRosterPayVersionFlag.V2 == version){
//			return convertOrderDTOForV2(parkingRechargeOrder, cmd.getClientAppName(),parkingLot,cmd.getPaymentType());
//		}
	}

	private CreateParkingGeneralOrderResponse convertOrderDTOForV3(ParkingRechargeOrder order, String clientAppName, ParkingLot parkingLot, Integer paymentType, Long organizationId){
		ParkingRechargeType rechargeType = ParkingRechargeType.fromCode(order.getRechargeType());
		String returnUrl = null;
		ParkingBusinessType bussinessType = null;
		String extendInfo = null;
		if(rechargeType == ParkingRechargeType.MONTHLY){
			bussinessType = ParkingBusinessType.MONTH_RECHARGE;
			returnUrl = String.format(configProvider.getValue("parking.recharge.return.url","zl://parking/monthCardRechargeStatus?orderId=%s"), String.valueOf(order.getId()));
			extendInfo = parkingLot.getName()+"（月卡车：" + order.getPlateNumber() + "）";
		}else if(rechargeType == ParkingRechargeType.TEMPORARY){
			bussinessType = ParkingBusinessType.TEMPFEE;
			returnUrl = String.format(configProvider.getValue("parking.tempfee.return.url","zl://parking/tempFeeStatus?orderId=%s"), String.valueOf(order.getId()));	
			extendInfo = parkingLot.getName()+"（月卡车：" + order.getPlateNumber() + "）";
		}
		
		try {
			returnUrl =URLEncoder.encode(returnUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//收款方是否有会员，无则报错
		List<ParkingBusinessPayeeAccount> payeeAccounts = parkingBusinessPayeeAccountProvider.findRepeatParkingBusinessPayeeAccounts(null, UserContext.getCurrentNamespaceId(),
				parkingLot.getOwnerType(), parkingLot.getOwnerId(), parkingLot.getId(),bussinessType.getCode());
		if(payeeAccounts==null || payeeAccounts.size()==0){
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_NO_PAYEE_ACCOUNT,
					"");
		}
		ListPayUsersByMerchantIdsCommand cmd2 = new ListPayUsersByMerchantIdsCommand();
		Long bizPayeeId = payeeAccounts.get(0).getMerchantId();
		cmd2.setIds(Arrays.asList(bizPayeeId));
		ListPayUsersByMerchantIdsRestResponse resp = payServiceV2.listPayUsersByMerchantIds(cmd2);
		if(null == resp || null == resp.getResponse()) {
			LOGGER.error("resp:"+(null == resp ? null :StringHelper.toJsonString(resp)));
		}
		List<PayUserDTO> payUserDTOs = resp.getResponse();
        if (payUserDTOs == null || payUserDTOs.size() == 0){
            LOGGER.error("payeeUserId no find, order={}", order);
            throw RuntimeErrorException.errorWith(PrintErrorCode.SCOPE, PrintErrorCode.ERROR_PAYEE_ACCOUNT_NOT_CONFIG,
                    "暂未绑定收款账户");
        }
        
        CreateOrderBaseInfo baseInfo = new CreateOrderBaseInfo();
		String backUrl = configProvider.getValue(UserContext.getCurrentNamespaceId(),"parking.pay.callBackUrl", "/parking/notifyParkingRechargeOrderPaymentV2"); 
        baseInfo.setAppOriginId(getAppOriginId());
        baseInfo.setCallBackUrl(backUrl);
        baseInfo.setClientAppName(clientAppName);
        baseInfo.setOrderTitle("停车订单");
        baseInfo.setOwnerId(order.getOwnerId());
        baseInfo.setPaymentMerchantId(bizPayeeId);
        baseInfo.setPaySourceType(SourceType.MOBILE.getCode());
        baseInfo.setTotalAmount(order.getPrice());
        baseInfo.setGoods(buildGoods(parkingLot, order, bizPayeeId, bussinessType));
        baseInfo.setGoodsDetail(buildGoodsDetails(parkingLot, order, bussinessType));
        baseInfo.setReturnUrl(returnUrl);
        baseInfo.setOrganizationId(organizationId);
        baseInfo.setExtendInfo(extendInfo);
        
		CreateMerchantOrderResponse generalOrderResp = getParkingGeneralOrderHandler().createOrder(baseInfo);
        order.setGeneralOrderId(generalOrderResp.getMerchantOrderId()+"");
		String paySource = ParkingPaySourceType.APP.getCode();
		if (paymentType != null && paymentType == PaymentType.WECHAT_SCAN_PAY.getCode()) {
			paySource = ParkingPaySourceType.QRCODE.getCode();
//			createOrderCommand.setPayerUserId(configProvider.getLongValue("parking.order.defaultpayer",1041));
		} else if(paymentType != null && paymentType == PaymentType.WECHAT_JS_PAY.getCode()) {
			paySource = ParkingPaySourceType.PUBLICACCOUNT.getCode();
		}
		order.setPaySource(paySource);
        parkingProvider.updateParkingRechargeOrder(order);

        
        CreateParkingGeneralOrderResponse resp2 = new CreateParkingGeneralOrderResponse();
		resp2.setOrderId(generalOrderResp.getMerchantOrderId());
		resp2.setMerchantId(generalOrderResp.getMerchantId());
		resp2.setPayUrl(generalOrderResp.getPayUrl());
		return resp2;
		
	}
	
	private List<GoodDTO> buildGoods(ParkingLot parkingLot, ParkingRechargeOrder order, Long merchantId,ParkingBusinessType bussinessType) {
		List<GoodDTO> goods = new ArrayList<>();
		GoodDTO good = new GoodDTO();
		good.setNamespace("NS");
		good.setTag1(order.getOwnerId() + "");
		good.setTag2(String.valueOf(parkingLot.getId()));
		Community community = communityProvider.findCommunityById(parkingLot.getOwnerId());
		if (null != community) {
			good.setServeApplyName(community.getName()); //
		}
		good.setServeType(ServiceModuleConstants.PARKING_MODULE+"");
		good.setServeApplyName("停车缴费");
		good.setGoodName(bussinessType.getDesc());
		good.setGoodDescription(order.getPlateNumber());// 商品描述
		good.setCounts(1);
		good.setPrice(order.getPrice());
		good.setTotalPrice(order.getPrice());
		goods.add(good);
		return goods;
	}
	private List<OrderDescriptionEntity> buildGoodsDetails(ParkingLot parkingLot, ParkingRechargeOrder order, ParkingBusinessType bussinessType) {

		// 设置订单展示
		List<OrderDescriptionEntity> goodsDetail = new ArrayList<>();
		OrderDescriptionEntity e = new OrderDescriptionEntity();
		Community community = communityProvider.findCommunityById(parkingLot.getOwnerId());
		e.setKey("项目名称");
		e.setValue(community.getName());
		goodsDetail.add(e);

		e = new OrderDescriptionEntity();
		e.setKey("停车场名");
		e.setValue(parkingLot.getName());
		goodsDetail.add(e);

		e = new OrderDescriptionEntity();
		e.setKey("车牌号码");
		e.setValue(order.getPlateNumber());
		goodsDetail.add(e);
		
		ParkingRechargeType rechargeType = ParkingRechargeType.fromCode(order.getRechargeType());
		if(rechargeType == ParkingRechargeType.MONTHLY){
			ParkingVendorHandler handler = getParkingVendorHandler(parkingLot.getVendorName());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String sdate = null;
			if (order.getOrderType().equals(ParkingOrderType.RECHARGE.getCode())){
				List<ParkingCardDTO> cards = handler.listParkingCardsByPlate(parkingLot, order.getPlateNumber());
				sdate = sdf.format(cards.get(0).getEndTime());
			} else if(order.getOrderType().equals(ParkingOrderType.OPEN_CARD.getCode())) {
				ParkingCardRequest cardRequest = parkingProvider.findParkingCardRequestByPlateNumber( order.getPlateNumber());
				GetOpenCardInfoCommand cmd = new GetOpenCardInfoCommand();
				cmd.setOwnerId(order.getOwnerId());
				cmd.setOwnerType(order.getOwnerType());
				cmd.setParkingLotId(parkingLot.getId());
				cmd.setParkingRequestId(cardRequest.getId());
				cmd.setPlateNumber(order.getPlateNumber());
				OpenCardInfoDTO cardDTO = handler.getOpenCardInfo(cmd);
				sdate = sdf.format(cardDTO.getExpireDate());
			}
			e = new OrderDescriptionEntity();
			e.setKey("当前有效期");
			e.setValue(sdate);
			goodsDetail.add(e);
			
			e = new OrderDescriptionEntity();
			e.setKey("充值月数");
			e.setValue(String.valueOf(order.getMonthCount()));
			goodsDetail.add(e);
		} else if (rechargeType == ParkingRechargeType.TEMPORARY){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			e = new OrderDescriptionEntity();
			String startDate = sdf.format(order.getStartPeriod());
			e.setKey("入场时间");
			e.setValue(startDate);
			goodsDetail.add(e);
			
			e = new OrderDescriptionEntity();
			String endDate = sdf.format(order.getEndPeriod());
			e.setKey("查询时间");
			e.setValue(endDate);
			goodsDetail.add(e);
			
			e = new OrderDescriptionEntity();
			e.setKey("停车时长");
			e.setValue(String.valueOf(order.getParkingTime()));
			goodsDetail.add(e);
		}
		
		e = new OrderDescriptionEntity();
		e.setKey("订单金额");
		e.setValue(String.valueOf(order.getPrice()));
		goodsDetail.add(e);
		return goodsDetail;
	}
	private Long getAppOriginId() {
		List<ServiceModuleApp> apps = serviceModuleAppService.listReleaseServiceModuleApp(
				UserContext.getCurrentNamespaceId(), 
				ServiceModuleConstants.PARKING_MODULE, 
				null, null, null);
		
		return apps.get(0).getOriginId();
	}
	
	private GeneralOrderBizHandler getParkingGeneralOrderHandler() {
		return PlatformContextNoWarnning.getComponent(GeneralOrderBizHandler.GENERAL_ORDER_HANDLER + OrderType.OrderTypeEnum.PARKING.getPycode());
	}
	
	private PreOrderDTO convertOrderDTOForV2(ParkingRechargeOrder parkingRechargeOrder, String clientAppName, ParkingLot parkingLot, Integer paymentType) {
		Long amount = parkingRechargeOrder.getPrice().multiply(new BigDecimal(100)).longValue();
		boolean flag = configProvider.getBooleanValue("parking.order.amount", false);
		if(flag) {
			amount = 1L;
		}
		String extendInfo = null;
//		LOGGER.info("createAppPreOrder clientAppName={}", clientAppName);
//		PreOrderDTO callBack = payService.createAppPreOrder(UserContext.getCurrentNamespaceId(), clientAppName, OrderType.OrderTypeEnum.PARKING.getPycode(),
//				parkingRechargeOrder.getId(), parkingRechargeOrder.getPayerUid(), amount,null, null, null,extendInfo);
		ParkingRechargeType rechargeType = ParkingRechargeType.fromCode(parkingRechargeOrder.getRechargeType());
		ParkingBusinessType bussinessType = null;
		if(rechargeType == ParkingRechargeType.MONTHLY){
			bussinessType = ParkingBusinessType.MONTH_RECHARGE;
			if(parkingLot!=null){
				Community community = communityProvider.findCommunityById(parkingLot.getOwnerId());
				if(community!=null){
					//$停车场名称$
					extendInfo = parkingLot.getName()+"（月卡车：" + parkingRechargeOrder.getPlateNumber() + "）";
				}
			}
		}else if(rechargeType == ParkingRechargeType.TEMPORARY){
			bussinessType = ParkingBusinessType.TEMPFEE;
			if(parkingLot!=null){
				Community community = communityProvider.findCommunityById(parkingLot.getOwnerId());
				if(community!=null){
					//$停车场名称$
					extendInfo = parkingLot.getName()+"（临时车：" + parkingRechargeOrder.getPlateNumber() + "）";
				}
			}
		}
		User user = UserContext.current().getUser();
		String sNamespaceId = BIZ_ACCOUNT_PRE+UserContext.getCurrentNamespaceId();		//todoed
//		String sNamespaceId = BIZ_ACCOUNT_PRE+"999957";		//todoed
		TargetDTO userTarget = userProvider.findUserTargetById(user.getId());
		CreateOrderCommand createOrderCommand = new CreateOrderCommand();
		//公众号支付
		String paySource = ParkingPaySourceType.APP.getCode();
		if (paymentType != null && paymentType == PaymentType.WECHAT_SCAN_PAY.getCode()) {
			paySource = ParkingPaySourceType.QRCODE.getCode();
//			createOrderCommand.setPayerUserId(configProvider.getLongValue("parking.order.defaultpayer",1041));
		} else if(paymentType != null && paymentType == PaymentType.WECHAT_JS_PAY.getCode()) {
			paySource = ParkingPaySourceType.PUBLICACCOUNT.getCode();
		}

//		ListBizPayeeAccountDTO payerDto = parkingProvider.createPersonalPayUserIfAbsent(user.getId() + "",
//				sNamespaceId, (userTarget==null||userTarget.getUserIdentifier()==null)?"12000001802":userTarget.getUserIdentifier(), null, null, null);
//		createOrderCommand.setPayerUserId(payerDto.getAccountId());
		//保存支付方id
		List<ParkingBusinessPayeeAccount> payeeAccounts = parkingBusinessPayeeAccountProvider.findRepeatParkingBusinessPayeeAccounts(null, UserContext.getCurrentNamespaceId(),
				parkingLot.getOwnerType(), parkingLot.getOwnerId(), parkingLot.getId(),bussinessType.getCode());
		if(payeeAccounts==null || payeeAccounts.size()==0){
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_NO_PAYEE_ACCOUNT,
					"");
		}
		//根据merchantId获取payeeId
		GetPayUserByMerchantIdCommand getPayUserByMerchantIdCommand = new GetPayUserByMerchantIdCommand();
		getPayUserByMerchantIdCommand.setMerchantId(payeeAccounts.get(0).getPayeeId());
		GetPayerInfoByMerchantIdRestResponse getPayerInfoByMerchantIdRestResponse = orderService.getPayerInfoByMerchantId(getPayUserByMerchantIdCommand);
		createOrderCommand.setAccountCode(sNamespaceId);
		createOrderCommand.setBizOrderNum(generateBizOrderNum(sNamespaceId,OrderType.OrderTypeEnum.PARKING.getPycode(),parkingRechargeOrder.getId()));
		createOrderCommand.setClientAppName(clientAppName);//todoed

		createOrderCommand.setPayeeUserId(getPayerInfoByMerchantIdRestResponse.getResponse().getId());
		
		parkingRechargeOrder.setPayeeId(createOrderCommand.getPayeeUserId());
		parkingRechargeOrder.setInvoiceStatus((byte)0);
		parkingRechargeOrder.setPaySource(paySource);

		createOrderCommand.setAmount(amount);
		createOrderCommand.setExtendInfo(extendInfo);
		createOrderCommand.setGoodsName(extendInfo);
		createOrderCommand.setSourceType(1);//下单源，参考com.everhomes.pay.order.SourceType，0-表示手机下单，1表示电脑PC下单
        String homeurl = configProvider.getValue(UserContext.getCurrentNamespaceId(),"home.url", "");
//		String homeurl = "http://10.1.110.79:8080";
		String callbackurl = homeurl + contextPath + configProvider.getValue(UserContext.getCurrentNamespaceId(),"parking.pay.callBackUrl", "/parking/notifyParkingRechargeOrderPaymentV2");
		createOrderCommand.setBackUrl(callbackurl);
		//公众号支付
		if (paymentType != null && paymentType == PaymentType.WECHAT_JS_PAY.getCode()) {
			createOrderCommand.setPaymentType(PaymentType.WECHAT_JS_ORG_PAY.getCode());
			Map<String, String> flattenMap = new HashMap<>();
			flattenMap.put("acct",user.getNamespaceUserToken());
//			String vspCusid = configProvider.getValue(UserContext.getCurrentNamespaceId(), "tempVspCusid", "550584053111NAJ");
//			flattenMap.put("vspCusid",vspCusid);
			flattenMap.put("payType","no_credit");
			createOrderCommand.setPaymentParams(flattenMap);
			createOrderCommand.setCommitFlag(1);
			createOrderCommand.setOrderType(3);
			createOrderCommand.setAccountCode(configProvider.getValue(UserContext.getCurrentNamespaceId(),"parking.wx.subnumberpay","NS"+UserContext.getCurrentNamespaceId()));
		} else if (paymentType != null && paymentType == PaymentType.ALI_JS_PAY.getCode()) {
			createOrderCommand.setPaymentType(PaymentType.ALI_JS_PAY.getCode());
			Map<String, String> flattenMap = new HashMap<>();
			flattenMap.put("acct",user.getNamespaceUserToken());
			//flattenMap.put("acct","11");
//			String vspCusid = configProvider.getValue(UserContext.getCurrentNamespaceId(), "tempVspCusid", "550584053111NAJ");
//			flattenMap.put("vspCusid",vspCusid);
			flattenMap.put("payType","no_credit");
			createOrderCommand.setPaymentParams(flattenMap);
			createOrderCommand.setCommitFlag(1);
			createOrderCommand.setOrderType(3);
			createOrderCommand.setAccountCode(configProvider.getValue(UserContext.getCurrentNamespaceId(),"parking.wx.subnumberpay","NS"+UserContext.getCurrentNamespaceId()));
		} else if (paymentType != null && paymentType == PaymentType.WECHAT_SCAN_PAY.getCode()){
			createOrderCommand.setPaymentType(PaymentType.WECHAT_JS_ORG_PAY.getCode());
			Map<String, String> flattenMap = new HashMap<>();
			flattenMap.put("acct",user.getNamespaceUserToken());
//			String vspCusid = configProvider.getValue(UserContext.getCurrentNamespaceId(), "tempVspCusid", "550584053111NAJ");
//			flattenMap.put("vspCusid",vspCusid);
			flattenMap.put("payType","no_credit");
			createOrderCommand.setPaymentParams(flattenMap);
			createOrderCommand.setCommitFlag(1);
			createOrderCommand.setOrderType(3);
			createOrderCommand.setAccountCode(configProvider.getValue(UserContext.getCurrentNamespaceId(),"parking.wx.subnumberpay","NS"+UserContext.getCurrentNamespaceId()));
		}
		createOrderCommand.setOrderRemark1(configProvider.getValue("parking.pay.OrderRemark1","停车缴费"));
		LOGGER.info("createPurchaseOrder params"+createOrderCommand);
		CreatePurchaseOrderCommand createPurchaseOrderCommand = convertToGorderCommand(createOrderCommand);
		CreatePurchaseOrderRestResponse createOrderResp = orderService.createPurchaseOrder(createPurchaseOrderCommand);//统一订单
//		if (paymentType != null && paymentType == PaymentType.WECHAT_JS_PAY.getCode()) {
//			purchaseOrder = sdkPayService.createCustomOrder(createOrderCommand);
//		}else {
//			purchaseOrder = sdkPayService.createPurchaseOrder(createOrderCommand);
//		}
		if(!checkOrderRestResponseIsSuccess(createOrderResp)){
			LOGGER.info("purchaseOrderRestResponse "+createOrderResp);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_UNUSUAL,
					"preorder failed "+StringHelper.toJsonString(createOrderResp));
		}
		PurchaseOrderCommandResponse orderCommandResponse = createOrderResp.getResponse();

		OrderCommandResponse response = orderCommandResponse.getPayResponse();
		PreOrderDTO preDto = ConvertHelper.convert(response,PreOrderDTO.class);
		preDto.setExpiredIntervalTime(response.getExpirationMillis());
		List<com.everhomes.pay.order.PayMethodDTO> paymentMethods = response.getPaymentMethods();
		String format = "{\"getOrderInfoUrl\":\"%s\"}";
		if(paymentMethods!=null){
			preDto.setPayMethod(paymentMethods.stream().map(bizPayMethod->{
				PayMethodDTO payMethodDTO = ConvertHelper.convert(bizPayMethod, PayMethodDTO.class);
				payMethodDTO.setPaymentName(bizPayMethod.getPaymentName());
				payMethodDTO.setExtendInfo(String.format(format, response.getOrderPaymentStatusQueryUrl()));
				String paymentLogo = contentServerService.parserUri(bizPayMethod.getPaymentLogo());
				payMethodDTO.setPaymentLogo(paymentLogo);
				payMethodDTO.setPaymentType(bizPayMethod.getPaymentType());
				PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO();
				com.everhomes.pay.order.PaymentParamsDTO bizPaymentParamsDTO = bizPayMethod.getPaymentParams();
				if(bizPaymentParamsDTO != null) {
					paymentParamsDTO.setPayType(bizPaymentParamsDTO.getPayType());
				}
				payMethodDTO.setPaymentParams(paymentParamsDTO);

				return payMethodDTO;
			}).collect(Collectors.toList()));//todo
		}
		Long expiredIntervalTime = getExpiredIntervalTime(response.getExpirationMillis());
		preDto.setExpiredIntervalTime(expiredIntervalTime);
		preDto.setOrderId(parkingRechargeOrder.getId());
		parkingRechargeOrder.setBizOrderNo(response.getBizOrderNum());
		parkingProvider.updateParkingRechargeOrder(parkingRechargeOrder);
//		preDto.setPayMethod(getPayMethods(response.getOrderPaymentStatusQueryUrl()));//todo
		return preDto;
	}

	/*
    * 由于从支付系统里回来的CreateOrderRestResponse有可能没有errorScope，故不能直接使用CreateOrderRestResponse.isSuccess()来判断，
      CreateOrderRestResponse.isSuccess()里会对errorScope进行比较
    */
	private boolean checkOrderRestResponseIsSuccess(CreatePurchaseOrderRestResponse response){
		if(response != null && response.getErrorCode() != null
				&& (response.getErrorCode().intValue() == 200 || response.getErrorCode().intValue() == 201))
			return true;
		return false;
	}

	private CreatePurchaseOrderCommand convertToGorderCommand(CreateOrderCommand cmd){
		CreatePurchaseOrderCommand preOrderCommand = new CreatePurchaseOrderCommand();
		//PaymentParamsDTO转为Map


		preOrderCommand.setAmount(cmd.getAmount());

		preOrderCommand.setAccountCode(cmd.getAccountCode());
		preOrderCommand.setClientAppName(cmd.getClientAppName());

		preOrderCommand.setBusinessOrderType(OrderType.OrderTypeEnum.PARKING.getPycode());
		// 移到统一订单系统完成
		// String BizOrderNum  = getOrderNum(orderId, OrderType.OrderTypeEnum.WUYE_CODE.getPycode());
		// preOrderCommand.setBizOrderNum(BizOrderNum);
		BusinessPayerType payerType = BusinessPayerType.USER;
		preOrderCommand.setBusinessPayerType(payerType.getCode());
		preOrderCommand.setBusinessPayerId(String.valueOf(UserContext.currentUserId()));
		String businessPayerParams = getBusinessPayerParams(UserContext.getCurrentNamespaceId());
		preOrderCommand.setBusinessPayerParams(businessPayerParams);

		// preOrderCommand.setPaymentPayeeType(billGroup.getBizPayeeType()); 不填会不会有问题?
		preOrderCommand.setPaymentPayeeId(cmd.getPayeeUserId());

		preOrderCommand.setExtendInfo(cmd.getExtendInfo());
		preOrderCommand.setPaymentParams(cmd.getPaymentParams());
		preOrderCommand.setPaymentType(cmd.getPaymentType());
		preOrderCommand.setCommitFlag(cmd.getCommitFlag());
		//preOrderCommand.setExpirationMillis(EXPIRE_TIME_15_MIN_IN_SEC);
		preOrderCommand.setCallbackUrl(cmd.getBackUrl());

		preOrderCommand.setGoodsName(cmd.getExtendInfo());
		preOrderCommand.setGoodsDescription(null);
		preOrderCommand.setIndustryName(null);
		preOrderCommand.setIndustryCode(null);
		preOrderCommand.setSourceType(cmd.getSourceType());
		preOrderCommand.setOrderRemark1(cmd.getOrderRemark1());
		//preOrderCommand.setOrderRemark2(String.valueOf(cmd.getOrderId()));
		preOrderCommand.setOrderRemark3(cmd.getOrderRemark3());
		preOrderCommand.setOrderRemark4(null);
		preOrderCommand.setOrderRemark5(null);
		String systemId = configProvider.getValue(UserContext.getCurrentNamespaceId(), PaymentConstants.KEY_SYSTEM_ID, "");
		preOrderCommand.setBusinessSystemId(Long.parseLong(systemId));

		return preOrderCommand;
	}

	private String getBusinessPayerParams(Integer namespaceId) {

		Long businessPayerId = UserContext.currentUserId();


		UserIdentifier buyerIdentifier = userProvider.findUserIdentifiersOfUser(businessPayerId, namespaceId);
		String buyerPhone = null;
		if(buyerIdentifier != null) {
			buyerPhone = buyerIdentifier.getIdentifierToken();
		}
		// 找不到手机号则默认一个
		if(buyerPhone == null || buyerPhone.trim().length() == 0) {
			buyerPhone = configProvider.getValue(UserContext.getCurrentNamespaceId(), PaymentConstants.KEY_ORDER_DEFAULT_PERSONAL_BIND_PHONE, "");
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("businessPayerPhone", buyerPhone);
		return StringHelper.toJsonString(map);
	}

	private Long getExpiredIntervalTime(Long expiration){
		Long expiredIntervalTime = null;
		if(expiration != null){
			expiredIntervalTime = expiration - System.currentTimeMillis();
			//转换成秒
			expiredIntervalTime = expiredIntervalTime/1000;
			if(expiredIntervalTime < 0){
				expiredIntervalTime = 0L;
			}
		}
		return expiredIntervalTime;
	}
	private CommonOrderDTO convertOrderDTOForV1(ParkingRechargeOrder parkingRechargeOrder, Byte rechargeType) {
		//调用统一处理订单接口，返回统一订单格式
		CommonOrderCommand orderCmd = new CommonOrderCommand();
		orderCmd.setBody(ParkingRechargeType.fromCode(parkingRechargeOrder.getRechargeType()).toString());
		orderCmd.setOrderNo(parkingRechargeOrder.getId().toString());
		orderCmd.setOrderType(OrderType.OrderTypeEnum.PARKING.getPycode());
		if(rechargeType.equals(ParkingRechargeType.MONTHLY.getCode())) {
			orderCmd.setSubject("停车缴费（月卡车：" + parkingRechargeOrder.getPlateNumber() + "）");
		}else {
			orderCmd.setSubject("停车缴费（临时车：" + parkingRechargeOrder.getPlateNumber() + "）");
		}

		boolean flag = configProvider.getBooleanValue("parking.order.amount", false);
		if(flag) {
			orderCmd.setTotalFee(new BigDecimal(0.01).setScale(2, RoundingMode.FLOOR));
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

	private void createParkingUserInvoice(Long invoiceType, ParkingLot parkingLot, User user) {
		if (null != invoiceType) {
			ParkingUserInvoice userType = parkingProvider.findParkingUserInvoiceByUserId(parkingLot.getOwnerType(),
					parkingLot.getOwnerId(), parkingLot.getId(), user.getId());
			if (null == userType) {
				ParkingUserInvoice parkingUserInvoice = new ParkingUserInvoice();
				parkingUserInvoice.setNamespaceId(user.getNamespaceId());
				parkingUserInvoice.setOwnerType(parkingLot.getOwnerType());
				parkingUserInvoice.setOwnerId(parkingLot.getOwnerId());
				parkingUserInvoice.setParkingLotId(parkingLot.getId());
				parkingUserInvoice.setUserId(user.getId());
				parkingUserInvoice.setInvoiceTypeId(invoiceType);
				parkingProvider.createParkingUserInvoice(parkingUserInvoice);
			}else {
				userType.setInvoiceTypeId(invoiceType);
				parkingProvider.updateParkingUserInvoice(userType);
			}
		}
	}

	@Override
	public ListParkingRechargeOrdersResponse listParkingRechargeOrders(ListParkingRechargeOrdersCommand cmd){

		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		ListParkingRechargeOrdersResponse response = new ListParkingRechargeOrdersResponse();
		//设置分页
		cmd.setPageSize(PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize()));
		User user = UserContext.current().getUser();

		List<ParkingRechargeOrder> list = parkingProvider.listParkingRechargeOrders(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getParkingLotId(), cmd.getPlateNumber(), user.getId(), cmd.getPageAnchor(), cmd.getPageSize());
		int size = list.size();
		if(size > 0){
			response.setOrders(list.stream().map(r -> {
				ParkingRechargeOrderDTO d = ConvertHelper.convert(r, ParkingRechargeOrderDTO.class);

				d.setParkingLotName(parkingLot.getName());
				d.setContact(parkingLot.getContact());
				d.setInvoiceFlag(parkingLot.getInvoiceFlag());
				return d;
			}).collect(Collectors.toList()));
			if(size != cmd.getPageSize()){
				response.setNextPageAnchor(null);
			}else{
				response.setNextPageAnchor(list.get(size-1).getCreateTime().getTime());
			}
		}

		return response;
	}

	@Override
	public ParkingRechargeRateDTO createParkingRechargeRate(CreateParkingRechargeRateCommand cmd){
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		String vendorName = parkingLot.getVendorName();
		ParkingVendorHandler handler = getParkingVendorHandler(vendorName);

		return handler.createParkingRechargeRate(cmd);
	}

	@Override
	public boolean deleteParkingRechargeRate(DeleteParkingRechargeRateCommand cmd){
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		String vendorName = parkingLot.getVendorName();
		ParkingVendorHandler handler = getParkingVendorHandler(vendorName);
		handler.deleteParkingRechargeRate(cmd);
		return true;
	}

	@Override
	public ListParkingRechargeOrdersResponse searchParkingRechargeOrders(SearchParkingRechargeOrdersCommand cmd){
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
			//订单记录权限校验
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), PrivilegeConstants.PARKING_ORDER_MANAGERMENT, cmd.getAppId(), null,cmd.getCurrentProjectId());
		}

		ListParkingRechargeOrdersResponse response = new ListParkingRechargeOrdersResponse();
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
		Timestamp startDate = null;
		Timestamp endDate = null;
		if(null != cmd.getStartDate())
			startDate = new Timestamp(cmd.getStartDate());
		if(null != cmd.getEndDate())
			endDate = new Timestamp(cmd.getEndDate());
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		Integer pageNum = 1;
        if (cmd.getPageNum() != null) {
        	pageNum = cmd.getPageNum();
        }
		List<ParkingRechargeOrder> list = parkingProvider.searchParkingRechargeOrders(cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(), cmd.getPlateOwnerName(),
				cmd.getPayerPhone(), startDate, endDate, cmd.getRechargeType(), cmd.getPaidType(), cmd.getCardNumber(),
				cmd.getStatus(), cmd.getPaySource(),cmd.getKeyWords(),cmd.getPageAnchor(), pageSize, cmd.getPayMode(),pageNum);
		int size = list.size();
		if(size > 0){
			response.setOrders(list.stream().map(r -> {
				ParkingRechargeOrderDTO d = ConvertHelper.convert(r, ParkingRechargeOrderDTO.class);
				d.setVendorName(parkingLot.getName());

				if (null != r.getInvoiceType()) {
					ParkingInvoiceType parkingInvoiceType = parkingProvider.findParkingInvoiceTypeById(r.getInvoiceType());
					if (null != parkingInvoiceType) {
						d.setInvoiceName(parkingInvoiceType.getName());
					}
				}
				return d;
			}).collect(Collectors.toList()));

			if(size != pageSize){
				response.setNextPageAnchor(null);
			}else{
	            Integer nextPageNum = pageNum + 1;
	            response.setNextPageAnchor(nextPageNum.longValue());
//				response.setNextPageAnchor(list.get(size - 1).getCreateTime().getTime());
			}
		}

		BigDecimal totalAmount = parkingProvider.countParkingRechargeOrders(cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(), cmd.getPlateOwnerName(),
				cmd.getPayerPhone(), startDate, endDate, cmd.getRechargeType(), cmd.getPaidType(),cmd.getCardNumber(),
				cmd.getStatus(),cmd.getPaySource(),cmd.getKeyWords());
		response.setTotalAmount(totalAmount);
		Long totalNum = parkingProvider.countRechargeOrdersPageNums(cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(), cmd.getPlateOwnerName(),
				cmd.getPayerPhone(), startDate, endDate, cmd.getRechargeType(), cmd.getPaidType(),cmd.getCardNumber(),
				cmd.getStatus(),cmd.getPaySource(),cmd.getKeyWords());
		response.setTotalNum(totalNum);
		return response;
	}

	@Override
	public ListParkingCardRequestResponse searchParkingCardRequests(SearchParkingCardRequestsCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
			//应用管理权限校验
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), PrivilegeConstants.PARKING_APPLY_MANAGERMENT, cmd.getAppId(), null,cmd.getCurrentProjectId());//月卡申请权限
		}
		ListParkingCardRequestResponse response = new ListParkingCardRequestResponse();
		Timestamp startDate = null;
		Timestamp endDate = null;
		if(null != cmd.getStartDate())
			startDate = new Timestamp(cmd.getStartDate());
		if(null != cmd.getEndDate())
			endDate = new Timestamp(cmd.getEndDate());
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

//		Flow flow = flowProvider.getFlowById(cmd.getFlowId());
		int order = 1;
		TableField field = Tables.EH_PARKING_CARD_REQUESTS.CREATE_TIME;
		//排序
		if (null != cmd.getStatus()) {
			ParkingLot parkingLot = parkingProvider.findParkingLotById(cmd.getParkingLotId());
			Integer flowMode = parkingLot.getFlowMode();
			if (ParkingCardRequestStatus.AUDITING.getCode() == cmd.getStatus()) {
				field = Tables.EH_PARKING_CARD_REQUESTS.CREATE_TIME;
			}else if (ParkingCardRequestStatus.QUEUEING.getCode() == cmd.getStatus()) {
				if (ParkingRequestFlowType.QUEQUE.getCode().equals(flowMode)) {
					field = Tables.EH_PARKING_CARD_REQUESTS.CREATE_TIME;
				}else {
					field = Tables.EH_PARKING_CARD_REQUESTS.AUDIT_SUCCEED_TIME;
				}
			}else if (ParkingCardRequestStatus.PROCESSING.getCode() == cmd.getStatus()) {
				field = Tables.EH_PARKING_CARD_REQUESTS.ISSUE_TIME;
				order = -1;
			}else if (ParkingCardRequestStatus.SUCCEED.getCode() == cmd.getStatus()) {
				field = Tables.EH_PARKING_CARD_REQUESTS.PROCESS_SUCCEED_TIME;
				order = -1;
			}else if (ParkingCardRequestStatus.OPENED.getCode() == cmd.getStatus()) {
				field = Tables.EH_PARKING_CARD_REQUESTS.OPEN_CARD_TIME;
				order = -1;
			}else if (ParkingCardRequestStatus.INACTIVE.getCode() == cmd.getStatus()) {
				field = Tables.EH_PARKING_CARD_REQUESTS.CANCEL_TIME;
				order = -1;
			}

		}
		Integer pageNum = 1;
        if (cmd.getPageNum() != null) {
        	pageNum = cmd.getPageNum();
        }
		List<ParkingCardRequest> list = parkingProvider.searchParkingCardRequests(cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(), cmd.getPlateOwnerName(),
				cmd.getPlateOwnerPhone(), startDate, endDate, cmd.getStatus(), cmd.getCarBrand(),
				cmd.getCarSerieName(), cmd.getPlateOwnerEntperiseName(), cmd.getFlowId(),field, order, cmd.getCardTypeId(),cmd.getOwnerKeyWords(),
				cmd.getPageAnchor(), pageSize,pageNum);

		Long userId = UserContext.current().getUser().getId();
		int size = list.size();
		if(size > 0){
			response.setRequests(list.stream().map(r -> {
				ParkingCardRequestDTO dto = ConvertHelper.convert(r, ParkingCardRequestDTO.class);

				if(dto.getCardTypeName()==null) {
					ParkingCardType cardType = getParkingCardType(cmd.getOwnerType(), cmd.getOwnerId(),
							cmd.getParkingLotId(), r.getCardTypeId());
					if (null != cardType && cardType.getTypeName() != null) {
						dto.setCardTypeName(cardType.getTypeName());
					}
				}
				return dto;
			}).collect(Collectors.toList()));

			if(size != pageSize){
				response.setNextPageAnchor(null);
			}else{
	            Integer nextPageNum = pageNum + 1;
	            response.setNextPageAnchor(nextPageNum.longValue());
//				response.setNextPageAnchor(list.get(size-1).getAnchor().getTime());
			}
		}
		Long totalNum = parkingProvider.countParkingCardRequest(
				cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getFlowId(), null, cmd.getStatus()).longValue();
		response.setTotalNum(totalNum);
		return response;
	}

	@Override
	public void exportParkingCardRequests(SearchParkingCardRequestsCommand cmd, HttpServletResponse response) {
		ListParkingCardRequestResponse resp =  searchParkingCardRequests(cmd);

		List<ParkingCardRequestDTO> requests = resp.getRequests();

		Workbook wb = new XSSFWorkbook();

		Font font = wb.createFont();
		font.setFontName("黑体");
		font.setFontHeightInPoints((short) 16);
		CellStyle style = wb.createCellStyle();
		style.setFont(font);

		Sheet sheet = wb.createSheet("parkingCardRequests");
		sheet.setDefaultColumnWidth(20);
		sheet.setDefaultRowHeightInPoints(20);
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("月卡类型");
		row.createCell(1).setCellValue("公司名称");
		row.createCell(2).setCellValue("用户姓名");
		row.createCell(3).setCellValue("手机号码");
		row.createCell(4).setCellValue("车牌号码");
		row.createCell(5).setCellValue("车辆品牌");
		row.createCell(6).setCellValue("车系名称");
		row.createCell(7).setCellValue("车身颜色");
		row.createCell(8).setCellValue("申请时间");
		row.createCell(9).setCellValue("当前状态");

		SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (null != requests) {
			for(int i = 0, size = requests.size(); i < size; i++){
				Row tempRow = sheet.createRow(i + 1);
				ParkingCardRequestDTO request = requests.get(i);
				tempRow.createCell(0).setCellValue(null == request.getCardTypeName()?"":request.getCardTypeName());
				tempRow.createCell(1).setCellValue(request.getPlateOwnerEntperiseName());
				tempRow.createCell(2).setCellValue(request.getPlateOwnerName());
				tempRow.createCell(3).setCellValue(request.getPlateOwnerPhone());
				tempRow.createCell(4).setCellValue(request.getPlateNumber());
				tempRow.createCell(5).setCellValue(null == request.getCarBrand()?"":request.getCarBrand());
				tempRow.createCell(6).setCellValue(null == request.getCarSerieName()?"":request.getCarSerieName());
				tempRow.createCell(7).setCellValue(null == request.getCarColor()?"":request.getCarColor());
				tempRow.createCell(8).setCellValue(request.getCreateTime()==null?"":datetimeSF.format(request.getCreateTime()));
				tempRow.createCell(9).setCellValue(convertParkingCardRequestStatus(request.getStatus()));
			}
		}

		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			wb.write(out);
			DownloadUtils.download(out, response);
		} catch (IOException e) {
			LOGGER.error("exportParkingCardRequests is fail. {}",e);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.FAIL_EXPORT_FILE,
					"exportParkingCardRequests is fail.");
		}

	}

	private String convertParkingCardRequestStatus(Byte status) {
		if (null == status) {
			return "";
		}
		ParkingCardRequestStatus e = ParkingCardRequestStatus.fromCode(status);

		switch (e) {
			case INACTIVE: return "已取消";
			case AUDITING: return "待审核";
			case QUEUEING: return "排队中";
			case PROCESSING: return "待办理";
			case SUCCEED: return "办理中";
			case OPENED: return "已开通";
			default:return "";
		}
	}

	@Override
	public ParkingCardType getParkingCardType(String ownerType, Long ownerId, Long parkingLotId, String cardTypeId) {
		if (StringUtils.isNotBlank(cardTypeId)) {
			ParkingCardRequestType parkingCardRequestType = parkingProvider.findParkingCardTypeByTypeId(ownerType,
					ownerId, parkingLotId, cardTypeId);
			if (null != parkingCardRequestType) {
				ParkingCardType cardType = new ParkingCardType();
				cardType.setTypeId(parkingCardRequestType.getCardTypeId());
				cardType.setTypeName(parkingCardRequestType.getCardTypeName());
				return cardType;
			}else {
				String json = configProvider.getValue("parking.default.card.type", "");
				ParkingCardType cardType = JSONObject.parseObject(json, ParkingCardType.class);
				if (cardTypeId.equals(cardType.getTypeId())) {
					return cardType;
				}
			}
		}
		return null;
	}

	@Override
	public void setParkingLotConfig(SetParkingLotConfigCommand cmd){
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		if(null == cmd.getExpiredRechargeFlag()){
			LOGGER.error("ExpiredRechargeFlag cannot be null.");
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_LOSE,
					"ExpiredRechargeFlag cannot be null.");
		}

		ParkingRechargeConfig config = ConvertHelper.convert(cmd, ParkingRechargeConfig.class);
//		config.setExpiredRechargeFlag(cmd.getIsSupportRecharge());
//
//		if(ParkingConfigFlag.SUPPORT.getCode() == cmd.getIsSupportRecharge()) {
//
//			config.setMaxExpiredDay(cmd.getReserveDay());
//			config.setExpiredRechargeMonthCount(cmd.getRechargeMonthCount());
//			config.setExpiredRechargeType(cmd.getRechargeType());
//		}else {
//			config.setMaxExpiredDay(0);
//			config.setExpiredRechargeMonthCount(1);
//			config.setExpiredRechargeType(ParkingCardExpiredRechargeType.ALL.getCode());
//		}

		parkingLot.setRechargeJson(JSONObject.toJSONString(config));

		parkingProvider.updateParkingLot(parkingLot);
	}

	@Override
	public void issueParkingCards(IssueParkingCardsCommand cmd) {

		Byte status = cmd.getStatus();
		Integer count = cmd.getCount();
		Long flowId = cmd.getFlowId();
		if(null == count) {
			LOGGER.error("Count cannot be null.");
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_LOSE,
					"Count cannot be null.");
		}
		if(null == status) {
			LOGGER.error("Status cannot be null.");
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_LOSE,
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

		if(null != parkingFlow && parkingFlow.getMaxIssueNumFlag() == ParkingConfigFlag.SUPPORT.getCode()) {
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
//			Flow flow = flowProvider.findSnapshotFlow(flowId, FlowConstants.FLOW_CONFIG_START);
			Integer tag1 = parkingLot.getFlowMode();
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
				if(flowCase!=null) {
					FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
					stepDTO.setFlowCaseId(q.getFlowCaseId());
					stepDTO.setFlowMainId(flowCase.getFlowMainId());
					stepDTO.setFlowVersion(flowCase.getFlowVersion());
					stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
					stepDTO.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
					stepDTO.setStepCount(flowCase.getStepCount());
					flowService.processAutoStep(stepDTO);
				}
			});

			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("Issue parking cards, requestIds=[{}]", strBuilder.toString());
			}
			Integer namespaceId = UserContext.getCurrentNamespaceId();
			Map<String, Object> map = new HashMap<String, Object>();
			String deadline = deadline(parkingLot.getMaxExpiredDay());
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
			list.forEach(r -> {
				r.setIssueTime(new Timestamp(System.currentTimeMillis()));
				r.setStatus(status);
				if(strBuilder.length() > 0) {
					strBuilder.append(", ");
				}
				strBuilder.append(r.getId());
			});
		}else {
			list.forEach(r -> {
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

	private ParkingLot checkParkingLot(String ownerType, Long ownerId, Long parkingLotId){
		if(null == ownerId) {
			LOGGER.error("OwnerId cannot be null.");
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_LOSE,
					"OwnerId cannot be null.");
		}

		if(StringUtils.isBlank(ownerType)) {
			LOGGER.error("OwnerType cannot be null.");
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_LOSE,
					"OwnerType cannot be null.");
		}

		if(null == parkingLotId) {
			LOGGER.error("ParkingLotId cannot be null.");
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_LOSE,
					"ParkingLotId cannot be null.");
		}

		ParkingLot parkingLot = parkingProvider.findParkingLotById(parkingLotId);
		if(null == parkingLot) {
			LOGGER.error("ParkingLot not found, parkingLotId={}", parkingLotId);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_LOSE,
					"ParkingLot not found");
		}
		// 检查参数里的ownerType和ownerId是否与查出来停车场里的匹配
		if(ownerId.longValue() != parkingLot.getOwnerId().longValue()) {
			LOGGER.error("OwnerId is not match with parkingLot ownerId, ownerId={}", ownerId);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_UNUSUAL,
					"OwnerId is not match with parkingLot ownerId.");
		}
		if(ParkingOwnerType.fromCode(parkingLot.getOwnerType()) != ParkingOwnerType.fromCode(ownerType)){
			LOGGER.error("OwnerType is not match with parkingLot OwnerType, ownerType={}", ownerType);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_UNUSUAL,
					"OwnerType is not match with parkingLot OwnerType.");
		}
		return parkingLot;
	}

	private void checkPlateNumber(String plateNumber){
		if(StringUtils.isBlank(plateNumber)) {
			LOGGER.error("PlateNumber cannot be null.");
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_LOSE,
					"PlateNumber cannot be null.");
		}
	}

	private void checkOrderToken(String orderToken){
		if(StringUtils.isBlank(orderToken)) {
			LOGGER.error("OrderToken cannot be null.");
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_LOSE,
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
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_LOSE,
					"Invalid parameter, orderType not found");
		}
		return String.valueOf(code);
	}
	@Override
	public HttpServletResponse exportParkingRechargeOrders(SearchParkingRechargeOrdersCommand cmd, HttpServletResponse response){
		Timestamp startDate = null;
		Timestamp endDate = null;
		if(cmd.getStartDate() != null)
			startDate = new Timestamp(cmd.getStartDate());
		if(cmd.getEndDate() != null)
			endDate = new Timestamp(cmd.getEndDate());
		Integer pageNum = 1;
        if (cmd.getPageNum() != null) {
        	pageNum = cmd.getPageNum();
        }
        Integer pageSize = Integer.MAX_VALUE;
		List<ParkingRechargeOrder> list = parkingProvider.searchParkingRechargeOrders(cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPlateNumber(), cmd.getPlateOwnerName(),
				cmd.getPayerPhone(), startDate, endDate, cmd.getRechargeType(), cmd.getPaidType(), cmd.getCardNumber(),
				cmd.getStatus(), cmd.getPaySource(), cmd.getKeyWords(), cmd.getPageAnchor(), pageSize, cmd.getPayMode(),pageNum);

		Workbook wb = new XSSFWorkbook();

		Font font = wb.createFont();
		font.setFontName("黑体");
		font.setFontHeightInPoints((short) 16);
		CellStyle style = wb.createCellStyle();
		style.setFont(font);

		Sheet sheet = wb.createSheet("parkingRechargeOrders");
//		sheet.autoSizeColumn(1);
//		sheet.setDefaultColumnWidth(20);
		sheet.setDefaultRowHeightInPoints(20);
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("订单号码");
		row.createCell(1).setCellValue("车牌号码");
		row.createCell(2).setCellValue("用户名称");
		row.createCell(3).setCellValue("手机号码");
		row.createCell(4).setCellValue("缴费时间");
		row.createCell(5).setCellValue("月卡充值起始时间");
		row.createCell(6).setCellValue("月卡充值结束时间");
		row.createCell(7).setCellValue("缴费月数");
		row.createCell(8).setCellValue("临时车进场时间");
		row.createCell(9).setCellValue("临时车出场时间");
		row.createCell(10).setCellValue("临时车停车时长(分钟)");
		row.createCell(11).setCellValue("应收金额");
		row.createCell(12).setCellValue("实收金额");
		row.createCell(13).setCellValue("支付方式");
		row.createCell(14).setCellValue("缴费类型");
		row.createCell(15).setCellValue("订单状态");
		row.createCell(16).setCellValue("缴费来源");


		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		String vendor = parkingLot.getVendorName();
		ParkingVendorHandler handler = getParkingVendorHandler(vendor);
		List valueLengths = null;
		if(handler != null){
			handler.setCellValues(list,sheet);
		}

		for (int i = 0; i < 17; i++) {
			String stringCellValue = sheet.getRow(0).getCell(i).getStringCellValue();
			sheet.setColumnWidth(i,stringCellValue.length() * 700);
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			wb.write(out);
			DownloadUtils.download(out, response);
		} catch (IOException e) {
			LOGGER.error("exportParkingRechageOrders is fail. {}",e);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.FAIL_EXPORT_FILE,
					"exportParkingRechageOrders is fail.");
		}

		return response;
	}

	@Override
	public void deleteParkingRechargeOrder(DeleteParkingRechargeOrderCommand cmd) {
		ParkingRechargeOrder order = parkingProvider.findParkingRechargeOrderById(cmd.getId());
		if(null == order){
			LOGGER.error("Order not found, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE,
					ParkingErrorCode.PARAMTER_LOSE,
					"Order not found");
		}
		order.setIsDelete(ParkingOrderDeleteFlag.DELETED.getCode());
		parkingProvider.updateParkingRechargeOrder(order);
	}
	//为还没缴费的订单创建临时订单号，缴费之后会更新此订单号
	//http://devops.lab.everhomes.com/issues/35564
	private Long createUnPaidOrderNo() {
		String suffix = String.valueOf(generateRandomNumber(3));

		return Long.valueOf(String.valueOf(System.currentTimeMillis()) + suffix);
	}

	//创建连续规则的订单号
	//http://devops.lab.everhomes.com/issues/28392
	@Override
	public Long createOrderNo(ParkingLot lot) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String prefix = sdf.format(new Date());

		Tuple<Long, Boolean> enter = this.coordinationProvider.getNamedLock(CoordinationLocks.PARKING_GENERATE_ORDER_NO.getCode() + lot.getId()).enter(() -> {
			ParkingLot parkingLot = checkParkingLot(lot.getOwnerType(), lot.getOwnerId(), lot.getId());
			String orderCode = (lot.getOrderCode()+1)+"";
			int ordercodelength = configProvider.getIntValue("parking.ordercode.length", 8);
			while (orderCode.length()<ordercodelength){
				orderCode="0"+orderCode;
			}
			while (orderCode.length()>ordercodelength){
				orderCode=orderCode.substring(1,orderCode.length());
			}
			int ordertaglength = configProvider.getIntValue("parking.ordertag.length", 3);
			if(lot.getOrderTag()==null || lot.getOrderTag().length()!=ordertaglength){
				throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_GENERATE_ORDER_NO,	"生成订单编号失败,ordertaglength!={}",ordertaglength);
			}

			Long orderNo = Long.valueOf(String.valueOf(prefix) + lot.getOrderTag() + orderCode);
			parkingLot.setOrderCode(Long.valueOf(orderCode));
			parkingProvider.updateParkingLot(parkingLot);
			return orderNo;
		});
		if(!enter.second()){
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_GENERATE_ORDER_NO,	"生成订单编号失败");
		}
		return enter.first();
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
		String plateNumber = cmd.getPlateNumber().trim();
		checkPlateNumber(plateNumber);
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		String vendor = parkingLot.getVendorName();
		ParkingVendorHandler handler = getParkingVendorHandler(vendor);

		ParkingTempFeeDTO dto = handler.getParkingTempFee(parkingLot, plateNumber);

		if (null != parkingLot.getTempFeeDiscountFlag()) {
			if (ParkingConfigFlag.SUPPORT.getCode() == parkingLot.getTempFeeDiscountFlag()) {
				if (null != dto.getPrice()) {
					dto.setOriginalPrice(dto.getPrice());
					BigDecimal newPrice = dto.getPrice().multiply(new BigDecimal(parkingLot.getTempFeeDiscount()))
							.divide(new BigDecimal(10), DefaultParkingVendorHandler.TEMP_FEE_RETAIN_DECIMAL, RoundingMode.HALF_UP);
					dto.setPrice(newPrice);
					dto.setTempFeeDiscount(parkingLot.getTempFeeDiscount());
				}
			}
		}

		if (null != dto && null != dto.getPrice() && dto.getPrice().compareTo(new BigDecimal(0)) == 0) {

			int delayTime = dto.getDelayTime();
			long now = System.currentTimeMillis();

			long entryTime = dto.getEntryTime();
			long pastTime = now - entryTime;
			int pastMinute = (int) (pastTime / (60 * 1000));

			// 还未计费时，只显示剩余时间
			if (pastMinute <= delayTime) {
				dto.setRemainingTime(delayTime - pastMinute);

			}else {
				//延迟时间单位是分钟，这里转成毫秒
				Timestamp startDate = new Timestamp(now - delayTime * 60 * 1000);
				Timestamp endDate = new Timestamp(now);

				ParkingRechargeOrder order = parkingProvider.getParkingRechargeTempOrder(cmd.getOwnerType(), cmd.getOwnerId(),
						cmd.getParkingLotId(), plateNumber, startDate, endDate);

				if (null != order) {
					//如果已缴费，显示剩余离场时间
					Timestamp rechargeTime = order.getRechargeTime();
					pastTime = now - rechargeTime.getTime();
					pastMinute = (int) (pastTime / (60 * 1000));
					dto.setRemainingTime(delayTime - pastMinute);
				}
			}
		}

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

		ParkingRequestCardConfigDTO dto = null;

		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

//		User user = UserContext.current().getUser();
		Long flowId = cmd.getFlowId();
		if(null == flowId) {
			Flow flow = flowService.getEnabledFlow(UserContext.getCurrentNamespaceId(), ParkingFlowConstant.PARKING_RECHARGE_MODULE,
					FlowModuleType.NO_MODULE.getCode(), cmd.getParkingLotId(), FlowOwnerType.PARKING.getCode());

			if (null == flow) {
				return null;
			}
			flowId = flow.getFlowMainId();
//        	LOGGER.error("FlowId cannot be null.");
//    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//    				"FlowId cannot be null.");
		}

		ParkingFlow parkingFlow = parkingProvider.getParkingRequestCardConfig(cmd.getOwnerType(), cmd.getOwnerId(), parkingLot.getId(), flowId);

		if(null != parkingFlow) {
			dto = ConvertHelper.convert(parkingFlow, ParkingRequestCardConfigDTO.class);

			String host =  configProvider.getValue(UserContext.getCurrentNamespaceId(), "home.url", "");

			if (parkingFlow.getCardAgreementFlag() == ParkingConfigFlag.SUPPORT.getCode()) {
//				dto.setCardAgreementUrl(host + "/web/lib/html/park_payment_review.html?configId=" + parkingFlow.getId());
				dto.setCardAgreementUrl(host + configProvider.getValue("parking.agreement.url", "/park_payment_review/index.html?configId=") + parkingFlow.getId());
			}
		}else {
			dto = ConvertHelper.convert(cmd, ParkingRequestCardConfigDTO.class);
			dto.setCardAgreementFlag(ParkingConfigFlag.NOTSUPPORT.getCode());
			dto.setCardRequestTipFlag(ParkingConfigFlag.NOTSUPPORT.getCode());
			dto.setMaxIssueNumFlag(ParkingConfigFlag.NOTSUPPORT.getCode());
			dto.setMaxRequestNumFlag(ParkingConfigFlag.NOTSUPPORT.getCode());
			dto.setRequestMonthCount(ParkingVendorHandler.REQUEST_MONTH_COUNT);
			dto.setRequestRechargeType(ParkingVendorHandler.REQUEST_RECHARGE_TYPE);
			dto.setCardTypeTipFlag(ParkingConfigFlag.NOTSUPPORT.getCode());
		}

		ListParkingCardRequestTypesCommand typesCommand = ConvertHelper.convert(cmd, ListParkingCardRequestTypesCommand.class);
		dto.setRequestTypes(listParkingCardRequestTypes(typesCommand));
		return dto;
	}

	@Override
	public void setParkingRequestCardConfig(SetParkingRequestCardConfigCommand cmd) {

		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
		Long flowId = cmd.getFlowId();
		if(null == flowId) {
			LOGGER.error("FlowId cannot be null.");
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NO_WORK_FLOW_ENABLE,
					"请先设置工作流.");
		}

		Integer namespaceId = UserContext.current().getUser().getNamespaceId();
		ParkingFlow parkingFlow = parkingProvider.getParkingRequestCardConfig(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId(), flowId);
		if(null == parkingFlow) {
			parkingFlow = ConvertHelper.convert(cmd, ParkingFlow.class);
			parkingFlow.setNamespaceId(namespaceId);
			parkingFlow.setFlowId(flowId);

//			parkingFlow.setOwnerId(cmd.getOwnerId());
//			parkingFlow.setOwnerType(cmd.getOwnerType());
//			parkingFlow.setParkingLotId(parkingLot.getId());
//			parkingFlow.setCardAgreement(cmd.getCardAgreement());
//			parkingFlow.setCardRequestTip(cmd.getCardRequestTip());
//			parkingFlow.setMaxIssueNum(cmd.getMaxIssueNum());
//			parkingFlow.setRequestMonthCount(cmd.getRequestMonthCount());
//			parkingFlow.setRequestRechargeType(cmd.getRequestRechargeType());
//
//			parkingFlow.setMaxRequestNum(cmd.getMaxRequestNum());
//			parkingFlow.setCardAgreementFlag(cmd.getCardAgreementFlag());
//			parkingFlow.setCardRequestTipFlag(cmd.getCardRequestTipFlag());
//			parkingFlow.setMaxIssueNumFlag(cmd.getMaxIssueNumFlag());
//			parkingFlow.setMaxRequestNumFlag(cmd.getMaxRequestNumFlag());
			parkingProvider.createParkingRequestCardConfig(parkingFlow);
		}else {
			BeanUtils.copyProperties(cmd, parkingFlow);
//			parkingFlow.setCardAgreement(cmd.getCardAgreement());
//			parkingFlow.setCardRequestTip(cmd.getCardRequestTip());
//			parkingFlow.setMaxIssueNum(cmd.getMaxIssueNum());
//			parkingFlow.setRequestMonthCount(cmd.getRequestMonthCount());
//			parkingFlow.setRequestRechargeType(cmd.getRequestRechargeType());
//			parkingFlow.setMaxRequestNum(cmd.getMaxRequestNum());
//			parkingFlow.setCardAgreementFlag(cmd.getCardAgreementFlag());
//			parkingFlow.setCardRequestTipFlag(cmd.getCardRequestTipFlag());
//			parkingFlow.setMaxIssueNumFlag(cmd.getMaxIssueNumFlag());
//			parkingFlow.setMaxRequestNumFlag(cmd.getMaxRequestNumFlag());
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
	public ParkingExpiredRechargeInfoDTO getExpiredRechargeInfo(GetExpiredRechargeInfoCommand cmd) {
		checkPlateNumber(cmd.getPlateNumber());
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		String vendor = parkingLot.getVendorName();
		ParkingVendorHandler handler = getParkingVendorHandler(vendor);

		Byte rechargeFlag = parkingLot.getExpiredRechargeFlag();
		if(ParkingConfigFlag.fromCode(rechargeFlag) == ParkingConfigFlag.NOTSUPPORT){
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_SELF_DEFINE,
					"不支持过期充值");
		}

		ParkingExpiredRechargeInfoDTO dto = handler.getExpiredRechargeInfo(parkingLot, cmd);

		return dto;
	}

	@Override
	public SurplusCardCountDTO getSurplusCardCount(GetParkingRequestCardConfigCommand cmd) {
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		Long flowId = cmd.getFlowId();
//		if(null == flowId) {
//			LOGGER.error("FlowId cannot be null.");
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//					"FlowId cannot be null.");
//		}

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

	@Override
	public ParkingCardDTO getRechargeResult(GetRechargeResultCommand cmd) {

		long startTime = System.currentTimeMillis();
		//这个接口兼容老版本，用轮询阻塞10秒钟
		long endTime = startTime + 10 * 1000;
		checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		ParkingRechargeOrder order = parkingProvider.findParkingRechargeOrderById(cmd.getOrderId());

		if(null == order) {
			LOGGER.error("Order not found, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_LOSE,
					"Order not found.");
		}

//		if (order.getStatus() > ParkingRechargeOrderStatus.PAID.getCode()) {
//			ParkingCardDTO dto = new ParkingCardDTO();
//			dto.setOwnerId(cmd.getOwnerId());
//			dto.setOwnerType(cmd.getOwnerType());
//			dto.setParkingLotId(order.getParkingLotId());
//			dto.setPlateNumber(order.getPlateNumber());
//			dto.setPlateOwnerName(order.getPlateOwnerName());
//			dto.setPlateOwnerPhone(order.getPlateOwnerPhone());
//			dto.setEndTime(order.getEndPeriod().getTime());
//			return dto;
//		}
//		boolean flag = true;
		while(/*flag && */order.getStatus() == ParkingRechargeOrderStatus.UNPAID.getCode()
				&& endTime >= startTime) {
			try {

				order = parkingProvider.findParkingRechargeOrderById(cmd.getOrderId());

//				if (order.getStatus() > ParkingRechargeOrderStatus.PAID.getCode()) {
//					flag = false;
//				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			startTime = System.currentTimeMillis();
		}

		ParkingCardDTO dto = new ParkingCardDTO();

		dto.setOwnerId(cmd.getOwnerId());
		dto.setOwnerType(cmd.getOwnerType());
		dto.setParkingLotId(order.getParkingLotId());
		dto.setPlateNumber(order.getPlateNumber());
		dto.setPlateOwnerName(order.getPlateOwnerName());
		dto.setPlateOwnerPhone(order.getPlateOwnerPhone());
		dto.setEndTime(order.getEndPeriod().getTime());

		return dto;

	}

	@Override
	public void synchronizedData(ListParkingCardRequestsCommand cmd) {

		Integer namespaceId = UserContext.getCurrentNamespaceId();
		User user = UserContext.current().getUser();

		String ownerType = FlowOwnerType.PARKING.getCode();
		Flow flow = flowService.getEnabledFlow(namespaceId, ParkingFlowConstant.PARKING_RECHARGE_MODULE,
				FlowModuleType.NO_MODULE.getCode(), cmd.getParkingLotId(), ownerType);

		if(null == flow) {
			LOGGER.error("Enabled flow not found, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_LOSE,
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

		User user = UserContext.current().getUser();
		Long userId = user.getId();

		ParkingCarVerification verification = parkingProvider.findParkingCarVerificationByUserId(cmd.getOwnerType(), cmd.getOwnerId(),
				parkingLotId, cmd.getPlateNumber(), userId);

		ParkingCarLockInfoDTO dto = new ParkingCarLockInfoDTO();

		if (null == verification || verification.getStatus() == ParkingCarVerificationStatus.INACTIVE.getCode()
				|| verification.getStatus() == ParkingCarVerificationStatus.FAILED.getCode()
				|| verification.getStatus() == ParkingCarVerificationStatus.UN_AUTHORIZED.getCode()) {
			dto.setCarVerificationFlag(ParkingCarVerificationStatus.UN_AUTHORIZED.getCode());
		}else if (verification.getStatus() == ParkingCarVerificationStatus.AUDITING.getCode()) {
			dto.setCarVerificationFlag(ParkingCarVerificationStatus.AUDITING.getCode());

			String flowCaseUrl = configProvider.getValue(ConfigConstants.PARKING_CAR_VERIFICATION_FLOWCASE_URL, "");

			dto.setFlowCaseUrl(String.format(flowCaseUrl, verification.getFlowCaseId()));
		}else if (verification.getStatus() == ParkingCarVerificationStatus.SUCCEED.getCode()) {

			dto.setCarVerificationFlag(ParkingCarVerificationStatus.SUCCEED.getCode());

		}

		ParkingCarLockInfoDTO temp = getParkingCarLockInfoDTO(cmd, parkingLot, user);
		if (null != temp) {
			temp.setCarVerificationFlag(dto.getCarVerificationFlag());
			temp.setFlowCaseUrl(dto.getFlowCaseUrl());
			//兼容老版本app, 不论认证是什么状态，都返回锁车数据, 注意：前提是客户端根据carVerificationFlag标志来跳转
			BeanUtils.copyProperties(temp, dto);
		}else {
			if (dto.getCarVerificationFlag() == ParkingCarVerificationStatus.SUCCEED.getCode()) {
				dto = null;
			}
		}

		return dto;
	}

	private ParkingCarLockInfoDTO getParkingCarLockInfoDTO(GetParkingCarLockInfoCommand cmd, ParkingLot parkingLot, User user) {
		String vendorName = parkingLot.getVendorName();
		ParkingVendorHandler handler = getParkingVendorHandler(vendorName);

		ParkingCarLockInfoDTO dto = handler.getParkingCarLockInfo(cmd);

		if (null != dto) {
			dto.setOwnerId(cmd.getOwnerId());
			dto.setOwnerType(cmd.getOwnerType());
			dto.setParkingLotId(cmd.getParkingLotId());
			dto.setPlateNumber(cmd.getPlateNumber());
			dto.setParkingLotName(parkingLot.getName());

			Long organizationId = cmd.getOrganizationId();

			String plateOwnerName = user.getNickName();

			if(null != organizationId) {
				OrganizationMember organizationMember = organizationProvider.findOrganizationMemberByUIdAndOrgId(user.getId(), organizationId);
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

		String vendorName = parkingLot.getVendorName();
		ParkingVendorHandler handler = getParkingVendorHandler(vendorName);

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

	@Override
	public ParkingRechargeOrderDTO updateParkingOrder(UpdateParkingOrderCommand cmd) {

		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		String vendor = parkingLot.getVendorName();
		ParkingVendorHandler handler = getParkingVendorHandler(vendor);

		ParkingRechargeOrder order = parkingProvider.findParkingRechargeOrderById(cmd.getOrderId());

		if (order.getStatus() == ParkingRechargeOrderStatus.FAILED.getCode()) {
			//TODO:
			if (handler.notifyParkingRechargeOrderPayment(order)) {
				order.setStatus(ParkingRechargeOrderStatus.RECHARGED.getCode());
				order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
				parkingProvider.updateParkingRechargeOrder(order);

				ParkingRechargeOrderDTO dto = ConvertHelper.convert(order, ParkingRechargeOrderDTO.class);
				return dto;
			}
		}

		throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_RECHARGE_ORDER,
				"Parking recharge failed.");
	}

	@Override
	public void refundParkingOrder(RefundParkingOrderCommand cmd){
//		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
//
//		long startTime = System.currentTimeMillis();
		ParkingRechargeOrder order = parkingProvider.findParkingRechargeOrderById(cmd.getOrderId());

		//只有需要支付并已经支付的才需要退款
		if(order.getStatus() < ParkingRechargeOrderStatus.PAID.getCode()){
			return;
		}
		if (ActivityRosterPayVersionFlag.V1.getCode() == order.getPaidVersion()) {
			refundParkingOrderV1(cmd, order);
		}else {
			refundParkingOrderV2(cmd, order);
		}

//		order.setStatus(ParkingRechargeOrderStatus.REFUNDED.getCode());
//		order.setRefundTime(new Timestamp(System.currentTimeMillis()));
//		parkingProvider.updateParkingRechargeOrder(order);
	}

	private void refundParkingOrderV2 (RefundParkingOrderCommand cmd, ParkingRechargeOrder order) {
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
		Long amount = order.getPrice().multiply(new BigDecimal(100)).longValue();
		boolean flag = configProvider.getBooleanValue("parking.order.amount", false);
		if(flag) {
			amount = 1L;
		}
		if(order.getPayOrderNo()==null){
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_REFUND_ERROR,
					"支付系统订单号不存在");
		}


		CreateRefundOrderCommand createRefundOrderCommand = new CreateRefundOrderCommand();
		String systemId = configProvider.getValue(0, PaymentConstants.KEY_SYSTEM_ID, "");
		createRefundOrderCommand.setBusinessSystemId(Long.parseLong(systemId));
		String sNamespaceId = BIZ_ACCOUNT_PRE+UserContext.getCurrentNamespaceId();
		createRefundOrderCommand.setAccountCode(sNamespaceId);
		if (order.getBizOrderNo() == null) //兼容
			createRefundOrderCommand.setBusinessOrderNumber(generateBizOrderNum(sNamespaceId+"",OrderType.OrderTypeEnum.PARKING.getPycode(),order.getOrderNo()));
		else
			createRefundOrderCommand.setBusinessOrderNumber(order.getBizOrderNo());
		createRefundOrderCommand.setAmount(amount);
		createRefundOrderCommand.setBusinessOperatorType(BusinessPayerType.USER.getCode());
		createRefundOrderCommand.setBusinessOperatorId(String.valueOf(UserContext.currentUserId()));
		String homeurl = configProvider.getValue("home.url", "");
		String callbackurl = homeurl + contextPath + configProvider.getValue(UserContext.getCurrentNamespaceId(),"parking.pay.callBackUrl", "/parking/notifyParkingRechargeOrderPaymentV2");
		createRefundOrderCommand.setCallbackUrl(callbackurl);
		createRefundOrderCommand.setSourceType(SourceType.MOBILE.getCode());

		CreateRefundOrderRestResponse refundOrderRestResponse = this.orderService.createRefundOrder(createRefundOrderCommand);

		if(refundOrderRestResponse != null && refundOrderRestResponse.getErrorCode() != null && refundOrderRestResponse.getErrorCode().equals(HttpStatus.OK.value())){

		} else{
			LOGGER.error("Refund failed from vendor, cmd={}, response={}",
					cmd, refundOrderRestResponse);
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_REFUND_ERROR,
					"网络出错，服务连接失败");
		}
		if(refundOrderRestResponse.getResponse().getOrderId()!=null){
			order.setGeneralOrderId(refundOrderRestResponse.getResponse().getOrderId()+"");
		}
		order.setBizOrderNo(refundOrderRestResponse.getResponse().getBusinessOrderNumber());
		order.setStatus(ParkingRechargeOrderStatus.REFUNDING.getCode());
		parkingProvider.updateParkingRechargeOrder(order);
	}

	private void refundParkingOrderV1 (RefundParkingOrderCommand cmd, ParkingRechargeOrder order) {
		PayZuolinRefundCommand refundCmd = new PayZuolinRefundCommand();
		String refundApi =  configProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.zuolin.refound", "POST /EDS_PAY/rest/pay_common/refund/save_refundInfo_record");
		String appKey = configProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.appKey", "");
		refundCmd.setAppKey(appKey);
		Long timestamp = System.currentTimeMillis();
		refundCmd.setTimestamp(timestamp);
		Integer randomNum = (int) (Math.random()*1000);
		refundCmd.setNonce(randomNum);

		refundCmd.setRefundOrderNo(String.valueOf(order.getOrderNo()));
		refundCmd.setOrderNo(String.valueOf(order.getId()));

		refundCmd.setOnlinePayStyleNo(VendorType.fromCode(order.getPaidType()).getStyleNo());

		refundCmd.setOrderType(OrderType.OrderTypeEnum.PARKING.getPycode());
		boolean flag = configProvider.getBooleanValue("parking.order.amount", false);
		if (flag) {
			refundCmd.setRefundAmount(new BigDecimal(0.01).setScale(2, RoundingMode.FLOOR));
		}else {
			refundCmd.setRefundAmount(order.getPrice());
		}

		refundCmd.setRefundMsg("停车缴费退款");
		this.setSignatureParam(refundCmd);

		PayZuolinRefundResponse refundResponse = (PayZuolinRefundResponse) this.restCall(refundApi, refundCmd, PayZuolinRefundResponse.class);
		if(refundResponse.getErrorCode().equals(HttpStatus.OK.value())){
			order.setStatus(ParkingRechargeOrderStatus.REFUNDED.getCode());
			order.setRefundTime(new Timestamp(System.currentTimeMillis()));
			parkingProvider.updateParkingRechargeOrder(order);
		} else{
			LOGGER.error("Refund failed from vendor, cmd={}, refundCmd={}, response={}",
					cmd, refundCmd, refundResponse);
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_REFUND_ERROR,
					"bill refund error");
		}
	}

	/***给支付相关的参数签名*/
	private void setSignatureParam(PayZuolinRefundCommand cmd) {
		App app = appProvider.findAppByKey(cmd.getAppKey());

		Map<String,String> map = new HashMap<>();
		map.put("appKey",cmd.getAppKey());
		map.put("timestamp",cmd.getTimestamp()+"");
		map.put("nonce",cmd.getNonce()+"");
		map.put("refundOrderNo",cmd.getRefundOrderNo());
		map.put("orderNo", cmd.getOrderNo());
		map.put("onlinePayStyleNo",cmd.getOnlinePayStyleNo() );
		map.put("orderType",cmd.getOrderType() );
		//modify by wh 2016-10-24 退款使用toString,下订单的时候使用doubleValue,两边用的不一样,为了和电商保持一致,要修改成toString
//		map.put("refundAmount", cmd.getRefundAmount().doubleValue()+"");
		map.put("refundAmount", cmd.getRefundAmount().toString());
		map.put("refundMsg", cmd.getRefundMsg());
		String signature = SignatureHelper.computeSignature(map, app.getSecretKey());
		cmd.setSignature(signature);
	}

	private Object restCall(String api, Object command, Class<?> responseType) {
		String host = this.configProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.zuolin.host", "https://pay.zuolin.com");
		return RentalUtils.restCall(api, command, responseType, host);
	}

	@Override
	public DeferredResult getRechargeOrderResult(GetRechargeResultCommand cmd) {

		RuntimeErrorException exceptionResult = RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.RECHARE_TIME_OUT,
				"回调超时，请稍后再试");
		final DeferredResult<RestResponse> deferredResult = new DeferredResult<RestResponse>(60000L, exceptionResult);
//        System.out.println(Thread.currentThread().getName());
//        map.put("test", deferredResult);

//        new Thread(() -> {
//            RestResponse response = new RestResponse("Received deferTest response");
//
//            deferredResult.setResult(response);
//        });
		//这个方法是在客户端支付完成之后才被调用，防止调用此方法之前，支付模块已回调成功，此时直接返回订单
		ParkingRechargeOrder order = parkingProvider.findParkingRechargeOrderById(cmd.getOrderId());
		if (order.getStatus() > ParkingRechargeOrderStatus.PAID.getCode()) {

			ParkingRechargeOrderDTO dto = ConvertHelper.convert(order, ParkingRechargeOrderDTO.class);
			ParkingLot parkingLot = checkParkingLot(order.getOwnerType(), order.getOwnerId(), order.getParkingLotId());
			dto.setParkingLotName(parkingLot.getName());
			dto.setContact(parkingLot.getContact());
			dto.setInvoiceFlag(parkingLot.getInvoiceFlag());

			RestResponse response = new RestResponse(dto);
			response.setErrorCode(ErrorCodes.SUCCESS);
			response.setErrorDescription("OK");
			deferredResult.setResult(response);
			return deferredResult;

		}

		localBusSubscriberBuilder.build("Parking-Recharge" + cmd.getOrderId(), new LocalBusOneshotSubscriber() {
			@Override
			public Action onLocalBusMessage(Object sender, String subject,
											Object pingResponse, String path) {

				String respStr = (String) pingResponse;

//				ParkingRechargeOrderDTO dto = ConvertHelper.convert(respStr, ParkingRechargeOrderDTO.class);
				ParkingRechargeOrderDTO dto = JSONObject.parseObject(respStr, ParkingRechargeOrderDTO.class);
				ParkingLot parkingLot = checkParkingLot(order.getOwnerType(), order.getOwnerId(), order.getParkingLotId());
				dto.setParkingLotName(parkingLot.getName());
				dto.setContact(parkingLot.getContact());

				RestResponse response = new RestResponse(dto);
				response.setErrorCode(ErrorCodes.SUCCESS);
				response.setErrorDescription("OK");
				deferredResult.setResult(response);

				return null;
			}

			@Override
			public void onLocalBusListeningTimeout() {
				RestResponse response = ConvertHelper.convert(exceptionResult, RestResponse.class);
				deferredResult.setResult(response);
			}
		}).setTimeout(60000).create();

		return deferredResult;
	}

	@Override
	public ParkingFreeSpaceNumDTO getFreeSpaceNum(GetFreeSpaceNumCommand cmd) {
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		String vendor = parkingLot.getVendorName();
		ParkingVendorHandler handler = getParkingVendorHandler(vendor);

		ParkingFreeSpaceNumDTO dto = handler.getFreeSpaceNum(cmd);

		return dto;
	}

	@Override
	public ParkingCarLocationDTO getCarLocation(GetCarLocationCommand cmd) {
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		String vendor = parkingLot.getVendorName();
		ParkingVendorHandler handler = getParkingVendorHandler(vendor);

		ParkingCarLocationDTO dto = handler.getCarLocation(parkingLot, cmd);

		return dto;
	}

	@Override
	public List<ParkingCardRequestTypeDTO> listParkingCardRequestTypes(ListParkingCardRequestTypesCommand cmd) {

		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		List<ParkingCardRequestType> types = parkingProvider.listParkingCardTypes(cmd.getOwnerType(), cmd.getOwnerId(), parkingLot.getId());

		List<ParkingCardRequestTypeDTO> dtos = new ArrayList<>();
		if (!types.isEmpty()) {
			ListParkingRechargeRatesCommand listParkingRechargeRatesCommand = ConvertHelper.convert(cmd, ListParkingRechargeRatesCommand.class);
 			List<ParkingRechargeRateDTO> rates = listParkingRechargeRates(listParkingRechargeRatesCommand);
			for (ParkingCardRequestType type: types) {
	 			for (ParkingRechargeRateDTO rate :rates){
					ParkingCardRequestTypeDTO dto = new ParkingCardRequestTypeDTO();
					if (type.getCardTypeId().equals(rate.getCardTypeId())){
						dto = ConvertHelper.convert(type, ParkingCardRequestTypeDTO.class);
						dtos.add(dto);
					}
				}
			}

			for (ParkingCardRequestTypeDTO type: dtos) {
				for (ParkingRechargeRateDTO rate: rates) {
					if (rate.getCardTypeId().equals(type.getCardTypeId()) && rate.getMonthCount().intValue() == 1) {
						//默认去一个月的费率
						type.setPrice(rate.getPrice());
						break;
					}
				}
			}
		}else {
			String vendorName = parkingLot.getVendorName();
			ParkingVendorHandler handler = getParkingVendorHandler(vendorName);

			ListCardTypeCommand cardTypeCmd = new ListCardTypeCommand(cmd.getOwnerType(), cmd.getOwnerId(),
					cmd.getParkingLotId());
			ListCardTypeResponse listCardTypeResponse = handler.listCardType(cardTypeCmd);

			List<ParkingCardType> list = listCardTypeResponse.getCardTypes();
			if(list==null || list.size()==0){
				throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE,
						ParkingErrorCode.ERROR_PARKING_TYPE_NOT_FOUND, "未查询到月卡类型信息");
			}

			dtos = list.stream().map(r ->{
				ParkingCardRequestTypeDTO dto = new ParkingCardRequestTypeDTO();
				dto.setCardTypeId(r.getTypeId());
				dto.setCardTypeName(r.getTypeName());
				return dto;
			}).collect(Collectors.toList());

//			String json = configProvider.getValue("parking.default.card.type", "");
//			ParkingCardType cardType = JSONObject.parseObject(json, ParkingCardType.class);
//			ParkingCardRequestTypeDTO dto = ConvertHelper.convert(cmd, ParkingCardRequestTypeDTO.class);
//			dto.setCardTypeId(cardType.getTypeId());
//			dto.setCardTypeName(cardType.getTypeName());
//			dto.setNamespaceId(UserContext.getCurrentNamespaceId());
//			dtos.add(dto);
		}

		return dtos;
	}

	@Override
	public List<ParkingInvoiceTypeDTO> listParkingInvoiceTypes(ListParkingInvoiceTypesCommand cmd) {

		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		List<ParkingInvoiceType> types = parkingProvider.listParkingInvoiceTypes(cmd.getOwnerType(), cmd.getOwnerId(), parkingLot.getId());

		ParkingUserInvoice userType = parkingProvider.findParkingUserInvoiceByUserId(cmd.getOwnerType(),
				cmd.getOwnerId(), parkingLot.getId(), UserContext.currentUserId());

		if (null != userType) {
//			ParkingInvoiceType type = types.stream().filter(r -> r.getId().equals(userType.getInvoiceTypeId())).findFirst().get();
			ParkingInvoiceType temp = null;
			for (ParkingInvoiceType t: types) {
				if (t.getId().equals(userType.getInvoiceTypeId())) {
					temp = t;
					break;
				}
			}
			if (null != temp) {
				types.remove(temp);
				types.add(0, temp);
			}
		}

		List<ParkingInvoiceTypeDTO> dtos = types.stream().map(r -> ConvertHelper.convert(r, ParkingInvoiceTypeDTO.class))
				.collect(Collectors.toList());

		return dtos;
	}

	@Override
	public SearchParkingCarVerificationResponse searchParkingCarVerifications(SearchParkingCarVerificationsCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
			//申请管理权限校验
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), PrivilegeConstants.PARKING_APPLY_MANAGERMENT, cmd.getAppId(), null,cmd.getCurrentProjectId());
		}
		checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		Timestamp startTime = null;
		Timestamp endTime = null;
		if (null != cmd.getStartTime()) {
			startTime = new Timestamp(cmd.getStartTime());
		}
		if (null != cmd.getEndTime()) {
			endTime = new Timestamp(cmd.getEndTime());
		}
		Integer pageNum = 1;
        if (cmd.getPageNum() != null) {
        	pageNum = cmd.getPageNum();
        }
		List<ParkingCarVerification> verifications = parkingProvider.searchParkingCarVerifications(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getParkingLotId(), cmd.getPlateNumber(), cmd.getPlateOwnerName(), cmd.getPlateOwnerPhone(), startTime, endTime,
				cmd.getStatus(), cmd.getRequestorEnterpriseName(), cmd.getOwnerKeyWords(), cmd.getPageAnchor(), pageSize,pageNum);

		SearchParkingCarVerificationResponse response = new SearchParkingCarVerificationResponse();

		int size = verifications.size();
		if(size > 0){
			response.setRequests(verifications.stream().map(r -> {
				ParkingCarVerificationDTO dto = ConvertHelper.convert(r, ParkingCarVerificationDTO.class);
				return dto;
			}).collect(Collectors.toList()));

			if(size != pageSize){
				response.setNextPageAnchor(null);
			}else{
	            Integer nextPageNum = pageNum + 1;
	            response.setNextPageAnchor(nextPageNum.longValue());
//				response.setNextPageAnchor(verifications.get(size-1).getCreateTime().getTime());
			}
		}

		Long totalNum = parkingProvider.countCarVerifications(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getParkingLotId(), cmd.getPlateNumber(), cmd.getPlateOwnerName(), cmd.getPlateOwnerPhone(), startTime, endTime,
				cmd.getStatus(), cmd.getRequestorEnterpriseName(), cmd.getOwnerKeyWords());
		response.setTotalNum(totalNum);
		return response;
	}

	public void exportParkingCarVerifications(SearchParkingCarVerificationsCommand cmd, HttpServletResponse resp){
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
			//申请管理权限校验
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), PrivilegeConstants.PARKING_APPLY_MANAGERMENT, cmd.getAppId(), null,cmd.getCurrentProjectId());
		}
		checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		Timestamp startTime = null;
		Timestamp endTime = null;
		if (null != cmd.getStartTime()) {
			startTime = new Timestamp(cmd.getStartTime());
		}
		if (null != cmd.getEndTime()) {
			endTime = new Timestamp(cmd.getEndTime());
		}
		Integer pageNum = 1;
        if (cmd.getPageNum() != null) {
        	pageNum = cmd.getPageNum();
        }
		List<ParkingCarVerification> verifications = parkingProvider.searchParkingCarVerifications(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getParkingLotId(), cmd.getPlateNumber(), cmd.getPlateOwnerName(), cmd.getPlateOwnerPhone(), startTime, endTime,
				cmd.getStatus(), cmd.getRequestorEnterpriseName(),  cmd.getOwnerKeyWords(),null,
				configProvider.getIntValue("parking.exportcarverifications.maxcount",10000),pageNum);

		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("车辆认证申请记录");
		sheet.setDefaultColumnWidth(20);
		sheet.setDefaultRowHeightInPoints(20);
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("序号");
		row.createCell(1).setCellValue("公司名称");
		row.createCell(2).setCellValue("用户姓名");
		row.createCell(3).setCellValue("手机号码");
		row.createCell(4).setCellValue("车牌号码");
		row.createCell(5).setCellValue("申请时间");
		row.createCell(6).setCellValue("认证状态");

		DateTimeFormatter datetimeSF = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		if (null != verifications) {
			for(int i = 0, size = verifications.size(); i < size; i++){
				Row tempRow = sheet.createRow(i + 1);
				ParkingCarVerification carVerification = verifications.get(i);

				tempRow.createCell(0).setCellValue(i+1);
				tempRow.createCell(1).setCellValue(carVerification.getRequestorEnterpriseName()==null?"无":carVerification.getRequestorEnterpriseName());
				tempRow.createCell(2).setCellValue(carVerification.getPlateOwnerName()==null?"无":carVerification.getPlateOwnerName());
				tempRow.createCell(3).setCellValue(carVerification.getPlateOwnerPhone()==null?"无":carVerification.getPlateOwnerPhone());
				tempRow.createCell(4).setCellValue(carVerification.getPlateNumber()==null?"无":carVerification.getPlateNumber());
				Timestamp createTime = carVerification.getCreateTime();
				if(createTime==null){
					tempRow.createCell(5).setCellValue("无");
				}else {
					String applyTime = createTime.toLocalDateTime().format(datetimeSF);
					tempRow.createCell(5).setCellValue(applyTime);
				}
				ParkingCardRequestStatus requestStatus = ParkingCardRequestStatus.fromCode(carVerification.getStatus());
				tempRow.createCell(6).setCellValue(requestStatus==null?"无":requestStatus.getDesc());
			}
		}

		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			wb.write(out);
			DownloadUtils.download(out, resp);
		} catch (IOException e) {
			LOGGER.error("exportParkingCarVerifications is fail. {}",e);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.FAIL_EXPORT_FILE,
					"exportParkingCarVerifications is fail.");
		}
	}

	@Override
	public ListParkingCarVerificationsResponse listParkingCarVerifications(ListParkingCarVerificationsCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
			//应用管理权限校验
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), PrivilegeConstants.PARKING_APPLY_MANAGERMENT, cmd.getAppId(), null,cmd.getCurrentProjectId());//月卡申请权限
		}
		checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		Long userId = UserContext.currentUserId();

		List<ParkingCarVerification> verifications = parkingProvider.listParkingCarVerifications(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getParkingLotId(), userId, null, cmd.getPageAnchor(), pageSize);

		ListParkingCarVerificationsResponse response = new ListParkingCarVerificationsResponse();

		int size = verifications.size();
		if(size > 0){
			response.setRequests(verifications.stream().map(r -> {
				ParkingCarVerificationDTO dto = ConvertHelper.convert(r, ParkingCarVerificationDTO.class);

//				String flowCaseUrl = configProvider.getValue(ConfigConstants.PARKING_CAR_VERIFICATION_FLOWCASE_URL, "");
//
//				dto.setFlowCaseUrl(String.format(flowCaseUrl, verification.getFlowCaseId()));
				return dto;
			}).collect(Collectors.toList()));

			if(size != pageSize){
				response.setNextPageAnchor(null);
			}else{
				response.setNextPageAnchor(verifications.get(size-1).getCreateTime().getTime());
			}
		}

		return response;
	}

	@Override
	public ParkingCarVerificationDTO getParkingCarVerificationById(GetParkingCarVerificationByIdCommand cmd) {
//		checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		ParkingCarVerification verification = parkingProvider.findParkingCarVerificationById(cmd.getId());

		ParkingCarVerificationDTO dto = ConvertHelper.convert(verification, ParkingCarVerificationDTO.class);

		List<ParkingAttachment> attachments = parkingProvider.listParkingAttachments(verification.getId(),
				ParkingAttachmentType.PARKING_CAR_VERIFICATION.getCode());

		List<ParkingAttachmentDTO> attachmentDtos = attachments.stream().map(r -> {
			ParkingAttachmentDTO attachmentDto = ConvertHelper.convert(r, ParkingAttachmentDTO.class);

			String contentUrl = getResourceUrlByUir(r.getContentUri(),
					EntityType.USER.getCode(), r.getCreatorUid());
			attachmentDto.setContentUrl(contentUrl);
			attachmentDto.setInformationType(r.getDataType());
			return attachmentDto;
		}).collect(Collectors.toList());

		dto.setAttachments(attachmentDtos);
		return dto;
	}

	@Override
	public ParkingCarVerificationDTO requestCarVerification(RequestCarVerificationCommand cmd) {
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		Long userId = UserContext.currentUserId();

		ParkingCarVerification verification = parkingProvider.findParkingCarVerificationByUserId(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getParkingLotId(), cmd.getPlateNumber(), userId);

		if (null != verification) {
			if (verification.getStatus() == ParkingCarVerificationStatus.UN_AUTHORIZED.getCode()
					&& ParkingCarVerificationType.AUTHORIZED.getCode() == cmd.getRequestType()) {
				if (parkingLot.getLockCarFlag() == ParkingConfigFlag.SUPPORT.getCode()){
					BeanUtils.copyProperties(cmd, verification);
					verification.setRequestorUid(userId);
					verification.setCreatorUid(userId);
					verification.setCreateTime(new Timestamp(System.currentTimeMillis()));
//				verification.setStatus(ParkingCarVerificationStatus.AUDITING.getCode());
					parkingProvider.updateParkingCarVerification(verification);

					String ownerType = FlowOwnerType.PARKING_CAR_VERIFICATION.getCode();
					Flow flow = flowService.getEnabledFlow(UserContext.getCurrentNamespaceId(), ParkingFlowConstant.PARKING_RECHARGE_MODULE,
							FlowModuleType.NO_MODULE.getCode(), parkingLot.getId(), ownerType);

					if(null == flow) {
						LOGGER.error("Enable flow not found, moduleId={}", ParkingFlowConstant.PARKING_RECHARGE_MODULE);
						throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_ENABLE_FLOW,
								"Enable flow not found.");
					}

					FlowCase flowCase = createFlowCase(verification, flow, UserContext.currentUserId());
					if (null != flowCase) {
						verification.setFlowCaseId(flowCase.getId());
//					parkingProvider.updateParkingCarVerification(verification);
					}
					addAttachments(cmd.getAttachments(), UserContext.currentUserId(), verification.getId(),
							ParkingAttachmentType.PARKING_CAR_VERIFICATION.getCode());
				}else {
					throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_UNUSUAL,
							"Not support AUTHORIZED request");
				}
			}else if (cmd.getRequestType() == ParkingCarVerificationType.IGNORE_REPEAT_UN_AUTHORIZED.getCode()) {
				//TODO:什么都不做 忽略重复添加（应产品要求：去认证时也会调此接口做添加操作，如果是同一车牌，忽略添加，因为后面可能会调申请认证
				// 还是调此接口）
			}else {
				LOGGER.error("PlateNumber has been add, cmd={}", cmd);
				throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_PLATE_REPEAT_ADD,
						"PlateNumber has been add");
			}
		}else {
			verification = ConvertHelper.convert(cmd, ParkingCarVerification.class);
			verification.setSourceType(ParkingCarVerificationSourceType.CAR_VERIFICATION.getCode());

			verification.setRequestorUid(userId);
			verification.setCreatorUid(userId);
			verification.setCreateTime(new Timestamp(System.currentTimeMillis()));

			if (ParkingCarVerificationType.AUTHORIZED.getCode() == cmd.getRequestType()) {
				if (parkingLot.getLockCarFlag() == ParkingConfigFlag.SUPPORT.getCode()) {
//				verification.setStatus(ParkingCarVerificationStatus.AUDITING.getCode());
					parkingProvider.createParkingCarVerification(verification);

					String ownerType = FlowOwnerType.PARKING_CAR_VERIFICATION.getCode();
					Flow flow = flowService.getEnabledFlow(UserContext.getCurrentNamespaceId(), ParkingFlowConstant.PARKING_RECHARGE_MODULE,
							FlowModuleType.NO_MODULE.getCode(), parkingLot.getId(), ownerType);

					if(null == flow) {
						LOGGER.error("Enable flow not found, moduleId={}", ParkingFlowConstant.PARKING_RECHARGE_MODULE);
						throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_ENABLE_FLOW,
								"Enable flow not found.");
					}

					FlowCase flowCase = createFlowCase(verification, flow, UserContext.currentUserId());
					if (null != flowCase) {
						verification.setFlowCaseId(flowCase.getId());
//					parkingProvider.updateParkingCarVerification(verification);
					}
					addAttachments(cmd.getAttachments(), UserContext.currentUserId(), verification.getId(),
							ParkingAttachmentType.PARKING_CAR_VERIFICATION.getCode());
				}else {
					throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_UNUSUAL,
							"Not support AUTHORIZED request");
				}
			}else {
				verification.setStatus(ParkingCarVerificationStatus.UN_AUTHORIZED.getCode());
				parkingProvider.createParkingCarVerification(verification);
			}
		}

		return ConvertHelper.convert(verification, ParkingCarVerificationDTO.class);
	}

	@Override
	public void deleteCarVerification(DeleteCarVerificationCommand cmd) {
		checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

		ParkingCarVerification verification = parkingProvider.findParkingCarVerificationById(cmd.getId());

		verification.setStatus(ParkingCarVerificationStatus.INACTIVE.getCode());

		parkingProvider.updateParkingCarVerification(verification);

	}

	private FlowCase createFlowCase(ParkingCarVerification verification, Flow flow, Long userId) {

		CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
		createFlowCaseCommand.setApplyUserId(userId);
		createFlowCaseCommand.setFlowMainId(flow.getFlowMainId());
		createFlowCaseCommand.setFlowVersion(flow.getFlowVersion());
		createFlowCaseCommand.setReferId(verification.getId());
		createFlowCaseCommand.setReferType(EntityType.PARKING_CAR_VERIFICATION.getCode());
		createFlowCaseCommand.setContent("车牌号码：" + verification.getPlateNumber());
		createFlowCaseCommand.setCurrentOrganizationId(verification.getRequestorEnterpriseId());
		createFlowCaseCommand.setTitle("车辆认证申请");

		FlowCase flowCase = flowService.createFlowCase(createFlowCaseCommand);

		return flowCase;
	}

	@Override
	public ParkingSpaceDTO addParkingSpace(AddParkingSpaceCommand cmd) {
		ParkingSpaceStatus status = ParkingSpaceStatus.fromCode(cmd.getStatus());
		if(status==null){
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_SELF_DEINFE,
					"unknown status");
		}

		ParkingLot parkingLot = parkingProvider.findParkingLotById(cmd.getParkingLotId());

		ParkingSpace parkingSpace = parkingProvider.findParkingSpaceBySpaceNo(cmd.getSpaceNo());

		if (null != parkingSpace && parkingSpace.getStatus()!=ParkingSpaceStatus.DELETED.getCode()) {
			LOGGER.error("SpaceNo exist, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_REPEAT_SPACE_NO,
					"SpaceNo exist.");
		}

		parkingSpace = parkingProvider.findParkingSpaceByLockId(cmd.getLockId());

		if (null != parkingSpace  && parkingSpace.getStatus()!=ParkingSpaceStatus.DELETED.getCode()) {
			LOGGER.error("LockId exist, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_REPEAT_LOCK_ID,
					"LockId exist.");
		}
		ParkingHub hub = parkingHubProvider.findParkingHubById(cmd.getParkingHubsId());
		if(hub==null){
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_SELF_DEINFE,
					"无效的hub");
		}
		if(!dingDingParkingLockHandler.connParkingSpace(cmd.getLockId(),hub.getHubMac())){
			LOGGER.error("LockId conn failed, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_UNCONN_LOCK_ID,
					"您输入车锁ID无效，请重新输入");
		}

		parkingSpace = ConvertHelper.convert(cmd, ParkingSpace.class);

		parkingProvider.createParkingSpace(parkingSpace);
		RentalResourceHandler handler = rentalCommonService.getRentalResourceHandler(RentalV2ResourceType.VIP_PARKING.getCode());

		handler.updateRentalResource(JSONObject.toJSONString(parkingLot));
		return ConvertHelper.convert(parkingSpace, ParkingSpaceDTO.class);
	}

	@Override
	public ParkingSpaceDTO updateParkingSpace(UpdateParkingSpaceCommand cmd) {
		ParkingSpace parkingSpace = parkingProvider.findParkingSpaceById(cmd.getId());

		if (null == parkingSpace) {
			LOGGER.error("ParkingSpace not found, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_LOSE,
					"ParkingSpace not found.");
		}

		ParkingSpace parkingSpaceByLockId = parkingProvider.findParkingSpaceByLockId(cmd.getLockId());

		if (null != parkingSpaceByLockId && !cmd.getId().equals(parkingSpaceByLockId.getId())) {
			LOGGER.error("LockId exist, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_REPEAT_LOCK_ID,
					"LockId exist.");
		}

		ParkingHub hub = parkingHubProvider.findParkingHubById(cmd.getParkingHubsId());
		if(hub==null){
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_SELF_DEINFE,
					"无效的hub");
		}
		if(!dingDingParkingLockHandler.connParkingSpace(cmd.getLockId(),hub.getHubMac())){
			LOGGER.error("LockId conn failed, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_UNCONN_LOCK_ID,
					"您输入车锁ID无效，请重新输入");
		}

		parkingSpace.setSpaceAddress(cmd.getSpaceAddress());
		parkingSpace.setLockId(cmd.getLockId());

		parkingProvider.updateParkingSpace(parkingSpace);

		return ConvertHelper.convert(parkingSpace, ParkingSpaceDTO.class);
	}

	@Override
	public void updateParkingSpaceStatus(UpdateParkingSpaceStatusCommand cmd) {
		ParkingSpace parkingSpace = parkingProvider.findParkingSpaceById(cmd.getId());

		if (null == parkingSpace) {
			LOGGER.error("ParkingSpace not found, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_LOSE,
					"ParkingSpace not found.");
		}

		parkingSpace.setStatus(cmd.getStatus());

		parkingProvider.updateParkingSpace(parkingSpace);

	}

	@Override
	public void deleteParkingSpace(DeleteParkingSpaceCommand cmd) {
		ParkingSpace parkingSpace = parkingProvider.findParkingSpaceById(cmd.getId());

		if (null == parkingSpace) {
			LOGGER.error("ParkingSpace not found, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_LOSE,
					"ParkingSpace not found.");
		}

		if (parkingSpace.getStatus() == ParkingSpaceStatus.IN_USING.getCode()) {
			LOGGER.error("ParkingSpace in use, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_SPACE_IN_USE,
					"ParkingSpace in use.");
		}

		parkingSpace.setStatus(ParkingSpaceStatus.DELETED.getCode());
		parkingProvider.updateParkingSpace(parkingSpace);
	}

	@Override
	public SearchParkingSpacesResponse searchParkingSpaces(SearchParkingSpacesCommand cmd) {
//		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
//			//VIP车位管理权限
//			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4080040830L, cmd.getAppId(), null,cmd.getCurrentProjectId());
//		}
		SearchParkingSpacesResponse response = new SearchParkingSpacesResponse();

		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		List<ParkingSpace> spaces = parkingProvider.searchParkingSpaces(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getParkingLotId(), cmd.getKeyword(), cmd.getLockStatus(), cmd.getParkingHubsId(),cmd.getPageAnchor(), pageSize);

		int size = spaces.size();
		if(size > 0){
			response.setSpaceDTOS(spaces.stream().map(r -> {
				ParkingSpaceDTO dto = ConvertHelper.convert(r, ParkingSpaceDTO.class);
				if (dto.getStatus() == ParkingSpaceStatus.IN_USING.getCode()) {
					dto.setStatus(ParkingSpaceStatus.OPEN.getCode());
				}
				ParkingHub parkingHub = parkingHubProvider.findParkingHubById(r.getParkingHubsId());
				if(parkingHub!=null){
					dto.setHubName(parkingHub.getHubName());
				}
				return dto;
			}).collect(Collectors.toList()));

			if(size != pageSize){
				response.setNextPageAnchor(null);
			}else{
				response.setNextPageAnchor(spaces.get(size-1).getId());
			}
		}

		return response;
	}

	@Override
	public ListParkingSpaceLogsResponse listParkingSpaceLogs(ListParkingSpaceLogsCommand cmd) {
		ListParkingSpaceLogsResponse response = new ListParkingSpaceLogsResponse();

		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		List<ParkingSpaceLog> spaceLogs = parkingProvider.listParkingSpaceLogs(cmd.getSpaceNo(), cmd.getStartTime(),
				cmd.getEndTime(), cmd.getPageAnchor(), pageSize);

		int size = spaceLogs.size();
		if(size > 0){
			response.setLogDTOS(spaceLogs.stream().map(r -> {
				ParkingSpaceLogDTO dto = ConvertHelper.convert(r, ParkingSpaceLogDTO.class);
				return dto;
			}).collect(Collectors.toList()));

			if(size != pageSize){
				response.setNextPageAnchor(null);
			}else{
				response.setNextPageAnchor(spaceLogs.get(size-1).getOperateTime().getTime());
			}
		}

		return response;
	}

	@Override
	public ListParkingSpaceLogsResponse exportParkingSpaceLogs(ListParkingSpaceLogsCommand cmd,HttpServletResponse response) {
		cmd.setPageSize(1000);
		ListParkingSpaceLogsResponse resp =  listParkingSpaceLogs(cmd);

		List<ParkingSpaceLogDTO> requests = resp.getLogDTOS();

		Workbook wb = new XSSFWorkbook();

		Font font = wb.createFont();
		font.setFontName("黑体");
		font.setFontHeightInPoints((short) 16);
		CellStyle style = wb.createCellStyle();
		style.setFont(font);

		Sheet sheet = wb.createSheet("parkingSpaceLogDTOs");
		sheet.setDefaultColumnWidth(20);
		sheet.setDefaultRowHeightInPoints(20);
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("操作");
		row.createCell(1).setCellValue("操作时间");
		row.createCell(2).setCellValue("操作人");
		row.createCell(3).setCellValue("手机号");
		row.createCell(4).setCellValue("用户类型");
		row.createCell(5).setCellValue("公司名称");

		DateTimeFormatter datetimeSF = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		if (null != requests) {
			for(int i = 0, size = requests.size(); i < size; i++){
				Row tempRow = sheet.createRow(i + 1);
				ParkingSpaceLogDTO logDto = requests.get(i);

				tempRow.createCell(0).setCellValue(getOperateTypeChineseDesc(logDto.getOperateType()));
				tempRow.createCell(1).setCellValue(logDto.getOperateTime().toLocalDateTime().format(datetimeSF));
				tempRow.createCell(2).setCellValue(logDto.getContactName());
				tempRow.createCell(3).setCellValue(logDto.getContactPhone());
				ParkingSpaceLockOperateUserType enumOperateUserType = ParkingSpaceLockOperateUserType.fromCode(logDto.getUserType());
				tempRow.createCell(4).setCellValue(enumOperateUserType==null?"未知类型":enumOperateUserType.getDesc());
				tempRow.createCell(5).setCellValue(logDto.getContactEnterpriseName());
			}
		}

		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			wb.write(out);
			DownloadUtils.download(out, response);
		} catch (IOException e) {
			LOGGER.error("exportParkingCardRequests is fail. {}",e);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.FAIL_EXPORT_FILE,
					"exportParkingCardRequests is fail.");
		}
		return resp;

	}

	private String getOperateTypeChineseDesc(Byte operateType) {
		ParkingSpaceLockOperateType enumOperateType = ParkingSpaceLockOperateType.fromCode(operateType);
		if(enumOperateType == ParkingSpaceLockOperateType.UP){
			return "车锁升起";
		}
		if(enumOperateType == ParkingSpaceLockOperateType.DOWN){
			return "车锁降下";
		}
		return "未知操作";
	}

	@Override
	public void raiseParkingSpaceLock(RaiseParkingSpaceLockCommand cmd) {

		handleParkingSpaceLock(cmd.getOrderId(), cmd.getLockId(), ParkingSpaceLockOperateUserType.RESERVE_PERSON,
				ParkingSpaceLockOperateType.UP);
	}

	private void handleParkingSpaceLock(Long orderId, String lockId, ParkingSpaceLockOperateUserType userType,
										ParkingSpaceLockOperateType operateType) {
		RentalOrder order = rentalv2Provider.findRentalBillById(orderId);

		if (order.getStatus() != SiteBillStatus.IN_USING.getCode()) {
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE,ParkingErrorCode.PARAMTER_LOSE,
					"Invalid parameter");
		}

		boolean flag;

		if (operateType == ParkingSpaceLockOperateType.UP) {
			flag = dingDingParkingLockHandler.raiseParkingSpaceLock(lockId);
		}else {
			flag = dingDingParkingLockHandler.downParkingSpaceLock(lockId);
		}

		//TODO:
		if (flag) {
			ParkingSpace space = parkingProvider.findParkingSpaceByLockId(lockId);
			space.setLockStatus(operateType.getStatus());
			ParkingSpaceLog log;
			if (userType == ParkingSpaceLockOperateUserType.RESERVE_PERSON) {
				log = buildParkingSpaceLog(space, order);
			}else {
				log = buildParkingSpaceLogForWeb(space, order);
			}
			log.setOperateType(operateType.getCode());

			parkingProvider.createParkingSpaceLog(log);
			parkingProvider.updateParkingSpace(space);
		}else {
			if (operateType == ParkingSpaceLockOperateType.UP) {
				throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE,ParkingErrorCode.ERROR_RAISE_PARKING_LOCK,
						"Raise parking lock failed");
			}else {
				throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE,ParkingErrorCode.ERROR_DOWN_PARKING_LOCK,
						"Down parking lock failed");
			}
		}
	}

	private ParkingSpaceLog buildParkingSpaceLog(ParkingSpace space, RentalOrder order) {
		ParkingSpaceLog log = new ParkingSpaceLog();

		log.setLockId(space.getLockId());
		log.setSpaceNo(space.getSpaceNo());
		log.setContactPhone(order.getUserPhone());
		log.setContactName(order.getUserName());
		log.setContactEnterpriseName(order.getUserEnterpriseName());
		log.setUserType(ParkingSpaceLockOperateUserType.RESERVE_PERSON.getCode());
		log.setOperateTime(new Timestamp(System.currentTimeMillis()));

		return log;
	}

	private ParkingSpaceLog buildParkingSpaceLogForWeb(ParkingSpace space, RentalOrder order) {
		String customJson = order.getCustomObject();
		VipParkingUseInfoDTO useInfoDTO = JSONObject.parseObject(customJson, VipParkingUseInfoDTO.class);

		ParkingSpaceLog log = new ParkingSpaceLog();

		log.setLockId(space.getLockId());
		log.setSpaceNo(space.getSpaceNo());
		log.setContactPhone(useInfoDTO.getPlateOwnerPhone());
		log.setContactName(useInfoDTO.getPlateOwnerName());
		log.setContactEnterpriseName(useInfoDTO.getPlateOwnerEnterpriseName());
		log.setUserType(ParkingSpaceLockOperateUserType.PLATE_OWNER.getCode());
		log.setOperateTime(new Timestamp(System.currentTimeMillis()));

		return log;
	}

	@Override
	public void downParkingSpaceLock(DownParkingSpaceLockCommand cmd) {

		handleParkingSpaceLock(cmd.getOrderId(), cmd.getLockId(), ParkingSpaceLockOperateUserType.RESERVE_PERSON,
				ParkingSpaceLockOperateType.DOWN);
	}

	@Override
	public void raiseParkingSpaceLockForWeb(RaiseParkingSpaceLockCommand cmd) {

		handleParkingSpaceLock(cmd.getOrderId(), cmd.getLockId(), ParkingSpaceLockOperateUserType.PLATE_OWNER,
				ParkingSpaceLockOperateType.UP);
	}

	@Override
	public void downParkingSpaceLockForWeb(DownParkingSpaceLockCommand cmd) {
		handleParkingSpaceLock(cmd.getOrderId(), cmd.getLockId(), ParkingSpaceLockOperateUserType.PLATE_OWNER,
				ParkingSpaceLockOperateType.DOWN);
	}

	@Override
	public void refreshToken(RefreshTokenCommand cmd) {
		if(cmd.getParkingLotId()==null){
			for (ParkingVendorHandler listener : allListeners) {
				try {
					listener.refreshToken();
				}catch (Exception e){
					LOGGER.error("listener {}, exception = ", listener.getClass().getSimpleName(),e);
				}
			}
		}else {
			ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());

			String vendorName = parkingLot.getVendorName();
			ParkingVendorHandler handler = getParkingVendorHandler(vendorName);
			handler.refreshToken();
		}
	}

	@Override
	public List<ListBizPayeeAccountDTO> listPayeeAccount(ListPayeeAccountCommand cmd) {
		checkOwner(cmd.getOwnerType(),cmd.getCommunityId());
		ArrayList arrayList = new ArrayList(Arrays.asList("0", cmd.getCommunityId() + ""));
		String key = OwnerType.ORGANIZATION.getCode() + cmd.getOrganizationId();
		LOGGER.info("sdkPayService request params:{} {} ",key,arrayList);
		GetPayUserListByMerchantCommand cmd2 = new GetPayUserListByMerchantCommand();
		cmd2.setUserId(key);
		cmd2.setTag1(arrayList);
		GetMerchantListByPayUserIdRestResponse resp = payServiceV2.getMerchantListByPayUserId(cmd2);
		//List<PayUserDTO> payUserList = sdkPayService.getPayUserList(key,arrayList);
		if(null == resp || null == resp.getResponse()){
			LOGGER.error("resp:"+(null == resp ? null :StringHelper.toJsonString(resp)));
		}
		List<ListBizPayeeAccountDTO> payUserList = new ArrayList<ListBizPayeeAccountDTO>();
		if(resp != null && resp.getErrorCode() != null && resp.getErrorCode().equals(HttpStatus.OK.value())){
			List<GetPayUserListByMerchantDTO> merchantDTOS = resp.getResponse();
            for (GetPayUserListByMerchantDTO merchantDTO : merchantDTOS) {
                PayUserDTO payUserDTO = ConvertHelper.convert(merchantDTO,PayUserDTO.class);
                ListBizPayeeAccountDTO dto = new ListBizPayeeAccountDTO();
				dto.setAccountId(payUserDTO.getId());
				dto.setAccountType(payUserDTO.getUserType()==2?OwnerType.ORGANIZATION.getCode():OwnerType.USER.getCode());//帐号类型，1-个人帐号、2-企业帐号
				dto.setAccountName(payUserDTO.getRemark());
				dto.setAccountAliasName(payUserDTO.getUserAliasName());
		        if (payUserDTO.getRegisterStatus() != null && payUserDTO.getRegisterStatus().intValue() == 1) {
		            dto.setAccountStatus(PaymentUserStatus.ACTIVE.getCode());
		        } else {
		            dto.setAccountStatus(PaymentUserStatus.WAITING_FOR_APPROVAL.getCode());
		        }
                payUserList.add(dto);
            }
		}
		return payUserList;
	}

	@Override
	public void createOrUpdateBusinessPayeeAccount(CreateOrUpdateBusinessPayeeAccountCommand cmd) {
		checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
		List<ParkingBusinessPayeeAccount> accounts = parkingBusinessPayeeAccountProvider.findRepeatParkingBusinessPayeeAccounts
				(cmd.getId(),cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),cmd.getParkingLotId(),cmd.getBusinessType());
		if(accounts!=null && accounts.size()>0){
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_REPEATE_ACCOUNT,
					"repeat account");
		}
		if(cmd.getId()!=null){
			ParkingBusinessPayeeAccount oldPayeeAccount = parkingBusinessPayeeAccountProvider.findParkingBusinessPayeeAccountById(cmd.getId());
			if(oldPayeeAccount == null){
				throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_LOSE,
						"unknown payaccountid = "+cmd.getId());
			}
			ParkingBusinessPayeeAccount newPayeeAccount = ConvertHelper.convert(cmd,ParkingBusinessPayeeAccount.class);
			newPayeeAccount.setCreateTime(oldPayeeAccount.getCreateTime());
			newPayeeAccount.setCreatorUid(oldPayeeAccount.getCreatorUid());
			newPayeeAccount.setNamespaceId(oldPayeeAccount.getNamespaceId());
			newPayeeAccount.setOwnerType(oldPayeeAccount.getOwnerType());
			newPayeeAccount.setOwnerId(oldPayeeAccount.getOwnerId());
			newPayeeAccount.setMerchantId(cmd.getPayeeId());
			newPayeeAccount.setPayeeId(cmd.getPayeeId());
			parkingBusinessPayeeAccountProvider.updateParkingBusinessPayeeAccount(newPayeeAccount);
		}else{
			//ParkingBusinessPayeeAccount newPayeeAccount = ConvertHelper.convert(cmd,ParkingBusinessPayeeAccount.class);
			ParkingBusinessPayeeAccount newPayeeAccount = new ParkingBusinessPayeeAccount();
			newPayeeAccount.setMerchantId(cmd.getPayeeId());
			newPayeeAccount.setBusinessType(cmd.getBusinessType());
			newPayeeAccount.setNamespaceId(cmd.getNamespaceId());
			newPayeeAccount.setOwnerId(cmd.getOwnerId());
			newPayeeAccount.setOwnerType(cmd.getOwnerType());
			newPayeeAccount.setParkingLotId(cmd.getParkingLotId());
			newPayeeAccount.setParkingLotName(cmd.getParkingLotName());
			newPayeeAccount.setPayeeUserType(cmd.getPayeeUserType());
			newPayeeAccount.setStatus((byte)2);
			newPayeeAccount.setPayeeId(cmd.getPayeeId());
			
			parkingBusinessPayeeAccountProvider.createParkingBusinessPayeeAccount(newPayeeAccount);
		}
	}

	@Override
	public ListBusinessPayeeAccountResponse listBusinessPayeeAccount(ListBusinessPayeeAccountCommand cmd) {
		checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
		List<ParkingBusinessPayeeAccount> accounts = parkingBusinessPayeeAccountProvider
				.listParkingBusinessPayeeAccountByOwner(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),cmd.getParkingLotId(),cmd.getBusinessType());
		if(accounts==null || accounts.size()==0){
			return new ListBusinessPayeeAccountResponse();
		}
		
		ListPayUsersByMerchantIdsCommand cmd2 = new ListPayUsersByMerchantIdsCommand();
        cmd2.setIds(accounts.stream().map(r -> r.getMerchantId()).collect(Collectors.toList()));
        ListPayUsersByMerchantIdsRestResponse restResponse = orderService.listPayUsersByMerchantIds(cmd2);
        List<PayUserDTO> payUserDTOs = restResponse.getResponse();
//		List<PayUserDTO> payUserDTOS = sdkPayService.listPayUsersByIds(accounts.stream().map(r -> r.getPayeeId()).collect(Collectors.toList()));
		Map<Long,PayUserDTO> map = payUserDTOs.stream().collect(Collectors.toMap(PayUserDTO::getId,r->r));
		ListBusinessPayeeAccountResponse response = new ListBusinessPayeeAccountResponse();
		response.setAccountList(accounts.stream().map(r->{
			BusinessPayeeAccountDTO convert = ConvertHelper.convert(r, BusinessPayeeAccountDTO.class);
			PayUserDTO payUserDTO = map.get(convert.getPayeeId());
			if(payUserDTO!=null){
				convert.setPayeeUserType(payUserDTO.getUserType()==2?OwnerType.ORGANIZATION.getCode():OwnerType.USER.getCode());
				convert.setPayeeUserName(payUserDTO.getRemark());
				convert.setPayeeUserAliasName(payUserDTO.getUserAliasName());
				convert.setPayeeAccountCode(payUserDTO.getAccountCode());
				convert.setPayeeRegisterStatus(payUserDTO.getRegisterStatus()+1);//由，0非认证，1认证，变为 1，非认证，2，审核通过
				convert.setPayeeRemark(payUserDTO.getRemark());
				convert.setMerchantId(payUserDTO.getId());
			}
			return convert;
		}).collect(Collectors.toList()));
		return response;
	}
	public SearchParkingHubsResponse searchParkingHubs(SearchParkingHubsCommand cmd) {
		cmd.setPageSize(PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize()));
		List<ParkingHub> parkingHubs = parkingHubProvider.listParkingHub(cmd.getNamespaceId(), cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getParkingLotId(), cmd.getPageAnchor(), cmd.getPageSize());
		if(parkingHubs==null || parkingHubs.size()==0) {
			return null;
		}
		SearchParkingHubsResponse response = new SearchParkingHubsResponse();
		response.setHubList(parkingHubs.stream().map(r -> {
			ParkingHubDTO convert = ConvertHelper.convert(r, ParkingHubDTO.class);
			List<ParkingSpace> parkingSpaces = parkingProvider.listParkingSpaceByParkingHubsId(r.getNamespaceId(), r.getOwnerType(),
					r.getOwnerId(), r.getParkingLotId(), r.getId());
			if(parkingSpaces!=null && parkingSpaces.size()>0){
				convert.setRelatedSpaceNos(parkingSpaces.stream().map(space->space.getSpaceNo()).collect(Collectors.toList()));
			}
			return convert;
		}).collect(Collectors.toList()));
		if(parkingHubs.size()==cmd.getPageSize()){
			response.setNextPageAnchor(parkingHubs.get(cmd.getPageSize()-1).getId());
		}
		return response;
	}

	@Override
	public void delBusinessPayeeAccount(CreateOrUpdateBusinessPayeeAccountCommand cmd) {
		parkingBusinessPayeeAccountProvider.deleteParkingBusinessPayeeAccount(cmd.getId());
	}
	@Override
	public void initPayeeAccount(MultipartFile[] files) {}


	public ParkingHubDTO createOrUpdateParkingHub(CreateOrUpdateParkingHubCommand cmd) {
		if(cmd.getId()==null){
			ParkingHub oldHub = parkingHubProvider.findParkingHubByMac(cmd.getNamespaceId(),cmd.gethubMac());
			if(oldHub!=null){
				throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE,ParkingErrorCode.ERROR_SELF_DEINFE,
						"设备ID已存在，不可重复添加");
			}
			ParkingHub parkingHub = ConvertHelper.convert(cmd, ParkingHub.class);
			parkingHub.setStatus((byte)2);
			parkingHubProvider.createParkingHub(parkingHub);
			return ConvertHelper.convert(parkingHub,ParkingHubDTO.class);
		}else{
			ParkingHub parkingHub = parkingHubProvider.findParkingHubById(cmd.getId());
			if(parkingHub==null){
				throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE,ParkingErrorCode.ERROR_SELF_DEINFE,
						"非法的hubId");
			}
			parkingHub.setHubName(cmd.getHubName());
			parkingHubProvider.updateParkingHub(parkingHub);
			return ConvertHelper.convert(parkingHub,ParkingHubDTO.class);
		}
	}

	@Override
	public void notifyParkingRechargeOrderPaymentV2(MerchantPaymentNotificationCommand cmd) {
		parkingOrderEmbeddedV2Handler.payCallBack(cmd);
	}

	private ParkingOwnerType checkOwner(String ownerType, Long ownerId) {
		ParkingOwnerType enumOwnerType = checkOwnerType(ownerType);
		switch (enumOwnerType){
			case COMMUNITY:
				Community community = communityProvider.findCommunityById(ownerId);
				if(community==null){
					throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_LOSE,
							"unknown ownerId "+ownerId);
				}
				break;
		}
		return enumOwnerType;

	}

	/**
	 * 检查所属枚举
	 * @param ownerType
	 * @return
	 */
	private ParkingOwnerType checkOwnerType(String ownerType) {
		ParkingOwnerType enumOwnerType = ParkingOwnerType.fromCode(ownerType);
		if(enumOwnerType==null){
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.PARAMTER_LOSE,
					"unknown ownerType "+ownerType);
		}
		return enumOwnerType;
	}

//	public List<PayMethodDTO> getPayMethods(String paymentStatusQueryUrl) {
//		List<PayMethodDTO> payMethods = new ArrayList<>();
//		String format = "{\"getOrderInfoUrl\":\"%s\"}";
//		PayMethodDTO alipay = new PayMethodDTO();
//		alipay.setPaymentName("支付宝支付");
//		PaymentParamsDTO alipayParamsDTO = new PaymentParamsDTO();
//		alipayParamsDTO.setPayType("A01");
//		alipay.setExtendInfo(String.format(format, paymentStatusQueryUrl));
//		String url = contentServerService.parserUri("cs://1/image/aW1hZ2UvTVRveVpEWTNPV0kwWlRJMU0yRTFNakJtWkRCalpETTVaalUzTkdaaFltRmtOZw");
//		alipay.setPaymentLogo(url);
//		alipay.setPaymentParams(alipayParamsDTO);
//		alipay.setPaymentType(8);
//		payMethods.add(alipay);
//
//		PayMethodDTO wxpay = new PayMethodDTO();
//		wxpay.setPaymentName("微信支付");
//		wxpay.setExtendInfo(String.format(format, paymentStatusQueryUrl));
//		url = contentServerService.parserUri("cs://1/image/aW1hZ2UvTVRveU1UUmtaRFExTTJSbFpETXpORE5rTjJNME9Ua3dOVFkxTVRNek1HWXpOZw");
//		wxpay.setPaymentLogo(url);
//		PaymentParamsDTO wxParamsDTO = new PaymentParamsDTO();
//		wxParamsDTO.setPayType("no_credit");
//		wxpay.setPaymentParams(wxParamsDTO);
//		wxpay.setPaymentType(1);
//
//		payMethods.add(wxpay);
//		return payMethods;
//	}

	private String generateBizOrderNum(String sNamespaceId, String pyCode, Long orderNo) {
		return sNamespaceId+BIZ_ORDER_NUM_SPILT+pyCode+BIZ_ORDER_NUM_SPILT+orderNo;
	}

	@Override
	public void rechargeOrderMigration() {
		Long pageAnchor = null;
		Integer pageSize = 100;
		boolean hasNext = true;
		while (hasNext) {
			List<PaymentOrderRecord> records = parkingProvider.listParkingPaymentOrderRecords(pageAnchor,pageSize);
			for (PaymentOrderRecord record : records) {
				ParkingRechargeOrder parkingOrder = parkingProvider.findParkingRechargeOrderById(record.getOrderId());
				if (parkingOrder == null) {
					continue;
				}
				parkingOrder.setPayOrderNo(record.getPaymentOrderId() + "");
				parkingProvider.updateParkingRechargeOrder(parkingOrder);
			}
			if(records.size()<pageSize){
				hasNext = false;
			}
			else
			{
				pageAnchor=records.get(records.size()-1).getId();
			}
		}
	}


	public void deleteParkingHub(DeleteParkingHubCommand cmd) {
		ParkingHub parkingHub = parkingHubProvider.findParkingHubById(cmd.getId());
		if(parkingHub!=null) {
			List<ParkingSpace> parkingSpaces = parkingProvider.listParkingSpaceByParkingHubsId(parkingHub.getNamespaceId(), parkingHub.getOwnerType(),
					parkingHub.getOwnerId(), parkingHub.getParkingLotId(), parkingHub.getId());
			if(parkingSpaces!=null && parkingSpaces.size()>0){
				throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE,ParkingErrorCode.ERROR_HUB_IN_USE,
						"\n" +
								"无法删除\n" +
								"\n" +
								"此HUB设备已关联车位，无法进行删除，你可以先前往 车位管理 删除相关车位");
			}
			parkingHub.setStatus((byte) 0);
			parkingHubProvider.updateParkingHub(parkingHub);
		}
	}

	@Override
	public GetParkingSpaceLockFullStatusDTO getParkingSpaceLockFullStatus(DeleteParkingSpaceCommand cmd) {
		GetParkingSpaceLockFullStatusDTO fullStatusDTO = dingDingParkingLockHandler.readParkingSpaceLock(cmd.getId());
		return fullStatusDTO;
	}
	@Override
	public ParkingLotDTO getParkingLotByToken(GetParkingLotByTokenCommand cmd) {
		int tokenlen = configProvider.getIntValue("parking.token.maxlen", 20);
		List<ParkingLot> list = parkingProvider.findParkingLotByIdHash(cmd.getParkingLotToken());
		if(list==null || list.size()==0 || list.size()>1){
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_SELF_DEFINE,
					"非法parkingLotToken");
		}
		ParkingLot r = list.get(0);
//		User user = UserContext.current().getUser();
		ParkingLotDTO dto = ConvertHelper.convert(r, ParkingLotDTO.class);

		if (r.getVipParkingFlag() == ParkingConfigFlag.SUPPORT.getCode()) {
			String homeUrl = configProvider.getValue(ConfigConstants.HOME_URL, "");
			String detailUrl = configProvider.getValue(ConfigConstants.RENTAL_ORDER_DETAIL_URL, "");

			RentalResourceType type = rentalv2Provider.findRentalResourceTypes(UserContext.getCurrentNamespaceId(),
					RentalV2ResourceType.VIP_PARKING.getCode());

			detailUrl = String.format(detailUrl, RentalV2ResourceType.VIP_PARKING.getCode(), type.getId(),
					RuleSourceType.RESOURCE.getCode(), dto.getId());
			dto.setVipParkingUrl(homeUrl + detailUrl);
		}
		if (r.getDefaultData() != null && r.getDefaultData().length() >0){
			dto.setData(Arrays.asList(r.getDefaultData().split(",")));
		}
		if (r.getDefaultPlate() != null && r.getDefaultPlate().length() >0){
			String[] plate = r.getDefaultPlate().split(",");
			dto.setProvince(plate[0]);
			dto.setCity(plate[1]);
		}
		dto.setFlowId(null);
		dto.setFlowMode(ParkingRequestFlowType.FORBIDDEN.getCode());
		if(ParkingConfigFlag.fromCode(r.getMonthCardFlag()) == ParkingConfigFlag.SUPPORT) {
			Flow flow = flowService.getEnabledFlow(UserContext.getCurrentNamespaceId(), ParkingFlowConstant.PARKING_RECHARGE_MODULE,
					FlowModuleType.NO_MODULE.getCode(), r.getId(), FlowOwnerType.PARKING.getCode());
			//当没有设置工作流的时候，表示是禁用模式
			if (flow!=null){
				//模式由发布的时候决定，这里不根据工作流的名称和stringTag1字段决定。
				// 如果没有工作流或者工作流主版本不匹配，则不启用月卡申请模式
//					String tag1 = flow.getStringTag1();
//					Integer flowMode = Integer.valueOf(tag1);
				dto.setFlowMode(r.getFlowMode());
				dto.setFlowId(flow.getId());
				LOGGER.info("parking enabled flow, flow={}", flow);
				Flow mainFlow = flowProvider.getFlowById(flow.getFlowMainId());
				LOGGER.info("parking main flow, flow={}", mainFlow);
				//当获取到的工作流与 main工作流中的版本不一致时，表示获取到的工作流不是编辑后最新的工作流（工作流没有启用），是禁用模式
//				if (null != mainFlow && mainFlow.getFlowVersion().intValue() != flow.getFlowVersion().intValue()) {
//					dto.setFlowMode(ParkingRequestFlowType.FORBIDDEN.getCode());
//					dto.setFlowId(null);
//				}
			}
		}
		return dto;

	}

	@Override
	public String transformToken(TransformTokenCommand cmd) {
		return transformToken(cmd.getId());
	}

	public String transformToken(String strId) {
		Long id = null;
		try {
			id = Long.valueOf(strId);
		}catch (Exception e){
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_SELF_DEFINE,
					"no long");
		}
		String token = WebTokenGenerator.getInstance().toWebToken(strId);
		int tokenlen = configProvider.getIntValue("parking.token.maxlen", 20);
		if(token!=null && token.length()>tokenlen){
			token = token.substring(0,tokenlen);
		}
		ParkingLot parkinglot = parkingProvider.findParkingLotById(id);
		if(parkinglot==null){
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_SELF_DEFINE,
					"非法停车场id");
		}
		parkinglot.setIdHash(token);
		parkingProvider.updateParkingLot(parkinglot);

		List<ParkingLot> parkingLotByIdHash = parkingProvider.findParkingLotByIdHash(token);
		if(parkingLotByIdHash == null || parkingLotByIdHash.size()==0 || parkingLotByIdHash.size()>1){
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_SELF_DEFINE,
					"hash过短");
		}
		return token;
	}

	@Override
	public void getWxParkingQrcode(GetWxParkingQrcodeCommand cmd, HttpServletResponse resp) {
		String parkingUrlFormat = configProvider.getValue("parking.wxparking.urlformat","%s/pp/b/a.html?token=%s&ns=%s#/home");
		String parkingUrl = String.format(parkingUrlFormat, configProvider.getValue("home.url",""),transformToken(cmd.getParkingLotId()+""),UserContext.getCurrentNamespaceId());
		BufferedOutputStream bos = null;
		ByteArrayOutputStream out = null;
		try {
			Integer width = configProvider.getIntValue("parking.wxparking.qrcodewidth",300);
			Integer height = configProvider.getIntValue("parking.wxparking.qrcodeheight",300);
			BufferedImage image = QRCodeEncoder.createQrCodeWithOutWhite(parkingUrl, QRCodeConfig.CHARSET_UTF8,
					width, height, null,QRCodeConfig.FORMAT_PNG,QRCodeConfig.BLACK,0xEEFFFFFF);
			out = new ByteArrayOutputStream();
			ImageIO.write(image, QRCodeConfig.FORMAT_PNG, out);

			String fileName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + "." + QRCodeConfig.FORMAT_PNG;
			resp.setContentType("application/octet-stream;");
			resp.setHeader("Content-disposition", "attachment; filename="
					+ new String(fileName.getBytes("utf-8"), "ISO8859-1"));
			resp.setHeader("Content-Length", String.valueOf(out.size()));

			bos = new BufferedOutputStream(resp.getOutputStream());
			ImageIO.write(image, QRCodeConfig.FORMAT_PNG, bos);
		} catch (Exception e) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Failed to download the package file");
		} finally {
			FileHelper.closeOuputStream(out);
			FileHelper.closeOuputStream(bos);
		}

	}

	@Override
	public GetParkingBussnessStatusResponse getParkingBussnessStatus(GetParkingBussnessStatusCommand cmd) {
		GetParkingBussnessStatusResponse response  = new GetParkingBussnessStatusResponse();
		List<ParkingFuncDTO> dockingFuncLists = new ArrayList<>();
		List<ParkingFuncDTO> funcLists = new ArrayList<>();
		ArrayList<Byte> flowModeList = new ArrayList<>(Arrays.asList((byte) 1, (byte) 2));
		ParkingLot lot = parkingProvider.findParkingLotById(cmd.getParkingLotId());
		if(lot.getFuncList()!=null && lot.getFuncList().contains("[")){
			JSONArray array = JSONObject.parseArray(lot.getFuncList());
			for (Object o : array) {
				//暂时如此
				ParkingFuncDTO dto = new ParkingFuncDTO();
				if (ParkingBusinessType.VIP_PARKING.getCode().equals(o.toString())) {
					dto.setCode(ParkingBusinessType.VIP_PARKING.getCode());
					dto.setEnableFlag(lot.getVipParkingFlag());
					funcLists.add(dto);
					continue;
				} else if (ParkingBusinessType.USER_NOTICE.getCode().equals(o.toString())){
					dto.setCode(ParkingBusinessType.USER_NOTICE.getCode());
					dto.setEnableFlag(lot.getNoticeFlag());
					funcLists.add(dto);
					continue;
				} else if (ParkingBusinessType.INVOICE_APPLY.getCode().equals(o.toString())){
					dto.setCode(ParkingBusinessType.INVOICE_APPLY.getCode());
					dto.setEnableFlag(lot.getInvoiceFlag());
					funcLists.add(dto);
					continue;
				}
				dto.setCode(o.toString());
				dto.setEnableFlag(ParkingConfigFlag.NOTSUPPORT.getCode());
				try {
					Method method = ParkingLot.class.getMethod("get"+o.toString().substring(0,1).toUpperCase()+o.toString().substring(1) + "Flag");
					Byte enbaleFlag = (Byte)method.invoke(lot);
					dto.setEnableFlag(enbaleFlag);
				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
					ParkingBusinessType parkingBusinessType = ParkingBusinessType.fromCode(o.toString());
					ParkingCurrentInfoType parkingCurrentInfoType = ParkingCurrentInfoType.fromCode(lot.getCurrentInfoType());
					if(parkingBusinessType == ParkingBusinessType.CAR_NUM && parkingCurrentInfoType == ParkingCurrentInfoType.CAR_NUM){
						dto.setEnableFlag(ParkingConfigFlag.SUPPORT.getCode());
					}
					if(parkingBusinessType == ParkingBusinessType.FREE_PLACE && parkingCurrentInfoType == ParkingCurrentInfoType.FREE_PLACE){
						dto.setEnableFlag(ParkingConfigFlag.SUPPORT.getCode());
					}
					if(parkingBusinessType == ParkingBusinessType.MONTH_CARD_APPLY){
						flowModeList.add(Byte.valueOf(ParkingRequestFlowType.INTELLIGENT.getCode()+""));
						break;
					}
				}
				dockingFuncLists.add(dto);
			}
		}
		response.setDockingFuncLists(dockingFuncLists);
		

//		ParkingFuncDTO dto = new ParkingFuncDTO();
//		dto.setCode(ParkingBusinessType.VIP_PARKING.getCode());
//		dto.setEnableFlag(lot.getVipParkingFlag());
//		funcLists.add(dto);
		response.setFuncLists(funcLists);

		response.setEnableMonthCard(lot.getMonthCardFlag());
		if(lot.getMonthCardFlag()!=null && ParkingConfigFlag.fromCode(lot.getMonthCardFlag()) == ParkingConfigFlag.SUPPORT) {
			response.setMonthCardFlow(Byte.valueOf(lot.getFlowMode() == null? (ParkingRequestFlowType.FORBIDDEN.getCode()+""):(lot.getFlowMode() + "")));
		}
		response.setDefaultData(lot.getDefaultData());
		response.setDefaultPlate(lot.getDefaultPlate());
		response.setFlowModeList(flowModeList);

		return response;
	}

	@Override
	public void initFuncLists(GetParkingBussnessStatusCommand cmd) {
		List<ParkingLot> parkingLots = parkingProvider.listParkingLots(null, null);
		if(parkingLots==null || parkingLots.size()==0){
			return;
		}
		for (ParkingLot parkingLot : parkingLots) {
			if(parkingLot.getFuncList()==null || parkingLot.getFuncList().trim().length()==0){
				JSONArray array = new JSONArray();
				array.add(ParkingBusinessType.VIP_PARKING.getCode());
				array.add(ParkingBusinessType.INVOICE_APPLY.getCode());
				array.add(ParkingBusinessType.USER_NOTICE.getCode());
				if(parkingLot.getTempfeeFlag()==1){
					array.add(ParkingBusinessType.TEMPFEE.getCode());
				}
				if(parkingLot.getMonthRechargeFlag()==1){
					array.add(ParkingBusinessType.MONTH_RECHARGE.getCode());
				}
				if(parkingLot.getSearchCarFlag()==1){
					array.add(ParkingBusinessType.SEARCH_CAR.getCode());
				}
				if(parkingLot.getLockCarFlag()==1){
					array.add(ParkingBusinessType.LOCK_CAR.getCode());
				}
				if(parkingLot.getCurrentInfoType()==1){
					array.add(ParkingBusinessType.CAR_NUM.getCode());
				}else if(parkingLot.getCurrentInfoType()==2){
					array.add(ParkingBusinessType.FREE_PLACE.getCode());
				}

				Community community = communityProvider.findCommunityById(parkingLot.getOwnerId());
				Flow flow = flowService.getEnabledFlow(community==null?parkingLot.getNamespaceId():community.getNamespaceId(), ParkingFlowConstant.PARKING_RECHARGE_MODULE,
						FlowModuleType.NO_MODULE.getCode(), parkingLot.getId(), FlowOwnerType.PARKING.getCode());
				//当没有设置工作流的时候，表示是禁用模式
				ParkingLotConfig parkingLotConfig = JSONObject.parseObject(parkingLot.getConfigJson(),new TypeReference<ParkingLotConfig>() {});
				if(null == flow) {
					parkingLotConfig.setMonthCardFlag(ParkingConfigFlag.NOTSUPPORT.getCode());
				}else {
					parkingLotConfig.setMonthCardFlag(ParkingConfigFlag.SUPPORT.getCode());
					String tag1 = flow.getStringTag1();
					ParkingRequestFlowType flowType = ParkingRequestFlowType.fromCode(Integer.valueOf(tag1));
					parkingLotConfig.setFlowMode(flowType.getCode());
					if(flowType==ParkingRequestFlowType.INTELLIGENT){
						array.add(ParkingBusinessType.MONTH_CARD_APPLY.getCode());
					}

					LOGGER.info("parking enabled flow, flow={}", flow);
					Flow mainFlow = flowProvider.getFlowById(flow.getFlowMainId());
					LOGGER.info("parking main flow, flow={}", mainFlow);
					//当获取到的工作流与 main工作流中的版本不一致时，表示获取到的工作流不是编辑后最新的工作流（工作流没有启用），是禁用模式
//					if (null != mainFlow) {
//						if (mainFlow.getFlowVersion().intValue() != flow.getFlowVersion().intValue()) {
//							parkingLotConfig.setMonthCardFlag(ParkingConfigFlag.SUPPORT.getCode());
//						}
//					}
				}
				parkingLot.setFuncList(array.toString());
				parkingLot.setConfigJson(parkingLotConfig.toString());
				parkingProvider.updateParkingLot(parkingLot);
			}
		}
	}
	@Override
	public void updateParkingUserNotice(UpdateUserNoticeCommand cmd) {
		ParkingLot parkingLot = checkParkingLot(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getParkingLotId());
		parkingLot.setId(parkingLot.getId());
		parkingLot.setSummary(cmd.getSummary());
		parkingLot.setNoticeContact(cmd.getContact());
		parkingProvider.updateParkingLot(parkingLot);
	}
	

	@Override
	public void notifyParkingRechargeOrderPayment(PayNotifyCommand cmd) {
		ParkingRechargeOrder order = parkingProvider.findParkingRechargeOrderById(cmd.getOrderId());
		//微信通知开关
		if (!configProvider.getBooleanValue(UserContext.getCurrentNamespaceId(),"parking.wechatNotify",false))
			return;
//		if (!(ParkingRechargeOrderStatus.UNPAID.getCode() == order.getStatus()))
//			return;
		String lockId = null;
		if (order.getGeneralOrderId() != null){
			lockId = String.valueOf(order.getGeneralOrderId());
		}else{
			lockId = order.getBizOrderNo();
		}
		this.coordinationProvider.getNamedLock(CoordinationLocks.PARKING_UPDATE_ORDER_STATUS.getCode() + lockId).enter(()-> {
			if (ParkingRechargeOrderStatus.UNPAID.getCode() == order.getStatus()){
				ParkingLot lot = parkingProvider.findParkingLotById(order.getParkingLotId());
				String vendorName = lot.getVendorName();
				ParkingVendorHandler handler = getParkingVendorHandler(vendorName);
				if (cmd.getPaymentType() != null && cmd.getPaymentType() == PaymentType.WECHAT_JS_PAY.getCode())
					order.setPaidType(VendorType.WEI_XIN.getCode());
				 else if (cmd.getPaymentType() != null && cmd.getPaymentType() == PaymentType.ALI_JS_PAY.getCode())
					order.setPaidType(VendorType.ZHI_FU_BAO.getCode());
				if (handler.notifyParkingRechargeOrderPayment(order)) {
					order.setStatus(ParkingRechargeOrderStatus.RECHARGED_NOTCALL.getCode());
					order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
					parkingProvider.updateParkingRechargeOrder(order);
					LOGGER.info("Notify parking recharge by wechat success, cmd={}, order={}", cmd, order);
				}else{
					//充值失败
					order.setStatus(ParkingRechargeOrderStatus.FAILED_NOTCALL.getCode());
					//充值失败时，将返回的错误信息记录下来
					if (StringUtils.isBlank(order.getErrorDescription())) {
						String locale = Locale.SIMPLIFIED_CHINESE.toString();
						String scope = ParkingErrorCode.SCOPE;
						String code = String.valueOf(ParkingErrorCode.ERROR_RECHARGE_ORDER);
						String defaultText = localeService.getLocalizedString(scope, code, locale, "");
						order.setErrorDescription(defaultText);
					}
					parkingProvider.updateParkingRechargeOrder(order);
				}
			}
			return null;
		});
	}
	
	@Override
	public GetInvoiceUrlResponse getInvoiceUrl (GetInvoiceUrlCommand cmd){
		String homeurl = configProvider.getValue("invoice.home.url", "");
		ParkingRechargeOrder order = parkingProvider.findParkingRechargeOrderById(cmd.getOrderId());
		String bizOrderNo = order.getBizOrderNo();
		GetInvoiceUrlResponse response = new GetInvoiceUrlResponse();
		String invoiceUrl = null;
		if (order.getInvoiceStatus() == null) {
			invoiceUrl = homeurl + "/promotion/app-invoice?businessOrderNumber=" + bizOrderNo + "#/invoice-application";
		} else if (order.getInvoiceStatus() == 0 ) {
			invoiceUrl = homeurl + "/promotion/app-invoice?businessOrderNumber=" + bizOrderNo + "#/invoice-application";
		} else {
			invoiceUrl = homeurl + "/promotion/app-invoice?businessOrderNumber=" + bizOrderNo + "#/invoice-detail/2";
		}
		response.setInvoiceUrl(invoiceUrl);
		return response;
	}
	
	boolean isReachMonthCardRequestMaxNum(String ownerType, Long ownerId, Long parkingLotId) {
		
		Long userId = UserContext.currentUserId();
		if (null == userId) {
			return false;
		}

		ParkingFlow parkingFlow = parkingProvider.getParkingRequestCardConfig(ownerType,
				ownerId, parkingLotId, null);

		List<ParkingCardRequest> requestList = parkingProvider.listParkingCardRequests(userId, ownerType,
				ownerId, parkingLotId, null, null,
				ParkingCardRequestStatus.INACTIVE.getCode(), null, null, null);

		int requestListSize = requestList.size();
		if(null != parkingFlow && parkingFlow.getMaxRequestNumFlag() == ParkingConfigFlag.SUPPORT.getCode()
				&& requestListSize >= parkingFlow.getMaxRequestNum()){
			return true;
		}
		
		return false;
	}
}
