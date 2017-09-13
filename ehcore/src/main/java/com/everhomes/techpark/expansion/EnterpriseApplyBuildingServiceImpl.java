package com.everhomes.techpark.expansion;

import com.everhomes.community.Building;
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

	@Override
	public ListLeaseBuildingsResponse listLeaseBuildings(ListLeaseBuildingsCommand cmd) {

		ListLeaseBuildingsResponse response = new ListLeaseBuildingsResponse();

		Integer namespaceId = UserContext.getCurrentNamespaceId();

		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		List<LeaseBuilding> leaseBuildings = enterpriseApplyBuildingProvider.listLeaseBuildings(namespaceId,
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

		Integer namespaceId = UserContext.getCurrentNamespaceId();

		//检查楼栋名
		if (!enterpriseApplyBuildingProvider.verifyBuildingName(namespaceId, cmd.getCommunityId(), cmd.getName())) {
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
		processDetailUrl(dto);
		//表单信息
		populateFormInfo(dto);

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

	private void processDetailUrl(LeaseBuildingDTO dto) {
		String homeUrl = configProvider.getValue(ConfigConstants.HOME_URL, "");
		String detailUrl = configProvider.getValue(ConfigConstants.APPLY_ENTRY_LEASE_BUILDING_DETAIL_URL, "");

		detailUrl = String.format(detailUrl, dto.getId());

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
}
