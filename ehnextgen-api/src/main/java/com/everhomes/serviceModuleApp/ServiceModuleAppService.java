// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ServiceModuleAppDTO;

import java.util.List;

public interface ServiceModuleAppService {
	List<ServiceModuleApp> listReleaseServiceModuleApps(Integer namespaceId);

	List<ServiceModuleApp> listServiceModuleApps(Integer namespaceId, Long versionId, Long moduleId, Byte actionType, String customTag, String customPath);

	List<ServiceModuleApp> listReleaseServiceModuleAppsByOriginIds(Integer namespaceId, List<Long> originIds);

	List<ServiceModuleApp> listServiceModuleAppByModuleIds(Integer namespaceId, List<Long> moduleIds);

	List<ServiceModuleApp> listServiceModuleApp(Integer namespaceId, Long versionId, Long moduleId, Byte actionType, String customTag, String controlType);

	List<ServiceModuleApp> listReleaseServiceModuleApp(Integer namespaceId, Long moduleId, Byte actionType, String customTag, String controlType);
}