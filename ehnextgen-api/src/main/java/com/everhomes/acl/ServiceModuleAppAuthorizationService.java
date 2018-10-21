package com.everhomes.acl;

import com.everhomes.rest.acl.*;
import com.everhomes.rest.portal.ServiceModuleAppDTO;

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

    ListAppAuthorizationsByOwnerIdResponse listAppAuthorizationsByOwnerId(ListAppAuthorizationsByOwnerIdCommand cmd);

    ListAppAuthorizationsByOwnerIdResponse listAppAuthorizationsByOrganizatioinId(ListAppAuthorizationsByOrganizatioinIdCommand cmd);

    void addAllCommunityAppAuthorizations(Integer namespaceId, Long ownerId, Long appId);

    void updateAllAuthToNewOrganization(Integer namespaceId, Long organizationId, Long communityId);

    void removeAllCommunityAppAuthorizations(Long ownerId, Long appId);

    ServiceModuleAppDTO getAppProfile(GetAppProfileCommand cmd);

    ServiceModuleAppAuthorization findServiceModuleAppAuthorization(Long projectId, Long appId);
}
