// @formatter:off
package com.everhomes.portal;

import com.everhomes.rest.portal.ServiceModuleAppDTO;

import java.util.List;

public interface ServiceModuleAppProvider {

	void createServiceModuleApp(ServiceModuleApp serviceModuleApp);

	void updateServiceModuleApp(ServiceModuleApp serviceModuleApp);

	ServiceModuleApp findServiceModuleAppById(Long id);

	List<ServiceModuleApp> listServiceModuleApp(Integer namespaceId, Long moduleId, Long versionId);

	List<ServiceModuleApp> listServiceModuleAppByActionType(Integer namespaceId, Byte actionType, Long versionId);

	List<ServiceModuleAppDTO> listServiceModuleAppsByModuleIds(Integer namespaceId, List<Long> moduleIds);

	void createServiceModuleApps(List<ServiceModuleApp> serviceModuleApps);

    void deleteByVersionId(Long versionId);

    List<ServiceModuleApp> listServiceModuleApp(Integer namespaceId, Long moduleId, Byte actionType, String customTag, String customPath, Long versionId);

    ServiceModuleApp findServiceModuleApp(Integer namespaceId, Long versionId, Long moduleId, String customTag);

	ServiceModuleApp findServiceModuleApp(Integer namespaceId, Long versionId, Byte actionType, String instanceConfig);
}