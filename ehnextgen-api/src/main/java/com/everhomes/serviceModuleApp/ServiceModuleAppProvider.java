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
}