// @formatter:off
package com.everhomes.relocation;


import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.*;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.organization.*;
import com.everhomes.organization.pm.OrganizationOwnerType;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.portal.PortalService;
import com.everhomes.qrcode.QRCodeService;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.flow.*;

import com.everhomes.rest.pmtask.PmTaskErrorCode;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.qrcode.NewQRCodeCommand;
import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.rest.qrcode.QRCodeHandler;
import com.everhomes.rest.relocation.*;
import com.everhomes.rest.relocation.AttachmentDescriptor;

import com.everhomes.rest.rentalv2.RentalBillDTO;
import com.everhomes.rest.rentalv2.RentalServiceErrorCode;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DownloadUtils;
import com.everhomes.util.RuntimeErrorException;

import com.everhomes.util.StringHelper;
import net.greghaines.jesque.Job;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class RelocationServiceImpl implements RelocationService, ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RelocationServiceImpl.class);

	private static final String RELOCATION_CANCEL_QUE = "relocationCancelQue";

	@Autowired
	private LocaleStringService localeStringService;
	@Autowired
	private LocaleTemplateService localeTemplateService;
	@Autowired
	private OrganizationProvider organizationProvider;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private UserService userService;
    @Autowired
    private ConfigurationProvider configProvider;
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private ContentServerService contentServerService;
    @Autowired
	private FlowService flowService;
	@Autowired
	private RelocationProvider relocationProvider;
	@Autowired
	private QRCodeService qRCodeService;
	@Autowired
	private WorkerPoolFactory workerPoolFactory;
	@Autowired
	private JesqueClientFactory jesqueClientFactory;
	@Autowired
	private UserPrivilegeMgr userPrivilegeMgr;
	@Autowired
	private PortalService portalService;
	@Autowired
    private PropertyMgrProvider propertyMgrProvider;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(null == event.getApplicationContext().getParent()) {
			workerPoolFactory.getWorkerPool().addQueue(RELOCATION_CANCEL_QUE);
		}
	}

	private void pushToQueque(String relocationId, long delayTime) {

		final Job job = new Job(RelocationCancelAction.class.getName(), relocationId);

		jesqueClientFactory.getClientPool().delayedEnqueue(RELOCATION_CANCEL_QUE, job, delayTime);
	}

	@Override
	public SearchRelocationRequestsResponse searchRelocationRequests(SearchRelocationRequestsCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4920049210L, cmd.getAppId(), null,cmd.getCurrentProjectId());//物品搬迁全部权限
		}
		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		Long orgId = null;
		if (RelocationOwnerType.fromCode(cmd.getOwnerType()) == RelocationOwnerType.ORGANIZATION) {
			orgId = cmd.getOwnerId();
			cmd.setOwnerId(null);
			cmd.setOwnerType(null);
		}

		List<RelocationRequest> requests = relocationProvider.searchRelocationRequests(cmd.getNamespaceId(), cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getKeyword(), cmd.getStartTime(), cmd.getEndTime(), cmd.getStatus(), orgId, cmd.getPageAnchor(),
				cmd.getPageSize());

		SearchRelocationRequestsResponse response = new SearchRelocationRequestsResponse();

		int size = requests.size();
		if(size > 0){
			response.setRequests(requests.stream().map(r -> {
				RelocationRequestDTO d = ConvertHelper.convert(r, RelocationRequestDTO.class);

				return d;
			}).collect(Collectors.toList()));
			if(size != pageSize){
				response.setNextPageAnchor(null);
			}else{
				response.setNextPageAnchor(requests.get(size - 1).getCreateTime().getTime());
			}
		}

		return response;
	}

	@Override
	public RelocationRequestDTO getRelocationRequestDetail(GetRelocationRequestDetailCommand cmd) {

        RelocationRequest request = relocationProvider.findRelocationRequestById(cmd.getId());
        if (null == request) {
            LOGGER.error("RelocationRequest not found.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "RelocationRequest not found.");
        }
        RelocationRequestDTO dto = ConvertHelper.convert(request, RelocationRequestDTO.class);
        populateRequestDTO(request, dto);
//      小区场景查询客户类型
        if (null != request.getOrgOwnerTypeId()) {
            OrganizationOwnerType ownerType = propertyMgrProvider.findOrganizationOwnerTypeById(request.getOrgOwnerTypeId());
            dto.setOrgOwnerType(ownerType.getDisplayName());
        }
        return dto;
	}

	private void populateRequestDTO(RelocationRequest request, RelocationRequestDTO dto) {
		List<RelocationRequestItem> items = relocationProvider.listRelocationRequestItems(request.getId());
		dto.setItems(items.stream().map(r -> {
			RelocationRequestItemDTO itemDTO = ConvertHelper.convert(r, RelocationRequestItemDTO.class);

			List<RelocationRequestAttachment> attachments = relocationProvider.listRelocationRequestAttachments(
					EntityType.RELOCATION_REQUEST_ITEM.getCode(), r.getId());
//			itemDTO.setAttachments(attachments.stream().map(a -> {
//				AttachmentDescriptor ad = ConvertHelper.convert(a, AttachmentDescriptor.class);
//				ad.setContentUrl(contentServerService.parserUri(a.getContentUri(), EntityType.USER.getCode(), a.getCreatorUid()));
//				return ad;
//			}).collect(Collectors.toList()));
			if(attachments.size() == 1){
				RelocationRequestAttachment att = attachments.get(0);
				AttachmentDescriptor ad = ConvertHelper.convert(att, AttachmentDescriptor.class);
				ad.setContentUrl(contentServerService.parserUri(att.getContentUri(), EntityType.USER.getCode(), att.getCreatorUid()));
				itemDTO.setAttachment(ad);
			}

			return itemDTO;
		}).collect(Collectors.toList()));

		String flowCaseUrl = configProvider.getValue(ConfigConstants.RELOCATION_FLOWCASE_URL, "");

		dto.setFlowCaseUrl(String.format(flowCaseUrl, request.getFlowCaseId(), FlowUserType.APPLIER.getCode()));
	}

	@Override
	public RelocationInfoDTO getRelocationUserInfo(GetRelocationUserInfoCommand cmd) {


		RelocationInfoDTO dto = new RelocationInfoDTO();
		return dto;

		//TODO 标准版没有场景的概念，直接使用园区场景。根据下面的逻辑，园区场景什么事情都没做，直接返回

//		Long userId = UserContext.currentUserId();
//		Integer namespaceId = UserContext.getCurrentNamespaceId();
//		SceneTokenDTO sceneToken = userService.checkSceneToken(userId, cmd.getSceneToken());
//		SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
//		RelocationInfoDTO dto = new RelocationInfoDTO();
//		switch(sceneType) {
//			case DEFAULT:
//			case PARK_TOURIST:
////				community = communityProvider.findCommunityById(sceneToken.getEntityId());
//
//				break;
//			case FAMILY:
////				FamilyDTO family = familyProvider.getFamilyById(sceneToken.getEntityId());
//
//				break;
//			case PM_ADMIN:
//			case ENTERPRISE:
//			case ENTERPRISE_NOAUTH:
//				Long organizationId = sceneToken.getEntityId();
//				dto.setCurrOrgId(organizationId);
//				OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, organizationId);
//				if (null != member) {
//					dto.setUserName(member.getContactName());
//					dto.setContactPhone(member.getContactToken());
//				}
//
//				OrganizationGroupType groupType = OrganizationGroupType.ENTERPRISE;
//				List<Organization> organizations = organizationService.listUserOrganizations(namespaceId, userId, groupType);
//
//				dto.setOrganizations(organizations.stream().map(r -> {
//					OrganizationBriefInfoDTO org = new OrganizationBriefInfoDTO();
//					org.setOrganizationId(r.getId());
//					org.setOrganizationName(r.getName());
//					return org;
//				}).collect(Collectors.toList()));
//
//				break;
//			default:
//				LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneToken);
//				break;
//		}
//
//		return dto;
	}

	@Override
	public RelocationRequestDTO requestRelocation(RequestRelocationCommand cmd) {

		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		RelocationRequest request = ConvertHelper.convert(cmd, RelocationRequest.class);
		request.setRelocationDate(new Timestamp(cmd.getRelocationDate()));
//标准版修改，由前端传入当前的项目Id
//		if(StringUtils.isNotEmpty(cmd.getOrgOwnerType())){
////		    小区场景
//            OrganizationOwnerType ownerType = propertyMgrProvider.findOrganizationOwnerTypeByDisplayName(cmd.getOrgOwnerType());
//            request.setOrgOwnerTypeId(ownerType.getId());
//        }
//
//		OrganizationCommunityRequest orgRequest = organizationProvider.getOrganizationCommunityRequestByOrganizationId(cmd.getRequestorEnterpriseId());
//		if (null == orgRequest) {
//			LOGGER.error("OrganizationCommunityRequest not found, cmd={}", cmd);
//			throw RuntimeErrorException.errorWith(RelocationErrorCode.SCOPE, RelocationErrorCode.ERROR_INVALID_PARAMETER,
//					"OrganizationCommunityRequest not found.");
//		}
//		request.setOwnerId(orgRequest.getCommunityId());
//		request.setOwnerType(RelocationOwnerType.COMMUNITY.getCode());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

		request.setRequestNo(sdf.format(new Date()) + String.valueOf(generateRandomNumber(3)));
		dbProvider.execute(status -> {
			relocationProvider.createRelocationRequest(request);

			if (null != cmd.getItems()) {
				cmd.getItems().stream().forEach(r -> {
					RelocationRequestItem item = new RelocationRequestItem();
					item.setNamespaceId(request.getNamespaceId());
					item.setOwnerType(request.getOwnerType());
					item.setOwnerId(request.getOwnerId());
					item.setRequestId(request.getId());
					item.setItemName(r.getItemName());
					item.setItemQuantity(r.getItemQuantity());
					relocationProvider.createRelocationRequestItem(item);

//					List<AttachmentDescriptor> attachments = r.getAttachments();
//					if (null != attachments) {
//						attachments.forEach(a -> {
//							RelocationRequestAttachment att = ConvertHelper.convert(a, RelocationRequestAttachment.class);
//							att.setOwnerType(EntityType.RELOCATION_REQUEST_ITEM.getCode());
//							att.setOwnerId(item.getId());
//							relocationProvider.createRelocationRequestAttachment(att);
//						});
//					}
					if(null != r.getAttachment()){
						RelocationRequestAttachment att = ConvertHelper.convert(r.getAttachment(), RelocationRequestAttachment.class);
						att.setOwnerType(EntityType.RELOCATION_REQUEST_ITEM.getCode());
						att.setOwnerId(item.getId());
						relocationProvider.createRelocationRequestAttachment(att);
					}
				});
			}
			//创建工作流case
			FlowCase flowCase = createFlowCase(request, cmd.getItems());
			request.setFlowCaseId(flowCase.getId());

			//创建二维码
			String flowCaseUrl = configProvider.getValue(ConfigConstants.RELOCATION_FLOWCASE_URL, "");
			NewQRCodeCommand qrCmd = new NewQRCodeCommand();
			qrCmd.setRouteUri(String.format(flowCaseUrl, flowCase.getId(), FlowUserType.PROCESSOR.getCode()));
			qrCmd.setHandler(QRCodeHandler.FLOW.getCode());
			QRCodeDTO qRCodeDTO = qRCodeService.createQRCode(qrCmd);
			request.setQrCodeUrl(qRCodeDTO.getUrl());

			relocationProvider.updateRelocationRequest(request);
			return null;
		});

		//获取搬迁日期后一天,当作过期时间
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(request.getRelocationDate().getTime());

		//TODO:方便测试  5分钟后过期
		if (configProvider.getBooleanValue("relocation.test", false)) {
			int hour = configProvider.getIntValue("relocation.hour", 0);
			int minute = configProvider.getIntValue("relocation.minute", 0);

			if (hour != 0) {
				calendar.set(Calendar.HOUR_OF_DAY, hour);
			}
			if (minute != 0) {
				calendar.set(Calendar.MINUTE, minute);
			}
		}else {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}

		pushToQueque(String.valueOf(request.getId()), calendar.getTimeInMillis());

		RelocationRequestDTO dto = ConvertHelper.convert(request, RelocationRequestDTO.class);
		populateRequestDTO(request, dto);

		return dto;
	}

	@Override
	public QueryRelocationStatisticsResponse queryRelocationStatistics(QueryRelocationStatisticsCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4920049220L, cmd.getAppId(), null,cmd.getCurrentProjectId());
		}
		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		QueryRelocationStatisticsResponse response = new QueryRelocationStatisticsResponse();

		List<RelocationStatistics> relocationStatistics = relocationProvider.queryRelocationStatistics(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getStartTime(), cmd.getEndTime());
		if (relocationStatistics == null || relocationStatistics.size() == 0 ){
			response.setTotalCount(0);
			return response;
		}
		response.setClassifyStatistics(new ArrayList<>());
		Integer totalNum = 0;
		for (RelocationStatistics statistics : relocationStatistics){
			RelocationStatisticsDTO dto = new RelocationStatisticsDTO();
			dto.setCount(statistics.getCount());
			dto.setName(RelocationRequestStatus.fromCode(statistics.getStatus()).getDescription());
			response.getClassifyStatistics().add(dto);
			totalNum += dto.getCount();
		}
		response.setTotalCount(totalNum);
		return response;
	}

	@Override
	public void exportRelocationRequests(SearchRelocationRequestsCommand cmd, HttpServletResponse response) {

		List<RelocationRequest> requests = relocationProvider.searchRelocationRequests(cmd.getNamespaceId(), cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getKeyword(), cmd.getStartTime(), cmd.getEndTime(), cmd.getStatus(), null, null,
				null);
		if (requests == null)
			requests = new ArrayList<>();

		ByteArrayOutputStream out = createRequestStream(requests);
		DownloadUtils.download(out, response);
	}

	private ByteArrayOutputStream createRequestStream(List<RelocationRequest> requests){

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		if (null == requests || requests.isEmpty()) {
			return out;
		}
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("relocationRequest");
		createRequestSheetHead(sheet);
		for (RelocationRequest request : requests ) {
			RelocationRequestDTO dto = ConvertHelper.convert(request, RelocationRequestDTO.class);
			populateRequestDTO(request, dto);
			this.setNewRelocationRequestBookRow(sheet, dto);
		}

		try {
			wb.write(out);
			wb.close();

		} catch (IOException e) {
			LOGGER.error("export is fail", e);
		}
		return out;
	}

	private void setNewRelocationRequestBookRow(Sheet sheet ,RelocationRequestDTO dto){
		Row row = sheet.createRow(sheet.getLastRowNum()+1);
		int i = -1;
		//申请单编号
		row.createCell(++i).setCellValue(dto.getRequestNo());
		//申请时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
		row.createCell(++i).setCellValue(sdf.format(new Date(dto.getCreateTime().getTime())));
		//状态
		row.createCell(++i).setCellValue(RelocationRequestStatus.fromCode(dto.getStatus()).name());
		//申请人
		row.createCell(++i).setCellValue(dto.getRequestorName());
		//手机号码
		if (StringHelper.hasContent(dto.getContactPhone()))
			row.createCell(++i).setCellValue(dto.getContactPhone());
		else
			row.createCell(++i).setCellValue("");
		//公司名称
		if (StringHelper.hasContent(dto.getRequestorEnterpriseName()))
			row.createCell(++i).setCellValue(dto.getRequestorEnterpriseName());
		else
			row.createCell(++i).setCellValue("");
		//公司地址
		if (StringHelper.hasContent(dto.getRequestorEnterpriseAddress()))
			row.createCell(++i).setCellValue(dto.getRequestorEnterpriseAddress());
		else
			row.createCell(++i).setCellValue("");
		//放行日期
		sdf = new SimpleDateFormat("yyyy/MM/dd");
		row.createCell(++i).setCellValue(sdf.format(new Date(dto.getRelocationDate().getTime())));
		//物品清单
		StringBuilder sb = new StringBuilder();
		for (RelocationRequestItemDTO itemDTO : dto.getItems()){
			sb.append(itemDTO.getItemName()).append("*").append(itemDTO.getItemQuantity()).append("、");
		}
		sb.deleteCharAt(sb.length() - 1);
		row.createCell(++i).setCellValue(sb.toString());

	}

	private void createRequestSheetHead(Sheet sheet){
		Row row = sheet.createRow(sheet.getLastRowNum());
		int i =-1 ;
		row.createCell(++i).setCellValue("申请单编号");
		row.createCell(++i).setCellValue("申请时间");
		row.createCell(++i).setCellValue("状态");
		row.createCell(++i).setCellValue("申请人");
		row.createCell(++i).setCellValue("手机号码");
		row.createCell(++i).setCellValue("公司名称");
		row.createCell(++i).setCellValue("公司地址");
		row.createCell(++i).setCellValue("放行日期");
		row.createCell(++i).setCellValue("物品清单");

	}

	/**
	 *
	 * @param n 创建n位随机数
	 * @return
	 */
	private long generateRandomNumber(int n){
		return (long)((Math.random() * 9 + 1) * Math.pow(10, n-1));
	}

	private FlowCase createFlowCase(RelocationRequest request, List<RelocationRequestItemDTO> items) {

		Integer namespaceId = UserContext.getCurrentNamespaceId();

		String ownerType = FlowOwnerType.COMMUNITY.getCode();
		Flow flow = flowService.getEnabledFlow(namespaceId, FlowConstants.RELOCATION_MODULE,
				FlowModuleType.NO_MODULE.getCode(), request.getOwnerId(), ownerType);
		if(null == flow) {
			LOGGER.error("Enable flow not found, moduleId={}", FlowConstants.RELOCATION_MODULE);
			throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_ENABLE_FLOW,
					"Enable flow not found.");

		}
		CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
		createFlowCaseCommand.setApplyUserId(request.getRequestorUid());
		createFlowCaseCommand.setFlowMainId(flow.getFlowMainId());
		createFlowCaseCommand.setFlowVersion(flow.getFlowVersion());
		createFlowCaseCommand.setReferId(request.getId());
		createFlowCaseCommand.setReferType(EntityType.RELOCATION_REQUEST.getCode());

		String locale = UserContext.current().getUser().getLocale();

		Map<String, Object> map = new HashMap<>();
		map.put("requestorName", request.getRequestorName());
		map.put("requestorEnterpriseName", request.getRequestorEnterpriseName());
		map.put("items", getItemName(items));
		map.put("totalNum", items.stream().mapToInt(RelocationRequestItemDTO::getItemQuantity).summaryStatistics().getSum());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		map.put("relocationDate", sdf.format(request.getRelocationDate()));
		String content = localeTemplateService.getLocaleTemplateString(RelocationTemplateCode.SCOPE, RelocationTemplateCode.FLOW_PROCESSOR_CONTENT,
				locale, map, "");

		createFlowCaseCommand.setContent(content);
		createFlowCaseCommand.setCurrentOrganizationId(request.getRequestorEnterpriseId());
		ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
		listServiceModuleAppsCommand.setNamespaceId(namespaceId);
		listServiceModuleAppsCommand.setModuleId(FlowConstants.RELOCATION_MODULE);
		ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(listServiceModuleAppsCommand);
		if (apps!=null && apps.getServiceModuleApps().size()>0)
			createFlowCaseCommand.setTitle(apps.getServiceModuleApps().get(0).getName());

		String serviceName = localeStringService.getLocalizedString(RelocationTemplateCode.SCOPE,
				String.valueOf(RelocationTemplateCode.SERVICE_TYPE_NAME), locale, "");
		createFlowCaseCommand.setServiceType(serviceName);
		FlowCase flowCase = flowService.createFlowCase(createFlowCaseCommand);

		return flowCase;
	}

	private String getItemName(List<RelocationRequestItemDTO> items) {
		if (null == items) {
			return "";
		}else {
			StringBuilder sb = new StringBuilder();
			int size = items.size();
			for (int i = 0; i < size; i++) {
				if (i == size -1) {
					sb.append(items.get(i).getItemName());
				}else {
					sb.append(items.get(i).getItemName()).append("、");
				}
			}
			return sb.toString();
		}
	}
}
