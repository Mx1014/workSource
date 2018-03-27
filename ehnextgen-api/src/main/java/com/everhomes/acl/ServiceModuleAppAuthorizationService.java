package com.everhomes.acl;

import com.everhomes.rest.acl.DistributeServiceModuleAppAuthorizationCommand;
import com.everhomes.rest.acl.ProjectDTO;

import java.util.List;

public interface ServiceModuleAppAuthorizationService {
    boolean checkCommunityRelationOfOrgId(Integer namespaceId, Long currentOrgId, Long checkCommunityId);
    List<ProjectDTO> listCommunityRelationOfOrgId(Integer namespaceId, Long organizationId);
    List<ServiceModuleAppAuthorization> listCommunityRelationOfOrgIdAndAppId(Integer namespaceId, Long organizationId, Long appId, Long projectId);
    List<Long> listCommunityAppIdOfOrgId(Integer namespaceId, Long organizationId);
    void distributeServiceModuleAppAuthorization(DistributeServiceModuleAppAuthorizationCommand cmd);
}
