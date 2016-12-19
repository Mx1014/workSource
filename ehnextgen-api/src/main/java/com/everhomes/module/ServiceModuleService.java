package com.everhomes.module;

import java.util.List;

import com.everhomes.rest.acl.ListServiceModulesCommand;
import com.everhomes.rest.acl.ServiceModuleDTO;

public interface ServiceModuleService {
	List<ServiceModuleDTO> listServiceModules(ListServiceModulesCommand cmd);
	List<ServiceModuleDTO> listTreeServiceModules(ListServiceModulesCommand cmd);
}
