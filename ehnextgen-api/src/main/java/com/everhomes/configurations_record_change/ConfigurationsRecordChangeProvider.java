package com.everhomes.configurations_record_change;



/**
 * 
 * @author huanglm
 *
 */
public interface ConfigurationsRecordChangeProvider {
	
	/**
	 * 创建配置项变更信息
	 * @param bo	Configurations
	 */
	void crteateConfiguration(ConfigurationsRecordChange bo);
	
}
