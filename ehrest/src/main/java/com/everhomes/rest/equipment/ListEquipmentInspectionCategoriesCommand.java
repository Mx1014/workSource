package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerId: 拥有者id（机构id）</li>
 * </ul>
 */
public class ListEquipmentInspectionCategoriesCommand {

	private Long ownerId;

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
