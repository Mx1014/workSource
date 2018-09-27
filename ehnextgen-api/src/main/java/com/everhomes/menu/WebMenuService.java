package com.everhomes.menu;

import com.everhomes.acl.WebMenu;
import com.everhomes.rest.acl.WebMenuDTO;
import com.everhomes.rest.acl.admin.ListWebMenuResponse;
import com.everhomes.rest.menu.*;
import com.everhomes.rest.module.AppCategoryDTO;

import java.util.List;

public interface WebMenuService {

	List<WebMenuDTO> listUserRelatedWebMenus(ListUserRelatedWebMenusCommand cmd);

	List<AppCategoryDTO>  listUserAppCategory(ListUserAppCategoryCommand cmd);

	/**
	 * 获取左邻管理后台菜单
	 * @return
	 */
	ListWebMenuResponse listZuolinAdminWebMenu();

	GetTreeWebMenusByNamespaceResponse getTreeWebMenusByNamespace(GetTreeWebMenusByNamespaceCommand cmd);

    void updateMenuScopesByNamespace(UpdateMenuScopesByNamespaceCommand cmd);

    void refleshMenuByPortalVersion(Long versionId);

    ListMenuForPcEntryResponse listMenuForPcEntry(ListMenuForPcEntryCommand cmd);

    ListMenuForPcEntryServicesResponse listMenuForPcEntryServices(ListMenuForPcEntryServicesCommand cmd);
}