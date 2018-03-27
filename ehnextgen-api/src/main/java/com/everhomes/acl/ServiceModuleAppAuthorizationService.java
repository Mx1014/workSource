package com.everhomes.acl;

import com.everhomes.rest.acl.DistributeServiceModuleAppAuthorizationCommand;
import com.everhomes.rest.acl.ProjectDTO;

import java.util.List;

public interface ServiceModuleAppAuthorizationService {
    boolean checkCommunityRelationOfOrgId(Integer namespaceId, Long currentOrgId, Long checkCommunityId);
    List<ProjectDTO> listCommunityRelationOfOrgId(Integer namespaceId, Long organizationId);
    List<ServiceModuleAppAuthorization> listCommunityRelationOfOwnerIdAndAppId(Integer namespaceId, Long organizationId, Long appId);
    List<Long> listCommunityAppIdOfOrgId(Integer namespaceId, Long organizationId);
    void distributeServiceModuleAppAuthorization(DistributeServiceModuleAppAuthorizationCommand cmd);
}
