package com.everhomes.yellowPage.standard;

import java.util.List;

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
import com.everhomes.rest.yellowPage.GetFormListCommand;
import com.everhomes.rest.yellowPage.GetFormListResponse;
import com.everhomes.rest.yellowPage.GetWorkFlowListCommand;
import com.everhomes.rest.yellowPage.GetWorkFlowListResponse;
import com.everhomes.rest.yellowPage.ServiceAllianceAttachmentDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceAttachmentType;
import com.everhomes.rest.yellowPage.ServiceAllianceBelongType;
import com.everhomes.rest.yellowPage.ServiceAllianceCategoryDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceDTO;
import com.everhomes.rest.yellowPage.YellowPageServiceErrorCode;
import com.everhomes.rest.yellowPage.standard.SelfDefinedState;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
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

@Component
public class AllianceStandardServiceImpl implements AllianceStandardService {

	private static final long ALLIANCE_MODULE_ID = 40500L;

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private YellowPageProvider yellowPageProvider;

	@Autowired
	private ServiceModuleService serviceModuleService;

	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private YellowPageService yellowPageService;
	
	@Autowired
	private AllianceTagProvider allianceTagProvider;
	
	
	@Override
	public GetFormListResponse getFormList(GetFormListCommand cmd) {
		return null;
	}

	@Override
	public GetWorkFlowListResponse getWorkFlowList(GetWorkFlowListCommand cmd) {
		return null;
	}

	@Override
	public void enableSelfDefinedConfig(GetSelfDefinedStateCommand cmd) {
		updateSelfDefinedConfig(cmd, true);
	}

	private void updateSelfDefinedConfig(GetSelfDefinedStateCommand cmd, boolean enable) {

		if (!isIdValid(cmd.getType())) {
			throwError(YellowPageServiceErrorCode.ERROR_ALLIANCE_TYPE_NOT_VALID, "alliance type is not valid");
		}

		SelfDefinedState state = getSelfDefinedState(cmd.getType(), cmd.getProjectId());
		if (enable) {
			// 如果需要开启，而且当前是关闭状态，才进行创建
			if (SelfDefinedState.DISABLE == state) {
				createSelfDefinedConfig(cmd.getType(), cmd.getProjectId(), cmd.getCurrentPMId());
			}
			return;
		}
		
		if (SelfDefinedState.ENABLE == state) {
			// 关闭时删除所有该项目下的所有自定义配置
			deleteSelfDefinedConfig(cmd.getType(), cmd.getProjectId());
		}
	}

	private void deleteSelfDefinedConfig(Long type, Long projectId) {
		dbProvider.execute(r -> {
			// 删除主配置
			deleteProjectMainConfig(type, projectId);
			
			// 删除主样式
			deleteProjectCategories(type,projectId);
		
			// 删除tag
			deleteProjectTags(type,projectId);
			
			return null;
		});

	}

	private void deleteProjectTags(Long type, Long projectId) {
		allianceTagProvider.deleteProjectTags(projectId, type);
	}

	private void deleteProjectCategories(Long type, Long projectId) {
		yellowPageProvider.deleteProjectCategories(projectId, type);
	}

	private void deleteProjectMainConfig(Long type, Long projectId) {
		yellowPageProvider.deleteProjectMainConfig(projectId, type);
		
		//删除图片 attachements ，可以不删
		
	}

	// 创建自定义配置 包括主页样式，服务样式，筛选列表，表单，工作流
	private void createSelfDefinedConfig(Long type, Long projectId, Long organizationId) {
		dbProvider.execute(r -> {
			copyMainConfigToProject(type, projectId, organizationId);
			copyCategorysConfigToProject(type, projectId, organizationId);
			copyAllianceTagsToProject(type, projectId, organizationId);
			return null;
		});
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

		// 获取主样式配置
		ServiceAllianceCategories currentMainCag = getMainCategorys(type, organizationId);
		if (null == currentMainCag) {
			throwError(YellowPageServiceErrorCode.ERROR_ALLIANCE_MAIN_CATEGORY_NOT_EXIST,
					"service alliance main category not exist");
		}

		ServiceAllianceCategories newMainCag = copyMainCategorysConfigToProject(currentMainCag, projectId); // 复制主样式
		copyChildCategorysConfigToProject(currentMainCag.getId(), newMainCag.getId(), projectId); // 子样式复制
	}


	private void copyChildCategorysConfigToProject(Long oldParentId, Long newParentId, Long projectId) {
		List<ServiceAllianceCategories> childCags = getChildCategorys(oldParentId);
		for (ServiceAllianceCategories child : childCags) {
			child.setOwnerType(ServiceAllianceBelongType.COMMUNITY.getCode());
			child.setOwnerId(projectId);
			child.setParentId(newParentId);
			yellowPageProvider.createCategory(child);
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

	private ServiceAllianceCategories getMainCategorys(Long type, Long organizationId) {
		ServiceAllianceCategories sc = yellowPageProvider.findMainCategory(ServiceAllianceBelongType.ORGANAIZATION.getCode(), organizationId, type);
		if (null != sc) {
			return sc;
		}
		
		return yellowPageProvider.findMainCategory(ServiceAllianceBelongType.ORGANAIZATION.getCode(), null, type);
	}

	private ServiceAlliances getGeneralMainConfig(Long type, Long organizationId) {
		ServiceAlliances sa = yellowPageProvider.queryServiceAllianceTopic(ServiceAllianceBelongType.ORGANAIZATION.getCode(),
				organizationId, type);
		if (null != sa) {
			return sa;
		}
		
		return yellowPageProvider.queryServiceAllianceTopic(ServiceAllianceBelongType.ORGANAIZATION.getCode(),
				null, type);
	}

	private void copyMainConfigToProject(Long type, Long projectId, Long organizationId) {
		ServiceAlliances mainSa = getGeneralMainConfig(type, organizationId);
		if (null == mainSa) {
			throwError(YellowPageServiceErrorCode.ERROR_ALLIANCE_MAIN_CONFIG_NOT_EXIST,
					"service alliance main general config not exist");
		}

		//创建主配置
		mainSa.setId(null);
		mainSa.setOwnerType(ServiceAllianceBelongType.COMMUNITY.getCode());
		mainSa.setOwnerId(projectId);
		yellowPageProvider.createServiceAlliances(mainSa);
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

		SelfDefinedState state = getSelfDefinedState(cmd.getType(), cmd.getProjectId());
		GetSelfDefinedStateResponse resp = new GetSelfDefinedStateResponse();
		resp.setIsOpen(state.getCode());
		return resp;
	}

	private SelfDefinedState getSelfDefinedState(Long type, Long projectId) {
		ServiceAlliances sa = yellowPageProvider
				.queryServiceAllianceTopic(ServiceAllianceBelongType.COMMUNITY.getCode(), projectId, type);

		return null == sa ? SelfDefinedState.DISABLE : SelfDefinedState.ENABLE;
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
	public ServiceAlliances queryServiceAllianceTopic(String ownerType, Long ownerId, Long type) {

		if (!ServiceAllianceBelongType.COMMUNITY.getCode().equals(ownerType)) {
			return getGeneralMainConfig(type, ownerId);
		}

		ServiceAlliances sa = yellowPageProvider.queryServiceAllianceTopic(ownerType, ownerId, type);
		if (null != sa) {
			return sa;
		}

		// 根据园区查询，可以再查询通用配置
		Long orgId = getOrgIdByTypeAndProjectId(type, ownerId);
		return null == orgId ? null : getGeneralMainConfig(type, orgId);
	}
	
	@Override
	public ServiceAllianceCategories queryServiceAllianceCategoryTopic(String ownerType, Long ownerId, Long type) {

		if (!ServiceAllianceBelongType.COMMUNITY.getCode().equals(ownerType)) {
			return getMainCategorys(type, ownerId);
		}
		
		ServiceAllianceCategories sc = yellowPageProvider.findMainCategory(ownerType, ownerId, type);
		if (null != sc) {
			return sc;
		}

		// 根据园区查询，可以再查询通用配置
		Long orgId = getOrgIdByTypeAndProjectId(type, ownerId);
		return null == orgId ? null : getMainCategorys(type, orgId);
	}

	private Long getOrgIdByTypeAndProjectId(Long type, Long projectId) {

		// 根据type获取相应的appId
		List<ServiceModuleAppDTO> dtos = serviceModuleService.getModuleApps(UserContext.getCurrentNamespaceId(),
				ALLIANCE_MODULE_ID);
		if (CollectionUtils.isEmpty(dtos)) {
			return null;
		}

		ServiceModuleAppDTO targetAppDto = null;
		for (ServiceModuleAppDTO dto : dtos) {
			if (StringUtils.isEmpty(dto.getInstanceConfig())) {
				continue;
			}

			ServiceAllianceInstanceConfig config = (ServiceAllianceInstanceConfig) StringHelper
					.fromJsonString(dto.getInstanceConfig(), ServiceAllianceInstanceConfig.class);
			if (type.equals(config.getType())) {
				targetAppDto = dto;
				break;
			}
		}

		if (null == targetAppDto) {
			return null;
		}

		// 获取到管理公司
		GetAuthOrgByProjectIdAndAppIdCommand cmd = new GetAuthOrgByProjectIdAndAppIdCommand();
		cmd.setAppId(targetAppDto.getOriginId());
		cmd.setProjectId(projectId);
		OrganizationDTO orgDto = organizationService.getAuthOrgByProjectIdAndAppId(cmd);
		if (null == orgDto) {
			return null;
		}

		return null == orgDto ? null : orgDto.getId();
	}
	
	@Override
	public List<ServiceAllianceCategories> listChildCategories(CrossShardListingLocator locator, Integer pageSize,
			String ownerType, Long ownerId, Long organizationId, Long type) {
		boolean isQueryChild = true;
		Integer namespaceId = UserContext.getCurrentNamespaceId();

		// 获取配置
		List<ServiceAllianceCategories> cags = yellowPageProvider.listCategories(locator, pageSize, ownerType, ownerId,
				namespaceId, null, type, CategoryAdminStatus.ACTIVE, null, isQueryChild);
		if (!CollectionUtils.isEmpty(cags)) {
			return cags;
		}

		if (ServiceAllianceBelongType.COMMUNITY.getCode().equals(ownerType)) {
			
			if (null == organizationId) {
				organizationId = getOrgIdByTypeAndProjectId(type, ownerId);
			}
			
			return yellowPageProvider.listCategories(locator, pageSize,
					ServiceAllianceBelongType.ORGANAIZATION.getCode(), organizationId, namespaceId, null, type,
					CategoryAdminStatus.ACTIVE, null, isQueryChild);
		}

		//如果都没获取到返回空
		return null;
	}

}
