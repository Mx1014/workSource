package com.everhomes.rest.techpark.rental.admin;

import java.util.List;

import com.everhomes.rest.techpark.rental.SiteItemDTO;
import com.everhomes.util.StringHelper;

public class UpdateItemsAdminCommand {
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
