package com.everhomes.module;

import java.util.List;

import com.everhomes.rest.acl.ListServiceModulePrivilegesCommand;
import com.everhomes.rest.acl.ListServiceModulesCommand;
import com.everhomes.rest.acl.ServiceModuleAssignmentRelationDTO;
import com.everhomes.rest.acl.ServiceModuleDTO;
import com.everhomes.rest.module.*;

public interface ServiceModuleService {
	List<ServiceModuleDTO> listServiceModules(ListServiceModulesCommand cmd);

	List<ServiceModuleDTO> listTreeServiceModules(ListServiceModulesCommand cmd);

	List<ServiceModuleDTO>  listServiceModulePrivileges(ListServiceModulePrivilegesCommand cmd);

	void assignmentServiceModule(AssignmentServiceModuleCommand cmd);
	
	void deleteServiceModuleAssignmentRelation(DeleteServiceModuleAssignmentRelationCommand cmd);
	
	List<ServiceModuleAssignmentRelationDTO> listServiceModuleAssignmentRelations(ListServiceModuleAssignmentRelationsCommand cmd);

	List<ServiceModuleDTO> treeServiceModules(TreeServiceModuleCommand cmd);

	ServiceModuleDTO getServiceModule(GetServiceModuleCommand cmd);
}
