// @formatter:off
package com.everhomes.acl;

import java.util.List;

import com.everhomes.rest.acl.WebMenuPrivilegeShowFlag;


public interface WebMenuPrivilegeProvider {
	
	List<WebMenu> listWebMenuByType(String type);
	
	List<WebMenuPrivilege> listWebMenuByPrivilegeIds(List<Long> privilegeIds,WebMenuPrivilegeShowFlag showFlag);
	
	List<WebMenu> listWebMenuByMenuIds(List<Long> menuIds);
	
	/**
	 * 处理范围内的菜单
	 * @param ownerType
	 * @param ownerId
	 * @return
	 */
	List<WebMenuScope> listWebMenuScopeByOwnerId(String ownerType, Long ownerId);
	
}
