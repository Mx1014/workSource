package com.everhomes.serviceModule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everhomes.rest.acl.ListServiceModulesCommand;
import com.everhomes.rest.acl.ServiceModuleDTO;
import com.everhomes.util.ConvertHelper;

@Service
public class ServiceModuleServiceImpl implements ServiceModuleService{

	@Autowired
	private ServiceModuleProvider serviceModuleProvider;
	
	@Override
	public List<ServiceModuleDTO> listServiceModules(ListServiceModulesCommand cmd) {
		List<ServiceModule> list = serviceModuleProvider.listServiceModule(cmd.getLevel());
		List<ServiceModuleDTO> result = list.stream().map(r ->{
			ServiceModuleDTO dto = ConvertHelper.convert(r, ServiceModuleDTO.class);
			return dto;
		}).collect(Collectors.toList());
		
		result.stream().forEach(s -> {
			getChildCategories(result, s);
		});
		
		return result;
	}

	@Override
	public List<ServiceModuleDTO> listTreeServiceModules(ListServiceModulesCommand cmd) {
		
		List<ServiceModule> list = serviceModuleProvider.listServiceModule(null);
		List<ServiceModuleDTO> result = list.stream().map(r ->{
			ServiceModuleDTO dto = ConvertHelper.convert(r, ServiceModuleDTO.class);
			return dto;
		}).collect(Collectors.toList());
		
		result.stream().forEach(s -> {
			getChildCategories(result, s);
		});
		
		return result;
	}

	private ServiceModuleDTO getChildCategories(List<ServiceModuleDTO> list, ServiceModuleDTO dto){
		
		List<ServiceModuleDTO> childrens = new ArrayList<ServiceModuleDTO>();
		
		for (ServiceModuleDTO serviceModuleDTO : list) {
			if(dto.getId().equals(serviceModuleDTO.getParentId())){
				childrens.add(getChildCategories(list, serviceModuleDTO));
			}
		}
		dto.setServiceModules(childrens);
		
		return dto;
	}
}
