package com.everhomes.configurations;

import org.springframework.stereotype.Component;

import com.everhomes.rest.configurations.admin.ConfigurationsAdminCommand;
import com.everhomes.rest.configurations.admin.ConfigurationsAdminDTO;
import com.everhomes.rest.configurations.admin.ConfigurationsCreateAdminCommand;
import com.everhomes.rest.configurations.admin.ConfigurationsIdAdminCommand;
import com.everhomes.rest.configurations.admin.ConfigurationsIdAdminDTO;
import com.everhomes.rest.configurations.admin.ConfigurationsUpdateAdminCommand;

@Component
public class ConfigurationsServiceImpl implements  ConfigurationsService{

	@Override
	public ConfigurationsAdminDTO listConfigurations(
			ConfigurationsAdminCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConfigurationsIdAdminDTO getConfigurationById(
			ConfigurationsIdAdminCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void crteateConfiguration(ConfigurationsCreateAdminCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateConfiguration(ConfigurationsUpdateAdminCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteConfiguration(ConfigurationsIdAdminCommand cmd) {
		// TODO Auto-generated method stub
		
	}

}
