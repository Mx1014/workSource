package com.everhomes.officecubicle;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.alipay.api.domain.CardBinVO;
import com.everhomes.community.CommunityProvider;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowReferType;
import com.everhomes.rest.officecubicle.*;
import com.everhomes.rest.officecubicle.admin.*;
import com.everhomes.rest.region.RegionAdminStatus;
import com.everhomes.rest.region.RegionScope;
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
import com.everhomes.news.Attachment;
import com.everhomes.news.AttachmentProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.techpark.rental.RentalServiceErrorCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleAttachments;
import com.everhomes.settings.PaginationConfigHelper;
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
			if (null != cmd.getAttachments())
				cmd.getAttachments().forEach((dto) -> {
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
			if (null != cmd.getAttachments()) {
				cmd.getAttachments().forEach((dto) -> {
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
//		根据项目查询
		int pageSize = PaginationConfigHelper.getMaxPageSize(configurationProvider,999999);
		List<OfficeCubicleCity> cities = officeCubicleCityProvider.listOfficeCubicleCity(namespaceId,null,cmd.getOwnerType(),cmd.getOwnerId(),Long.MAX_VALUE,pageSize);
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
		List<OfficeCubicleCity> list=null;
		if(cmd.getParentName()==null){
			list = officeCubicleCityProvider.listOfficeCubicleProvince(UserContext.getCurrentNamespaceId());
		}else{
			list = officeCubicleCityProvider.listOfficeCubicleCitiesByProvince(cmd.getParentName(),UserContext.getCurrentNamespaceId());
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
}
