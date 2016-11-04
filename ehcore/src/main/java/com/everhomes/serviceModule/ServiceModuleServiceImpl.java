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

	@Override
	public List<ServiceModuleDTO> listTreeServiceModules(ListServiceModulesCommand cmd) {
		
		List<ServiceModule> list = serviceModuleProvider.listServiceModule(null);
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

	private ServiceModuleDTO getChildServiceModules(List<ServiceModuleDTO> list, ServiceModuleDTO dto){
		
		List<ServiceModuleDTO> childrens = new ArrayList<ServiceModuleDTO>();
		
		for (ServiceModuleDTO serviceModuleDTO : list) {
			if(dto.getId().equals(serviceModuleDTO.getParentId())){
				childrens.add(getChildServiceModules(list, serviceModuleDTO));
			}
		}
		dto.setServiceModules(childrens);
		
		return dto;
	}
}
