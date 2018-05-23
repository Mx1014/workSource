package com.everhomes.configurations_record_change;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;


/**
 * 
 * @author huanglm
 *
 */
public interface ConfigurationsRecordChangeProvider {
	
	/**
	 * 创建配置项信息
	 * @param bo	Configurations
	 */
	void crteateConfiguration(ConfigurationsRecordChange bo);
	
}
