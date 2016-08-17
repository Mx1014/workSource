package com.everhomes.configuration;

import com.everhomes.rest.configuration.admin.CreateConfigurationCommand;
import com.everhomes.rest.configuration.admin.CreateConfigurationResponse;

public interface ConfigurationsService {

	CreateConfigurationResponse createConfiguration(CreateConfigurationCommand cmd);

}
