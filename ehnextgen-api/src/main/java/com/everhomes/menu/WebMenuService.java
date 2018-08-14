package com.everhomes.menu;

import com.everhomes.acl.WebMenu;
import com.everhomes.rest.acl.WebMenuDTO;
import com.everhomes.rest.acl.admin.ListWebMenuResponse;
import com.everhomes.rest.menu.*;

import java.util.List;

public interface WebMenuService {

	List<WebMenuDTO> listUserRelatedWebMenus(ListUserRelatedWebMenusCommand cmd);

    List<Long> listUserAppIds(Long userId, WebMenu menu, Long organizationId);

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