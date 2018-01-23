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

import com.everhomes.community.CommunityProvider;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowReferType;
import com.everhomes.rest.officecubicle.*;
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
import com.everhomes.rest.officecubicle.admin.AddSpaceCommand;
import com.everhomes.rest.officecubicle.admin.SearchSpaceOrdersCommand;
import com.everhomes.rest.officecubicle.admin.SearchSpaceOrdersResponse;
import com.everhomes.rest.officecubicle.admin.SearchSpacesAdminCommand;
import com.everhomes.rest.officecubicle.admin.SearchSpacesAdminResponse;
import com.everhomes.rest.officecubicle.admin.UpdateSpaceCommand;
import com.everhomes.rest.techpark.rental.RentalServiceErrorCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleAttachments;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

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
	private ConfigurationProvider configurationProvider;
	@Autowired
	private AttachmentProvider attachmentProvider;
	@Autowired
	private UserProvider userProvider;
	@Autowired
	private OfficeCubicleRangeProvider officeCubicleRangeProvider;
	@Autowired
	private FlowService flowService;

	@Autowired private CommunityProvider communityProvider;

	private Integer getNamespaceId(Integer namespaceId){
		if(namespaceId!=null){
			return namespaceId;
		}
		return UserContext.getCurrentNamespaceId();
	}

	@Override
	public SearchSpacesAdminResponse searchSpaces(SearchSpacesAdminCommand cmd) {
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
		if (null != categories){
			dto.setCategories(new ArrayList<OfficeCategoryDTO>());
			categories.forEach((category) -> {
				OfficeCategoryDTO categoryDTO = ConvertHelper.convert(category, OfficeCategoryDTO.class);
				categoryDTO.setSize(category.getSpaceSize());
				dto.getCategories().add(categoryDTO);
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
		if (null == cmd.getManagerUid())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of Categories error: null ");
		if (null == cmd.getCategories())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of Categories error: null ");
		if (null == cmd.getCityId() || null == cmd.getCityName())
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
		if (null == cmd.getManagerUid())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of Categories error: null ");
		if (null == cmd.getCategories())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of Categories error: null ");
		if (null == cmd.getId())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter of ID error: null ");
		if (null == cmd.getCityId() || null == cmd.getCityName())
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
		row.createCell(++i).setCellValue("项目名称");
		row.createCell(++i).setCellValue("所在城市");
		row.createCell(++i).setCellValue("订单时间");
		row.createCell(++i).setCellValue("预定类别");
		row.createCell(++i).setCellValue("工位类别");
		row.createCell(++i).setCellValue("工位数/面积");
		row.createCell(++i).setCellValue("预订人");
		row.createCell(++i).setCellValue("联系电话");
		row.createCell(++i).setCellValue("公司名称");
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
		row.createCell(++i).setCellValue(OfficeOrderType.fromCode(dto.getOrderType()).getMsg());
		// 工位类别
		row.createCell(++i).setCellValue(OfficeRentType.fromCode(dto.getRentType()).getMsg());
		// 工位数/面积
		row.createCell(++i).setCellValue(dto.getSpaceSize() + OfficeSpaceType.fromCode(dto.getSpaceType()).getMsg());
		// 预订人
		row.createCell(++i).setCellValue(dto.getReserverName());

		// 联系电话
		row.createCell(++i).setCellValue(dto.getReserveContactToken());

		// 公司名称
		row.createCell(++i).setCellValue(dto.getReserveEnterprise());
	}

	@Override
	public List<CityDTO> queryCities(QueryCitiesCommand cmd) {
		List<CityDTO> resp = new ArrayList<CityDTO>();
		CityDTO dto = new CityDTO();
		dto.setCityId(0L);
		dto.setCityName("全国");
		resp.add(dto);
		Set<Long> cityIds = new HashSet<Long>();
		List<OfficeCubicleSpace> spaces = this.officeCubicleProvider.searchSpaces(cmd.getOwnerType(),cmd.getOwnerId(),null, new CrossShardListingLocator(),
				Integer.MAX_VALUE, getNamespaceId(cmd.getNamespaceId()));
		if (null != spaces) {
			spaces.forEach((space) -> {
				if (!cityIds.contains(space.getCityId())) {
					cityIds.add(space.getCityId());
					CityDTO cityDTO = ConvertHelper.convert(space, CityDTO.class);
					resp.add(cityDTO);
				}
			});
		}
		return resp;
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
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"unable to get enabled flow ");
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
		cmd21.setContent("工位预定");
		cmd21.setTitle("工位预定");
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

	private void sendMessage(OfficeCubicleSpace space,OfficeCubicleOrder order) {
		// 发消息 +推送
		StringBuffer sb = new StringBuffer();
		sb.append("您收到一条");
		sb.append(space.getName());
		sb.append("的工位续订订单:\n工位类型:");
		sb.append(OfficeRentType.fromCode(order.getRentType()).getMsg());
		sb.append("(");
		sb.append(order.getSpaceSize());
		sb.append(OfficeSpaceType.fromCode(order.getSpaceType()).getMsg());
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
		if (cmd.getCityId() == null || cmd.getCityId().equals(0L))
			cmd.setCityId(null);
		List<OfficeCubicleSpace> spaces = this.officeCubicleProvider.querySpacesByCityId(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getCityId(), locator, pageSize + 1,
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
	public void dataMigration() {

		//owner刷入space
		List<OfficeCubicleSpace> emptyOwnerList = officeCubicleProvider.listEmptyOwnerSpace();
		if(emptyOwnerList!=null) {
			for (OfficeCubicleSpace r : emptyOwnerList) {
				ListingLocator locator = new ListingLocator();
				locator.setAnchor(0L);
				List<CommunityDTO> communityDTOS = communityProvider.listCommunitiesByNamespaceId(CommunityType.COMMERCIAL.getCode(), r.getNamespaceId(), locator, 10);
				if(communityDTOS!=null && communityDTOS.size()>0){
					r.setOwnerId(communityDTOS.get(0).getId());
					r.setOwnerType(OfficeSpaceOwner.COMMUNITY.getCode());

					//owner刷入ranges
					for (CommunityDTO communityDTO : communityDTOS) {
						OfficeCubicleRange range = officeCubicleRangeProvider.findOfficeCubicleRangeByOwner(communityDTO.getId(),OfficeSpaceOwner.COMMUNITY.getCode(),r.getId(),r.getNamespaceId());
						if(range==null) {
							range = new OfficeCubicleRange();
							range.setNamespaceId(r.getNamespaceId());
							range.setOwnerId(communityDTO.getId());
							range.setOwnerType(OfficeSpaceOwner.COMMUNITY.getCode());
							range.setSpaceId(r.getId());
							officeCubicleRangeProvider.createOfficeCubicleRange(range);
						}

					}
					officeCubicleProvider.updateSpace(r);
				}
			}
		}

		List<OfficeCubicleOrder> emptyOwnerOrders = officeCubicleProvider.listEmptyOwnerOrders();
		if(emptyOwnerOrders!=null){
			for (OfficeCubicleOrder order : emptyOwnerOrders) {
				OfficeCubicleSpace space = officeCubicleProvider.getSpaceById(order.getSpaceId());
				if(space==null) {
					continue;
				}
				order.setOwnerType(space.getOwnerType());
				order.setOwnerId(space.getOwnerId());
				officeCubicleProvider.updateOrder(order);
			}
		}
	}
}
