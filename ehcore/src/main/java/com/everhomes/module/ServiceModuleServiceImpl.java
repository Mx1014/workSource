package com.everhomes.module;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.everhomes.acl.AclProvider;
import com.everhomes.acl.Privilege;
import com.everhomes.rest.acl.ServiceModuleTreeVType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.entity.EntityType;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.module.ServiceModuleScope;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.rest.acl.ListServiceModulesCommand;
import com.everhomes.rest.acl.ServiceModuleDTO;
import com.everhomes.rest.module.ServiceModuleScopeApplyPolicy;
import com.everhomes.rest.module.ServiceModuleType;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Service
public class ServiceModuleServiceImpl implements ServiceModuleService{
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceModuleServiceImpl.class);

	@Autowired
	private ServiceModuleProvider serviceModuleProvider;

	@Autowired
	private AclProvider aclProvider;
	
	@Override
	public List<ServiceModuleDTO> listServiceModules(ListServiceModulesCommand cmd) {
//		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		Integer namespaceId = UserContext.getCurrentNamespaceId();

		List<ServiceModuleScope> scopes = serviceModuleProvider.listServiceModuleScopes(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), ServiceModuleScopeApplyPolicy.REVERT.getCode());

		if(null == scopes || scopes.size() == 0){
			scopes = serviceModuleProvider.listServiceModuleScopes(namespaceId, null,
					null, ServiceModuleScopeApplyPolicy.REVERT.getCode());
		}

		List<ServiceModule> list = serviceModuleProvider.listServiceModule(cmd.getLevel(), ServiceModuleType.PARK.getCode());
		if(scopes.size() != 0)
			list = filterList(list, scopes);
		
		List<ServiceModuleDTO> temp = list.stream().map(r ->{
			ServiceModuleDTO dto = ConvertHelper.convert(r, ServiceModuleDTO.class);
			return dto;
		}).collect(Collectors.toList());
//		List<ServiceModuleDTO> result = new ArrayList<ServiceModuleDTO>();
//		
//		for(ServiceModuleDTO s: temp) {
//			getChildServiceModules(temp, s);
//			if(s.getParentId() == 0) {
//				result.add(s);
//			}
//		}
		
		return temp;
	}

	@Override
	public List<ServiceModuleDTO> listTreeServiceModules(ListServiceModulesCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		
		Integer namespaceId = UserContext.current().getUser().getNamespaceId();
		List<ServiceModuleScope> scopes = serviceModuleProvider.listServiceModuleScopes(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), ServiceModuleScopeApplyPolicy.REVERT.getCode());

		if(null == scopes || scopes.size() == 0){
			scopes = serviceModuleProvider.listServiceModuleScopes(namespaceId, null,
					null, ServiceModuleScopeApplyPolicy.REVERT.getCode());
		}

		List<ServiceModule> list = serviceModuleProvider.listServiceModule(null, ServiceModuleType.PARK.getCode());
		if(scopes.size() != 0)
			list = filterList(list, scopes);
		
		List<ServiceModuleDTO> temp = list.stream().map(r ->{
			ServiceModuleDTO dto = ConvertHelper.convert(r, ServiceModuleDTO.class);
			return dto;
		}).collect(Collectors.toList());
		
		List<ServiceModuleDTO> result = new ArrayList<ServiceModuleDTO>();

		for(ServiceModuleDTO s: temp) {
			getChildServiceModules(temp, s);
			if(s.getParentId() == 0) {
				result.add(s);
			}
		}
		return result;
	}

	private List<ServiceModule> filterList(List<ServiceModule> modules, List<ServiceModuleScope> scopes) {
		List<ServiceModule> result = new ArrayList<ServiceModule>();
		outer:
		for(ServiceModule m: modules) {
			for(ServiceModuleScope s: scopes) {
				if(s.getModuleId().equals(m.getId())) {
					result.add(m);
					continue outer;
				}
			}
		}
		return result;
	}
	
	private ServiceModuleDTO getChildServiceModules(List<ServiceModuleDTO> list, ServiceModuleDTO dto){
		
		List<ServiceModuleDTO> childrens = new ArrayList<ServiceModuleDTO>();
		
		for (ServiceModuleDTO serviceModuleDTO : list) {
			serviceModuleDTO.setvType(ServiceModuleTreeVType.SERVICE_MODULE.getCode());
			if(dto.getId().equals(serviceModuleDTO.getParentId())){
				childrens.add(getChildServiceModules(list, serviceModuleDTO));
				if(0 == serviceModuleDTO.getServiceModules().size()){
					List<ServiceModulePrivilege> modulePrivileges = serviceModuleProvider.listServiceModulePrivileges(serviceModuleDTO.getId(), ServiceModulePrivilegeType.ORDINARY);
					List<ServiceModuleDTO> ps = new ArrayList<ServiceModuleDTO>();
					for (ServiceModulePrivilege modulePrivilege: modulePrivileges) {
						ServiceModuleDTO p = new ServiceModuleDTO();
						p.setId(modulePrivilege.getId());
						Privilege privilege = aclProvider.getPrivilegeById(modulePrivilege.getId());
						if(null != privilege){
							p.setName(privilege.getName());
						}
						p.setvType(ServiceModuleTreeVType.PRIVILEGE.getCode());
						ps.add(p);
					}
					serviceModuleDTO.setServiceModules(ps);
				}
			}
		}
		dto.setServiceModules(childrens);
		
		return dto;
	}
	
	private void checkOwnerIdAndOwnerType(String ownerType, Long ownerId){
		if(null == ownerId) {
        	LOGGER.error("OwnerId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OwnerId cannot be null.");
        }
    	
    	if(StringUtils.isBlank(ownerType)) {
        	LOGGER.error("OwnerType cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OwnerType cannot be null.");
        }
	}
}
