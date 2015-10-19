package com.everhomes.techpark.rental;

import com.everhomes.util.StringHelper;
/**
 * <ul>
*
 * <li>itemName：商品名称</li>
 * <li>itemPrice：商品价格</li>
 * <li>counts：商品数量</li> 
 * </ul>
 */
public class SiteItemDTO {
	private String itemName;
	private Integer itemPrice;
	private Integer counts;
	
	
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }



	public String getItemName() {
		return itemName;
	}



	public void setItemName(String itemName) {
		this.itemName = itemName;
	}



	public Integer getItemPrice() {
		return itemPrice;
	}



	public void setItemPrice(Integer itemPrice) {
		this.itemPrice = itemPrice;
	}



	public Integer getCounts() {
		return counts;
	}



	public void setCounts(Integer counts) {
		this.counts = counts;
	}
}
