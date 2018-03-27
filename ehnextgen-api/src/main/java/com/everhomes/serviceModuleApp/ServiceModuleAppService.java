// @formatter:off
package com.everhomes.serviceModuleApp;

import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.acl.DistributeServiceModuleAppAuthorizationCommand;
import com.everhomes.rest.module.ListProjectIdsByAppIdAndOrganizationIdCommand;
import com.everhomes.rest.portal.ServiceModuleAppDTO;

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

	void distributeServiceModuleAppAuthorization(DistributeServiceModuleAppAuthorizationCommand cmd);

	List<Long> listProjectIdsByAppIdAndOrganizationId(ListProjectIdsByAppIdAndOrganizationIdCommand cmd);

	List<ServiceModuleAppDTO> listAppIdOfOrgId(ListProjectIdsByAppIdAndOrganizationIdCommand cmd);

	List<ProjectDTO> listCommunityRelationOfOrgId(ListProjectIdsByAppIdAndOrganizationIdCommand cmd);

	boolean checkCommunityRelationOfOrgId(Integer namespaceId, Long currentOrgId, Long checkCommunityId);

}