// @formatter:off
package com.everhomes.acl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.acl.WebMenuPrivilegeShowFlag;


public interface WebMenuPrivilegeProvider {
	
	List<WebMenu> listWebMenuByType(String type);

	List<WebMenu> listWebMenuByType(String type, List<String> categories, String path, List<Long> moduleIds);
	
	List<WebMenuPrivilege> listWebMenuByPrivilegeIds(List<Long> privilegeIds,WebMenuPrivilegeShowFlag showFlag);
	
	List<WebMenu> listWebMenuByMenuIds(List<Long> menuIds);
	
	/**
	 * 处理范围内的菜单
	 * @param ownerType
	 * @param ownerId
	 * @return
	 */
	List<WebMenuScope> listWebMenuScopeByOwnerId(String ownerType, Long ownerId);

	List<WebMenuPrivilege> listWebMenuPrivilegeByMenuId(Long menuId);

	//add by Janson
	Long createWebMenu(WebMenu obj);

	//add by Janson
	void updateWebMenu(WebMenu obj);

	//add by Janson
	void deleteWebMenu(WebMenu obj);

	//add by Janson
	WebMenu getWebMenuById(Long id);

	//add by Janson
	List<WebMenu> queryWebMenus(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	Long nextId();

	void createWebMenus(List<WebMenu> objs);

	List<WebMenuScope> getWebMenuScopeMapByOwnerId(String ownerType, Long ownerId);

	List<WebMenu> listWebMenus(Long parentId, String type, Byte sceneType);

	List<WebMenu> listWebMenusByPath(String path, List<String> types);

	void deleteWebMenuScopes(List<Long> ids);

	void createWebMenuScopes(List<WebMenuScope> scopes);

	void deleteWebMenuScopesByMenuIdAndNamespace(List<Integer> socpeIds, Integer namespaceId);

    List<WebMenu> listMenuByModuleIdAndType(Long moduleId, String type);

    List<WebMenu> listMenuByTypeAndConfigType(String type, Byte configType);

    void deleteMenuScopeByOwner(String ownerType, Long ownerId);
}
