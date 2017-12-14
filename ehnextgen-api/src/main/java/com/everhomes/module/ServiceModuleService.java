package com.everhomes.module;

import java.util.List;

import com.everhomes.rest.acl.*;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.module.*;
import com.everhomes.rest.portal.TreeServiceModuleAppsResponse;

public interface ServiceModuleService {
	List<ServiceModuleDTO> listServiceModules(ListServiceModulesCommand cmd);

	List<ServiceModuleDTO> listTreeServiceModules(ListServiceModulesCommand cmd);

	List<ServiceModuleDTO>  listServiceModulePrivileges(ListServiceModulePrivilegesCommand cmd);

	void assignmentServiceModule(AssignmentServiceModuleCommand cmd);
	
	void deleteServiceModuleAssignmentRelation(DeleteServiceModuleAssignmentRelationCommand cmd);
	
	List<ServiceModuleAssignmentRelationDTO> listServiceModuleAssignmentRelations(ListServiceModuleAssignmentRelationsCommand cmd);

	List<ServiceModuleDTO> treeServiceModules(TreeServiceModuleCommand cmd);

	TreeServiceModuleAppsResponse treeServiceModuleApps(TreeServiceModuleCommand cmd);

	ServiceModuleDTO getServiceModule(GetServiceModuleCommand cmd);

	List<ProjectDTO> listUserRelatedCategoryProjectByModuleId(ListUserRelatedProjectByModuleCommand cmd);

	List<ProjectDTO> listUserRelatedProjectByModuleId(ListUserRelatedProjectByModuleCommand cmd);

	List<CommunityDTO> listUserRelatedCommunityByModuleId(ListUserRelatedProjectByModuleCommand cmd);

	Byte checkModuleManage(CheckModuleManageCommand cmd);

	ServiceModuleDTO createServiceModule(CreateServiceModuleCommand cmd);

	ServiceModuleDTO updateServiceModule(UpdateServiceModuleCommand cmd);

	void deleteServiceModule(DeleteServiceModuleCommand cmd);

	ListServiceModulesResponse listAllServiceModules(ListServiceModulesCommand cmd);
	List<ServiceModuleDTO> filterByScopes(int namespaceId, String ownerType, Long ownerId);

	ReflectionServiceModuleApp getOrCreateReflectionServiceModuleApp(Integer namespaceId, String actionData, String instanceConfig, String itemLabel, ServiceModule serviceModule);

	List<Long> listServiceModulefunctions(ListServiceModulefunctionsCommand cmd);
}
