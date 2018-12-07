// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.rest.launchpad.ListAllAppsResponse;
import com.everhomes.rest.launchpad.ListWorkPlatformAppCommand;
import com.everhomes.rest.launchpad.ListWorkPlatformAppResponse;
import com.everhomes.rest.launchpad.SaveWorkPlatformAppCommand;
import com.everhomes.rest.launchpad.UpdateWorkPlatformAppCommand;
import com.everhomes.rest.launchpad.UpdateWorkPlatformAppSortCommand;
import com.everhomes.rest.launchpadbase.*;
import com.everhomes.rest.module.RouterInfo;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.servicemoduleapp.*;

import java.util.List;

public interface ServiceModuleAppService {
	List<ServiceModuleApp> listReleaseServiceModuleApps(Integer namespaceId);

	List<ServiceModuleApp> listServiceModuleApps(Integer namespaceId, Long versionId, Long moduleId, Byte actionType, String customTag, String customPath);

	List<ServiceModuleApp> listReleaseServiceModuleAppsByOriginIds(Integer namespaceId, List<Long> originIds);

	List<ServiceModuleApp> listReleaseServiceModuleAppByModuleIds(Integer namespaceId, List<Long> moduleIds);

	List<ServiceModuleApp> listServiceModuleApp(Integer namespaceId, Long versionId, Long moduleId, Byte actionType, String customTag, String controlType);

	List<ServiceModuleApp> listReleaseServiceModuleApp(Integer namespaceId, Long moduleId, Byte actionType, String customTag, String controlType);

	List<Long> listReleaseServiceModuleIdsByNamespace(Integer namespaceId);

	List<Long> listReleaseServiceModuleIdsWithParentByNamespace(Integer namespaceId);

	ServiceModuleApp findReleaseServiceModuleAppByOriginId(Long originId);

	ListServiceModuleAppsForBannerResponse listServiceModuleAppsForBanner(ListServiceModuleAppsForBannerCommand cmd);

	ServiceModuleAppDTO installApp(InstallAppCommand cmd);

	void uninstallApp(UninstallAppCommand cmd);

    void uninstallAppByOrganizationId(Long organizationId);

    ListServiceModuleAppsByOrganizationIdResponse listServiceModuleAppsByOrganizationId(ListServiceModuleAppsByOrganizationIdCommand cmd);

    ListLaunchPadAppsResponse listLaunchPadApps(ListLaunchPadAppsCommand cmd);

    RouterInfo convertRouterInfo(Long moduleId, Long appId, String title, String actionData, String path, Byte locationType, Byte sceneType, Byte clientHandlerType);

    void updateStatus(UpdateStatusCommand cmd);

	void changeCommunityConfigFlag(ChangeCommunityConfigFlagCommand cmd);

	ListAppCommunityConfigsResponse listAppCommunityConfigs(ListAppCommunityConfigsCommand cmd);

	void updateAppCommunityConfig(UpdateAppCommunityConfigCommand cmd);

	void installDefaultAppByOrganizationId(Long organizationId);

	void installAppForAllOrganizations(InstallAppForAllOrganizationsCommand cmd);

    ListAllLaunchPadAppsResponse listAllLaunchPadApps(ListAllLaunchPadAppsCommand cmd);

	void updateBaseUserApps(UpdateUserAppsCommand cmd);

	void updateBaseUserAppsForWorkPlatform(UpdateUserAppsForWorkPlatformCommand cmd);

	void updateRecommendApps(UpdateRecommendAppsCommand cmd);

    ListAllAppsResponse listAllApps(ListAllLaunchPadAppsCommand cmd);

    ListAllAppsResponse listAllAppsForWorkPlatform(ListAllLaunchPadAppsCommand cmd);

    ListServiceModuleAppsForEnterprisePayResponse listServiceModuleAppsForEnterprisePay(ListServiceModuleAppsForEnterprisePayCommand cmd);

	ListWorkPlatformAppResponse listWorkPlatformApp(ListWorkPlatformAppCommand cmd);

	void saveWorkPlatformApp(SaveWorkPlatformAppCommand cmd);

	void updateWorkPlatformApp(UpdateWorkPlatformAppCommand cmd);

	void updateWorkPlatformAppSort(UpdateWorkPlatformAppSortCommand cmd);

	List<AppDTO> toAppDtos(Long communityId, Long orgId, Byte sceneType, List<ServiceModuleApp> userCommunityApps);
}