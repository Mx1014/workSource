package com.everhomes.configurations;

import com.everhomes.rest.configurations.admin.ConfigurationsAdminCommand;
import com.everhomes.rest.configurations.admin.ConfigurationsAdminDTO;
import com.everhomes.rest.configurations.admin.ConfigurationsCreateAdminCommand;
import com.everhomes.rest.configurations.admin.ConfigurationsIdAdminCommand;
import com.everhomes.rest.configurations.admin.ConfigurationsIdAdminDTO;
import com.everhomes.rest.configurations.admin.ConfigurationsUpdateAdminCommand;

/**
 * 
 * @author huanglm
 *
 */
public interface ConfigurationsService {
	
	/**
	 * 通过传参查询配置项信息
	 * @param cmd
	 * @return ConfigurationsAdminDTO
	 */
	ConfigurationsAdminDTO listConfigurations(ConfigurationsAdminCommand cmd);
	
	/**
	 * 通过主键查询配置项信息，主键不能为空
	 * @param cmd
	 * @return ConfigurationsIdAdminDTO
	 */ 
	ConfigurationsIdAdminDTO getConfigurationById(ConfigurationsIdAdminCommand cmd);
	
	/**
	 * 创建配置项信息
	 * @param cmd
	 */
	void crteateConfiguration(ConfigurationsCreateAdminCommand cmd);
	
	/**
	 * 修改配置项信息，主键不能为空
	 * @param cmd
	 */
	void updateConfiguration(ConfigurationsUpdateAdminCommand cmd);
	
	/**
	 * 删除配置项信息，主键不能为空
	 * @param cmd
	 */
	void deleteConfiguration(ConfigurationsIdAdminCommand cmd);
	

}
