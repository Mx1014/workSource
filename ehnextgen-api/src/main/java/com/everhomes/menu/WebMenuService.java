package com.everhomes.menu;

import com.everhomes.rest.acl.admin.ListWebMenuResponse;

public interface WebMenuService {

	/**
	 * 获取左邻管理后台菜单
	 * @return
	 */
	ListWebMenuResponse listZuolinAdminWebMenu();
	
}