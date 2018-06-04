package com.everhomes.developer_account_info;

/**
 * 
 * @author huanglm 20180604
 *
 */
public interface DeveloperAccountInfoProvider {
	
	/**
	 * 通过 bundleId 查询开发者信息表中的信息
	 * @param bundleId
	 * @return
	 */
	DeveloperAccountInfo getDeveloperAccountInfoByBundleId(String  bundleId);

}
