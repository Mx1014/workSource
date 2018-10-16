// @formatter:off
package com.everhomes.module;

import java.util.List;
import java.util.Map;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.acl.ServiceModuleDTO;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
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
    List<ServiceModuleDTO> listServiceModuleDtos(List<Long> ids);

    Long createModuleAssignmentRetion(ServiceModuleAssignmentRelation reltaion);

    void batchCreateServiceModuleAssignment(List<ServiceModuleAssignment> moduleAssignmentList);

    ServiceModuleAssignmentRelation findServiceModuleAssignmentRelationById(Long id);

    List<ServiceModuleAssignment> findServiceModuleAssignmentListByRelationId(Long id);

    void deleteServiceModuleAssignmentRelationById(Long id);

    void deleteServiceModuleAssignments(List<ServiceModuleAssignment> assignments);

    List<ServiceModuleAssignmentRelation> listServiceModuleAssignmentRelations(String ownerType, Long ownerId);

    ServiceModulePrivilege getServiceModulePrivilegesByModuleIdAndPrivilegeId(Long moduleId, Long privilegeId);

    void updateServiceModuleAssignmentRelation(ServiceModuleAssignmentRelation relation);

    List<ServiceModule> listServiceModulesByMenuAuthFlag(Byte menuAuthFlag);

    List<ServiceModule> listServiceModule(String path);

    void createServiceModule(ServiceModule serviceModule);

    void updateServiceModule(ServiceModule serviceModule);

    void deleteServiceModuleById(Long id);

    List<ServiceModule> listServiceModule();

    List<ServiceModule> listServiceModule(Byte actionType);

    List<ServiceModule> listServiceModule(CrossShardListingLocator locator, Integer pageSize, ListingQueryBuilderCallback queryBuilderCallback);

    void createReflectionServiceModuleApp(ReflectionServiceModuleApp reflectionServiceModuleApp);

    void updateReflectionServiceModuleApp(ReflectionServiceModuleApp reflectionServiceModuleApp);

    ReflectionServiceModuleApp findReflectionServiceModuleAppById(Long id);

    ReflectionServiceModuleApp findReflectionServiceModuleAppByParam(Integer namespaceId, Long moduleId, String custom_tag);

    Long getMaxActiveAppId();

    void lapseReflectionServiceModuleAppByNamespaceId(Integer namespaceId);

    List<ServiceModuleAppDTO> listReflectionServiceModuleAppsByModuleIds(Integer namespaceId, List<Long> moduleIds);

    List<ServiceModuleAppDTO> listReflectionServiceModuleApp(Integer namespaceId, Long moduleId, Byte actionType, String customTag, String customPath, String controlOption);

    ServiceModuleApp findReflectionServiceModuleAppByActiveAppId(Long id);

    ServiceModuleApp findReflectionServiceModuleAppByMenuId(Long id);

    List<ServiceModuleAppDTO> listReflectionServiceModuleAppByActiveAppIds(Integer namespaceId, List<Long> appIds);

    Map<Long, ServiceModuleApp> listReflectionAcitveAppIdByNamespaceId(Integer namespaceId);

    List<ServiceModuleFunction> listFunctions(Long moduleId, List<Long> privilegeIds);
    List<ServiceModuleFunction> listFunctionsByIds(List<Long> ids);
    List<ServiceModuleExcludeFunction> listExcludeFunctions(Integer namespaceId, Long comunityId, Long moduleId);
    List<ServiceModuleIncludeFunction> listIncludeFunctions(Integer namespaceId, Long comunityId, Long moduleId);

	void deleteServiceModuleFromBlack(Long Id);

	Long getServiceModuleFromBlackId(Integer namespaceId, Long moduleId);

	void createServiceModuleExcludeFunction(ServiceModuleExcludeFunction serviceModuleExcludeFunction);

	List<Long> listExcludeCauseWhiteList();
}
