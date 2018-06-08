package com.everhomes.rest.welfare;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li>id: id</li> 
 * <li>itemType: 福利项类型 {@link com.everhomes.rest.welfare.WelfareItemType}</li>
 * <li>itemSingleQuantity: 福利项单个数量</li> 
 * </ul>
 */
public class WelfareItemDTO {
	private Long id; 
    private Byte itemType;
    private String itemSingleQuantity;
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Byte getItemType() {
		return itemType;
	}
	public void setItemType(Byte itemType) {
		this.itemType = itemType;
	}
	public String getItemSingleQuantity() {
		return itemSingleQuantity;
	}
	public void setItemSingleQuantity(String itemSingleQuantity) {
		this.itemSingleQuantity = itemSingleQuantity;
	}
	
}
