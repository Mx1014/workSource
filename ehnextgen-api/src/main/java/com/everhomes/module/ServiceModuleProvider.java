// @formatter:off
package com.everhomes.module;

import org.jooq.Condition;

import java.util.List;


public interface ServiceModuleProvider {
	
	List<ServiceModulePrivilege> listServiceModulePrivileges(Long moduleId, ServiceModulePrivilegeType privilegeType);

	List<ServiceModulePrivilege> listServiceModulePrivilegesByPrivilegeId(Long privilegeId, ServiceModulePrivilegeType privilegeType);

	Long createServiceModuleAssignment(ServiceModuleAssignment serviceModuleAssignment);

	List<ServiceModuleAssignment> listServiceModuleAssignments(Condition condition, Long organizationId);

	void deleteServiceModuleAssignmentById(Long id);

	ServiceModule findServiceModuleById(Long id);

	List<ServiceModuleAssignment> listResourceAssignments(String targetType, Long targetId, Long organizationId, List<Long> moduleIds);

	List<ServiceModuleAssignment> listServiceModuleAssignmentsByTargetId(String targetType, Long targetId, Long organizationId);

	List<ServiceModuleAssignment> listServiceModuleAssignmentsByTargetIdAndOwnerId(String ownerType, Long ownerId, String targetType, Long targetId, Long organizationId);


	List<ServiceModule> listServiceModule(Integer level, Byte type);
	
	List<ServiceModuleScope> listServiceModuleScopes(Integer namespaceId, String ownerType, Long ownerId, Byte applyPolicy);

	List<ServiceModulePrivilege> listServiceModulePrivileges(List<Long> moduleIds, ServiceModulePrivilegeType privilegeType);

	List<ServiceModuleAssignment> listResourceAssignments(String targetType, List<Long> targetIds, Long organizationId, List<Long> moduleIds);

	List<ServiceModuleAssignment> listServiceModuleAssignmentByModuleId(String ownerType, Long ownerId, Long organizationId, Long moduleId);
}
