// @formatter:off
package com.everhomes.serviceModuleApp;

import java.util.List;


public interface ServiceModuleAppProvider {

	void createServiceModuleApp(ServiceModuleApp serviceModuleApp);

	void updateServiceModuleApp(ServiceModuleApp serviceModuleApp);

	ServiceModuleApp findServiceModuleAppById(Long id);

	List<ServiceModuleApp> listServiceModuleApp(Integer namespaceId, Long versionId, Long moduleId);

	List<ServiceModuleApp> listServiceModuleAppByActionType(Integer namespaceId, Long versionId, Byte actionType);

	void createServiceModuleApps(List<ServiceModuleApp> serviceModuleApps);

	List<ServiceModuleApp> listServiceModuleAppsByModuleIds(Integer namespaceId, Long versionId, List<Long> moduleIds);

	List<ServiceModuleApp> listServiceModuleAppsByOriginIds(Long versionId, List<Long> originIds);

	void deleteByVersionId(Long versionId);

    List<ServiceModuleApp> listServiceModuleApp(Integer namespaceId, Long versionId, Long moduleId, Byte actionType, String customTag, String controlType);

    List<ServiceModuleApp> listServiceModuleAppByModuleIds(Integer namespaceId, Long versionId, List<Long> moduleIds);

    ServiceModuleApp findServiceModuleApp(Integer namespaceId, Long versionId, Long moduleId, String customTag);

	ServiceModuleApp findServiceModuleApp(Integer namespaceId, Long versionId, Byte actionType, String instanceConfig);

	List<ServiceModuleApp> listServiceModuleAppByOriginId(Long originId);

    List<ServiceModuleApp> listServiceModuleAppsByAppTypeAndKeyword(Long versionId, Byte appType, String keyword);

    List<ServiceModuleApp> listServiceModuleAppsByOrganizationId(Long versionId, Byte appType, String keyword, Long organizationId, Byte installFlag, Byte needSystemAppFlag, Long pageAnchor, int pageSize);

    List<ServiceModuleApp> listServiceModuleApp(Integer namespaceId, Long versionId, Long moduleId, String keywords, List<Long> developerIds, Byte appType, Byte mobileFlag, Byte pcFlag, Byte independentConfigFlag, Byte supportThirdFlag);

	List<ServiceModuleApp> listManageServiceModuleApps(Integer namespaceId, Long versionId, Long orgId, Byte locationType, Byte appType);

	List<ServiceModuleApp> listInstallServiceModuleApps(Integer namespaceId, Long versionId, Long orgId, Byte locationType, Byte appType, Byte sceneType, Byte organizationAppStatus, Long appCategoryId);

	List<ServiceModuleApp> listInstallServiceModuleAppsWithEntries(Integer namespaceId, Long versionId, Long orgId, Byte locationType, Byte appType, Byte sceneType, Byte organizationAppStatus, Long appCategoryId);

	List<ServiceModuleApp> listInstallServiceModuleApps(Integer namespaceId, Long versionId, Byte locationType, Byte appType, Byte sceneType, Byte organizationAppStatus, Long appCategoryId);

	List<ServiceModuleApp> listSystemApps(Long versionId);

	List<ServiceModuleApp> listDefaultApps(Long versionId);

	List<ServiceModuleApp> listServiceModuleAppsForEnterprisePay(Long versionId, Byte enableEnterprisePayFlag);

	void createServiceModuleAppEntryProfile(ServiceModuleAppEntryProfile serviceModuleAppEntryProfile);

	void updateServiceModuleAppEntryProfile(ServiceModuleAppEntryProfile serviceModuleAppEntryProfile);

	List<ServiceModuleAppEntryProfile> listServiceModuleAppEntryProfile(Long originId, Long entryId, Byte entryCategory, Byte entrySettingFlag);
}