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
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.everhomes.community.CommunityProvider;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowService;
import com.everhomes.gorder.sdk.order.GeneralOrderService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rentalv2.RentalDefaultRule;
import com.everhomes.rentalv2.RentalNotificationTemplateCode;
import com.everhomes.rentalv2.RentalOrder;
import com.everhomes.rentalv2.RentalOrderHandler;
import com.everhomes.rentalv2.RentalResource;
import com.everhomes.rentalv2.Rentalv2PriceRule;
import com.everhomes.rentalv2.Rentalv2PriceRuleProvider;
import com.everhomes.rentalv2.job.RentalMessageJob;
import com.everhomes.rentalv2.job.RentalMessageQuartzJob;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowReferType;
import com.everhomes.rest.general.order.CreateOrderBaseInfo;
import com.everhomes.rest.officecubicle.*;
import com.everhomes.rest.officecubicle.admin.*;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.OwnerType;
import com.everhomes.rest.order.PaymentUserStatus;
import com.everhomes.rest.promotion.merchant.GetPayUserByMerchantIdCommand;
import com.everhomes.rest.promotion.merchant.GetPayUserListByMerchantCommand;
import com.everhomes.rest.promotion.merchant.GetPayUserListByMerchantDTO;
import com.everhomes.rest.promotion.merchant.ListPayUsersByMerchantIdsCommand;
import com.everhomes.rest.promotion.merchant.controller.GetMerchantListByPayUserIdRestResponse;
import com.everhomes.rest.promotion.merchant.controller.GetPayerInfoByMerchantIdRestResponse;
import com.everhomes.rest.promotion.merchant.controller.ListPayUsersByMerchantIdsRestResponse;
import com.everhomes.rest.promotion.order.BusinessPayerType;
import com.everhomes.rest.promotion.order.CreateMerchantOrderResponse;
import com.everhomes.rest.promotion.order.CreatePurchaseOrderCommand;
import com.everhomes.rest.promotion.order.MerchantPaymentNotificationCommand;
import com.everhomes.rest.promotion.order.PurchaseOrderCommandResponse;
import com.everhomes.rest.promotion.order.controller.CreatePurchaseOrderRestResponse;
import com.everhomes.rest.region.RegionAdminStatus;
import com.everhomes.rest.region.RegionScope;
import com.everhomes.rest.rentalv2.PriceRuleType;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.rest.rentalv2.SiteBillStatus;
import com.everhomes.rest.rentalv2.admin.UpdateResourceRentalRuleCommand;
import com.everhomes.util.*;

import net.greghaines.jesque.Job;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tools.ant.taskdefs.Get;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.messaging.MessagingService;
import com.everhomes.news.Attachment;
import com.everhomes.news.AttachmentProvider;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.parking.ParkingBusinessPayeeAccount;
import com.everhomes.parking.ParkingBusinessPayeeAccountProvider;
import com.everhomes.parking.ParkingRechargeRate;
import com.everhomes.pay.order.CreateOrderCommand;
import com.everhomes.pay.order.OrderCommandResponse;
import com.everhomes.pay.order.PaymentType;
import com.everhomes.paySDK.PaySettings;
import com.everhomes.paySDK.PayUtil;
import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.print.SiyinPrintOrder;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.asset.TargetDTO;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.techpark.rental.RentalServiceErrorCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.server.schema.tables.EhRentalv2DefaultRules;
import com.everhomes.server.schema.tables.daos.EhParkingRechargeRatesDao;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleAttachments;
import com.everhomes.server.schema.tables.pojos.EhParkingRechargeRates;
import com.everhomes.server.schema.tables.pojos.EhRentalv2Resources;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.techpark.rental.IncompleteUnsuccessRentalBillAction;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;

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

		List<Attachment> attachments = this.attachmentProvider.listAttachmentByOwnerId(EhOfficeCubicleAttachments.class, dto.getId());
		if (null != attachments){
			dto.setAttachments(new ArrayList<OfficeAttachmentDTO>());
			attachments.forEach((attachment) -> {
				OfficeAttachmentDTO attachmentDTO = ConvertHelper.convert(attachment, OfficeAttachmentDTO.class);
				attachmentDTO.setContentUrl(this.contentServerService.parserUri(attachment.getContentUri(), EntityType.USER.getCode(),
						UserContext.current().getUser().getId()));
				dto.getAttachments().add(attachmentDTO);
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
		

		List<OfficeCubicleRange> ranges = officeCubicleRangeProvider.listRangesBySpaceId(dto.getId());
		dto.setRanges(ranges.stream().map(r->ConvertHelper.convert(r,OfficeRangeDTO.class)).collect(Collectors.toList()));
		
		return dto;
	}

	@Override
	public void addSpace(AddSpaceCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4020040210L, cmd.getAppId(), null,cmd.getCurrentProjectId());//空间管理权限
		}
		if (null == cmd.getManagerUid())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of Categories error: null ");
		if (null == cmd.getCategories())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of Categories error: null ");
		if (null == cmd.getCityName())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of city error: null id or name");
		this.dbProvider.execute((TransactionStatus status) -> {
			OfficeCubicleSpace space = ConvertHelper.convert(cmd, OfficeCubicleSpace.class);
			space.setNamespaceId(getNamespaceId(cmd.getNamespaceId()));
			space.setGeohash(GeoHashUtils.encode(space.getLatitude(), space.getLongitude()));
			space.setStatus(OfficeStatus.NORMAL.getCode());
			space.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			space.setCreatorUid(UserContext.current().getUser().getId());
			this.officeCubicleProvider.createSpace(space);
			if (null != cmd.getSpaceAttachments())
				cmd.getSpaceAttachments().forEach((dto) -> {
					this.saveAttachment(dto, space.getId());
				});
			if (null != cmd.getShortRentAttachments())
				cmd.getShortRentAttachments().forEach((dto) -> {
					this.saveAttachment(dto, space.getId());
				});
			cmd.getCategories().forEach((dto) -> {
				this.saveCategory(dto, space.getId(),getNamespaceId(cmd.getNamespaceId()));

			});

			cmd.getRanges().forEach(dto->saveRanges(dto,space.getId(),getNamespaceId(cmd.getNamespaceId())));

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
		if (null == cmd.getManagerUid())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of Categories error: null ");
		if (null == cmd.getCategories())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of Categories error: null ");
		if (null == cmd.getId())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of ID error: null ");
		if (null == cmd.getCityName())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of city error: null id or name");
		OfficeCubicleSpace oldSpace = this.officeCubicleProvider.getSpaceById(cmd.getId());
		if (null == oldSpace)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of space id error: no space found");

		this.dbProvider.execute((TransactionStatus status) -> {
			OfficeCubicleSpace space = ConvertHelper.convert(cmd, OfficeCubicleSpace.class);
			space.setNamespaceId(UserContext.getCurrentNamespaceId());
			space.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			space.setStatus(OfficeStatus.NORMAL.getCode());
			space.setOperatorUid(UserContext.current().getUser().getId());
			this.officeCubicleProvider.updateSpace(space);
			// TODO:删除附件唐彤没有提供
			this.officeCubicleProvider.deleteAttachmentsBySpaceId(space.getId());
			if (null != cmd.getSpaceAttachments()) {
				cmd.getSpaceAttachments().forEach((dto) -> {
					this.saveAttachment(dto, space.getId());
				});
			}
			this.officeCubicleProvider.deleteCategoriesBySpaceId(space.getId());
			if (null != cmd.getCategories()) {
				cmd.getCategories().forEach((dto) -> {
					this.saveCategory(dto, space.getId(), getNamespaceId(cmd.getNamespaceId()));
				});
			}

			this.officeCubicleRangeProvider.deleteRangesBySpaceId(space.getId());
			if (null != cmd.getCategories()) {
				cmd.getRanges().forEach((dto) -> {
					this.saveRanges(dto, space.getId(), getNamespaceId(cmd.getNamespaceId()));
				});
			}
			return null;
		});
	}

	public void saveAttachment(OfficeAttachmentDTO dto, Long spaceId) {
		Attachment attachment = ConvertHelper.convert(dto, Attachment.class);
		attachment.setOwnerId(spaceId);
		attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		attachment.setCreatorUid(UserContext.current().getUser().getId());

		this.attachmentProvider.createAttachment(EhOfficeCubicleAttachments.class, attachment);
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

	private void createRentalBillsBookSheetHead(Sheet sheet) {

		Row row = sheet.createRow(sheet.getLastRowNum());
		int i = -1;
		row.createCell(++i).setCellValue("序号");
		row.createCell(++i).setCellValue("空间名称");
		row.createCell(++i).setCellValue("所在城市");
		row.createCell(++i).setCellValue("订单时间");
		row.createCell(++i).setCellValue("预定类别");
		row.createCell(++i).setCellValue("工位类别");
//		row.createCell(++i).setCellValue("工位数/面积");
		row.createCell(++i).setCellValue("预订人");
		row.createCell(++i).setCellValue("联系电话");
		row.createCell(++i).setCellValue("状态");
	}

	private void setNewRentalBillsBookRow(Sheet sheet, OfficeOrderDTO dto) {
		Row row = sheet.createRow(sheet.getLastRowNum() + 1);
		int i = -1;
		// 序号
		row.createCell(++i).setCellValue(row.getRowNum());
		// 项目名称
		row.createCell(++i).setCellValue(dto.getSpaceName());
		// 所在城市
		row.createCell(++i).setCellValue(dto.getProvinceName() + dto.getCityName());
		// 订单时间
		row.createCell(++i).setCellValue(datetimeSF.format(new Timestamp(dto.getReserveTime())));
		// 预定类别
		OfficeOrderType ordertype = OfficeOrderType.fromCode(dto.getOrderType());
		row.createCell(++i).setCellValue(ordertype==null?"":ordertype.getMsg());
		// 工位类别
		OfficeRentType renttype = OfficeRentType.fromCode(dto.getRentType());
		row.createCell(++i).setCellValue(renttype==null?"":renttype.getMsg());
		// 工位数/面积
//		OfficeSpaceType spaceType = OfficeSpaceType.fromCode(dto.getSpaceType()==null?(byte)1:(byte)2);
//		row.createCell(++i).setCellValue(dto.getSpaceSize() + spaceType==null?"":spaceType.getMsg());
		// 预订人
		row.createCell(++i).setCellValue(dto.getReserverName());

		// 联系电话
		row.createCell(++i).setCellValue(dto.getReserveContactToken());

		// 工作流状态
		if(dto!=null && dto.getWorkFlowStatus()!=null) {
			OfficeOrderWorkFlowStatus workFlowStatus = OfficeOrderWorkFlowStatus.fromType(dto.getWorkFlowStatus());
			row.createCell(++i).setCellValue(workFlowStatus == null ? "" : workFlowStatus.getDescription());
		}else{
			row.createCell(++i).setCellValue("");
		}
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
		OfficeRentType type= OfficeRentType.fromCode(order.getRentType());
		if(type == OfficeRentType.OPENSITE) {
			cmd21.setContent("工位类型：" + type.getMsg() + "\n"
					+ "预订工位数：" + order.getPositionNums());
		}else if(type == OfficeRentType.WHOLE){
			cmd21.setContent("工位类型：" + type.getMsg() + "\n"
					+ "预订空间：" + order.getCategoryName());
		}
		cmd21.setTitle("工位预订");
		cmd21.setFlowMainId(flow.getFlowMainId());
		cmd21.setFlowVersion(flow.getFlowVersion());
		cmd21.setFlowCaseId(flowCaseId);
		return flowService.createFlowCase(cmd21);
	}

	private void checkAddOrderCmd(AddSpaceOrderCommand cmd) {
		if (null == cmd.getOrderType())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of OrderType error: null ");
		if (null == cmd.getReserveEnterprise())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of ReserveEnterprise error: null ");
		if (null == cmd.getReserveContactToken())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of ReserveContactToken error: null ");
		if (null == cmd.getReserverName())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of ReserverName error: null ");
		if (null == cmd.getSize())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of size error: null ");
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

		OfficeRentType officeRentType = OfficeRentType.fromCode(order.getRentType());
		StringBuffer sb = new StringBuffer();
		sb.append("您收到一条");
		sb.append(space.getName());
		sb.append("的工位预订订单:\n工位类型:");
		sb.append(officeRentType.getMsg());
		sb.append("(");
		sb.append(order.getSpaceSize());
		sb.append(officeRentType==OfficeRentType.OPENSITE?"个":"㎡");
		sb.append(")\n预订人:");
		sb.append(order.getReserverName());
		sb.append("\n手机号:");
		sb.append(order.getReserveContactToken());
		sb.append("\n公司名称:");
		sb.append(order.getReserveEnterprise());
		sb.append("\n您可以登陆管理后台查看详情");
		sendMessageToUser(order.getManagerUid(), sb.toString());
		// 小红点
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
		return null;
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
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4040040410L, cmd.getAppId(), null, cmd.getCurrentProjectId());//资源管理权限
		}

		this.dbProvider.execute((TransactionStatus status) -> {
			OfficeCubicleStation station = ConvertHelper.convert(cmd, OfficeCubicleStation.class);

			officeCubicleProvider.createCubicleSite(station);

			return null;
		});
	}
	
	@Override
	public void addRoom(AddRoomAdminCommand cmd) {
		if (cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4040040410L, cmd.getAppId(), null, cmd.getCurrentProjectId());//资源管理权限
		}

		this.dbProvider.execute((TransactionStatus status) -> {
			OfficeCubicleRoom room = ConvertHelper.convert(cmd, OfficeCubicleRoom.class);

			officeCubicleProvider.createCubicleRoom(room);

			return null;
		});
	}
	
	@Override
	public void deleteRoom(DeleteRoomAdminCommand cmd){
		OfficeCubicleRoom room = new OfficeCubicleRoom();
		officeCubicleProvider.deleteRoom(room);
	}
    
	@Override
	public void deleteCubicle(DeleteCubicleAdminCommand cmd){
		OfficeCubicleStation station = new OfficeCubicleStation();
		officeCubicleProvider.deleteStation(station);
	}
	
	@Override
	public void updateShortRentNums(UpdateShortRentNumsCommand cmd){

	}
	
	
	@Override
	public void refundOrder(RefundOrderCommand cmd){

	}
	
	@Override
	public void updateRoom(AddRoomAdminCommand cmd) {
		if (cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4040040410L, cmd.getAppId(), null, cmd.getCurrentProjectId());//资源管理权限
		}

		this.dbProvider.execute((TransactionStatus status) -> {
			OfficeCubicleRoom room = ConvertHelper.convert(cmd, OfficeCubicleRoom.class);

			officeCubicleProvider.updateRoom(room);

			return null;
		});
	}
	
	@Override
	public void updateCubicle(AddCubicleAdminCommand cmd) {
		if (cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4040040410L, cmd.getAppId(), null, cmd.getCurrentProjectId());//资源管理权限
		}

		this.dbProvider.execute((TransactionStatus status) -> {
			OfficeCubicleStation station = ConvertHelper.convert(cmd, OfficeCubicleStation.class);

			officeCubicleProvider.updateCubicle(station);

			return null;
		});
	}
	
	@Override
	public CreateOfficeCubicleOrderResponse createCubicleGeneralOrder(CreateOfficeCubicleOrderCommand cmd){
		OfficeCubicleRentOrder order = ConvertHelper.convert(cmd, OfficeCubicleRentOrder.class);
		this.dbProvider.execute((TransactionStatus status) -> {
			officeCubicleProvider.createCubicleRentOrder(order);
			return null;
		});
		List<OfficeCubicleStationRent> stationRent = 
				officeCubicleProvider.searchCubicleStationRent(cmd.getSpaceId(),UserContext.getCurrentNamespaceId(), cmd.getRentType(), cmd.getStationType());
		List<OfficeCubicleStation> station = officeCubicleProvider.getOfficeCubicleStation(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getSpaceId());
		Integer stationNums = station.size();
		
		this.coordinationProvider.getNamedLock(CoordinationLocks.OFFICE_CUBICLE_STATION_RENT.getCode() + order.getId()).enter(()-> {
			if (stationNums<(stationRent.size()+cmd.getRentCount())){
				
			} else {
				OfficeCubicleStationRent rent = ConvertHelper.convert(cmd, OfficeCubicleStationRent.class);
				rent.setOrderId(order.getId());
				for(int i=0;i<= cmd.getRentCount() ;i++){
					officeCubicleProvider.createCubicleStationRent(rent);
				}
			}
			return null;
		});
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
		String extendInfo = "工位预定订单";
		//根据merchantId获取payeeId
		GetPayUserByMerchantIdCommand getPayUserByMerchantIdCommand = new GetPayUserByMerchantIdCommand();
		getPayUserByMerchantIdCommand.setMerchantId(payeeAccounts.get(0).getPayeeId());
		GetPayerInfoByMerchantIdRestResponse getPayerInfoByMerchantIdRestResponse = orderService.getPayerInfoByMerchantId(getPayUserByMerchantIdCommand);
		createOrderCommand.setAccountCode(sNamespaceId);
		createOrderCommand.setBizOrderNum(generateBizOrderNum(sNamespaceId,OrderType.OrderTypeEnum.OFFICE_CUBICLE.getPycode(),order.getId()));
		createOrderCommand.setPayeeUserId(getPayerInfoByMerchantIdRestResponse.getResponse().getId());
		List<Rentalv2PriceRule> priceRule = rentalv2PriceRuleProvider.listPriceRuleByOwner("station_booking", PriceRuleType.RESOURCE.getCode(), cmd.getSpaceId());
		createOrderCommand.setAmount(priceRule.get(0).getApprovingUserWorkdayPrice().longValue());
		createOrderCommand.setExtendInfo(extendInfo);
		createOrderCommand.setGoodsName(extendInfo);
        String homeurl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"home.url", "");
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
		order.setBizOrderNo(response.getBizOrderNum());
		officeCubicleProvider.updateCubicleRentOrder(order);
		return null;
		
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
		OfficeCubicleRentOrder order = officeCubicleProvider.findOfficeCubicleRentOrderByBizOrderNum(cmd.getBizOrderNum());
		if(cmd.getOrderType() == 3) {
			officeCubicleProvider.updateCubicleRentOrder(order);
		}
		else if(cmd.getOrderType() == 4){
			officeCubicleProvider.updateCubicleRentOrder(order);
		}
	}
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
		List<OfficeCubicleStationRent> stationRent = 
				officeCubicleProvider.searchCubicleStationRent(cmd.getSpaceId(),UserContext.getCurrentNamespaceId(), cmd.getRentType(), cmd.getStationType());
		List<OfficeCubicleStation> station = officeCubicleProvider.getOfficeCubicleStation(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getSpaceId());
		Integer stationNums = station.size();
		OfficeCubicleRentOrder order = ConvertHelper.convert(cmd, OfficeCubicleRentOrder.class);
		this.dbProvider.execute((TransactionStatus status) -> {
			officeCubicleProvider.createCubicleRentOrder(order);
			return null;
		});
		
		this.coordinationProvider.getNamedLock(CoordinationLocks.OFFICE_CUBICLE_STATION_RENT.getCode() + order.getId()).enter(()-> {
			if (stationNums<(stationRent.size()+cmd.getRentCount())){
				
			} else {
				OfficeCubicleStationRent rent = ConvertHelper.convert(cmd, OfficeCubicleStationRent.class);
				rent.setOrderId(order.getId());
				for(int i=0;i<= cmd.getRentCount() ;i++){
					officeCubicleProvider.createCubicleStationRent(rent);
				}
			}
			return null;
		});

		return null;
		
	}
	
	@Override
	public OfficeCubicleDTO getCubicleDetail(GetCubicleDetailCommand cmd) {
		OfficeCubicleSpace space = this.officeCubicleProvider.getSpaceById(cmd.getSpaceId());
		OfficeCubicleDTO dto = new OfficeCubicleDTO();
		dto.setAddress(space.getAddress());
		dto.setSpaceName(space.getName());
		dto.setContactPhone(space.getContactPhone());
		return dto;
	}
	
	@Override
	public ListOfficeCubiclePayeeAccountResponse listOfficeCubiclPayeeAccount(ListOfficeCubiclePayeeAccountCommand cmd) {
		List<OfficeCubiclePayeeAccount> accounts = officeCubiclePayeeAccountProvider
				.listOfficeCubiclePayeeAccountByOwner(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),cmd.getSpaceId());
		if(accounts==null || accounts.size()==0){
			return new ListOfficeCubiclePayeeAccountResponse();
		}
		
		ListPayUsersByMerchantIdsCommand cmd2 = new ListPayUsersByMerchantIdsCommand();
        cmd2.setIds(accounts.stream().map(r -> r.getMerchantId()).collect(Collectors.toList()));
        ListPayUsersByMerchantIdsRestResponse restResponse = orderService.listPayUsersByMerchantIds(cmd2);
        List<PayUserDTO> payUserDTOs = restResponse.getResponse();
//		List<PayUserDTO> payUserDTOS = sdkPayService.listPayUsersByIds(accounts.stream().map(r -> r.getPayeeId()).collect(Collectors.toList()));
		Map<Long,PayUserDTO> map = payUserDTOs.stream().collect(Collectors.toMap(PayUserDTO::getId,r->r));
		ListOfficeCubiclePayeeAccountResponse response = new ListOfficeCubiclePayeeAccountResponse();
		response.setAccountList(accounts.stream().map(r->{
			OfficeCubiclePayeeAccountDTO convert = ConvertHelper.convert(r, OfficeCubiclePayeeAccountDTO.class);
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
		List<OfficeCubiclePayeeAccount> accounts = officeCubiclePayeeAccountProvider.findRepeatOfficeCubiclePayeeAccounts
				(cmd.getId(),cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),cmd.getSpaceId());
		if(accounts!=null && accounts.size()>0){
			throw RuntimeErrorException.errorWith(OfficeCubicleErrorCode.SCOPE, OfficeCubicleErrorCode.ERROR_REPEATE_ACCOUNT,
					"repeat account");
		}
		if(cmd.getId()!=null){
			OfficeCubiclePayeeAccount oldPayeeAccount = officeCubiclePayeeAccountProvider.findOfficeCubiclePayeeAccountById(cmd.getId());
			if(oldPayeeAccount == null){
				throw RuntimeErrorException.errorWith(OfficeCubicleErrorCode.SCOPE, OfficeCubicleErrorCode.PARAMTER_LOSE,
						"unknown payaccountid = "+cmd.getId());
			}
			OfficeCubiclePayeeAccount newPayeeAccount = ConvertHelper.convert(cmd,OfficeCubiclePayeeAccount.class);
			newPayeeAccount.setCreateTime(oldPayeeAccount.getCreateTime());
			newPayeeAccount.setCreatorUid(oldPayeeAccount.getCreatorUid());
			newPayeeAccount.setNamespaceId(oldPayeeAccount.getNamespaceId());
			newPayeeAccount.setOwnerType(oldPayeeAccount.getOwnerType());
			newPayeeAccount.setOwnerId(oldPayeeAccount.getOwnerId());
			newPayeeAccount.setMerchantId(oldPayeeAccount.getMerchantId());
			newPayeeAccount.setPayeeId(oldPayeeAccount.getMerchantId());
			officeCubiclePayeeAccountProvider.updateOfficeCubiclePayeeAccount(newPayeeAccount);
		}else{
			OfficeCubiclePayeeAccount newPayeeAccount = new OfficeCubiclePayeeAccount();
			newPayeeAccount.setMerchantId(cmd.getPayeeId());
			newPayeeAccount.setNamespaceId(cmd.getNamespaceId());
			newPayeeAccount.setOwnerId(cmd.getOwnerId());
			newPayeeAccount.setOwnerType(cmd.getOwnerType());
			newPayeeAccount.setSpaceId(cmd.getSpaceId());
			newPayeeAccount.setPayeeUserType(cmd.getPayeeUserType());
			newPayeeAccount.setStatus((byte)2);
			newPayeeAccount.setPayeeId(cmd.getPayeeId());
			
			officeCubiclePayeeAccountProvider.createOfficeCubiclePayeeAccount(newPayeeAccount);
		}
	}
	

	@Override
	public ListOfficeCubicleStatusResponse listOfficeCubicleStatus(ListOfficeCubicleStatusCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public SearchCubicleOrdersResponse searchCubicleOrders(SearchCubicleOrdersCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4020040220L, cmd.getAppId(), null,cmd.getCurrentProjectId());//预定详情权限
		}

		SearchCubicleOrdersResponse response = new SearchCubicleOrdersResponse();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(Long.MAX_VALUE);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		List<OfficeCubicleRentOrder> orders = this.officeCubicleProvider.searchCubicleOrders(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getBeginDate(), cmd.getEndDate(),
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
			response.getOrders().add(dto);
		});

		return response;
	}
	
	@Override
	public GetOfficeCubicleRentOrderResponse getOfficeCubicleRentOrder(GetOfficeCubicleRentOrderCommand cmd){
		GetOfficeCubicleRentOrderResponse response = new GetOfficeCubicleRentOrderResponse();
		OfficeCubicleRentOrder order = officeCubicleProvider.findOfficeCubicleRentOrderById(cmd.getOrderId());
		OfficeRentOrderDTO dto = ConvertHelper.convert(order, OfficeRentOrderDTO.class);
		response.setOrders(dto);
		return response;
	}
}
