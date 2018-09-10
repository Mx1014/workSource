package com.everhomes.appurl;

import java.util.List;


public interface AppUrlProvider {
	 AppUrls findByNamespaceIdAndOSType(Integer namespaceId, Byte osType);
	 
		
	/**
	 * 新增appURL信息方法
	 * @param bo
	 */
	void  createAppInfo( AppUrls bo);
		
	/**
	* 更新appURL信息方法
	* @param bo
	*/
	void  updateAppInfo( AppUrls bo);
	
	/**
	 * 通过namespaceId 查询信息
	 * @param namespaceId
	 * @return
	 */
    List<AppUrls> findByNamespaceId(Integer namespaceId);

}
