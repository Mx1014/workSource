package com.everhomes.menu;

import com.everhomes.rest.acl.WebMenuDTO;
import com.everhomes.rest.acl.admin.ListWebMenuResponse;
import com.everhomes.rest.menu.ListUserRelatedWebMenusCommand;

import java.util.List;

public interface WebMenuService {

	List<WebMenuDTO> listUserRelatedWebMenus(ListUserRelatedWebMenusCommand cmd);

	/**
	 * 获取左邻管理后台菜单
	 * @return
	 */
	ListWebMenuResponse listZuolinAdminWebMenu();
	
}