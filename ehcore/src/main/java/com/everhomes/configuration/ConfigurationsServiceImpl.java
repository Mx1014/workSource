package com.everhomes.configuration;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.configuration.admin.CreateConfigurationCommand;
import com.everhomes.rest.configuration.admin.CreateConfigurationResponse;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class ConfigurationsServiceImpl implements ConfigurationsService{

	@Autowired
	private ConfigurationsProvider configurationsProvider;
	
	@Override
	public CreateConfigurationResponse createConfiguration(CreateConfigurationCommand cmd) {
		if(StringUtils.isEmpty(cmd.getName()) || StringUtils.isEmpty(cmd.getValue()) || cmd.getNamespaceId() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters: "+cmd);
		}
		Configurations configurations = ConvertHelper.convert(cmd, Configurations.class);
		configurationsProvider.createConfiguration(configurations);
		
		return ConvertHelper.convert(configurations, CreateConfigurationResponse.class);
	}

}
