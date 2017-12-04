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
import com.everhomes.qrcode.QRCodeService;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.organization.OrganizationGroupType;

import com.everhomes.rest.pmtask.PmTaskErrorCode;
import com.everhomes.rest.qrcode.NewQRCodeCommand;
import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.rest.qrcode.QRCodeHandler;
import com.everhomes.rest.relocation.*;
import com.everhomes.rest.relocation.AttachmentDescriptor;

import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;

import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class RelocationServiceImpl implements RelocationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RelocationServiceImpl.class);

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

	@Override
	public SearchRelocationRequestsResponse searchRelocationRequests(SearchRelocationRequestsCommand cmd) {

		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		List<RelocationRequest> requests = relocationProvider.searchRelocationRequests(cmd.getNamespaceId(), cmd.getOwnerType(),
				cmd.getOwnerId(), cmd.getKeyword(), cmd.getStartTime(), cmd.getEndTime(), cmd.getStatus(), cmd.getPageAnchor(),
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

		return dto;
	}

	private void populateRequestDTO(RelocationRequest request, RelocationRequestDTO dto) {
		List<RelocationRequestItem> items = relocationProvider.listRelocationRequestItems(request.getId());
		dto.setItems(items.stream().map(r -> {
			RelocationRequestItemDTO itemDTO = ConvertHelper.convert(r, RelocationRequestItemDTO.class);

			List<RelocationRequestAttachment> attachments = relocationProvider.listRelocationRequestAttachments(
					EntityType.RELOCATION_REQUEST_ITEM.getCode(), r.getId());
			itemDTO.setAttachments(attachments.stream().map(a -> {
				AttachmentDescriptor ad = ConvertHelper.convert(a, AttachmentDescriptor.class);
				ad.setContentUrl(contentServerService.parserUri(a.getContentUri(), EntityType.USER.getCode(), a.getCreatorUid()));
				return ad;
			}).collect(Collectors.toList()));

			return itemDTO;
		}).collect(Collectors.toList()));

		String flowCaseUrl = configProvider.getValue(ConfigConstants.RELOCATION_FLOWCASE_URL, "");

		dto.setFlowCaseUrl(String.format(flowCaseUrl, request.getFlowCaseId(), FlowUserType.APPLIER.getCode()));
	}

	@Override
	public RelocationInfoDTO getRelocationUserInfo(GetRelocationUserInfoCommand cmd) {

		Long userId = UserContext.currentUserId();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		SceneTokenDTO sceneToken = userService.checkSceneToken(userId, cmd.getSceneToken());
		SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
		RelocationInfoDTO dto = new RelocationInfoDTO();
		switch(sceneType) {
			case DEFAULT:
			case PARK_TOURIST:
//				community = communityProvider.findCommunityById(sceneToken.getEntityId());

				break;
			case FAMILY:
//				FamilyDTO family = familyProvider.getFamilyById(sceneToken.getEntityId());

				break;
			case PM_ADMIN:
			case ENTERPRISE:
			case ENTERPRISE_NOAUTH:
				Long organizationId = sceneToken.getEntityId();
				OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, organizationId);
				if (null != member) {
					dto.setUserName(member.getContactName());
					dto.setContactPhone(member.getContactToken());
				}

				OrganizationGroupType groupType = OrganizationGroupType.ENTERPRISE;
				List<Organization> organizations = organizationService.listUserOrganizations(namespaceId, userId, groupType);

				dto.setOrganizations(organizations.stream().map(r -> {
					OrganizationBriefInfoDTO org = new OrganizationBriefInfoDTO();
					org.setOrganizationId(r.getId());
					org.setOrganizationName(r.getName());
					return org;
				}).collect(Collectors.toList()));

				break;
			default:
				LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneToken);
				break;
		}

		return dto;
	}

	@Override
	public RelocationRequestDTO requestRelocation(RequestRelocationCommand cmd) {

		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		RelocationRequest request = ConvertHelper.convert(cmd, RelocationRequest.class);
		request.setRelocationDate(new Timestamp(cmd.getRelocationDate()));

		OrganizationCommunityRequest orgRequest = organizationProvider.getOrganizationCommunityRequestByOrganizationId(cmd.getRequestorEnterpriseId());
		if (null == orgRequest) {
			LOGGER.error("OrganizationCommunityRequest not found, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"OrganizationCommunityRequest not found.");
		}

		request.setOwnerId(orgRequest.getCommunityId());
		request.setOwnerType(RelocationOwnerType.COMMUNITY.getCode());

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

					List<AttachmentDescriptor> attachments = r.getAttachments();
					if (null != attachments) {
						attachments.forEach(a -> {
							RelocationRequestAttachment att = ConvertHelper.convert(a, RelocationRequestAttachment.class);
							att.setOwnerType(EntityType.RELOCATION_REQUEST_ITEM.getCode());
							att.setOwnerId(item.getId());
							relocationProvider.createRelocationRequestAttachment(att);
						});
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

		RelocationRequestDTO dto = ConvertHelper.convert(request, RelocationRequestDTO.class);
		populateRequestDTO(request, dto);

		return dto;
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		map.put("relocationDate", sdf.format(request.getRelocationDate()));
		String content = localeTemplateService.getLocaleTemplateString(RelocationTemplateCode.SCOPE, RelocationTemplateCode.FLOW_PROCESSOR_CONTENT,
				locale, map, "");

		createFlowCaseCommand.setContent(content);
		createFlowCaseCommand.setCurrentOrganizationId(request.getRequestorEnterpriseId());

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
