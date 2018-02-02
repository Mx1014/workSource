// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.rest.portal.ServiceModuleAppDTO;

import java.util.List;

public interface ServiceModuleAppProvider {

	void createServiceModuleApp(ServiceModuleApp serviceModuleApp);

	void updateServiceModuleApp(ServiceModuleApp serviceModuleApp);

	ServiceModuleApp findServiceModuleAppById(Long id);

	List<ServiceModuleApp> listServiceModuleApp(Integer namespaceId, Long versionId, Long moduleId);

	List<ServiceModuleApp> listServiceModuleAppByActionType(Integer namespaceId, Long versionId, Byte actionType);

	List<ServiceModuleAppDTO> listServiceModuleAppsByModuleIds(Integer namespaceId, List<Long> moduleIds);

	void createServiceModuleApps(List<ServiceModuleApp> serviceModuleApps);

	List<ServiceModuleApp> listServiceModuleAppsByVersionIdAndOriginIds(Long versionId, List<Long> originIds);

	void deleteByVersionId(Long versionId);

    List<ServiceModuleApp> listServiceModuleApp(Integer namespaceId, Long versionId, Long moduleId, Byte actionType, String customTag, String customPath);

    ServiceModuleApp findServiceModuleApp(Integer namespaceId, Long versionId, Long moduleId, String customTag);

	ServiceModuleApp findServiceModuleApp(Integer namespaceId, Long versionId, Byte actionType, String instanceConfig);
}