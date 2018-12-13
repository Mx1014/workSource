package com.everhomes.officecubicle;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.alipay.api.domain.CardBinVO;
import com.everhomes.asset.PaymentConstants;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.CommunityService;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowService;
import com.everhomes.gorder.sdk.order.GeneralOrderService;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rentalv2.RentalCommonServiceImpl;
import com.everhomes.rentalv2.RentalOrder;
import com.everhomes.rentalv2.RentalResourceType;
import com.everhomes.rentalv2.Rentalv2PriceRule;
import com.everhomes.rentalv2.Rentalv2PriceRuleProvider;
import com.everhomes.rentalv2.Rentalv2Provider;
import com.everhomes.rentalv2.Rentalv2Service;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.community.ListCommunitiesByOrgIdAndAppIdCommand;
import com.everhomes.rest.community.ListCommunitiesByOrgIdAndAppIdResponse;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowReferType;
import com.everhomes.rest.general.order.GorderPayType;
import com.everhomes.rest.officecubicle.*;
import com.everhomes.rest.officecubicle.admin.*;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.OwnerType;
import com.everhomes.rest.order.PayMethodDTO;
import com.everhomes.rest.order.PaymentParamsDTO;
import com.everhomes.rest.order.PaymentUserStatus;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.organization.OrganizationCommunityDTO;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.promotion.merchant.GetPayUserByMerchantIdCommand;
import com.everhomes.rest.promotion.merchant.GetPayUserListByMerchantCommand;
import com.everhomes.rest.promotion.merchant.GetPayUserListByMerchantDTO;
import com.everhomes.rest.promotion.merchant.ListPayUsersByMerchantIdsCommand;
import com.everhomes.rest.promotion.merchant.controller.GetMerchantListByPayUserIdRestResponse;
import com.everhomes.rest.promotion.merchant.controller.GetPayerInfoByMerchantIdRestResponse;
import com.everhomes.rest.promotion.merchant.controller.ListPayUsersByMerchantIdsRestResponse;
import com.everhomes.rest.promotion.order.BusinessPayerType;
import com.everhomes.rest.promotion.order.CreatePurchaseOrderCommand;
import com.everhomes.rest.promotion.order.CreateRefundOrderCommand;
import com.everhomes.rest.promotion.order.MerchantPaymentNotificationCommand;
import com.everhomes.rest.promotion.order.PurchaseOrderCommandResponse;
import com.everhomes.rest.promotion.order.controller.CreatePurchaseOrderRestResponse;
import com.everhomes.rest.promotion.order.controller.CreateRefundOrderRestResponse;
import com.everhomes.rest.region.RegionAdminStatus;
import com.everhomes.rest.region.RegionScope;
import com.everhomes.rest.rentalv2.AddRentalOrderUsingInfoCommand;
import com.everhomes.rest.rentalv2.AddRentalOrderUsingInfoResponse;
import com.everhomes.rest.rentalv2.PriceRuleType;
import com.everhomes.rest.rentalv2.RentalBillDTO;
import com.everhomes.rest.rentalv2.RentalType;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.rest.rentalv2.RuleSourceType;
import com.everhomes.rest.rentalv2.SiteBillStatus;
import com.everhomes.rest.rentalv2.admin.GetRentalBillCommand;
import com.everhomes.rest.rentalv2.admin.PriceRuleDTO;
import com.everhomes.rest.rentalv2.admin.QueryDefaultRuleAdminCommand;
import com.everhomes.rest.rentalv2.admin.QueryDefaultRuleAdminResponse;
import com.everhomes.rest.rentalv2.admin.RentalDurationType;
import com.everhomes.rest.rentalv2.admin.RentalDurationUnit;
import com.everhomes.rest.rentalv2.admin.RentalOrderStrategy;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.util.*;


import org.apache.commons.lang.StringUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.messaging.MessagingService;
import com.everhomes.news.AttachmentProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.pay.order.CreateOrderCommand;
import com.everhomes.pay.order.OrderCommandResponse;
import com.everhomes.pay.order.PaymentType;
import com.everhomes.pay.order.SourceType;
import com.everhomes.paySDK.PaySettings;
import com.everhomes.paySDK.PayUtil;
import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.print.SiyinPrintNotificationTemplateCode;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.asset.TargetDTO;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.techpark.rental.RentalServiceErrorCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.techpark.onlinePay.OnlinePayService;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;

/**
 * 工位预定service实现
 * 
 * */
@Component
public class OfficeCubicleServiceImpl implements OfficeCubicleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OfficeCubicleController.class);

	@Autowired
	private MessagingService messagingService;

	SimpleDateFormat timeSF = new SimpleDateFormat("HH:mm:ss");
	SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final String BIZ_ACCOUNT_PRE = "NS";//账号前缀
	public static final String BIZ_ORDER_NUM_SPILT = "_";//业务订单分隔符
	@Value("${server.contextPath:}")
    private String contextPath;
	final String downloadDir = "\\download\\";
	@Autowired
	ContentServerService contentServerService;
	@Autowired
	private DbProvider dbProvider;
	@Autowired
	private OfficeCubicleProvider officeCubicleProvider;
	@Autowired
	private OfficeCubicleCityProvider officeCubicleCityProvider;
	@Autowired
	private OfficeCubicleSelectedCityProvider cubicleSelectedCityProvider;
	@Autowired
	private ConfigurationProvider configurationProvider;
	@Autowired
	private AttachmentProvider attachmentProvider;
	@Autowired
	private UserProvider userProvider;
	@Autowired
	private OfficeCubicleRangeProvider officeCubicleRangeProvider;
	@Autowired
	private FlowService flowService;

	@Autowired
	private RegionProvider regionProvider;
	
	@Autowired
	private CoordinationProvider coordinationProvider;

	@Autowired
	private UserPrivilegeMgr userPrivilegeMgr;

	@Autowired private CommunityProvider communityProvider;

	@Autowired
	public OfficeCubiclePayeeAccountProvider officeCubiclePayeeAccountProvider;
	@Autowired
	private ScheduleProvider scheduleProvider;
	@Autowired
	protected GeneralOrderService orderService;
	@Autowired
	public Rentalv2PriceRuleProvider rentalv2PriceRuleProvider;
	@Autowired
	private SmsProvider smsProvider;
	@Autowired
	private OrganizationProvider organizationProvider;
	@Autowired
	private CommunityService communityService;
	@Autowired
	private OnlinePayService onlinePayService;
	@Autowired
	private Rentalv2Service rentalv2Service;
	@Autowired
	private Rentalv2Provider rentalv2Provider;
	@Autowired
	private RentalCommonServiceImpl rentalCommonService;
	@Autowired
    private UserService userService;
	private Integer getNamespaceId(Integer namespaceId){
		if(namespaceId!=null){
			return namespaceId;
		}
		return UserContext.getCurrentNamespaceId();
	}

	@Override
	public SearchSpacesAdminResponse searchSpaces(SearchSpacesAdminCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4020040210L, cmd.getAppId(), null,cmd.getCurrentProjectId());//空间管理权限
		}
		SearchSpacesAdminResponse response = new SearchSpacesAdminResponse();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(Long.MAX_VALUE);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		List<OfficeCubicleSpace> spaces = this.officeCubicleProvider.searchSpaces(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getKeyWords(), locator, pageSize + 1,
				getNamespaceId(cmd.getNamespaceId()));
		if (null == spaces)
			return response;
		Long nextPageAnchor = null;
		if (spaces != null && spaces.size() > pageSize) {
			spaces.remove(spaces.size() - 1);
			nextPageAnchor = spaces.get(spaces.size() - 1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setSpaces(new ArrayList<OfficeSpaceDTO>());
		spaces.forEach((other) -> {
			OfficeSpaceDTO dto = convertSpaceDTO(other);
			response.getSpaces().add(dto);
		});

		return response;
	}

	/**
	 * 转换space为DTO
	 * */
	private OfficeSpaceDTO convertSpaceDTO(OfficeCubicleSpace other) {
		// dto需要对图片，attachments ，category 做特殊处理
		if (null == other) {
			OfficeSpaceDTO dto = new OfficeSpaceDTO();
			dto.setStatus(OfficeStatus.DELETED.getCode());
			return dto;
		}
		OfficeSpaceDTO dto = ConvertHelper.convert(other, OfficeSpaceDTO.class);
		if (null != other.getManagerUid()) {
			User manager = this.userProvider.findUserById(other.getManagerUid());
			if (null != manager) {
				dto.setManagerName(manager.getNickName());
				UserIdentifier identifier = this.userProvider.findClaimedIdentifierByOwnerAndType(other.getManagerUid(),
						IdentifierType.MOBILE.getCode());
				if (null != identifier)
					dto.setManagerPhone(identifier.getIdentifierToken());
			}
		}
		dto.setCoverUrl(this.contentServerService.parserUri(other.getCoverUri(), EntityType.USER.getCode(), UserContext.current()
				.getUser().getId()));

		//List<Attachment> attachments = this.attachmentProvider.listAttachmentByOwnerId(EhOfficeCubicleAttachments.class, dto.getId());
		List<OfficeCubicleAttachment> spaceAttachments = this.officeCubicleProvider.listAttachmentsBySpaceId(other.getId(),(byte)1);
		if (null != spaceAttachments){
//			dto.setAttachments(new ArrayList<OfficeAttachmentDTO>());
//			attachments.forEach((attachment) -> {
//				OfficeAttachmentDTO attachmentDTO = ConvertHelper.convert(attachment, OfficeAttachmentDTO.class);
//				attachmentDTO.setContentUrl(this.contentServerService.parserUri(attachment.getContentUri(), EntityType.USER.getCode(),
//						UserContext.current().getUser().getId()));
//				dto.getAttachments().add(attachmentDTO);
//			});
			dto.setSpaceAttachments(new ArrayList<OfficeAttachmentDTO>());
			spaceAttachments.forEach((attachment) -> {
			OfficeAttachmentDTO attachmentDTO = ConvertHelper.convert(attachment, OfficeAttachmentDTO.class);
			attachmentDTO.setContentUrl(this.contentServerService.parserUri(attachment.getContentUri(), EntityType.USER.getCode(),
					UserContext.current().getUser().getId()));
			dto.getSpaceAttachments().add(attachmentDTO);
			});
		}
		List<OfficeCubicleAttachment> shortRentAttachments = this.officeCubicleProvider.listAttachmentsBySpaceId(dto.getId(),(byte)2);
		if (null != shortRentAttachments){
			dto.setShortRentAttachments(new ArrayList<OfficeAttachmentDTO>());
			shortRentAttachments.forEach((attachment) -> {
			OfficeAttachmentDTO attachmentDTO = ConvertHelper.convert(attachment, OfficeAttachmentDTO.class);
			attachmentDTO.setContentUrl(this.contentServerService.parserUri(attachment.getContentUri(), EntityType.USER.getCode(),
					UserContext.current().getUser().getId()));
			dto.getShortRentAttachments().add(attachmentDTO);
			});
		}
		List<OfficeCubicleAttachment> stationAttachments = this.officeCubicleProvider.listAttachmentsBySpaceId(dto.getId(),(byte)3);
		if (null != stationAttachments){
			dto.setStationAttachments(new ArrayList<OfficeAttachmentDTO>());
			stationAttachments.forEach((attachment) -> {
			OfficeAttachmentDTO attachmentDTO = ConvertHelper.convert(attachment, OfficeAttachmentDTO.class);
			attachmentDTO.setContentUrl(this.contentServerService.parserUri(attachment.getContentUri(), EntityType.USER.getCode(),
					UserContext.current().getUser().getId()));
			dto.getStationAttachments().add(attachmentDTO);
			});
		}

		List<OfficeCubicleCategory> categories = this.officeCubicleProvider.queryCategoriesBySpaceId(dto.getId());
		dto.setAllPositionNums(0);
		if (null != categories){
			dto.setCategories(new ArrayList<OfficeCategoryDTO>());
			categories.forEach((category) -> {
				OfficeCategoryDTO categoryDTO = ConvertHelper.convert(category, OfficeCategoryDTO.class);
				categoryDTO.setSize(category.getSpaceSize());
				if(category.getPositionNums()!=null){
					dto.setAllPositionNums(dto.getAllPositionNums()+category.getPositionNums());
				}
				dto.getCategories().add(categoryDTO);
				if(dto.getMinUnitPrice()==null || (category.getUnitPrice()!=null 
						&& dto.getMinUnitPrice()!=null 
						&& category.getUnitPrice().doubleValue()<dto.getMinUnitPrice().doubleValue())
						){
					dto.setMinUnitPrice(category.getUnitPrice());
				}
			});
			Collections.sort(dto.getCategories(),new Comparator<OfficeCategoryDTO>(){
				public int compare(OfficeCategoryDTO s1, OfficeCategoryDTO s2) {
	                return s2.getSize() - s1.getSize();
	            }
			});	
		}
		List<OfficeCubicleChargeUser> chargeUsers = officeCubicleProvider.findChargeUserBySpaceId(other.getId());
		List<ChargeUserDTO> users = new ArrayList<ChargeUserDTO>();
		if(chargeUsers != null){
			for(OfficeCubicleChargeUser user : chargeUsers){
				ChargeUserDTO chargeUserDTO = new ChargeUserDTO();
				chargeUserDTO.setChargeName(user.getChargeName());
				chargeUserDTO.setChargeUid(user.getChargeUid());
				users.add(chargeUserDTO);
			}
			dto.setChargeUserDTO(users);
		}
		List<OfficeCubicleRange> ranges = officeCubicleRangeProvider.listRangesBySpaceId(dto.getId());
		dto.setRanges(ranges.stream().map(r->ConvertHelper.convert(r,OfficeRangeDTO.class)).collect(Collectors.toList()));
		QueryDefaultRuleAdminCommand cmd = new QueryDefaultRuleAdminCommand();
        RentalResourceType type = rentalv2Provider.findRentalResourceTypes(other.getNamespaceId(),
                RentalV2ResourceType.STATION_BOOKING.getCode());
		cmd.setResourceType(RentalV2ResourceType.STATION_BOOKING.getCode());
		cmd.setResourceTypeId(type.getId());
		cmd.setSourceId(other.getId());
		cmd.setSourceType(RuleSourceType.RESOURCE.getCode());
		QueryDefaultRuleAdminResponse resp = rentalv2Service.queryDefaultRule(cmd);
		dto.setHolidayOpenFlag(resp.getHolidayOpenFlag());
		dto.setHolidayType(resp.getHolidayType());
		dto.setNeedPay(resp.getNeedPay());

		PriceRuleDTO dailyPriceRule = new PriceRuleDTO();
		PriceRuleDTO halfdailyPriceRule = new PriceRuleDTO();
		for (PriceRuleDTO priceRule :resp.getPriceRules()){
			if (priceRule.getRentalType() ==RentalType.DAY.getCode()){
				dailyPriceRule = priceRule;
			}
			if (priceRule.getRentalType() ==RentalType.HALFDAY.getCode()){
				halfdailyPriceRule = priceRule;
			}
			if (priceRule.getRentalType() ==RentalType.THREETIMEADAY.getCode()){
				halfdailyPriceRule = priceRule;
			}
		}
		BigDecimal dailyPrice = new BigDecimal(0.00);
		BigDecimal halfdailyPrice = new BigDecimal(0.00);
		if (dailyPriceRule.getWorkdayPrice()!=null){
			dailyPrice = dailyPriceRule.getWorkdayPrice();
			
		}
		if(halfdailyPriceRule.getWorkdayPrice()!=null){
			halfdailyPrice = halfdailyPriceRule.getWorkdayPrice();
			
		}
		dto.setDailyPrice(dailyPrice);
		dto.setHalfdailyPrice(halfdailyPrice);
		return dto;
	}

	@Override
	public void addSpace(AddSpaceCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4020040210L, cmd.getAppId(), null,cmd.getCurrentProjectId());//空间管理权限
		}
//		if (null == cmd.getManagerUid())
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Invalid paramter of Categories error: null ");
//		if (null == cmd.getCategories())
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Invalid paramter of Categories error: null ");
//		if (null == cmd.getCityName())
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Invalid paramter of city error: null id or name");
		if (null == cmd.getRanges())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
			"Invalid paramter of Ranges error: null id or name");
		this.dbProvider.execute((TransactionStatus status) -> {
			OfficeCubicleSpace space = ConvertHelper.convert(cmd, OfficeCubicleSpace.class);
			space.setNamespaceId(getNamespaceId(cmd.getNamespaceId()));
			space.setGeohash(GeoHashUtils.encode(space.getLatitude(), space.getLongitude()));
			space.setStatus(OfficeStatus.NORMAL.getCode());
			space.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			space.setCreatorUid(UserContext.current().getUser().getId());
			Community community = this.communityProvider.findCommunityById(cmd.getOwnerId());
			space.setCityId(community.getCityId());
			space.setCityName(community.getCityName());
			this.officeCubicleProvider.createSpace(space);
			if (null != cmd.getSpaceAttachments())
				cmd.getSpaceAttachments().forEach((dto) -> {
					this.saveAttachment(dto, space.getId(),(byte)1);
				});
			if (null != cmd.getShortRentAttachments())
				cmd.getShortRentAttachments().forEach((dto) -> {
					this.saveAttachment(dto, space.getId(),(byte)2);
				});
			if (null != cmd.getStationAttachments())
				cmd.getStationAttachments().forEach((dto) -> {
					this.saveAttachment(dto, space.getId(),(byte)3);
				});
			if (cmd.getChargeUserDTO() != null){
				LOGGER.info("cmd : " + cmd);
				officeCubicleProvider.deleteChargeUsers(space.getId());
				for (ChargeUserDTO dto : cmd.getChargeUserDTO()){
					OfficeCubicleChargeUser user = new OfficeCubicleChargeUser();
					user.setChargeName(dto.getChargeName());
					user.setChargeUid(dto.getChargeUid());
					user.setSpaceId(space.getId());
					user.setNamespaceId(cmd.getNamespaceId());
					user.setOwnerId(cmd.getOwnerId());
					user.setOwnerType(cmd.getOwnerType());
					officeCubicleProvider.createChargeUsers(user);
				}
			}
//			cmd.getCategories().forEach((dto) -> {
//				this.saveCategory(dto, space.getId(),getNamespaceId(cmd.getNamespaceId()));
//
//			});

			cmd.getRanges().forEach(dto->saveRanges(dto,space.getId(),getNamespaceId(cmd.getNamespaceId())));
			QueryDefaultRuleAdminCommand cmd2 = new QueryDefaultRuleAdminCommand();
	        RentalResourceType type = rentalv2Provider.findRentalResourceTypes(space.getNamespaceId(),
	                RentalV2ResourceType.STATION_BOOKING.getCode());
			cmd2.setResourceType(RentalV2ResourceType.STATION_BOOKING.getCode());
			cmd2.setResourceTypeId(type.getId());
			cmd2.setSourceId(space.getId());
			cmd2.setSourceType(RuleSourceType.RESOURCE.getCode());
			rentalv2Service.queryDefaultRule(cmd2);
			return null;
		});
	}

	private void saveRanges(OfficeRangeDTO dto, Long spaceId, Integer namespaceId) {
		OfficeCubicleRange range = ConvertHelper.convert(dto,OfficeCubicleRange.class);
		range.setNamespaceId(namespaceId);
		range.setSpaceId(spaceId);
		officeCubicleRangeProvider.createOfficeCubicleRange(range);
	}

	@Override
	public void updateSpace(UpdateSpaceCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4020040210L, cmd.getAppId(), null,cmd.getCurrentProjectId());//空间管理权限
		}

//		if (null == cmd.getCategories())
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Invalid paramter of Categories error: null ");
//		if (null == cmd.getId())
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Invalid paramter of ID error: null ");
//		if (null == cmd.getCityName())
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Invalid paramter of city error: null id or name");
//		OfficeCubicleSpace oldSpace = this.officeCubicleProvider.getSpaceById(cmd.getId());
//		if (null == oldSpace)
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Invalid paramter of space id error: no space found");
		this.dbProvider.execute((TransactionStatus status) -> {
			OfficeCubicleSpace space = officeCubicleProvider.getSpaceById(cmd.getId());
			space.setNamespaceId(UserContext.getCurrentNamespaceId());
			space.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			space.setStatus(OfficeStatus.NORMAL.getCode());
			space.setOperatorUid(UserContext.current().getUser().getId());
			if (cmd.getShortRentNums()!= null){
				space.setShortRentNums(cmd.getShortRentNums());
			}
			if (cmd.getAddress()!=null){
				space.setAddress(cmd.getAddress());
			}
			if (cmd.getLatitude() > 0){
				space.setLatitude(cmd.getLatitude());
			}
			if (cmd.getLatitude() > 0){
				space.setLongitude(cmd.getLongitude());
			}
			if (cmd.getContactPhone()!=null){
				space.setContactPhone(cmd.getContactPhone());
			}
			if (cmd.getName()!=null){
				space.setName(cmd.getName());
			}
			if (cmd.getLongRentPrice()!=null){
				space.setLongRentPrice(cmd.getLongRentPrice());
			}
			if (cmd.getDescription()!=null){
				space.setDescription(cmd.getDescription());
			}
			if(cmd.getOpenFlag()!=null){
				space.setOpenFlag(cmd.getOpenFlag());
			}
			this.officeCubicleProvider.updateSpace(space);

			if (null != cmd.getSpaceAttachments()) {
				this.officeCubicleProvider.deleteAttachmentsBySpaceId(space.getId(),(byte)1);
				cmd.getSpaceAttachments().forEach((dto) -> {
					this.saveAttachment(dto, space.getId(),(byte)1);
				});
			}
			if (null != cmd.getShortRentAttachments()){
				this.officeCubicleProvider.deleteAttachmentsBySpaceId(space.getId(),(byte)2);
				cmd.getShortRentAttachments().forEach((dto) -> {
					this.saveAttachment(dto, space.getId(),(byte)2);
				});
			}
			if (null != cmd.getStationAttachments()){
				this.officeCubicleProvider.deleteAttachmentsBySpaceId(space.getId(),(byte)3);
				cmd.getStationAttachments().forEach((dto) -> {
					this.saveAttachment(dto, space.getId(),(byte)3);
				});
			}
//			this.officeCubicleProvider.deleteCategoriesBySpaceId(space.getId());
//			if (null != cmd.getCategories()) {
//				cmd.getCategories().forEach((dto) -> {
//					this.saveCategory(dto, space.getId(), getNamespaceId(cmd.getNamespaceId()));
//				});
//			}

			if (cmd.getChargeUserDTO() != null){
				officeCubicleProvider.deleteChargeUsers(space.getId());
				for (ChargeUserDTO dto : cmd.getChargeUserDTO()){
					OfficeCubicleChargeUser user = new OfficeCubicleChargeUser();
					user.setChargeName(dto.getChargeName());
					user.setChargeUid(dto.getChargeUid());
					user.setSpaceId(space.getId());
					user.setNamespaceId(cmd.getNamespaceId());
					user.setOwnerId(cmd.getOwnerId());
					user.setOwnerType(cmd.getOwnerType());
					officeCubicleProvider.createChargeUsers(user);
				}
			}
			if (null != cmd.getRanges()) {
				this.officeCubicleRangeProvider.deleteRangesBySpaceId(space.getId());
				cmd.getRanges().forEach((dto) -> {
					this.saveRanges(dto, space.getId(), getNamespaceId(cmd.getNamespaceId()));
				});
			}
			return null;
		});
	}

	public void saveAttachment(OfficeAttachmentDTO dto, Long spaceId,Byte ownerType) {
//		Attachment attachment = ConvertHelper.convert(dto, Attachment.class);
//		attachment.setOwnerId(spaceId);
//		attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		attachment.setCreatorUid(UserContext.current().getUser().getId());
//		this.attachmentProvider.createAttachment(EhOfficeCubicleAttachments.class, attachment);
		OfficeCubicleAttachment attachment = ConvertHelper.convert(dto, OfficeCubicleAttachment.class);
		attachment.setType(ownerType);
		attachment.setOwnerId(spaceId);
		attachment.setCreatorUid(UserContext.current().getUser().getId());
		this.officeCubicleProvider.createAttachments(attachment);
	}

	public void saveCategory(OfficeCategoryDTO dto, Long spaceId, Integer namespaceId) {
		if (null == dto.getSize())
			return;
		OfficeCubicleCategory category = ConvertHelper.convert(dto, OfficeCubicleCategory.class);
		category.setSpaceSize(dto.getSize());
		category.setSpaceId(spaceId);
		category.setNamespaceId(namespaceId);
		category.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		category.setCreatorUid(UserContext.current().getUser().getId());
		category.setPositionNums(dto.getPositionNums());
		category.setUnitPrice(dto.getUnitPrice());
		this.officeCubicleProvider.createCategory(category);

	}

	@Override
	public void deleteSpace(DeleteSpaceCommand cmd) {
		OfficeCubicleSpace oldSpace = this.officeCubicleProvider.getSpaceById(cmd.getId());
		if (null == oldSpace)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of space id error: no space found");
		oldSpace.setStatus(OfficeStatus.DELETED.getCode());
		this.officeCubicleProvider.updateSpace(oldSpace);
	}

	@Override
	public SearchSpaceOrdersResponse searchSpaceOrders(SearchSpaceOrdersCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4020040220L, cmd.getAppId(), null,cmd.getCurrentProjectId());//预定详情权限
		}

		SearchSpaceOrdersResponse response = new SearchSpaceOrdersResponse();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(Long.MAX_VALUE);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		List<OfficeCubicleOrder> orders = this.officeCubicleProvider.searchOrders(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getBeginDate(), cmd.getEndDate(),
				cmd.getReserveKeyword(), cmd.getSpaceName(), locator, pageSize + 1, getNamespaceId(cmd.getNamespaceId()), cmd.getWorkFlowStatus());
		if (null == orders)
			return response;
		Long nextPageAnchor = null;
		if (orders != null && orders.size() > pageSize) {
			orders.remove(orders.size() - 1);
			nextPageAnchor = orders.get(orders.size() - 1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setOrders(new ArrayList<OfficeOrderDTO>());
		orders.forEach((other) -> {
			OfficeOrderDTO dto = this.convertOfficeOrderDTO(other);
			response.getOrders().add(dto);
		});

		return response;
	}

	@Override
	public HttpServletResponse exportSpaceOrders(SearchSpaceOrdersCommand cmd, HttpServletResponse response) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4020040220L, cmd.getAppId(), null,cmd.getCurrentProjectId());//预定详情权限
		}
		Integer pageSize = Integer.MAX_VALUE;
		List<OfficeCubicleOrder> orders = this.officeCubicleProvider.searchOrders(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getBeginDate(), cmd.getEndDate(),
				cmd.getReserveKeyword(), cmd.getSpaceName(), new CrossShardListingLocator(), pageSize,
				getNamespaceId(cmd.getNamespaceId()), cmd.getWorkFlowStatus());

		if (null == orders) {
			return null;
		}

		List<OfficeOrderDTO> dtos = new ArrayList<OfficeOrderDTO>();

		orders.forEach((other) -> {
			OfficeOrderDTO dto = ConvertHelper.convert(other, OfficeOrderDTO.class);
			dto.setReserveTime(other.getReserveTime().getTime());
			dto.setCreateTime(other.getCreateTime().getTime());
			dtos.add(dto);
		});
		URL rootPath = OfficeCubicleServiceImpl.class.getResource("/");
		String filePath = rootPath.getPath() + this.downloadDir;
		File file = new File(filePath);
		if (!file.exists())
			file.mkdirs();
		filePath = filePath + "RentalBills" + System.currentTimeMillis() + ".xlsx";
		// 新建了一个文件
		this.createRentalBillsBook(filePath, dtos);

		return download(filePath, response);

	}

	@Override
	public HttpServletResponse exportCubicleOrders(SearchCubicleOrdersCommand cmd, HttpServletResponse response) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4920049600L, cmd.getAppId(), null,cmd.getCurrentProjectId());//预定详情权限
		}
		List<OfficeCubicleRentOrder> orders = this.officeCubicleProvider.searchCubicleOrders(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getBeginTime(), cmd.getEndTime(),
				new CrossShardListingLocator(), cmd.getPageSize(), getNamespaceId(cmd.getNamespaceId()),cmd.getPaidType(),cmd.getPaidMode(),cmd.getRequestType(),cmd.getRentType(), cmd.getOrderStatus());

		if (null == orders) {
			return null;
		}

		List<OfficeRentOrderDTO> dtos = new ArrayList<OfficeRentOrderDTO>();

		orders.forEach((other) -> {
			OfficeRentOrderDTO dto = ConvertHelper.convert(other, OfficeRentOrderDTO.class);
			dto.setCreateTime(other.getCreateTime().getTime());
			if (other.getRentType() == 1){
				dto.setBeginTime(other.getBeginTime().getTime());
				dto.setEndTime(other.getEndTime().getTime());
			} else if(other.getRentType() == 0){
				dto.setUserDetail(other.getUseDetail());
			}
			dtos.add(dto);
		});
		URL rootPath = OfficeCubicleServiceImpl.class.getResource("/");
		String filePath = rootPath.getPath() + this.downloadDir;
		File file = new File(filePath);
		if (!file.exists())
			file.mkdirs();
		filePath = filePath + "RentalBills" + System.currentTimeMillis() + ".xlsx";
		// 新建了一个文件
		this.createCubicleRentBillsBook(filePath, dtos);

		return download(filePath, response);

	}
	
	public HttpServletResponse download(String path, HttpServletResponse response) {
		try {
			// path是指欲下载的文件的路径。
			File file = new File(path);
			// 取得文件名。
			String filename = file.getName();
			// 取得文件的后缀名。
			String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();

			// 读取完成删除文件
			if (file.isFile() && file.exists()) {
				file.delete();
			}
		} catch (IOException ex) {
			LOGGER.error(ex.getMessage());
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_DOWNLOAD_EXCEL,
					ex.getLocalizedMessage());

		}
		return response;
	}

	public void createRentalBillsBook(String path, List<OfficeOrderDTO> dtos) {
		if (null == dtos || dtos.size() == 0)
			return;
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("rentalBill");

		this.createRentalBillsBookSheetHead(sheet);
		for (OfficeOrderDTO dto : dtos) {
			this.setNewRentalBillsBookRow(sheet, dto);
		}

		try {
			FileOutputStream out = new FileOutputStream(path);

			wb.write(out);
			wb.close();
			out.close();

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_CREATE_EXCEL,
					e.getLocalizedMessage());
		}
	}
	
	public void createCubicleRentBillsBook(String path, List<OfficeRentOrderDTO> dtos) {
		if (null == dtos || dtos.size() == 0)
			return;
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("rentalBill");

		this.createCubicleBillsBookSheetHead(sheet);
		for (OfficeRentOrderDTO dto : dtos) {
			this.setNewCubicleRentBillsBookRow(sheet, dto);
		}

		try {
			FileOutputStream out = new FileOutputStream(path);

			wb.write(out);
			wb.close();
			out.close();

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_CREATE_EXCEL,
					e.getLocalizedMessage());
		}
	}
	
	private void createRentalBillsBookSheetHead(Sheet sheet) {

		Row row = sheet.createRow(sheet.getLastRowNum());
		int i = -1;
		row.createCell(++i).setCellValue("序号");
		row.createCell(++i).setCellValue("申请时间");
		row.createCell(++i).setCellValue("预约人");
		row.createCell(++i).setCellValue("联系电话");
		row.createCell(++i).setCellValue("预约空间");
		row.createCell(++i).setCellValue("参观日期");
		row.createCell(++i).setCellValue("状态");
	}

	private void createCubicleBillsBookSheetHead(Sheet sheet) {

		Row row = sheet.createRow(sheet.getLastRowNum());
		int i = -1;
		row.createCell(++i).setCellValue("序号");
		row.createCell(++i).setCellValue("订单提交时间");
		row.createCell(++i).setCellValue("订单类型");
		row.createCell(++i).setCellValue("预定时间");
		row.createCell(++i).setCellValue("预定人");
		row.createCell(++i).setCellValue("联系方式");
		row.createCell(++i).setCellValue("订单金额（元）");
		row.createCell(++i).setCellValue("支付类型");
		row.createCell(++i).setCellValue("支付方式");
		row.createCell(++i).setCellValue("订单来源");
		row.createCell(++i).setCellValue("订单状态");
	}
	
	
	private void setNewRentalBillsBookRow(Sheet sheet, OfficeOrderDTO dto) {
		Row row = sheet.createRow(sheet.getLastRowNum() + 1);
		int i = -1;
		// 序号
		row.createCell(++i).setCellValue(row.getRowNum());
		
		// 申请时间
		row.createCell(++i).setCellValue(datetimeSF.format(new Timestamp(dto.getCreateTime())));
		//预定人
		row.createCell(++i).setCellValue(dto.getReserverName());
		// 联系电话
		row.createCell(++i).setCellValue(dto.getReserveContactToken());
		// 项目名称
		row.createCell(++i).setCellValue(dto.getSpaceName());
//		// 所在城市
//		row.createCell(++i).setCellValue(dto.getProvinceName() + dto.getCityName());
		// 订单时间
		row.createCell(++i).setCellValue(datetimeSF.format(new Timestamp(dto.getReserveTime())));
		// 预定类别
//		OfficeOrderType ordertype = OfficeOrderType.fromCode(dto.getOrderType());
//		row.createCell(++i).setCellValue(ordertype==null?"":ordertype.getMsg());
		// 工位类别
//		OfficeRentType renttype = OfficeRentType.fromCode(dto.getRentType());
//		row.createCell(++i).setCellValue(renttype==null?"":renttype.getMsg());
		// 工位数/面积
//		OfficeSpaceType spaceType = OfficeSpaceType.fromCode(dto.getSpaceType()==null?(byte)1:(byte)2);
//		row.createCell(++i).setCellValue(dto.getSpaceSize() + spaceType==null?"":spaceType.getMsg());


		

		// 工作流状态
		if(dto!=null && dto.getWorkFlowStatus()!=null) {
			OfficeOrderWorkFlowStatus workFlowStatus = OfficeOrderWorkFlowStatus.fromType(dto.getWorkFlowStatus());
			row.createCell(++i).setCellValue(workFlowStatus == null ? "" : workFlowStatus.getDescription());
		}else{
			row.createCell(++i).setCellValue("");
		}
	}

	private void setNewCubicleRentBillsBookRow(Sheet sheet, OfficeRentOrderDTO dto) {
		Row row = sheet.createRow(sheet.getLastRowNum() + 1);
		int i = -1;
		// 序号
		row.createCell(++i).setCellValue(row.getRowNum());
		// 申请时间
		row.createCell(++i).setCellValue(datetimeSF.format(new Timestamp(dto.getCreateTime())));
		//订单类型
		OfficeCubiceRentType rentType = OfficeCubiceRentType.fromCode(dto.getRentType());
		row.createCell(++i).setCellValue(rentType==null?"":rentType.getDescription());
		// 预定时间
		if (dto.getRentType() == 1){
			String value = dto.getBeginTime() + "至" + dto.getEndTime();
			row.createCell(++i).setCellValue(value);
		} else if(dto.getRentType() == 0){
			row.createCell(++i).setCellValue(dto.getUserDetail());
		}
		// 预订人
		row.createCell(++i).setCellValue(dto.getReserverName());
		// 联系电话
		row.createCell(++i).setCellValue(dto.getReserverContactToken());
		//订单金额
		row.createCell(++i).setCellValue(String.valueOf(dto.getPrice()));
		//支付类型
		GorderPayType gorderPayType = GorderPayType.fromCode(dto.getPaidMode());
		row.createCell(++i).setCellValue(gorderPayType == null?"":gorderPayType.getDesc());
		//支付方式
        VendorType type = VendorType.fromCode(dto.getPaidType());
		row.createCell(++i).setCellValue(null==type?"":type.getDescribe());
		// 订单来源
		OfficeCubicleRequestType requestTpye = OfficeCubicleRequestType.fromCode(dto.getRequestType());
		row.createCell(++i).setCellValue(requestTpye==null?"":requestTpye.getDesc());
		//订单状态
		OfficeCubicleOrderStatus orderStatus = OfficeCubicleOrderStatus.fromCode(dto.getOrderStatus());
		row.createCell(++i).setCellValue(orderStatus==null?"":orderStatus.getDescription());
		
	}
	
	@Override
	public List<CityDTO> queryCities(QueryCitiesCommand cmd) {
		checkOwnerTypeOwnerId(cmd.getOwnerType(),cmd.getOwnerId());
		Integer namespaceId = UserContext.getCurrentNamespaceId();

//		查询自定义配置标识
		GetCustomizeCommand newCmd = ConvertHelper.convert(cmd,GetCustomizeCommand.class);
		Byte custmFlag = getProjectCustomize(newCmd);

//		根据项目查询
		int pageSize = PaginationConfigHelper.getMaxPageSize(configurationProvider,999999);
		List<OfficeCubicleCity> cities;
		if(custmFlag.equals((byte)1)){
			cities = officeCubicleCityProvider.listOfficeCubicleCity(namespaceId,null,cmd.getOwnerType(),cmd.getOwnerId(),Long.MAX_VALUE,pageSize);
		}else{
			cities = officeCubicleCityProvider.listOfficeCubicleCity(namespaceId,null,null,null,Long.MAX_VALUE,pageSize);
		}
		final OfficeCubicleSelectedCity selecetedCity = cubicleSelectedCityProvider.findOfficeCubicleSelectedCityByCreator(UserContext.current().getUser().getId());
		return cities.stream().map(r->{
			CityDTO dto = ConvertHelper.convert(r, CityDTO.class);
			//根据上次用户选中的城市，这里设置当前选中的城市。
			if(selecetedCity!=null 
					&& !StringUtils.isEmpty(selecetedCity.getProvinceName())
					&& !StringUtils.isEmpty(selecetedCity.getCityName())
					&& selecetedCity.getProvinceName().equals(dto.getProvinceName()) 
					&& selecetedCity.getCityName().equals(dto.getCityName())){
				dto.setSelectFlag((byte)1);
			}
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public OfficeSpaceDTO getSpaceDetail(GetSpaceDetailCommand cmd) {
		OfficeCubicleSpace space = this.officeCubicleProvider.getSpaceById(cmd.getSpaceId());
		OfficeSpaceDTO dto = convertSpaceDTO(space);
		return dto;
	}

	@Override
	public AddSpaceOrderResponse addSpaceOrder(AddSpaceOrderCommand cmd) {
		checkAddOrderCmd(cmd);
		OfficeCubicleSpace space = this.officeCubicleProvider.getSpaceById(cmd.getSpaceId());
		if (null == space)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of space id error: space not found ");
		Flow flow = flowService.getEnabledFlow(space.getNamespaceId(), OfficeCubicleFlowModuleListener.MODULE_ID,
				FlowModuleType.NO_MODULE.getCode(),space.getOwnerId(), FlowOwnerType.COMMUNITY.getCode());
		if(flow==null){
			throw RuntimeErrorException.errorWith(OfficeCubicleErrorCode.SCOPE, OfficeCubicleErrorCode.ERROR_UNENABLE_FLOW,
					"提交失败，未启用工作流，请联系管理员");
		}

		Long flowCaseId = flowService.getNextFlowCaseId();
		OfficeCubicleOrder order =  generateOfficeCubicleOrders(cmd, space, flowCaseId);

		dbProvider.execute(status -> {
			this.officeCubicleProvider.createOrder(order);
			FlowCase flowCase = createFlowCase(order, flow, flowCaseId);
			return flowCase;
		});
		sendMessage(space,order);
		return new AddSpaceOrderResponse(flowCaseId);
	}

	private FlowCase createFlowCase(OfficeCubicleOrder order, Flow flow, Long flowCaseId) {
		CreateFlowCaseCommand cmd21 = new CreateFlowCaseCommand();
		cmd21.setApplyUserId(UserContext.current().getUser().getId());
		cmd21.setReferType(FlowReferType.OFFICE_CUBICLE.getCode());
		cmd21.setReferId(order.getId());
		cmd21.setProjectType(order.getOwnerType());
		cmd21.setProjectId(order.getOwnerId());
//		OfficeRentType type= OfficeRentType.fromCode(order.getRentType());
//		if(type == OfficeRentType.OPENSITE) {
//			cmd21.setContent("工位类型：" + type.getMsg() + "\n"
//					+ "预订工位数：" + order.getPositionNums());
//		}else if(type == OfficeRentType.WHOLE){
//			cmd21.setContent("工位类型：" + type.getMsg() + "\n"
//					+ "预订空间：" + order.getCategoryName());
//		}
		cmd21.setContent("姓名： " + order.getReserverName() + "\n" 
				+ "手机号码： " + order.getReserveContactToken());
		cmd21.setTitle("工位预订");
		cmd21.setFlowMainId(flow.getFlowMainId());
		cmd21.setFlowVersion(flow.getFlowVersion());
		cmd21.setFlowCaseId(flowCaseId);
		return flowService.createFlowCase(cmd21);
	}


	private void checkAddOrderCmd(AddSpaceOrderCommand cmd) {
//		if (null == cmd.getOrderType())
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Invalid paramter of OrderType error: null ");
//		if (null == cmd.getReserveEnterprise())
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Invalid paramter of ReserveEnterprise error: null ");
		if (null == cmd.getReserveContactToken())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of ReserveContactToken error: null ");
		if (null == cmd.getReserverName())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of ReserverName error: null ");
//		if (null == cmd.getSize())
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Invalid paramter of size error: null ");
	}

	private void checkOwnerTypeOwnerId(String ownerType,Long ownerId){
		if(null == ownerType || null == ownerId){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of ownerType ownerId: null ");
		}
	}

	private void checkOrgId(Long orgId){
		if(null == orgId){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of orgId: null ");
		}
	}

	private void sendMessage(OfficeCubicleSpace space,OfficeCubicleOrder order) {
		// 发消息 +推送

//		OfficeRentType officeRentType = OfficeRentType.fromCode(order.getRentType());
		StringBuffer sb = new StringBuffer();
		sb.append("您收到一条");
		sb.append(space.getName());
		sb.append("的工位预订订单:");
//		sb.append(officeRentType.getMsg());
//		sb.append(order.getSpaceSize());
//		sb.append(officeRentType==OfficeRentType.OPENSITE?"个":"㎡");
		sb.append("\n预订人:");
		sb.append(order.getReserverName());
		sb.append("\n手机号:");
		sb.append(order.getReserveContactToken());
		sb.append("\n公司名称:");
		sb.append(order.getReserveEnterprise());
		sb.append("\n您可以登陆管理后台查看详情");
		List<OfficeCubicleChargeUser> chargeUser = officeCubicleProvider.findChargeUserBySpaceId(space.getId());
		for(OfficeCubicleChargeUser dto :chargeUser){
			sendMessageToUser(dto.getChargeUid(), sb.toString());
		}
	}

	private OfficeCubicleOrder generateOfficeCubicleOrders(AddSpaceOrderCommand cmd,OfficeCubicleSpace space, Long flowCaseId) {
		OfficeCubicleOrder order = ConvertHelper.convert(space, OfficeCubicleOrder.class);
		order.setSpaceId(cmd.getSpaceId());
		order.setSpaceName(space.getName());
		order.setSpaceSize(cmd.getSize() + "");
		order.setRentType(cmd.getRentType());
		order.setSpaceType(cmd.getSpaceType());
		order.setReserveTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		order.setReserverUid(UserContext.current().getUser().getId());
		order.setReserverName(cmd.getReserverName());
		order.setReserveContactToken(cmd.getReserveContactToken());
		order.setReserveEnterprise(cmd.getReserveEnterprise());
		order.setOrderType(cmd.getOrderType());
		order.setNamespaceId(UserContext.getCurrentNamespaceId());
		order.setStatus(OfficeOrderStatus.NORMAL.getCode());
		order.setOwnerType(space.getOwnerType());
		order.setOwnerId(space.getOwnerId());
		order.setWorkFlowStatus(OfficeOrderWorkFlowStatus.PROCESSING.getCode());
		order.setFlowCaseId(flowCaseId);
		order.setPositionNums(cmd.getPositionNums());
		order.setCategoryName(cmd.getCategoryName());
		order.setCategoryId(cmd.getCategoryId());
		order.setContactPhone(cmd.getReserveContactToken());
		order.setEmployeeNumber(cmd.getEmployeeNumber());
		order.setFinancingFlag(cmd.getFinancingFlag());
		return order;
	}

	private void sendMessageToUser(Long userId, String content) {
		MessageDTO messageDto = new MessageDTO();
		messageDto.setAppId(AppConstants.APPID_MESSAGING);
		messageDto.setSenderUid(User.SYSTEM_USER_LOGIN.getUserId());
		messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()));
		messageDto
				.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
		messageDto.setBodyType(MessageBodyType.TEXT.getCode());
		messageDto.setBody(content);
		messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
		LOGGER.debug("messageDTO : ++++ \n " + messageDto);
		// 发消息 +推送
		messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
				userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
	}

	@Override
	public void updateOfficeCubicleRefundRule(UpdateOfficeCubicleRefundRuleCommand cmd){
		List<OfficeCubicleRefundRule> refundRule = officeCubicleProvider.findRefundRule(cmd.getSpaceId());
		if (refundRule != null){
			officeCubicleProvider.deleteRefundRule(cmd.getSpaceId());
		}
		if (cmd.getRefundStrategies()!=null){
			for(OfficeCubicleRefundRuleDTO dto :cmd.getRefundStrategies()){
				OfficeCubicleRefundRule rule  = ConvertHelper.convert(dto,OfficeCubicleRefundRule.class);
				rule.setRefundStrategy(cmd.getRefundStrategy());
				rule.setSpaceId(cmd.getSpaceId());
				rule.setNamespaceId(cmd.getNamespaceId());
				rule.setOwnerId(cmd.getOwnerId());
				rule.setOwnerType(cmd.getOwnerType());
				officeCubicleProvider.createRefundRule(rule);
			}
		}
		OfficeCubicleSpace space = officeCubicleProvider.getSpaceById(cmd.getSpaceId());
		space.setRefundStrategy(cmd.getRefundStrategy());
		officeCubicleProvider.updateSpace(space);
		officeCubicleProvider.deleteRefundTip(cmd.getSpaceId());
		createOfficeCubicleRefundTips(cmd.getSpaceId(),cmd.getRefundTips());
	}
	private void createOfficeCubicleRefundTips(Long spaceId,List<OfficeCubicleRefundTipDTO> refundTips){
		if (refundTips != null && !refundTips.isEmpty()){
			refundTips.forEach(r->{
				OfficeCubicleRefundTips tip = ConvertHelper.convert(r,OfficeCubicleRefundTips.class);
				tip.setNamespaceId(UserContext.getCurrentNamespaceId());
				tip.setSpaceId(spaceId);
				officeCubicleProvider.createRefundTip(tip);
			});
		}
	}
	@Override
	public GetOfficeCubicleRefundRuleResponse getOfficeCubicleRefundRule(GetOfficeCubicleRefundRuleCommand cmd){
		GetOfficeCubicleRefundRuleResponse resp = new GetOfficeCubicleRefundRuleResponse();
		List<OfficeCubicleRefundRule> rules = officeCubicleProvider.findRefundRule(cmd.getSpaceId());
		OfficeCubicleSpace space = officeCubicleProvider.getSpaceById(cmd.getSpaceId());
		if (space.getRefundStrategy() != null){
			resp.setRefundStrategy(space.getRefundStrategy());
		} else {
			resp.setRefundStrategy(RentalOrderStrategy.NONE.getCode());
		}
		if (rules != null){
			resp.setRefundStrategies(rules.stream().map(r->{
				OfficeCubicleRefundRuleDTO dto = ConvertHelper.convert(r,OfficeCubicleRefundRuleDTO.class);
				return dto;
				}).collect(Collectors.toList()));
		}
		List<OfficeCubicleRefundTips> refundTips = officeCubicleProvider.listRefundTips(cmd.getSpaceId(), null);
		if (cmd.getOrderId() !=null){
			OfficeCubicleRentOrder order = officeCubicleProvider.findOfficeCubicleRentOrderById(cmd.getOrderId());
			BigDecimal refundPrice = calculateRefundAmount(order,System.currentTimeMillis(),space);
			resp.setRefundPrice(refundPrice);
			OfficeCubicleRefundRule orderRule = findOfficeCubicleRefundRule(order,System.currentTimeMillis(),space);
			BigDecimal refundRate = new BigDecimal(0);
			if (orderRule != null){
				refundRate = new BigDecimal(orderRule.getFactor());
			}
			resp.setRefundRate(refundRate);
		}
		if(refundTips!=null){
			resp.setRefundTips(refundTips.stream().map(r->ConvertHelper.convert(r,OfficeCubicleRefundTipDTO.class)).collect(Collectors.toList()));
		}
		return resp;
	}
    private BigDecimal calculateRefundAmount(OfficeCubicleRentOrder order, Long now, OfficeCubicleSpace space) {

        if (space.getRefundStrategy().equals(RentalOrderStrategy.CUSTOM.getCode())) {

            List<OfficeCubicleRefundRule> refundRules = officeCubicleProvider.findRefundRule(order.getSpaceId());

            List<OfficeCubicleRefundRule> outerRules = refundRules.stream().filter(r -> r.getDurationType() == RentalDurationType.OUTER.getCode())
                    .collect(Collectors.toList());
            List<OfficeCubicleRefundRule> innerRules = refundRules.stream().filter(r -> r.getDurationType() == RentalDurationType.INNER.getCode())
                    .collect(Collectors.toList());

            OfficeCubicleRefundRule orderRule = null;

            Long startUseTime = order.getBeginTime().getTime();

            long intervalTime = startUseTime - now;

            //处于时间外，查找最大的时间
            for (OfficeCubicleRefundRule r: outerRules) {
                long duration = 0;

                if (r.getDurationUnit() == RentalDurationUnit.HOUR.getCode()) {
                    duration = (long)(r.getDuration() * 60 * 60 * 1000);
                }
                if (intervalTime > duration) {
                    if (null == orderRule || r.getDuration() > orderRule.getDuration()) {
                        orderRule = r;
                    }
                }
            }
            if (orderRule == null) {
                //处于时间内，查找最小的时间
                for (OfficeCubicleRefundRule r: innerRules) {
                    long duration = 0;

                    if (r.getDurationUnit() == RentalDurationUnit.HOUR.getCode()) {
                        duration = (long)(r.getDuration() * 60 * 60 * 1000);
                    }
                    if (intervalTime < duration) {
                        if (null == orderRule || r.getDuration() < orderRule.getDuration()) {
                            orderRule = r;
                        }
                    }
                }
            }

            BigDecimal amount = order.getPrice().multiply(new BigDecimal(orderRule.getFactor()))
                    .divide(new BigDecimal(100),2, RoundingMode.HALF_UP);


            return amount;
        }else if (space.getRefundStrategy().equals(RentalOrderStrategy.FULL.getCode())) {
            return order.getPrice();
        }else if (space.getRefundStrategy().equals(RentalOrderStrategy.NONE.getCode())){
        	return new BigDecimal(0);
        }
        
        return order.getPrice();
    }
    private OfficeCubicleRefundRule findOfficeCubicleRefundRule(OfficeCubicleRentOrder order, Long now, OfficeCubicleSpace space) {

        if (space.getRefundStrategy().equals(RentalOrderStrategy.CUSTOM.getCode())) {

            List<OfficeCubicleRefundRule> refundRules = officeCubicleProvider.findRefundRule(order.getSpaceId());

            List<OfficeCubicleRefundRule> outerRules = refundRules.stream().filter(r -> r.getDurationType() == RentalDurationType.OUTER.getCode())
                    .collect(Collectors.toList());
            List<OfficeCubicleRefundRule> innerRules = refundRules.stream().filter(r -> r.getDurationType() == RentalDurationType.INNER.getCode())
                    .collect(Collectors.toList());

            OfficeCubicleRefundRule orderRule = null;

            Long startUseTime = order.getBeginTime().getTime();

            long intervalTime = startUseTime - now;

            //处于时间外，查找最大的时间
            for (OfficeCubicleRefundRule r: outerRules) {
                long duration = 0;

                if (r.getDurationUnit() == RentalDurationUnit.HOUR.getCode()) {
                    duration = (long)(r.getDuration() * 60 * 60 * 1000);
                }
                if (intervalTime > duration) {
                    if (null == orderRule || r.getDuration() > orderRule.getDuration()) {
                        orderRule = r;
                    }
                }
            }
            if (orderRule == null) {
                //处于时间内，查找最小的时间
                for (OfficeCubicleRefundRule r: innerRules) {
                    long duration = 0;

                    if (r.getDurationUnit() == RentalDurationUnit.HOUR.getCode()) {
                        duration = (long)(r.getDuration() * 60 * 60 * 1000);
                    }
                    if (intervalTime < duration) {
                        if (null == orderRule || r.getDuration() < orderRule.getDuration()) {
                            orderRule = r;
                        }
                    }
                }
            }
            return orderRule;
        }
        return null;
    }
	@Override
	public List<OfficeOrderDTO> getUserOrders() {
		List<OfficeOrderDTO> resp = new ArrayList<OfficeOrderDTO>();
		List<OfficeCubicleOrder> orders = this.officeCubicleProvider.queryOrdersByUser(UserContext.current().getUser().getId(),
				UserContext.getCurrentNamespaceId());
		if (null == orders)
			return resp;
		orders.forEach((other) -> {
			OfficeOrderDTO dto = this.convertOfficeOrderDTO(other);
			resp.add(dto);
		});
		return resp;
	}

	public OfficeOrderDTO convertOfficeOrderDTO(OfficeCubicleOrder other) {
		OfficeOrderDTO dto = ConvertHelper.convert(other, OfficeOrderDTO.class);
		dto.setCoverUrl(this.contentServerService.parserUri(other.getCoverUri(), EntityType.USER.getCode(), UserContext.current()
				.getUser().getId()));
		dto.setReserveTime(other.getReserveTime().getTime());
		return dto;
	}

	
	@Override
	public void deleteUserSpaceOrder(DeleteUserSpaceOrderCommand cmd) {
		OfficeCubicleOrder order = this.officeCubicleProvider.getOrderById(cmd.getOrderId());
		order.setStatus(OfficeOrderStatus.UNVISABLE.getCode());
		this.officeCubicleProvider.updateOrder(order);
	}

	@Override
	public QuerySpacesResponse querySpaces(QuerySpacesCommand cmd) {
		QuerySpacesResponse response = new QuerySpacesResponse();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(Long.MAX_VALUE);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<OfficeCubicleSpace> spaces = this.officeCubicleProvider.querySpacesByCityName(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getProvinceName(),cmd.getCityName(), locator, pageSize + 1,
				getNamespaceId(UserContext.getCurrentNamespaceId()));
		if (null == spaces)
			return response;
		Long nextPageAnchor = null;
		if (spaces != null && spaces.size() > pageSize) {
			spaces.remove(spaces.size() - 1);
			nextPageAnchor = spaces.get(spaces.size() - 1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setSpaces(new ArrayList<OfficeSpaceDTO>());
		spaces.forEach((other) -> {
			OfficeSpaceDTO dto = convertSpaceDTO(other);
			response.getSpaces().add(dto);
		});

		return response;
	}
	
	@Override
	public ListRentCubicleResponse listRentCubicle(ListRentCubicleCommand cmd){
		List<OfficeCubicleRange> ranges = 
				officeCubicleRangeProvider.getOfficeCubicleRangeByOwner(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getNamespaceId());
		ListRentCubicleResponse resp = new ListRentCubicleResponse();
		if (ranges.size() <= 0){
			resp.setLongRentFlag((byte)0);
			resp.setShortRentFlag((byte)0);
		}else{
			resp.setLongRentFlag((byte)1);
			for(OfficeCubicleRange range : ranges){
				List<OfficeCubicleStation> station = 
						officeCubicleProvider.getOfficeCubicleStation(cmd.getOwnerId(), cmd.getOwnerType(), range.getSpaceId(),null, null,null,null,null);
				if (station ==null){
					resp.setShortRentFlag((byte)0);
				} else {
					resp.setShortRentFlag((byte)1);
				}
			}
		}
		return resp;
	}
	
	@Override
	public void updateCurrentUserSelectedCity(String provinceName, String cityName) {
		OfficeCubicleSelectedCity selectedCity = new OfficeCubicleSelectedCity();
		selectedCity.setCityName(cityName);
		selectedCity.setProvinceName(provinceName);
		selectedCity.setNamespaceId(UserContext.getCurrentNamespaceId());
		cubicleSelectedCityProvider.deleteSelectedCityByCreator(UserContext.current().getUser().getId());
		cubicleSelectedCityProvider.createOfficeCubicleSelectedCity(selectedCity);
	}

	@Override
	public void dataMigration() {
		List<OfficeCubicleSpace> allspaces =officeCubicleProvider.listAllSpaces(0L,100);
		if (allspaces==null || allspaces.size()==0) {
			return;
		}

		boolean continueFlag = true;
		while(continueFlag) {
			for (OfficeCubicleSpace allspace : allspaces) {
				OfficeCubicleCity city = officeCubicleCityProvider.findOfficeCubicleCityByProvinceAndCity(allspace.getProvinceName(), allspace.getCityName(), allspace.getNamespaceId());
				if (city == null) {
					city = new OfficeCubicleCity();
					city.setNamespaceId(allspace.getNamespaceId());
					city.setProvinceName(allspace.getProvinceName());
					city.setCityName(allspace.getCityName());
					officeCubicleCityProvider.createOfficeCubicleCity(city);
				}
			}
			if (allspaces.size() == 100) {
				allspaces = officeCubicleProvider.listAllSpaces(allspaces.get(99).getId(), 100);
			} else {
				continueFlag=false;
			}
		}


	}
	//	@Override
//	public void dataMigration() {
//
//		//owner刷入space
//		List<OfficeCubicleSpace> emptyOwnerList = officeCubicleProvider.listEmptyOwnerSpace();
//		if(emptyOwnerList!=null) {
//			for (OfficeCubicleSpace r : emptyOwnerList) {
//				ListingLocator locator = new ListingLocator();
//				locator.setAnchor(0L);
//				List<CommunityDTO> communityDTOS = communityProvider.listCommunitiesByNamespaceId(CommunityType.COMMERCIAL.getCode(), r.getNamespaceId(), locator, 10);
//				if(communityDTOS!=null && communityDTOS.size()>0){
//					r.setOwnerId(communityDTOS.get(0).getId());
//					r.setOwnerType(OfficeSpaceOwner.COMMUNITY.getCode());
//
//					//owner刷入ranges
//					for (CommunityDTO communityDTO : communityDTOS) {
//						OfficeCubicleRange range = officeCubicleRangeProvider.findOfficeCubicleRangeByOwner(communityDTO.getId(),OfficeSpaceOwner.COMMUNITY.getCode(),r.getId(),r.getNamespaceId());
//						if(range==null) {
//							range = new OfficeCubicleRange();
//							range.setNamespaceId(r.getNamespaceId());
//							range.setOwnerId(communityDTO.getId());
//							range.setOwnerType(OfficeSpaceOwner.COMMUNITY.getCode());
//							range.setSpaceId(r.getId());
//							officeCubicleRangeProvider.createOfficeCubicleRange(range);
//						}
//
//					}
//					officeCubicleProvider.updateSpace(r);
//				}
//			}
//		}
//
//		List<OfficeCubicleOrder> emptyOwnerOrders = officeCubicleProvider.listEmptyOwnerOrders();
//		if(emptyOwnerOrders!=null){
//			for (OfficeCubicleOrder order : emptyOwnerOrders) {
//				OfficeCubicleSpace space = officeCubicleProvider.getSpaceById(order.getSpaceId());
//				if(space==null) {
//					continue;
//				}
//				order.setOwnerType(space.getOwnerType());
//				order.setOwnerId(space.getOwnerId());
//				officeCubicleProvider.updateOrder(order);
//			}
//		}
//	}

	@Override
	public ListRegionsResponse listRegions(ListRegionsCommand cmd) {
		List<Region> entityResultList;
		if(cmd.getParentId()==null) {
			entityResultList = this.regionProvider.listRegions(0, RegionScope.PROVINCE, RegionAdminStatus.ACTIVE, null);
		}else{
			entityResultList = this.regionProvider.listChildRegions(0, cmd.getParentId(),RegionScope.CITY, RegionAdminStatus.ACTIVE, null);

		}
		if(entityResultList==null || entityResultList.size()==0){
			return null;
		}
		ListRegionsResponse response = new ListRegionsResponse();
		response.setRegions(entityResultList.stream().map(r->ConvertHelper.convert(r,RegionDTO.class)).collect(Collectors.toList()));
		return response;
	}

	@Override
	public ListCitiesResponse listCities(ListCitiesCommand cmd) {
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		long pageAnchor = cmd.getNextPageAnchor()==null?Long.MAX_VALUE:cmd.getNextPageAnchor();
		Integer namespaceId = cmd.getNamespaceId();
		if(namespaceId==null){
			namespaceId = UserContext.getCurrentNamespaceId();
		}
//		checkOrgId(cmd.getOrgId());
		List<OfficeCubicleCity> cities = this.officeCubicleCityProvider.listOfficeCubicleCity(namespaceId,cmd.getOrgId(),cmd.getOwnerType(),cmd.getOwnerId(),pageAnchor,pageSize+1);

		if (null == cities || cities.size()==0)
			return null;
		Long nextPageAnchor = null;
		if (cities != null && cities.size() > pageSize) {
			cities.remove(cities.size() - 1);
			nextPageAnchor = cities.get(cities.size() - 1).getDefaultOrder();
		}
		ListCitiesResponse response = new ListCitiesResponse();
		response.setNextPageAnchor(nextPageAnchor);
		response.setCities(cities.stream().map(r->ConvertHelper.convert(r,CityDTO.class)).collect(Collectors.toList()));
		return response;
	}

	@Override
	public void deleteCity(DeleteCityCommand cmd) {
		Tuple<Boolean, Boolean> result = coordinationProvider.getNamedLock(CoordinationLocks.OFFICE_CUBICLE_CITY_LOCK.getCode()).enter(()->{
			List<OfficeCubicleCity> list = officeCubicleCityProvider.listOfficeCubicleCity(UserContext.getCurrentNamespaceId());
			if(list!=null && list.size()<2){
				return false;
			}
			OfficeCubicleCity city = officeCubicleCityProvider.findOfficeCubicleCityById(cmd.getCityId());
			if(city!=null){
				officeCubicleCityProvider.deleteOfficeCubicleCity(cmd.getCityId());
				officeCubicleProvider.updateSpaceByProvinceAndCity(UserContext.getCurrentNamespaceId(),city.getProvinceName(),city.getCityName());
			}
			return true;
		});
		if(!result.first()){
			throw RuntimeErrorException.errorWith(OfficeCubicleErrorCode.SCOPE, OfficeCubicleErrorCode.ERROR_DELETE_CITYS,
					"最后一个城市不能删除");
		}
	}

	@Override
	public void createOrUpdateCity(CreateOrUpdateCityCommand cmd) {
		checkCityName(cmd.getProvinceName(),cmd.getCityName());
		checkOrgId(cmd.getOrgId());
		if(cmd.getId()!=null){
			OfficeCubicleCity officeCubicleCity = officeCubicleCityProvider.findOfficeCubicleCityById(cmd.getId());
			if(officeCubicleCity==null){
				throw RuntimeErrorException.errorWith(OfficeCubicleErrorCode.SCOPE, OfficeCubicleErrorCode.ERROR_CITY_ID,
						"id未找到");
			}
			officeCubicleCity.setCityName(cmd.getCityName());
			officeCubicleCity.setProvinceName(cmd.getProvinceName());
			officeCubicleCity.setIconUri(cmd.getIconUri());
			officeCubicleCity.setOrgId(cmd.getOrgId());
			officeCubicleCity.setStatus((byte)2);
			if(StringUtils.isNotEmpty(cmd.getOwnerType()) && null != cmd.getOwnerId()){
				officeCubicleCity.setOwnerType(cmd.getOwnerType());
				officeCubicleCity.setOwnerId(cmd.getOwnerId());
			}
			officeCubicleCityProvider.updateOfficeCubicleCity(officeCubicleCity);
		}else{
			OfficeCubicleCity oldOfficeCubicleCity = null;
			if(StringUtils.isNotEmpty(cmd.getOwnerType()) && null != cmd.getOwnerId()){
				oldOfficeCubicleCity = officeCubicleCityProvider.findOfficeCubicleCityByProvinceAndCity(cmd.getProvinceName(), cmd.getCityName(), UserContext.getCurrentNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId());
			}else{
				oldOfficeCubicleCity = officeCubicleCityProvider.findOfficeCubicleCityByProvinceAndCity(cmd.getProvinceName(), cmd.getCityName(), UserContext.getCurrentNamespaceId());
				if(oldOfficeCubicleCity != null && (StringUtils.isNotEmpty(oldOfficeCubicleCity.getOwnerType()) || null != oldOfficeCubicleCity.getOwnerId())){
					oldOfficeCubicleCity = null;
				}
			}
			if(oldOfficeCubicleCity!=null){
				throw RuntimeErrorException.errorWith(OfficeCubicleErrorCode.SCOPE, OfficeCubicleErrorCode.ERROR_EXIST_CITY,
						"城市已存在，不能重复添加");
			}
			OfficeCubicleCity officeCubicleCity = ConvertHelper.convert(cmd,OfficeCubicleCity.class);
			officeCubicleCity.setNamespaceId(UserContext.getCurrentNamespaceId());
			officeCubicleCityProvider.createOfficeCubicleCity(officeCubicleCity);
		}
	}

	private void checkCityName(String provinceName, String cityName) {
		if(StringUtils.isEmpty(cityName) || StringUtils.isEmpty(cityName)){
			throw RuntimeErrorException.errorWith(OfficeCubicleErrorCode.SCOPE, OfficeCubicleErrorCode.ERROR_EMPTY_CITYS,
					"城市名称不能为空");
		}
	}

	@Override
	public void reOrderCity(ReOrderCityCommand cmd) {
		OfficeCubicleCity officeCubicleCity1 = officeCubicleCityProvider.findOfficeCubicleCityById(cmd.getCityid1());

		if(officeCubicleCity1==null){
			throw RuntimeErrorException.errorWith(OfficeCubicleErrorCode.SCOPE, OfficeCubicleErrorCode.ERROR_CITY_ID,
					"cityid1未找到");
		}
		OfficeCubicleCity officeCubicleCity2 = officeCubicleCityProvider.findOfficeCubicleCityById(cmd.getCityid2());
		if(officeCubicleCity2==null){
			throw RuntimeErrorException.errorWith(OfficeCubicleErrorCode.SCOPE, OfficeCubicleErrorCode.ERROR_CITY_ID,
					"cityid2未找到");
		}
		Long order  = officeCubicleCity1.getDefaultOrder();
		officeCubicleCity1.setDefaultOrder(officeCubicleCity2.getDefaultOrder());
		officeCubicleCity2.setDefaultOrder(order);

		dbProvider.execute(r->{
			officeCubicleCityProvider.updateOfficeCubicleCity(officeCubicleCity1);
			officeCubicleCityProvider.updateOfficeCubicleCity(officeCubicleCity2);
			return null;
		});

	}

	@Override
	public CityDTO getCityById(GetCityByIdCommand cmd) {
		OfficeCubicleCity city = officeCubicleCityProvider.findOfficeCubicleCityById(cmd.getCityId());
		if(UserContext.getCurrentNamespaceId().intValue()!=city.getNamespaceId()){
			return null;
		}
		return ConvertHelper.convert(city,CityDTO.class);
	}

	@Override
	public ListCitiesResponse listProvinceAndCites(ListCitiesCommand cmd) {
//		查询自定义配置标识
		GetCustomizeCommand newCmd = ConvertHelper.convert(cmd,GetCustomizeCommand.class);
		Byte custmFlag = getProjectCustomize(newCmd);
		List<OfficeCubicleCity> list=null;
		if(custmFlag.equals((byte)1)){
			if(cmd.getParentName()==null){
				list = officeCubicleCityProvider.listOfficeCubicleProvince(UserContext.getCurrentNamespaceId(),cmd.getOwnerId());
			}else{
				list = officeCubicleCityProvider.listOfficeCubicleCitiesByProvince(cmd.getParentName(),UserContext.getCurrentNamespaceId(),cmd.getOwnerId());
			}
		}else{
			if(cmd.getParentName()==null){
				list = officeCubicleCityProvider.listOfficeCubicleProvince(UserContext.getCurrentNamespaceId(),null);
			}else{
				list = officeCubicleCityProvider.listOfficeCubicleCitiesByProvince(cmd.getParentName(),UserContext.getCurrentNamespaceId(),null);
			}
		}

		return new ListCitiesResponse(list.stream().map(r->ConvertHelper.convert(r, CityDTO.class)).collect(Collectors.toList()));
	}

	@Override
	public ListCitiesResponse copyCities(CopyCitiesCommand cmd) {

		checkOwnerTypeOwnerId(cmd.getOwnerType(),cmd.getOwnerId());

		OfficeCubicleConfig config = officeCubicleProvider.findConfigByOwnerId(cmd.getOwnerType(),cmd.getOwnerId());

		if (null != config && config.getCustomizeFlag().equals(TrueOrFalseFlag.TRUE.getCode())){
			throw RuntimeErrorException.errorWith(OfficeCubicleErrorCode.SCOPE, OfficeCubicleErrorCode.ERROR_AlREADY_CUSTOMIZE_CONFIG,
					"Already customize config");
		}

		List<OfficeCubicleCity> generalCities = officeCubicleCityProvider.listOfficeCubicleCityByOrgId(cmd.getOrgId());

		List<OfficeCubicleCity> cities = officeCubicleCityProvider.listOfficeCubicleCityByOwnerId(cmd.getOwnerType(),cmd.getOwnerId());

		if(null != cities){
			cities.stream().forEach(r ->{
				officeCubicleCityProvider.deleteOfficeCubicleCity(r.getId());
			});
		}
		if(null != generalCities){
			generalCities.stream().forEach(r ->{
				r.setOwnerType(cmd.getOwnerType());
				r.setOwnerId(cmd.getOwnerId());
				officeCubicleCityProvider.createOfficeCubicleCity(r);
			});
		}
		ListCitiesCommand listcmd = new ListCitiesCommand();
		listcmd.setOwnerType(cmd.getOwnerType());
		listcmd.setOwnerId(cmd.getOwnerId());
		listcmd.setOrgId(cmd.getOrgId());
		ListCitiesResponse response = this.listCities(listcmd);

		config.setCustomizeFlag(TrueOrFalseFlag.TRUE.getCode());
		officeCubicleProvider.updateConfig(config);

		return response;
	}

	@Override
	public ListCitiesResponse removeCustomizedCities(CopyCitiesCommand cmd) {

		checkOwnerTypeOwnerId(cmd.getOwnerType(),cmd.getOwnerId());

		OfficeCubicleConfig config = officeCubicleProvider.findConfigByOwnerId(cmd.getOwnerType(),cmd.getOwnerId());

		if (null != config && config.getCustomizeFlag().equals(TrueOrFalseFlag.FALSE.getCode())){
			throw RuntimeErrorException.errorWith(OfficeCubicleErrorCode.SCOPE, OfficeCubicleErrorCode.ERROR_AlREADY_GENERAL_CONFIG,
					"Already general config");
		}

		List<OfficeCubicleCity> cities = officeCubicleCityProvider.listOfficeCubicleCityByOwnerId(cmd.getOwnerType(),cmd.getOwnerId());
		if(null != cities){
			cities.stream().forEach(r -> {
				officeCubicleCityProvider.deleteOfficeCubicleCity(r.getId());
			});
		}
		ListCitiesCommand listcmd = new ListCitiesCommand();
		listcmd.setOrgId(cmd.getOrgId());
		ListCitiesResponse response = this.listCities(listcmd);

		config.setCustomizeFlag(TrueOrFalseFlag.FALSE.getCode());
		officeCubicleProvider.updateConfig(config);

		return response;
	}

	@Override
	public Byte getProjectCustomize(GetCustomizeCommand cmd) {
		checkOwnerTypeOwnerId(cmd.getOwnerType(),cmd.getOwnerId());
		OfficeCubicleConfig config = officeCubicleProvider.findConfigByOwnerId(cmd.getOwnerType(),cmd.getOwnerId());
		if(null != config){
			return config.getCustomizeFlag();
		}else{
			config = new OfficeCubicleConfig();
			config.setOwnerId(cmd.getOwnerId());
			config.setOwnerType(cmd.getOwnerType());
			config.setOrgId(cmd.getOrgId());
			config.setNamespaceId(UserContext.getCurrentNamespaceId());
			config.setCustomizeFlag(TrueOrFalseFlag.FALSE.getCode());
			officeCubicleProvider.createConfig(config);
		}
		return config.getCustomizeFlag();
	}

	@Override
	public Byte getCurrentProjectOnlyFlag(GetCurrentProjectOnlyFlagCommand cmd) {
		String currentProjectOnly = configurationProvider.getValue(cmd.getNamespaceId(),"officecubicle.currentProjectOnly","0");
		return Byte.valueOf(currentProjectOnly);
	}
	
	@Override
	public void addCubicle(AddCubicleAdminCommand cmd) {
		if (cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4920049500L, cmd.getAppId(), null, cmd.getCurrentProjectId());//资源管理权限
		}

		this.dbProvider.execute((TransactionStatus status) -> {
			OfficeCubicleStation station = ConvertHelper.convert(cmd, OfficeCubicleStation.class);
			station.setOwnerType(cmd.getOwnerType());
			station.setOwnerId(cmd.getOwnerId());
			station.setNamespaceId(cmd.getNamespaceId());
			station.setStatus((byte)1);
			officeCubicleProvider.createCubicleSite(station);

			return null;
		});
	}
	
	@Override
	public void addRoom(AddRoomAdminCommand cmd) {
		if (cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4920049500L, cmd.getAppId(), null, cmd.getCurrentProjectId());//资源管理权限
		}

		this.dbProvider.execute((TransactionStatus status) -> {
			OfficeCubicleRoom room = ConvertHelper.convert(cmd, OfficeCubicleRoom.class);
			room.setOwnerType(cmd.getOwnerType());
			room.setOwnerId(cmd.getOwnerId());
			room.setNamespaceId(cmd.getNamespaceId());
			room.setStatus((byte)1);
			officeCubicleProvider.createCubicleRoom(room);
			if(cmd.getAssociateStation()!= null){
				for(AssociateStationDTO dto :cmd.getAssociateStation()){
					OfficeCubicleStation station = officeCubicleProvider.getOfficeCubicleStationById(dto.getStationId());
					station.setAssociateRoomId(room.getId());
					officeCubicleProvider.updateCubicle(station);
				}
			}
			return null;
		});
	}
	
	@Override
	public void deleteRoom(DeleteRoomAdminCommand cmd){
		if (cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4920049500L, cmd.getAppId(), null, cmd.getCurrentProjectId());//资源管理权限
		}
		OfficeCubicleRoom room = officeCubicleProvider.getOfficeCubicleRoomById(cmd.getRoomId());
		OfficeCubicleStation station = officeCubicleProvider.getOfficeCubicleStationById(cmd.getRoomId());
		station.setAssociateRoomId(0L);
		officeCubicleProvider.updateCubicle(station);
		officeCubicleProvider.deleteRoom(room);
	}
    
	@Override
	public void deleteCubicle(DeleteCubicleAdminCommand cmd){
		if (cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4920049500L, cmd.getAppId(), null, cmd.getCurrentProjectId());//资源管理权限
		}
		OfficeCubicleStation station = officeCubicleProvider.getOfficeCubicleStationById(cmd.getStationId());
		officeCubicleProvider.deleteStation(station);
	}
	
	@Override
	public void refundOrder(RefundOrderCommand cmd){
		OfficeCubicleRentOrder order = this.officeCubicleProvider.findOfficeCubicleRentOrderById(cmd.getOrderId());
		if(order==null){
			throw RuntimeErrorException.errorWith(OfficeCubicleErrorCode.SCOPE,
					OfficeCubicleErrorCode.ERROR_REFUND_ERROR,
					"支付系统订单号不存在");
		}
		OfficeCubicleSpace space = this.officeCubicleProvider.getSpaceById(cmd.getSpaceId());
		if(space.getRefundStrategy()!=null){
			if (space.getRefundStrategy().equals(RentalOrderStrategy.NONE.getCode())){
				order.setOrderStatus(OfficeCubicleOrderStatus.REFUNDED.getCode());
				this.officeCubicleProvider.updateCubicleRentOrder(order);
				return;
			}
		} else {
			order.setOrderStatus(OfficeCubicleOrderStatus.REFUNDED.getCode());
			this.officeCubicleProvider.updateCubicleRentOrder(order);
			return;
		}
		if (order.getPrice().compareTo(new BigDecimal(0.00)) == 0){
			order.setOrderStatus(OfficeCubicleOrderStatus.REFUNDED.getCode());
			this.officeCubicleProvider.updateCubicleRentOrder(order);
			return;
		}
		 CreateRefundOrderCommand createRefundOrderCommand = new CreateRefundOrderCommand();
	        String systemId = configurationProvider.getValue(0, PaymentConstants.KEY_SYSTEM_ID, "");
	        createRefundOrderCommand.setBusinessSystemId(Long.parseLong(systemId));
	        createRefundOrderCommand.setAccountCode("NS"+order.getNamespaceId().toString());
	        BigDecimal refundPrice = calculateRefundAmount(order,System.currentTimeMillis(),space);
	        createRefundOrderCommand.setBusinessOrderNumber(order.getBizOrderNo());
	        createRefundOrderCommand.setAmount(refundPrice.multiply(new BigDecimal(100)).longValue());
	        createRefundOrderCommand.setBusinessOperatorType(BusinessPayerType.USER.getCode());
	        createRefundOrderCommand.setBusinessOperatorId(String.valueOf(UserContext.currentUserId()));
	        String homeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"home.url", "");
			String callbackurl = homeUrl + contextPath + configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"officecubicle.pay.callBackUrl", "/officecubicle/payNotify");
	        createRefundOrderCommand.setCallbackUrl(callbackurl);
	        createRefundOrderCommand.setSourceType(SourceType.MOBILE.getCode());

	        CreateRefundOrderRestResponse refundOrderRestResponse = this.orderService.createRefundOrder(createRefundOrderCommand);
	        if(refundOrderRestResponse != null && refundOrderRestResponse.getErrorCode() != null && refundOrderRestResponse.getErrorCode().equals(HttpStatus.OK.value())){

	        } else{
	            LOGGER.error("Refund failed from vendor, refundOrderNo={}, order={}, response={}", order.getOrderNo(), order,
	                    refundOrderRestResponse);
	            throw RuntimeErrorException.errorWith(OfficeCubicleErrorCode.SCOPE,
	            		OfficeCubicleErrorCode.ERROR_REFUND_ERROR,
	                    "bill refund error");
	        }
	        order.setBizOrderNo(refundOrderRestResponse.getResponse().getBusinessOrderNumber());
	        officeCubicleProvider.updateCubicleRentOrder(order);
	}
	
	@Override
	public void updateRoom(AddRoomAdminCommand cmd) {
		if (cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4920049500L, cmd.getAppId(), null, cmd.getCurrentProjectId());//资源管理权限
		}

		this.dbProvider.execute((TransactionStatus status) -> {
			OfficeCubicleRoom room = officeCubicleProvider.getOfficeCubicleRoomById(cmd.getRoomId());
			if(cmd.getAssociateStation()!= null){
				List<OfficeCubicleStation> associateStation =
						officeCubicleProvider.getOfficeCubicleStation(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSpaceId(), cmd.getRoomId(), (byte)1, null, null, null);
				for(OfficeCubicleStation s : associateStation){
					s.setAssociateRoomId(null);
					officeCubicleProvider.updateCubicle(s);
				}
				for(AssociateStationDTO dto :cmd.getAssociateStation()){
					OfficeCubicleStation station = officeCubicleProvider.getOfficeCubicleStationById(dto.getStationId());
					station.setAssociateRoomId(cmd.getRoomId());
					officeCubicleProvider.updateCubicle(station);
				}
			}	
			room.setOwnerId(cmd.getOwnerId());
			room.setOwnerType(cmd.getOwnerType());
			room.setNamespaceId(cmd.getNamespaceId());
			room.setSpaceId(cmd.getSpaceId());
			room.setDescription(cmd.getDescription());
			room.setNamespaceId(cmd.getNamespaceId());
			room.setCoverUri(cmd.getCoverUri());
			room.setPrice(cmd.getPrice());
			room.setRentFlag(cmd.getRentFlag());
			room.setRoomName(cmd.getRoomName());
			room.setId(cmd.getRoomId());
			officeCubicleProvider.updateRoom(room);

			return null;
		});
	}
	
	@Override
	public void updateCubicle(AddCubicleAdminCommand cmd) {
		if (cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4920049500L, cmd.getAppId(), null, cmd.getCurrentProjectId());//资源管理权限
		}

		this.dbProvider.execute((TransactionStatus status) -> {
			OfficeCubicleStation station = officeCubicleProvider.getOfficeCubicleStationById(cmd.getStationId());
			station.setOwnerId(cmd.getOwnerId());
			station.setOwnerType(cmd.getOwnerType());
			station.setNamespaceId(cmd.getNamespaceId());
			station.setSpaceId(cmd.getSpaceId());
			station.setId(cmd.getStationId());
			station.setDescription(cmd.getDescription());
			station.setNamespaceId(cmd.getNamespaceId());
			station.setCoverUri(cmd.getCoverUri());
			station.setPrice(cmd.getPrice());
			station.setRentFlag(cmd.getRentFlag());
			station.setStationName(cmd.getStationName());
			station.setAssociateRoomId(cmd.getAssociateRoom());
			officeCubicleProvider.updateCubicle(station);

			return null;
		});
	}
	
	@Override
	public PreOrderDTO createCubicleOrderV2(CreateOfficeCubicleOrderCommand cmd){
		OfficeCubicleRentOrder order = ConvertHelper.convert(cmd, OfficeCubicleRentOrder.class);
		RentalOrder rentalOrder = rentalv2Provider.findRentalBillById(cmd.getRentalOrderNo());
		order.setUseDetail(rentalOrder.getUseDetail());
		order.setRentCount(rentalOrder.getRentalCount().longValue());
		Long orderNo = onlinePayService.createBillId(DateHelper.currentGMTTime().getTime());
		List<Rentalv2PriceRule> price = rentalv2PriceRuleProvider.listPriceRuleByOwner("station_booking",
				PriceRuleType.RESOURCE.getCode(), cmd.getSpaceId());
		BigDecimal amount = new BigDecimal(0);
		for (Rentalv2PriceRule r : price){
			if (rentalOrder.getRentalType() == r.getRentalType()){
				if(r.getWorkdayPrice()!=null){
					amount = r.getWorkdayPrice();
				}
			}
		}
		order.setPrice(amount);
		order.setOrderNo(orderNo);
		order.setOwnerId(cmd.getOwnerId());
		order.setOwnerType(cmd.getOwnerType());
		order.setNamespaceId(UserContext.getCurrentNamespaceId());
		order.setReserverEnterpriseName(cmd.getReserverEnterpriseName());
		order.setReserverEnterpriseId(cmd.getReserverEnterpriseId());
		order.setReserverContactToken(cmd.getReserverContactToken());
		order.setReserverName(cmd.getReserverName());
		order.setRentType(OfficeCubicleRentType.SHORT_RENT.getCode());
		order.setReserverUid(UserContext.currentUserId());
		order.setRequestType(OfficeCubicleRequestType.CLIENT.getCode());
		this.dbProvider.execute((TransactionStatus status) -> {
			officeCubicleProvider.createCubicleRentOrder(order);
			return null;
		});
		List<OfficeCubicleStationRent> stationRent = 
				officeCubicleProvider.searchCubicleStationRent(cmd.getSpaceId(),UserContext.getCurrentNamespaceId());
		OfficeCubicleSpace space = officeCubicleProvider.getSpaceById(cmd.getSpaceId());
		Integer rentNums = Integer.valueOf(space.getShortRentNums());
		this.coordinationProvider.getNamedLock(CoordinationLocks.OFFICE_CUBICLE_STATION_RENT.getCode() + order.getOrderNo()).enter(()-> {
			int rentSize = 0;
			if (stationRent !=null){
				rentSize = stationRent.size();
			}
			if (rentNums<(rentSize+rentalOrder.getRentalCount().longValue())){
				throw RuntimeErrorException.errorWith(OfficeCubicleErrorCode.SCOPE, OfficeCubicleErrorCode.STATION_NOT_ENOUGH,
				"工位数量不足");
			}
			return null;
		});
		AddRentalOrderUsingInfoCommand rentalCommand = new AddRentalOrderUsingInfoCommand(); 
		rentalCommand.setRentalBillId(cmd.getRentalOrderNo());
		rentalCommand.setRentalType(cmd.getRentalType());
		AddRentalOrderUsingInfoResponse rentalResponse = rentalv2Service.addRentalOrderUsingInfo(rentalCommand);
		order.setRentalOrderNo(rentalResponse.getBillId());
		order.setSpaceName(space.getName());
		PreOrderDTO preDto = new PreOrderDTO();
		if (amount.compareTo(new BigDecimal(0.00)) == 0){
			order.setOrderStatus(OfficeCubicleOrderStatus.PAID.getCode());
			order.setOperateTime(new Timestamp(System.currentTimeMillis()));
			order.setOperatorUid(UserContext.currentUserId());
			rentalCommonService.rentalOrderSuccess(order.getRentalOrderNo());
			int templateId = SmsTemplateCode.OFFICE_CUBICLE_NOT_USE;
			List<Tuple<String, Object>> variables =  smsProvider.toTupleList("spaceName", order.getSpaceName());
			smsProvider.addToTupleList(variables, "reserveTime", order.getUseDetail());
			smsProvider.addToTupleList(variables, "orderNo", order.getOrderNo());
			sendMessageToUser(UserContext.getCurrentNamespaceId(),order.getCreatorUid(),templateId, variables);
			OfficeCubicleStationRent rent = ConvertHelper.convert(cmd, OfficeCubicleStationRent.class);
			rent.setOrderId(order.getId());
			for(int i=0;i<= order.getRentCount() ;i++){
				officeCubicleProvider.createCubicleStationRent(rent);
			}
			officeCubicleProvider.updateCubicleRentOrder(order);
			preDto.setOrderId(order.getId());
			return preDto;
		}
		User user = UserContext.current().getUser();
		String sNamespaceId = BIZ_ACCOUNT_PRE+UserContext.getCurrentNamespaceId();		//todoed
		TargetDTO userTarget = userProvider.findUserTargetById(user.getId());
		CreateOrderCommand createOrderCommand = new CreateOrderCommand();
		List<OfficeCubiclePayeeAccount> payeeAccounts = officeCubiclePayeeAccountProvider.findRepeatOfficeCubiclePayeeAccounts(null, UserContext.getCurrentNamespaceId(),
				cmd.getOwnerType(), cmd.getOwnerId(),cmd.getSpaceId());
		if(payeeAccounts==null || payeeAccounts.size()==0){
			throw RuntimeErrorException.errorWith(OfficeCubicleErrorCode.SCOPE, OfficeCubicleErrorCode.ERROR_NO_PAYEE_ACCOUNT,
					"");
		}
		order.setAccountName(payeeAccounts.get(0).getAccountName());
		String extendInfo = "工位预定订单";
		//根据merchantId获取payeeId
		GetPayUserByMerchantIdCommand getPayUserByMerchantIdCommand = new GetPayUserByMerchantIdCommand();
		getPayUserByMerchantIdCommand.setMerchantId(payeeAccounts.get(0).getAccountId());
		GetPayerInfoByMerchantIdRestResponse getPayerInfoByMerchantIdRestResponse = orderService.getPayerInfoByMerchantId(getPayUserByMerchantIdCommand);
		createOrderCommand.setAccountCode(sNamespaceId);
		createOrderCommand.setPayeeUserId(getPayerInfoByMerchantIdRestResponse.getResponse().getId());
		List<Rentalv2PriceRule> priceRule = rentalv2PriceRuleProvider.listPriceRuleByOwner("station_booking", PriceRuleType.RESOURCE.getCode(), cmd.getSpaceId());
		createOrderCommand.setAmount(priceRule.get(0).getWorkdayPrice().multiply(new BigDecimal(100)).longValue());
		createOrderCommand.setExtendInfo(extendInfo);
		createOrderCommand.setGoodsName(extendInfo);
		createOrderCommand.setClientAppName(cmd.getClientAppName());
        String homeurl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"home.url", "");
//		String homeurl = "http://10.1.110.79:8080";
		String callbackurl = homeurl + contextPath + configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"officecubicle.pay.callBackUrl", "/officecubicle/payNotify");
		createOrderCommand.setBackUrl(callbackurl);
		createOrderCommand.setSourceType(1);
		if (cmd.getPaymentType() != null && cmd.getPaymentType() == PaymentType.WECHAT_JS_PAY.getCode()) {
			createOrderCommand.setPaymentType(PaymentType.WECHAT_JS_ORG_PAY.getCode());
			Map<String, String> flattenMap = new HashMap<>();
			flattenMap.put("acct",user.getNamespaceUserToken());
//			String vspCusid = configProvider.getValue(UserContext.getCurrentNamespaceId(), "tempVspCusid", "550584053111NAJ");
//			flattenMap.put("vspCusid",vspCusid);
			flattenMap.put("payType","no_credit");
			createOrderCommand.setPaymentParams(flattenMap);
			createOrderCommand.setCommitFlag(1);
			createOrderCommand.setOrderType(3);
			createOrderCommand.setAccountCode("NS"+UserContext.getCurrentNamespaceId());
		}
		createOrderCommand.setOrderRemark1("工位预定订单");
		LOGGER.info("createPurchaseOrder params"+createOrderCommand);
		CreatePurchaseOrderCommand createPurchaseOrderCommand = convertToGorderCommand(createOrderCommand);
		CreatePurchaseOrderRestResponse createOrderResp = orderService.createPurchaseOrder(createPurchaseOrderCommand);//统一订单
		if(!checkOrderRestResponseIsSuccess(createOrderResp)){
			LOGGER.info("purchaseOrderRestResponse "+createOrderResp);
			throw RuntimeErrorException.errorWith(OfficeCubicleErrorCode.SCOPE, OfficeCubicleErrorCode.PARAMTER_UNUSUAL,
					"preorder failed "+StringHelper.toJsonString(createOrderResp));
		}
		PurchaseOrderCommandResponse orderCommandResponse = createOrderResp.getResponse();

		OrderCommandResponse response = orderCommandResponse.getPayResponse();
		preDto = ConvertHelper.convert(response,PreOrderDTO.class);
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
		preDto.setOrderId(order.getId());
		preDto.setAmount(priceRule.get(0).getWorkdayPrice().multiply(new BigDecimal(100)).longValue());
		order.setBizOrderNo(response.getBizOrderNum());
		order.setGeneralOrderId(response.getOrderId());
		officeCubicleProvider.updateCubicleRentOrder(order);
		return preDto;
		
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
	
	@Override
	public ListCitiesByOrgIdAndCommunitIdResponse listCitiesByOrgIdAndCommunitId (ListCitiesByOrgIdAndCommunitIdCommand cmd){
		List<OrganizationCommunityDTO> orgcoms = organizationProvider.findOrganizationCommunityByCommunityId(cmd.getCommunityId());
		Organization org = null;
		if(orgcoms != null && orgcoms.size() > 0){
			org = organizationProvider.findOrganizationById(orgcoms.get(0).getOrganizationId());
		}
		ListCommunitiesByOrgIdAndAppIdCommand cmd2 = new ListCommunitiesByOrgIdAndAppIdCommand();
		cmd2.setAppId(cmd.getAppId());
		cmd2.setOrgId(org.getId());
		
		ListCommunitiesByOrgIdAndAppIdResponse resp2 = communityService.listCommunitiesByOrgIdAndAppId(cmd2);
		Community community = this.communityProvider.findCommunityById(cmd.getCommunityId());
		ListCitiesByOrgIdAndCommunitIdResponse resp = new ListCitiesByOrgIdAndCommunitIdResponse();
		List<Long> communityIds = new ArrayList<Long>();
		for(ProjectDTO dto : resp2.getDtos()){
			communityIds.add(dto.getProjectId());
		}
		List<CityForAppDTO> cities = new ArrayList<CityForAppDTO>();
		Map<Long, Community> temp = officeCubicleProvider.listCommunitiesByIds(communityIds);
		temp.values().stream().map(r -> {
			CityForAppDTO dto = new CityForAppDTO();
			dto.setCityId(r.getCityId());
			dto.setCityName(r.getCityName());
			cities.add(dto);
			return cities;
		}).collect(Collectors.toList());
		resp.setCity(cities);
		CityForAppDTO dto = new	CityForAppDTO();
		dto.setCityId(community.getCityId());
		dto.setCityName(community.getCityName());
		resp.setDefaultCity(dto);
		return resp;
	}
	@Override
	public void payNotify (MerchantPaymentNotificationCommand cmd){

		if(!PayUtil.verifyCallbackSignature(cmd)){
			LOGGER.error("Failed to verify pay-callback signature, appKey={}, signature={}", 
					PaySettings.getAppKey(), cmd.getSignature());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"sign verify faild");
		}
		// * RAW(0)：
		// * SUCCESS(1)：支付成功
		// * PENDING(2)：挂起
		// * ERROR(3)：错误
		if(cmd.getPaymentStatus()== null || 1!=cmd.getPaymentStatus()){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"invaild paymentstatus,"+cmd.getPaymentStatus());
		}//检查状态

		//检查orderType
		//RECHARGE(1), WITHDRAW(2), PURCHACE(3), REFUND(4);
		//充值，体现，支付，退款
		if(cmd.getOrderType()==null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"invaild ordertype,"+cmd.getOrderType());
		}
		OfficeCubicleRentOrder order = officeCubicleProvider.findOfficeCubicleRentOrderByBizOrderNo(cmd.getBizOrderNum());
		if(cmd.getOrderType() == 3) {
			coordinationProvider.getNamedLock(CoordinationLocks.OFFICE_CUBICLE_ORDER_STATUS.getCode() + order.getId()).enter(()-> {
				order.setOrderStatus(OfficeCubicleOrderStatus.PAID.getCode());
				order.setOperateTime(new Timestamp(System.currentTimeMillis()));
				order.setOperatorUid(UserContext.currentUserId());
				if (cmd.getPaymentType() != null){
					order.setPaidType(transferPaidType(cmd.getPaymentType()));
				}
				if (cmd.getPaymentType() != null && cmd.getPaymentType() == 29)
					order.setPaidMode(GorderPayType.WAIT_FOR_ENTERPRISE_PAY.getCode());
				else
					order.setPaidMode(GorderPayType.PERSON_PAY.getCode());
				officeCubicleProvider.updateCubicleRentOrder(order);
				rentalCommonService.rentalOrderSuccess(order.getRentalOrderNo());
				int templateId = SmsTemplateCode.OFFICE_CUBICLE_NOT_USE;
				List<Tuple<String, Object>> variables =  smsProvider.toTupleList("spaceName", order.getSpaceName());
				smsProvider.addToTupleList(variables, "reserveTime", order.getUseDetail());
				smsProvider.addToTupleList(variables, "orderNo", order.getOrderNo());
				sendMessageToUser(UserContext.getCurrentNamespaceId(),order.getCreatorUid(),templateId, variables);
				OfficeCubicleStationRent rent = ConvertHelper.convert(cmd, OfficeCubicleStationRent.class);
				rent.setOrderId(order.getId());
				for(int i=0;i<= order.getRentCount() ;i++){
					officeCubicleProvider.createCubicleStationRent(rent);
				}
				return null;
			});
		}else if(cmd.getOrderType() == 4){
			order.setOrderStatus(OfficeCubicleOrderStatus.REFUNDED.getCode());
			order.setOperateTime(new Timestamp(System.currentTimeMillis()));
			order.setOperatorUid(UserContext.currentUserId());
			officeCubicleProvider.updateCubicleRentOrder(order);
			rentalCommonService.rentalOrderCancel(order.getRentalOrderNo());
			int templateId = SmsTemplateCode.OFFICE_CUBICLE_REFUND;
			List<Tuple<String, Object>> variables =  smsProvider.toTupleList("spaceName", order.getSpaceId());
			smsProvider.addToTupleList(variables, "orderNo", order.getOrderNo());
			smsProvider.addToTupleList(variables, "price", order.getPrice());
			smsProvider.addToTupleList(variables, "refundPrice", order.getId());
			sendMessageToUser(UserContext.getCurrentNamespaceId(),order.getCreatorUid(),templateId, variables);
		}
	}
	
	/**
	 * 将3.0版本支付的支付方式转为老的支付宝和微信的支付方式
	 * @param paymentType
	 * @return
	 */
	private String transferPaidType(Integer paymentType) {
						/* 支付类型
						 * WECHAT_APPPAY(1): 微信APP支付
						 * GATEWAY_PAY(2): 网关支付
						 * POS_PAY(3): 订单POS
						 * REALNAME_PAY(4): 实名付（单笔）
						 * REALNAME_BATCHPAY(5): 实名付（批量）
						 * WITHOLD(6): 通联通代扣
						 * WECHAT_SCAN_PAY(7):  微信扫码支付(正扫)
						 * ALI_SCAN_PAY(8): 支付宝扫码支付(正扫)
						 * WECHAT_JS_PAY(9): 微信JS 支付（公众号）
						 * ALI_JS_PAY(10): 支付宝JS 支付（生活号）
						 * QQWALLET_JS_PAY(11): QQ 钱包JS 支付
						 * WCHAT_CODE_PAY(12): 微信刷卡支付（被扫）
						 * ALI_CODE_PAY(13): 支付宝刷卡支付(被扫)
						 * QQWALLET_CODE_PAY(14): QQ 钱包刷卡支付(被扫)
						 * BALANCE_PAY(15): 账户余额
						 * COUPON_PAY(16): 代金券（不建议使用，建议使用下面的批量代金券方式）
						 * COUPON_BATCHPAY(17): 批量代金券
						 * WITHDRAW(18): 通联通代付
						 * QUICK_PAY(19):
						 * WITHDRAW_AUTO(20):*/
		if(paymentType==1 || paymentType==7 || paymentType==9 || paymentType==12 || paymentType==21)
			return "10002";
		if(paymentType==8 || paymentType==10 || paymentType==13)
			return "10001";
		return paymentType+"";
	}
	
	private boolean checkOrderRestResponseIsSuccess(CreatePurchaseOrderRestResponse response){
		if(response != null && response.getErrorCode() != null
				&& (response.getErrorCode().intValue() == 200 || response.getErrorCode().intValue() == 201))
			return true;
		return false;
	}
	
    private void sendMessageToUser(Integer namespaceId, Long creatorUid, int templateId,  List<Tuple<String, Object>> variables) {
        String templateScope = SmsTemplateCode.SCOPE;
        String templateLocale = SiyinPrintNotificationTemplateCode.locale;
        //List<Tuple<String, Object>> variables = smsProvider.toTupleList("appName", appName);

        UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(creatorUid,
                IdentifierType.MOBILE.getCode());
        if (null == userIdentifier) {
            LOGGER.error("userIdentifier is null...userId = " + creatorUid);
        } else {
            smsProvider.sendSms(userIdentifier.getNamespaceId(), userIdentifier.getIdentifierToken(), templateScope,
                    templateId, templateLocale, variables);
        }
    }

	private CreatePurchaseOrderCommand convertToGorderCommand(CreateOrderCommand cmd){
		CreatePurchaseOrderCommand preOrderCommand = new CreatePurchaseOrderCommand();
		//PaymentParamsDTO转为Map


		preOrderCommand.setAmount(cmd.getAmount());

		preOrderCommand.setAccountCode(cmd.getAccountCode());
		preOrderCommand.setClientAppName(cmd.getClientAppName());

		preOrderCommand.setBusinessOrderType(OrderType.OrderTypeEnum.OFFICE_CUBICLE.getPycode());
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
		String systemId = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), PaymentConstants.KEY_SYSTEM_ID, "");
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
			buyerPhone = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), PaymentConstants.KEY_ORDER_DEFAULT_PERSONAL_BIND_PHONE, "");
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("businessPayerPhone", buyerPhone);
		return StringHelper.toJsonString(map);
	}
	private String generateBizOrderNum(String sNamespaceId, String pyCode, Long orderNo) {
		return sNamespaceId+BIZ_ORDER_NUM_SPILT+pyCode+BIZ_ORDER_NUM_SPILT+orderNo;
	}
	
	@Override
	public CreateCubicleOrderBackgroundResponse createCubicleOrderBackground(CreateCubicleOrderBackgroundCommand cmd){
		if (cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4920049600L, cmd.getAppId(), null, cmd.getCurrentProjectId());//资源管理权限
		}
		List<OfficeCubicleStationRent> stationRent = 
				officeCubicleProvider.searchCubicleStationRent(cmd.getSpaceId(),UserContext.getCurrentNamespaceId());

		OfficeCubicleSpace space = officeCubicleProvider.getSpaceById(cmd.getSpaceId());	
		OfficeCubicleRentOrder order = ConvertHelper.convert(cmd, OfficeCubicleRentOrder.class);
		order.setBeginTime(new Timestamp(cmd.getBeginTime()));
		order.setEndTime(new Timestamp(cmd.getEndTime()));
		order.setRentType(cmd.getRentType());
		order.setOrderStatus(OfficeCubicleOrderStatus.PAID.getCode());
		order.setOwnerId(cmd.getOwnerId());
		order.setOwnerType(cmd.getOwnerType());
		if (cmd.getRentCount()!=null){
			order.setRentCount(cmd.getRentCount().longValue());
		}
		order.setRequestType(OfficeCubicleRequestType.BACKGROUND.getCode());
		order.setReserverContactToken(cmd.getReserverContactToken());
		order.setReserverEnterpriseId(cmd.getReserverEnterpriseId());
		order.setReserverEnterpriseName(cmd.getReserverEnterpriseName());
		order.setReserverName(cmd.getReserverName());
		if (cmd.getReserverUid()!= null){
			order.setReserverUid(cmd.getReserverUid());
		}
		order.setSpaceId(cmd.getSpaceId());
		Long orderNo = onlinePayService.createBillId(DateHelper.currentGMTTime().getTime());
		order.setOrderNo(orderNo);
		order.setUseDetail(cmd.getUseDetail());
		order.setSpaceName(space.getName());
		this.dbProvider.execute((TransactionStatus status) -> {
			officeCubicleProvider.createCubicleRentOrder(order);
			return null;
		});
		if(cmd.getRentType().equals(OfficeCubicleRentType.SHORT_RENT.getCode())){
			this.coordinationProvider.getNamedLock(CoordinationLocks.OFFICE_CUBICLE_STATION_RENT.getCode() + order.getId()).enter(()-> {
				int rentSize = 0;
				if (stationRent !=null){
					rentSize = stationRent.size();
				}
				int rentCount = 1;
				if (cmd.getRentCount()!=null){
					rentCount = cmd.getRentCount();
				} else {
					Integer stationSize =0;
					Integer roomIdSize = 0;
					if(cmd.getStationId()!=null){
						stationSize = cmd.getStationId().size();
					}
					if (cmd.getRoomId()!=null){
						roomIdSize = cmd.getRoomId().size();
					}
					rentCount = stationSize+roomIdSize;
				}
				if (Integer.valueOf(space.getShortRentNums())<(rentSize+rentCount)){
					throw RuntimeErrorException.errorWith(OfficeCubicleErrorCode.SCOPE, OfficeCubicleErrorCode.STATION_NOT_ENOUGH,
					"工位数量不足");
				} 
				order.setRentType(OfficeCubicleRentType.SHORT_RENT.getCode());
				officeCubicleProvider.updateCubicleRentOrder(order);
				return null;
			});
		} else {
			if(cmd.getStationId()!=null){
				for (Long s :cmd.getStationId()){
					OfficeCubicleStation station =officeCubicleProvider.getOfficeCubicleStationById(s);
					station.setStatus((byte)2);
					station.setBeginTime(new Timestamp(cmd.getBeginTime()));
					station.setEndTime(new Timestamp(cmd.getEndTime()));
					officeCubicleProvider.updateCubicle(station);
				}
			}
			if (cmd.getRoomId()!=null){
				for (Long r :cmd.getRoomId()){
					OfficeCubicleRoom room = officeCubicleProvider.getOfficeCubicleRoomById(r);
					room.setStatus((byte)2);
					room.setBeginTime(new Timestamp(cmd.getBeginTime()));
					room.setEndTime(new Timestamp(cmd.getEndTime()));
					officeCubicleProvider.updateRoom(room);
					List<OfficeCubicleStation> stationList = 
							officeCubicleProvider.getOfficeCubicleStation(cmd.getOwnerId(), cmd.getOwnerType(), space.getId(), r, null, null, null, null);
					for (OfficeCubicleStation s : stationList){
						s.setStatus((byte)2);
						s.setBeginTime(new Timestamp(cmd.getBeginTime()));
						s.setEndTime(new Timestamp(cmd.getEndTime()));
						officeCubicleProvider.updateCubicle(s);
					}
				}
			}
			order.setRentType(OfficeCubicleRentType.LONG_RENT.getCode());
			officeCubicleProvider.updateCubicleRentOrder(order);
		}
		
		if (cmd.getRentalOrderNo()!=null){
			rentalCommonService.rentalOrderSuccess(cmd.getRentalOrderNo());
		}
		return null;
		
	}
	
	
	@Override
	public ListOfficeCubicleAccountDTO getOfficeCubiclePayeeAccount(GetOfficeCubiclePayeeAccountCommand cmd) {
		List<OfficeCubiclePayeeAccount> accounts = officeCubiclePayeeAccountProvider
				.listOfficeCubiclePayeeAccountByOwner(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId());
		if(accounts==null || accounts.size()==0){
			return new ListOfficeCubicleAccountDTO();
		}
		
		ListPayUsersByMerchantIdsCommand cmd2 = new ListPayUsersByMerchantIdsCommand();
        cmd2.setIds(accounts.stream().map(r -> r.getMerchantId()).collect(Collectors.toList()));
        ListPayUsersByMerchantIdsRestResponse restResponse = orderService.listPayUsersByMerchantIds(cmd2);
        List<PayUserDTO> payUserDTOs = restResponse.getResponse();
//		List<PayUserDTO> payUserDTOS = sdkPayService.listPayUsersByIds(accounts.stream().map(r -> r.getPayeeId()).collect(Collectors.toList()));
		Map<Long,PayUserDTO> map = payUserDTOs.stream().collect(Collectors.toMap(PayUserDTO::getId,r->r));
        if (payUserDTOs == null || payUserDTOs.size() == 0)
            return null;
        ListOfficeCubicleAccountDTO dto = convertAccount(payUserDTOs.get(0));
		return dto;
	}
	
    private ListOfficeCubicleAccountDTO convertAccount(PayUserDTO payUserDTO){
        if (payUserDTO == null)
            return null;
        ListOfficeCubicleAccountDTO dto = new ListOfficeCubicleAccountDTO();
        // 支付系统中的用户ID
        dto.setAccountId(payUserDTO.getId());
        // 用户向支付系统注册帐号时填写的帐号名称
        dto.setAccountName(payUserDTO.getRemark());
        dto.setAccountAliasName(payUserDTO.getUserAliasName());//企业名称（认证企业）
        // 帐号类型，1-个人帐号、2-企业帐号
        Integer userType = payUserDTO.getUserType();
        if (userType != null && userType.equals(2)) {
            dto.setAccountType(OwnerType.ORGANIZATION.getCode());
        } else {
            dto.setAccountType(OwnerType.USER.getCode());
        }
        // 企业账户：0未审核 1审核通过  ; 个人帐户：0 未绑定手机 1 绑定手机
        Integer registerStatus = payUserDTO.getRegisterStatus();
        if (registerStatus != null && registerStatus.intValue() == 1) {
            dto.setAccountStatus(PaymentUserStatus.ACTIVE.getCode());
        } else {
            dto.setAccountStatus(PaymentUserStatus.WAITING_FOR_APPROVAL.getCode());
        }
        return  dto;
    }
    
	@Override
	public List<ListOfficeCubicleAccountDTO> listOfficeCubicleAccount(ListOfficeCubicleAccountCommand cmd) {
		ArrayList arrayList = new ArrayList(Arrays.asList("0", cmd.getCommunityId() + ""));
		String key = OwnerType.ORGANIZATION.getCode() + cmd.getOrganizationId();
		LOGGER.info("sdkPayService request params:{} {} ",key,arrayList);
		GetPayUserListByMerchantCommand cmd2 = new GetPayUserListByMerchantCommand();
		cmd2.setUserId(key);
		cmd2.setTag1(arrayList);
		GetMerchantListByPayUserIdRestResponse resp = orderService.getMerchantListByPayUserId(cmd2);
		//List<PayUserDTO> payUserList = sdkPayService.getPayUserList(key,arrayList);
		if(null == resp || null == resp.getResponse()){
			LOGGER.error("resp:"+(null == resp ? null :StringHelper.toJsonString(resp)));
		}
		List<GetPayUserListByMerchantDTO> payUserList = resp.getResponse();
		return payUserList.stream().map(r->{
			ListOfficeCubicleAccountDTO dto = new ListOfficeCubicleAccountDTO();
			dto.setAccountId(r.getId());
			dto.setAccountType(r.getUserType()==2?OwnerType.ORGANIZATION.getCode():OwnerType.USER.getCode());//帐号类型，1-个人帐号、2-企业帐号
			dto.setAccountName(r.getRemark());
			dto.setAccountAliasName(r.getUserAliasName());
	        if (r.getRegisterStatus() != null && r.getRegisterStatus().intValue() == 1) {
	            dto.setAccountStatus(PaymentUserStatus.ACTIVE.getCode());
	        } else {
	            dto.setAccountStatus(PaymentUserStatus.WAITING_FOR_APPROVAL.getCode());
	        }
			return dto;
		}).collect(Collectors.toList());
	}
	
	@Override
	public void createOrUpdateOfficeCubiclePayeeAccount(CreateOrUpdateOfficeCubiclePayeeAccountCommand cmd) {
		officeCubiclePayeeAccountProvider.deleteOfficeCubiclePayeeAccount(null,cmd.getOwnerId());
		OfficeCubiclePayeeAccount account = new OfficeCubiclePayeeAccount();
		account.setAccountId(cmd.getAccountId());
		account.setOwnerId(cmd.getOwnerId());
		account.setOwnerType(cmd.getOwnerType());
		account.setNamespaceId(cmd.getNamespaceId());
		account.setMerchantId(cmd.getAccountId());
		account.setAccountName(cmd.getAccountName());
		officeCubiclePayeeAccountProvider.createOfficeCubiclePayeeAccount(account);
	}
	

	@Override
	public ListOfficeCubicleStatusResponse listOfficeCubicleStatus(ListOfficeCubicleStatusCommand cmd) {
		ListOfficeCubicleStatusResponse resp = new ListOfficeCubicleStatusResponse();
		Integer stationSize = 0;
		List<OfficeCubicleStation> station = 
				officeCubicleProvider.getOfficeCubicleStation(cmd.getOwnerId(), cmd.getOwnerType(),cmd.getSpaceId(), null, null,null,null,null);
		if (station.size() == 0)
			return resp;
		stationSize = station.size();
		resp.setCubicleNums(station.size());
		List<OfficeCubicleStation> longRentStation = 
				officeCubicleProvider.getOfficeCubicleStation(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSpaceId(), null, (byte)1, null, (byte)2, null);
		List<OfficeCubicleStationRent> shortRentStation = officeCubicleProvider.searchCubicleStationRent(cmd.getSpaceId(),UserContext.getCurrentNamespaceId());
		Integer shortRentStationSize = 0;
		Integer longRentStationSize =0;
		Integer closeStationSize = 0;
		if (longRentStation != null){
			longRentStationSize = longRentStation.size();
		}
		if (shortRentStation != null){
			shortRentStationSize = shortRentStation.size();
		}
		OfficeCubicleSpace space = officeCubicleProvider.getSpaceById(cmd.getSpaceId());
		Integer shortRentNums = 0;
		if (space.getShortRentNums()!=null){
			shortRentNums = Integer.valueOf(space.getShortRentNums());
		}
		resp.setShortCubicleIdleNums(shortRentNums-shortRentStationSize);
		resp.setLongCubicleRentedNums(longRentStationSize);
		List<OfficeCubicleStation> closeStation = 
				officeCubicleProvider.getOfficeCubicleStation(cmd.getOwnerId(), cmd.getOwnerType(),cmd.getSpaceId(), null, (byte)0,null,null,null);
		if (closeStation != null){
			closeStationSize = closeStation.size();
		}
		resp.setLongRentCloseCubicleNums(closeStationSize);
		resp.setLongCubicleIdleNums(stationSize-longRentStationSize);

		resp.setShortCubicleRentedNums(shortRentStationSize);
		Integer rentRates =((shortRentStationSize+longRentStationSize)*100)/stationSize;
		resp.setRentRates(rentRates);
		return resp;
	}
	
	@Override
	public GetRoomDetailResponse getRoomDetail(GetRoomDetailCommand cmd){
		GetRoomDetailResponse resp = new GetRoomDetailResponse();
		OfficeCubicleRoom room = officeCubicleProvider.getOfficeCubicleRoomById(cmd.getRoomId());
		List<OfficeCubicleStation> station = 
				officeCubicleProvider.getOfficeCubicleStation(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSpaceId(),room.getId(),null,null,null,null);
		List<AssociateStationDTO> associateStaionList = setAssociateStaion(station);
		RoomDTO dto = ConvertHelper.convert(room,RoomDTO.class);
		dto.setAssociateStation(associateStaionList);
		dto.setRoomId(room.getId());
		dto.setRoomName(room.getRoomName());
		dto.setCoverUrl(this.contentServerService.parserUri(dto.getCoverUri(), EntityType.USER.getCode(),
				UserContext.current().getUser().getId()));
		resp.setRoom(dto);
		return resp;
	}
	
	@Override
	public GetStationForRoomResponse getStationForRoom (GetStationForRoomCommand cmd){
		GetStationForRoomResponse resp = new GetStationForRoomResponse();
		List<OfficeCubicleStation> station = 
				officeCubicleProvider.getOfficeCubicleStation(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSpaceId(),null,(byte)1,cmd.getKeyword(),(byte)1,null);
		List<StationDTO> stationDTO = new ArrayList<StationDTO>();
		for (OfficeCubicleStation s : station){
			StationDTO dto = new StationDTO();
			if (s.getAssociateRoomId() != null){
				continue;
			}
			dto = ConvertHelper.convert(s,StationDTO.class);
			dto.setStationId(s.getId());
			stationDTO.add(dto);
		}
		resp.setStation(stationDTO);
		return resp;
	}
	
	@Override
	public GetCubicleForOrderResponse getCubicleForOrder (GetCubicleForOrderCommand cmd){
		GetCubicleForOrderResponse resp = new GetCubicleForOrderResponse();
		String keyword = "";
		if (StringUtils.isNotBlank(cmd.getKeyword())){
			keyword = cmd.getKeyword();
		}
		List<OfficeCubicleStation> station = 
				officeCubicleProvider.getOfficeCubicleStation(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSpaceId(), null, (byte)1, cmd.getKeyword(), (byte)1, null);
		List<OfficeCubicleStation> rentStation = 
				officeCubicleProvider.getOfficeCubicleStationByTime(cmd.getSpaceId(),(byte)1,cmd.getBeginTime(),cmd.getEndTime(),keyword);
		List<OfficeCubicleRoom> room = 
				officeCubicleProvider.getOfficeCubicleRoom(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSpaceId(),null,(byte)1,null,cmd.getKeyword());
		List<OfficeCubicleRoom> rentRoom = 
				officeCubicleProvider.getOfficeCubicleRoomByTime(cmd.getSpaceId(),(byte)1,cmd.getBeginTime(),cmd.getEndTime(),keyword);
		station.addAll(rentStation);
		room.addAll(rentRoom);
		List<StationDTO> stationDTO = new ArrayList<StationDTO>();
		for (OfficeCubicleStation s : station){
			StationDTO dto = new StationDTO();
			if (s.getAssociateRoomId() != null){
				continue;
			}
			dto = ConvertHelper.convert(s,StationDTO.class);
			dto.setStationId(s.getId());
			stationDTO.add(dto);
		}
		resp.setStation(stationDTO);
		resp.setRoom(room.stream().map(r->{
			RoomDTO dto = ConvertHelper.convert(r,RoomDTO.class);
			dto.setRoomId(r.getId());
			return dto;
			}).collect(Collectors.toList()));
		return resp;
	}
	@Override
	public GetCubicleForAppResponse getCubicleForApp(GetCubicleForAppCommand cmd){
		GetCubicleForAppResponse resp = new GetCubicleForAppResponse();
		List<OfficeCubicleStation> station = 
				officeCubicleProvider.getOfficeCubicleStationNotAssociate(cmd.getSpaceId());
		List<OfficeCubicleAttachment> stationAttachments = this.officeCubicleProvider.listAttachmentsBySpaceId(cmd.getSpaceId(),(byte)3);
		if (null != stationAttachments){
			resp.setStationAttachments(new ArrayList<OfficeAttachmentDTO>());
			stationAttachments.forEach((attachment) -> {
			OfficeAttachmentDTO attachmentDTO = ConvertHelper.convert(attachment, OfficeAttachmentDTO.class);
			attachmentDTO.setContentUrl(this.contentServerService.parserUri(attachment.getContentUri(), EntityType.USER.getCode(),
					UserContext.current().getUser().getId()));
			resp.getStationAttachments().add(attachmentDTO);
			});
		}
		Integer stationNums = 0;
		if (station!=null){
			stationNums = station.size();
		}
		resp.setStationNums(stationNums);
		List<OfficeCubicleRoom> room = officeCubicleProvider.getOfficeCubicleRoom(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSpaceId(),(byte)1,null,null,null);
		if (room!=null){
		resp.setRoom(room.stream().map(r->{
			RoomDTO dto = ConvertHelper.convert(r,RoomDTO.class);
			dto.setRoomId(r.getId());
			List<OfficeCubicleStation> associateStaion = 
					officeCubicleProvider.getOfficeCubicleStation(null, cmd.getOwnerType(), cmd.getSpaceId(),r.getId(),(byte)1,null,null,null);
			List<AssociateStationDTO> associateStaionList = setAssociateStaion(associateStaion);
			dto.setAssociateStation(associateStaionList);
			Long rentDate = System.currentTimeMillis();
			if (r.getEndTime() != null){
		        Calendar c = Calendar.getInstance();
		        c.setTime(r.getEndTime());
		        c.add(Calendar.DAY_OF_MONTH, 1);
		        rentDate = c.getTime().getTime();
			}
			dto.setRentDate(rentDate);
			dto.setCoverUrl(this.contentServerService.parserUri(dto.getCoverUri(), EntityType.USER.getCode(),
					UserContext.current().getUser().getId()));
			dto.setStatus(r.getStatus());
			return dto;
			}).collect(Collectors.toList()));
		}
		BigDecimal minStationPrice = officeCubicleProvider.getStationMinPrice(cmd.getSpaceId());
		if (minStationPrice == null){
			minStationPrice = new BigDecimal(0);
		}
		resp.setMinStationPrice(minStationPrice);
		return resp;
	}
	private List<AssociateStationDTO> setAssociateStaion(List<OfficeCubicleStation> station){
		List<AssociateStationDTO> associateStaionList = new ArrayList<AssociateStationDTO>();
		for (OfficeCubicleStation s :station){
			AssociateStationDTO dto = new AssociateStationDTO();
			dto.setStationId(s.getId());
			dto.setStationName(s.getStationName());
			associateStaionList.add(dto);
		}
		return associateStaionList;
	}

	@Override
	public GetStationDetailResponse getCubicleDetail(GetStationDetailCommand cmd){
		GetStationDetailResponse resp = new GetStationDetailResponse();
		OfficeCubicleStation station =  officeCubicleProvider.getOfficeCubicleStationById(cmd.getStationId());
		StationDTO dto = new StationDTO();
		dto = ConvertHelper.convert(station,StationDTO.class);
		dto.setAssociateRoomId(station.getAssociateRoomId());
		OfficeCubicleRoom room = officeCubicleProvider.getOfficeCubicleRoomById(station.getAssociateRoomId());
		if (room != null){
			dto.setAssociateRoomName(room.getRoomName());
		}
		dto.setCoverUrl(this.contentServerService.parserUri(dto.getCoverUri(), EntityType.USER.getCode(),
				UserContext.current().getUser().getId()));
		dto.setStationId(station.getId());
		resp.setStation(dto);
		return resp;
	}
	
	@Override
	public GetRoomByStatusResponse getRoomByStatus(GetRoomByStatusCommand cmd){
		GetRoomByStatusResponse resp = new GetRoomByStatusResponse();
		List<OfficeCubicleRoom> room = new ArrayList<OfficeCubicleRoom>();
		if(cmd.getStatus() == 0){
			room = officeCubicleProvider.getOfficeCubicleRoom(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSpaceId(),cmd.getStatus(),null,null,null);
		} else{
			room = officeCubicleProvider.getOfficeCubicleRoom(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSpaceId(),(byte)1,cmd.getStatus(),null,null);
		}
		resp.setRoom(room.stream().map(r->{
			RoomDTO dto = ConvertHelper.convert(r,RoomDTO.class);
			List<OfficeCubicleStation> station = 
					officeCubicleProvider.getOfficeCubicleStation(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSpaceId(),r.getId(),null,null,null,null);
			List<AssociateStationDTO> associateStaionList = setAssociateStaion(station);
			dto.setAssociateStation(associateStaionList);
			dto.setRoomId(r.getId());
			return dto;
			}).collect(Collectors.toList()));
		return resp;

	}
	
	@Override
	public GetCubicleByStatusResponse getCubicleByStatus(GetCubicleByStatusCommand cmd){
		GetCubicleByStatusResponse resp = new GetCubicleByStatusResponse();
		List<OfficeCubicleStation> station = new ArrayList<OfficeCubicleStation>();
		if (cmd.getStatus() != null){
			if (cmd.getStatus() ==0){
				station = officeCubicleProvider.getOfficeCubicleStation(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSpaceId(),null,(byte)0,null,null,null);
			} else {
				station = officeCubicleProvider.getOfficeCubicleStation(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSpaceId(),null,(byte)1,null,cmd.getStatus(),null);
			}
		}
		List<StationDTO> stationDTO = new ArrayList<StationDTO>();
		for (OfficeCubicleStation s : station){
			StationDTO dto = new StationDTO();
			if (s.getAssociateRoomId() != null ){
				if (s.getAssociateRoomId().compareTo(0L)!=0){
					continue;
				}
			}
			dto = ConvertHelper.convert(s,StationDTO.class);
			dto.setStationId(s.getId());
			stationDTO.add(dto);
		}
		resp.setStation(stationDTO);
		return resp;
	}
	@Override
	public SearchCubicleOrdersResponse searchCubicleOrders(SearchCubicleOrdersCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4920049600L, cmd.getAppId(), null,cmd.getCurrentProjectId());//预定详情权限
		}

		SearchCubicleOrdersResponse response = new SearchCubicleOrdersResponse();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(Long.MAX_VALUE);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<OfficeCubicleRentOrder> orders = this.officeCubicleProvider.searchCubicleOrders(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getBeginTime(), cmd.getEndTime(),
				 locator, pageSize + 1, getNamespaceId(cmd.getNamespaceId()),cmd.getPaidType(),cmd.getPaidMode(),cmd.getRequestType(),cmd.getRentType(), cmd.getOrderStatus());
		if (null == orders)
			return response;
		Long nextPageAnchor = null;
		if (orders != null && orders.size() > pageSize) {
			orders.remove(orders.size() - 1);
			nextPageAnchor = orders.get(orders.size() - 1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setOrders(new ArrayList<OfficeRentOrderDTO>());

		orders.forEach((other) -> {
			OfficeRentOrderDTO dto = ConvertHelper.convert(other, OfficeRentOrderDTO.class);
			dto.setAccountName(other.getAccountName());
			dto.setCreateTime(other.getCreateTime().getTime());
			dto.setOrderNo(other.getOrderNo());
			if (other.getRentType() == 1){
				dto.setBeginTime(other.getBeginTime().getTime());
				dto.setEndTime(other.getEndTime().getTime());
			} else if(other.getRentType() == 0){
				dto.setUserDetail(other.getUseDetail());
			}
			dto.setSpaceName(other.getSpaceName());
			response.getOrders().add(dto);
		});

		return response;
	}
	
	@Override
	public ListRentOrderForAppResponse listRentOrderForApp(ListRentOrderForAppCommand cmd) {

		ListRentOrderForAppResponse response = new ListRentOrderForAppResponse();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(Long.MAX_VALUE);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<OfficeCubicleRentOrder> orders = this.officeCubicleProvider.searchCubicleOrdersByUid(
				 locator, pageSize + 1, getNamespaceId(cmd.getNamespaceId()),cmd.getRentType(), cmd.getOrderStatus(), UserContext.currentUserId());
		if (null == orders)
			return response;
		Long nextPageAnchor = null;
		if (orders != null && orders.size() > pageSize) {
			orders.remove(orders.size() - 1);
			nextPageAnchor = orders.get(orders.size() - 1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setOrders(new ArrayList<OfficeRentOrderDTO>());
		orders.forEach((other) -> {
			OfficeRentOrderDTO dto = ConvertHelper.convert(other, OfficeRentOrderDTO.class);
			dto.setCreateTime(other.getCreateTime().getTime());
			if (cmd.getRentType() == 0){
				GetRentalBillCommand cmd2 = new GetRentalBillCommand();
				cmd2.setBillId(other.getRentalOrderNo());
				RentalBillDTO rentalDTO = rentalv2Service.getRentalBill(cmd2);
				dto.setOpenTime(rentalDTO.getOpenTime());
				dto.setUserDetail(other.getUseDetail());
			}else if (other.getRentType() == 1){
				dto.setBeginTime(other.getBeginTime().getTime());
				dto.setEndTime(other.getEndTime().getTime());
			}
			dto.setSpaceName(other.getSpaceName());
			response.getOrders().add(dto);
		});

		return response;
	}
	
	
	@Override
	public GetOfficeCubicleRentOrderResponse getOfficeCubicleRentOrder(GetOfficeCubicleRentOrderCommand cmd){
		GetOfficeCubicleRentOrderResponse response = new GetOfficeCubicleRentOrderResponse();
		OfficeCubicleRentOrder order = officeCubicleProvider.findOfficeCubicleRentOrderById(cmd.getOrderId());
		OfficeRentOrderDTO dto = ConvertHelper.convert(order, OfficeRentOrderDTO.class);
		OfficeCubicleSpace space = officeCubicleProvider.getSpaceById(order.getSpaceId());
		dto.setSpaceName(space.getName());
		if (order.getRentalOrderNo()!=null){
			RentalOrder rentalOrder = rentalv2Provider.findRentalBillById(order.getRentalOrderNo());
			dto.setBeginTime(rentalOrder.getStartTime().getTime());
		}
		List<OfficeCubicleStationRent> stationRent = 
				officeCubicleProvider.searchCubicleStationRentByOrderId(order.getSpaceId(),order.getId());
		List<String> rentStation = new ArrayList<String>();
		if (stationRent!=null){
			for(OfficeCubicleStationRent s :stationRent){
				rentStation.add(s.getStationName());
			}
		}
		dto.setRentStation(rentStation);
		dto.setCreateTime(order.getCreateTime().getTime());
		if (order.getRentType() == 0){
			GetRentalBillCommand cmd2 = new GetRentalBillCommand();
			cmd2.setBillId(order.getRentalOrderNo());
			RentalBillDTO rentalDTO = rentalv2Service.getRentalBill(cmd2);
			dto.setOpenTime(rentalDTO.getOpenTime());
			dto.setUserDetail(order.getUseDetail());
			dto.setRentCount(order.getRentCount());
		}else if (order.getRentType() == 1){
			dto.setBeginTime(order.getBeginTime().getTime());
			dto.setEndTime(order.getEndTime().getTime());
		}
		response.setOrders(dto);
		return response;
	}
	
	@Override
	public GetSpaceResponse getSpace(GetSpaceCommand cmd){
		GetSpaceResponse resp = new GetSpaceResponse();
		OfficeCubicleSpace space = officeCubicleProvider.getSpaceByOwnerId(cmd.getOwnerId());
		if (space !=null){
			resp.setSpaceId(space.getId());
		}
		return resp;
	}
	
	@Override
	public BigDecimal getLongRentPrice(GetLongRentPriceCommand cmd){
		OfficeCubicleSpace space = this.officeCubicleProvider.getSpaceById(cmd.getSpaceId());
		if (space.getLongRentPrice() != null){
			return space.getLongRentPrice();
		}
		return new BigDecimal(0.00);
	}
	
	@Override
	public ListSpaceByCityResponse listSpaceByCity(ListSpaceByCityCommand cmd){
		ListSpaceByCityResponse resp = new ListSpaceByCityResponse();
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<OfficeCubicleSpace> list = 
				officeCubicleProvider.querySpacesByCityId(
						cmd.getOwnerType(),cmd.getOwnerId(), cmd.getCityId(), locator, pageSize + 1, getNamespaceId(UserContext.getCurrentNamespaceId()));
		if (list == null){
			return resp;
		}
		List<SpaceForAppDTO> spaceList = new ArrayList<SpaceForAppDTO>();
		spaceList = list.stream().map(r->{
			SpaceForAppDTO dto = new SpaceForAppDTO();
			dto.setAddress(r.getAddress());
			dto.setSpaceId(r.getId());
			dto.setSpaceName(r.getName());
			List<OfficeCubicleAttachment> attachments = null;
			if (cmd.getRentType() ==1){
				BigDecimal roomMinPrice = officeCubicleProvider.getRoomMinPrice(r.getId());
				BigDecimal stationMinPrice = officeCubicleProvider.getStationMinPrice(r.getId());
				if (roomMinPrice == null){
					roomMinPrice =  new BigDecimal(0);
				}
				if (stationMinPrice == null){
					stationMinPrice =  new BigDecimal(0);
				}
				dto.setMinUnitPrice(roomMinPrice.compareTo(stationMinPrice)>0?stationMinPrice:roomMinPrice);
				attachments = this.officeCubicleProvider.listAttachmentsBySpaceId(r.getId(),(byte)1);
			}else{
				attachments = this.officeCubicleProvider.listAttachmentsBySpaceId(r.getId(),(byte)2);
				QueryDefaultRuleAdminCommand cmd2 = new QueryDefaultRuleAdminCommand();
		        RentalResourceType type = rentalv2Provider.findRentalResourceTypes(r.getNamespaceId(),
		                RentalV2ResourceType.STATION_BOOKING.getCode());
				cmd2.setResourceType(RentalV2ResourceType.STATION_BOOKING.getCode());
				cmd2.setResourceTypeId(type.getId());
				cmd2.setSourceId(r.getId());
				cmd2.setSourceType(RuleSourceType.RESOURCE.getCode());
				QueryDefaultRuleAdminResponse resp2 = rentalv2Service.queryDefaultRule(cmd2);
				PriceRuleDTO dailyPriceRule = new PriceRuleDTO();
				PriceRuleDTO halfdailyPriceRule = new PriceRuleDTO();
				for (PriceRuleDTO priceRule :resp2.getPriceRules()){
					if (priceRule.getRentalType() ==RentalType.DAY.getCode()){
						dailyPriceRule = priceRule;
					}
					if (priceRule.getRentalType() ==RentalType.HALFDAY.getCode()){
						halfdailyPriceRule = priceRule;
					}
				}
				BigDecimal dailyPrice = new BigDecimal(0.00);
				BigDecimal halfdailyPrice = new BigDecimal(0.00);
				if(dailyPriceRule.getWorkdayPrice() != null){
					dailyPrice = dailyPriceRule.getWorkdayPrice();
				}
				if(halfdailyPriceRule.getWorkdayPrice()!=null){
					halfdailyPrice = halfdailyPriceRule.getWorkdayPrice();
				}
				dto.setMinUnitPrice(dailyPrice.compareTo(halfdailyPrice)>0?halfdailyPrice:dailyPrice);
			}
			List<OfficeCubicleStation> station = officeCubicleProvider.getOfficeCubicleStation(cmd.getOwnerId(),cmd.getOwnerType(), r.getId(),null,null,null,null,null);
			List<OfficeCubicleRoom> room = officeCubicleProvider.getOfficeCubicleRoom(cmd.getOwnerId(),cmd.getOwnerType(),r.getId(),null,null,null,null);
			Integer stationSize = 0;
			Integer roomSize = 0;
			if (station !=null){
				stationSize =station.size();
			}
			if (room!=null){
				roomSize = room.size();
			}
			Integer allPositonNums = stationSize + roomSize;
			dto.setAllPositonNums(allPositonNums);
			if (attachments!=null){
				dto.setCoverUrl(this.contentServerService.parserUri(attachments.get(0).getContentUri(), EntityType.USER.getCode(),
						UserContext.current().getUser().getId()));
			}
			dto.setRentType(cmd.getRentType());
			return dto;
		}).collect(Collectors.toList());
		Collections.sort(spaceList,new CompartorPinYin());
		resp.setSpace(spaceList);
		return resp;
	}
	
	@Scheduled(cron = "0 0 8,12,18 * * ? ")
	@Override
	public void schedule(){
		List<OfficeCubicleRentOrder> orders = 
				this.officeCubicleProvider.findOfficeCubicleRentOrderByStatus(new Byte[]{OfficeCubicleOrderStatus.IN_USE.getCode(),
						OfficeCubicleOrderStatus.PAID.getCode()});
		for(OfficeCubicleRentOrder order:orders){
			if(order.getOrderStatus().equals(OfficeCubicleOrderStatus.IN_USE.getCode())){
				if(order.getEndTime().getTime()<System.currentTimeMillis()){
					order.setOrderStatus(OfficeCubicleOrderStatus.COMPLETE.getCode());
					officeCubicleProvider.updateCubicleRentOrder(order);
				}
			} else if(order.getOrderStatus().equals(OfficeCubicleOrderStatus.PAID.getCode())){
				if(order.getBeginTime().getTime()<System.currentTimeMillis()){
					order.setOrderStatus(OfficeCubicleOrderStatus.IN_USE.getCode());
					officeCubicleProvider.updateCubicleRentOrder(order);
				}
			}
		}
	}
}
