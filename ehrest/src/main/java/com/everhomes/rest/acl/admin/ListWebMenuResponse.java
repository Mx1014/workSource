package com.everhomes.rest.acl.admin;


import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.acl.WebMenuDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>menus: 菜单集合，参考{@link com.everhomes.rest.acl.WebMenuDTO}</li>
 * </ul>
 */
public class ListWebMenuResponse {
	
	@ItemType(WebMenuDTO.class)
	private List<WebMenuDTO> menus;
	
	


	public List<WebMenuDTO> getMenus() {
		return menus;
	}




	public void setMenus(List<WebMenuDTO> menus) {
		this.menus = menus;
	}




	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
