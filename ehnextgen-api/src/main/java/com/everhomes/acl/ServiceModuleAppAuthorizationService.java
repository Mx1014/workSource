package com.everhomes.acl;

import com.everhomes.rest.acl.DistributeServiceModuleAppAuthorizationCommand;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.acl.UpdateAppProfileCommand;

import java.util.List;

public interface ServiceModuleAppAuthorizationService {
    boolean checkCommunityRelationOfOrgId(Integer namespaceId, Long currentOrgId, Long checkCommunityId);
    List<ProjectDTO> listCommunityRelationOfOrgId(Integer namespaceId, Long organizationId);
    List<ServiceModuleAppAuthorization> listCommunityRelationOfOwnerIdAndAppId(Integer namespaceId, Long ownerId, Long appId);
    List<ServiceModuleAppAuthorization> listCommunityRelationOfOrgIdAndAppId(Integer namespaceId, Long organizationId, Long appId);

    List<ServiceModuleAppAuthorization> listCommunityRelations(Integer namespaceId, Long organizationId, Long communityId);

    List<Long> listCommunityAppIdOfOrgId(Integer namespaceId, Long organizationId);
    void distributeServiceModuleAppAuthorization(DistributeServiceModuleAppAuthorizationCommand cmd);

    void updateAppProfile(UpdateAppProfileCommand cmd);

    void deleteServiceModuleAppAuthorizationByOrganizationId(Long organizationId);
}
