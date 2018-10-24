package com.everhomes.yellowPage.standard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.enterprise.GetAuthOrgByProjectIdAndAppIdCommand;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.portal.ServiceAllianceInstanceConfig;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.yellowPage.AllianceTagDTO;
import com.everhomes.rest.yellowPage.AllianceTagGroupDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceAttachmentDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceAttachmentType;
import com.everhomes.rest.yellowPage.ServiceAllianceBelongType;
import com.everhomes.rest.yellowPage.ServiceAllianceCategoryDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceDTO;
import com.everhomes.rest.yellowPage.YellowPageServiceErrorCode;
import com.everhomes.rest.yellowPage.YellowPageStatus;
import com.everhomes.rest.yellowPage.standard.SelfDefinedState;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.yellowPage.AllianceConfigState;
import com.everhomes.yellowPage.AllianceConfigStateProvider;
import com.everhomes.yellowPage.AllianceStandardService;
import com.everhomes.yellowPage.AllianceTagProvider;
import com.everhomes.yellowPage.ServiceAllianceAttachment;
import com.everhomes.yellowPage.ServiceAllianceCategories;
import com.everhomes.yellowPage.ServiceAlliances;
import com.everhomes.yellowPage.YellowPageProvider;
import com.everhomes.yellowPage.YellowPageService;
import com.everhomes.rest.yellowPage.GetSelfDefinedStateCommand;
import com.everhomes.rest.yellowPage.GetSelfDefinedStateResponse;
import com.everhomes.organization.OrganizationService;
import com.everhomes.portal.PortalVersion;
import com.everhomes.portal.PortalVersionProvider;

@Component
public class AllianceStandardServiceImpl implements AllianceStandardService { 

	private static final long ALLIANCE_MODULE_ID = 40500L;

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private YellowPageProvider yellowPageProvider;

	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private YellowPageService yellowPageService;
	
	@Autowired
	private AllianceTagProvider allianceTagProvider;
	
	@Autowired
	private AllianceConfigStateProvider allianceConfigStateProvider;
	
	@Autowired
	PortalVersionProvider portalVersionProvider;
	
	@Autowired
	ServiceModuleAppProvider serviceModuleAppProvider;
	
	@Autowired
	private ServiceCategoryMatchProvider serviceCategoryMatchProvider;
	

	@Override
	public void enableSelfDefinedConfig(GetSelfDefinedStateCommand cmd) {
		updateSelfDefinedConfig(cmd, true);
	}

	private void updateSelfDefinedConfig(GetSelfDefinedStateCommand cmd, boolean enable) {

		if (!isIdValid(cmd.getType())) {
			throwError(YellowPageServiceErrorCode.ERROR_ALLIANCE_TYPE_NOT_VALID, "alliance type is not valid");
		}

		AllianceConfigState state = allianceConfigStateProvider.findConfigState(cmd.getType(), cmd.getProjectId());;
		if (enable) {
			// 如果需要开启，而且当前是关闭状态，才进行创建
			if (isDisableSelfConfig(state)) {
				createSelfDefinedConfig(state, cmd.getType(), cmd.getProjectId(), cmd.getCurrentPMId());
			}
			return;
		}
		
		if (isEnableSelfConfig(state)) {
			// 关闭时删除所有该项目下的所有自定义配置
			deleteSelfDefinedConfig(state, cmd.getType(), cmd.getProjectId());
		}
	}

	private void deleteSelfDefinedConfig(AllianceConfigState state, Long type, Long projectId) {
		dbProvider.execute(r -> {
			
			// 删除主样式
			deleteProjectCategories(type,projectId);
		
			// 删除tag
			deleteProjectTags(type,projectId);
			
			// 更新配置状态
			updateAllianceConfigState(state, type, projectId, SelfDefinedState.DISABLE.getCode());
			
			return null;
		});

	}

	private void deleteProjectTags(Long type, Long projectId) {
		allianceTagProvider.deleteProjectTags(projectId, type);
	}

	private void deleteProjectCategories(Long type, Long projectId) {
		yellowPageProvider.deleteProjectCategories(projectId, type);
		serviceCategoryMatchProvider.deleteMathes(ServiceAllianceBelongType.COMMUNITY.getCode(), projectId, type);
	}

	// 创建自定义配置 包括主页样式，服务样式，筛选列表，表单，工作流
	private void createSelfDefinedConfig(AllianceConfigState state, Long type, Long projectId, Long organizationId) {
		dbProvider.execute(r -> {
			updateAllianceConfigState(state, type, projectId, SelfDefinedState.ENABLE.getCode());
			copyCategorysConfigToProject(type, projectId, organizationId);
			copyAllianceTagsToProject(type, projectId, organizationId);
			copyAllianceCategoryMathToProject(type, projectId, organizationId);
			return null;
		});
	}

	private void copyAllianceCategoryMathToProject(Long type, Long projectId, Long organizationId) {
		
		
		
	}

	private void updateAllianceConfigState(AllianceConfigState state,  Long type, Long projectId, byte status) {
		
		if (null == state) {
			state =  new AllianceConfigState();
			state.setNamespaceId(UserContext.getCurrentNamespaceId());
			state.setType(type);
			state.setProjectId(projectId);
			state.setStatus(status);
			allianceConfigStateProvider.createAllianceConfigState(state);
			return;
		}
		
		state.setStatus(status);
		allianceConfigStateProvider.updateAllianceConfigState(state);
		
	}

	private void copyAllianceTagsToProject(Long type, Long projectId, Long organizationId) {
		List<AllianceTagGroupDTO> dtos = getGeneralAllianceTags(type, organizationId);
		for (AllianceTagGroupDTO dto : dtos) {
			createAllianceTagToProject(type, projectId, dto);
		}
	}

	private List<AllianceTagGroupDTO> getGeneralAllianceTags(Long type, Long organizationId) {
		return yellowPageService.getAllianceTagList(null, null, UserContext.getCurrentNamespaceId(),
				ServiceAllianceBelongType.ORGANAIZATION.getCode(), organizationId, type);
	}

	private void createAllianceTagToProject(Long type, Long projectId, AllianceTagGroupDTO dto) {
		dto.getParentTag().setId(null);
		if (null != dto.getChildTags()) {
			for (AllianceTagDTO tagDto : dto.getChildTags()) {
				tagDto.setId(null);
			}
		}
		
		yellowPageService.updateAllianceTag(UserContext.getCurrentNamespaceId(),
				ServiceAllianceBelongType.COMMUNITY.getCode(), projectId, type, dto.getParentTag(), dto.getChildTags());
	}

	private void copyCategorysConfigToProject(Long type, Long projectId, Long organizationId) {
		
		Map<Long, ServiceAllianceCategories> oldAndNewCategoryMatch = new HashMap<>();

		// 获取主样式配置
		ServiceAllianceCategories currentMainCag = yellowPageProvider
				.findMainCategory(ServiceAllianceBelongType.ORGANAIZATION.getCode(), organizationId, type);
		if (null == currentMainCag) { 
			currentMainCag = getAllianceTypeBaseCategory(type);
		}
		
		ServiceAllianceCategories newMainCag = copyMainCategorysConfigToProject(currentMainCag, projectId); // 复制主样式
		oldAndNewCategoryMatch.put(currentMainCag.getId(), newMainCag);
		
		copyChildCategorysConfigToProject(currentMainCag.getId(), newMainCag.getId(), projectId, oldAndNewCategoryMatch); // 子样式复制
		
		//添加到关联表中
		createServiceCategoryMath(projectId, organizationId, type, oldAndNewCategoryMatch);
		
	}


	private void createServiceCategoryMath(Long projectId, Long organizationId, Long type, Map<Long, ServiceAllianceCategories> oldAndNewCaIdMatch) {
		
		if (oldAndNewCaIdMatch.isEmpty()) {
			return;
		}
		
		//找到所有
		List<ServiceCategoryMatch> mathes = serviceCategoryMatchProvider.listMatches(ServiceAllianceBelongType.ORGANAIZATION.getCode(), organizationId, type);
		if (CollectionUtils.isEmpty(mathes)) {
			return;
		}
		
		for (ServiceCategoryMatch match : mathes) {
			ServiceAllianceCategories newCa= oldAndNewCaIdMatch.get(match.getCategoryId());
			if (null != newCa) {
				ServiceCategoryMatch tmp = new ServiceCategoryMatch();
				tmp.setNamespaceId(UserContext.getCurrentNamespaceId());
				tmp.setOwnerType(ServiceAllianceBelongType.COMMUNITY.getCode());
				tmp.setOwnerId(projectId);
				tmp.setServiceId(match.getServiceId());
				tmp.setCategoryId(newCa.getId());
				tmp.setCategoryName(newCa.getName());
				tmp.setType(type);
				serviceCategoryMatchProvider.createMatch(tmp);
			}
		}
		
	}

	private void copyChildCategorysConfigToProject(Long oldParentId, Long newParentId, Long projectId, Map<Long, ServiceAllianceCategories> oldAndNewCaIdMatch) {
		List<ServiceAllianceCategories> childCags = getChildCategorys(oldParentId);
		for (ServiceAllianceCategories child : childCags) {
			Long oldCaId = child.getId();
			child.setOwnerType(ServiceAllianceBelongType.COMMUNITY.getCode());
			child.setOwnerId(projectId);
			child.setParentId(newParentId);
			yellowPageProvider.createCategory(child);
			oldAndNewCaIdMatch.put(oldCaId, child);
		}
	}

	private List<ServiceAllianceCategories> getChildCategorys(Long oldParentId) {
		return yellowPageProvider.listChildCategories(oldParentId);
	}

	private ServiceAllianceCategories copyMainCategorysConfigToProject(ServiceAllianceCategories oldMainCag,
			Long projectId) {
		// 样式创建
		ServiceAllianceCategories newMainCag = ConvertHelper.convert(oldMainCag, ServiceAllianceCategories.class);
		newMainCag.setId(null);
		newMainCag.setOwnerType(ServiceAllianceBelongType.COMMUNITY.getCode());
		newMainCag.setOwnerId(projectId);
		yellowPageProvider.createCategory(newMainCag);

		// 图片保存
		List<ServiceAllianceAttachment> attches = yellowPageProvider.listAttachments(oldMainCag.getId(),
				ServiceAllianceAttachmentType.COVER_ATTACHMENT.getCode());
		for (ServiceAllianceAttachment attch : attches) {
			attch.setOwnerId(newMainCag.getId());
			yellowPageProvider.createServiceAllianceAttachments(attch);
		}

		return newMainCag;
	}

	@Override
	public void disableSelfDefinedConfig(GetSelfDefinedStateCommand cmd) {
		updateSelfDefinedConfig(cmd, false);
	}

	@Override
	public GetSelfDefinedStateResponse getSelfDefinedState(GetSelfDefinedStateCommand cmd) {

		if (!ServiceAllianceBelongType.COMMUNITY.getCode().equals(cmd.getProjectType())) {
			throwError(YellowPageServiceErrorCode.ERROR_OWNER_TYPE_NOT_COMMUNITY, "owner type should be 'community'");
		}

		AllianceConfigState state = allianceConfigStateProvider.findConfigState(cmd.getType(), cmd.getProjectId());
		GetSelfDefinedStateResponse resp = new GetSelfDefinedStateResponse();
		resp.setIsOpen(state == null ? SelfDefinedState.DISABLE.getCode() : state.getStatus());
		return resp;
	}

	private boolean isIdValid(Long id) {
		if (null == id || id < 1) {
			return false;
		}

		return true;
	}

	/**
	 * 默认code为YellowPageServiceErrorCode.SCOPE
	 * 
	 * @param errorCode 错误码，见YellowPageServiceErrorCode
	 * @param errorMsg  错误信息。
	 */
	public static void throwError(int errorCode, String errorMsg) {
		throw RuntimeErrorException.errorWith(YellowPageServiceErrorCode.SCOPE, errorCode, errorMsg);
	}

	@Override
	public ServiceAllianceCategories queryHomePageCategoryByAdmin(String ownerType, Long ownerId, Long type) {
		return yellowPageProvider.findMainCategory(ownerType, ownerId, type);
	}
	
	@Override
	public ServiceAllianceCategories queryHomePageCategoryByScene(Long type, Long projectId) {

		// 查看当前项目下配置状态
		AllianceConfigState state = allianceConfigStateProvider.findConfigState(type, projectId);
		if (isEnableSelfConfig(state)) {
			return yellowPageProvider.findMainCategory(ServiceAllianceBelongType.COMMUNITY.getCode(), projectId, type);
		}

		// 根据园区查询，可以再查询通用配置
		Long orgId = getOrgIdByTypeAndProjectId(type, projectId);
		return null == orgId ? null
				: yellowPageProvider.findMainCategory(ServiceAllianceBelongType.ORGANAIZATION.getCode(), orgId, type);
	}
	
	@Override
	public ServiceAllianceCategories createHomePageCategory(String ownerType, Long ownerId, Long type) {
		if (ServiceAllianceBelongType.COMMUNITY.getCode().equals(ownerType)) {
			AllianceConfigState state = allianceConfigStateProvider.findConfigState(type, ownerId);
			if (isDisableSelfConfig(state)) {
				throwError(YellowPageServiceErrorCode.ERROR_SELF_CONFIG_NOT_ENABLE, "you should enbale self config first");
			}
		}
		
		// 根据type获取基础数据
		ServiceAllianceCategories baseCa = getAllianceTypeBaseCategory(type);
		if (null == baseCa) {
			throwError(YellowPageServiceErrorCode.ERROR_ALLIANCE_MAIN_CONFIG_NOT_EXIST, "service alliance main general config not exist");
		}
		
		ServiceAllianceCategories serviceAllianceCategories = ConvertHelper.convert(baseCa, ServiceAllianceCategories.class);
		serviceAllianceCategories.setOwnerType(ownerType);
		serviceAllianceCategories.setOwnerId(ownerId);
		yellowPageProvider.createCategory(serviceAllianceCategories);
		
		return serviceAllianceCategories;
	}
	
	@Override
	public Long getOrgIdByTypeAndProjectId(Long type, Long projectId) {

		// 根据type获取相应的appId
		ServiceModuleApp targetApp = getModuleAppByType(UserContext.getCurrentNamespaceId(), type);
		if (null == targetApp) {
			return null;
		}

		// 获取到管理公司
		GetAuthOrgByProjectIdAndAppIdCommand cmd = new GetAuthOrgByProjectIdAndAppIdCommand();
		cmd.setAppId(targetApp.getOriginId());
		cmd.setProjectId(projectId);
		OrganizationDTO orgDto = organizationService.getAuthOrgByProjectIdAndAppId(cmd);
		if (null == orgDto) {
			return null;
		}

		return null == orgDto ? null : orgDto.getId();
	}
	
	private ServiceModuleApp getModuleAppByType(Integer namespaceId, Long type) {
		PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(namespaceId);
		Long versionId = releaseVersion == null ? null : releaseVersion.getId();
		return serviceModuleAppProvider.findServiceModuleApp(namespaceId, versionId, ALLIANCE_MODULE_ID, "" + type);
	}
	
	@Override
	public List<ServiceAllianceCategories> listChildCategoriesByAdmin(CrossShardListingLocator locator, Integer pageSize,
			String ownerType, Long ownerId, Long organizationId, Long type) {
		boolean isQueryChild = true;
		Integer namespaceId = UserContext.getCurrentNamespaceId();

		// 获取配置
		List<ServiceAllianceCategories> cags = yellowPageProvider.listCategories(locator, pageSize, ownerType, ownerId,
				namespaceId, null, type, null, isQueryChild);
		if (!CollectionUtils.isEmpty(cags)) {
			return cags;
		}

		if (!ServiceAllianceBelongType.COMMUNITY.getCode().equals(ownerType)) {
			return null;
		}

		// 查看当前项目下配置状态
		AllianceConfigState state = allianceConfigStateProvider.findConfigState(type, ownerId);
		if (isEnableSelfConfig(state)) {
			return null;
		}

		organizationId = null == organizationId ? null : getOrgIdByTypeAndProjectId(type, ownerId);
		return yellowPageProvider.listCategories(locator, pageSize, ServiceAllianceBelongType.ORGANAIZATION.getCode(),
				organizationId, namespaceId, null, type, null, isQueryChild);
	}
	
	@Override
	public List<ServiceAllianceCategories> listChildCategoriesByScene(CrossShardListingLocator locator, Integer pageSize,
			String ownerType, Long ownerId, Long organizationId, Long type) {
		boolean isQueryChild = true;
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		
		if (!ServiceAllianceBelongType.COMMUNITY.getCode().equals(ownerType)) {
			throwError(YellowPageServiceErrorCode.ERROR_OWNER_TYPE_NOT_COMMUNITY, "ownerType must be community");
		}

		// 查看当前项目下配置状态
		AllianceConfigState state = allianceConfigStateProvider.findConfigState(type, ownerId);
		if (isEnableSelfConfig(state)) {
			return yellowPageProvider.listCategories(locator, pageSize, ownerType, organizationId, namespaceId, null,
					type, null, isQueryChild);
		}

		organizationId = null == organizationId ? null : getOrgIdByTypeAndProjectId(type, ownerId);
		return yellowPageProvider.listCategories(locator, pageSize, ServiceAllianceBelongType.ORGANAIZATION.getCode(),
				organizationId, namespaceId, null, type, null, isQueryChild);
	}
	
	@Override
	public boolean isDisableSelfConfig(AllianceConfigState state) {
		return null == state || SelfDefinedState.DISABLE.getCode() == state.getStatus();
	}
	
	@Override
	public boolean isEnableSelfConfig(AllianceConfigState state) {
		return null != state && SelfDefinedState.ENABLE.getCode() == state.getStatus();
	}

	private ServiceAllianceCategories getAllianceTypeBaseCategory(Long type) {
		return yellowPageProvider.findMainCategory(ServiceAllianceBelongType.ORGANAIZATION.getCode(), -1L, type);
	}

	@Override
	public void updateHomePageCategorysByPublish(ServiceAllianceInstanceConfig config, String name) {
		yellowPageProvider.updateMainCategorysByType(config.getType(), config.getEnableComment(), config.getEnableProvider(), name);
	}

	@Override
	public ServiceCategoryMatch findServiceCategoryMatch(String ownerType, Long ownerId, Long type, Long serviceId) {
		return serviceCategoryMatchProvider.findMatch(ownerType, ownerId, type, serviceId);
	}
	
	@Override
	public void updateMatchCategoryName(Long type, Long categoryId, String categoryName) {
		serviceCategoryMatchProvider.updateMatchCategoryName(type, categoryId, categoryName);
	}
	
}
