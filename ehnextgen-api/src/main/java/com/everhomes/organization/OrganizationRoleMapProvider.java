// @formatter:off
package com.everhomes.organization;

import java.util.List;

import com.everhomes.entity.EntityType;
import com.everhomes.rest.organization.PrivateFlag;


public interface OrganizationRoleMapProvider {
	
	List<OrganizationRoleMap> listOrganizationRoleMaps(Long ownerId, EntityType ownerType, PrivateFlag privateFlag);
	
	List<OrganizationRoleMap> listOrganizationRoleMapsByOwnerIds(List<Long> ownerIds, EntityType ownerType, PrivateFlag privateFlag);
	
	void createOrganizationRoleMap(OrganizationRoleMap organizationRoleMap);
	
	void updateOrganizationRoleMap(OrganizationRoleMap organizationRoleMap);
	
	OrganizationRoleMap getOrganizationRoleMap(Long roleId);
	
}
