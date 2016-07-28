package com.everhomes.rest.rentalv2.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.rentalv2.SiteItemDTO;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>itemDTOs：物品列表</li> 
 * </ul>
 */
public class UpdateItemsAdminCommand {
	@ItemType(SiteItemDTO.class)
	private List<SiteItemDTO> itemDTOs;

	public List<SiteItemDTO> getItemDTOs() {
		return itemDTOs;
	}

	public void setItemDTOs(List<SiteItemDTO> itemDTOs) {
		this.itemDTOs = itemDTOs;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
