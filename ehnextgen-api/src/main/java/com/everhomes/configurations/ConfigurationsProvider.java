package com.everhomes.configurations;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;


/**
 * 
 * @author huanglm
 *
 */
public interface ConfigurationsProvider {
	
	/**
	 * 通过传参查询配置项信息
	 * @param namespaceId 域空间ID
	 * @param name 配置项名称
	 * @param value 配置项值
	 * @param pageSize 页数据量
	 * @param pageAnchor 本页开始的锚点
	 * @return Configurations
	 */
	List<Configurations> listConfigurations(Integer namespaceId,String name,
								String value,Integer  pageSize,CrossShardListingLocator locator,Boolean likeSearch);
	
	/**
	 * 通过主键查询配置项信息，主键不能为空 
	 * @param id 主键ID
	 * @param namespaceId 域空间ID
	 * @return
	 */
	Configurations getConfigurationById(Integer id,Integer namespaceId);
	
	/**
	 * 创建配置项信息
	 * @param bo	Configurations
	 */
	void crteateConfiguration(Configurations bo);
	
	/**
	 * 修改配置项信息，主键不能为空
	 * @param bo	Configurations
	 */
	void updateConfiguration(Configurations bo);
	
	/**
	 * 删除配置项信息，主键不能为空
	 * @param id	主键ID
	 */
	void deleteConfiguration(Configurations bo);
	

}
