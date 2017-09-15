// @formatter:off
package com.everhomes.module;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import org.jooq.Condition;

public interface ServiceModuleProvider {

    List<ServiceModulePrivilege> listServiceModulePrivileges(Long moduleId, ServiceModulePrivilegeType privilegeType);

    List<ServiceModulePrivilege> listServiceModulePrivilegesByPrivilegeId(Long privilegeId, ServiceModulePrivilegeType privilegeType);

    Long createServiceModuleAssignment(ServiceModuleAssignment serviceModuleAssignment);

    List<ServiceModuleAssignment> listServiceModuleAssignments(Condition condition);

    void deleteServiceModuleAssignmentById(Long id);

    ServiceModule findServiceModuleById(Long id);

    List<ServiceModuleAssignment> listResourceAssignments(String targetType, Long targetId, Long organizationId, List<Long> moduleIds);

    List<ServiceModuleAssignment> listServiceModuleAssignmentsByTargetId(String targetType, Long targetId, Long organizationId);

    List<ServiceModuleAssignment> listServiceModuleAssignmentsByTargetIdAndOwnerId(String ownerType, Long ownerId, String targetType, Long targetId, Long organizationId);

    List<ServiceModule> listServiceModule(Integer level, Byte type);

    List<ServiceModuleScope> listServiceModuleScopes(Integer namespaceId, String ownerType, Long ownerId, Byte applyPolicy);

    List<ServiceModulePrivilege> listServiceModulePrivileges(List<Long> moduleIds, ServiceModulePrivilegeType privilegeType);

    List<ServiceModuleAssignment> listResourceAssignments(String targetType, List<Long> targetIds, Long organizationId, List<Long> moduleIds);

    List<ServiceModuleAssignment> listServiceModuleAssignmentByModuleId(String ownerType, Long ownerId, Long moduleId);

    List<ServiceModuleAssignment> listResourceAssignmentGroupByTargets(String ownerType, Long ownerId, Long organizationId);

    List<ServiceModule> listServiceModule(Integer startLevel, List<Byte> types);
    List<ServiceModule> listServiceModule(List<Long> ids);

    Long createModuleAssignmentRetion(ServiceModuleAssignmentRelation reltaion);

    void batchCreateServiceModuleAssignment(List<ServiceModuleAssignment> moduleAssignmentList);

    ServiceModuleAssignmentRelation findServiceModuleAssignmentRelationById(Long id);

    List<ServiceModuleAssignment> findServiceModuleAssignmentListByRelationId(Long id);

    void deleteServiceModuleAssignmentRelationById(Long id);

    void deleteServiceModuleAssignments(List<ServiceModuleAssignment> assignments);

    List<ServiceModuleAssignmentRelation> listServiceModuleAssignmentRelations(String ownerType, Long ownerId);

    ServiceModulePrivilege getServiceModulePrivilegesByModuleIdAndPrivilegeId(Long moduleId, Long privilegeId);

    void updateServiceModuleAssignmentRelation(ServiceModuleAssignmentRelation relation);

    List<ServiceModule> listServiceModule(String path);

    void createServiceModule(ServiceModule serviceModule);

    void updateServiceModule(ServiceModule serviceModule);

    void deleteServiceModuleById(Long id);

    List<ServiceModule> listServiceModule();

    List<ServiceModule> listServiceModule(Byte actionType);

    List<ServiceModule> listServiceModule(CrossShardListingLocator locator, Integer pageSize, ListingQueryBuilderCallback queryBuilderCallback);

}
