package com.everhomes.rest.rentalv2;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>itemId：物品id</li> 
 * </ul>
 */
public class DeleteItemAdminCommand { 
	@NotNull
	private Long itemId; 
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}   
}
