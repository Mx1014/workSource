package com.everhomes.techpark.expansion;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.community.Building;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.general_form.GeneralFormValProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.general_approval.GetGeneralFormValuesCommand;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.addGeneralFormValuesCommand;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.techpark.expansion.*;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.pojos.EhLeaseBuildings;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class EnterpriseApplyBuildingServiceImpl implements EnterpriseApplyBuildingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseApplyBuildingServiceImpl.class);

	@Autowired
	private EnterpriseApplyBuildingProvider enterpriseApplyBuildingProvider;
	@Autowired
	private DbProvider dbProvider;
	@Autowired
	private EnterpriseApplyEntryProvider enterpriseApplyEntryProvider;
	@Autowired
	private GeneralFormService generalFormService;
	@Autowired
	private GeneralFormValProvider generalFormValProvider;
	@Autowired
	private ContentServerService contentServerService;
	@Autowired
	private ConfigurationProvider configProvider;
	@Autowired
	private CommunityProvider communityProvider;
	@Autowired
	private SequenceProvider sequenceProvider;
	@Autowired
	private RegionProvider regionProvider;

	@Override
	public ListLeaseBuildingsResponse listLeaseBuildings(ListLeaseBuildingsCommand cmd) {

		ListLeaseBuildingsResponse response = new ListLeaseBuildingsResponse();

		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		List<LeaseBuilding> leaseBuildings = enterpriseApplyBuildingProvider.listLeaseBuildings(cmd.getNamespaceId(),
				cmd.getCommunityId(), cmd.getPageAnchor(), pageSize);

		int size = leaseBuildings.size();

		response.setLeaseBuildingDTOs(leaseBuildings.stream().map(r -> {
			LeaseBuildingDTO dto = ConvertHelper.convert(r, LeaseBuildingDTO.class);
			populateLeaseBuildingDTO(dto, r);

			return dto;
		}).collect(Collectors.toList()));

		if(size != pageSize){
			response.setNextPageAnchor(null);
		}else{
			response.setNextPageAnchor(leaseBuildings.get(size - 1).getDefaultOrder());
		}

		return response;
	}

	@Override
	public LeaseBuildingDTO createLeaseBuilding(CreateLeaseBuildingCommand cmd) {

		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		//检查楼栋名
		if (!enterpriseApplyBuildingProvider.verifyBuildingName(cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getName())) {
			throw RuntimeErrorException.errorWith(ApplyEntryErrorCodes.SCOPE, ApplyEntryErrorCodes.ERROR_BUILDING_NAME_EXIST,
					"Building name exist");
		}

		LeaseBuilding leaseBuilding = ConvertHelper.convert(cmd, LeaseBuilding.class);

		leaseBuilding.setNamespaceId(UserContext.getCurrentNamespaceId());
		leaseBuilding.setStatus(LeaseBulidingStatus.ACTIVE.getCode());
		leaseBuilding.setDeleteFlag((byte) 1);
		dbProvider.execute((TransactionStatus status) -> {
			enterpriseApplyBuildingProvider.createLeaseBuilding(leaseBuilding);
			addAttachments(cmd.getAttachments(), leaseBuilding);

			addGeneralFormInfo(cmd.getGeneralFormId(), cmd.getFormValues(), EntityType.LEASE_BUILDING.getCode(),
					leaseBuilding.getId(), cmd.getCustomFormFlag());

			return null;
		});

		LeaseBuildingDTO dto = ConvertHelper.convert(leaseBuilding, LeaseBuildingDTO.class);

		populateLeaseBuildingDTO(dto, leaseBuilding);

		return dto;
	}

	@Override
	public LeaseBuildingDTO updateLeaseBuilding(UpdateLeaseBuildingCommand cmd) {

		LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingById(cmd.getId());

		BeanUtils.copyProperties(cmd, leaseBuilding);

		dbProvider.execute((TransactionStatus status) -> {
			enterpriseApplyBuildingProvider.updateLeaseBuilding(leaseBuilding);

			//重新添加banner
			enterpriseApplyEntryProvider.deleteLeasePromotionAttachment(EntityType.LEASE_BUILDING.getCode(), leaseBuilding.getId());
			addAttachments(cmd.getAttachments(), leaseBuilding);

			generalFormValProvider.deleteGeneralFormVals(EntityType.LEASE_BUILDING.getCode(), leaseBuilding.getId());
			addGeneralFormInfo(cmd.getGeneralFormId(), cmd.getFormValues(), EntityType.LEASE_BUILDING.getCode(),
					leaseBuilding.getId(), cmd.getCustomFormFlag());

			return null;
		});

		LeaseBuildingDTO dto = ConvertHelper.convert(leaseBuilding, LeaseBuildingDTO.class);

		populateLeaseBuildingDTO(dto, leaseBuilding);

		return dto;
	}

	private void populateFormInfo(LeaseBuildingDTO dto) {
		if (LeasePromotionFlag.ENABLED.getCode() == dto.getCustomFormFlag()) {

			GetGeneralFormValuesCommand cmdValues = new GetGeneralFormValuesCommand();
			cmdValues.setSourceType(EntityType.LEASE_BUILDING.getCode());
			cmdValues.setSourceId(dto.getId());
			cmdValues.setOriginFieldFlag(NormalFlag.NEED.getCode());
			List<PostApprovalFormItem> formValues = generalFormService.getGeneralFormValues(cmdValues);
			dto.setFormValues(formValues);
		}
	}

	private void addGeneralFormInfo(Long generalFormId, List<PostApprovalFormItem> formValues, String sourceType,
									Long sourceId, Byte customFormFlag) {
		if (LeasePromotionFlag.ENABLED.getCode() == customFormFlag) {
			addGeneralFormValuesCommand cmd = new addGeneralFormValuesCommand();
			cmd.setGeneralFormId(generalFormId);
			cmd.setValues(formValues);
			cmd.setSourceId(sourceId);
			cmd.setSourceType(sourceType);
			generalFormService.addGeneralFormValues(cmd);
		}
	}

	@Override
	public LeaseBuildingDTO getLeaseBuildingById(GetLeaseBuildingByIdCommand cmd) {

		LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingById(cmd.getId());

		LeaseBuildingDTO dto = ConvertHelper.convert(leaseBuilding, LeaseBuildingDTO.class);

		populateLeaseBuildingDTO(dto, leaseBuilding);

		return dto;
	}

	@Override
	public void deleteLeaseBuilding(DeleteLeaseBuildingCommand cmd) {
		LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingById(cmd.getId());
		leaseBuilding.setStatus(LeaseBulidingStatus.INACTIVE.getCode());
		enterpriseApplyBuildingProvider.updateLeaseBuilding(leaseBuilding);

	}

	private void populateLeaseBuildingDTO(LeaseBuildingDTO dto, LeaseBuilding leaseBuilding) {
		List<LeasePromotionAttachment> attachments = enterpriseApplyEntryProvider.findAttachmentsByOwnerTypeAndOwnerId(
				EntityType.LEASE_BUILDING.getCode(), leaseBuilding.getId());

		populatePostUrl(dto, leaseBuilding.getPosterUri());
		populateLeaseBuildingAttachments(dto, attachments);
		processBuilingDetailUrl(dto);
		//表单信息
		populateFormInfo(dto);

		Community community = communityProvider.findCommunityById(leaseBuilding.getCommunityId());
		dto.setCommunityName(community.getName());
		if (null != leaseBuilding.getBuildingId()) {
			Building building = communityProvider.findBuildingById(leaseBuilding.getBuildingId());
			if (null != building) {
				dto.setBuildingName(building.getName());
			}
		}
	}

	private void addAttachments(List<BuildingForRentAttachmentDTO> attachments, LeaseBuilding leaseBuilding) {
		if (null != attachments) {
			attachments.forEach(a -> {
				LeasePromotionAttachment attachment = ConvertHelper.convert(a, LeasePromotionAttachment.class);
				attachment.setOwnerId(leaseBuilding.getId());
				attachment.setOwnerType(EntityType.LEASE_BUILDING.getCode());
				attachment.setCreatorUid(leaseBuilding.getCreatorUid());
				enterpriseApplyEntryProvider.addPromotionAttachment(attachment);
			});
		}
	}

	private void processBuilingDetailUrl(LeaseBuildingDTO dto) {
		String homeUrl = configProvider.getValue(ConfigConstants.HOME_URL, "");
		String detailUrl = configProvider.getValue(ConfigConstants.APPLY_ENTRY_LEASE_BUILDING_DETAIL_URL, "");

		detailUrl = String.format(detailUrl, dto.getId());

		dto.setDetailUrl(homeUrl + detailUrl);

	}

	private void processProjectDetailUrl(LeaseProjectDTO dto) {
		String homeUrl = configProvider.getValue(ConfigConstants.HOME_URL, "");
		String detailUrl = configProvider.getValue(ConfigConstants.APPLY_ENTRY_LEASE_PROJECT_DETAIL_URL, "");

		detailUrl = String.format(detailUrl, dto.getProjectId());

		dto.setDetailUrl(homeUrl + detailUrl);

	}

	private void populatePostUrl(LeaseBuildingDTO dto, String uri) {
		String url = contentServerService.parserUri(uri, EntityType.USER.getCode(), UserContext.currentUserId());
		dto.setPosterUrl(url);
	}

	private void populateLeaseBuildingAttachments(LeaseBuildingDTO dto, List<LeasePromotionAttachment> attachments) {

		if(attachments != null) {
			dto.setAttachments(attachments.stream().map(a -> {

				String url = contentServerService.parserUri(a.getContentUri(), EntityType.USER.getCode(), UserContext.currentUserId());

				BuildingForRentAttachmentDTO d = new BuildingForRentAttachmentDTO();
				d.setContentUrl(url);
				d.setContentUri(a.getContentUri());
				return d;
			}).collect(Collectors.toList()));
		}
	}

	@Override
	public void updateLeaseBuildingOrder(UpdateLeaseBuildingOrderCommand cmd) {
		LeaseBuilding leaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingById(cmd.getId());
		LeaseBuilding exchangeLeaseBuilding = enterpriseApplyBuildingProvider.findLeaseBuildingById(cmd.getExchangeId());

		Long order = leaseBuilding.getDefaultOrder();
		leaseBuilding.setDefaultOrder(exchangeLeaseBuilding.getDefaultOrder());
		exchangeLeaseBuilding.setDefaultOrder(order);

		dbProvider.execute((TransactionStatus status) -> {
			enterpriseApplyBuildingProvider.updateLeaseBuilding(leaseBuilding);
			enterpriseApplyBuildingProvider.updateLeaseBuilding(exchangeLeaseBuilding);

			return null;
		});
	}

	@Override
	public void syncLeaseBuildings(ListLeaseBuildingsCommand cmd) {

		CrossShardListingLocator locator = new CrossShardListingLocator();

		List<LeaseBuilding> existLeaseBuildings = enterpriseApplyBuildingProvider.listLeaseBuildings(cmd.getNamespaceId(),
				cmd.getCommunityId(), null, null);

		List<Building> buildings = communityProvider.ListBuildingsByCommunityId(locator, Integer.MAX_VALUE,
				cmd.getCommunityId(), cmd.getNamespaceId(), null);

		List<LeaseBuilding> leaseBuildings = buildings.stream().filter(r ->
			existLeaseBuildings.stream().noneMatch(e -> e.getBuildingId().equals(r.getId()))
			).map(r -> {
			long id = sequenceProvider.getNextSequence(NameMapper
					.getSequenceDomainFromTablePojo(EhLeaseBuildings.class));
			LeaseBuilding leaseBuilding = ConvertHelper.convert(r, LeaseBuilding.class);
			leaseBuilding.setId(id);
			leaseBuilding.setBuildingId(r.getId());
			leaseBuilding.setManagerContact(r.getContact());
			leaseBuilding.setDeleteFlag((byte)0);
			leaseBuilding.setDefaultOrder(id);
			return leaseBuilding;
		}).collect(Collectors.toList());

		enterpriseApplyBuildingProvider.createLeaseBuildings(leaseBuildings);
	}

	@Override
	public List<BriefLeaseProjectDTO> listAllLeaseProjects(ListAllLeaseProjectsCommand cmd) {

		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		List<Community> communities = communityProvider.listCommunitiesByNamespaceId(cmd.getNamespaceId());

		List<BriefLeaseProjectDTO> result = communities.stream().map(r -> {
			BriefLeaseProjectDTO dto = new BriefLeaseProjectDTO();
			dto.setProjectId(r.getId());
			dto.setProjectName(r.getName());
			return dto;
		}).collect(Collectors.toList());

		return result;
	}

	@Override
	public listLeaseProjectsResponse listLeaseProjects(ListLeaseProjectsCommand cmd) {

		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		List<Community> communities = communityProvider.listCommunitiesByCityIdAndAreaId(cmd.getNamespaceId(), cmd.getCityId(),
				cmd.getAreaId(), cmd.getKeyword(), cmd.getPageAnchor(), pageSize);

		listLeaseProjectsResponse response = new listLeaseProjectsResponse();

		int size = communities.size();

		response.setProjects(communities.stream().map(r -> {
			LeaseProjectDTO dto = new LeaseProjectDTO();
			populateProjectBasicInfo(dto, r);

			processProjectDetailUrl(dto);
			return dto;
		}).collect(Collectors.toList()));

		if(size != pageSize){
			response.setNextPageAnchor(null);
		}else{
			response.setNextPageAnchor(communities.get(size - 1).getId());
		}

		return response;
	}

	private void populateProjectBasicInfo (LeaseProjectDTO dto, Community r) {

		LeaseProject leaseProject = enterpriseApplyBuildingProvider.findLeaseProjectByProjectId(r.getId());

		if (null == leaseProject) {
			dto.setProjectId(r.getId());
			dto.setNamespaceId(r.getNamespaceId());
			dto.setName(r.getName());
			dto.setCityId(r.getCityId());
			dto.setCityName(r.getCityName());
			dto.setAreaId(r.getAreaId());
			dto.setAreaName(r.getAreaName());
			dto.setAddress(r.getAddress());
//			dto.setContactPhone(r.get);
		}else {
			dto.setProjectId(leaseProject.getProjectId());
			dto.setNamespaceId(leaseProject.getNamespaceId());
			dto.setName(r.getName());
			dto.setCityId(leaseProject.getCityId());
			dto.setCityName(leaseProject.getCityName());
			dto.setAreaId(leaseProject.getAreaId());
			dto.setAreaName(leaseProject.getAreaName());
			dto.setAddress(leaseProject.getAddress());
			dto.setContactPhone(leaseProject.getContactPhone());
		}
	}

	private void populateProjectDetailInfo (LeaseProjectDTO dto, Community r, LeaseProject leaseProject) {
//		populateProjectBasicInfo(dto, r);
		dto.setName(r.getName());
		String json = leaseProject.getExtraInfoJson();
		LeaseProjectExtraInfo extraInfo = JSONObject.parseObject(json, LeaseProjectExtraInfo.class);
		BeanUtils.copyProperties(extraInfo, dto);

		List<Long> communityIds = enterpriseApplyBuildingProvider.listLeaseProjectCommunities(leaseProject.getId());
		dto.setProjectDTOS(communityIds.stream().map(c -> {
			ProjectDTO d = new ProjectDTO();
			d.setProjectId(c);
			return d;
		}).collect(Collectors.toList()));

		Long userId = UserContext.currentUserId();
		//设置封面图url 和banner图
		if (null != dto.getPosterUri()) {
			dto.setPosterUrl(contentServerService.parserUri(dto.getPosterUri(), EntityType.USER.getCode(), userId));
		}

		List<LeasePromotionAttachment> attachments = enterpriseApplyEntryProvider.findAttachmentsByOwnerTypeAndOwnerId(
				EntityType.LEASE_PROJECT.getCode(), leaseProject.getId());
		dto.setAttachments(attachments.stream().map(a -> {
			BuildingForRentAttachmentDTO ad = ConvertHelper.convert(a, BuildingForRentAttachmentDTO.class);
			ad.setContentUrl(contentServerService.parserUri(a.getContentUri(), EntityType.USER.getCode(), userId));
			return ad;
		}).collect(Collectors.toList()));

	}

	@Override
	public LeaseProjectDTO updateLeaseProject(UpdateLeaseProjectCommand cmd) {

		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}

		if (null != cmd.getCityId()) {
			Region region = regionProvider.findRegionById(cmd.getCityId());
			cmd.setCityName(region.getName());
		}

		if (null != cmd.getAreaId()) {
			Region region = regionProvider.findRegionById(cmd.getAreaId());
			cmd.setAreaName(region.getName());
		}

		LeaseProject leaseProject = enterpriseApplyBuildingProvider.findLeaseProjectByProjectId(cmd.getProjectId());
		LeaseProject[] leaseProjects = new LeaseProject[1];

		if (null == leaseProject) {
			leaseProject = ConvertHelper.convert(cmd, LeaseProject.class);

			leaseProject.setNamespaceId(cmd.getNamespaceId());
			LeaseProjectExtraInfo extraInfo = ConvertHelper.convert(cmd, LeaseProjectExtraInfo.class);
			leaseProject.setExtraInfoJson(JSONObject.toJSONString(extraInfo));

			leaseProjects[0] = leaseProject;

			dbProvider.execute((TransactionStatus status) -> {
				enterpriseApplyBuildingProvider.createLeaseProject(leaseProjects[0]);

				addAttachments(cmd.getAttachments(), leaseProjects[0]);
				addLeaseProjectCommunities(cmd.getCommunityIds(), leaseProjects[0]);
				return null;
			});
		}else {

			BeanUtils.copyProperties(cmd, leaseProject);
			LeaseProjectExtraInfo extraInfo = ConvertHelper.convert(cmd, LeaseProjectExtraInfo.class);

			leaseProject.setExtraInfoJson(JSONObject.toJSONString(extraInfo));

			leaseProjects[0] = leaseProject;
			dbProvider.execute((TransactionStatus status) -> {
				enterpriseApplyBuildingProvider.updateLeaseProject(leaseProjects[0]);

				enterpriseApplyEntryProvider.deleteLeasePromotionAttachment(EntityType.LEASE_PROJECT.getCode(), leaseProjects[0].getId());
				addAttachments(cmd.getAttachments(), leaseProjects[0]);

				enterpriseApplyBuildingProvider.deleteLeaseProjectCommunity(leaseProjects[0].getId());
				addLeaseProjectCommunities(cmd.getCommunityIds(), leaseProjects[0]);

				return null;
			});
		}

		return ConvertHelper.convert(leaseProject, LeaseProjectDTO.class);
	}

	@Override
	public LeaseProjectDTO getLeaseProjectById(GetLeaseProjectByIdCommand cmd) {

		LeaseProjectDTO dto;

		Community community = communityProvider.findCommunityById(cmd.getProjectId());

		if (null == community) {
			throw RuntimeErrorException.errorWith(ApplyEntryErrorCodes.SCOPE, ApplyEntryErrorCodes.ERROR_BUILDING_NAME_EXIST,
					"Community not found");
		}

		LeaseProject leaseProject = enterpriseApplyBuildingProvider.findLeaseProjectByProjectId(cmd.getProjectId());

		if (null == leaseProject) {
			dto = new LeaseProjectDTO();

			populateProjectBasicInfo(dto, community);
		}else {
			dto = ConvertHelper.convert(leaseProject, LeaseProjectDTO.class);

			populateProjectDetailInfo(dto, community, leaseProject);
		}

		List<LeaseBuilding> leaseBuildings = enterpriseApplyBuildingProvider.listLeaseBuildings(community.getNamespaceId(),
				cmd.getProjectId(), null, 5);

		dto.setBuildings(leaseBuildings.stream().map(r -> {
			LeaseBuildingDTO d = ConvertHelper.convert(r, LeaseBuildingDTO.class);
			populatePostUrl(d, r.getPosterUri());
			processBuilingDetailUrl(d);
			return d;
		}).collect(Collectors.toList()));
		return dto;
	}

	private void addLeaseProjectCommunities(List<Long> communityIds, LeaseProject leaseProject) {
		if (null != communityIds) {
			communityIds.forEach(m -> {
				LeaseProjectCommunity leaseProjectCommunity = new LeaseProjectCommunity();
				leaseProjectCommunity.setLeaseProjectId(leaseProject.getId());
				leaseProjectCommunity.setCommunityId(m);
				enterpriseApplyBuildingProvider.createLeaseProjectCommunity(leaseProjectCommunity);
			});
		}
	}

	private void addAttachments(List<BuildingForRentAttachmentDTO> attachmentDTOs, LeaseProject leaseProject) {
		if (null != attachmentDTOs) {
			for (BuildingForRentAttachmentDTO buildingForRentAttachmentDTO : attachmentDTOs) {
				LeasePromotionAttachment attachment = ConvertHelper.convert(buildingForRentAttachmentDTO, LeasePromotionAttachment.class);
				attachment.setOwnerId(leaseProject.getId());
				attachment.setOwnerType(EntityType.LEASE_PROJECT.getCode());
				attachment.setCreatorUid(leaseProject.getCreatorUid());
				enterpriseApplyEntryProvider.addPromotionAttachment(attachment);
			}
		}
	}
}
