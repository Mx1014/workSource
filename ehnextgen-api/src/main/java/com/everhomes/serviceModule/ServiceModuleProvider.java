// @formatter:off
package com.everhomes.serviceModule;

import com.everhomes.acl.WebMenu;
import com.everhomes.acl.WebMenuPrivilege;
import com.everhomes.acl.WebMenuScope;
import com.everhomes.rest.acl.WebMenuPrivilegeShowFlag;
import org.jooq.Condition;

import java.util.List;


public interface ServiceModuleProvider {
	
	List<ServiceModulePrivilege> listServiceModulePrivileges(Long moduleId, ServiceModulePrivilegeType privilegeType);

	Long createServiceModuleAssignment(ServiceModuleAssignment serviceModuleAssignment);

	List<ServiceModuleAssignment> listServiceModuleAssignments(Condition condition, Long organizationId);

	void deleteServiceModuleAssignmentById(Long id);
}
