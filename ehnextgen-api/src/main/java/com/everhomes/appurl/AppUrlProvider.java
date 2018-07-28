package com.everhomes.appurl;


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

}
