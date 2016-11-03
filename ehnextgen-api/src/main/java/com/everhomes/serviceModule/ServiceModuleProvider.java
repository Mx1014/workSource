// @formatter:off
package com.everhomes.serviceModule;

import org.jooq.Condition;

import java.util.List;


public interface ServiceModuleProvider {
	
	List<ServiceModulePrivilege> listServiceModulePrivileges(Long moduleId, ServiceModulePrivilegeType privilegeType);

	Long createServiceModuleAssignment(ServiceModuleAssignment serviceModuleAssignment);

	List<ServiceModuleAssignment> listServiceModuleAssignments(Condition condition, Long organizationId);

	void deleteServiceModuleAssignmentById(Long id);

	ServiceModule findServiceModuleById(Long id);

	List<ServiceModuleAssignment> listResourceAssignments(String targetType, Long targetId, Long organizationId);

	List<ServiceModuleAssignment> listServiceModuleAssignmentsByTargetId(String targetType, Long targetId, Long organizationId);
}
