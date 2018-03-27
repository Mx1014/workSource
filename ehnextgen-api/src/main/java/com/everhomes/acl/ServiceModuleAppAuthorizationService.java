package com.everhomes.acl;

import com.everhomes.rest.acl.ProjectDTO;

import java.util.List;

public interface ServiceModuleAppAuthorizationService {
    boolean checkCommunityRelationOfOrgId(Integer namespaceId, Long currentOrgId, Long checkCommunityId);
    List<ProjectDTO> listCommunityRelationOfOrgId(Integer namespaceId, Long organizationId);
}
